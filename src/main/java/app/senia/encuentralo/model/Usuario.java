package app.senia.encuentralo.model;

public class Usuario {

    // Atributos de la base de datos
    private Integer id;
    private String email;
    private String password;
    private String nombre;
    private String apellidos;
    private String rol;

    // Constructor
    public Usuario(String email, String password, String nombre, String apellidos, String rol) {
        this.email = email;
        this.rol = rol;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
