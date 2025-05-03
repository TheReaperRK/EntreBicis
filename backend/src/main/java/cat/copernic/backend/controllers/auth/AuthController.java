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
 *
 * @author carlo
 */
@Controller
public class AuthController {
    
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

    @GetMapping("/login")
    public String login() {
        userLogic.createSampleUser();
        return "/auth/login";
    }
    
    @PostMapping("/login")
    @ResponseBody 
    public ResponseEntity<?> loginWeb(@RequestParam String email, @RequestParam String word) {

        
        User user = userLogic.authUser(email, word);

        System.out.println(user);
        if (user != null) {
            System.out.println("in");
            UserSession sessio = userSessionLogic.createSession(email);
            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "sessionKey", sessio.getSessionKey()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
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
    
    
    @GetMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 🔐 Invalida la sessió
        return "redirect:/login?logout"; // 🔁 Redirigeix al login
    }
}
