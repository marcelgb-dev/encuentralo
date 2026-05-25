package app.senia.encuentralo.repository;

import app.senia.encuentralo.model.Busqueda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BusquedaRepository extends JpaRepository<Busqueda, Integer> {
    // Método útil: ver el historial de búsquedas de un usuario ordenado por fecha
    List<Busqueda> findByUsuarioIdOrderByFechaDesc(Integer idUsuario);
}