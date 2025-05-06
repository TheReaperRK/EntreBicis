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
import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.enums.RewardStatus;
import cat.copernic.backend.logic.RewardLogic;
import cat.copernic.backend.logic.UserLogic;
import cat.copernic.backend.repository.RewardRepo;
import cat.copernic.backend.repository.UserRepo;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rewards")
public class RewardController {

    @Autowired
    private RewardLogic rewardLogic;

    @Autowired
    private RewardRepo rewardRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserLogic userLogic; // Necesario si quieres seleccionar usuario en el formulario

    // ✅ Lista de recompensas
    @GetMapping("/list")
    public String listRewards(Model model) {
        model.addAttribute("rewards", rewardLogic.getAllRewards());
        model.addAttribute("showCreateButton", true);
        model.addAttribute("userMail", null);
        
        return "rewards/rewards-list";
    }

    @GetMapping("/list/{mail}")
    public String listUserRewards(@PathVariable String mail, Model model) {
        User user = userRepo.findByMail(mail);

        model.addAttribute("rewards", rewardLogic.getAllUserRewards(user));
        model.addAttribute("showCreateButton", false);
        model.addAttribute("userMail", mail);
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

    @GetMapping("/delete/{id}")
    public String deleteReward(@PathVariable("id") Long id,
            @RequestParam(required = false) String mail,
            RedirectAttributes redirectAttributes) {

        if (rewardLogic.deleteable(id)) {
            rewardLogic.deleteReward(id);
            redirectAttributes.addFlashAttribute("successMessage", "Recompensa esborrada correctament");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "La recompensa no pot ser esborrada, ja que està assignada a un usuari");
        }

        return (mail != null) ? "redirect:/rewards/list/" + mail : "redirect:/rewards/list";
    }

    @GetMapping("/accept/{id}")
    public String acceptReward(@PathVariable("id") Long id,
            @RequestParam(required = false) String mail) {
        Reward reward = rewardLogic.getRewardById(id);
        User user = reward.getUser();

        reward.setEstat(RewardStatus.ACCEPTED);
        reward.setDataSolicitud(LocalDateTime.now());

        rewardLogic.rewardAccept(id, user);
        rewardRepo.save(reward);

        return (mail != null) ? "redirect:/rewards/list/" + mail : "redirect:/rewards/list";
    }

}
