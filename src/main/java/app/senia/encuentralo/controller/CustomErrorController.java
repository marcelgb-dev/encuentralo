package app.senia.encuentralo.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Throwable exception = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        int codigo = 0;
        if (status != null) {
            codigo = Integer.parseInt(status.toString());
        }

        String mensaje;
        String descripcion;

        String mensajeReal = null;
        if (exception != null && exception.getMessage() != null && !exception.getMessage().isBlank()) {
            mensajeReal = exception.getMessage();
        } else if (message != null && !message.toString().isEmpty()) {
            mensajeReal = message.toString();
        }

        switch (codigo) {
            case 404:
                mensaje = "Página no encontrada";
                descripcion = "La página que buscas no existe o ha sido movida.";
                break;
            case 403:
                mensaje = "Acceso denegado";
                descripcion = "No tienes permiso para acceder a este recurso.";
                break;
            case 500:
                mensaje = mensajeReal != null ? mensajeReal : "Error interno del servidor";
                descripcion = mensajeReal != null
                        ? "Se produjo un error al procesar la solicitud."
                        : "Algo salió mal. Inténtalo de nuevo más tarde.";
                break;
            case 400:
                mensaje = mensajeReal != null ? mensajeReal : "Solicitud incorrecta";
                descripcion = mensajeReal != null
                        ? "Revisa los datos e intenta de nuevo."
                        : "La solicitud no pudo ser procesada. Revisa los datos e intenta de nuevo.";
                break;
            default:
                mensaje = mensajeReal != null ? mensajeReal : "Ha ocurrido un error inesperado";
                descripcion = mensajeReal != null
                        ? "Si el problema persiste, contacta con soporte."
                        : "Si el problema persiste, contacta con soporte.";
                break;
        }

        model.addAttribute("codigo", codigo);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("descripcion", descripcion);

        String homeUrl = "/buscar";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() &&
            auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            homeUrl = "/admin";
        }
        model.addAttribute("homeUrl", homeUrl);

        return "error";
    }

}
