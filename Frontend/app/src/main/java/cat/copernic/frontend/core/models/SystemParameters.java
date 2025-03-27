/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.frontend.core.models;

/**
 *
 * @author carlo
 */


public class SystemParameters {
    

    private String nameConfig = "default";

    private int averageValidSpeed = 25;

    private int maxInactivity = 300;

    private int pointsPerKilometer = 100;

    private int pickupTime = 72;

    public SystemParameters() {
    }

    public SystemParameters(String nameConfig, int averageValidSpeed, int maxInactivity, int pointsPerKilometer, int pickupTime) {
        this.nameConfig = nameConfig;
        this.averageValidSpeed = averageValidSpeed;
        this.maxInactivity = maxInactivity;
        this.pointsPerKilometer = pointsPerKilometer;
        this.pickupTime = pickupTime;
    }

    public String getNameConfig() {
        return nameConfig;
    }

    public void setNameConfig(String nameConfig) {
        this.nameConfig = nameConfig;
    }

    public int getAverageValidSpeed() {
        return averageValidSpeed;
    }

    public void setAverageValidSpeed(int averageValidSpeed) {
        this.averageValidSpeed = averageValidSpeed;
    }

    public int getMaxInactivity() {
        return maxInactivity;
    }

    public void setMaxInactivity(int maxInactivity) {
        this.maxInactivity = maxInactivity;
    }

    public int getPointsPerKilometer() {
        return pointsPerKilometer;
    }

    public void setPointsPerKilometer(int pointsPerKilometer) {
        this.pointsPerKilometer = pointsPerKilometer;
    }

    public int getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(int pickupTime) {
        this.pickupTime = pickupTime;
    }
}
