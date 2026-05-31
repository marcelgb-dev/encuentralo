package app.senia.encuentralo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.repository.CategoriaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepo;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void obtenerCategorias_desdeResultados_deduplicaYOrdena() {
        Categoria comida = new Categoria("Comida");
        Categoria bares = new Categoria("Bares");

        Resultado r1 = new Resultado();
        r1.setCategorias(List.of(comida, bares));

        Resultado r2 = new Resultado();
        r2.setCategorias(List.of(comida));

        List<Categoria> categorias = categoriaService.obtenerCategorias(List.of(r1, r2));

        assertThat(categorias).hasSize(2);
        assertThat(categorias.get(0).getNombre()).isEqualTo("Bares");
        assertThat(categorias.get(1).getNombre()).isEqualTo("Comida");
    }

    @Test
    void obtenerCategorias_sinResultados_devuelveVacio() {
        List<Categoria> categorias = categoriaService.obtenerCategorias(List.of());
        assertThat(categorias).isEmpty();
    }

    @Test
    void limpiarCategoriasBusqueda_reemplazaConInstanciasDeBD() {
        Categoria comidaDB = new Categoria("Comida");
        comidaDB.setId(1);
        Categoria baresDB = new Categoria("Bares");
        baresDB.setId(2);

        Resultado r1 = new Resultado();
        Categoria comidaTransient = new Categoria("Comida");
        Categoria baresTransient = new Categoria("Bares");
        r1.setCategorias(new ArrayList<>(List.of(comidaTransient, baresTransient)));

        Busqueda busqueda = new Busqueda();
        busqueda.setResultados(List.of(r1));

        when(categoriaRepo.findByNombre("Comida")).thenReturn(Optional.of(comidaDB));
        when(categoriaRepo.findByNombre("Bares")).thenReturn(Optional.of(baresDB));

        Busqueda resultado = categoriaService.limpiarCategoriasBusqueda(busqueda);

        List<Categoria> categoriasFinales = resultado.getResultados().get(0).getCategorias();
        assertThat(categoriasFinales).hasSize(2);
        assertThat(categoriasFinales.get(0).getId()).isEqualTo(1);
        assertThat(categoriasFinales.get(1).getId()).isEqualTo(2);
    }

    @Test
    void limpiarCategoriasBusqueda_categoriaNueva_creaSinPersistir() {
        Categoria comidaTransient = new Categoria("Comida");

        Resultado r1 = new Resultado();
        r1.setCategorias(new ArrayList<>(List.of(comidaTransient)));

        Busqueda busqueda = new Busqueda();
        busqueda.setResultados(List.of(r1));

        when(categoriaRepo.findByNombre("Comida")).thenReturn(Optional.empty());

        Busqueda resultado = categoriaService.limpiarCategoriasBusqueda(busqueda);

        List<Categoria> categoriasFinales = resultado.getResultados().get(0).getCategorias();
        assertThat(categoriasFinales).hasSize(1);
        assertThat(categoriasFinales.get(0).getNombre()).isEqualTo("Comida");
        assertThat(categoriasFinales.get(0).getId()).isNull();
    }

    @Test
    void limpiarCategoriasBusqueda_categoriasDuplicadasEnResultados_usaMismaInstancia() {
        Categoria comidaDB = new Categoria("Comida");
        comidaDB.setId(1);

        Resultado r1 = new Resultado();
        r1.setCategorias(new ArrayList<>(List.of(new Categoria("Comida"))));
        Resultado r2 = new Resultado();
        r2.setCategorias(new ArrayList<>(List.of(new Categoria("Comida"))));

        Busqueda busqueda = new Busqueda();
        busqueda.setResultados(List.of(r1, r2));

        when(categoriaRepo.findByNombre("Comida")).thenReturn(Optional.of(comidaDB));

        categoriaService.limpiarCategoriasBusqueda(busqueda);

        Categoria catR1 = busqueda.getResultados().get(0).getCategorias().get(0);
        Categoria catR2 = busqueda.getResultados().get(1).getCategorias().get(0);

        assertThat(catR1).isSameAs(catR2);
        verify(categoriaRepo, times(1)).findByNombre("Comida");
    }
}
