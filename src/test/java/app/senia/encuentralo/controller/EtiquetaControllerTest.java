package app.senia.encuentralo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.senia.encuentralo.model.Etiqueta;
import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.UsuarioRepository;
import app.senia.encuentralo.service.EtiquetaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EtiquetaController.class)
@WithMockUser(roles = "USER")
class EtiquetaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EtiquetaService etiquetaService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    void crearEtiqueta_sinResultado_redirige() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(usuario);
        when(etiquetaService.guardarEtiqueta(1, "nueva-tag")).thenReturn(new Etiqueta("nueva-tag", usuario));

        mockMvc.perform(post("/etiqueta/crear")
                        .with(csrf())
                        .param("nombre", "nueva-tag")
                        .header("referer", "/buscar")
                        .principal(() -> "user@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buscar"));

        verify(etiquetaService).guardarEtiqueta(1, "nueva-tag");
    }

    @Test
    void crearEtiqueta_conResultado_etiquetaYRedirige() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(usuario);
        Etiqueta etiqueta = new Etiqueta("nueva-tag", usuario);
        etiqueta.setId(1);
        when(etiquetaService.guardarEtiqueta(1, "nueva-tag")).thenReturn(etiqueta);

        mockMvc.perform(post("/etiqueta/crear")
                        .with(csrf())
                        .param("nombre", "nueva-tag")
                        .param("resultadoId", "42")
                        .header("referer", "/resultados/1")
                        .principal(() -> "user@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/resultados/1"));

        verify(etiquetaService).etiquetarResultado(42, 1);
    }

    @Test
    void borrarEtiqueta_redirige() throws Exception {
        mockMvc.perform(post("/etiqueta/1/borrar")
                        .with(csrf())
                        .header("referer", "/resultados/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/resultados/1"));

        verify(etiquetaService).borrarEtiqueta(1);
    }

    @Test
    void agregarEtiquetaAResultado_redirige() throws Exception {
        mockMvc.perform(post("/resultado/1/etiqueta/2/agregar")
                        .with(csrf())
                        .header("referer", "/resultados/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/resultados/1"));

        verify(etiquetaService).etiquetarResultado(1, 2);
    }

    @Test
    void quitarEtiquetaDeResultado_redirige() throws Exception {
        mockMvc.perform(post("/resultado/1/etiqueta/2/quitar")
                        .with(csrf())
                        .header("referer", "/resultados/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/resultados/1"));

        verify(etiquetaService).desetiquetarResultado(1, 2);
    }
}
