package app.senia.encuentralo.repository;

import app.senia.encuentralo.model.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Integer> {
    List<Etiqueta> findByUsuarioId(Integer idUsuario);
    void deleteByUsuarioId(Integer usuarioId);
}