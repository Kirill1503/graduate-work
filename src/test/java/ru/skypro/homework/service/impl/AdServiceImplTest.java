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
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdDTOForGetAll;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
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
        var adDTO = mappingAdDTO.mapToAdDTOForGet(ad);
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
        List<AdDTO> adsDTOTest = mappingAdDTO.toDTOList(testList);
        when(mappingAdDTO.toDTOList(testList)).thenReturn(adsDTOTest);
        AdDTOForGetAll adDTOForGetAllTest = new AdDTOForGetAll(adsDTOTest.size(), adsDTOTest);
        assertThat(adService.getAllAdsUser()).isEqualTo(adDTOForGetAllTest);
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
        List<AdDTO> adsDTOTest = List.of(new AdDTO());
        when(adRepository.findAll()).thenReturn(testList);
        when(mappingAdDTO.toDTOList(testList)).thenReturn(adsDTOTest);
        AdDTOForGetAll expected = new AdDTOForGetAll(adsDTOTest.size(), adsDTOTest);
        AdDTOForGetAll actual = adService.getAllAds();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteAd() {
        when(securityUtils.getCurrentUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(user);
        when(adRepository.findById(ad.getId())).thenReturn(Optional.of(ad));

        adService.deleteAd(ad.getId());

        verify(adRepository, times(1)).delete(ad);
    }

    @Test
    void createAd() throws IOException {
        CreateOrUpdateAdDTO createOrUpdateAdDTO = new CreateOrUpdateAdDTO("title", 1000,
                "description", "authorname");
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
        AdDTO adDTO = new AdDTO();
        when(mappingAdDTO.mapToAdDTO(savedAd)).thenReturn(adDTO);

        AdDTO result = adService.createAd(createOrUpdateAdDTO, image);

        assertThat(result).isEqualTo(adDTO);
    }

    @Test
    void updateAd() {
        CreateOrUpdateAdDTO updateDTO = new CreateOrUpdateAdDTO("newTitle", 2000,
                "newDescription", "authorname");

        when(securityUtils.getCurrentUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(user);
        when(adRepository.findById(ad.getId())).thenReturn(Optional.of(ad));

        doAnswer(invocation -> {
            Ad updatedAd = invocation.getArgument(0);
            ad.setTitle(updatedAd.getTitle());
            ad.setDescription(updatedAd.getDescription());
            ad.setPrice(updatedAd.getPrice());
            return updatedAd;
        }).when(adRepository).save(any(Ad.class));

        AdDTO adDTO = new AdDTO();
        when(mappingAdDTO.mapToAdDTO(ad)).thenReturn(adDTO);

        AdDTO result = adService.updateAd(ad.getId(), updateDTO);

        assertThat(result).isEqualTo(adDTO);
        assertThat(ad.getTitle()).isEqualTo("newTitle");
        assertThat(ad.getPrice()).isEqualTo(2000);
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