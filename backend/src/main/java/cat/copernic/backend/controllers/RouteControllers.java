/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers;

import cat.copernic.backend.entity.DTO.GpsPointDTOLight;
import cat.copernic.backend.entity.DTO.RouteUserSpeedDistanceDTO;
import cat.copernic.backend.entity.GpsPoint;
import cat.copernic.backend.entity.Reward;
import cat.copernic.backend.entity.Route;
import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.enums.Validation;
import cat.copernic.backend.logic.RouteService;
import cat.copernic.backend.repository.GpsPointRepo;
import cat.copernic.backend.repository.RouteRepo;
import cat.copernic.backend.repository.UserRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador per gestionar les rutes (web admin).
 * Permet llistar totes les rutes, veure'n el detall, validar-les o invalidar-les manualment.
 * Les validacions afecten directament al saldo del ciclista.
 * 
 * Aquesta funcionalitat és exclusiva per a l'administrador.
 * 
 * @author carlo
 */
@Controller
@RequestMapping("/routes")
public class RouteControllers {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RouteControllers.class);

    @Autowired
    private RouteRepo routeRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GpsPointRepo gpsPointRepo;

    @Autowired
    private RouteService routeService;

    /**
     * Mostra la llista completa de rutes registrades al sistema.
     *
     * @param model model utilitzat per passar la llista de rutes a la vista
     * @return vista de llista de rutes
     */
    @GetMapping("/list")
    public String listRoutes(Model model) {
        logger.info("Consulta de llista completa de rutes");
        List<RouteUserSpeedDistanceDTO> routes = routeService.getAllRoutesAsDTO();
        model.addAttribute("routes", routes);
        return "routes/routes-list";
    }

    /**
     * Mostra les rutes registrades per un usuari específic.
     *
     * @param mail correu de l'usuari
     * @param model model per passar dades a la vista
     * @return vista de llista de rutes per usuari
     */
    @GetMapping("/list/{mail}")
    public String mostrarRutesPerUsuari(@PathVariable String mail, Model model) {
        logger.info("Consulta de rutes per a l'usuari {}", mail);
        try {
            User user = userRepo.getById(mail);
            List<RouteUserSpeedDistanceDTO> routes = routeService.getAllUserRoutesAsDTO(user);
            model.addAttribute("routes", routes);
            model.addAttribute("mail", mail);
        } catch (Exception e) {
            logger.error("Error en carregar rutes de l'usuari {}: {}", mail, e.getMessage());
        }
        return "routes/routes-list";
    }

    /**
     * Mostra el detall d'una ruta concreta, incloent els punts GPS i càlculs relacionats amb la velocitat màxima.
     *
     * @param id identificador de la ruta
     * @param mail correu de l'usuari (opcional, per mostrar context)
     * @param model model per passar dades a la vista
     * @return vista amb el detall de la ruta
     */
    @GetMapping("/view/{id}")
    public String viewRoute(@PathVariable("id") Long id, @RequestParam(required = false) String mail, Model model) {
        logger.info("Visualització de detall de ruta amb id {}", id);
        try {
            Route route = routeService.getRouteById(id);
            List<GpsPoint> gpsPoints = gpsPointRepo.findByRoute(route);

            model.addAttribute("route", route);
            model.addAttribute("mail", mail);
            model.addAttribute("velocitatMaxima", routeService.calcularVelocitatMaxima(route));
            model.addAttribute("velocitatMaximaColor", routeService.obtenirColorVelocitatMaxima(route));

            List<GpsPointDTOLight> gpsPointDTOs = gpsPoints.stream()
                    .map(p -> new GpsPointDTOLight(p.getLatitud().doubleValue(), p.getLongitud().doubleValue()))
                    .toList();

            model.addAttribute("gpsPoints", gpsPointDTOs);
        } catch (Exception e) {
            logger.error("Error en mostrar ruta amb id {}: {}", id, e.getMessage());
        }

        return "routes/route-detail";
    }

    /**
     * Acció per validar una ruta. L'estat passa a VALIDATED i s'incrementa el saldo de l'usuari.
     *
     * @param id identificador de la ruta
     * @param mail correu de l'usuari (opcional, per redirigir a la seva vista)
     * @param redirectAttributes missatges flash de resultat per a la vista
     * @return redirecció a la vista de detall de la ruta
     */
    @PostMapping("/validate/{id}")
    public String validateRoute(@PathVariable Long id,
                                @RequestParam(required = false) String mail,
                                RedirectAttributes redirectAttributes) {
        logger.info("Intent de validació de la ruta amb id {}", id);
        try {
            Route route = routeService.getRouteById(id);
            User user = route.getUser();

            user.setBalance(user.getBalance() + route.getGenerated_balance());
            route.setValidation_state(Validation.VALIDATED);

            routeRepo.save(route);
            userRepo.save(user);

            logger.info("Ruta amb id {} validada correctament per l'usuari {}", id, user.getMail());
            redirectAttributes.addFlashAttribute("successMessage", "La ruta s'ha validat correctament.");
        } catch (Exception e) {
            logger.error("Error en validar la ruta amb id {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error en validar la ruta.");
        }

        return (mail != null) ? "redirect:/routes/view/" + id + "?mail=" + mail : "redirect:/routes/view/" + id;
    }

    /**
     * Acció per invalidar una ruta. Si ja estava validada, es resta el saldo generat a l'usuari.
     * Si no té saldo suficient, no es pot invalidar.
     *
     * @param id identificador de la ruta
     * @param mail correu de l’usuari (opcional, per redirigir a la seva vista)
     * @param redirectAttributes missatges flash de resultat per a la vista
     * @return redirecció a la vista de detall de la ruta
     */
    @PostMapping("/invalidate/{id}")
    public String invalidateRoute(@PathVariable Long id,
                                  @RequestParam(required = false) String mail,
                                  RedirectAttributes redirectAttributes) {
        logger.info("Intent d'invalidació de la ruta amb id {}", id);
        try {
            Route route = routeService.getRouteById(id);

            if (route.getValidation_state() == Validation.VALIDATED) {
                User user = route.getUser();

                if (user.getBalance() < route.getGenerated_balance()) {
                    logger.warn("No es pot invalidar la ruta amb id {} per saldo insuficient de l'usuari {}", id, user.getMail());
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "La ruta no pot ser invalidada, ja que l'usuari no compta amb els punts suficients per restar-li.");
                    return "redirect:/routes/view/" + id;
                }

                user.setBalance(user.getBalance() - route.getGenerated_balance());
                userRepo.save(user);
            }

            route.setValidation_state(Validation.INVALIDATED);
            routeRepo.save(route);

            logger.info("Ruta amb id {} invalidada correctament", id);
            redirectAttributes.addFlashAttribute("successMessage", "La ruta s'ha invalidat correctament.");
        } catch (Exception e) {
            logger.error("Error en invalidar la ruta amb id {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error en invalidar la ruta.");
        }

        return (mail != null) ? "redirect:/routes/view/" + id + "?mail=" + mail : "redirect:/routes/view/" + id;
    }
}
