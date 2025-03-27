/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cat.copernic.backend.repository;

import cat.copernic.backend.entity.UserSession;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author carlo
 */
public interface UserSessionRepo extends JpaRepository<UserSession, Long>{
    Optional<UserSession> findByEmailAndSessionKey(String email, String sessionKey);

    UserSession save(UserSession sessio);
}
