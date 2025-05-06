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
 *
 * @author carlo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDtoClear {

    private Long idRouteDTO;
    private double distance;
    private double averageSpeed;
    private String totalTime;
    private int generatedBalance;
    private String userEmail;
    private Validation validation_state;

}
