package app.senia.encuentralo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Etiqueta;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.UsuarioRepository;
import app.senia.encuentralo.service.BusquedaService;
import app.senia.encuentralo.service.CategoriaService;
import app.senia.encuentralo.service.EtiquetaService;
import app.senia.encuentralo.service.ExportService;
import app.senia.encuentralo.service.ResultadoService;
import app.senia.encuentralo.service.YelpService;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ResultadosController.class)
@WithMockUser(roles = "USER")
class ResultadosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private YelpService yelpService;

    @MockitoBean
    private BusquedaService busquedaService;

    @MockitoBean
    private ResultadoService resultadoService;

    @MockitoBean
    private CategoriaService categoriaService;

    @MockitoBean
    private ExportService exportService;

    @MockitoBean
    private EtiquetaService etiquetaService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    void mostrarResultados_sinFiltros_retornaVistaYModelo() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(usuario);

        Busqueda busqueda = new Busqueda();
        busqueda.setId(1);
        busqueda.setResultados(List.of(new Resultado()));

        when(busquedaService.obtenerBusqueda(1)).thenReturn(busqueda);
        when(categoriaService.obtenerCategorias(anyList())).thenReturn(List.of(new Categoria("Test")));
        when(etiquetaService.obtenerEtiquetasUsuario(1)).thenReturn(List.of());

        mockMvc.perform(get("/resultados/1")
                        .with(csrf())
                        .principal(() -> "user@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("resultados"))
                .andExpect(model().attributeExists("resultados", "busqueda", "categorias", "etiquetas"));
    }

    @Test
    void mostrarResultados_conFiltros_pasaParamsAlServicio() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(usuario);

        Resultado r = new Resultado();
        r.setId(1);
        Busqueda busqueda = new Busqueda();
        busqueda.setId(1);
        busqueda.setResultados(List.of(r));

        when(busquedaService.obtenerBusqueda(1)).thenReturn(busqueda);
        when(categoriaService.obtenerCategorias(anyList())).thenReturn(List.of());
        when(etiquetaService.obtenerEtiquetasUsuario(1)).thenReturn(List.of());

        mockMvc.perform(get("/resultados/1")
                        .param("categorias", "Comida", "Bares")
                        .param("orden", "valoracion")
                        .param("inverso", "true")
                        .param("soloFavoritos", "true")
                        .param("valoracionMinima", "3")
                        .principal(() -> "user@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("resultados"))
                .andExpect(model().attribute("soloFavoritos", true))
                .andExpect(model().attribute("valoracionMinima", 3));
    }

    @Test
    void exportarResultadosCSV_devuelveCSV() throws Exception {
        Busqueda busqueda = new Busqueda();
        busqueda.setId(1);
        busqueda.setResultados(List.of());

        when(busquedaService.obtenerBusqueda(1)).thenReturn(busqueda);
        when(exportService.exportarResultadosCSV(anyList())).thenReturn(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 65, 66, 67});

        mockMvc.perform(get("/resultados/1/exportar/csv"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "text/csv"))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resultados-1.csv\"; filename*=UTF-8''resultados-1.csv"));
    }

    @Test
    void mostrarFavoritos_retornaVistaYModelo() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(usuario);
        when(resultadoService.obtenerResultadosFavoritos(1)).thenReturn(List.of());
        when(categoriaService.obtenerCategorias(anyList())).thenReturn(List.of());
        when(etiquetaService.obtenerEtiquetasUsuario(1)).thenReturn(List.of());

        mockMvc.perform(get("/favoritos")
                        .principal(() -> "user@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("favoritos"))
                .andExpect(model().attributeExists("resultados", "categorias", "etiquetas"));
    }

    @Test
    void marcarFavorito_redirige() throws Exception {
        mockMvc.perform(post("/favoritos/toggle/1")
                        .with(csrf())
                        .header("referer", "/resultados/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/resultados/1"));

        verify(resultadoService).guardarFavorito(1);
    }
}
