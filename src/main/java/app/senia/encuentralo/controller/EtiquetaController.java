package app.senia.encuentralo.controller;

import app.senia.encuentralo.service.EtiquetaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EtiquetaController {

    private final EtiquetaService etiquetaService;

    public EtiquetaController(EtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    @PostMapping("/etiqueta/crear")
    public String crearEtiqueta(@RequestParam("nombre") String nombre,
                                @RequestHeader("referer") String referer) {
        etiquetaService.guardarEtiqueta(1, nombre);
        return "redirect:" + referer;
    }

    @PostMapping("/etiqueta/{etiquetaId}/borrar")
    public String borrarEtiqueta(@PathVariable Integer etiquetaId,
                                 @RequestHeader("referer") String referer) {
        etiquetaService.borrarEtiqueta(etiquetaId);
        return "redirect:" + referer;
    }

    @PostMapping("/resultado/{resultadoId}/etiqueta/{etiquetaId}/agregar")
    public String agregarEtiquetaAResultado(@PathVariable Integer resultadoId,
                                             @PathVariable Integer etiquetaId,
                                             @RequestHeader("referer") String referer) {
        etiquetaService.etiquetarResultado(resultadoId, etiquetaId);
        return "redirect:" + referer;
    }

    @PostMapping("/resultado/{resultadoId}/etiqueta/{etiquetaId}/quitar")
    public String quitarEtiquetaDeResultado(@PathVariable Integer resultadoId,
                                             @PathVariable Integer etiquetaId,
                                             @RequestHeader("referer") String referer) {
        etiquetaService.desetiquetarResultado(resultadoId, etiquetaId);
        return "redirect:" + referer;
    }
}
