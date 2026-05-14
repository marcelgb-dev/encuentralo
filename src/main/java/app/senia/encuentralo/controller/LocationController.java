package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.UserLocation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class LocationController {

    @PostMapping("/location")
    public String getUserLocation(@ModelAttribute("coordenadas") UserLocation coordenadas) {

        coordenadas.setFechaHora(LocalDateTime.now());

        System.out.println(coordenadas);

        return "redirect:/";
    }


}
