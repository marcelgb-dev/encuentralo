package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.service.BusquedaService;
import app.senia.encuentralo.service.CategoriaService;
import app.senia.encuentralo.service.ResultadoService;
import app.senia.encuentralo.service.YelpService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador que gestiona el mostrar los resultados
@Controller
public class ResultadosController {

    private final YelpService ys;
    private final BusquedaService bs;
    private final ResultadoService rs;
    private final CategoriaService cs;

    public ResultadosController(YelpService ys, BusquedaService bs, ResultadoService rs, CategoriaService cs) {
        this.ys = ys;
        this.bs = bs;
        this.rs = rs;
        this.cs = cs;
    }

    // Recibe una SolicitudBusqueda, llama a la API mediante YelpService y
    // sirve los resultados a Thymeleaf en resultados.html
    @GetMapping("/resultados/{busquedaId}")
    public String mostrarResultados(
            @PathVariable Integer busquedaId,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "etiqueta", required = false) String etiqueta,
            @RequestParam(value = "orden", required = false, defaultValue = "default") String orden,
            Model model) {

        Busqueda busqueda = bs.obtenerBusqueda(busquedaId);
        List<Resultado> resultados = busqueda.getResultados();

        List<Categoria> categorias = cs.obtenerCategorias(resultados);


        model.addAttribute("busqueda", busqueda);
        model.addAttribute("resultados", resultados);
        model.addAttribute("categorias", categorias);

        // Ordenación
        switch (orden) {
            case "distancia":
                // Ordenar por distancia
                break;
            case "valoracion":
                // Ordenar por valoración
                break;
            case "nombre":
                // Ordenar por nombre
                break;
            default:
                // Ordenar por defecto
                break;
        }

        // Devolvemos el nombre del html resultados.html
        return "resultados";
    }

    @GetMapping("/favoritos")
    public String mostrarFavoritos(
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "etiqueta", required = false) String etiqueta,
            @RequestParam(value = "orden", required = false, defaultValue = "default") String orden,
            Model model) {

        List<Resultado> resultados = rs.obtenerResultadosFavoritos(1);


        model.addAttribute("resultados", resultados);

        // Ordenación
        switch (orden) {
            case "distancia":
                // Ordenar por distancia
                break;
            case "valoracion":
                // Ordenar por valoración
                break;
            case "nombre":
                // Ordenar por nombre
                break;
            default:
                // Ordenar por defecto
                break;
        }

        // Devolvemos el nombre del html resultados.html
        return "favoritos";
    }

    @PostMapping("/favoritos/toggle/{resultadoId}")
    public String marcarFavorito(@PathVariable ("resultadoId") Integer resultadoId,
                                 @RequestHeader(value = "referer", required = false, defaultValue = "/") String referer) {

        rs.guardarFavorito(resultadoId);

        return "redirect:" + referer;
    }
}
