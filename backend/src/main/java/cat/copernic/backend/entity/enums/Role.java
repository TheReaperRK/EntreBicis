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
public enum Role {
    ADMIN,USER;
    
    @Override
    public String toString() {
        return switch (this) {
            case USER ->
                "Usuari";
            case ADMIN ->
                "Admin";
        };
    }
}
