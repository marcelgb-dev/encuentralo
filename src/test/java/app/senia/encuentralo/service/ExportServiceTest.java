package app.senia.encuentralo.service;

import static org.assertj.core.api.Assertions.*;

import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Etiqueta;
import app.senia.encuentralo.model.Resultado;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

class ExportServiceTest {

    private final ExportService exportService = new ExportService();

    @Test
    void exportarResultadosCSV_incluyeBOM() {
        List<Resultado> resultados = List.of();
        byte[] csv = exportService.exportarResultadosCSV(resultados);
        assertThat(csv[0]).isEqualTo((byte) 0xEF);
        assertThat(csv[1]).isEqualTo((byte) 0xBB);
        assertThat(csv[2]).isEqualTo((byte) 0xBF);
    }

    @Test
    void exportarResultadosCSV_tieneHeadersCorrectos() {
        Resultado r = crearResultadoBasico();
        byte[] csv = exportService.exportarResultadosCSV(List.of(r));
        String contenido = new String(csv, StandardCharsets.UTF_8);
        assertThat(contenido).contains("\"ID\",\"Nombre\"");
    }

    @Test
    void exportarResultadosCSV_incluyeDatosDelResultado() {
        Resultado r = crearResultadoBasico();
        byte[] csv = exportService.exportarResultadosCSV(List.of(r));
        String contenido = new String(csv, StandardCharsets.UTF_8);
        assertThat(contenido).contains("Test Nombre");
        assertThat(contenido).contains("123456789");
        assertThat(contenido).contains("Calle Principal 123");
        assertThat(contenido).contains("4.5");
    }

    @Test
    void exportarResultadosCSV_incluyeCategoriasYEtiquetas() {
        Resultado r = crearResultadoBasico();
        byte[] csv = exportService.exportarResultadosCSV(List.of(r));
        String contenido = new String(csv, StandardCharsets.UTF_8);
        assertThat(contenido).contains("Comida; Bares");
        assertThat(contenido).contains("favorito; vip");
    }

    @Test
    void exportarResultadosCSV_sinCategoriasNiEtiquetas_noFalla() {
        Resultado r = new Resultado();
        r.setId(1);
        r.setNombre("Test");
        r.setTelefono("");
        r.setDireccion("");
        r.setNumReviews(0);
        r.setValoracion(0.0);
        r.setUrl("");

        byte[] csv = exportService.exportarResultadosCSV(List.of(r));
        String contenido = new String(csv, StandardCharsets.UTF_8);
        assertThat(contenido).contains("Test");
    }

    private Resultado crearResultadoBasico() {
        Resultado r = new Resultado();
        r.setId(1);
        r.setNombre("Test Nombre");
        r.setTelefono("123456789");
        r.setDireccion("Calle Principal 123");
        r.setNumReviews(42);
        r.setValoracion(4.5);
        r.setUrl("https://test.com");
        r.setCategorias(List.of(new Categoria("Comida"), new Categoria("Bares")));
        r.setEtiquetas(List.of(new Etiqueta("favorito", null), new Etiqueta("vip", null)));
        return r;
    }
}
