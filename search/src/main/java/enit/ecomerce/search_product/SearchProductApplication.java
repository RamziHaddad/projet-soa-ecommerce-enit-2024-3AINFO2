package enit.ecomerce.search_product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SearchProductApplication {

	 
	public static void main(String[] args) {
		SpringApplication.run(SearchProductApplication.class, args);
	}
	
}
