package group7.se1876.kcs_backend;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import group7.se1876.kcs_backend.dto.request.AuthenticationRequest;
import group7.se1876.kcs_backend.dto.request.UserDto;
import group7.se1876.kcs_backend.entity.RoleDetail;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.enums.Role;
import group7.se1876.kcs_backend.exception.AppException;
import group7.se1876.kcs_backend.exception.ErrorCode;
import group7.se1876.kcs_backend.repository.RoleRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTesting {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    //Register test
    @BeforeTransaction
    void setupRoles() {
        // Ensure roles exist without duplicating them

            RoleDetail userRole = roleRepository.findByRoleType(Role.USER.name())
                    .orElseGet(()->{
                        RoleDetail newRole = new RoleDetail();
                        newRole.setRoleType(Role.USER.name());
                        roleRepository.save(newRole);
                        return newRole;
                    });



        RoleDetail shopRole = roleRepository.findByRoleType(Role.SHOP.name())
                .orElseGet(()->{
                    RoleDetail newRole = new RoleDetail();
                    newRole.setRoleType(Role.SHOP.name());
                    roleRepository.save(newRole);
                    return newRole;
                });
    }

    @Test
    void registerUserWithUserRole() throws Exception {
        // Use a unique username and email to prevent conflicts
        UserDto userDto = new UserDto(
                null,
                "testuser1" + System.currentTimeMillis(),
                "password123",
                "1234567890",
                "testuser" + System.currentTimeMillis() + "@example.com",
                true
        );

        String userDtoJson = objectMapper.writeValueAsString(userDto);

        // Perform the registration request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
                        .param("userRoleChoice", "user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.userName").value(userDto.getUserName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(userDto.getEmail()));

        // Verify the user is saved with USER role
        User savedUser = userRepository.findByUserName(userDto.getUserName()).orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserName()).isEqualTo(userDto.getUserName());
    }

    @Test
    void registerUserWithShopRole() throws Exception {
        // Use a unique username and email to prevent conflicts
        UserDto userDto = new UserDto(
                null,
                "shopuser" + System.currentTimeMillis(),
                "password123",
                "0987654321",
                "shopuser" + System.currentTimeMillis() + "@example.com",
                true
        );

        String userDtoJson = objectMapper.writeValueAsString(userDto);

        // Perform the registration request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
                        .param("userRoleChoice", "shop"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.userName").value(userDto.getUserName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(userDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.roles[0].userType").value("SHOP"))
            ;

        // Verify the user is saved with SHOP role
        User savedUser = userRepository.findByUserName(userDto.getUserName()).orElse(null);
        RoleDetail roleDetail = roleRepository.findByRoleType(Role.SHOP.name()).orElseThrow(()-> new AppException(ErrorCode.DATA_NOT_EXISTED));
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserName()).isEqualTo(userDto.getUserName());


    }

    @Test
    void registerUserAlreadyExists() throws Exception {
        // Prepare an existing user to test duplicate prevention
        String uniqueUsername = "existinguser" + System.currentTimeMillis();
        String uniqueEmail = "existinguser" + System.currentTimeMillis() + "@example.com";

        RoleDetail userRole = roleRepository.findByRoleType(Role.USER.name())
                .orElseGet(() -> {
                    RoleDetail newRole = new RoleDetail();
                    newRole.setRoleType(Role.USER.name());
                    return roleRepository.save(newRole);
                });

        User existingUser = new User();
        existingUser.setUserName(uniqueUsername);
        existingUser.setPassword("password123");
        existingUser.setEmail(uniqueEmail);
        existingUser.setPhone("0123456789");
        existingUser.setStatus(true);
        existingUser.setRoles(Collections.singleton(userRole));
        userRepository.save(existingUser);

        // Try to register a new user with the same username and email
        UserDto userDto = new UserDto(
                null,
                uniqueUsername,
                "newpassword123",
                "0123456789",
                uniqueEmail,
                true
        );

        String userDtoJson = objectMapper.writeValueAsString(userDto);

        // Perform the registration request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
                        .param("userRoleChoice", "user"))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User existed"));
    }


    //Login test

    @Test
    void testSuccessfulLogin() throws Exception {
        // Arrange: Create a user with a known password
        String userName = "testuser1730804954794";
        String password = "password123";


        // Prepare login request
        AuthenticationRequest loginRequest = new AuthenticationRequest(userName, password);
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // Act & Assert: Perform the login request and expect a successful response with a token
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.authenticated").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.token").exists());
    }

    @Test
    void testLoginWithIncorrectPassword() throws Exception {
        // Arrange:  user with a known password
        String userName = "testuser1730804954794";
        String password = "password123";
        String incorrectPassword = "wrongpassword";


        // Prepare login request with incorrect password
        AuthenticationRequest loginRequest = new AuthenticationRequest(userName, incorrectPassword);
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // Act & Assert: Perform the login request and expect an Unauthorized error
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Unauthenticated"));
    }

    @Test
    void testLoginTokenCompatibility() throws Exception {
        // Arrange: user with a known password
        String userName = "testuser1730804954794";
        String password = "password123";
        User user = new User();

        // Prepare login request
        AuthenticationRequest loginRequest = new AuthenticationRequest(userName, password);
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        // Act: Perform login and capture the token
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.authenticated").value(true))
                .andReturn();

        // Extract token from the response
        String token = JsonPath.parse(result.getResponse().getContentAsString())
                .read("$.result.token");

        // Assert: Use the token to access a protected endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/api/myInfo")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("1010"));
    }

}
