package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Etiqueta;
import app.senia.encuentralo.repository.EtiquetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepo;

    public EtiquetaService(EtiquetaRepository etiquetaRepo) {
        this.etiquetaRepo = etiquetaRepo;
    }


    public List<Etiqueta> obtenerEtiquetasUsuario(Integer usuarioId) {
        // Lógica para obtener todas las etiquetas de un usuario
        List<Etiqueta> etiquetas = etiquetaRepo.findByUsuarioId(usuarioId);
        return etiquetas;
    }

    public List<Etiqueta> obtenerEtiquetasResultado(Integer resultadoId) {
        // Lógica para obtener todas las etiquetas de un resultado
        return null;
    }

    public void guardarEtiqueta(Integer usuarioId, Etiqueta etiqueta) {
        // Lógica para guardar una nueva etiqueta
    }

    public void borrarEtiqueta(Integer etiquetaId) {
        // Lógica para borrar una etiqueta
    }

    public void etiquetarResultado(Integer resultadoId, Integer etiquetaId) {
        // Lógica para añadir una etiqueta a un resultado
    }
}
