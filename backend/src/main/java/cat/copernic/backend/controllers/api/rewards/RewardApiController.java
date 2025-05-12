/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers.api.rewards;

import cat.copernic.backend.entity.Reward;
import cat.copernic.backend.entity.User;
import cat.copernic.backend.logic.RewardLogic;
import cat.copernic.backend.logic.UserLogic;
import cat.copernic.backend.repository.UserRepo;
import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author carlo
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardApiController {

    private static final Logger logger = LoggerFactory.getLogger(RewardApiController.class);

    @Autowired
    private RewardLogic rewardLogic;

    @Autowired
    private UserLogic userLogic;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/list")
    public List<Reward> listRewards() {
        logger.info("Obtenint llista de recompenses disponibles");
        return rewardLogic.getAvailableRewards();
    }

    @GetMapping("/list/{mail}")
    public List<Reward> listUserRewards(@PathVariable String mail) {
        logger.info("Obtenint recompenses per a l'usuari amb mail: {}", mail);
        User user = userRepo.findByMail(mail);
        List<Reward> rewards = rewardLogic.getAllUserRewards(user);
        logger.debug("Recompenses trobades per a {}: {}", mail, rewards.size());
        return rewards;
    }

    @GetMapping("/{id}")
    public Reward RewardDetails(@PathVariable Long id) {
        logger.info("Consultant detalls de la recompensa amb ID: {}", id);
        return rewardLogic.getRewardById(id);
    }

    @PostMapping("/{id}/request")
    @ResponseBody
    public ResponseEntity<?> solicitarRecompensa(@PathVariable Long id, Principal principal) {
        try {
            String userMail = principal.getName();
            logger.info("Usuari {} intenta sol·licitar la recompensa amb ID {}", userMail, id);

            User user = userLogic.getUserByMail(userMail);
            Reward reward = rewardLogic.getRewardById(id);

            if (user.getBalance() < reward.getPreu()) {
                logger.warn("Usuari {} no té prou saldo. Saldo: {}, Preu: {}", userMail, user.getBalance(), reward.getPreu());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Saldo insuficient per bescanviar aquesta recompensa.");
            }

            rewardLogic.rewardRequest(id, user);
            logger.info("Recompensa amb ID {} sol·licitada correctament per {}", id, userMail);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            logger.error("Error al sol·licitar la recompensa amb ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al sol·licitar la recompensa: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/take")
    @ResponseBody
    public ResponseEntity<?> recollirRecompensa(@PathVariable Long id, Principal principal) {
        try {
            String userMail = principal.getName();
            logger.info("Usuari {} intenta recollir la recompensa amb ID {}", userMail, id);

            User user = userLogic.getUserByMail(userMail);
            Reward reward = rewardLogic.getRewardById(id);

            rewardLogic.rewardTake(id, user);
            logger.info("Recompensa amb ID {} recollida correctament per {}", id, userMail);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            logger.error("Error al recollir la recompensa amb ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al recollir la recompensa: " + e.getMessage());
        }
    }
}
