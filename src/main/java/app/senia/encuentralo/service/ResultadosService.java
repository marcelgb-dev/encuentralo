package app.senia.encuentralo.service;

import org.springframework.stereotype.Service;
import app.senia.encuentralo.model.Resultado;

import java.util.ArrayList;
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
}
