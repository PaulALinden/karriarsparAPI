package se.karriarspar.karriarsparAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

@SpringBootApplication
public class KarriarsparApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("GEMINI.API.KEY", Objects.requireNonNull(dotenv.get("GEMINI.API.KEY")));
		System.setProperty("GEMINI.API.URL", Objects.requireNonNull(dotenv.get("GEMINI.API.URL")));
		SpringApplication.run(KarriarsparApplication.class, args);
	}

}


