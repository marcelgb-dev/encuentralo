package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Usuario;

import java.util.Optional;
import java.util.List;

public class UsuarioService {

    public boolean verificarUsuario(String email, String password) {
        // Lógica para verificar si los datos introducidos corresponden a un usuario
        // (tener en cuenta encriptación de contraseña)
        return false;
    }

    public Optional<Usuario> obtenerUsuario(Integer usuarioId) {
        // Lógica para obtener un Optional con el usuario seleccionado

        return Optional.empty();
    }

    public List<Usuario> obtenerListaUsuarios() {
        // Lógica para obtener una lista de todos los usuarios
        return null;
    }

    public void nuevoUsuario(Usuario usuario) {
        // Lógica para encriptar contraseña
        // Lógica para guardar un nuevo usuario en la BD
    }

    public void editarUsuario(Integer usuarioId, Usuario usuarioEditado) {
        // Lógica para guardar los datos de usuarioEditado en el usuario de la BD indicado por el ID
    }
}
