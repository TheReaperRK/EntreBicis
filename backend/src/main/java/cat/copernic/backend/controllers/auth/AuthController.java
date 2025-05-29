/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers.auth;

import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.UserSession;
import cat.copernic.backend.entity.enums.Role;
import cat.copernic.backend.logic.UserLogic;
import cat.copernic.backend.logic.UserSessionLogic;
import cat.copernic.backend.repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador per gestionar l’autenticació i registre d’usuaris per la interfície web.
 * Inclou accés a les vistes de login i registre, validació de credencials d’administradors,
 * creació de sessions i tancament de sessió.
 * 
 * Aquesta classe només s’utilitza per la part web (no per l’app mòbil).
 * 
 * Ruta base: /
 * 
 * @author carlo
 */
@Controller
public class AuthController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserSessionLogic userSessionLogic;

    @Autowired
    private UserLogic userLogic;

    public AuthController(UserRepo userRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    /**
     * Mostra la vista de login web.
     * 
     * @return ruta de la plantilla de login HTML
     */
    @GetMapping("/login")
    public String login() {
        logger.info("Acceso a la vista de login");
        return "/auth/login";
    }

    /**
     * Endpoint per processar el login des de l’entorn web.
     * Només permet iniciar sessió si l’usuari té rol ADMIN.
     *
     * @param email correu electrònic introduït al formulari
     * @param word contrasenya introduïda
     * @return resposta amb claus de sessió si es valida, error 401 si les credencials no són vàlides
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginWeb(@RequestParam String email, @RequestParam String word) {
        logger.info("Intentando iniciar sesión para el usuario {}", email);

        try {
            User user = userLogic.authUser(email, word);

            if (user != null && user.getRole() == Role.ADMIN) {
                logger.info("Autenticación exitosa para {}", email);
                UserSession sessio = userSessionLogic.createSession(email);
                return ResponseEntity.ok(Map.of(
                        "email", email,
                        "sessionKey", sessio.getSessionKey()
                ));
            } else {
                logger.warn("Fallo de autenticación para {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
            }
        } catch (Exception e) {
            logger.error("Error durante la autenticación de {}: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno"));
        }
    }

    /**
     * Mostra el formulari de registre d’usuari.
     * 
     * @param model model de la vista amb un usuari buit per al binding
     * @return ruta de la plantilla de registre HTML
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        logger.info("Acceso a la vista de registro");
        model.addAttribute("user", new User());
        return "/auth/register";
    }

    /**
     * Endpoint per registrar un nou usuari des de la web.
     * La contrasenya es codifica i s’assigna el rol per defecte USER.
     *
     * @param user objecte {@code User} provinent del formulari
     * @return redirecció al login amb indicador de registre correcte
     */
    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        logger.info("Registrando nuevo usuario con email: {}", user.getMail());
        user.setWord(encoder.encode(user.getWord()));
        user.setRole(Role.USER);
        userRepo.save(user);
        return "redirect:/login?registered";
    }

    /**
     * Endpoint per tancar la sessió web de l’usuari.
     * 
     * @param session objecte {@code HttpSession} a invalidar
     * @return redirecció a login amb paràmetre de logout
     */
    @GetMapping("/auth/logout")
    public String logout(HttpSession session) {
        logger.info("Cierre de sesión");
        session.invalidate();
        return "redirect:/login?logout";
    }
}
