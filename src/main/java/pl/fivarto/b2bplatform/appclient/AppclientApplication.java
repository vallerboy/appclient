package pl.fivarto.b2bplatform.appclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AppclientApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppclientApplication.class, args);
	}
}
