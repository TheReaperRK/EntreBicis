/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers.auth;

import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.enums.Role;
import cat.copernic.backend.repository.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author carlo
 */
@Controller
public class AuthController {
    
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;

    public AuthController(UserRepo userRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "/auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        user.setWord(encoder.encode(user.getWord()));
        user.setRole(Role.USER); // Por defecto
        userRepo.save(user);
        return "redirect:/login?registered";
    }
}
