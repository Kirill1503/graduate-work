package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdDTOForGet;
import ru.skypro.homework.dto.AdDTOForGetAll;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;

/**
 * Сервис для управления объявлениями.
 */
public interface AdService {
    /**
     * Получает объявление по его идентификатору.
     * @param id идентификатор объявления
     * @return DTO с подробной информацией об объявлении
     */
    AdDTOForGet getAd(long id);

    /**
     * Получает все объявления текущего пользователя.
     * @return DTO со списком объявлений пользователя
     */
    AdDTOForGetAll getAllAdsUser();

    /**
     * Получает все объявления.
     * @return DTO со списком всех объявлений
     */
    AdDTOForGetAll getAllAds();

    /**
     * Удаляет объявление по его идентификатору.
     * @param id идентификатор объявления
     */
    void deleteAd(long id);

    /**
     * Обновляет объявление.
     * @param id идентификатор объявления
     * @param adDTO данные для обновления
     * @return обновленное DTO объявления
     */
    AdDTO updateAd(long id, CreateOrUpdateAdDTO adDTO);

    /**
     * Создает новое объявление.
     * @param adDTO данные для создания
     * @param image изображение объявления
     * @return DTO созданного объявления
     */
    AdDTO createAd(CreateOrUpdateAdDTO adDTO, MultipartFile image);

    /**
     * Обновляет изображение объявления.
     * @param id идентификатор объявления
     * @param image новое изображение
     */
    void updateAdImage(long id, MultipartFile image);
}