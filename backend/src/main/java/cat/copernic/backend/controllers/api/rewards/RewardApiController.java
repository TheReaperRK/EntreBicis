/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers.api.rewards;

import cat.copernic.backend.entity.Reward;
import cat.copernic.backend.entity.User;
import cat.copernic.backend.logic.RewardLogic;
import cat.copernic.backend.logic.UserLogic;
import java.security.Principal;
import java.util.List;
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
@RestController // 👈 importante para API REST
@RequestMapping("/api/rewards")
public class RewardApiController {

    @Autowired
    private RewardLogic rewardLogic;

    @Autowired
    private UserLogic userLogic; // Necesario si quieres seleccionar usuario en el formulario

    // ✅ Lista de recompensas
    @GetMapping("/list")
    public List<Reward> listRewards() {
        return rewardLogic.getAllRewards();
    }

    // ✅ Lista de recompensas
    @GetMapping("/{id}")
    public Reward RewardDetails(@PathVariable Long id) {
        return rewardLogic.getRewardById(id);
    }

    @PostMapping("/{id}/request")
    @ResponseBody
    public ResponseEntity<?> solicitarRecompensa(@PathVariable Long id, Principal principal) {
        try {
            User user = userLogic.getUserByMail(principal.getName());
            Reward reward = rewardLogic.getRewardById(id);

            if (user.getBalance() < reward.getPreu()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Saldo insuficient per bescanviar aquesta recompensa.");
            }

            rewardLogic.rewardRequest(id, user); // Aquí ya puedes cambiar el estado internamente
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al sol·licitar la recompensa: " + e.getMessage());
        }
    }
}
