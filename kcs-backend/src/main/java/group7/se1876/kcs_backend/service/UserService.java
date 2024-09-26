package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.UserDto;
import group7.se1876.kcs_backend.dto.request.UserUpdateRequest;
import group7.se1876.kcs_backend.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse register(UserDto userDto);
    UserResponse getUser(Long userId);
    List<UserResponse >getAllUser();
    UserResponse updateUser(Long userId, UserUpdateRequest newInfoUser);
}
