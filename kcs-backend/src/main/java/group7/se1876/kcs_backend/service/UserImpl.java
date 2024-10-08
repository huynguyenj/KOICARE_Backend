package group7.se1876.kcs_backend.service;

import group7.se1876.kcs_backend.dto.request.UserDto;
import group7.se1876.kcs_backend.dto.request.UserUpdateRequest;
import group7.se1876.kcs_backend.dto.response.UserResponse;
import group7.se1876.kcs_backend.entity.InvalidatedToken;
import group7.se1876.kcs_backend.entity.RoleDetail;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.entity.VerificationToken;
import group7.se1876.kcs_backend.enums.Role;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.mapper.UserMapper;
import group7.se1876.kcs_backend.repository.InvalidatedTokenRepository;
import group7.se1876.kcs_backend.repository.RoleRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import group7.se1876.kcs_backend.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserImpl implements UserService {

    private final JavaMailSenderImpl mailSender;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepository verificationTokenRepository;

    //Register
    @Override
    public UserResponse register(UserDto userRequest) {

        // Map data object to entity
        if (userRepository.existsByUserName(userRequest.getUserName()) || userRepository.existsByEmail(userRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.mapToUser(userRequest);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        //Set new role for user is USER ROLE
        RoleDetail userRole = roleRepository.findByRoleType(Role.USER.name())
                .orElseGet(() -> {
                    RoleDetail newRole = new RoleDetail();
                    newRole.setRoleType(Role.USER.name());
                    return roleRepository.save(newRole);
                });

        HashSet<RoleDetail> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Save data already map to entity to database
        User saveUser = userRepository.save(user);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(saveUser)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        verificationTokenRepository.save(verificationToken);
        sendVerificationEmail(user.getUserName(), token);

        return userMapper.mapToUserResponse(saveUser);
    }

    @Async
    protected void sendVerificationEmail(String recipientEmail, String token) {
        // Send email to user
        String subject = "Complete your registration";
        String verificationUrl = "http://localhost:8081/auth/verifyToken?token=" + token;
        String message = "To verify your account, please click the link below:\n" + verificationUrl;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(subject);
            helper.setTo(recipientEmail);
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);
    }

    //Get user
    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USERID));
        return UserMapper.mapToUserResponse(user);
    }

    // Identity which user is login based on token (when many user login)
    @Override
    public UserResponse getMyInfo() {
        // When login, info of user will save in Security context holder
        var context = SecurityContextHolder.getContext();
        Long userId = Long.valueOf(context.getAuthentication().getName());


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

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
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USERID));

        user.setUserName(newInfoUser.getUserName());
        user.setPassword(passwordEncoder.encode(newInfoUser.getPassword()));
        user.setPhone(newInfoUser.getPhone());
        user.setEmail(newInfoUser.getEmail());

//        var roles = roleRepository.findAllById(newInfoUser.getRoles());
//        user.setRoles(new HashSet<>(roles));

        return userMapper.mapToUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userRepository.deleteById(userId);

    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")

    public UserResponse setStatusAccount(Long userId, String decision) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_USERID));

        if (decision.equalsIgnoreCase("unactive")) {
            user.setStatus(false);
        } else {
            user.setStatus(true);
        }

        return userMapper.mapToUserResponse(userRepository.save(user));
    }
}
