package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.repository.CategoriaRepository;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepo;

    public CategoriaService(CategoriaRepository categoriaRepo) {
        this.categoriaRepo = categoriaRepo;
    }

    public List<Categoria> obtenerCategorias(Integer userId) {
        // Lógica para obtener todas las categorías de un usuario
        return null;
    }

    public List<Categoria> obtenerCategorias(List<Resultado> resultados) {
        
        List<Categoria> categorias = new ArrayList<Categoria>();

        for (Resultado r : resultados) {
            for (Categoria c : r.getCategorias())
            {
                if (!categorias.contains(c))
                    categorias.add(c);
            }
        }

        categorias.sort(Comparator.comparing(Categoria::getNombre));

        return categorias;
    }

    // Filtra las categorías para evitar duplicados en la base de datos
    public Busqueda limpiarCategoriasBusqueda(Busqueda busqueda) {

        // Mapa para cachear las categorías de esta búsqueda y no repetir consultas ni objetos
        Map<String, Categoria> mapaCategorias = new HashMap<>();

        // Recorremos la lista de resultados
        for (Resultado r : busqueda.getResultados()) {
            List<Categoria> categoriasOriginales = r.getCategorias();
            List<Categoria> categoriasProcesadas = new ArrayList<>();

            // Recorremos la lista de categorías del resultado
            for (Categoria cat : categoriasOriginales) {
                String nombre = cat.getNombre();

                // Miramos si ya hemos procesado esta categoría en este bucle, y usamos su referencia para añadirla al resultado
                if (mapaCategorias.containsKey(nombre)) {
                    categoriasProcesadas.add(mapaCategorias.get(nombre));
                // Si no la hemos procesado, la buscamos en la BD o la creamos
                } else {
                    Categoria categoriaFinal = categoriaRepo.findByNombre(nombre)
                            .orElseGet(() -> new Categoria(nombre));

                    // La añadimos tanto a la lista de categorías procesadas para el objeto como al mapa general
                    mapaCategorias.put(nombre, categoriaFinal);
                    categoriasProcesadas.add(categoriaFinal);
                }
            }
            // Actualizamos la lista del resultado con las instancias "limpias"
            r.setCategorias(categoriasProcesadas);
        }

        return busqueda;
    } 

    public Categoria guardarCategoria(Categoria categoria) {
        // Lógica para guardar una nueva categoría y devolver el objeto de la BD
        return null;
    }
}
