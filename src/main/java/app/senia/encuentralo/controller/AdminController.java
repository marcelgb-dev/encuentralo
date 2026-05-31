package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import app.senia.encuentralo.service.UsuarioService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class AdminController {

    private final UsuarioService us;
    private final UsuarioRepository usuarioRepository;

    public AdminController(UsuarioService us, UsuarioRepository usuarioRepository) {
        this.us = us;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/admin")
    public String showAdminPanel(Model model, Principal principal) {

        List<Usuario> usuarios = us.obtenerListaUsuarios();
        model.addAttribute("usuarios", usuarios);

        String email = principal.getName();
        Usuario logueado = usuarioRepository.findByEmail(email);
        if (logueado != null) {
            String nombreCompleto = logueado.getNombre();
            if (logueado.getApellidos() != null && !logueado.getApellidos().isBlank()) {
                nombreCompleto += " " + logueado.getApellidos();
            }
            model.addAttribute("nombreUsuario", nombreCompleto);
            model.addAttribute("inicialUsuario", logueado.getNombre().substring(0, 1).toUpperCase());
        } else {
            model.addAttribute("nombreUsuario", email);
            model.addAttribute("inicialUsuario", email.substring(0, 1).toUpperCase());
        }

        return "panel_admin";
    }

    @GetMapping("/editar_usuario")
    public String mostrarEditarUsuario(@RequestParam("id") Integer id, Model model) {
        return us.obtenerUsuario(id).map(usuario -> {
            model.addAttribute("usuario", usuario);
            return "editar_usuario";
        }).orElse("redirect:/admin");
    }

    @PostMapping("/editar_usuario")
    public String guardarEdicionUsuario(@ModelAttribute("usuario") Usuario usuario) {
        us.editarUsuario(usuario.getId(), usuario);
        return "redirect:/admin";
    }

    @GetMapping("/admin/nuevo")
    public String nuevoUsuario(Model model) {
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

    @PostMapping("/eliminar_usuario")
    public String eliminarUsuario(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes,
                                  Principal principal) {
        Optional<Usuario> opt = us.obtenerUsuario(id);
        if (opt.isPresent()) {
            if (principal.getName().equals(opt.get().getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No puedes eliminar tu propia cuenta");
            }
            String nombre = opt.get().getNombre();
            if (opt.get().getApellidos() != null && !opt.get().getApellidos().isBlank()) {
                nombre += " " + opt.get().getApellidos();
            }
            us.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("eliminado", nombre);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/guardar")
    public String guardarUsuario(@ModelAttribute("user") Usuario usuario) {

        if (usuario.getId() == 0) {
            try {
                us.nuevoUsuario(usuario);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            us.editarUsuario(usuario.getId(), usuario);
        }

        return "redirect:/admin";
    }
}
