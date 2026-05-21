package app.senia.encuentralo.service;

import app.senia.encuentralo.service.ResultadosService;
import app.senia.encuentralo.dto.yelp.BusinessDTO;
import app.senia.encuentralo.dto.yelp.YelpResponse;
import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Resultado;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${yelp.locale")
    private String yelpLocale;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final ResultadosService resultadosService;

    // Constructor
    public YelpService(RestClient yelpRestClient, ObjectMapper objectMapper, ResultadosService resultadosService) {
        this.restClient = yelpRestClient;
        this.objectMapper = objectMapper;
        this.resultadosService = resultadosService;
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
//                        .queryParam("locale", yelpLocale) // Código de país / idioma locale

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
            double valoracion = dto.rating();
            int numValoraciones = dto.reviewCount();
            String url = dto.url();
            String telefono = dto.displayPhone();
            double longitud = dto.coordinates().longitude();
            double latitud = dto.coordinates().latitude();
            int distancia = (int)dto.distance();
            String direccion = String.join(", ", dto.location().displayAddress());
            String ciudad = dto.location().city();
            List<Categoria> categorias = Categoria.toCategorias(dto.getCategoryTitles());

            Resultado resultado = new Resultado(nombre, telefono, distancia, direccion, valoracion, numValoraciones, url, false, 0, 0);
            resultado.setCiudad(ciudad);
            resultado.setCategorias(categorias);
            listaResultados.add(resultado);
        }

        return listaResultados;

    }

    // Método de pruebas que devuelve el objeto Busqueda correspondiente a los datos en resources/json/yelp_example.json
    public Busqueda llamarApiMock() {

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
            Busqueda busqueda = new Busqueda( "TEST", LocalDateTime.now(), 0);
            // Usamos obtenerCiudad para obtener la ciudad del resultado más cercano
            busqueda.setCiudad(obtenerCiudad(busqueda));
            // Añadimos la lista de resultados a la búsqueda
            busqueda.setResultados(parsearDtos(response));




            return busqueda;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo JSON", e);
        }
    }

    // Ordena los resultados de busqueda por distancia
    private String obtenerCiudad(Busqueda busqueda) {
        return resultadosService.ordenarPorDistancia(busqueda.getResultados(), false).getFirst().getCiudad();
    }

}
