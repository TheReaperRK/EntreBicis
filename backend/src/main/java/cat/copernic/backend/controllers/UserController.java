package cat.copernic.backend.controllers;

import cat.copernic.backend.entity.User;
import cat.copernic.backend.logic.UserLogic;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author carlo
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserLogic userLogic;

    @GetMapping("/list")
    public String listClients(Model model) {
        logger.info("Accés a la llista d'usuaris");
        model.addAttribute("users", userLogic.getAllUsers());
        return "user/users-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        logger.info("Accés al formulari de creació d'usuari");
        model.addAttribute("user", new User());
        model.addAttribute("formMode", "create");
        return "user/user-form";
    }

    @GetMapping("/edit/{mail}")
    public String showEditForm(@PathVariable("mail") String mail, Model model) {
        logger.info("Accés al formulari d'edició per a l'usuari: {}", mail);
        User user = userLogic.getUserByMail(mail);
        if (user == null) {
            logger.warn("Intent d'accedir a un usuari inexistent: {}", mail);
            return "redirect:/users/list";
        }
        model.addAttribute("user", user);
        model.addAttribute("formMode", "edit");
        return "user/user-form";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam(value = "observations", required = false) String observations,
            Model model) {
        logger.info("Intent de creació d'usuari amb correu: {}", user.getMail());

        if (user.getWord() == null || user.getWord().isBlank()) {
            logger.warn("Creació fallida: contrasenya buida");
            model.addAttribute("error", "La contrasenya és obligatòria.");
            model.addAttribute("formMode", "create");
            return "user/user-form";
        }

        if (user.getMail() == null || user.getMail().isBlank()) {
            logger.warn("Creació fallida: correu electrònic buit");
            model.addAttribute("error", "El correu electrònic és obligatori.");
            model.addAttribute("formMode", "create");
            return "user/user-form";
        }

        try {
            if (!imageFile.isEmpty()) {
                user.setImage(imageFile.getBytes());
            }

            if (observations != null) {
                user.setObservations(observations);
            }

            if (userLogic.findUserByMail(user.getMail()).isPresent()) {
                logger.warn("Creació fallida: usuari ja existeix amb correu {}", user.getMail());
                model.addAttribute("error", "Ja existeix un Usuari amb aquest correu");
            } else {
                userLogic.saveWithEncoder(user);
                logger.info("Usuari creat correctament: {}", user.getMail());
                return "redirect:/users/list";
            }
        } catch (IOException e) {
            logger.error("Error al pujar la imatge per a l'usuari {}: {}", user.getMail(), e.getMessage());
            model.addAttribute("error", "Error en pujar la imatge.");
        }

        model.addAttribute("user", user);
        model.addAttribute("formMode", "create");
        return "user/user-form";
    }

    @PostMapping("/edit/{mail}")
    public String editUser(@PathVariable("mail") String mail,
            @ModelAttribute User user,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam(value = "observations", required = false) String observations,
            Model model) {
        logger.info("Edició d'usuari: {}", mail);
        User existingUser = userLogic.getUserByMail(mail);
        if (existingUser == null) {
            logger.warn("Intent d'editar un usuari inexistent: {}", mail);
            return "redirect:/users/list";
        }

        user.setWord(existingUser.getWord());
        user.setMail(existingUser.getMail());

        try {
            if (!imageFile.isEmpty()) {
                user.setImage(imageFile.getBytes());
            } else {
                user.setImage(existingUser.getImage());
            }

            user.setObservations(observations != null ? observations : existingUser.getObservations());

            userLogic.saveUser(user);
            logger.info("Usuari editat correctament: {}", mail);
        } catch (IOException e) {
            logger.error("Error al pujar la imatge de l'usuari {}: {}", mail, e.getMessage());
            model.addAttribute("error", "Error en pujar la imatge.");
            model.addAttribute("user", user);
            model.addAttribute("formMode", "edit");
            return "user/user-form";
        }

        return "redirect:/users/list";
    }

    @GetMapping("/api/users/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        logger.info("Consulta d'usuari per API: {}", email);
        User usuari = userLogic.getUserByMail(email);
        return ResponseEntity.ok(usuari);
    }
}
