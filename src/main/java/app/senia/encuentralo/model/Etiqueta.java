package app.senia.encuentralo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Etiqueta") // Se conecta con la tabla Etiqueta
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "id_etiqueta") // Mapea con id_etiqueta
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    // LA MAGIA DE LA RELACIÓN:
    // Muchas etiquetas pertenecen a un único Usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false) // Esta es la Foreign Key de tu SQL
    private Usuario usuario; // ¡Guardamos el objeto Usuario entero!

    // Constructor vacío obligatorio para JPA
    public Etiqueta() {
    }

    // Constructor adaptado (ahora recibe el objeto Usuario completo)
    public Etiqueta(String nombre, Usuario usuario) {
        this.nombre = nombre;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}