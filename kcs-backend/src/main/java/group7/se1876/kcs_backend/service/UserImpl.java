package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.UserDto;
import group7.se1876.kcs_backend.dto.request.UserUpdateRequest;
import group7.se1876.kcs_backend.dto.response.UserResponse;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.enums.Role;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.mapper.UserMapper;
import group7.se1876.kcs_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@AllArgsConstructor
public class UserImpl implements  UserService{

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public UserResponse register(UserDto userDto) {

        // Map data object to entity
        if(userRepository.existsByUserName(userDto.getUserName()) || userRepository.existsByEmail(userDto.getEmail())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.mapToUser(userDto);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        //Set role for user is USER ROLE
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

        // Save data already map to entity to database
        User saveUser = userRepository.save(user);

        return userMapper.mapToUserResponse(saveUser);
    }

    @Override
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.INVALID_USERID));
        return UserMapper.mapToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(Long userId, UserUpdateRequest newInfoUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.INVALID_USERID));

        user.setUserName(newInfoUser.getUserName());
        user.setPassword(newInfoUser.getPassword());
        user.setEmail(newInfoUser.getEmail());

        return userMapper.mapToUserResponse(userRepository.save(user));
    }
}
