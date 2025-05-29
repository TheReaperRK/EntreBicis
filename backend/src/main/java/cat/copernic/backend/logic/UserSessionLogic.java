/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.logic;

import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.UserSession;
import cat.copernic.backend.repository.UserSessionRepo;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlo
 */

@Service
public class UserSessionLogic {
    @Autowired
    private UserSessionRepo repo;

    public UserSession createSession(String email) {
        UserSession sessio = new UserSession();
        sessio.setEmail(email);
        sessio.setSessionKey(UUID.randomUUID().toString());
        sessio.setCreatedAt(LocalDateTime.now());
        System.out.println("sessio creada");
        return repo.save(sessio);
    }

    public boolean esSessioValida(String email, String sessionKey) {
        return repo.findByEmailAndSessionKey(email, sessionKey).isPresent();
    }
}
