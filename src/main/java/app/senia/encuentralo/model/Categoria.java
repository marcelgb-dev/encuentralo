package app.senia.encuentralo.model;

import java.util.ArrayList;
import java.util.List;

public class Categoria {

    // Atributos
    private int id;
    private String nombre;

    // Constructor
    public Categoria(String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Util
    public static List<Categoria> toCategorias(List<String> categoriasString) {
        List<Categoria> categorias = new ArrayList<>();

        for (String s : categoriasString) {
            categorias.add(new Categoria(s));
        }

        return categorias;
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
}
