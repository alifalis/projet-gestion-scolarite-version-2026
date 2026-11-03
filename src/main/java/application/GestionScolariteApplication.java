package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"application", "controller", "dao", "modele", "service"})
@EnableJpaRepositories(basePackages = "dao")
@EntityScan(basePackages = "modele")
public class GestionScolariteApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GestionScolariteApplication.class, args);
    }
}
