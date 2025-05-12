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

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RewardController.class);

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
        logger.info("Visualització de la llista de recompenses (admin)");
        model.addAttribute("rewards", rewardLogic.getAllRewards());
        model.addAttribute("showCreateButton", true);
        model.addAttribute("userMail", null);

        return "rewards/rewards-list";
    }

    @GetMapping("/list/{mail}")
    public String listUserRewards(@PathVariable String mail, Model model) {
        logger.info("Visualització de recompenses per a l'usuari {}", mail);

        User user = userRepo.findByMail(mail);

        model.addAttribute("rewards", rewardLogic.getAllUserRewards(user));
        model.addAttribute("showCreateButton", false);
        model.addAttribute("userMail", mail);
        return "rewards/rewards-list";
    }

    // ✅ Formulario de creación
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        logger.info("Accés al formulari de creació de recompensa");

        model.addAttribute("reward", new Reward());
        model.addAttribute("formMode", "create");
        model.addAttribute("users", userLogic.getAllUsers()); // para selector de usuarios
        return "rewards/reward-form";
    }

    // ✅ Formulario de edición
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        logger.info("Accés al formulari d'edició per la recompensa amb id {}", id);

        Reward reward = rewardLogic.getRewardById(id);
        if (reward == null) {
            logger.warn("Intent d'editar recompensa inexistent amb id {}", id);

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
        logger.info("Recompensa creada amb nom '{}', assignada a '{}'", reward.getNom(), reward.getUser() != null ? reward.getUser().getMail() : "cap");

        return "redirect:/rewards/list";
    }

    // ✅ Editar recompensa
    @PostMapping("/edit/{id}")
    public String editReward(@PathVariable("id") Long id, @ModelAttribute Reward reward) {
        reward.setId(id); // asegúrate de mantener el ID
        rewardLogic.saveReward(reward);
        logger.info("Recompensa editada amb id {}", id);

        return "redirect:/rewards/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteReward(@PathVariable("id") Long id,
            @RequestParam(required = false) String mail,
            RedirectAttributes redirectAttributes) {

        if (rewardLogic.deleteable(id)) {
            rewardLogic.deleteReward(id);
            logger.info("Recompensa amb id {} eliminada correctament", id);

            redirectAttributes.addFlashAttribute("successMessage", "Recompensa esborrada correctament");
        } else {
            logger.warn("Intent d'esborrar recompensa amb id {} no permès (assignada a un usuari)", id);

            redirectAttributes.addFlashAttribute("errorMessage", "La recompensa no pot ser esborrada, ja que està assignada a un usuari");
        }

        return (mail != null) ? "redirect:/rewards/list/" + mail : "redirect:/rewards/list";
    }

    @GetMapping("/accept/{id}")
    public String acceptReward(@PathVariable("id") Long id,
            @RequestParam(required = false) String mail) {
        Reward reward = rewardLogic.getRewardById(id);
        if (reward == null) {
            logger.error("No s'ha trobat la recompensa amb id {}", id);
            return (mail != null) ? "redirect:/rewards/list/" + mail : "redirect:/rewards/list";
        }

        User user = reward.getUser();
        reward.setEstat(RewardStatus.ACCEPTED);
        reward.setDataSolicitud(LocalDateTime.now());
        rewardLogic.rewardAccept(id, user);
        rewardRepo.save(reward);

        logger.info("Recompensa amb id {} acceptada per l'usuari {}", id, user != null ? user.getMail() : "desconegut");

        return (mail != null) ? "redirect:/rewards/list/" + mail : "redirect:/rewards/list";
    }
}
