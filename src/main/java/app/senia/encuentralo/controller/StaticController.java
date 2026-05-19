package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.SolicitudBusqueda;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("busqueda", new SolicitudBusqueda());
        return "index";
    }

}