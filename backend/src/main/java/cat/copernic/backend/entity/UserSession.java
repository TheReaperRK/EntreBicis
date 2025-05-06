/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author carlo
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String sessionKey;
    private LocalDateTime createdAt;

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setSessionKey(String newSessionKey) {
        this.sessionKey = newSessionKey;
    }
    
    public String getSessionKey (){
        return this.sessionKey;
    }

    public void setCreatedAt(LocalDateTime newCreationDate) {
        this.createdAt = newCreationDate;
    }
    
    
}


