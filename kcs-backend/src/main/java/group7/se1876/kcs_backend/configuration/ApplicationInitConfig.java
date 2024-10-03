package group7.se1876.kcs_backend.configuration;

import group7.se1876.kcs_backend.entity.RoleDetail;
import group7.se1876.kcs_backend.entity.User;
import group7.se1876.kcs_backend.enums.Role;
import group7.se1876.kcs_backend.repository.RoleRepository;
import group7.se1876.kcs_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@Slf4j
//Config when application run create admin acc
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository){
        return args -> {
               if(userRepository.findByUserName("admin").isEmpty()) {
                   User user = new User();
                   var roles = new HashSet<RoleDetail>();

                   //Save userRoleType to UserRole Entity
                   RoleDetail userRole = new RoleDetail();
                   userRole.setRoleType(Role.ADMIN.name());
                   roleRepository.save(userRole);
                   roles.add(userRole);

                   //create admin acc
                   user.setUserName("admin");
                   user.setPassword(passwordEncoder.encode("admin"));
                   user.setRoles(roles);
                   user.setStatus(true);
                   userRepository.save(user);
                   log.warn("admin user has been created!");
               }

        };
    }
}
