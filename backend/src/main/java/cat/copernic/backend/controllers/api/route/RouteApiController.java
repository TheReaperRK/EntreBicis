/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers.api.route;

import cat.copernic.backend.entity.DTO.GpsPointDTO;
import cat.copernic.backend.entity.DTO.RouteDTO;
import cat.copernic.backend.entity.DTO.RouteDtoClear;
import cat.copernic.backend.entity.Route;
import cat.copernic.backend.entity.SystemParameters;
import cat.copernic.backend.entity.User;
import cat.copernic.backend.logic.RouteService;
import cat.copernic.backend.logic.SystemParametersLogic;
import cat.copernic.backend.logic.UserLogic;
import cat.copernic.backend.repository.RouteRepo;
import cat.copernic.backend.repository.SystemParametersRepo;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author carlo
 */
@RestController
@RequestMapping("/api/routes")
public class RouteApiController {

    private static final Logger logger = LoggerFactory.getLogger(RouteApiController.class);

    @Autowired
    private RouteService routeService;

    @Autowired
    private SystemParametersRepo systemParamRepo;

    @Autowired
    private UserLogic userLogic;

    @Autowired
    private RouteRepo routeRepo;

    @PostMapping("/send")
    public ResponseEntity<Route> sendRoute(@RequestBody RouteDTO routeDTO) {
        logger.info("Rebuda petició per enviar una nova ruta de l'usuari {}", routeDTO.getUserEmail());

        try {
            routeService.saveRoute(routeDTO);
            Route route = routeService.getLastRoute(routeDTO.getUserEmail());

            if (route != null) {
                logger.info("Ruta guardada correctament per l'usuari {}", routeDTO.getUserEmail());
                return ResponseEntity.ok().body(route);
            } else {
                logger.error("Error: no s'ha pogut recuperar la ruta després de guardar.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (Exception e) {
            logger.error("Error al guardar la ruta: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @PostMapping("/points")
    public ResponseEntity<Void> sendPoints(@RequestBody List<GpsPointDTO> gpsPoints) {
        logger.info("Rebuda petició per guardar {} punts GPS", gpsPoints.size());

        try {
            routeService.savePoints(gpsPoints);
            logger.debug("Punts GPS guardats correctament");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al guardar punts GPS: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<?> getAllRoutesByUser(Principal principal) {
        logger.info("Rebuda petició per obtenir totes les rutes de l'usuari autenticat");

        try {
            User usuari = userLogic.getUserByMail(principal.getName());
            logger.debug("Usuari recuperat: {}", usuari.getMail());

            List<RouteDtoClear> userRoutes = routeService.getRoutesByUser(usuari);
            logger.info("S'han trobat {} rutes per l'usuari {}", userRoutes.size(), usuari.getMail());

            return ResponseEntity.ok(userRoutes);

        } catch (Exception e) {
            logger.error("Error en obtenir les rutes de l'usuari: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en obtenir les rutes de l'usuari: " + e.getMessage());
        }
    }

    @GetMapping("/route/{id}")
    public ResponseEntity<?> getRouteById(@PathVariable Long id, Principal principal) {
        logger.info("Rebuda petició per obtenir la ruta amb ID {}", id);

        if (principal == null) {
            logger.warn("Accés denegat: principal null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuari no autenticat");
        }

        try {
            Optional<Route> optionalRoute = routeRepo.findById(id);

            if (optionalRoute.isEmpty()) {
                logger.warn("Ruta amb ID {} no trobada", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruta no trobada.");
            }

            Route route = optionalRoute.get();

            if (!route.getUser().getMail().equals(principal.getName())) {
                logger.warn("Accés denegat: l'usuari {} no és propietari de la ruta {}", principal.getName(), id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accés denegat.");
            }

            RouteDTO routeDTO = routeService.convertToDto(route);
            logger.info("Ruta amb ID {} retornada correctament", id);

            return ResponseEntity.ok(routeDTO);

        } catch (Exception e) {
            logger.error("Error en obtenir la ruta amb ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en obtenir la ruta: " + e.getMessage());
        }
    }
}

