/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers.auth.apiControllers;

import cat.copernic.backend.config.JwtUtil;
import cat.copernic.backend.entity.DTO.LoginResponse;
import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.UserSession;
import cat.copernic.backend.logic.UserLogic;
import cat.copernic.backend.logic.UserSessionLogic;
import cat.copernic.backend.repository.UserRepo;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author carlo
 */
@RestController
@RequestMapping("/api/auth")
public class AuthApiControllers {

    @Autowired
    private UserLogic userLogic;

    @Autowired
    private UserSessionLogic userSessionLogic;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
        @Autowired
        private EmailLogic emailLogic;
     */
    @PostMapping(value = "/loginMobile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String word) {
        User user = userLogic.authUser(email, word);

        if (user != null) {
            String token = jwtUtil.generateToken(user.getMail());
            LoginResponse response = new LoginResponse(token, user);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
    }
    
    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            System.out.println(email);
            User user = userLogic.getUserByMail(email);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuari no trobat");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error en recuperar l'usuari: " + e.getMessage());
        }
    }
    
}
