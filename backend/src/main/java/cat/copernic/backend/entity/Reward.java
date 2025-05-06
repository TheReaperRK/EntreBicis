/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity;

import cat.copernic.backend.entity.enums.RewardStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 *
 * @author carlo
 */


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SuperBuilder
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "email_user", nullable = true)
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    private double preu;

    @Column(length = 50, nullable = false)
    private String nom;

    @Column(length = 150, nullable = false)
    private String direccio;

    @Column(length = 100)
    private String comerc;
    
    @Column(length = 255)
    private String descripcio;

    @Column(name = "request_data", nullable = true)
    private LocalDateTime dataSolicitud;

    @Column(name = "collect_data", nullable = true)
    private LocalDateTime dataRecollida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardStatus estat;
    
}