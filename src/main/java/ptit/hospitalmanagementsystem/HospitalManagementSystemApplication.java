package ptit.hospitalmanagementsystem;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class HospitalManagementSystemApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone", "UTC");
        SpringApplication.run(HospitalManagementSystemApplication.class, args);
    }

}
