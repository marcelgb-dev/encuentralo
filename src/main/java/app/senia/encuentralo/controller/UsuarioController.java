package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

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

    @GetMapping("/cambiar_contrasena")
    public String mostrarCambiarContrasena() {
        return "password";
    }

    @PostMapping("/cambiar_contrasena")
    public String procesarCambiarContrasena(
            @RequestParam("contrasenaAntigua") String contrasenaAntigua,
            @RequestParam("contrasenaNueva") String contrasenaNueva,
            @RequestParam("contrasenaVerificar") String contrasenaVerificar,
            Principal principal, Model model) {

        if (!contrasenaNueva.equals(contrasenaVerificar)) {
            model.addAttribute("error", "Las contraseñas nuevas no coinciden.");
            return "password";
        }
        if (contrasenaNueva.isBlank()) {
            model.addAttribute("error", "La nueva contraseña no puede estar vacía.");
            return "password";
        }

        try {
            usuarioService.cambiarContrasena(principal.getName(), contrasenaAntigua, contrasenaNueva);
            return "redirect:/buscar";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "password";
        }
    }
}
