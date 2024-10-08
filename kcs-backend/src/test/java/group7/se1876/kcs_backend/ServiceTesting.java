package group7.se1876.kcs_backend;

import group7.se1876.kcs_backend.repository.UserRepository;
import group7.se1876.kcs_backend.service.UserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ServiceTesting {
    @Autowired
    private UserImpl userImpl;

    @MockBean
    private UserRepository userRepository;

}
