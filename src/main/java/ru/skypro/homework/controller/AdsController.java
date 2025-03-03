package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdDTOForGet;
import ru.skypro.homework.dto.AdDTOForGetAll;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.service.AdService;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    private final AdService adService;

    @GetMapping("{id}")
    public AdDTOForGet getAd(@PathVariable Long id) {
        return adService.getAd(id);
    }

    @GetMapping("/me")
    public AdDTOForGetAll getAllAdsUser() {
        return adService.getAllAdsUser();
    }

    @GetMapping()
    public AdDTOForGetAll getAllAds() {
        return adService.getAllAds();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
    }

    @PostMapping(consumes = "multipart/form-data")
    public AdDTO createAd(@RequestPart("properties") CreateOrUpdateAdDTO createOrUpdateAdDTO,
                          @RequestPart("image") MultipartFile image) {
        return adService.createAd(createOrUpdateAdDTO, image);
    }

    @PatchMapping("{id}")
    public AdDTO updateAd(@PathVariable Long id,
                          @RequestBody CreateOrUpdateAdDTO createOrUpdateAdDTO) {
        return adService.updateAd(id, createOrUpdateAdDTO);
    }

    @PatchMapping(value = "{id}/image", consumes = "multipart/form-data")
    public void updateAdImage(@PathVariable Long id,
                              @RequestPart("image") MultipartFile image) {
        adService.updateAdImage(id, image);
    }
}