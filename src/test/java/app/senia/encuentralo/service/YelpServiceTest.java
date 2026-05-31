package app.senia.encuentralo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.repository.UsuarioRepository;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class YelpServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private ResultadoService resultadoService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private YelpService yelpService;

    @BeforeEach
    void setUp() {
        ObjectMapper realMapper = new ObjectMapper();
        yelpService = new YelpService(restClient, realMapper, resultadoService, usuarioRepository);
        when(resultadoService.ordenarPorDistancia(anyList(), eq(false)))
                .thenAnswer(invocation -> (List<Resultado>) invocation.getArgument(0));
    }

    @Test
    void llamarApiMock_conIdUsuario_valido_devuelveBusquedaConResultados() {
        Busqueda busqueda = yelpService.llamarApiMock(1);

        assertThat(busqueda).isNotNull();
        assertThat(busqueda.getResultados()).isNotEmpty();
        assertThat(busqueda.getTermino()).isEqualTo("TEST");
        assertThat(busqueda.getCiudad()).isNotBlank();
    }

    @Test
    void llamarApiMock_resultados_tienenDatosCompletos() {
        Busqueda busqueda = yelpService.llamarApiMock(1);

        assertThat(busqueda.getResultados()).allSatisfy(r -> {
            assertThat(r.getNombre()).isNotBlank();
            assertThat(r.getDireccion()).isNotBlank();
            assertThat(r.getValoracion()).isBetween(0.0, 5.0);
            assertThat(r.getNumReviews()).isPositive();
            assertThat(r.getUrl()).isNotBlank();
        });
    }

    @Test
    void llamarApiMock_resultados_tienenCategorias() {
        Busqueda busqueda = yelpService.llamarApiMock(1);

        assertThat(busqueda.getResultados()).allSatisfy(r -> {
            assertThat(r.getCategorias()).isNotEmpty();
        });
    }
}
