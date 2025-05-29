/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.logic;

import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.enums.Role;
import cat.copernic.backend.repository.UserRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author carlo
 */
@Service
public class UserLogic {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // Método para obtener un usuario por ID
    public User getUserByMail(String mail) {
        return userRepo.getById(mail);
    }
    
    public Optional<User> findUserByMail(String mail){
        return userRepo.findById(mail);
    }

    public void saveWithEncoder(User user){
        Optional<User> existingOpt = userRepo.findById(user.getMail());

        if (existingOpt.isPresent()) {
            User existing = existingOpt.get();
            user.setReward(existing.getReward());
            user.setRoute(existing.getRoute());
        }
        user.setWord(passwordEncoder.encode(user.getWord()));
        userRepo.save(user);
    }
    public void saveUser(User user) {
        Optional<User> existingOpt = userRepo.findById(user.getMail());

        if (existingOpt.isPresent()) {
            User existing = existingOpt.get();
            user.setReward(existing.getReward());
            user.setRoute(existing.getRoute());
        }

        userRepo.save(user);
    }

    public User authUser(String email, String rawWord) {

        User user = userRepo.findByMail(email);

        if (passwordEncoder.matches(rawWord, user.getWord())) {
            return user;
        }
        return null;
    }

    public void createSampleUser() {
        User user = new User();

// Email (clave primaria)
        user.setMail("ciclista@example.com");

// Información básica
        user.setName("Carlos");
        user.setSurnames("Pérez García");
        user.setPopulation("Barcelona");
        user.setPhone_number("612345678");

// Rol (enum)
        user.setRole(Role.ADMIN); // o Role.ADMIN según tu enum

// Contraseña encriptada (si tienes acceso a BCryptPasswordEncoder)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setWord(encoder.encode("123"));

// Puntos de recompensa
        user.setBalance(100);

// Imagen (ejemplo en blanco)
        user.setImage(new byte[0]); // o puedes cargar desde archivo

// Observaciones (ejemplo vacío)
        user.setObservations("");

// Inicializar listas por si quieres añadir rutas o recompensas
        user.setReward(new ArrayList<>());
        user.setRoute(new ArrayList<>());

        userRepo.save(user);
    }

}
