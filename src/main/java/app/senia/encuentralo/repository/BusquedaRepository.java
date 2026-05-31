package app.senia.encuentralo.repository;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusquedaRepository extends JpaRepository<Busqueda, Integer> {
    List<Busqueda> findByUsuarioOrderByFechaDesc(Usuario usuario);
    void deleteByUsuarioId(Integer usuarioId);
}