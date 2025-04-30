/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package cat.copernic.backend.entity.enums;

/**
 *
 * @author carlo
 */
public enum Validation {
    NOT_CHECKED, VALIDATED, INVALIDATED;
    
    @Override
    public String toString() {
        return switch (this) {
            case NOT_CHECKED ->
                "No verificat";
            case VALIDATED ->
                "Validat";
            case INVALIDATED ->
                "Invalidat";
        };
    }
}
