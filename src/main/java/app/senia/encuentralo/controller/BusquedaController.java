package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.dto.SolicitudBusqueda;
import app.senia.encuentralo.service.ResultadoService;
import app.senia.encuentralo.service.YelpService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// Controlador que gestiona la página de búsqueda
@Controller
public class BusquedaController {

    // Dependencias
    private final YelpService ys;
    private final ResultadoService rs;

    public BusquedaController(YelpService ys, ResultadoService rs) {
        this.ys = ys;
        this.rs = rs;
    }

    // Página temporal index.html que contiene un objeto vacío de SolicitudBusqueda para
    // ser rellenado en el formulario
    @GetMapping("/")
    public String getIndex(Model model) {
        // Búsqueda de ejemplo
        SolicitudBusqueda solicitud = new SolicitudBusqueda("Restaurante", 39.42166875024546, -0.41739016142493673, 10, 10000);

        model.addAttribute("busqueda", solicitud);
        return "index";
    }

    // ENDPOINT DE ACCIÓN: Ejecuta la nueva búsqueda y redirige
    @PostMapping("/buscar/nueva")
    public String nuevaBusqueda(@ModelAttribute("busqueda") SolicitudBusqueda input, RedirectAttributes redirectAttributes) {

        // Ejecutamos la llamada a la API externa, que nos devuelve la búsqueda (NO TIENE ID)
        Busqueda busqueda = ys.llamarApi(
                1,
                input.getTemino(),
                input.getLatitud(),
                input.getLongitud(),
                input.getRadio(),
                input.getLimite()
        );

        // Obtenemos la lista de resultados de la búsqueda
        List<Resultado> resultados = busqueda.getResultados();

        // TEMPORAL - Inyectamos la lista completa como un Flash Attribute
        // Vivirá en la sesión solo durante la redirección
        redirectAttributes.addFlashAttribute("resultados", resultados);
        redirectAttributes.addFlashAttribute("busqueda", busqueda);

        return "redirect:/resultados";

        // Guardamos la Búsqueda y sus Resultados asociados en la Base de Datos.
        // Este método en tu Service debería devolver la entidad Busqueda ya persistida con su ID autogenerado.
        //Busqueda busquedaGuardada = busquedaService.guardarBusquedaConResultados(busqueda, resultadosApi);

        // Redirección limpia (PRG) pasándole el ID de la búsqueda por la URL
        //return "redirect:/resultados/" + busquedaGuardada.getId();
    }





}
