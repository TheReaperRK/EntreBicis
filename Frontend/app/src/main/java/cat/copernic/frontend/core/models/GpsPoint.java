package cat.copernic.frontend.core.models;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



import java.math.BigDecimal;
import java.time.LocalDateTime;
import cat.copernic.frontend.core.models.Route;

/**
 *
 * @author carlo
 */

public class GpsPoint {

    private Long idPunt;
    private Route route;
    private BigDecimal latitud;

    private BigDecimal longitud;

    private LocalDateTime timestamp;


    public GpsPoint(Long idPunt, Route route, BigDecimal latitud, BigDecimal longitud, LocalDateTime timestamp) {
        this.idPunt = idPunt;
        this.route = route;
        this.latitud = latitud;
        this.longitud = longitud;
        this.timestamp = timestamp;
    }

    public GpsPoint() {
    }

    public Long getIdPunt() {
        return idPunt;
    }

    public void setIdPunt(Long idPunt) {
        this.idPunt = idPunt;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}