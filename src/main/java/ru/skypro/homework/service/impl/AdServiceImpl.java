package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdDTOForGet;
import ru.skypro.homework.dto.AdDTOForGetAll;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.exception.AdNotFound;
import ru.skypro.homework.exception.TheUserIsNotAuthenticated;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.utils.MappingAdDTO;
import ru.skypro.homework.utils.SecurityUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class AdServiceImpl implements AdService {

    private final String ADS_PATH;
    private final AdRepository adRepository;
    private final MappingAdDTO mappingAdDTO;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public AdServiceImpl(@Value("${ads.dir.path}") String adsPath,
                         AdRepository adRepository,
                         MappingAdDTO mappingAdDTO,
                         UserRepository userRepository,
                         SecurityUtils securityUtils) {
        this.ADS_PATH = adsPath;
        this.adRepository = adRepository;
        this.mappingAdDTO = mappingAdDTO;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    public AdDTOForGet getAd(long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFound("Ad not found"));
        return mappingAdDTO.mapToAdDTOForGet(ad);
    }

    @Override
    public AdDTOForGetAll getAllAdsUser() {
        User user = getAuthenticatedUser();
        List<Ad> ads = adRepository.findByAuthor_Id(user.getId());
        List<AdDTO> adDTOList = mappingAdDTO.toDTOList(ads);
        return new AdDTOForGetAll(adDTOList.size(), adDTOList);
    }

    @Override
    public AdDTOForGetAll getAllAds() {
        List<Ad> ads = adRepository.findAll();
        List<AdDTO> adDTOList = mappingAdDTO.toDTOList(ads);
        return new AdDTOForGetAll(ads.size(), adDTOList);
    }

    @Override
    public void deleteAd(long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFound("Ad not found"));
        User currentUser = getAuthenticatedUser();
        // Разрешаем удаление либо владельцу объявления, либо администратору
        if (!ad.getAuthor().getUsername().equals(currentUser.getUsername())
                && !currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("You can only delete your own ads");
        }
        adRepository.delete(ad);
    }

    @Override
    public AdDTO createAd(CreateOrUpdateAdDTO createOrUpdateAdDTO, MultipartFile image) {
        User user = getAuthenticatedUser();
        Ad ad = new Ad();
        ad.setTitle(createOrUpdateAdDTO.getTitle());
        ad.setPrice(createOrUpdateAdDTO.getPrice());
        ad.setDescription(createOrUpdateAdDTO.getDescription());
        ad.setAuthor(user);
        if (image != null && !image.isEmpty()) {
            String imageName = saveImage(image);
            ad.setImage(imageName);
        }
        Ad savedAd = adRepository.save(ad);
        return mappingAdDTO.mapToAdDTO(savedAd);
    }

    @Override
    public AdDTO updateAd(long id, CreateOrUpdateAdDTO createOrUpdateAdDTO) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFound("Ad not found"));
        User currentUser = getAuthenticatedUser();
        if (!ad.getAuthor().getUsername().equals(currentUser.getUsername())
                && !currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("You can only update your own ads");
        }
        ad.setTitle(createOrUpdateAdDTO.getTitle());
        ad.setPrice(createOrUpdateAdDTO.getPrice());
        ad.setDescription(createOrUpdateAdDTO.getDescription());
        Ad updatedAd = adRepository.save(ad);
        return mappingAdDTO.mapToAdDTO(updatedAd);
    }

    @Override
    public void updateAdImage(long id, MultipartFile image) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFound("Ad not found"));
        User currentUser = getAuthenticatedUser();
        if (!ad.getAuthor().getUsername().equals(currentUser.getUsername())
                && !currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("You can only update your own ad images");
        }
        if (image != null && !image.isEmpty()) {
            String imageName = saveImage(image);
            ad.setImage(imageName);
            adRepository.save(ad);
        }
    }

    private String saveImage(MultipartFile image) {
        try {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(ADS_PATH, fileName);
            Files.write(filePath, image.getBytes());
            return fileName; // Возвращаем имя файла для формирования URL
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private User getAuthenticatedUser() {
        String username = securityUtils.getCurrentUsername();
        return Optional.ofNullable(username)
                .map(userRepository::findUserByUsername)
                .orElseThrow(() -> new TheUserIsNotAuthenticated("The user is not authenticated"));
    }
}
