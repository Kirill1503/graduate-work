package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdDTOForGet;
import ru.skypro.homework.dto.AdDTOForGetAll;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;

public interface AdService {
    AdDTOForGet getAd(Long id);

    AdDTOForGetAll getAllAdsUser();

    AdDTOForGetAll getAllAds();

    void deleteAd(Long id);

    AdDTO updateAd(Long id, CreateOrUpdateAdDTO adDTO);

    AdDTO createAd(CreateOrUpdateAdDTO adDTO, MultipartFile image);

    void updateAdImage(Long id, MultipartFile image);
}