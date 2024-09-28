package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.UserDto;
import group7.se1876.kcs_backend.dto.request.UserUpdateRequest;
import group7.se1876.kcs_backend.dto.response.UserResponse;
import group7.se1876.kcs_backend.entity.RoleDetail;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.enums.Role;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.mapper.UserMapper;
import group7.se1876.kcs_backend.repository.RoleRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserImpl implements  UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;

    //Register
    @Override
    public UserResponse register(UserDto userRequest) {

        // Map data object to entity
        if(userRepository.existsByUserName(userRequest.getUserName()) || userRepository.existsByEmail(userRequest.getEmail())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.mapToUser(userRequest);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        //Set new role for user is USER ROLE
        RoleDetail userRole = roleRepository.findByRoleType(Role.USER.name())
                .orElseGet(()->{
                    RoleDetail newRole = new RoleDetail();
                    newRole.setRoleType(Role.USER.name());
                    return roleRepository.save(newRole);
                });

        HashSet<RoleDetail> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Save data already map to entity to database
        User saveUser = userRepository.save(user);

        return userMapper.mapToUserResponse(saveUser);
    }

    //Get user
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.INVALID_USERID));
        return UserMapper.mapToUserResponse(user);
    }

    // Identity which user is login based on token (when many user login)
    @Override
    public UserResponse getMyInfo() {
        // When login, info of user will save in Security context holder
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUserName(name)
                    .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.mapToUserResponse(user);
    }

    //Get all user
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponse> getAllUser() {

        List<User> users = userRepository.findAll();

        return users.stream().map((user) -> userMapper.mapToUserResponse(user))
                .collect(Collectors.toList());
    }


    //Update user
    @Override
    public UserResponse updateUser(Long userId, UserUpdateRequest newInfoUser) {

            User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.INVALID_USERID));

        user.setUserName(newInfoUser.getUserName());
        user.setPassword(newInfoUser.getPassword());
        user.setEmail(newInfoUser.getEmail());

        return userMapper.mapToUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteUser(Long userId) {
       User user = userRepository.findById(userId)
               .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
       userRepository.delete(user);

    }
}
