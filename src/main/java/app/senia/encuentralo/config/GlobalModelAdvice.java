package app.senia.encuentralo.config;

import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelAdvice {

    private final UsuarioRepository usuarioRepository;

    public GlobalModelAdvice(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute
    public void addUserAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return;
        }

        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            String nombreCompleto = usuario.getNombre();
            if (usuario.getApellidos() != null && !usuario.getApellidos().isBlank()) {
                nombreCompleto += " " + usuario.getApellidos();
            }
            model.addAttribute("nombreUsuario", nombreCompleto);
            model.addAttribute("inicialUsuario", usuario.getNombre().substring(0, 1).toUpperCase());
        } else {
            model.addAttribute("nombreUsuario", email);
            model.addAttribute("inicialUsuario", email.substring(0, 1).toUpperCase());
        }
    }
}
