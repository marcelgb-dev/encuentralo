package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Etiqueta;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtiquetaService {

    public List<Etiqueta> obtenerEtiquetasUsuario(Integer usuarioId) {
        // Lógica para obtener todas las etiquetas de un usuario
        return null;
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
