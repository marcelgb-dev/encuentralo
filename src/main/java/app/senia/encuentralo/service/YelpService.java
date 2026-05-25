package app.senia.encuentralo.service;

import app.senia.encuentralo.dto.yelp.BusinessDTO;
import app.senia.encuentralo.dto.yelp.YelpResponse;
import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.model.Usuario; // Añadido
import app.senia.encuentralo.repository.UsuarioRepository; // Añadido para recuperar el objeto real
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class YelpService implements ProviderService {

    @Value("${yelp.locale}")
    private String yelpLocale;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final ResultadoService resultadoService;
    private final UsuarioRepository usuarioRepository; // Inyectamos la despensa de usuarios

    // Constructor actualizado con el nuevo repositorio requerido
    public YelpService(RestClient yelpRestClient, ObjectMapper objectMapper, ResultadoService resultadoService,
            UsuarioRepository usuarioRepository) {
        this.restClient = yelpRestClient;
        this.objectMapper = objectMapper;
        this.resultadoService = resultadoService;
        this.usuarioRepository = usuarioRepository;
    }

    public Busqueda llamarApi(Integer idUsuario, String termino, double latitud, double longitud, int radio,
            int limite) {

        // MOCKING (LLAMADA FALSA) - Ahora le pasamos el idUsuario para vincularlo
        // correctamente
        return llamarApiMock(idUsuario);

        /*
         * // CÓDIGO REAL CORREGIDO (Descomentar cuando paséis a producción)
         * YelpResponse response = restClient.get()
         * .uri(uriBuilder -> uriBuilder
         * .path("/businesses/search")
         * .queryParam("term", termino)
         * .queryParam("latitude", latitud)
         * .queryParam("longitude", longitud)
         * .queryParam("radius", radio)
         * .queryParam("limit", limite)
         * .queryParam("locale", yelpLocale)
         * .build())
         * .retrieve()
         * .body(YelpResponse.class);
         * 
         * if (response != null && response.businesses() != null) {
         * // 1. Buscamos el objeto Usuario real usando el ID
         * Usuario usuario = usuarioRepository.findById(idUsuario)
         * .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " +
         * idUsuario));
         * 
         * // 2. Inicializamos la búsqueda con el objeto usuario completo y ciudad vacía
         * temporal
         * Busqueda busqueda = new Busqueda(termino, LocalDateTime.now(), usuario, "");
         * 
         * // 3. Parseamos pasando el usuario y la búsqueda para que los resultados se
         * aten correctamente
         * List<Resultado> resultados = parsearDtos(response, usuario, busqueda);
         * busqueda.setResultados(resultados);
         * busqueda.setCiudad(obtenerCiudad(busqueda));
         * 
         * // 4. Guardamos usando los getters de ID correspondientes
         * resultadoService.guardarResultados(busqueda.getId(), usuario.getId(),
         * resultados);
         * 
         * return busqueda;
         * }
         * 
         * System.out.println("ERROR: Respuesta de Yelp vacía");
         * return null;
         */
    }

    // Convierte datos del DTO YelpResponse a una Lista<Resultado>
    // CORREGIDO: Ahora acepta el Usuario y la Busqueda activos para cumplir con el
    // nuevo constructor
    private List<Resultado> parsearDtos(YelpResponse yelpResponse, Usuario usuario, Busqueda busqueda) {

        List<BusinessDTO> listaDtos = yelpResponse.businesses();
        List<Resultado> listaResultados = new ArrayList<>();

        for (BusinessDTO dto : listaDtos) {
            String nombre = dto.name();
            double valoracion = dto.rating();
            int numValoraciones = dto.reviewCount();
            String url = dto.url();
            String telefono = dto.displayPhone();
            int distancia = (int) dto.distance();
            String direccion = String.join(", ", dto.location().displayAddress());
            String ciudad = dto.location().city();
            List<Categoria> categorias = Categoria.toCategorias(dto.getCategoryTitles());

            // CORREGIDO: Pasamos los objetos completos en lugar de "0, 0"
            Resultado resultado = new Resultado(nombre, telefono, distancia, direccion, valoracion, numValoraciones,
                    url, false, usuario, busqueda);
            resultado.setCiudad(ciudad);
            resultado.setCategorias(categorias);
            listaResultados.add(resultado);
        }

        return listaResultados;
    }

    // Método de pruebas actualizado para que no rompa la compilación
    public Busqueda llamarApiMock(Integer idUsuario) {

        System.out.println("WARNING: LLAMADA FALSA A LA API CON YelpService.llamarApiMock()");

        try {
            ClassPathResource resource = new ClassPathResource("json/yelp_example.json");

            YelpResponse response = objectMapper.readValue(
                    resource.getInputStream(),
                    YelpResponse.class);

            // 1. Obtenemos el usuario real de la BD si existe, o creamos un cascarón mock
            // para el test
            Usuario usuario = null;
            if (idUsuario != null && idUsuario != 0) {
                usuario = usuarioRepository.findById(idUsuario).orElse(null);
            }
            if (usuario == null) {
                usuario = new Usuario("mock@test.com", "123", "Mock", "User", "USER");
                usuario.setId(idUsuario != null ? idUsuario : 1);
            }

            // 2. Creamos el objeto Busqueda con el objeto Usuario completo
            Busqueda busqueda = new Busqueda("TEST", LocalDateTime.now(), usuario, "Ciudad Temporal");

            // 3. Parseamos los resultados de la respuesta asociándoles el usuario y la
            // búsqueda actuales
            List<Resultado> resultados = parsearDtos(response, usuario, busqueda);

            // 4. Enlazamos la lista a la búsqueda
            busqueda.setResultados(resultados);
            busqueda.setCiudad(obtenerCiudad(busqueda));

            // 5. Guardamos llamando al servicio con el ID numérico correcto extraído del
            // objeto
            resultadoService.guardarResultados(busqueda.getId(), usuario.getId(), resultados);

            return busqueda;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo JSON", e);
        }
    }

    // Ordena los resultados de busqueda por distancia
    private String obtenerCiudad(Busqueda busqueda) {
        return resultadoService.ordenarPorDistancia(busqueda.getResultados(), false).getFirst().getCiudad();
    }
}