package app.senia.encuentralo.repository;

import app.senia.encuentralo.model.Categoria;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Si necesitas buscar una categoría por su nombre exacto más adelante:
    Optional<Categoria> findByNombre(String nombre);
}