package app.senia.encuentralo.controller;

import app.senia.encuentralo.model.Etiqueta;
import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.UsuarioRepository;
import app.senia.encuentralo.service.EtiquetaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class EtiquetaController {

    private final EtiquetaService etiquetaService;
    private final UsuarioRepository usuarioRepository;

    public EtiquetaController(EtiquetaService etiquetaService, UsuarioRepository usuarioRepository) {
        this.etiquetaService = etiquetaService;
        this.usuarioRepository = usuarioRepository;
    }

    private Integer obtenerUsuarioId(Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName());
        return usuario != null ? usuario.getId() : 1;
    }

    @PostMapping("/etiqueta/crear")
    public String crearEtiqueta(@RequestParam("nombre") String nombre,
                                @RequestParam(value = "resultadoId", required = false) Integer resultadoId,
                                @RequestHeader(value = "referer", required = false, defaultValue = "/buscar") String referer,
                                Principal principal) {
        Etiqueta etiqueta = etiquetaService.guardarEtiqueta(obtenerUsuarioId(principal), nombre);
        if (resultadoId != null) {
            etiquetaService.etiquetarResultado(resultadoId, etiqueta.getId());
        }
        return "redirect:" + referer;
    }

    @PostMapping("/etiqueta/{etiquetaId}/borrar")
    public String borrarEtiqueta(@PathVariable Integer etiquetaId,
                                 @RequestHeader(value = "referer", required = false, defaultValue = "/buscar") String referer) {
        etiquetaService.borrarEtiqueta(etiquetaId);
        return "redirect:" + referer;
    }

    @PostMapping("/resultado/{resultadoId}/etiqueta/{etiquetaId}/agregar")
    public String agregarEtiquetaAResultado(@PathVariable Integer resultadoId,
                                             @PathVariable Integer etiquetaId,
                                             @RequestHeader(value = "referer", required = false, defaultValue = "/buscar") String referer) {
        etiquetaService.etiquetarResultado(resultadoId, etiquetaId);
        return "redirect:" + referer;
    }

    @PostMapping("/resultado/{resultadoId}/etiqueta/{etiquetaId}/quitar")
    public String quitarEtiquetaDeResultado(@PathVariable Integer resultadoId,
                                             @PathVariable Integer etiquetaId,
                                             @RequestHeader(value = "referer", required = false, defaultValue = "/buscar") String referer) {
        etiquetaService.desetiquetarResultado(resultadoId, etiquetaId);
        return "redirect:" + referer;
    }
}
