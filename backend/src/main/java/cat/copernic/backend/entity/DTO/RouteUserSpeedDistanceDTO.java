/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.DTO;

import java.time.LocalDateTime;

/**
 * DTO que representa una ruta amb informació essencial per a la seva visualització 
 * en llistats, incloent dades de distància, velocitat, temps, estat de validació i data d'inici.
 *
 * Aquesta classe és usada principalment a la vista web de l'administrador per mostrar 
 * un resum de totes les rutes d'usuaris, amb informació rellevant per a la seva validació.
 * 
 * @author carlo
 */
public class RouteUserSpeedDistanceDTO {

    /** Identificador de la ruta */
    private Long idRouteDTO;

    /** Distància total recorreguda en km */
    private double distance;

    /** Velocitat mitjana en km/h */
    private double averageSpeed;

    /** Temps total de la ruta en format HH:mm:ss */
    private String totalTime;

    /** Punts generats per la ruta */
    private int generatedBalance;

    /** Correu electrònic de l'usuari que ha fet la ruta */
    private String userEmail;

    /** Estat de validació (VALIDATED, INVALIDATED, PENDING) com a String */
    private String validation_state;

    /** Data i hora d’inici de la ruta */
    private LocalDateTime startDate;

    // Getters i setters

    public String getValidation_state() {
        return validation_state;
    }

    public void setValidation_state(String validation_state) {
        this.validation_state = validation_state;
    }

    public Long getIdRouteDTO() {
        return idRouteDTO;
    }

    public void setIdRouteDTO(Long idRouteDTO) {
        this.idRouteDTO = idRouteDTO;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}


