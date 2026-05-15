package app.senia.encuentralo.service;

import app.senia.encuentralo.dto.yelp.BusinessDTO;
import app.senia.encuentralo.dto.yelp.YelpResponse;
import app.senia.encuentralo.model.Resultado;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class YelpService {

    private final RestClient restClient;

    // Spring inyecta automáticamente el Bean que configuramos arriba
    public YelpService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Resultado> llamarApi(String termino, double latitud, double longitud, int radio, int limite) {
        YelpResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/businesses/search")
                        .queryParam("term", termino) // Término a buscar (ej: Restaurantes)
                        .queryParam("latitude", latitud) // Latitud del usuario
                        .queryParam("longitude", longitud) // Longitud del usuario
                        .queryParam("radius", radio) // Radio de búsqueda (en metros)
                        .queryParam("limit", limite) // Número máximo de resultados a pedir
                        .build())
                .retrieve()
                .body(YelpResponse.class);

        // Gracias a Jackson, 'response' ya contiene toda la jerarquía de records mapeada
        if (response != null && response.businesses() != null) {
            return parsearDtos(response);
        }

        System.out.println("ERROR: Respuesta de Yelp vacía");
        return List.of();
    }

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
            List<String> categorias = dto.getCategoryTitles();

            Resultado resultado = new Resultado(nombre, rating, url, telefono, longitud, latitud, direccion, categorias);

            listaResultados.add(resultado);
        }

        return listaResultados;

    }
}
