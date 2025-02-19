package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdDTOForGet;
import ru.skypro.homework.dto.AdDTOForGetAll;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;

public interface AdService {
    AdDTOForGet getAd(long id);

    AdDTOForGetAll getAllAdsUser();

    AdDTOForGetAll getAllAds();

    void deleteAd(long id);

    AdDTO updateAd(long id, CreateOrUpdateAdDTO adDTO);

    AdDTO createAd(CreateOrUpdateAdDTO adDTO, MultipartFile image);

    void updateAdImage(long id, MultipartFile image);
}
