package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 1. RUTA PARA MOSTRAR LA PÁGINA DE REGISTRO (GET)
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        // Creamos un objeto Usuario vacío y se lo mandamos a la plantilla HTML.
        // Thymeleaf lo usará para "atar" los inputs del formulario a este objeto.
        model.addAttribute("usuario", new Usuario());

        return "registro"; // Esto buscará un archivo llamado 'registro.html' en
                           // src/main/resources/templates/
    }

    // 2. RUTA PARA PROCESAR LOS DATOS ENVIADOS DESDE EL HTML (POST)
    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute("usuario") Usuario usuario, Model model) {
        try {
            // Le pedimos al servicio que haga su magia (validar y guardar)
            usuarioService.nuevoUsuario(usuario);

            // Si todo va bien, lo redirigimos a la pantalla de login con un mensaje de
            // éxito
            return "redirect:/login?exito";

        } catch (Exception e) {
            // Si el servicio lanza una excepción (por ejemplo, email repetido),
            // capturamos el error, lo metemos en el modelo para mostrarlo en el HTML...
            model.addAttribute("error", e.getMessage());

            // ... y lo dejamos en la misma página de registro para que lo intente de nuevo
            return "registro";
        }
    }
}