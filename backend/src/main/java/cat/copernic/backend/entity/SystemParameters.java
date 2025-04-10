/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author carlo
 */

@Entity
@Table(name = "system_parameters")
@Getter
@Setter
@NoArgsConstructor
public class SystemParameters {
    
    @Id
    @Column(name = "name_config", length = 20, nullable = false)
    private String nameConfig = "default";
    
    @Column(name = "average_valid_speed", nullable = false, columnDefinition = "DOUBLE DEFAULT 25")
    private double averageValidSpeed = 25;
    
    @Column(name = "max_inactivity", nullable = false, columnDefinition = "INT DEFAULT 300")
    private int maxInactivity = 300;
    
    @Column(name = "points_per_kilometer", nullable = false, columnDefinition = "DOUBLE DEFAULT 100")
    private double pointsPerKilometer = 100;
    
    @Column(name = "pickup_time", nullable = false, columnDefinition = "INT DEFAULT 72")
    private int pickupTime = 72;
    
    
    
}
