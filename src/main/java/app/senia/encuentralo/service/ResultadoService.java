package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Categoria;
import app.senia.encuentralo.repository.CategoriaRepository;
import app.senia.encuentralo.repository.ResultadoRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import app.senia.encuentralo.model.Resultado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ResultadoService {

    // Atributos / dependencias
    private final ResultadoRepository resultadoRepo;

    // Constructor
    public ResultadoService(ResultadoRepository resultadosRepo) {
        this.resultadoRepo = resultadosRepo;
    }

    // Comunicación con el repositorio

    public List<Resultado> obtenerResultadosFavoritos (Integer idUsuario) {
        // Lógica para devolver una lista con todos los resultados guardados como favoritos de un usuario
        return resultadoRepo.findByUsuarioIdAndEsFavoritoTrue(idUsuario);
    }

    public void guardarResultados(Integer idBusqueda, Integer idUsuario, List<Resultado> resultados) {
        // Lógica de guardado
    }

    public void guardarFavorito(Integer resultadoId) {
        // Obtenemos el objeto resultado por la id
        Resultado resultado = resultadoRepo.findById(resultadoId)
            .orElseThrow(() -> new EntityNotFoundException("Resultado no encontrado"));

        // Invertimos el booleano de esFavorito (si es false lo convertimos a true, si es true lo convertimos a false)
        resultado.setEsFavorito(!resultado.isEsFavorito());
        resultadoRepo.save(resultado);


    }


    // Métodos de ordenación (Sorting). inverso = true para orden inverso

    public List<Resultado> ordenarPorDistancia (List<Resultado> listaResultados, boolean inverso) {

        if (!inverso)
            listaResultados.sort(Comparator.comparing(Resultado::getDistancia));
        else
            listaResultados.sort(Comparator.comparing(Resultado::getDistancia).reversed());

        return listaResultados;
    }

    public List<Resultado> ordenarPorNombre (List<Resultado> listaResultados, boolean inverso) {

        if (!inverso)
            listaResultados.sort(Comparator.comparing(Resultado::getNombre));
        else
            listaResultados.sort(Comparator.comparing(Resultado::getNombre).reversed());

        return listaResultados;
    }

    public List<Resultado> ordenarPorValoracion (List<Resultado> listaResultados, boolean inverso) {

        if (!inverso)
            listaResultados.sort(Comparator.comparing(Resultado::getValoracion).reversed());
        else
            listaResultados.sort(Comparator.comparing(Resultado::getValoracion));

        return listaResultados;
    }

    // Métodos de filtrado (filtering)

    // Valoración >= parámetro
    public List<Resultado> filtrarPorValoracion (List<Resultado> listaResultados, double minimo) {
        List<Resultado> listaFiltrada = new ArrayList<>();

        for (Resultado r : listaResultados) {
            if (r.getValoracion() >= minimo)
                listaFiltrada.add(r);
        }
        return listaFiltrada;
    }

    // Una sola categoría
    public List<Resultado> filtrarPorCategoria (List<Resultado> listaResultados, String nombreCategoria) {
        List<Resultado> listaFiltrada = new ArrayList<>();

        for (Resultado r : listaResultados) {
            List<Categoria> categorias = r.getCategorias();

            for (Categoria c : categorias) {
                if (nombreCategoria.equals(c.getNombre())) {
                    listaFiltrada.add(r);
                    break;
                }
            }
        }
        return listaFiltrada;
    }

    // Una lista de categorías
    public List<Resultado> filtrarPorCategoria (List<Resultado> listaResultados, List<String> nombresCategoria) {

        List<Resultado> listaFiltrada = new ArrayList<>();

        for (Resultado r : listaResultados) {
            // Entra en la condición si r.getCategorias() y nombresCategoria tienen algún elemento en común
            if (!Collections.disjoint(Categoria.toStringList(r.getCategorias()), nombresCategoria))
                listaFiltrada.add(r);
        }

        return listaFiltrada;
    }

    // Solo favoritos
    public List<Resultado> filtrarSoloFavoritos (List<Resultado> listaResultados) {
        List<Resultado> listaFiltrada = new ArrayList<>();

        for (Resultado r : listaResultados) {
            if (r.isEsFavorito())
                listaFiltrada.add(r);
        }
        return listaFiltrada;
    }
}
