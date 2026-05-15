package app.senia.encuentralo.model;

public class SolicitudBusqueda {

    private String temino; // Término de búsqueda
    private double latitud; // Latitud del usuario
    private double longitud; // Longitud del usuario
    private int limite; // Número máximo de resultados
    private int radio; // Radio de búsqueda en torno al usuario (en metros)

    public SolicitudBusqueda(){}
    public SolicitudBusqueda(String temino, double latitud, double longitud, int limite, int radio) {
        this.temino = temino;
        this.latitud = latitud;
        this.longitud = longitud;
        this.limite = limite;
        this.radio = radio;
    }

    public String getTemino() {
        return temino;
    }

    public void setTemino(String temino) {
        this.temino = temino;
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
