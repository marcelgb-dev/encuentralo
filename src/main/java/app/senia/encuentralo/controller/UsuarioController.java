package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "frontend/login";
    }

    @GetMapping("/registrar_usuario")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registrar_usuario")
    public String procesarRegistro(@ModelAttribute("usuario") Usuario usuario, Model model) {
        try {
            usuarioService.nuevoUsuario(usuario);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }
}
