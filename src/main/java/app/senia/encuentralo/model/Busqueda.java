package app.senia.encuentralo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Busqueda") // Conecta con la tabla Busqueda
public class Busqueda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "id_busqueda") // Mapea con id_busqueda
    private Integer id;

    @Column(name = "termino_busqueda", nullable = false) // Mapea con termino_busqueda
    private String termino;

    @Column(name = "fecha_busqueda", nullable = false) // Mapea con fecha_busqueda
    private LocalDateTime fecha;

    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    // RELACIÓN 1: Una búsqueda pertenece a un único Usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false) // Clave foránea en SQL
    private Usuario usuario; // Objeto Usuario completo

    // RELACIÓN 2: Una búsqueda mapea muchos Resultados en Java
    // El 'mappedBy' apunta al atributo 'busqueda' que crearemos en la clase
    // Resultado
    @OneToMany(mappedBy = "busqueda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resultado> resultados;

    // Constructor vacío obligatorio para JPA
    public Busqueda() {
    }

    // Constructor adaptado para usar los objetos reales
    public Busqueda(String termino, LocalDateTime fecha, Usuario usuario, String ciudad) {
        this.termino = termino;
        this.fecha = fecha;
        this.usuario = usuario;
        this.ciudad = ciudad;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Resultado> getResultados() {
        return resultados;
    }

    public void setResultados(List<Resultado> resultados) {
        this.resultados = resultados;
    }
}