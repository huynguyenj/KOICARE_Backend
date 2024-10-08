package group7.se1876.kcs_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import group7.se1876.kcs_backend.dto.request.UserDto;
import group7.se1876.kcs_backend.dto.response.UserResponse;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.mapper.UserMapper;
import group7.se1876.kcs_backend.service.UserService;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTesting {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDto request;
    private UserResponse userResponse;
    private UserMapper userMapper;


    @BeforeEach
    //Create data for test
    void initData(){
        request = new UserDto(
                3L,
                "Jack",
                "123456789",
                Long.valueOf("0879197994"),
                "jacknguyen@gmail.com",
                true
        );

        User user = userMapper.mapToUser(request);

        userResponse = userMapper.mapToUserResponse(user);
    }

    //Test case for register
    @Test
    void registerTestDataInit() throws Exception {

        //Given:
        ObjectMapper objectMapper = new ObjectMapper();

        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.register(ArgumentMatchers.any()))
                        .thenReturn(userResponse);
        //When:
        mockMvc.perform(MockMvcRequestBuilders.post("/api/register") //
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
        //Assert:
                .andExpect(MockMvcResultMatchers.status().isOk()) // expect that code run successfully
                .andExpect(MockMvcResultMatchers.jsonPath("code") // expect that value return is fit with value expected
                        .value(1010))
                .andExpect(MockMvcResultMatchers.jsonPath("result.userId")  // expect that value of id return is fit with value of id expected
                        .value(3)

        );
    }

    @Test
//Test case for getAllUsers function
    //Set up Security config to permit the url without using token
    //Go to UserImpl to set @PreAuthorize("hasAuthority('ROLE_ADMIN')") off
    void getAllUsersTest() throws Exception {
        // Given: Set up mock data for the list of users
        UserResponse userResponse1 = new UserResponse(
                1L, "Alice", 1234567890L, "alice@example.com", true, new HashSet<>()
        );
        UserResponse userResponse2 = new UserResponse(
                2L, "Bob", 1234567891L, "bob@example.com", true, new HashSet<>()
        );

        List<UserResponse> userResponses = List.of(userResponse1, userResponse2);

        // Mock the userService.getAllUser() method to return the list of userResponses
        Mockito.when(userService.getAllUser()).thenReturn(userResponses);

        // When: Perform the GET request to /api/getUsers
        mockMvc.perform(MockMvcRequestBuilders.get("/api/getUsers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                // Assert: Verify the response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.length()").value(2)) // Expect 2 users
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].userId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].userName").value("Alice"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[1].userId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[1].userName").value("Bob"));
    }
}
