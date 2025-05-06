/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.DTO;

/**
 *
 * @author carlo
 */
public class GpsPointDTOLight {
    private double latitud;
    private double longitud;

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
