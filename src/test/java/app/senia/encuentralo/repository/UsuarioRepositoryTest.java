package app.senia.encuentralo.repository;

import static org.assertj.core.api.Assertions.*;

import app.senia.encuentralo.model.Usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Test
    void findByEmail_usuarioExistente_devuelveUsuario() {
        Usuario usuario = new Usuario("test@example.com", "pass", "Test", "User", "USER");
        entityManager.persist(usuario);
        entityManager.flush();

        Usuario encontrado = usuarioRepo.findByEmail("test@example.com");

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getEmail()).isEqualTo("test@example.com");
        assertThat(encontrado.getNombre()).isEqualTo("Test");
    }

    @Test
    void findByEmail_usuarioNoExistente_devuelveNull() {
        Usuario encontrado = usuarioRepo.findByEmail("no@existe.com");
        assertThat(encontrado).isNull();
    }

    @Test
    void findByEmail_emailUnico_devuelveSoloUno() {
        Usuario u1 = new Usuario("test@example.com", "pass1", "User1", "A", "USER");
        Usuario u2 = new Usuario("test2@example.com", "pass2", "User2", "B", "USER");
        entityManager.persist(u1);
        entityManager.persist(u2);
        entityManager.flush();

        Usuario encontrado = usuarioRepo.findByEmail("test2@example.com");

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getEmail()).isEqualTo("test2@example.com");
    }
}
