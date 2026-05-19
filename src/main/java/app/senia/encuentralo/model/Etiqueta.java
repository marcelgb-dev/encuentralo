package app.senia.encuentralo.model;

public class Etiqueta {

    // Atributos
    private int id;
    private String nombre;
    private int idUsuario;

    // Constructor
    public Etiqueta(String nombre, int idUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
