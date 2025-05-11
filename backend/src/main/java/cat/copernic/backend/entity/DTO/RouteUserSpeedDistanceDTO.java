/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.DTO;

import cat.copernic.backend.entity.enums.Validation;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author carlo
 */
public class RouteUserSpeedDistanceDTO {
    private Long idRouteDTO;
    private double distance;
    private double averageSpeed;
    private String totalTime;
    private int generatedBalance;
    private String userEmail;
    private String validation_state;
    private LocalDateTime startDate;

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

