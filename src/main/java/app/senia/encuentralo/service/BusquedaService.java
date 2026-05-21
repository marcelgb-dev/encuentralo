package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Busqueda;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusquedaService {

    // Conexión con el repositorio

    public Busqueda obtenerBusqueda(Integer busquedaId) {
        // Lógica para obtener una búsqueda por su id
        return null;
    }

    public List<Busqueda> obtenerHistorial() {
        // Lógica para obtener el historial de búsquedas
        return null;
    }

    // Guarda una Busqueda en la base de datos y la devuelve con los campos autogenerados (el ID)
    public Busqueda guardarBusqueda(Busqueda busqueda) {
        // Lógica para guardar una nueva Búsqueda y devolverla
        return null;
    }
}
