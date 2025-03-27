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
    private final SystemParametersLogic logic;

    public SystemParametersController(SystemParametersLogic logic) {
        this.logic = logic;
    }

    @GetMapping
    public String showForm(Model model) {
        SystemParameters parameters = logic.getParameters();
        model.addAttribute("params", parameters);
        return "systemParameters/system-parameters";
    }

    @PostMapping
    public String updateParameters(@ModelAttribute("params") SystemParameters updated) {
        logic.updateParameters(updated);
        return "redirect:/admin/system-parameters?success";
    }
}
