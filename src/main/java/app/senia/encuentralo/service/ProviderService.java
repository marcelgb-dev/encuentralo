package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Busqueda;

public interface ProviderService {

    Busqueda llamarApi(String termino, double latitud, double longitud, int radio, int limite);

}
