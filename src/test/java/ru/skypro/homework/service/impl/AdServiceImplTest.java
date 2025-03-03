package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTOForGet;
import ru.skypro.homework.dto.AdDTOForGetAll;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.AdNotFound;
import ru.skypro.homework.exception.TheUserIsNotAuthenticated;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.utils.MappingAdDTO;
import ru.skypro.homework.utils.SecurityUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

    @Mock
    private AdRepository adRepository;
    @Mock
    private MappingAdDTO mappingAdDTO;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private AdServiceImpl adService;

    @TempDir
    private Path tempDir;

    private User user;
    private Ad ad;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPhone("1234567890");

        ad = new Ad();
        ad.setId(1L);
        ad.setDescription("description");
        ad.setTitle("title");
        ad.setImage(tempDir.toString());
        ad.setPrice(1000);
        ad.setAuthor(user);

        setAdsPathForService(tempDir.toString());
    }

    private void setAdsPathForService(String path) {
        try {
            var field = AdServiceImpl.class.getDeclaredField("ADS_PATH");
            field.setAccessible(true);
            field.set(adService, path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        user = null;
        ad = null;
    }

    @Test
    void getAdPositiveTest() {
        when(adRepository.findById(1L)).thenReturn(Optional.of(ad));
        AdDTOForGet adDTO = mappingAdDTO.mapToAdDTOForGet(ad);
        when(mappingAdDTO.mapToAdDTOForGet(ad)).thenReturn(adDTO);

        assertThat(adService.getAd(ad.getId())).isEqualTo(adDTO);
    }

    @Test
    void getAdNegativeTest() {
        when(adRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(AdNotFound.class)
                .isThrownBy(() -> adService.getAd(ad.getId()));
    }

    @Test
    void getAllAdsUserPositiveTest() {
        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(userRepository.findUserByUsername("username")).thenReturn(user);
        List<Ad> testList = List.of(ad);
        when(adRepository.findByAuthor_Id(user.getId())).thenReturn(testList);
        // Создаём dummy-объект типа AdDTOForGet
        AdDTOForGet dummyDto = new AdDTOForGet(
                ad.getId(), user.getFirstName(), user.getLastName(),
                ad.getDescription(), user.getEmail(), ad.getImage(),
                user.getPhone(), ad.getPrice(), ad.getTitle()
        );
        List<AdDTOForGet> adsDTOTest = List.of(dummyDto);
        when(mappingAdDTO.toDTOList(testList)).thenReturn(adsDTOTest);
        AdDTOForGetAll expected = new AdDTOForGetAll(adsDTOTest.size(), adsDTOTest);
        assertThat(adService.getAllAdsUser()).isEqualTo(expected);
    }

    @Test
    void getAllAdsUserNegativeTest() {
        when(securityUtils.getCurrentUsername()).thenReturn(null);

        assertThatExceptionOfType(TheUserIsNotAuthenticated.class)
                .isThrownBy(() -> adService.getAllAdsUser());
    }

    @Test
    void getAllAds() {
        List<Ad> testList = List.of(ad);
        AdDTOForGet dummyDto = new AdDTOForGet(
                ad.getId(), user.getFirstName(), user.getLastName(),
                ad.getDescription(), user.getEmail(), ad.getImage(),
                user.getPhone(), ad.getPrice(), ad.getTitle()
        );
        List<AdDTOForGet> adsDTOTest = List.of(dummyDto);
        when(adRepository.findAll()).thenReturn(testList);
        when(mappingAdDTO.toDTOList(testList)).thenReturn(adsDTOTest);
        AdDTOForGetAll expected = new AdDTOForGetAll(adsDTOTest.size(), adsDTOTest);
        AdDTOForGetAll actual = adService.getAllAds();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteAd() {
        when(adRepository.findById(ad.getId())).thenReturn(Optional.of(ad));
        // Текущий пользователь совпадает с автором объявления
        when(securityUtils.getCurrentUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(user);

        adService.deleteAd(ad.getId());

        verify(adRepository, times(1)).delete(ad);
    }

    @Test
    void createAd() throws IOException {
        CreateOrUpdateAdDTO createOrUpdateAdDTO = new CreateOrUpdateAdDTO("title", 1000, "description");
        MultipartFile image = mock(MultipartFile.class);
        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(userRepository.findUserByUsername("username")).thenReturn(user);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("image.jpg");
        when(image.getBytes()).thenReturn(new byte[]{1, 2, 3});

        Ad savedAd = new Ad();
        savedAd.setId(1L);
        savedAd.setTitle(createOrUpdateAdDTO.getTitle());
        savedAd.setDescription(createOrUpdateAdDTO.getDescription());
        savedAd.setPrice(createOrUpdateAdDTO.getPrice());
        savedAd.setAuthor(user);
        savedAd.setImage("some-image-path");

        when(adRepository.save(any(Ad.class))).thenReturn(savedAd);
        ru.skypro.homework.dto.AdDTO returnedDto = new ru.skypro.homework.dto.AdDTO(user, "some-image-path", 1L, 1000, "title");
        when(mappingAdDTO.mapToAdDTO(savedAd)).thenReturn(returnedDto);

        ru.skypro.homework.dto.AdDTO result = adService.createAd(createOrUpdateAdDTO, image);
        verify(mappingAdDTO).mapToAdDTO(savedAd);
        assertThat(result).isEqualTo(returnedDto);
    }

    @Test
    void updateAd() {
        CreateOrUpdateAdDTO updateDTO = new CreateOrUpdateAdDTO("newTitle", 2000, "newDescription");
        when(adRepository.findById(ad.getId())).thenReturn(Optional.of(ad));
        Ad updatedAd = new Ad();
        updatedAd.setId(ad.getId());
        updatedAd.setTitle(updateDTO.getTitle());
        updatedAd.setDescription(updateDTO.getDescription());
        updatedAd.setPrice(updateDTO.getPrice());
        updatedAd.setAuthor(user);
        when(adRepository.save(any(Ad.class))).thenReturn(updatedAd);
        ru.skypro.homework.dto.AdDTO adDTO = new ru.skypro.homework.dto.AdDTO();
        when(mappingAdDTO.mapToAdDTO(updatedAd)).thenReturn(adDTO);

        ru.skypro.homework.dto.AdDTO result = adService.updateAd(ad.getId(), updateDTO);

        assertThat(result).isEqualTo(adDTO);
    }

    @Test
    void updateAdImage() throws IOException {
        MultipartFile image = mock(MultipartFile.class);
        when(adRepository.findById(ad.getId())).thenReturn(Optional.of(ad));
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("new-image.jpg");
        when(image.getBytes()).thenReturn(new byte[]{1, 2, 3});

        adService.updateAdImage(ad.getId(), image);

        verify(adRepository, times(1)).save(ad);
    }
}
