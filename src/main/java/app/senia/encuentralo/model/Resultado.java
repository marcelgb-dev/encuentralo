package app.senia.encuentralo.model;

import java.util.List;

public class Resultado {

    // Atributos
    private String nombre;
    private double rating;
    private String url;
    private String telefono;
    private double longitud;
    private double latitud;
    private String direccion;
    private List<String> categorias;

    // Constructor
    public Resultado(String nombre, double rating, String url, String telefono, double longitud, double latitud, String direccion, List<String> categorias) {
        this.nombre = nombre;
        this.rating = rating;
        this.url = url;
        this.telefono = telefono;
        this.longitud = longitud;
        this.latitud = latitud;
        this.direccion = direccion;
        this.categorias = categorias;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre +
                "\nRating: " + rating +
                "\nUrl: " + url +
                "\nTeléfono: " + telefono +
                "\nCoordenadas: " + longitud + " long ," + latitud + " lat" +
                "\nDireccion: " + direccion +
                "\nCategorias: " + String.join(", ", categorias);
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }
}
