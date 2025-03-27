package cat.copernic.backend.controllers;

import cat.copernic.backend.entity.User;
import cat.copernic.backend.logic.UserLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // ✅ Crear usuario
    @PostMapping("/create")
    public String createUser(@ModelAttribute User user) {
        userLogic.saveUser(user);
        return "redirect:/users/list";
    }

    // ✅ Editar usuario
    @PostMapping("/edit/{mail}")
    public String editUser(@PathVariable("mail") String mail, @ModelAttribute User user) {
        user.setMail(mail); // asegúrate de mantener el mail
        userLogic.saveUser(user);
        return "redirect:/users/list";
    }
}
