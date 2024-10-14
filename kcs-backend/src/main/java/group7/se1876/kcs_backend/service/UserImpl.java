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
import group7.se1876.kcs_backend.repository.TrackingUserRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserImpl implements  UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    //Register
    @Override
    public UserResponse register(UserDto userRequest, String userRoleChoice) {

        // Map data object to entity
        if(userRepository.existsByUserName(userRequest.getUserName()) || userRepository.existsByEmail(userRequest.getEmail())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.mapToUser(userRequest);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        HashSet<RoleDetail> roles = new HashSet<>();
        //Set shop role if user choose role shop when register
        if (userRoleChoice.equalsIgnoreCase("shop")){
            RoleDetail userRole = roleRepository.findByRoleType(Role.SHOP.name())
                    .orElseGet(()->{
                        RoleDetail newRole = new RoleDetail();
                        newRole.setRoleType(Role.SHOP.name());
                        return roleRepository.save(newRole);
                    });

            roles.add(userRole);
            user.setRoles(roles);
        }else {
            //Set new role for user is USER ROLE
            RoleDetail userRole = roleRepository.findByRoleType(Role.USER.name())
                    .orElseGet(()->{
                        RoleDetail newRole = new RoleDetail();
                        newRole.setRoleType(Role.USER.name());
                        return roleRepository.save(newRole);
                    });

            roles.add(userRole);
            user.setRoles(roles);
        }

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
        Long userId = Long.valueOf(context.getAuthentication().getName());


        User user = userRepository.findById(userId)
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
        user.setPassword(passwordEncoder.encode(newInfoUser.getPassword()));
        user.setPhone(newInfoUser.getPhone());
        user.setEmail(newInfoUser.getEmail());

//        var roles = roleRepository.findAllById(newInfoUser.getRoles());
//        user.setRoles(new HashSet<>(roles));

        return userMapper.mapToUserResponse(userRepository.save(user));
    }

    //Delete user
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteUser(Long userId) {
       User user = userRepository.findById(userId)
               .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
       userRepository.deleteById(userId);

    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")

    public UserResponse setStatusAccount(Long userId, String decision) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.INVALID_USERID));

        if(decision.equalsIgnoreCase("unactive")){
            user.setStatus(false);
        }
        else{
            user.setStatus(true);
        }

    return userMapper.mapToUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse setRole(Long userId, String role) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException((ErrorCode.INVALID_USERID)));

        RoleDetail roleDetail;

        switch (role.toLowerCase()) {
            case "admin":
                roleDetail = new RoleDetail();
                roleDetail.setRoleType(Role.ADMIN.name());
                user.getRoles().add(roleDetail);
                break;

            case "shop":
                roleDetail = roleRepository.findByRoleType(Role.SHOP.name())
                        .orElseGet(() -> {
                            RoleDetail newRole = new RoleDetail();
                            newRole.setRoleType(Role.SHOP.name());
                            return roleRepository.save(newRole);
                        });
                user.getRoles().add(roleDetail);
                break;

            case "unshop":
                // Logic for removing the 'shop' role
               Set<RoleDetail> roles = user.getRoles();
               roles.removeIf(rol->rol.getRoleType().equalsIgnoreCase("shop"));
                break;

            default:
                throw new IllegalArgumentException("Invalid role type: " + role);
        }

        userRepository.save(user);

        return userMapper.mapToUserResponse(user);
    }
}
