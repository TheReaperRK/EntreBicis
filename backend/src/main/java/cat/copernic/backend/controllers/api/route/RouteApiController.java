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

    @Autowired
    private RouteService routeService;

    @Autowired
    private SystemParametersRepo systemParamRepo;

    @Autowired
    private UserLogic userLogic; // Necesario si quieres seleccionar usuario en el formulario

    @Autowired
    private RouteRepo routeRepo;

    @PostMapping("/send")
    public ResponseEntity<Route> sendRoute(@RequestBody RouteDTO routeDTO) {
        System.out.println(">>> ENTRANDO A ROUTE API <<<");

        routeService.saveRoute(routeDTO); // lógica para convertir y guardar
        Route route = routeService.getLastRoute(routeDTO.getUserEmail());

        if (route != null) {
            System.out.println("ruta enviada");
            return ResponseEntity.ok().body(route);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // <--- Esto evita devolver HTML u otros errores no parseables
        }
    }

    @PostMapping("/points")
    public ResponseEntity<Void> sendPoints(@RequestBody List<GpsPointDTO> gpsPoints) {
        routeService.savePoints(gpsPoints); // lógica para guardar puntos GPS
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<?> getAllRoutesByUser(Principal principal) {
        try {
            System.out.println("dentro");
            // Obtener el usuario autenticado por su correo
            User usuari = userLogic.getUserByMail(principal.getName());

            System.out.println(usuari);
            // Obtener la lista de rutas del usuario
            List<RouteDtoClear> userRoutes = routeService.getRoutesByUser(usuari);

            return ResponseEntity.ok(userRoutes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en obtenir les rutes de l'usuari: " + e.getMessage());
        }
    }

    @GetMapping("/route/{id}")
    public ResponseEntity<?> getRouteById(@PathVariable Long id, Principal principal) {
        System.out.println(">>> Entrando en getRouteById con ID: " + id);
        if (principal == null) {
            System.out.println(">>> Principal es NULL: No autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuari no autenticat");
        }
        try {
            // Primero, buscar la ruta por ID
            Optional<Route> optionalRoute = routeRepo.findById(id);

            if (optionalRoute.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruta no trobada.");
            }

            Route route = optionalRoute.get();

            // Verificar si la ruta pertenece al usuario autenticado (seguridad)
            if (!route.getUser().getMail().equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accés denegat.");
            }

            // Convertir a DTO completo
            RouteDTO routeDTO = routeService.convertToDto(route);

            return ResponseEntity.ok(routeDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en obtenir la ruta: " + e.getMessage());
        }
    }
}
