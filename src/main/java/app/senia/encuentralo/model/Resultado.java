package app.senia.encuentralo.model;

import java.util.ArrayList;
import java.util.List;

public class Resultado {

    // Atributos
    private Integer id;
    private String nombre;
    private String telefono;
    private int distancia; // Distancia en metros al origen de la búsqueda
    private String direccion;
    private double valoracion;
    private int numValoraciones;
    private String url;
    private boolean esFavorito;
    private Integer idUsuario;
    private Integer idBusqueda;

    private List<Categoria> categorias;
    private String ciudad;

    // Constructor
    public Resultado(String nombre, String telefono, int distancia, String direccion, double valoracion, int numValoraciones, String url, boolean esFavorito, Integer idUsuario, Integer idBusqueda) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.distancia = distancia;
        this.direccion = direccion;
        this.valoracion = valoracion;
        this.numValoraciones = numValoraciones;
        this.url = url;
        this.esFavorito = esFavorito;
        this.idUsuario = idUsuario;
        this.idBusqueda = idBusqueda;
    }


    // Constructor


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
                "\nDistancia: " + String.format("%.2f", distancia) +
                "\nDireccion: " + direccion +
                "\nCategorias: " + String.join(", ", categoriasString);
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    // Getters y Setters
    public boolean isEsFavorito() {
        return esFavorito;
    }

    public void setEsFavorito(boolean esFavorito) {
        this.esFavorito = esFavorito;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdBusqueda() {
        return idBusqueda;
    }

    public void setIdBusqueda(Integer idBusqueda) {
        this.idBusqueda = idBusqueda;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getNumValoraciones() {
        return numValoraciones;
    }

    public void setNumValoraciones(int numValoraciones) {
        this.numValoraciones = numValoraciones;
    }

}
