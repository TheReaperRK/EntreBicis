/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.mappers;

import cat.copernic.backend.entity.DTO.RouteDtoClear;
import cat.copernic.backend.entity.Route;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

/**
 * Classe mapejadora per convertir entitats `Route` a objectes DTO de tipus `RouteDtoClear`.
 * Aquesta conversió simplifica la representació d'una ruta, deixant fora els punts GPS
 * i altres dades pesades, especialment pensat per a llistats o resums.
 * 
 * S'utilitza, per exemple, a la vista de la llista de rutes (`routes-list.html`)
 * o a l'API quan no cal carregar informació completa de la ruta.
 * 
 * @author carlo
 */
@Component
public class RouteMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Converteix una entitat `Route` a un DTO simplificat `RouteDtoClear`.
     * 
     * @param route la ruta completa de la base de dades
     * @return objecte `RouteDtoClear` amb les dades més rellevants de la ruta
     */
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
