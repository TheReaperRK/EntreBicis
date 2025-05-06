/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package cat.copernic.backend.entity.enums;

import static cat.copernic.backend.entity.enums.State.ENDED;
import static cat.copernic.backend.entity.enums.State.STARTED;

/**
 *
 * @author carlo
 */
public enum RewardStatus {
    AVAILABLE, //disponible
    PENDING, // Pendiente
    ACCEPTED, // Aceptada
    COLLECTED, // Recogida
    CANCELED; // Cancelada

    @Override
    public String toString() {
        return switch (this) {
            case AVAILABLE ->
                "Disponible";
            case PENDING ->
                "Pendent";
            case ACCEPTED ->
                "Acceptada";
            case COLLECTED ->
                "Recollida";
            case CANCELED ->
                "Cancelada";
        };
    }
}
