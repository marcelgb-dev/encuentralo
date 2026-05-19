package app.senia.encuentralo.model;

import java.util.ArrayList;
import java.util.List;

public class Resultado {

    // Atributos
    private int id;
    private String nombre;
    private String url;
    private String telefono;
    private String direccion;
    private double valoracion;
    private List<Categoria> categorias;

    private double longitud;
    private double latitud;

    // Constructor
    public Resultado(String nombre, double valoracion, String url, String telefono, double longitud, double latitud, String direccion, List<Categoria> categorias) {
        this.id = id;
        this.nombre = nombre;
        this.valoracion = valoracion;
        this.url = url;
        this.telefono = telefono;
        this.longitud = longitud;
        this.latitud = latitud;
        this.direccion = direccion;
        this.categorias = categorias;
    }

    @Override
    public String toString() {

        List<String> categoriasString = new ArrayList<>();
        for (Categoria c : categorias){
            categoriasString.add(c.getNombre());
        }

        return "ID: " + id +
                "\nNombre: " + nombre +
                "\nRating: " + valoracion +
                "\nUrl: " + url +
                "\nTeléfono: " + telefono +
                "\nCoordenadas: " + longitud + " long ," + latitud + " lat" +
                "\nDireccion: " + direccion +
                "\nCategorias: " + String.join(", ", categoriasString);
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
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

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
