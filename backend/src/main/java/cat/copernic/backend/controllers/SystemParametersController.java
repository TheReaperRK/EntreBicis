/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers;

import cat.copernic.backend.entity.SystemParameters;
import cat.copernic.backend.logic.SystemParametersLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador per gestionar els paràmetres del sistema (només accessible per
 * l’administrador). Permet visualitzar i modificar valors globals com la
 * velocitat màxima, temps màxim d’aturada, etc. Aquest formulari s’accedeix des
 * del panell d’administració.
 *
 * @author carlo
 */
@Controller
@RequestMapping("/admin/system-parameters")
public class SystemParametersController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SystemParametersController.class);

    private final SystemParametersLogic logic;

    /**
     * Constructor que rep la lògica per gestionar els paràmetres del sistema.
     *
     * @param logic instància del servei de lògica de paràmetres
     */
    public SystemParametersController(SystemParametersLogic logic) {
        this.logic = logic;
    }

    /**
     * Mostra el formulari amb els paràmetres actuals del sistema. Si no
     * existeixen, es crea una nova instància amb valors per defecte.
     *
     * @param model model per passar l’objecte {@code SystemParameters} a la
     * vista
     * @return nom de la plantilla Thymeleaf del formulari
     */
    @GetMapping
    public String showForm(Model model) {
        logger.info("Accés al formulari de paràmetres del sistema");
        try {
            SystemParameters parameters = logic.getParameters();

            if (parameters == null) {
                logger.warn("No hi ha paràmetres del sistema definits, es crea una nova instància");
                parameters = new SystemParameters();
                logic.updateParameters(parameters);
            }

            model.addAttribute("params", parameters);
            return "systemParameters/system-parameters";
        } catch (Exception e) {
            logger.error("Error en carregar els paràmetres del sistema: {}", e.getMessage());
            return "redirect:/error"; // alternativa: mostrar error informatiu a la vista
        }
    }

    /**
     * Rep els nous valors dels paràmetres i els desa.
     *
     * @param updated objecte {@code SystemParameters} amb els valors modificats
     * des del formulari
     * @return redirecció al formulari amb missatge d’èxit o d’error
     */
    @PostMapping
    public String updateParameters(@ModelAttribute("params") SystemParameters updated) {
        try {
            logic.updateParameters(updated);
            logger.info("Paràmetres del sistema actualitzats correctament");
            return "redirect:/admin/system-parameters?success";
        } catch (Exception e) {
            logger.error("Error en actualitzar els paràmetres del sistema: {}", e.getMessage());
            return "redirect:/admin/system-parameters?error";
        }
    }
}
