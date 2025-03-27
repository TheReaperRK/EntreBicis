/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.controllers.auth.apiControllers;

import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.UserSession;
import cat.copernic.backend.logic.UserLogic;
import cat.copernic.backend.logic.UserSessionLogic;
import cat.copernic.backend.repository.UserRepo;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    
    /*
        @Autowired
        private EmailLogic emailLogic;
     */
    @PostMapping("/loginMobile")
    @ResponseBody 
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String word) {

        //userLogic.createSampleUser();
        
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
}
