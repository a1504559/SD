package fi.haagahelia.bzot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.ArrayList;

@SpringBootApplication
public class Application {
/*
 http://localhost:8080/start
 http://localhost:8080/rest?word=house
 http://localhost:8080/rest?word=talo&direction=Fi-En
 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}