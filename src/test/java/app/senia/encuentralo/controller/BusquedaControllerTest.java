package app.senia.encuentralo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.UsuarioRepository;
import app.senia.encuentralo.service.BusquedaService;
import app.senia.encuentralo.service.ResultadoService;
import app.senia.encuentralo.service.YelpService;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BusquedaController.class)
@WithMockUser(username = "user@test.com", roles = "USER")
class BusquedaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private YelpService yelpService;

    @MockitoBean
    private ResultadoService resultadoService;

    @MockitoBean
    private BusquedaService busquedaService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("user@test.com", "pass", "Test", "User", "USER");
        usuario.setId(1);
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(usuario);
    }

    @Test
    void getIndex_retornaVistaBusquedas() throws Exception {
        mockMvc.perform(get("/buscar"))
                .andExpect(status().isOk())
                .andExpect(view().name("busquedas"))
                .andExpect(model().attributeExists("busqueda"));
    }

    @Test
    void indice_redirigeABuscar() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buscar"));
    }

    @Test
    void nuevaBusqueda_llamaApiYRedirige() throws Exception {
        Busqueda busqueda = new Busqueda("test", LocalDateTime.now(), usuario, "Ciudad");
        busqueda.setId(42);
        Busqueda busquedaGuardada = new Busqueda("test", LocalDateTime.now(), usuario, "Ciudad");
        busquedaGuardada.setId(42);

        when(yelpService.llamarApi(anyInt(), anyString(), anyDouble(), anyDouble(), anyInt(), anyInt())).thenReturn(busqueda);
        when(busquedaService.guardarBusqueda(any(Busqueda.class))).thenReturn(busquedaGuardada);

        mockMvc.perform(post("/buscar/nueva")
                        .with(csrf())
                        .param("termino", "Restaurante"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/resultados/42"));

        verify(yelpService).llamarApi(anyInt(), anyString(), anyDouble(), anyDouble(), anyInt(), anyInt());
        verify(busquedaService).guardarBusqueda(any(Busqueda.class));
    }

    @Test
    void mostrarHistorial_retornaVista() throws Exception {
        when(busquedaService.obtenerHistorial(1)).thenReturn(List.of());

        mockMvc.perform(get("/historial"))
                .andExpect(status().isOk())
                .andExpect(view().name("historial"))
                .andExpect(model().attributeExists("busquedas"));
    }
}
