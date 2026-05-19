package app.senia.encuentralo.model;

public class Usuario {

    // Atributos
    private int id;
    private String password;
    private String nombre;
    private String apellidos;

    // Constructor
    public Usuario(String password, String nombre, String apellidos) {
        this.id = id;
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
}
