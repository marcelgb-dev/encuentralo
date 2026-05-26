package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.dto.SolicitudBusqueda;
import app.senia.encuentralo.service.BusquedaService;
import app.senia.encuentralo.service.ResultadoService;
import app.senia.encuentralo.service.YelpService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// Controlador que gestiona la página de búsqueda
@Controller
public class BusquedaController {

    // Dependencias
    private final YelpService ys;
    private final ResultadoService rs;
    private final BusquedaService bs;

    public BusquedaController(YelpService ys, ResultadoService rs, BusquedaService bs) {
        this.ys = ys;
        this.rs = rs;
        this.bs = bs;
    }

    // Redirect básico temporal
    @GetMapping("/")
    public String indice() {
        return "redirect:/buscar";
    }

    @GetMapping("/buscar")
    public String getIndex(Model model) {
        // Búsqueda de ejemplo
        SolicitudBusqueda solicitud = new SolicitudBusqueda("Restaurante", 0, 0, 10, 20000);
        model.addAttribute("busqueda", solicitud);
        model.addAttribute("testMode", true);
        return "busquedass";
    }

    // ENDPOINT DE ACCIÓN: Ejecuta la nueva búsqueda y redirige
    @PostMapping("/buscar/nueva")
    public String nuevaBusqueda(@ModelAttribute("busqueda") SolicitudBusqueda input, RedirectAttributes redirectAttributes) {

        // Ejecutamos la llamada a la API externa, que nos devuelve la búsqueda (NO TIENE ID)
        Busqueda busqueda = ys.llamarApi(
                1,
                input.getTermino(),
                input.getLatitud(),
                input.getLongitud(),
                input.getRadio(),
                input.getLimite()
        );

        System.out.println("Realizando búsqueda para coords: " + input.getLatitud() + "lat, " + input.getLongitud() + "long");

        // Guardamos la Búsqueda y sus Resultados asociados en la Base de Datos.
        Busqueda busquedaGuardada = bs.guardarBusqueda(busqueda);

        // Redirección limpia (PRG) pasándole el ID de la búsqueda por la URL
        return "redirect:/resultados/" + busquedaGuardada.getId();

    }

    // TEMPORAL - GET mapping para pruebas rápidas, pero habrá que pasarle un parametro con POST luego
    @GetMapping("/historial")
    public String mostrarHistorial(/*@RequestParam("idUsuario") Integer idUsuario,*/ Model model) {

        List<Busqueda> historial = bs.obtenerHistorial(1);
        model.addAttribute("busquedas", historial);

        return "historial_busquedas_thymeleaf";
    }


}
