/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.frontend.core.models;

import cat.copernic.backend.entity.enums.State;
import cat.copernic.backend.entity.enums.Validation;
import java.sql.Time;
import java.time.LocalDateTime;

/**
 *
 * @author carlo
 */

public class Route {

    private int id_route;
    
    private LocalDateTime start_date;

    private LocalDateTime end_date;
    
    //distance on km
    private double distance;
    
    //average speed on km/h
    private double average_speed;
    
    private Time total_time;
    
    private Validation validation_state;    
    
    private int generated_balance;
    
    private State state;

    private User user;

    public Route(int id_route, LocalDateTime start_date, LocalDateTime end_date, double distance, double average_speed, Time total_time, Validation validation_state, int generated_balance, State state, User user) {
        this.id_route = id_route;
        this.start_date = start_date;
        this.end_date = end_date;
        this.distance = distance;
        this.average_speed = average_speed;
        this.total_time = total_time;
        this.validation_state = validation_state;
        this.generated_balance = generated_balance;
        this.state = state;
        this.user = user;
    }

    public Route() {
    }

    public int getId_route() {
        return id_route;
    }

    public void setId_route(int id_route) {
        this.id_route = id_route;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAverage_speed() {
        return average_speed;
    }

    public void setAverage_speed(double average_speed) {
        this.average_speed = average_speed;
    }

    public Time getTotal_time() {
        return total_time;
    }

    public void setTotal_time(Time total_time) {
        this.total_time = total_time;
    }

    public Validation getValidation_state() {
        return validation_state;
    }

    public void setValidation_state(Validation validation_state) {
        this.validation_state = validation_state;
    }

    public int getGenerated_balance() {
        return generated_balance;
    }

    public void setGenerated_balance(int generated_balance) {
        this.generated_balance = generated_balance;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
