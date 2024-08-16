package oloo.mwm_pms;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MwmPmsApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        SpringApplication.run(MwmPmsApplication.class, args);
    }

}
