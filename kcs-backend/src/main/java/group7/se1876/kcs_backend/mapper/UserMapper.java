package group7.se1876.kcs_backend.mapper;

import group7.se1876.kcs_backend.dto.request.UserDto;
import group7.se1876.kcs_backend.dto.response.RoleRespone;
import group7.se1876.kcs_backend.dto.response.UserResponse;
import group7.se1876.kcs_backend.entity.RoleDetail;
import group7.se1876.kcs_backend.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public static UserDto mapToUserDto(User user){
        return new UserDto(
            user.getUserId(),
            user.getUserName(),
            user.getPassword(),
            user.getPhone(),
            user.getEmail(),
            user.isStatus()
        );
    }
    public static User mapToUser(UserDto userDto){
        return new User(
                userDto.getUserId(),
                userDto.getUserName(),
                userDto.getPassword(),
                userDto.getPhone(),
                userDto.getEmail(),
                userDto.isStatus(),
                null
        );
    }
    public static UserResponse mapToUserResponse(User user){
        Set<RoleRespone> role = user.getRoles().stream()
                .map(roleDetail -> new RoleRespone(roleDetail.getRoleType())).collect(Collectors.toSet());
        return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getPhone(),
                user.getEmail(),
                user.isStatus(),
                role
        );
    }

}
