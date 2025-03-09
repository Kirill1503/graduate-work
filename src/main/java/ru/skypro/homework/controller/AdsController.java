package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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

    @Operation(summary = "Получение объявления по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявление найдено"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @GetMapping("{id}")
    public AdDTOForGet getAd(@PathVariable Long id) {
        return adService.getAd(id);
    }

    @Operation(summary = "Получение всех объявлений авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список объявлений получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @GetMapping("/me")
    public AdDTOForGetAll getAllAdsUser(Authentication authentication) {
        // Логика определения текущего пользователя выполняется в сервисном слое
        return adService.getAllAdsUser();
    }

    @Operation(summary = "Получение всех объявлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список объявлений получен")
    })
    @GetMapping
    public AdDTOForGetAll getAllAds() {
        return adService.getAllAds();
    }

    @Operation(summary = "Создание нового объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Объявление успешно создано"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data")
    public AdDTO createAd(
            @RequestPart("properties") CreateOrUpdateAdDTO createOrUpdateAdDTO,
            @RequestPart("image") MultipartFile image,
            Authentication authentication) {
        // Автор определяется внутри сервиса через SecurityUtils
        return adService.createAd(createOrUpdateAdDTO, image);
    }

    @Operation(summary = "Обновление объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявление успешно обновлено"),
            @ApiResponse(responseCode = "403", description = "Нет доступа для редактирования"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @PatchMapping("{id}")
    public AdDTO updateAd(@PathVariable Long id,
                          @RequestBody CreateOrUpdateAdDTO createOrUpdateAdDTO,
                          Authentication authentication) {
        return adService.updateAd(id, createOrUpdateAdDTO);
    }

    @Operation(summary = "Удаление объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Объявление успешно удалено"),
            @ApiResponse(responseCode = "403", description = "Нет доступа для удаления"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @DeleteMapping("{id}")
    public void deleteAd(@PathVariable Long id, Authentication authentication) {
        adService.deleteAd(id);
    }

    @Operation(summary = "Обновление изображения объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Картинка успешно обновлена"),
            @ApiResponse(responseCode = "403", description = "Нет доступа для обновления изображения"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @PatchMapping(value = "{id}/image", consumes = "multipart/form-data")
    public void updateAdImage(@PathVariable Long id,
                              @RequestPart("image") MultipartFile image,
                              Authentication authentication) {
        adService.updateAdImage(id, image);
    }
}
