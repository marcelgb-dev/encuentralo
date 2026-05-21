package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Categoria;
import org.springframework.stereotype.Service;
import app.senia.encuentralo.model.Resultado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ResultadosService {


    // Métodos de ordenación (Sorting)

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
            if (!Collections.disjoint(r.getCategorias(), nombresCategoria))
                listaFiltrada.add(r);
        }

        return listaFiltrada;
    }

    public List<Resultado> filtrarSoloFavoritos (List<Resultado> listaResultados) {
        List<Resultado> listaFiltrada = new ArrayList<>();

        for (Resultado r : listaResultados) {
            if (r.isEsFavorito())
                listaFiltrada.add(r);
        }
        return listaFiltrada;
    }
}
