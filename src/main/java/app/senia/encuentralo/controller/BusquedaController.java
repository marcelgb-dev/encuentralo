package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.model.Busqueda;
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

// Controlador que gestiona la página de búsqueda
@Controller
public class BusquedaController {

    private final YelpService ys;

    public BusquedaController(YelpService ys) {
        this.ys = ys;
    }

    // Página temporal index.html que contiene un objeto vacío de SolicitudBusqueda para
    // ser rellenado en el formulario
    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("busqueda", new SolicitudBusqueda());
        return "index";
    }

    // ENDPOINT DE ACCIÓN: Ejecuta la nueva búsqueda y redirige
    @PostMapping("/busqueda/new")
    public String makeSearch(@ModelAttribute("busqueda") SolicitudBusqueda busqueda) {

        // Ejecutamos la llamada a la API externa
        List<Resultado> resultadosApi = ys.llamarApi(
                busqueda.getTemino(),
                busqueda.getLatitud(),
                busqueda.getLongitud(),
                busqueda.getRadio(),
                busqueda.getLimite()
        ).getResultados();

        // Guardamos la Búsqueda y sus Resultados asociados en la Base de Datos.
        // Este método en tu Service debería devolver la entidad Busqueda ya persistida con su ID autogenerado.
        //Busqueda busquedaGuardada = busquedaService.guardarBusquedaConResultados(busqueda, resultadosApi);

        // Redirección limpia (PRG) pasándole el ID de la búsqueda por la URL
        return "redirect:/resultados/" + busquedaGuardada.getId();
    }





}
