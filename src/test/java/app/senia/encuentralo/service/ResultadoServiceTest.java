package app.senia.encuentralo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Etiqueta;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.repository.ResultadoRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResultadoServiceTest {

    @Mock
    private ResultadoRepository resultadoRepo;

    @InjectMocks
    private ResultadoService resultadoService;

    private List<Resultado> resultados;

    @BeforeEach
    void setUp() {
        Categoria comida = new Categoria("Comida");
        Categoria bares = new Categoria("Bares");
        Categoria tiendas = new Categoria("Tiendas");

        Etiqueta favorito = new Etiqueta("favorito", null);
        favorito.setId(1);

        Resultado r1 = crearResultado(1, "Restaurante A", 4.5, 100, 1.5, true, List.of(comida));
        r1.setEtiquetas(new ArrayList<>(List.of(favorito)));

        Resultado r2 = crearResultado(2, "Bar B", 3.0, 200, 0.5, false, List.of(bares));
        r2.setEtiquetas(new ArrayList<>());

        Resultado r3 = crearResultado(3, "Tienda C", 5.0, 50, 3.0, true, List.of(tiendas));
        r3.setEtiquetas(new ArrayList<>());

        Resultado r4 = crearResultado(4, "Cafetería D", 4.0, 150, 2.0, false, List.of(comida, bares));
        r4.setEtiquetas(new ArrayList<>(List.of(favorito)));

        resultados = new ArrayList<>(List.of(r1, r2, r3, r4));
    }

    private Resultado crearResultado(Integer id, String nombre, double valoracion, int numReviews,
                                     double distancia, boolean esFavorito, List<Categoria> categorias) {
        Resultado r = new Resultado();
        r.setId(id);
        r.setNombre(nombre);
        r.setValoracion(valoracion);
        r.setNumReviews(numReviews);
        r.setDistancia(distancia);
        r.setEsFavorito(esFavorito);
        r.setCategorias(categorias);
        return r;
    }

    @Test
    void ordenarPorDistancia_ascendente() {
        List<Resultado> ordenados = resultadoService.ordenarPorDistancia(resultados, false);
        assertThat(ordenados.get(0).getDistancia()).isEqualTo(0.5);
        assertThat(ordenados.get(3).getDistancia()).isEqualTo(3.0);
    }

    @Test
    void ordenarPorDistancia_descendente() {
        List<Resultado> ordenados = resultadoService.ordenarPorDistancia(resultados, true);
        assertThat(ordenados.get(0).getDistancia()).isEqualTo(3.0);
        assertThat(ordenados.get(3).getDistancia()).isEqualTo(0.5);
    }

    @Test
    void ordenarPorNombre_ascendente() {
        List<Resultado> ordenados = resultadoService.ordenarPorNombre(resultados, false);
        assertThat(ordenados.get(0).getNombre()).isEqualTo("Bar B");
        assertThat(ordenados.get(3).getNombre()).isEqualTo("Tienda C");
    }

    @Test
    void ordenarPorNombre_descendente() {
        List<Resultado> ordenados = resultadoService.ordenarPorNombre(resultados, true);
        assertThat(ordenados.get(0).getNombre()).isEqualTo("Tienda C");
        assertThat(ordenados.get(3).getNombre()).isEqualTo("Bar B");
    }

    @Test
    void ordenarPorValoracion_descendente_porDefecto() {
        List<Resultado> ordenados = resultadoService.ordenarPorValoracion(resultados, false);
        assertThat(ordenados.get(0).getValoracion()).isEqualTo(5.0);
        assertThat(ordenados.get(3).getValoracion()).isEqualTo(3.0);
    }

    @Test
    void ordenarPorValoracion_ascendente() {
        List<Resultado> ordenados = resultadoService.ordenarPorValoracion(resultados, true);
        assertThat(ordenados.get(0).getValoracion()).isEqualTo(3.0);
        assertThat(ordenados.get(3).getValoracion()).isEqualTo(5.0);
    }

    @Test
    void filtrarPorValoracion_filtraCorrectamente() {
        List<Resultado> filtrados = resultadoService.filtrarPorValoracion(resultados, 4.0);
        assertThat(filtrados).hasSize(3);
        assertThat(filtrados).extracting(Resultado::getNombre)
                .containsExactlyInAnyOrder("Restaurante A", "Tienda C", "Cafetería D");
    }

    @Test
    void filtrarPorValoracion_sinResultados() {
        List<Resultado> filtrados = resultadoService.filtrarPorValoracion(resultados, 6.0);
        assertThat(filtrados).isEmpty();
    }

    @Test
    void filtrarPorCategoria_unicoNombre_coincide() {
        List<Resultado> filtrados = resultadoService.filtrarPorCategoria(resultados, "Comida");
        assertThat(filtrados).hasSize(2);
        assertThat(filtrados).extracting(Resultado::getNombre)
                .containsExactlyInAnyOrder("Restaurante A", "Cafetería D");
    }

    @Test
    void filtrarPorCategoria_unicoNombre_sinCoincidencia() {
        List<Resultado> filtrados = resultadoService.filtrarPorCategoria(resultados, "Inexistente");
        assertThat(filtrados).isEmpty();
    }

    @Test
    void filtrarPorCategoria_listaNombres_logicaOR() {
        List<Resultado> filtrados = resultadoService.filtrarPorCategoria(resultados, List.of("Comida", "Tiendas"));
        assertThat(filtrados).hasSize(3);
        assertThat(filtrados).extracting(Resultado::getNombre)
                .containsExactlyInAnyOrder("Restaurante A", "Tienda C", "Cafetería D");
    }

    @Test
    void filtrarPorCategoria_listaVacia_devuelveVacio() {
        List<Resultado> filtrados = resultadoService.filtrarPorCategoria(resultados, List.of());
        assertThat(filtrados).isEmpty();
    }

    @Test
    void filtrarSoloFavoritos_devuelveSoloFavoritos() {
        List<Resultado> filtrados = resultadoService.filtrarSoloFavoritos(resultados);
        assertThat(filtrados).hasSize(2);
        assertThat(filtrados).allMatch(Resultado::isEsFavorito);
    }

    @Test
    void filtrarPorEtiqueta_unicoNombre_coincide() {
        List<Resultado> filtrados = resultadoService.filtrarPorEtiqueta(resultados, "favorito");
        assertThat(filtrados).hasSize(2);
    }

    @Test
    void filtrarPorEtiqueta_listaNombres_logicaOR() {
        List<Resultado> filtrados = resultadoService.filtrarPorEtiqueta(resultados, List.of("favorito"));
        assertThat(filtrados).hasSize(2);
    }

    @Test
    void guardarFavorito_toggleActiva() {
        Resultado resultado = resultados.get(1);
        resultado.setEsFavorito(false);
        when(resultadoRepo.findById(2)).thenReturn(Optional.of(resultado));

        resultadoService.guardarFavorito(2);

        assertThat(resultado.isEsFavorito()).isTrue();
        verify(resultadoRepo).save(resultado);
    }

    @Test
    void guardarFavorito_toggleDesactiva() {
        Resultado resultado = resultados.get(0);
        resultado.setEsFavorito(true);
        when(resultadoRepo.findById(1)).thenReturn(Optional.of(resultado));

        resultadoService.guardarFavorito(1);

        assertThat(resultado.isEsFavorito()).isFalse();
        verify(resultadoRepo).save(resultado);
    }

    @Test
    void guardarFavorito_resultadoNoExiste_lanzaExcepcion() {
        when(resultadoRepo.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resultadoService.guardarFavorito(999))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
