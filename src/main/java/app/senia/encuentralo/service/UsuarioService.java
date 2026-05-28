package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
    );

    private static final Set<String> TLD_VALIDOS = Set.of(
            "com", "es", "net", "org", "info", "biz", "io", "co", "eu", "edu", "gov",
            "dev", "app", "me", "us", "uk", "de", "fr", "it", "pt", "mx", "ar", "cl",
            "br", "cat", "online", "store", "tech", "site", "xyz", "pro", "name",
            "int", "mil", "coop", "museum", "travel", "jobs", "mobi", "asia",
            "com.es", "org.es", "co.uk", "com.ar", "com.mx", "com.br"
    );

    private void validarEmail(String email) throws Exception {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new Exception("El formato del email no es válido.");
        }
        String dominio = email.substring(email.lastIndexOf('@') + 1);
        String tld = dominio.contains(".")
                ? dominio.substring(dominio.indexOf('.') + 1).toLowerCase()
                : "";
        if (!TLD_VALIDOS.contains(tld)) {
            throw new Exception("El dominio del email no es válido. Usa un dominio real (.com, .es, .net, etc.).");
        }
    }

    public Usuario nuevoUsuario(Usuario usuario) throws Exception {
        validarEmail(usuario.getEmail());

        Usuario existente = usuarioRepository.findByEmail(usuario.getEmail());

        if (existente != null) {
            throw new Exception("El email ya está registrado en la plataforma.");
        }

        // Solo asigna USER por defecto si el admin no seleccionó ningún rol
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("USER");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    public void editarUsuario(Integer usuarioId, Usuario datosNuevos) {
        Optional<Usuario> opt = usuarioRepository.findById(usuarioId);
        if (opt.isEmpty()) return;

        Usuario usuario = opt.get();
        usuario.setNombre(datosNuevos.getNombre());
        usuario.setApellidos(datosNuevos.getApellidos());
        usuario.setEmail(datosNuevos.getEmail());
        usuario.setRol(datosNuevos.getRol());

        // Solo actualiza la contraseña si se introdujo una nueva
        if (datosNuevos.getPassword() != null && !datosNuevos.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(datosNuevos.getPassword()));
        }

        usuarioRepository.save(usuario);
    }

    public List<Usuario> obtenerListaUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId);
    }

    public void eliminarUsuario(Integer usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }
}
