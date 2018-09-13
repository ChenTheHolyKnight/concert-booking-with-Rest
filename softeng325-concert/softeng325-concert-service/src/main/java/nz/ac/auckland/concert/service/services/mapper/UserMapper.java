package nz.ac.auckland.concert.service.services.mapper;

import nz.ac.auckland.concert.common.dto.UserDTO;
import nz.ac.auckland.concert.service.domain.model.User;

public class UserMapper {
    public static User ToUser(UserDTO userDTO){
        return new User(

                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getLastname(),
                userDTO.getFirstname()
        );
    }
    public static UserDTO ToDTO(User user){
        return new UserDTO(

                user.getUsername(),
                user.getPassword(),
                user.getLastname(),
                user.getFirstname()
        );
    }
}
