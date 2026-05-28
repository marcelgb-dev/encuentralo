package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Etiqueta;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.EtiquetaRepository;
import app.senia.encuentralo.repository.ResultadoRepository;
import app.senia.encuentralo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepo;
    private final UsuarioRepository usuarioRepo;
    private final ResultadoRepository resultadoRepo;

    public EtiquetaService(EtiquetaRepository etiquetaRepo, UsuarioRepository usuarioRepo, ResultadoRepository resultadoRepo) {
        this.etiquetaRepo = etiquetaRepo;
        this.usuarioRepo = usuarioRepo;
        this.resultadoRepo = resultadoRepo;
    }

    public List<Etiqueta> obtenerEtiquetasUsuario(Integer usuarioId) {
        return etiquetaRepo.findByUsuarioId(usuarioId);
    }

    public Etiqueta guardarEtiqueta(Integer usuarioId, String nombre) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
        Etiqueta etiqueta = new Etiqueta(nombre, usuario);
        return etiquetaRepo.save(etiqueta);
    }

    public void borrarEtiqueta(Integer etiquetaId) {
        List<Resultado> resultados = resultadoRepo.findByEtiquetasId(etiquetaId);
        for (Resultado r : resultados) {
            r.getEtiquetas().removeIf(e -> e.getId().equals(etiquetaId));
            resultadoRepo.save(r);
        }
        etiquetaRepo.deleteById(etiquetaId);
    }

    public void etiquetarResultado(Integer resultadoId, Integer etiquetaId) {
        Resultado resultado = resultadoRepo.findById(resultadoId)
                .orElseThrow(() -> new RuntimeException("Resultado no encontrado con ID: " + resultadoId));
        Etiqueta etiqueta = etiquetaRepo.findById(etiquetaId)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada con ID: " + etiquetaId));

        if (resultado.getEtiquetas() == null)
            resultado.setEtiquetas(new ArrayList<>());

        if (!resultado.getEtiquetas().contains(etiqueta)) {
            resultado.getEtiquetas().add(etiqueta);
            resultadoRepo.save(resultado);
        }
    }

    public void desetiquetarResultado(Integer resultadoId, Integer etiquetaId) {
        Resultado resultado = resultadoRepo.findById(resultadoId)
                .orElseThrow(() -> new RuntimeException("Resultado no encontrado con ID: " + resultadoId));

        if (resultado.getEtiquetas() != null) {
            resultado.getEtiquetas().removeIf(e -> e.getId().equals(etiquetaId));
            resultadoRepo.save(resultado);
        }
    }
}
