/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package cat.copernic.backend.entity.enums;

import static cat.copernic.backend.entity.enums.Validation.INVALIDATED;
import static cat.copernic.backend.entity.enums.Validation.NOT_CHECKED;
import static cat.copernic.backend.entity.enums.Validation.VALIDATED;

/**
 *
 * @author carlo
 */
public enum State {
    STARTED, ENDED;
    
    @Override
    public String toString() {
        return switch (this) {
            case STARTED ->
                "Començada";
            case ENDED ->
                "Acabada";
        };
    }
}
