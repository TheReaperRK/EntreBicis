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
 * Entitat que representa una sessió iniciada per un usuari del sistema web.
 * Cada sessió es guarda amb un identificador únic, associada a un correu electrònic (usuari)
 * i una clau de sessió (`sessionKey`), amb la seva data de creació.
 *
 * Aquesta entitat s’utilitza per controlar l’accés a les funcionalitats d’administració via web.
 * 
 * @author carlo
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class UserSession {

    /**
     * Identificador únic de la sessió (autogenerat).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Correu electrònic de l’usuari que ha iniciat sessió.
     */
    private String email;

    /**
     * Clau única de sessió generada a l’inici de sessió.
     * Aquesta clau s’utilitza per validar la sessió activa del client web.
     */
    private String sessionKey;

    /**
     * Data i hora de creació de la sessió.
     * Serveix per establir una caducitat si calgués.
     */
    private LocalDateTime createdAt;

    // Setters personalitzats

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setSessionKey(String newSessionKey) {
        this.sessionKey = newSessionKey;
    }

    public String getSessionKey() {
        return this.sessionKey;
    }

    public void setCreatedAt(LocalDateTime newCreationDate) {
        this.createdAt = newCreationDate;
    }
}



