package cat.copernic.frontend.core.models.enums;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

/**
 *
 * @author carlo
 */
public enum Validation {
    NOT_CHECKED, VALIDATED, INVALIDATED;

    @Override
    public String toString() {
        switch (this) {
            case NOT_CHECKED:
                return "No verificat";
            case VALIDATED:
                return "Validat";
            case INVALIDATED:
                return "Invalidat";
            default:
                return super.toString();
        }
    }
}
