package cat.copernic.backend.controllers;

import cat.copernic.backend.entity.User;
import cat.copernic.backend.logic.UserLogic;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserLogic userLogic;

    // ✅ Lista de usuarios
    @GetMapping("/list")
    public String listClients(Model model) {
        model.addAttribute("users", userLogic.getAllUsers());
        return "user/users-list";
    }

    // ✅ Formulario de creación
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User()); // objeto vacío
        model.addAttribute("formMode", "create"); // ⚠️ Esto faltaba
        return "user/user-form";
    }

    // ✅ Formulario de edición
    @GetMapping("/edit/{mail}")
    public String showEditForm(@PathVariable("mail") String mail, Model model) {
        User user = userLogic.getUserByMail(mail);
        if (user == null) {
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
        if (user.getWord() == null || user.getWord().isBlank()) {
            model.addAttribute("user", user);
            model.addAttribute("formMode", "create");
            model.addAttribute("error", "La contrasenya és obligatòria.");
            return "user/user-form";
        }

        try {
            if (!imageFile.isEmpty()) {
                user.setImage(imageFile.getBytes());
            }

            if (observations != null) {
                user.setObservations(observations);
            }

            userLogic.saveWithEncoder(user);
        } catch (IOException e) {
            model.addAttribute("error", "Error en pujar la imatge.");
            return "user/user-form";
        }

        return "redirect:/users/list";
    }

    //edit user
    @PostMapping("/edit/{mail}")
    public String editUser(@PathVariable("mail") String mail,
            @ModelAttribute User user,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam(value = "observations", required = false) String observations,
            Model model) {
        User existingUser = userLogic.getUserByMail(mail);
        if (existingUser == null) {
            return "redirect:/users/list";
        }

        // Mantenim valors antics
        user.setWord(existingUser.getWord());
        user.setMail(existingUser.getMail());

        try {
            // Actualitza imatge si se selecciona nova
            if (!imageFile.isEmpty()) {
                user.setImage(imageFile.getBytes());
            } else {
                user.setImage(existingUser.getImage());
            }

            // Observacions
            if (observations != null) {
                user.setObservations(observations);
            } else {
                user.setObservations(existingUser.getObservations());
            }

            userLogic.saveUser(user);
        } catch (IOException e) {
            model.addAttribute("error", "Error en pujar la imatge.");
            model.addAttribute("user", user);
            model.addAttribute("formMode", "edit");
            return "user/user-form";
        }

        return "redirect:/users/list";
    }   

}
