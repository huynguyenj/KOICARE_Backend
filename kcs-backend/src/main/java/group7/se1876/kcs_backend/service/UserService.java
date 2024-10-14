package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.UserDto;
import group7.se1876.kcs_backend.dto.request.UserUpdateRequest;
import group7.se1876.kcs_backend.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse register(UserDto userDto);
    UserResponse getUser(Long userId);
    UserResponse getMyInfo();
    List<UserResponse >getAllUser();
    UserResponse updateUser(Long userId, UserUpdateRequest newInfoUser);
    void deleteUser(Long userId);
    UserResponse setStatusAccount(Long userId, String decision);
    UserResponse setRole(Long userId,String role);
}
