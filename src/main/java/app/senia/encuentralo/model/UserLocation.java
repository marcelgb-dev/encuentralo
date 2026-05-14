package app.senia.encuentralo.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class UserLocation {

    private double longitud;
    private double latitud;

    private LocalDateTime fechaHora;

    public String toString() {
        return "Longitud: " + longitud+
                "\nLatitud: " + latitud+
                "\nFecha y Hora: " + fechaHora.toString();
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
