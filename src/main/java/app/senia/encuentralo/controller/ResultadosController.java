package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.model.SolicitudBusqueda;
import app.senia.encuentralo.service.ResultadosService;
import app.senia.encuentralo.service.YelpService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador que gestiona el mostrar los resultados
@Controller
public class ResultadosController {

    private final YelpService ys;

    public ResultadosController(YelpService ys) {
        this.ys = ys;
    }

    // Recibe una SolicitudBusqueda, llama a la API mediante YelpService y
    // sirve los resultados a Thymeleaf en resultados.html
    @GetMapping("/resultados")
    public String mostrarResultados(
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "etiqueta", required = false) String etiqueta,
            @RequestParam(value = "orden", required = false, defaultValue = "default") String orden,
            Model model) {

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
}
