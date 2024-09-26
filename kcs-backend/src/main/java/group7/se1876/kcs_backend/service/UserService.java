package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.UserDto;
import group7.se1876.kcs_backend.dto.request.UserUpdateRequest;
import group7.se1876.kcs_backend.dto.response.UserResponse;

public interface UserService {
    UserResponse register(UserDto userDto);
    UserResponse getUser(Long userId);
    UserResponse updateUser(Long userId, UserUpdateRequest newInfoUser);
}
