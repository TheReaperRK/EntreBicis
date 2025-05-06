/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.mappers;

/**
 *
 * @author carlo
 */
import cat.copernic.backend.entity.DTO.RouteDtoClear;
import cat.copernic.backend.entity.Route;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class RouteMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public RouteDtoClear toDtoClear(Route route) {
        return new RouteDtoClear(
                route.getId_route(),
                route.getDistance(),
                route.getAverage_speed(),
                route.getTotal_time() != null ? route.getTotal_time().toString() : null,
                route.getGenerated_balance(),
                route.getUser().getMail(),
                route.getValidation_state()
        );
    }
}
