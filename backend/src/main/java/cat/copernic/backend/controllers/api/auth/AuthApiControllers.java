/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers.api.auth;

import cat.copernic.backend.config.JwtUtil;
import cat.copernic.backend.entity.DTO.LoginResponse;
import cat.copernic.backend.entity.DTO.ProfileDTO;
import cat.copernic.backend.entity.DTO.UserDTO;
import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.UserSession;
import cat.copernic.backend.logic.EmailLogic;
import cat.copernic.backend.logic.UserLogic;
import cat.copernic.backend.logic.UserSessionLogic;
import cat.copernic.backend.repository.UserRepo;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
import org.springframework.web.bind.annotation.RequestPart;
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
    private EmailLogic emailLogic;

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

            UserDTO dto = new UserDTO();
            dto.setMail(user.getMail());
            dto.setName(user.getName());


            LoginResponse response = new LoginResponse(token, dto);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            System.out.println(email);
            User user = userLogic.getUserByMail(email);
            
            ProfileDTO userDto = new ProfileDTO();
            
            userDto.setName(user.getName());
            userDto.setSurnames(user.getSurnames());
            userDto.setMail(user.getMail());
            userDto.setObservations(user.getObservations());
            userDto.setPhone_number(user.getPhone_number());
            userDto.setPopulation(user.getPopulation());
            userDto.setReward(user.getReward());
            userDto.setBalance(user.getBalance());
            
            
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
    
    @GetMapping("/userDTO/{email}")
    public ResponseEntity<?> getUserByEmailDTO(@PathVariable String email) {
        try {
            System.out.println(email);
            UserDTO user = new UserDTO();
            
            User userComplert = userLogic.getUserByMail(email);
            
            if (user != null) {
                
                user.setMail(userComplert.getMail());
                user.setName(userComplert.getName());
                
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuari no trobat");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en recuperar l'usuari: " + e.getMessage());
        }
    }

    @PostMapping(value = "/recover")
    public ResponseEntity<?> recoverPassword(@RequestParam String email) {
        System.out.println("RECIBIDO EMAIL: " + email);

        User user = userLogic.getUserByMail(email);

        if (user == null) {
            System.out.println("user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "El correo no está registrado"));
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepo.save(user);

        emailLogic.sendPasswordResetEmail(user.getMail(), token);
        System.out.println("Correo enviado con token: " + token);

        return ResponseEntity.ok(Collections.singletonMap("message", "Correo enviado"));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String token, @RequestParam String word) {
        User user = userLogic.getUserByMail(email);
        System.out.println(user);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "El correo no está registrado"));
        } else if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "El correo no consta con un token de recuperacion"));
        }

        if (!(user.getResetToken().contentEquals(token))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Token invalido"));
        }
        user.setWord(passwordEncoder.encode(word));
        user.setResetToken(null); // per tal que no es pugui reutilitzar

        userLogic.saveUser(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "se ha restablecido la contraseña"));
    }

}
