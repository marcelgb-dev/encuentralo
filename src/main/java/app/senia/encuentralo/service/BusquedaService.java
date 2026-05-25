package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Usuario;
import app.senia.encuentralo.repository.BusquedaRepository;
import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusquedaService {

    private final BusquedaRepository busquedaRepo;
    private final UsuarioRepository usuarioRepo;

    public BusquedaService(BusquedaRepository busquedaRepo, UsuarioRepository usuarioRepo) {
        this.busquedaRepo = busquedaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    // Conexión con el repositorio

    public Busqueda obtenerBusqueda(Integer busquedaId) {
        // Lógica para obtener una búsqueda por su id
        return busquedaRepo.getReferenceById(busquedaId);
    }

    public List<Busqueda> obtenerHistorial(Integer usuarioId) {
        // Lógica para obtener el historial de búsquedas
        Usuario usuario = usuarioRepo.findById(usuarioId).orElseThrow();

        List<Busqueda> busquedas = busquedaRepo.findByUsuarioOrderByFechaDesc(usuario);
        for (Busqueda b : busquedas) {
            System.out.println("ID: " + b.getId());
        }

        return busquedas;
    }

    // Guarda una Busqueda en la base de datos y la devuelve con los campos autogenerados (el ID)
    public Busqueda guardarBusqueda(Busqueda busqueda) {
        // Lógica para guardar una nueva Búsqueda y devolverla

        return busquedaRepo.save(busqueda);
    }
}
