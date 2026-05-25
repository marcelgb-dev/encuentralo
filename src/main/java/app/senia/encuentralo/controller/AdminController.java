package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Usuario;
import org.springframework.stereotype.Controller;
import app.senia.encuentralo.service.UsuarioService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class AdminController {

    private final UsuarioService us;

    public AdminController(UsuarioService us) {
        this.us = us;
    }

    @GetMapping("/admin")
    public String showAdminPanel(Model model) {

        List <Usuario> usuarios = us.obtenerListaUsuarios();

        model.addAttribute("usuarios", usuarios);

        return "panel_admin";
    }

    @GetMapping("/admin/nuevo")
    public String nuevoUsuario(Model model) {

        // Creamos un objeto Usuario vacío y se lo pasamos a Thymeleaf para que lo rellene
        model.addAttribute("usuario", new Usuario());

        return "usuario_nuevo";
    }

    @PostMapping("/admin/editar")
    public String editarUsuario(@RequestParam("idUsuario") Integer idUsuario, Model model) {

        Usuario usuario;

        try {
            usuario = us.obtenerUsuario(idUsuario).get();
            model.addAttribute("usuario", usuario);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        return "usuario_editar";
    }

    @PostMapping("/admin/guardar")
    public String guardarUsuario(@ModelAttribute("user") Usuario usuario) {

        // Si el objeto usuario tiene ID = 0, no existe en la BD así que lo creamos como nuevo
        if (usuario.getId() == 0)
            us.nuevoUsuario(usuario);
        // Si el usuario ya existía al llamar a esta función, editamos sus datos
        else
            us.editarUsuario(usuario.getId(), usuario);

        // Redirigimos a la lista de usuarios al acabar de editar
        return "redirect:/admin";
    }
}
