package app.senia.encuentralo.dto;

public class SolicitudBusqueda {

    // Atributos
    private String termino; // Término de búsqueda
    private double latitud; // Latitud del usuario
    private double longitud; // Longitud del usuario
    private int limite; // Número máximo de resultados
    private int radio; // Radio de búsqueda en torno al usuario (en metros)

    public SolicitudBusqueda() {
    }

    public SolicitudBusqueda(String termino, double latitud, double longitud, int limite, int radio) {
        this.termino = termino;
        this.latitud = latitud;
        this.longitud = longitud;
        this.limite = limite;
        this.radio = radio;
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

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }
}
