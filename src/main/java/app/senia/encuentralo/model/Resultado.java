package app.senia.encuentralo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Resultados") // Se conecta con tu tabla Resultados
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "id_resultado") // Mapea con id_resultado
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "distancia", nullable = false)
    private double distancia; // Mapea tu FLOAT de SQL

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "valoracion", nullable = false)
    private double valoracion; // Mapea tu FLOAT de SQL

    @Column(name = "num_reviews", nullable = false) // En tu SQL se llama num_reviews
    private Integer numReviews;

    @Column(name = "url", nullable = false, unique = true) // UNIQUE en tu SQL
    private String url;

    @Column(name = "esFavorito", nullable = false) // BOOLEAN en SQL
    private boolean esFavorito;

    // RELACIONES SIMPLES (Muchos resultados pertenecen a un Usuario/Búsqueda)
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false) // FK en SQL
    private Usuario usuario; // Objeto Usuario entero

    @ManyToOne
    @JoinColumn(name = "id_busqueda", nullable = false) // FK en SQL
    private Busqueda busqueda; // Objeto Busqueda entero

    // RELACIÓN N:M CON CATEGORÍAS
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "Resultados_Categoria",
            joinColumns = @JoinColumn(name = "id_resultado"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private List<Categoria> categorias;

    // RELACIÓN N:M CON ETIQUETAS
    @ManyToMany
    @JoinTable(name = "Etiqueta_Resultados", // Tu tabla intermedia en SQL
            joinColumns = @JoinColumn(name = "id_resultado"), // FK de esta clase
            inverseJoinColumns = @JoinColumn(name = "id_etiqueta") // FK de la otra clase
    )
    private List<Etiqueta> etiquetas;

    @Transient // Esto le dice a JPA que ignore este campo, no existe en SQL
    private String ciudad; // Atributo propio de Java que tenías

    // Constructor vacío obligatorio
    public Resultado() {
    }

    // Constructor con todos los campos de la tabla
    public Resultado(String nombre, String telefono, double distancia, String direccion, double valoracion,
            Integer numReviews, String url, boolean esFavorito, Usuario usuario, Busqueda busqueda) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.distancia = distancia;
        this.direccion = direccion;
        this.valoracion = valoracion;
        this.numReviews = numReviews;
        this.url = url;
        this.esFavorito = esFavorito;
        this.usuario = usuario;
        this.busqueda = busqueda;
    }

    // Constructor secundario vacío por si vuestro JSON de pruebas lo necesita
    public Resultado(double valoracion, int numReviews, String url, String telefono, double longitud,
            double latitud, int distancia, String direccion, String ciudad) {
    }

    @Override
    public String toString() {
        List<String> categoriasString = new ArrayList<>();
        if (categorias != null) {
            for (Categoria c : categorias) {
                categoriasString.add(c.getNombre());
            }
        }
        return "ID: " + id +
                "\nNombre: " + nombre +
                "\nRating: " + valoracion +
                "\nNum reviews: " + numReviews +
                "\nUrl: " + url +
                "\nTeléfono: " + telefono +
                "\nDistancia: " + String.format("%.2f", distancia) +
                "\nDireccion: " + direccion +
                "\nCategorias: " + String.join(", ", categoriasString);
    }

    // GETTERS Y SETTERS CORREGIDOS
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

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
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

    public Integer getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(Integer numReviews) {
        this.numReviews = numReviews;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEsFavorito() {
        return esFavorito;
    }

    public void setEsFavorito(boolean esFavorito) {
        this.esFavorito = esFavorito;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Busqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(Busqueda busqueda) {
        this.busqueda = busqueda;
    }

    // Métodos puente para mantener la compatibilidad con el código antiguo de tu
    // compañero
    public Integer getId_usuario() {
        return this.usuario != null ? this.usuario.getId() : null;
    }

    public void setId_usuario(Integer id_usuario) {
        // No hace nada, JPA gestiona el objeto completo
    }

    public Integer getIdbusqueda() {
        return this.busqueda != null ? this.busqueda.getId() : null;
    }

    public void setIdbusqueda(Integer id_busqueda) {
        // No hace nada, JPA gestiona el objeto completo
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}