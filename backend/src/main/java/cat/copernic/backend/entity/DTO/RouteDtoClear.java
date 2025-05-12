/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.DTO;

import cat.copernic.backend.entity.enums.State;
import cat.copernic.backend.entity.enums.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO simplificat per representar informació bàsica d’una ruta.
 * 
 * Aquesta classe s’utilitza principalment en pantalles on es vol mostrar
 * un resum de les rutes (com el llistat), sense incloure detalls ni punts GPS.
 * 
 * Inclou valors essencials com la distància, velocitat mitjana, temps total,
 * validació i correu de l’usuari.
 * 
 * @author carlo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDtoClear {

    /** Identificador únic de la ruta */
    private Long idRouteDTO;

    /** Distància total recorreguda en km */
    private double distance;

    /** Velocitat mitjana de la ruta en km/h */
    private double averageSpeed;

    /** Temps total de la ruta (format HH:mm:ss) */
    private String totalTime;

    /** Punts obtinguts per aquesta ruta */
    private int generatedBalance;

    /** Correu electrònic de l’usuari que ha realitzat la ruta */
    private String userEmail;

    /** Estat de validació de la ruta (VALIDATED, INVALIDATED o PENDING) */
    private Validation validation_state;
}
