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

@Controller
public class BusquedaController {

    private final YelpService ys;

    public BusquedaController(YelpService ys) {
        this.ys = ys;
    }

    @PostMapping("/buscar")
    public String makeSearch(@ModelAttribute("busqueda") SolicitudBusqueda busqueda, Model model) {

        String termino = busqueda.getTemino();
        double latitud = busqueda.getLatitud();
        double longitud = busqueda.getLongitud();
        int radio = busqueda.getRadio();
        int limite = busqueda.getLimite();


        List<Resultado> resultados = ys.llamarApi(termino, latitud, longitud, radio, limite).getResultados();

        System.out.println("Coordenadas: " + latitud + " lat / " + longitud + " long" );

        System.out.println("Resultados: \n");
        for (Resultado r : resultados) {
            System.out.println(r);
        }

        model.addAttribute("resultados", resultados);

        return "resultados";
    }



}
