/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) que representa un punt GPS per a l'enviament o recepció
 * de dades entre el frontend (Android) i el backend.
 * 
 * Aquest objecte s'utilitza per encapsular informació d'un punt geogràfic concret,
 * incloent latitud, longitud i moment en què es va registrar.
 *
 * S'utilitza típicament dins d'una llista per construir una ruta (`RouteDTO`).
 * 
 * @author carlo
 */
public class GpsPointDTO {

    /**
     * Latitud del punt GPS, en graus decimals.
     */
    private BigDecimal latitud;

    /**
     * Longitud del punt GPS, en graus decimals.
     */
    private BigDecimal longitud;

    /**
     * Instant en què es va registrar aquest punt GPS.
     */
    private LocalDateTime timestamp;

    // Getters i Setters

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
