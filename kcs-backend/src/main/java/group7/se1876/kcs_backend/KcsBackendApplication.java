package group7.se1876.kcs_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "group7.se1876.kcs_backend.repository")
@EnableScheduling
public class KcsBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(KcsBackendApplication.class, args);
	}

}
