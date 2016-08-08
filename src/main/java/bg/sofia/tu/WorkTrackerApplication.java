package bg.sofia.tu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("bg.tu.sofia.repository")
@SpringBootApplication
public class WorkTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkTrackerApplication.class, args);
	}
}
