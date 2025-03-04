package ru.skypro.homework.utils;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdDTOForGet;
import ru.skypro.homework.model.Ad;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MappingAdDTO {

    public AdDTOForGet mapToAdDTOForGet(Ad ad) {
        return new AdDTOForGet(
                ad.getId(),
                ad.getAuthor().getFirstName(),
                ad.getAuthor().getLastName(),
                ad.getDescription(),
                ad.getAuthor().getEmail(),
                ad.getImage(),
                ad.getAuthor().getPhone(),
                ad.getPrice(),
                ad.getTitle()
        );
    }

    public AdDTO mapToAdDTO(Ad ad) {
        return new AdDTO(
                ad.getAuthor(),
                ad.getImage(),
                ad.getId(),
                ad.getPrice(),
                ad.getTitle()
        );
    }

    public List<AdDTO> toDTOList(List<Ad> ads) {
        return ads.stream()
                .map(this::mapToAdDTO)
                .collect(Collectors.toList());
    }
}
