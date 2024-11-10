package group7.se1876.kcs_backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import group7.se1876.kcs_backend.controller.PondController;
import group7.se1876.kcs_backend.dto.request.AddPondRequest;
import group7.se1876.kcs_backend.dto.request.AddWaterParameterRequest;
import group7.se1876.kcs_backend.dto.request.AuthenticationRequest;
import group7.se1876.kcs_backend.dto.response.PondResponse;
import group7.se1876.kcs_backend.entity.Pond;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.repository.UserRepository;
import group7.se1876.kcs_backend.service.FirebaseStorageService;
import group7.se1876.kcs_backend.service.PondService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTesting {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private PondService pondService;

    @Mock
    private FirebaseStorageService firebaseStorageService;

    @InjectMocks
    private PondController pondController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    private String authToken;

    @BeforeEach
    void setup() throws Exception {
        // Create and save a test user in the database
        authToken = loginAndGetToken("testuser11730805294546", "password123");
    }
    @Test
    public void testCreateNewPond() throws Exception {
        // Mock Firebase service to return a URL when called


        // Create a MultipartFile for testing
        Path filePath = Path.of("D:\\Download\\Downloads\\testImg.png");

        MockMultipartFile pondImgFile = new MockMultipartFile(
                "pondImg",
                "testImg.png",
                "image/png",
                Files.readAllBytes(filePath)
        );
        // Prepare the request data
        Random random = new Random();
        AddPondRequest request = new AddPondRequest();
        request.setPondName("Test Pond"+random.nextInt(100));
        request.setSize(50.0 + (random.nextDouble() * 150.0)); // Random size between 50 and 200
        request.setDepth(1.0 + (random.nextDouble() * 5.0)); // Random depth between 1 and 6
        request.setVolume(100.0 + (random.nextDouble() * 500.0)); // Random volume between 100 and 600
        request.setDrainCount(random.nextInt(5) + 1); // Random drain count between 1 and 5
        request.setPumpCapacity(5.0 + (random.nextDouble() * 20.0)); // Random pump capacity between 5 and 25
        request.setPondImg(pondImgFile);

        // Perform the POST request with the multipart form
          mockMvc.perform(MockMvcRequestBuilders.multipart("/pond/add_Pond")
                            .file("pondImg", pondImgFile.getBytes())
                        .param("pondName", request.getPondName())
                        .param("size", String.valueOf(request.getSize()))
                        .param("depth", String.valueOf(request.getDepth()))
                        .param("volume", String.valueOf(request.getVolume()))
                        .param("drainCount", String.valueOf(request.getDrainCount()))
                        .param("pumpCapacity", String.valueOf(request.getPumpCapacity()))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.pondName").value(request.getPondName()))
                .andExpect(jsonPath("$.result.pondImg").value("https://firebasestorage.googleapis.com/v0/b/koicare-d7f6c.appspot.com/o/pond-images%2F?alt=media"));

    }

    @Test
    public void testCreateNewPondWithInvalidToken() throws Exception {

        // Create a MultipartFile for testing
        // Create a MultipartFile for testing
        Path filePath = Path.of("D:\\Download\\Downloads\\testImg.png");

        MockMultipartFile pondImgFile = new MockMultipartFile(
                "pondImg",
                "testImg.png",
                "image/png",
                Files.readAllBytes(filePath)
        );

        // Prepare the request data
        Random random = new Random();
        AddPondRequest request = new AddPondRequest();
        request.setPondName("Test Pond2"+random.nextInt(100));
        request.setSize(50.0 + (random.nextDouble() * 150.0)); // Random size between 50 and 200
        request.setDepth(1.0 + (random.nextDouble() * 5.0)); // Random depth between 1 and 6
        request.setVolume(100.0 + (random.nextDouble() * 500.0)); // Random volume between 100 and 600
        request.setDrainCount(random.nextInt(5) + 1); // Random drain count between 1 and 5
        request.setPumpCapacity(5.0 + (random.nextDouble() * 20.0)); // Random pump capacity between 5 and 25
        request.setPondImg(pondImgFile);

        // Perform the POST request with the multipart form
        mockMvc.perform(MockMvcRequestBuilders.multipart("/pond/add_Pond")
                        .file("pondImg", pondImgFile.getBytes())
                        .param("pondName", request.getPondName())
                        .param("size", String.valueOf(request.getSize()))
                        .param("depth", String.valueOf(request.getDepth()))
                        .param("volume", String.valueOf(request.getVolume()))
                        .param("drainCount", String.valueOf(request.getDrainCount()))
                        .param("pumpCapacity", String.valueOf(request.getPumpCapacity()))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthenticated"));


    }

    @Test
    public void AddWaterParamTesting() throws Exception {
        Long pondId = 52L;
        AddWaterParameterRequest request = new AddWaterParameterRequest();
        request.setParameterId(null);
        request.setMeasurementTime(new Date());
        // Generate random temperature between 15.0 and 35.0 degrees Celsius
        request.setTemperature(ThreadLocalRandom.current().nextDouble(15.0, 35.0));

        // Generate random salinity between 0.5 and 3.5 (typical range for freshwater to brackish water)
        request.setSalinity(ThreadLocalRandom.current().nextDouble(0.5, 3.5));

        // Generate random pH level between 6.5 and 9.0
        request.setPh(ThreadLocalRandom.current().nextDouble(6.5, 9.0));

        // Generate random O2 (oxygen level) between 5.0 and 15.0 mg/L
        request.setO2(ThreadLocalRandom.current().nextDouble(5.0, 15.0));

        // Generate random NO2 (nitrite) between 0.01 and 1.0 mg/L
        request.setNo2(ThreadLocalRandom.current().nextDouble(0.01, 1.0));

        // Generate random NO3 (nitrate) between 1.0 and 50.0 mg/L
        request.setNo3(ThreadLocalRandom.current().nextDouble(1.0, 50.0));

        // Generate random PO4 (phosphate) between 0.1 and 5.0 mg/L
        request.setPo4(ThreadLocalRandom.current().nextDouble(0.1, 5.0));


        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/pond/water_parameter/{pondId}",pondId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.parameterId").exists())
                .andExpect(jsonPath("$.result.temperature").value(request.getTemperature()))
                .andExpect(jsonPath("$.result.salinity").value(request.getSalinity()))
                .andExpect(jsonPath("$.result.ph").value(request.getPh()))
                .andExpect(jsonPath("$.result.o2").value(request.getO2()))
                .andExpect(jsonPath("$.result.no2").value(request.getNo2()))
                .andExpect(jsonPath("$.result.no3").value(request.getNo3()))
                .andExpect(jsonPath("$.result.po4").value(request.getPo4()));
    }

    @Test
    public void AddWaterParamWithInvalidPondIdTesting() throws Exception {
        Long pondId = 0L;
        AddWaterParameterRequest request = new AddWaterParameterRequest();
        request.setParameterId(null);
        request.setMeasurementTime(new Date());
        // Generate random temperature between 15.0 and 35.0 degrees Celsius
        request.setTemperature(ThreadLocalRandom.current().nextDouble(15.0, 35.0));

        // Generate random salinity between 0.5 and 3.5 (typical range for freshwater to brackish water)
        request.setSalinity(ThreadLocalRandom.current().nextDouble(0.5, 3.5));

        // Generate random pH level between 6.5 and 9.0
        request.setPh(ThreadLocalRandom.current().nextDouble(6.5, 9.0));

        // Generate random O2 (oxygen level) between 5.0 and 15.0 mg/L
        request.setO2(ThreadLocalRandom.current().nextDouble(5.0, 15.0));

        // Generate random NO2 (nitrite) between 0.01 and 1.0 mg/L
        request.setNo2(ThreadLocalRandom.current().nextDouble(0.01, 1.0));

        // Generate random NO3 (nitrate) between 1.0 and 50.0 mg/L
        request.setNo3(ThreadLocalRandom.current().nextDouble(1.0, 50.0));

        // Generate random PO4 (phosphate) between 0.1 and 5.0 mg/L
        request.setPo4(ThreadLocalRandom.current().nextDouble(0.1, 5.0));

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/pond/water_parameter/{pondId}",pondId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("1015"));

    }


    @Test
    public void addPondAndAddWaterParam() throws Exception {

        Path filePath = Path.of("D:\\Download\\Downloads\\testImg.png");
        Random random = new Random();
        MockMultipartFile pondImgFile = new MockMultipartFile(
                "pondImg",
                "testImg.png",
                "image/png",
                Files.readAllBytes(filePath)
        );
        // Prepare the request data
        AddPondRequest request = new AddPondRequest();
        request.setPondName("Test Pond"+random.nextInt(100));
        request.setSize(50.0 + (random.nextDouble() * 150.0)); // Random size between 50 and 200
        request.setDepth(1.0 + (random.nextDouble() * 5.0)); // Random depth between 1 and 6
        request.setVolume(100.0 + (random.nextDouble() * 500.0)); // Random volume between 100 and 600
        request.setDrainCount(random.nextInt(5) + 1); // Random drain count between 1 and 5
        request.setPumpCapacity(5.0 + (random.nextDouble() * 20.0)); // Random pump capacity between 5 and 25
        request.setPondImg(pondImgFile);

        // Perform the POST request with the multipart form
        MvcResult result =  mockMvc.perform(MockMvcRequestBuilders.multipart("/pond/add_Pond")
                        .file("pondImg", pondImgFile.getBytes())
                        .param("pondName", request.getPondName())
                        .param("size", String.valueOf(request.getSize()))
                        .param("depth", String.valueOf(request.getDepth()))
                        .param("volume", String.valueOf(request.getVolume()))
                        .param("drainCount", String.valueOf(request.getDrainCount()))
                        .param("pumpCapacity", String.valueOf(request.getPumpCapacity()))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.pondName").value(request.getPondName()))
                .andExpect(jsonPath("$.result.pondImg").value("https://firebasestorage.googleapis.com/v0/b/koicare-d7f6c.appspot.com/o/pond-images%2F?alt=media"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        // Extract the pondId
        Long pondId = responseJson.path("result").path("pondId").asLong();

        AddWaterParameterRequest requestWater = new AddWaterParameterRequest();
        requestWater.setParameterId(null);
        requestWater.setMeasurementTime(new Date());
        // Generate random temperature between 15.0 and 35.0 degrees Celsius
        requestWater.setTemperature(ThreadLocalRandom.current().nextDouble(15.0, 35.0));

        // Generate random salinity between 0.5 and 3.5 (typical range for freshwater to brackish water)
        requestWater.setSalinity(ThreadLocalRandom.current().nextDouble(0.5, 3.5));

        // Generate random pH level between 6.5 and 9.0
        requestWater.setPh(ThreadLocalRandom.current().nextDouble(6.5, 9.0));

        // Generate random O2 (oxygen level) between 5.0 and 15.0 mg/L
        requestWater.setO2(ThreadLocalRandom.current().nextDouble(5.0, 15.0));

        // Generate random NO2 (nitrite) between 0.01 and 1.0 mg/L
        requestWater.setNo2(ThreadLocalRandom.current().nextDouble(0.01, 1.0));

        // Generate random NO3 (nitrate) between 1.0 and 50.0 mg/L
        requestWater.setNo3(ThreadLocalRandom.current().nextDouble(1.0, 50.0));

        // Generate random PO4 (phosphate) between 0.1 and 5.0 mg/L
        requestWater.setPo4(ThreadLocalRandom.current().nextDouble(0.1, 5.0));

        String requestJson = objectMapper.writeValueAsString(requestWater);

        mockMvc.perform(MockMvcRequestBuilders.post("/pond/water_parameter/{pondId}",pondId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.parameterId").exists())
                .andExpect(jsonPath("$.result.temperature").value(requestWater.getTemperature()))
                .andExpect(jsonPath("$.result.salinity").value(requestWater.getSalinity()))
                .andExpect(jsonPath("$.result.ph").value(requestWater.getPh()))
                .andExpect(jsonPath("$.result.o2").value(requestWater.getO2()))
                .andExpect(jsonPath("$.result.no2").value(requestWater.getNo2()))
                .andExpect(jsonPath("$.result.no3").value(requestWater.getNo3()))
                .andExpect(jsonPath("$.result.po4").value(requestWater.getPo4()));
    }

    //login to get token
    public String loginAndGetToken(String username, String password) throws Exception {
        // Prepare login request
         AuthenticationRequest loginRequest = new AuthenticationRequest(username,password);
         String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

         MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andReturn();

        // Extract token from response
        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        return jsonNode.get("result").get("token").asText();
    }
}

