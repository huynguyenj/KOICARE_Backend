package group7.se1876.kcs_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KcsBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(KcsBackendApplication.class, args);
	}

}
