package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdDTOForGet;
import ru.skypro.homework.dto.AdDTOForGetAll;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.service.impl.AdServiceImpl;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdServiceImpl adServiceImpl;


    @GetMapping("{id}")
    public AdDTOForGet getAd(@PathVariable Long id) {
        return adServiceImpl.getAd(id);
    }

    @GetMapping("/me")
    public AdDTOForGetAll getAllAdsUser() {
        return adServiceImpl.getAllAdsUser();
    }

    @GetMapping()
    public AdDTOForGetAll getAllAds() {
        return adServiceImpl.getAllAds();
    }

    @DeleteMapping("{id}")
    public void deleteAd(@PathVariable long id) {
        adServiceImpl.deleteAd(id);
    }

    @PostMapping(consumes = "multipart/form-data")
    public CreateOrUpdateAdDTO createAd(@RequestPart("properties") CreateOrUpdateAdDTO createOrUpdateAdDTO,
                                        @RequestPart("image") MultipartFile image) {
        return adServiceImpl.createAd(createOrUpdateAdDTO, image);
    }


    @PatchMapping("{id}")
    public AdDTO updateAd(@PathVariable long id,
                          @RequestBody CreateOrUpdateAdDTO createOrUpdateAdDTO) {
        return adServiceImpl.updateAd(id, createOrUpdateAdDTO);
    }

    @PatchMapping(value = "{id}/image", consumes = "multipart/form-data")
    public void updateAdImage(@PathVariable long id,
                              @RequestPart("image") MultipartFile image) {
        adServiceImpl.updateAdImage(id, image);
    }
}
