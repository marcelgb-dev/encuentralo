package app.senia.encuentralo.service;

import app.senia.encuentralo.model.Busqueda;

// Interfaz que sirve como plantilla para los serivces de las diferentes APIs
public interface ProviderService {

    Busqueda llamarApi(String termino, double latitud, double longitud, int radio, int limite);

}
