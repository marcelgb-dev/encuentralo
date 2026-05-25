package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. MÉTODO DE REGISTRO (El tuyo rematado)
    public Usuario nuevoUsuario(Usuario usuario) throws Exception {
        // 1. Comprobamos si el email ya está pillado
        Usuario usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioExistente != null) {
            throw new Exception("El email ya está registrado en la plataforma.");
        }

        // 2. Si no existe, le asignamos el rol básico
        usuario.setRol("USER");

        // 3. ¡EL CIERRE QUE FALTABA! Lo guardamos en la despensa y lo devolvemos
        return usuarioRepository.save(usuario);
    }

    // 2. MÉTODO DE LOGIN (El que añade la nueva funcionalidad)
    public Usuario login(String email, String password) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new Exception("Credenciales incorrectas.");
        }

        if (!usuario.getPassword().equals(password)) {
            throw new Exception("Credenciales incorrectas.");
        }

        return usuario;
    }

    public void editarUsuario(Integer usuarioId, Usuario usuario) {

    }

    public List<Usuario> obtenerListaUsuarios() {
        return null;
    }

    public Optional<Usuario> obtenerUsuario(Integer usuarioId) {
        return Optional.empty();
    }
}