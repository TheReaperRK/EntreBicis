/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers.api.user;

import cat.copernic.backend.entity.User;
import cat.copernic.backend.logic.UserLogic;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author carlo
 */

@RestController
@RequestMapping("/api/auth")
public class UserApiController {
    
    @Autowired
    private UserLogic userLogic;

    @PostMapping("/user/update")
    public ResponseEntity<?> updateUser(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String surnames,
            @RequestParam String population,
            @RequestParam String phone_number,
            @RequestParam(required = false) String observations,
            @RequestParam(required = false) MultipartFile image
    ) {
        try {
            User user = userLogic.getUserByMail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuari no trobat");
            }

            user.setName(name);
            user.setSurnames(surnames);
            user.setPopulation(population);
            user.setPhone_number(phone_number);
            user.setObservations(observations);

            if (image != null && !image.isEmpty()) {
                user.setImage(image.getBytes());
            }

            userLogic.saveUser(user);
            return ResponseEntity.ok("Usuari actualitzat correctament");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualitzar l'usuari: " + e.getMessage());
        }
    }
}
