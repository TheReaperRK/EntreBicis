/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity;

import cat.copernic.backend.entity.enums.State;
import cat.copernic.backend.entity.enums.Validation;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author carlo
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SuperBuilder
public class Route {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_route;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    private LocalDateTime end_date;

    //distance on km
    private double distance;

    //average speed on km/h
    private double average_speed;

    private Time total_time;

    private Validation validation_state;

    private int generated_balance;

    private State state;

    /*@OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<GpsPoint> gpsPoints;
*/

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    @JsonBackReference
    // 💥 ESTA LÍNEA ROMPE EL BUCLE INFINITO
    private User user;

}
