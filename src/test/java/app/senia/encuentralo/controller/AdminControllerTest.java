package app.senia.encuentralo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.UsuarioRepository;
import app.senia.encuentralo.service.UsuarioService;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
@WithMockUser(roles = "ADMIN")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    void showAdminPanel_retornaVistaYUsuarios() throws Exception {
        Usuario admin = new Usuario("admin@test.com", "pass", "Admin", "User", "ADMIN");
        when(usuarioService.obtenerListaUsuarios()).thenReturn(List.of(admin, new Usuario("user@test.com", "pass", "Normal", "User", "USER")));
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(admin);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("panel_admin"))
                .andExpect(model().attributeExists("usuarios"));
    }

    @Test
    void mostrarEditarUsuario_existente_retornaVista() throws Exception {
        Usuario usuario = new Usuario("test@test.com", "pass", "Test", "User", "USER");
        usuario.setId(1);
        when(usuarioService.obtenerUsuario(1)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/editar_usuario").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editar_usuario"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    void mostrarEditarUsuario_noExistente_redirige() throws Exception {
        when(usuarioService.obtenerUsuario(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/editar_usuario").param("id", "999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    void guardarEdicionUsuario_llamaServicioYRedirige() throws Exception {
        mockMvc.perform(post("/editar_usuario")
                        .with(csrf())
                        .param("id", "1")
                        .param("nombre", "Nuevo")
                        .param("email", "nuevo@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(usuarioService).editarUsuario(eq(1), any(Usuario.class));
    }

    @Test
    void eliminarUsuario_existente_eliminaYRedirige() throws Exception {
        Usuario usuario = new Usuario("test@test.com", "pass", "Test", "User", "USER");
        usuario.setId(1);
        when(usuarioService.obtenerUsuario(1)).thenReturn(Optional.of(usuario));

        mockMvc.perform(post("/eliminar_usuario")
                        .with(csrf())
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(flash().attributeExists("eliminado"));

        verify(usuarioService).eliminarUsuario(1);
    }
}
