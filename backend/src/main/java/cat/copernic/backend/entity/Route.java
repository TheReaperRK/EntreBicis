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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Entitat que representa una ruta registrada per un usuari.
 * Una ruta conté informació temporal (inici/fi), mètrica (distància, temps, velocitat),
 * estat de validació i punts GPS associats.
 * 
 * Aquesta entitat es fa servir tant per visualització com per càlculs de puntuació.
 * 
 * Nota: els punts GPS associats poden estar descomentats segons la lògica de serialització.
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

    /**
     * Identificador únic de la ruta (autogenerat).
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_route;

    /**
     * Data i hora d’inici de la ruta.
     */
    @Column(name = "start_date")
    private LocalDateTime startDate;

    /**
     * Data i hora de finalització de la ruta.
     */
    private LocalDateTime end_date;

    /**
     * Distància total recorreguda, en kilòmetres.
     */
    private double distance;

    /**
     * Velocitat mitjana durant la ruta, en km/h.
     */
    private double average_speed;

    /**
     * Temps total de la ruta (com a objecte {@link java.sql.Time}).
     * S’utilitza per mostrar la duració en format hh:mm:ss.
     */
    private Time total_time;

    /**
     * Estat de validació de la ruta (VALIDATED / INVALIDATED / PENDING).
     */
    @Enumerated(EnumType.STRING)
    private Validation validation_state;

    /**
     * Saldo (punts) generat per aquesta ruta, calculat a partir de la distància.
     */
    private int generated_balance;

    /**
     * Estat intern de la ruta: activa, finalitzada, etc.
     * (Depèn de l'enum {@link State}).
     */
    private State state;

    /**
     * Usuari al qual pertany aquesta ruta.
     * Aquesta relació és obligatòria.
     */
    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    @JsonBackReference // Evita bucle infinit a la serialització
    private User user;

    /*
    // Llista de punts GPS associats a la ruta.
    // Aquesta relació és bidireccional (GpsPoint → Route)
    // Es pot descomentar si es necessita la llista directament.
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<GpsPoint> gpsPoints;
    */
}

