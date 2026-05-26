package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.service.BusquedaService;
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

    public ResultadosController(YelpService ys, BusquedaService bs, ResultadoService rs) {
        this.ys = ys;
        this.bs = bs;
        this.rs = rs;
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
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("resultados", busqueda.getResultados());

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
}
