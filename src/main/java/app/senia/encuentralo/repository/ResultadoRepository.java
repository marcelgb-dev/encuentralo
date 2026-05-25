package app.senia.encuentralo.repository;

import app.senia.encuentralo.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Integer> {
    // Método útil: sacar solo los resultados que el usuario ha marcado como
    // favoritos
    List<Resultado> findByUsuarioIdAndEsFavoritoTrue(Integer idUsuario);
}