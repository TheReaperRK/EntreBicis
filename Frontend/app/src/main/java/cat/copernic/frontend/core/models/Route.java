package cat.copernic.frontend.core.models;

import com.google.gson.annotations.SerializedName;
import cat.copernic.backend.entity.enums.State;
import cat.copernic.frontend.core.models.enums.Validation;

import java.time.LocalDateTime;

public class Route {

    @SerializedName("id_route")
    private int id_route;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("end_date")
    private String end_date;

    private double distance;

    @SerializedName("average_speed")
    private double average_speed;

    @SerializedName("total_time")
    private String total_time; // 🔥 Cambiado de Time a String

    @SerializedName("validation_state")
    private Validation validation_state;

    @SerializedName("generated_balance")
    private int generated_balance;

    private State state;

    private User user;

    public Route() {
    }

    public Route(int id_route, String startDate, String end_date, double distance, double average_speed, String total_time, Validation validation_state, int generated_balance, State state, User user) {
        this.id_route = id_route;
        this.startDate = startDate;
        this.end_date = end_date;
        this.distance = distance;
        this.average_speed = average_speed;
        this.total_time = total_time;
        this.validation_state = validation_state;
        this.generated_balance = generated_balance;
        this.state = state;
        this.user = user;
    }

    public int getId_route() {
        return id_route;
    }

    public void setId_route(int id_route) {
        this.id_route = id_route;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
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

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
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
