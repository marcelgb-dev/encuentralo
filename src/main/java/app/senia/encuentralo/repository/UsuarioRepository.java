package app.senia.encuentralo.repository;

import app.senia.encuentralo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Añadimos este método personalizado para el Login y el Registro
    Usuario findByEmail(String email);
}