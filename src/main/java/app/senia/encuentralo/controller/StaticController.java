package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.SolicitudBusqueda;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Controlador para las páginas estáticas / pruebas (Ej. index.html)
@Controller
public class StaticController {

    // Página temporal index.html que contiene un objeto vacío de SolicitudBusqueda para
    // ser rellenado en el formulario
    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("busqueda", new SolicitudBusqueda());
        return "index";
    }

}