package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Busqueda;
import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.model.Resultado;
import app.senia.encuentralo.repository.CategoriaRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        return categorias;
    }

    // Filtra las categorías para evitar duplicados en la base de datos
    public Busqueda limpiarCategoriasBusqueda(Busqueda busqueda) {

        List<Categoria> categoriasTotales = new ArrayList<>();

        for (Resultado r : busqueda.getResultados()) {
            List<Categoria> categorias = r.getCategorias();
            List <Categoria> categoriasFiltradas = new ArrayList<>();

            for (int i = 0; i < categorias.size(); i++) {
                String nombre = categorias.get(i).getNombre();

                Categoria categoria = categoriaRepo.findByNombre(nombre)
                    .orElseGet(() -> {
                        // Si NO existe, creamos una nueva instancia transitoria
                        return new Categoria(nombre);
                    });

                for (Categoria c : categoriasTotales) {
                    if (c.getNombre() == categoria.getNombre())
                    {
                        
                    }
                }

                categoriasFiltradas.add(categoria);
                categoriasTotales.add(categoria);
            }

            r.setCategorias(categoriasFiltradas);
        }

        for (Resultado r : busqueda.getResultados()) {
            for (Categoria c : r.getCategorias())
                System.out.println()
        }

        return busqueda;
    } 

    public Categoria guardarCategoria(Categoria categoria) {
        // Lógica para guardar una nueva categoría y devolver el objeto de la BD
        return null;
    }
}
