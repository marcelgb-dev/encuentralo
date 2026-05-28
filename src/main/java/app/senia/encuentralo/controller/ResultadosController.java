package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.service.BusquedaService;
import app.senia.encuentralo.service.CategoriaService;
import app.senia.encuentralo.service.EtiquetaService;
import app.senia.encuentralo.service.ExportService;
import app.senia.encuentralo.service.ResultadoService;
import app.senia.encuentralo.service.YelpService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

// Controlador que gestiona el mostrar los resultados
@Controller
public class ResultadosController {

    private final YelpService ys;
    private final BusquedaService bs;
    private final ResultadoService rs;
    private final CategoriaService cs;
    private final ExportService exportService;
    private final EtiquetaService es;

    public ResultadosController(YelpService ys, BusquedaService bs, ResultadoService rs, CategoriaService cs, ExportService exportService, EtiquetaService es) {
        this.ys = ys;
        this.bs = bs;
        this.rs = rs;
        this.cs = cs;
        this.exportService = exportService;
        this.es = es;
    }

    // Recibe una SolicitudBusqueda, llama a la API mediante YelpService y
    // sirve los resultados a Thymeleaf en resultados.html
    @GetMapping("/resultados/{busquedaId}")
    public String mostrarResultados(
            @PathVariable Integer busquedaId,
            @RequestParam(value = "categorias", required = false) List<String> filtroCategorias,
            @RequestParam(value = "etiqueta", required = false) String etiqueta,
            @RequestParam(value = "orden", required = false, defaultValue = "default") String orden,
            @RequestParam(value = "inverso", required = false, defaultValue = "false") boolean inverso,
            @RequestParam(value = "soloFavoritos", required = false, defaultValue = "false") boolean soloFavoritos,
            @RequestParam(value = "valoracionMinima", required = false, defaultValue = "0") int valoracionMinima,
            Model model) {

        Busqueda busqueda = bs.obtenerBusqueda(busquedaId);
        List<Resultado> resultados = busqueda.getResultados();
        List<Categoria> categorias = cs.obtenerCategorias(resultados);



        // Filtros
        if (filtroCategorias != null) {
            resultados = rs.filtrarPorCategoria(resultados, filtroCategorias);
            model.addAttribute("categoriasSeleccionadas", filtroCategorias);
        }
        if (soloFavoritos == true) {
            resultados = rs.filtrarSoloFavoritos(resultados);
        }
        if (valoracionMinima > 0)
            resultados = rs.filtrarPorValoracion(resultados, valoracionMinima);

        model.addAttribute("soloFavoritos", soloFavoritos);
        model.addAttribute("valoracionMinima", valoracionMinima);





        // Ordenación
        switch (orden) {
            case "distancia":
                // Ordenar por distancia
                rs.ordenarPorDistancia(resultados, inverso);
                break;
            case "valoracion":
                // Ordenar por valoración
                rs.ordenarPorValoracion(resultados, inverso);
                break;
            case "alfabeticamente":
                // Ordenar por nombre
                rs.ordenarPorNombre(resultados, inverso);
                break;
            default:
                // Ordenar por defecto
                if (inverso)
                    Collections.reverse(resultados);
                break;
        }

        model.addAttribute("resultados", resultados);
        model.addAttribute("orden", orden);
        model.addAttribute("inverso", inverso);
        model.addAttribute("etiqueta", etiqueta);

        model.addAttribute("busqueda", busqueda);
        model.addAttribute("resultados", resultados);
        model.addAttribute("categorias", categorias);
        model.addAttribute("etiquetas", es.obtenerEtiquetasUsuario(1));


        // Devolvemos el nombre del html resultados.html
        return "resultados";
    }

    @GetMapping("/resultados/{busquedaId}/exportar/csv")
    public ResponseEntity<byte[]> exportarResultadosCSV(
            @PathVariable Integer busquedaId,
            @RequestParam(value = "categorias", required = false) List<String> filtroCategorias,
            @RequestParam(value = "etiqueta", required = false) String etiqueta,
            @RequestParam(value = "orden", required = false, defaultValue = "default") String orden,
            @RequestParam(value = "inverso", required = false, defaultValue = "false") boolean inverso,
            @RequestParam(value = "soloFavoritos", required = false, defaultValue = "false") boolean soloFavoritos,
            @RequestParam(value = "valoracionMinima", required = false, defaultValue = "0") int valoracionMinima) {

        Busqueda busqueda = bs.obtenerBusqueda(busquedaId);
        List<Resultado> resultados = busqueda.getResultados();

        if (filtroCategorias != null) {
            resultados = rs.filtrarPorCategoria(resultados, filtroCategorias);
        }
        if (soloFavoritos) {
            resultados = rs.filtrarSoloFavoritos(resultados);
        }
        if (valoracionMinima > 0) {
            resultados = rs.filtrarPorValoracion(resultados, valoracionMinima);
        }

        switch (orden) {
            case "distancia":
                rs.ordenarPorDistancia(resultados, inverso);
                break;
            case "valoracion":
                rs.ordenarPorValoracion(resultados, inverso);
                break;
            case "alfabeticamente":
                rs.ordenarPorNombre(resultados, inverso);
                break;
            default:
                if (inverso)
                    Collections.reverse(resultados);
                break;
        }

        byte[] csv = exportService.exportarResultadosCSV(resultados);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("resultados-" + busquedaId + ".csv", StandardCharsets.UTF_8).build());

        return new ResponseEntity<>(csv, headers, HttpStatus.OK);
    }

    @GetMapping("/favoritos")
    public String mostrarFavoritos(
            @RequestParam(value = "categorias", required = false) List<String> filtroCategorias,
            @RequestParam(value = "etiqueta", required = false) String etiqueta,
            @RequestParam(value = "orden", required = false, defaultValue = "default") String orden,
            @RequestParam(value = "inverso", required = false, defaultValue = "false") boolean inverso,
            @RequestParam(value = "valoracionMinima", required = false, defaultValue = "0") int valoracionMinima,            
            Model model) {




        List<Resultado> resultados = rs.obtenerResultadosFavoritos(1);
        List<Categoria> categorias = cs.obtenerCategorias(resultados);



        // Filtros
        if (filtroCategorias != null) {
            resultados = rs.filtrarPorCategoria(resultados, filtroCategorias);
            model.addAttribute("categoriasSeleccionadas", filtroCategorias);
        }
        if (valoracionMinima > 0)
            resultados = rs.filtrarPorValoracion(resultados, valoracionMinima);

        model.addAttribute("valoracionMinima", valoracionMinima);

        // Ordenación
        switch (orden) {
            case "valoracion":
                // Ordenar por valoración
                rs.ordenarPorValoracion(resultados, inverso);
                break;
            case "alfabeticamente":
                // Ordenar por nombre
                rs.ordenarPorNombre(resultados, inverso);
                break;
            default:
                // Ordenar por defecto
                if (inverso)
                    Collections.reverse(resultados);
                break;
        }

        model.addAttribute("resultados", resultados);
        model.addAttribute("orden", orden);
        model.addAttribute("inverso", inverso);
        model.addAttribute("etiqueta", etiqueta);

        model.addAttribute("resultados", resultados);
        model.addAttribute("categorias", categorias);
        model.addAttribute("etiquetas", es.obtenerEtiquetasUsuario(1));

        // Devolvemos el nombre del html resultados.html
        return "favoritos";
    }

    @GetMapping("/favoritos/exportar/csv")
    public ResponseEntity<byte[]> exportarFavoritosCSV(
            @RequestParam(value = "categorias", required = false) List<String> filtroCategorias,
            @RequestParam(value = "etiqueta", required = false) String etiqueta,
            @RequestParam(value = "orden", required = false, defaultValue = "default") String orden,
            @RequestParam(value = "inverso", required = false, defaultValue = "false") boolean inverso,
            @RequestParam(value = "valoracionMinima", required = false, defaultValue = "0") int valoracionMinima) {

        List<Resultado> resultados = rs.obtenerResultadosFavoritos(1);

        if (filtroCategorias != null) {
            resultados = rs.filtrarPorCategoria(resultados, filtroCategorias);
        }
        if (valoracionMinima > 0) {
            resultados = rs.filtrarPorValoracion(resultados, valoracionMinima);
        }

        switch (orden) {
            case "valoracion":
                rs.ordenarPorValoracion(resultados, inverso);
                break;
            case "alfabeticamente":
                rs.ordenarPorNombre(resultados, inverso);
                break;
            default:
                if (inverso)
                    Collections.reverse(resultados);
                break;
        }

        byte[] csv = exportService.exportarResultadosCSV(resultados);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("favoritos.csv", StandardCharsets.UTF_8).build());

        return new ResponseEntity<>(csv, headers, HttpStatus.OK);
    }

    @PostMapping("/favoritos/toggle/{resultadoId}")
    public String marcarFavorito(@PathVariable ("resultadoId") Integer resultadoId,
                                 @RequestHeader(value = "referer", required = false, defaultValue = "/") String referer) {

        rs.guardarFavorito(resultadoId);

        return "redirect:" + referer;
    }
}
