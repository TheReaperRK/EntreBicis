/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.logic.auth;

/**
 *
 * @author carlo
 */
import cat.copernic.backend.repository.UserRepo;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        cat.copernic.backend.entity.User user = userRepo.findById(mail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.builder()
                .username(user.getMail())
                .password(user.getWord())
                .roles(user.getRole().name()) // ADMIN o USER
                .build();
    }

}
