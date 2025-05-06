/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.DTO;

/**
 *
 * @author carlo
 */
import java.time.LocalDateTime;
import java.sql.Time;
import java.util.List;

public class RouteDTO {
    private Long idRouteDTO = null;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double distance;
    private double averageSpeed;
    private String totalTime;
    private int generatedBalance;
    private String userEmail;
    private List<GpsPointDTO> gpsPoints;

    // Getters y setters

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

    public Long getIdRouteDTO() {
        return idRouteDTO;
    }

    public void setIdRouteDTO(Long idRouteDTO) {
        this.idRouteDTO = idRouteDTO;
    }
    
    
}