package de.griesser.outfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OutfitApplication {

	public static void main(String[] args) {
		SpringApplication.run(OutfitApplication.class, args);
	}

}

