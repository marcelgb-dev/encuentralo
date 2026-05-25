package app.senia.encuentralo.repository;

import app.senia.encuentralo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Si necesitas buscar una categoría por su nombre exacto más adelante:
    Categoria findByNombre(String nombre);
}