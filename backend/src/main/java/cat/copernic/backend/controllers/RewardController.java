package cat.copernic.backend.controllers;

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

/**
 * Controlador per gestionar les recompenses (part web - admin).
 * Inclou funcionalitats de visualització, creació, edició, acceptació i esborrat de recompenses.
 * Aquestes operacions són principalment usades per l'administrador des de la interfície web.
 * 
 * @author carlo
 */
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
    private UserLogic userLogic; // Ens serveix per carregar usuaris al formulari

    /**
     * Mostra la llista completa de recompenses (per a l'administrador).
     *
     * @param model model utilitzat per passar dades a la vista
     * @return nom de la plantilla Thymeleaf
     */
    @GetMapping("/list")
    public String listRewards(Model model) {
        logger.info("Visualització de la llista de recompenses (admin)");
        model.addAttribute("rewards", rewardLogic.getAllRewards());
        model.addAttribute("showCreateButton", true);
        model.addAttribute("userMail", null);
        return "rewards/rewards-list";
    }

    /**
     * Mostra la llista de recompenses d’un usuari concret.
     *
     * @param mail correu electrònic de l'usuari
     * @param model model utilitzat per passar dades a la vista
     * @return nom de la plantilla Thymeleaf
     */
    @GetMapping("/list/{mail}")
    public String listUserRewards(@PathVariable String mail, Model model) {
        logger.info("Visualització de recompenses per a l'usuari {}", mail);
        User user = userRepo.findByMail(mail);
        model.addAttribute("rewards", rewardLogic.getAllUserRewards(user));
        model.addAttribute("showCreateButton", false);
        model.addAttribute("userMail", mail);
        return "rewards/rewards-list";
    }

    /**
     * Mostra el formulari per crear una nova recompensa.
     *
     * @param model model per passar dades al formulari (recompensa buida i llista d'usuaris)
     * @return nom de la plantilla Thymeleaf
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        logger.info("Accés al formulari de creació de recompensa");
        model.addAttribute("reward", new Reward());
        model.addAttribute("formMode", "create");
        model.addAttribute("users", userLogic.getAllUsers());
        return "rewards/reward-form";
    }

    /**
     * Mostra el formulari per editar una recompensa concreta.
     *
     * @param id identificador de la recompensa a editar
     * @param model model per carregar la recompensa i llista d’usuaris
     * @return vista del formulari o redirecció si no existeix
     */
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

    /**
     * Acció per crear una recompensa.
     * L’estat inicial serà {@code AVAILABLE}.
     *
     * @param reward objecte recompensa que s’ha omplert al formulari
     * @return redirecció a la llista de recompenses
     */
    @PostMapping("/create")
    public String createReward(@ModelAttribute Reward reward) {
        reward.setEstat(RewardStatus.AVAILABLE);
        rewardLogic.saveReward(reward);
        logger.info("Recompensa creada amb nom '{}', assignada a '{}'",
                reward.getNom(), reward.getUser() != null ? reward.getUser().getMail() : "cap");
        return "redirect:/rewards/list";
    }

    /**
     * Acció per guardar els canvis en una recompensa ja existent.
     *
     * @param id identificador de la recompensa a editar
     * @param reward objecte amb les dades modificades
     * @return redirecció a la llista de recompenses
     */
    @PostMapping("/edit/{id}")
    public String editReward(@PathVariable("id") Long id, @ModelAttribute Reward reward) {
        reward.setId(id);
        rewardLogic.saveReward(reward);
        logger.info("Recompensa editada amb id {}", id);
        return "redirect:/rewards/list";
    }

    /**
     * Acció per eliminar una recompensa si no està assignada a cap usuari.
     *
     * @param id identificador de la recompensa a eliminar
     * @param mail (opcional) correu de l’usuari si es vol tornar a la seva llista de recompenses
     * @param redirectAttributes atributs per mostrar missatges flash a la vista
     * @return redirecció a la llista corresponent
     */
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

    /**
     * Acció per acceptar una recompensa assignada a un usuari.
     * Es canvia l’estat a {@code ACCEPTED} i es registra la data de sol·licitud.
     *
     * @param id identificador de la recompensa a acceptar
     * @param mail (opcional) correu de l’usuari per redirigir la vista
     * @return redirecció a la llista corresponent
     */
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

