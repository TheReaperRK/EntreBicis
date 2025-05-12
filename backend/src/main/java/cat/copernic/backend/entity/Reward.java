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
 * Entitat que representa una recompensa del sistema.
 * Una recompensa pot estar disponible o associada a un usuari concret,
 * i conté informació com el preu en punts, el nom, la descripció, el comerç associat,
 * l’estat actual (disponible, assignada, recollida) i les dates de sol·licitud i recollida.
 * 
 * Aquesta entitat és utilitzada tant a la vista web com al frontend mòbil.
 * 
 * @author carlo
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SuperBuilder
public class Reward {

    /**
     * Identificador únic de la recompensa (autogenerat).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_id", unique = true, nullable = false)
    private Long id;

    /**
     * Usuari que ha sol·licitat la recompensa.
     * Pot ser null si la recompensa encara no ha estat assignada.
     */
    @ManyToOne
    @JoinColumn(name = "email_user", nullable = true)
    @JsonBackReference
    private User user;

    /**
     * Preu de la recompensa en punts.
     */
    @Column(nullable = false)
    private double preu;

    /**
     * Nom de la recompensa.
     */
    @Column(length = 50, nullable = false)
    private String nom;

    /**
     * Direcció del comerç on es pot recollir la recompensa.
     */
    @Column(length = 150, nullable = false)
    private String direccio;

    /**
     * Nom del comerç o establiment associat a la recompensa.
     */
    @Column(length = 100)
    private String comerc;

    /**
     * Descripció breu de la recompensa.
     */
    @Column(length = 255)
    private String descripcio;

    /**
     * Data i hora en què l’usuari ha sol·licitat la recompensa.
     * Pot ser null si encara no ha estat sol·licitada.
     */
    @Column(name = "request_data", nullable = true)
    private LocalDateTime dataSolicitud;

    /**
     * Data i hora en què l’usuari ha recollit la recompensa.
     * Pot ser null si encara no s’ha recollit.
     */
    @Column(name = "collect_data", nullable = true)
    private LocalDateTime dataRecollida;

    /**
     * Estat actual de la recompensa:
     * - DISPONIBLE: encara no sol·licitada,
     * - RESERVADA o ASSIGNADA: pendent de recollida,
     * - RECOLLIDA: ja recollida per l’usuari.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardStatus estat;
}
