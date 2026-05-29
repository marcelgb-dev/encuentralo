package app.senia.encuentralo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

import java.util.TimeZone;

@SpringBootApplication(/* exclude = {DataSourceAutoConfiguration.class} */) // Excluye la necesidad de configurar una
																			// base de datos (temporal)
public class EncuentraloApplication {

	@Value("${server.timezone}")
	private String timezone;

	@PostConstruct
	public void init() {
		// Establece la zona horaria por defecto para toda la aplicación
		TimeZone.setDefault(TimeZone.getTimeZone(timezone));
	}

	public static void main(String[] args) {
		SpringApplication.run(EncuentraloApplication.class, args);
	}

}
