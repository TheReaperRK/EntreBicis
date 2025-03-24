/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.logic;

import cat.copernic.backend.entity.User;
import cat.copernic.backend.repository.UserRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlo
 */
@Service
public class UserLogic {

    @Autowired
    private UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // Método para obtener un usuario por ID
    public User getUserByMail(String mail) {
        return userRepo.getById(mail);
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

}
