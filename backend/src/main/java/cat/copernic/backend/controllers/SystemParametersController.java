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
 *
 * @author carlo
 */
@Controller
@RequestMapping("/admin/system-parameters")
public class SystemParametersController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SystemParametersController.class);

    private final SystemParametersLogic logic;

    public SystemParametersController(SystemParametersLogic logic) {
        this.logic = logic;
    }

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
            return "redirect:/error"; // o mostra un error informatiu a la vista
        }
    }

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
