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
 * Entitat que representa la configuració global del sistema.
 * Inclou valors predeterminats per la validació de rutes, recompenses i punts.
 * Aquesta entitat només hauria de tenir una instància amb `nameConfig = "default"`.
 * 
 * Els valors poden ser modificats per l’administrador des de la interfície web.
 * 
 * Taula: system_parameters
 * 
 * @author carlo
 */
@Entity
@Table(name = "system_parameters")
@Getter
@Setter
@NoArgsConstructor
public class SystemParameters {

    /**
     * Clau de configuració. Sempre hauria de ser "default".
     */
    @Id
    @Column(name = "name_config", length = 20, nullable = false)
    private String nameConfig = "default";

    /**
     * Velocitat mitjana vàlida màxima (km/h) per considerar una ruta vàlida.
     * Si una ruta supera aquest valor, pot ser descartada o revisada.
     */
    @Column(name = "average_valid_speed", nullable = false, columnDefinition = "DOUBLE DEFAULT 25")
    private double averageValidSpeed = 25;

    /**
     * Temps màxim d’inactivitat (en segons) abans de finalitzar automàticament una ruta.
     */
    @Column(name = "max_inactivity", nullable = false, columnDefinition = "INT DEFAULT 300")
    private int maxInactivity = 300;

    /**
     * Conversió de km a punts. 
     * Ex: 1 km = 100 punts si aquest valor és 100.
     */
    @Column(name = "points_per_kilometer", nullable = false, columnDefinition = "DOUBLE DEFAULT 100")
    private double pointsPerKilometer = 100;

    /**
     * Temps límit (en hores) per recollir una recompensa des de la seva sol·licitud.
     */
    @Column(name = "pickup_time", nullable = false, columnDefinition = "INT DEFAULT 72")
    private int pickupTime = 72;
}

