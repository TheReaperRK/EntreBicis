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
 *
 * @author carlo
 */
@Controller
@RequestMapping("/routes")
public class RouteControllers {

    @Autowired
    private RouteRepo routeRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GpsPointRepo gpsPointRepo;

    @Autowired
    private RouteService routeService;

    @GetMapping("/list")
    public String listRoutes(Model model) {
        List<RouteUserSpeedDistanceDTO> routes = routeService.getAllRoutesAsDTO();
        model.addAttribute("routes", routes);
        return "routes/routes-list";
    }

    @GetMapping("/list/{mail}")
    public String mostrarRutesPerUsuari(@PathVariable String mail, Model model) {
        User user = userRepo.getById(mail);

        List<RouteUserSpeedDistanceDTO> routes = routeService.getAllUserRoutesAsDTO(user);
        model.addAttribute("routes", routes);
        model.addAttribute("mail", mail); // ✅ pasamos el correo al modelo

        return "routes/routes-list"; //el mateix que usem per la llista normal
    }

    @GetMapping("/view/{id}")
    public String viewRoute(@PathVariable("id") Long id, @RequestParam(required = false) String mail, Model model) {
        Route route = routeService.getRouteById(id);
        List<GpsPoint> gpsPoints = gpsPointRepo.findByRoute(route);

        model.addAttribute("route", route);
        model.addAttribute("mail", mail); // ✅ pasamos el correo al modelo
        model.addAttribute("velocitatMaxima", routeService.calcularVelocitatMaxima(route));
        System.out.println(routeService.calcularVelocitatMaxima(route));
        model.addAttribute("velocitatMaximaColor", routeService.obtenirColorVelocitatMaxima(route));

        List<GpsPointDTOLight> gpsPointDTOs = gpsPointRepo.findByRoute(route).stream()
                .map(p -> new GpsPointDTOLight(p.getLatitud().doubleValue(), p.getLongitud().doubleValue()))
                .toList();
        model.addAttribute("gpsPoints", gpsPointDTOs);

        return "routes/route-detail";
    }

    @PostMapping("/validate/{id}")
    public String validateRoute(@PathVariable Long id, @RequestParam(required = false) String mail, RedirectAttributes redirectAttributes) {
        Route route = routeService.getRouteById(id);

        User user = route.getUser();
        user.setBalance(user.getBalance() + route.getGenerated_balance());

        route.setValidation_state(Validation.VALIDATED);

        routeRepo.save(route);
        userRepo.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "La ruta s'ha validat correctament.");

        return (mail != null) ? "redirect:/routes/view/" + id + "?mail=" + mail : "redirect:/routes/view/" + id;
    }

    @PostMapping("/invalidate/{id}")
    public String invalidateRoute(@PathVariable Long id, @RequestParam(required = false) String mail, RedirectAttributes redirectAttributes) {
        Route route = routeService.getRouteById(id);

        if (route.getValidation_state() == Validation.VALIDATED) {
            User user = route.getUser();

            if (user.getBalance() < route.getGenerated_balance()) {
                redirectAttributes.addFlashAttribute("errorMessage", "La ruta no pot ser invalidada, ja que l'usuari no compta amb els punts suficients per restar-li.");
                return "redirect:/routes/view/" + id;
            }

            user.setBalance(user.getBalance() - route.getGenerated_balance());
            userRepo.save(user);
        }

        route.setValidation_state(Validation.INVALIDATED);
        routeRepo.save(route);

        redirectAttributes.addFlashAttribute("successMessage", "La ruta s'ha invalidat correctament.");
        return (mail != null) ? "redirect:/routes/view/" + id + "?mail=" + mail : "redirect:/routes/view/" + id;
    }
}
