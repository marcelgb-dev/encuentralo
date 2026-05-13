package app.senia.encuentralo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // Excluye la necesidad de configurar una base de datos (temporal)
public class EncuentraloApplication {

	public static void main(String[] args) {
		SpringApplication.run(EncuentraloApplication.class, args);
	}

}
