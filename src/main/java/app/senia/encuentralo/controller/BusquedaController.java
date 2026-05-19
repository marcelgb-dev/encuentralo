package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.model.SolicitudBusqueda;
import app.senia.encuentralo.service.YelpService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

// Controlador que gestiona todo lo relacionado con peticiones de búsqueda y mostrar resultados
@Controller
public class BusquedaController {

    private final YelpService ys;

    public BusquedaController(YelpService ys) {
        this.ys = ys;
    }

    // Recibe una SolicitudBusqueda, llama a la API mediante YelpService y
    // sirve los resultados a Thymeleaf en resultados.html
    @PostMapping("/buscar")
    public String makeSearch(@ModelAttribute("busqueda") SolicitudBusqueda busqueda, Model model) {

        // Creamos las variables necesarias para la API en base a la búsqueda
        String termino = busqueda.getTemino();
        double latitud = busqueda.getLatitud();
        double longitud = busqueda.getLongitud();
        int radio = busqueda.getRadio();
        int limite = busqueda.getLimite();

        // Guardamos los resultados de la API
        List<Resultado> resultados = ys.llamarApi(termino, latitud, longitud, radio, limite).getResultados();

        // Mostramos las coordenadas del usuario
        System.out.println("Coordenadas: " + latitud + " lat / " + longitud + " long" );

        // Imprimimos los resultados de la query por pantalla
        System.out.println("Resultados: \n");
        for (Resultado r : resultados) {
            System.out.println(r);
        }

        // Le pasamos los datos al modelo de Thymeleaf en una lista
        model.addAttribute("resultados", resultados);

        // Devolvemos el nombre del html resultados.html
        return "resultados";
    }



}
