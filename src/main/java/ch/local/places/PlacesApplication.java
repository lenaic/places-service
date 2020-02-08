package ch.local.places;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PlacesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlacesApplication.class, args);
	}

}
