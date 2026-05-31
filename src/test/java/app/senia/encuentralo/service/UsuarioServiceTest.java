package app.senia.encuentralo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void nuevoUsuario_emailValido_guardaUsuario() throws Exception {
        Usuario usuario = new Usuario("test@example.com", "password", "Test", "User", "USER");
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(null);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioService.nuevoUsuario(usuario);

        assertThat(resultado.getPassword()).isEqualTo("encoded");
        assertThat(resultado.getRol()).isEqualTo("USER");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void nuevoUsuario_sinRol_asignaUSERporDefecto() throws Exception {
        Usuario usuario = new Usuario("test@example.com", "password", "Test", "User", null);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(null);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioService.nuevoUsuario(usuario);

        assertThat(resultado.getRol()).isEqualTo("USER");
    }

    @Test
    void nuevoUsuario_emailInvalido_lanzaExcepcion() {
        Usuario usuario = new Usuario("invalido", "password", "Test", "User", "USER");
        assertThatThrownBy(() -> usuarioService.nuevoUsuario(usuario))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("formato del email");
    }

    @Test
    void nuevoUsuario_emailDuplicado_lanzaExcepcion() {
        Usuario usuario = new Usuario("test@example.com", "password", "Test", "User", "USER");
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(usuario);

        assertThatThrownBy(() -> usuarioService.nuevoUsuario(usuario))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("ya está registrado");
    }

    @Test
    void editarUsuario_usuarioExistente_actualizaDatos() {
        Usuario existente = new Usuario("old@test.com", "encoded", "Old", "Name", "USER");
        existente.setId(1);
        Usuario datosNuevos = new Usuario("new@test.com", "", "New", "Name", "ADMIN");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(existente));

        usuarioService.editarUsuario(1, datosNuevos);

        assertThat(existente.getEmail()).isEqualTo("new@test.com");
        assertThat(existente.getNombre()).isEqualTo("New");
        assertThat(existente.getRol()).isEqualTo("ADMIN");
        assertThat(existente.getPassword()).isEqualTo("encoded");
        verify(usuarioRepository).save(existente);
    }

    @Test
    void editarUsuario_usuarioNoExistente_noHaceNada() {
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        usuarioService.editarUsuario(999, new Usuario());

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void eliminarUsuario_eliminaPorId() {
        usuarioService.eliminarUsuario(1);
        verify(usuarioRepository).deleteById(1);
    }

    @Test
    void obtenerListaUsuarios_devuelveTodos() {
        when(usuarioRepository.findAll()).thenReturn(List.of(new Usuario(), new Usuario()));
        assertThat(usuarioService.obtenerListaUsuarios()).hasSize(2);
    }

    @Test
    void obtenerUsuario_existente_devuelveOptional() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.obtenerUsuario(1);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1);
    }

    @Test
    void cambiarContrasena_datosCorrectos_actualiza() throws Exception {
        Usuario usuario = new Usuario("test@test.com", "encoded-old", "Test", "User", "USER");
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(usuario);
        when(passwordEncoder.matches("oldPass", "encoded-old")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encoded-new");

        usuarioService.cambiarContrasena("test@test.com", "oldPass", "newPass");

        assertThat(usuario.getPassword()).isEqualTo("encoded-new");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void cambiarContrasena_contrasenaActualIncorrecta_lanzaExcepcion() {
        Usuario usuario = new Usuario("test@test.com", "encoded-old", "Test", "User", "USER");
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(usuario);
        when(passwordEncoder.matches("wrongPass", "encoded-old")).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.cambiarContrasena("test@test.com", "wrongPass", "newPass"))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("actual es incorrecta");
    }

    @Test
    void cambiarContrasena_usuarioNoExiste_lanzaExcepcion() {
        when(usuarioRepository.findByEmail("no@existe.com")).thenReturn(null);

        assertThatThrownBy(() -> usuarioService.cambiarContrasena("no@existe.com", "pass", "newPass"))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("no encontrado");
    }
}
