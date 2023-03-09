package co.empathy.academy.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SearchApplication {

	public static void main(String[] args) {

		SpringApplication.run(SearchApplication.class, args);

	}



}
