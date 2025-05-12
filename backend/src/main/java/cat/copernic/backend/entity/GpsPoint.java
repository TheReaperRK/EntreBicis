/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Entitat que representa un punt GPS individual dins d’una ruta.
 * Cada punt conté coordenades (latitud i longitud) i una marca de temps (timestamp).
 * Està associat a una única ruta mitjançant relació @ManyToOne.
 * 
 * Aquesta entitat s’utilitza tant per visualitzar la ruta recorreguda
 * com per calcular distància, velocitat mitjana i velocitat màxima.
 * 
 * La precisió de les coordenades està limitada a 6 decimals.
 * 
 * S’ignoren referències circulars en JSON gràcies a les anotacions `@JsonBackReference` i `@JsonIgnoreProperties`.
 * 
 * @author carlo
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SuperBuilder
public class GpsPoint {

    /**
     * Identificador únic del punt GPS (autogenerat).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_punt", unique = true, nullable = false)
    private Long idPunt;

    /**
     * Ruta a la qual pertany aquest punt GPS.
     * La relació és obligatòria.
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "id_ruta", nullable = false)
    private Route route;

    /**
     * Coordenada de latitud (fins a 6 decimals).
     */
    @Column(precision = 10, scale = 6, nullable = false)
    private BigDecimal latitud;

    /**
     * Coordenada de longitud (fins a 6 decimals).
     */
    @Column(precision = 10, scale = 6, nullable = false)
    private BigDecimal longitud;

    /**
     * Data i hora exacta en què es va registrar el punt GPS.
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
