package app.senia.encuentralo.repository;

import static org.assertj.core.api.Assertions.*;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Etiqueta;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.model.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@DataJpaTest
class ResultadoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ResultadoRepository resultadoRepo;

    private Usuario usuario;
    private Busqueda busqueda;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("test@test.com", "pass", "Test", "User", "USER");
        entityManager.persist(usuario);

        busqueda = new Busqueda("test", LocalDateTime.now(), usuario, "Ciudad");
        entityManager.persist(busqueda);
    }

    @Test
    void findByUsuarioIdAndEsFavoritoTrue_encuentraSoloFavoritos() {
        Resultado favorito = crearResultado("Favorito", true);
        Resultado noFavorito = crearResultado("No Favorito", false);
        entityManager.persist(favorito);
        entityManager.persist(noFavorito);
        entityManager.flush();

        List<Resultado> favoritos = resultadoRepo.findByUsuarioIdAndEsFavoritoTrue(usuario.getId());

        assertThat(favoritos).hasSize(1);
        assertThat(favoritos.get(0).getNombre()).isEqualTo("Favorito");
    }

    @Test
    void findByUsuarioIdAndEsFavoritoTrue_sinFavoritos_devuelveVacio() {
        Resultado noFavorito = crearResultado("No Favorito", false);
        entityManager.persist(noFavorito);
        entityManager.flush();

        List<Resultado> favoritos = resultadoRepo.findByUsuarioIdAndEsFavoritoTrue(usuario.getId());

        assertThat(favoritos).isEmpty();
    }

    @Test
    void findByEtiquetasId_encuentraResultadosConEsaEtiqueta() {
        Etiqueta etiqueta = new Etiqueta("test-tag", usuario);
        entityManager.persist(etiqueta);

        Resultado conEtiqueta = crearResultado("Con Etiqueta", false);
        conEtiqueta.setEtiquetas(new ArrayList<>(List.of(etiqueta)));
        Resultado sinEtiqueta = crearResultado("Sin Etiqueta", false);
        entityManager.persist(conEtiqueta);
        entityManager.persist(sinEtiqueta);
        entityManager.flush();

        List<Resultado> resultados = resultadoRepo.findByEtiquetasId(etiqueta.getId());

        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getNombre()).isEqualTo("Con Etiqueta");
    }

    private Resultado crearResultado(String nombre, boolean esFavorito) {
        return new Resultado(nombre, "123456789", 1.0, "Calle Test 123",
                4.0, 10, "https://test.com", esFavorito, usuario, busqueda);
    }
}
