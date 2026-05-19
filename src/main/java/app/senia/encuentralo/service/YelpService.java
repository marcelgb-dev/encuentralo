package app.senia.encuentralo.service;

import app.senia.encuentralo.dto.yelp.BusinessDTO;
import app.senia.encuentralo.dto.yelp.YelpResponse;
import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Resultado;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class YelpService implements ProviderService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    // Constructor
    public YelpService(RestClient yelpRestClient, ObjectMapper objectMapper) {
        this.restClient = yelpRestClient;
        this.objectMapper = objectMapper;
    }

    public Busqueda llamarApi(String termino, double latitud, double longitud, int radio, int limite) {

        // MOCKING (LLAMADA FALSA)
        return llamarApiMock();

//        YelpResponse response = restClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/businesses/search")
//                        .queryParam("term", termino) // Término a buscar (ej: Restaurantes)
//                        .queryParam("latitude", latitud) // Latitud del usuario
//                        .queryParam("longitude", longitud) // Longitud del usuario
//                        .queryParam("radius", radio) // Radio de búsqueda (en metros)
//                        .queryParam("limit", limite) // Número máximo de resultados a pedir
//                        .build())
//                .retrieve()
//                .body(YelpResponse.class);
//
//        // Gracias a Jackson, 'response' ya contiene toda la jerarquía de records mapeada
//        if (response != null && response.businesses() != null) {
//            Busqueda busqueda = new Busqueda(0, termino, latitud, longitud, LocalDateTime.now());
//            busqueda.setResultados(parsearDtos(response));
//
//            return busqueda;
//        }
//
//        System.out.println("ERROR: Respuesta de Yelp vacía");
//        return List.of();
    }

    // Convierte datos del DTO YelpResponse a una Lista<Resultado>
    private List<Resultado> parsearDtos(YelpResponse yelpResponse) {

        List<BusinessDTO> listaDtos = yelpResponse.businesses();
        List<Resultado> listaResultados = new ArrayList<>();

        for (BusinessDTO dto : listaDtos) {
            String nombre = dto.name();
            double rating = dto.rating();
            String url = dto.url();
            String telefono = dto.displayPhone();
            double longitud = dto.coordinates().longitude();
            double latitud = dto.coordinates().latitude();
            String direccion = String.join(", ", dto.location().displayAddress());
            List<Categoria> categorias = Categoria.toCategorias(dto.getCategoryTitles());

            Resultado resultado = new Resultado(nombre, rating, url, telefono, longitud, latitud, direccion, categorias);

            listaResultados.add(resultado);
        }

        return listaResultados;

    }

    // Método de pruebas que devuelve el objeto Busqueda correspondiente a los datos en resources/json/yelp_example.json
    private Busqueda llamarApiMock() {

        System.out.println("WARNING: LLAMADA FALSA A LA API CON YelpService.llamarApiMock()");

        try {
            // Indicamos el path al JSON de pruebas en la carpeta de resources
            ClassPathResource resource = new ClassPathResource("json/yelp_example.json");

            // Creamos una YelpResponse en base a los datos del JSON mediante el ObjectMapper de Jackson
            YelpResponse response = objectMapper.readValue(
                    resource.getInputStream(),
                    YelpResponse.class
            );

            // Convertimos los datos de la YelpResponse al objeto Busqueda, añadiendo también los metadatos
            Busqueda busqueda = new Busqueda( "TEST", 0, 0, LocalDateTime.now());
            busqueda.setResultados(parsearDtos(response));

            return busqueda;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo JSON", e);
        }
    }

}
