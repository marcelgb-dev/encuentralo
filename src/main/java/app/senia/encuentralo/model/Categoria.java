package app.senia.encuentralo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Categoria") // Se conecta con la tabla Categoria
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT en SQL
    @Column(name = "id_categoria") // Mapea con la columna de tu base de datos
    private Integer id;

    @Column(name = "nombre_categoria", nullable = false) // Mapea con nombre_categoria
    private String nombre;

    // Constructor vacío obligatorio para que JPA funcione mediante reflexión
    public Categoria() {
    }

    // Constructor corregido (quitado el bug de asignación de id)
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // Tu método de utilidad se queda igual
    public static List<Categoria> toCategorias(List<String> categoriasString) {
        List<Categoria> categorias = new ArrayList<>();

        for (String s : categoriasString) {
            categorias.add(new Categoria(s));
        }

        return categorias;
    }

    // Getters y Setters corregidos con Integer
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
}