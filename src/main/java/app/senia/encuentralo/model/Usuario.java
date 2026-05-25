
package app.senia.encuentralo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuario") // Coincide exactamente con tu tabla SQL
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "id_usuario") // En tu SQL se llama id_usuario
    private Integer id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellidos") // En SQL pusiste NULL, así que no pasa nada si va vacío
    private String apellidos;

    @Column(name = "rol", nullable = false)
    private String rol;

    // Constructor vacío obligatorio para que JPA funcione mediante reflexión
    public Usuario() {
    }

    // Tu constructor actual
    public Usuario(String email, String password, String nombre, String apellidos, String rol) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.rol = rol;
    }

    // ... Tus Getters y Setters se quedan exactamente igual que los tienes

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
