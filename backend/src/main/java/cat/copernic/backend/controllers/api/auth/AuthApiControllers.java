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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Controlador REST per gestionar l'autenticació i la recuperació de contrasenya a l'API mòbil.
 * Inclou login amb JWT, consulta d'usuaris per email, enviament de correus de recuperació i restabliment de contrasenyes.
 * Aquest controlador respon a peticions a la ruta base: /api/auth
 * 
 * @author carlo
 */
@RestController
@RequestMapping("/api/auth")
public class AuthApiControllers {

    private static final Logger logger = LoggerFactory.getLogger(AuthApiControllers.class);

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

    /**
     * Endpoint de login per a l'app mòbil. Valida credencials i retorna un token JWT si són correctes.
     *
     * @param email correu electrònic de l'usuari
     * @param word contrasenya en pla
     * @return resposta amb token JWT i dades bàsiques si és correcte; error 401 si falla
     */
    @PostMapping(value = "/loginMobile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String word) {
        logger.info("Intent de login per l'usuari: {}", email);
        User user = userLogic.authUser(email, word);

        if (user != null) {
            String token = jwtUtil.generateToken(user.getMail());

            UserDTO dto = new UserDTO();
            dto.setMail(user.getMail());
            dto.setName(user.getName());

            logger.info("Login correcte per l'usuari: {}", email);
            LoginResponse response = new LoginResponse(token, dto);
            return ResponseEntity.ok(response);
        }

        logger.warn("Credencials incorrectes per l'usuari: {}", email);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales incorrectas"));
    }

    /**
     * Endpoint per obtenir les dades completes d’un usuari pel seu correu.
     *
     * @param email correu electrònic de l’usuari
     * @return objecte {@code ProfileDTO} amb totes les dades rellevants del perfil o error 404
     */
    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        logger.info("Petició per obtenir usuari complet amb email: {}", email);
        try {
            User user = userLogic.getUserByMail(email);
            if (user != null) {
                ProfileDTO userDto = new ProfileDTO();
                userDto.setName(user.getName());
                userDto.setSurnames(user.getSurnames());
                userDto.setMail(user.getMail());
                userDto.setObservations(user.getObservations());
                userDto.setPhone_number(user.getPhone_number());
                userDto.setPopulation(user.getPopulation());
                userDto.setReward(user.getReward());
                userDto.setBalance(user.getBalance());

                logger.info("Usuari {} trobat correctament", email);
                return ResponseEntity.ok(userDto);
            } else {
                logger.warn("Usuari amb email {} no trobat", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuari no trobat");
            }
        } catch (Exception e) {
            logger.error("Error en recuperar l'usuari {}: {}", email, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en recuperar l'usuari: " + e.getMessage());
        }
    }

    /**
     * Endpoint per obtenir una versió simplificada (nom + email) d’un usuari.
     *
     * @param email correu electrònic de l’usuari
     * @return objecte {@code UserDTO} o error 404
     */
    @GetMapping("/userDTO/{email}")
    public ResponseEntity<?> getUserByEmailDTO(@PathVariable String email) {
        logger.info("Petició per obtenir DTO simple de l'usuari: {}", email);
        try {
            User userComplert = userLogic.getUserByMail(email);
            if (userComplert != null) {
                UserDTO user = new UserDTO();
                user.setMail(userComplert.getMail());
                user.setName(userComplert.getName());

                logger.info("Usuari DTO retornat per: {}", email);
                return ResponseEntity.ok(user);
            } else {
                logger.warn("Usuari amb email {} no trobat per DTO", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuari no trobat");
            }
        } catch (Exception e) {
            logger.error("Error en recuperar DTO de l'usuari {}: {}", email, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en recuperar l'usuari: " + e.getMessage());
        }
    }

    /**
     * Endpoint per iniciar el procés de recuperació de contrasenya.
     * Si l'usuari existeix, es genera un token i s'envia per correu.
     *
     * @param email correu electrònic de l’usuari
     * @return missatge de confirmació o error 404
     */
    @PostMapping(value = "/recover")
    public ResponseEntity<?> recoverPassword(@RequestParam String email) {
        logger.info("Petició per recuperar contrasenya per a: {}", email);

        User user = userLogic.getUserByMail(email);

        if (user == null) {
            logger.warn("Recuperació de contrasenya: usuari no trobat per {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "El correu no està registrat"));
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepo.save(user);

        emailLogic.sendPasswordResetEmail(user.getMail(), token);
        logger.info("Token de recuperació generat i enviat per a {}: {}", email, token);

        return ResponseEntity.ok(Collections.singletonMap("message", "Correu enviat"));
    }

    /**
     * Endpoint per restablir la contrasenya d’un usuari mitjançant el token rebut per correu.
     *
     * @param email correu electrònic de l’usuari
     * @param token token de recuperació enviat per correu
     * @param word nova contrasenya (en pla)
     * @return missatge d’èxit o error en cas de token invàlid
     */
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestParam String token,
                                           @RequestParam String word) {
        logger.info("Petició per restablir contrasenya de: {}", email);

        User user = userLogic.getUserByMail(email);

        if (user == null) {
            logger.warn("Restabliment: usuari no trobat per {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "El correu no està registrat"));
        } else if (token == null) {
            logger.warn("Token nul en restabliment per {}", email);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "El correu no consta amb un token de recuperació"));
        }

        if (!user.getResetToken().contentEquals(token)) {
            logger.warn("Token invàlid per a l'usuari {}", email);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Token invàlid"));
        }

        user.setWord(passwordEncoder.encode(word));
        user.setResetToken(null);
        userLogic.saveUser(user);

        logger.info("Contrasenya restablerta amb èxit per a {}", email);
        return ResponseEntity.ok(Collections.singletonMap("message", "S'ha restablert la contrasenya"));
    }
}
