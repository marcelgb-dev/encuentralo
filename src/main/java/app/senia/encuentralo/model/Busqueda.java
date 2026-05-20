package app.senia.encuentralo.model;

import java.time.LocalDateTime;
import java.util.List;

public class Busqueda {

    // Atributos
    private Integer id;
    private String termino;
    private LocalDateTime fecha;
    private Integer idUsuario;
    private String ciudad;

    private List<Resultado> resultados;

    // Constructor
    public Busqueda(String termino, LocalDateTime fecha, Integer idUsuario, String ciudad) {
        this.termino = termino;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
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

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public List<Resultado> getResultados() {
        return resultados;
    }

    public void setResultados(List<Resultado> resultados) {
        this.resultados = resultados;
    }
}
