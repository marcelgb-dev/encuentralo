package app.senia.encuentralo.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        int codigo = 0;
        if (status != null) {
            codigo = Integer.parseInt(status.toString());
        }

        String mensaje;
        String descripcion;

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
                mensaje = "Error interno del servidor";
                descripcion = "Algo salió mal. Inténtalo de nuevo más tarde.";
                break;
            case 400:
                mensaje = "Solicitud incorrecta";
                descripcion = "La solicitud no pudo ser procesada. Revisa los datos e intenta de nuevo.";
                break;
            default:
                mensaje = "Ha ocurrido un error inesperado";
                descripcion = (message != null && !message.toString().isEmpty())
                        ? message.toString()
                        : "Si el problema persiste, contacta con soporte.";
                break;
        }

        model.addAttribute("codigo", codigo);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("descripcion", descripcion);

        return "error";
    }

}
