package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.model.SolicitudBusqueda;
import app.senia.encuentralo.service.ResultadosService;
import app.senia.encuentralo.service.YelpService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// Controlador que gestiona el mostrar los resultados
public class ResultadosController {

    private final YelpService ys;

    public ResultadosController(YelpService ys) {
        this.ys = ys;
    }

    // Recibe una SolicitudBusqueda, llama a la API mediante YelpService y
    // sirve los resultados a Thymeleaf en resultados.html
    @PostMapping("/resultados")
    public String mostrarResultados(
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "etiqueta", required = false) String etiqueta,
            @RequestParam(value = "orden", required = false, defaultValue = "distancia") String orden,
            Model model) {

        // Devolvemos el nombre del html resultados.html
        return "resultados";
    }
}
