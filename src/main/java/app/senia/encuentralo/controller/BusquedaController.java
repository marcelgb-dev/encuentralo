package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.dto.SolicitudBusqueda;
import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.UsuarioRepository;
import app.senia.encuentralo.service.BusquedaService;
import app.senia.encuentralo.service.ResultadoService;
import app.senia.encuentralo.service.YelpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

// Controlador que gestiona la página de búsqueda
@Controller
public class BusquedaController {

    @Value("${mock.json}")
    private boolean mocking = false;
    // Dependencias
    private final YelpService ys;
    private final ResultadoService rs;
    private final BusquedaService bs;
    private final UsuarioRepository usuarioRepo;

    @Value("${max.results}")
    private int maxResults;

    public BusquedaController(YelpService ys, ResultadoService rs, BusquedaService bs, UsuarioRepository usuarioRepo) {
        this.ys = ys;
        this.rs = rs;
        this.bs = bs;
        this.usuarioRepo = usuarioRepo;
    }

    private Integer obtenerUsuarioId(Principal principal) {
        Usuario usuario = usuarioRepo.findByEmail(principal.getName());
        return usuario != null ? usuario.getId() : 1;
    }

    // Redirect básico temporal
    @GetMapping("/")
    public String indice() {
        return "redirect:/buscar";
    }

    @GetMapping("/buscar")
    public String getIndex(Model model) {
        // Búsqueda de ejemplo
        SolicitudBusqueda solicitud = new SolicitudBusqueda("Restaurante", 0, 0, maxResults, 20000);
        model.addAttribute("busqueda", solicitud);
        model.addAttribute("testMode", mocking);
        return "busquedas";
    }

    // ENDPOINT DE ACCIÓN: Ejecuta la nueva búsqueda y redirige
    @PostMapping("/buscar/nueva")
    public String nuevaBusqueda(@ModelAttribute("busqueda") SolicitudBusqueda input, RedirectAttributes redirectAttributes, Principal principal) {

        // Ejecutamos la llamada a la API externa, que nos devuelve la búsqueda (NO TIENE ID)
        Busqueda busqueda;
        try {
            busqueda = ys.llamarApi(
                    obtenerUsuarioId(principal),
                    input.getTermino(),
                    input.getLatitud(),
                    input.getLongitud(),
                    input.getRadio(),
                    input.getLimite()
            );
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorBusqueda", e.getMessage());
            return "redirect:/buscar";
        }

        System.out.println("Realizando búsqueda para coords: " + input.getLatitud() + "lat, " + input.getLongitud() + "long");
        System.out.println("Máximo de resultados: " + input.getLimite());

        // Guardamos la Búsqueda y sus Resultados asociados en la Base de Datos.
        Busqueda busquedaGuardada = bs.guardarBusqueda(busqueda);

        // Redirección limpia (PRG) pasándole el ID de la búsqueda por la URL
        return "redirect:/resultados/" + busquedaGuardada.getId();

    }

    // TEMPORAL - GET mapping para pruebas rápidas, pero habrá que pasarle un parametro con POST luego
    @GetMapping("/historial")
    public String mostrarHistorial(Model model, Principal principal) {

        List<Busqueda> historial = bs.obtenerHistorial(obtenerUsuarioId(principal));
        model.addAttribute("busquedas", historial);

        return "historial";
    }


}
