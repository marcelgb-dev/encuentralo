package app.senia.encuentralo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

// Configuración de RestTemplate para usar los datos de la API de Yelp
@Configuration
public class YelpConfig {

    @Value("${yelp.api.key}")
    private String apiKey;

    @Bean
    public RestClient yelpRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.yelp.com/v3")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }
}