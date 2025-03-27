/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity;

import cat.copernic.backend.entity.enums.State;
import cat.copernic.backend.entity.enums.Validation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.sql.Time;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author carlo
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SuperBuilder
public class Route {
    
    @Id
    @Column(unique = true, nullable = false)
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
    
    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;
}
