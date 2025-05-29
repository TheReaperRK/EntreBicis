/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.DTO;

/**
 * DTO lleuger per representar punts GPS de manera simplificada.
 * 
 * S’utilitza principalment per enviar només la latitud i la longitud 
 * d’una ruta al frontend web, per exemple per dibuixar punts sobre un mapa.
 * 
 * Aquest objecte no conté el `timestamp`, ja que no és necessari per a la visualització.
 * 
 * @author carlo
 */
public class GpsPointDTOLight {

    /**
     * Latitud en graus decimals.
     */
    private double latitud;

    /**
     * Longitud en graus decimals.
     */
    private double longitud;

    /**
     * Constructor amb latitud i longitud.
     * 
     * @param latitud coordenada nord-sud
     * @param longitud coordenada est-oest
     */
    public GpsPointDTOLight(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}

