package oloo.mwm_pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MwmPmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MwmPmsApplication.class, args);
    }

}
