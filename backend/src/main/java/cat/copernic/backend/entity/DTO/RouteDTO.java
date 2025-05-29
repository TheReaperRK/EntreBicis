/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.DTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO per representar una ruta completa, incloent dades generals
 * (distància, durada, velocitat mitjana) i els punts GPS.
 * 
 * Aquesta classe s’utilitza per enviar o rebre dades entre l’app Android
 * i el backend quan una ruta és creada, finalitzada o consultada.
 * 
 * ⚠️ El temps total es guarda com a String per compatibilitat de format.
 * 
 * Exemple d’ús: quan l’usuari finalitza una ruta i es vol enviar tota la informació
 * al backend.
 * 
 * @author carlo
 */
public class RouteDTO {

    /** Identificador únic de la ruta (opcional en creació) */
    private Long idRouteDTO = null;

    /** Data i hora d’inici de la ruta */
    private LocalDateTime startDate;

    /** Data i hora de finalització de la ruta */
    private LocalDateTime endDate;

    /** Distància total recorreguda en km */
    private double distance;

    /** Velocitat mitjana de la ruta en km/h */
    private double averageSpeed;

    /** Temps total de la ruta (format HH:mm:ss) */
    private String totalTime;

    /** Punts generats per la ruta, segons la distància i el sistema de paràmetres */
    private int generatedBalance;

    /** Correu electrònic de l’usuari que ha realitzat la ruta */
    private String userEmail;

    /** Llista de punts GPS associats a la ruta */
    private List<GpsPointDTO> gpsPoints;

    // Getters i setters

    public Long getIdRouteDTO() {
        return idRouteDTO;
    }

    public void setIdRouteDTO(Long idRouteDTO) {
        this.idRouteDTO = idRouteDTO;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public int getGeneratedBalance() {
        return generatedBalance;
    }

    public void setGeneratedBalance(int generatedBalance) {
        this.generatedBalance = generatedBalance;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<GpsPointDTO> getGpsPoints() {
        return gpsPoints;
    }

    public void setGpsPoints(List<GpsPointDTO> gpsPoints) {
        this.gpsPoints = gpsPoints;
    }
}
