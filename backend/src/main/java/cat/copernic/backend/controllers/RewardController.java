/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers;

/**
 *
 * @author carlo
 */

import cat.copernic.backend.entity.Reward;
import cat.copernic.backend.entity.enums.RewardStatus;
import cat.copernic.backend.logic.RewardLogic;
import cat.copernic.backend.logic.UserLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/rewards")
public class RewardController {

    @Autowired
    private RewardLogic rewardLogic;

    @Autowired
    private UserLogic userLogic; // Necesario si quieres seleccionar usuario en el formulario

    // ✅ Lista de recompensas
    @GetMapping("/list")
    public String listRewards(Model model) {
        model.addAttribute("rewards", rewardLogic.getAllRewards());
        return "rewards/rewards-list";
    }

    // ✅ Formulario de creación
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("reward", new Reward());
        model.addAttribute("formMode", "create");
        model.addAttribute("users", userLogic.getAllUsers()); // para selector de usuarios
        return "rewards/reward-form";
    }

    // ✅ Formulario de edición
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Reward reward = rewardLogic.getRewardById(id);
        if (reward == null) {
            return "redirect:/rewards/list";
        }
        model.addAttribute("reward", reward);
        model.addAttribute("formMode", "edit");
        model.addAttribute("users", userLogic.getAllUsers());
        return "rewards/reward-form";
    }

    // ✅ Crear recompensa
    @PostMapping("/create")
    public String createReward(@ModelAttribute Reward reward) {
        reward.setEstat(RewardStatus.AVAILABLE);
        rewardLogic.saveReward(reward);
        return "redirect:/rewards/list";
    }

    // ✅ Editar recompensa
    @PostMapping("/edit/{id}")
    public String editReward(@PathVariable("id") Long id, @ModelAttribute Reward reward) {
        reward.setId(id); // asegúrate de mantener el ID
        rewardLogic.saveReward(reward);
        return "redirect:/rewards/list";
    }

    // ✅ Eliminar recompensa
    @GetMapping("/delete/{id}")
    public String deleteReward(@PathVariable("id") Long id) {
        rewardLogic.deleteReward(id);
        return "redirect:/rewards/list";
    }
}
