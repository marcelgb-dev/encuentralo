package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.SolicitudBusqueda;
import app.senia.encuentralo.model.UserLocation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("busqueda", new SolicitudBusqueda());
        return "index";
    }

}