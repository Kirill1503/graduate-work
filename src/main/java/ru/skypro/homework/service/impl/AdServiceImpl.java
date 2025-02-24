package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    @Value("${ads.dir.path}")
    private static String ADS_PATH;

    private final AdRepository adRepository;
    private final MappingAdDTO mappingAdDTO;
    private final UserRepository userRepository;

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
            String imagePath = saveImage(image);
            ad.setImage(imagePath);
        }

        Ad savedAd = adRepository.save(ad);
        return mappingAdDTO.mapToAdDTO(savedAd);
    }

    @Override
    public AdDTO updateAd(long id, CreateOrUpdateAdDTO createOrUpdateAdDTO) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFound("Ad not found"));

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

        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            ad.setImage(imagePath);
            adRepository.save(ad);
        }
    }

    private String saveImage(MultipartFile image) {
        try {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(ADS_PATH, fileName);
            Files.write(filePath, image.getBytes());
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private User getAuthenticatedUser() {
        return Optional.ofNullable(SecurityUtils.getCurrentUsername())
                .map(userRepository::findUserByUsername)
                .orElseThrow(() -> new TheUserIsNotAuthenticated("The user is not authenticated"));
    }
}