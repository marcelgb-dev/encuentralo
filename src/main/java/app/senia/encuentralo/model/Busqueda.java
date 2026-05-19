package app.senia.encuentralo.model;

import java.time.LocalDateTime;
import java.util.List;

public class Busqueda {

    // Atributos
    private int id;
    private String termino;
    private double latitud;
    private double longitud;
    private LocalDateTime fecha;

    private List<Resultado> resultados;

    // Constructor
    public Busqueda(String termino, double latitud, double longitud, LocalDateTime fecha) {
        this.id = id;
        this.termino = termino;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public List<Resultado> getResultados() {
        return resultados;
    }

    public void setResultados(List<Resultado> resultados) {
        this.resultados = resultados;
    }

}
