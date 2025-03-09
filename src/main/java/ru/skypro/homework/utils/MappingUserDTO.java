package ru.skypro.homework.utils;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.GetUserDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.model.User;

@Component
public class MappingUserDTO {

    public GetUserDTO mapToUserDTOForGetUserInformation(User user) {
        GetUserDTO getUserDTO = new GetUserDTO();
        getUserDTO.setId(user.getId());
        getUserDTO.setFirstName(user.getFirstName());
        getUserDTO.setLastName(user.getLastName());
        getUserDTO.setEmail(user.getEmail());
        getUserDTO.setPhone(user.getPhone());
        getUserDTO.setImage(user.getImage());
        getUserDTO.setRole(user.getRole());
        return getUserDTO;
    }

    public UpdateUserDTO mapToUserDTOForUpdateUser(User user) {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName(user.getFirstName());
        updateUserDTO.setLastName(user.getLastName());
        updateUserDTO.setPhone(user.getPhone());
        return updateUserDTO;
    }
}
