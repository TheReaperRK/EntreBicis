/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.DTO;

/**
 * DTO senzill que representa un usuari amb les seves dades mínimes essencials:
 * correu electrònic i nom. Aquest objecte s’utilitza habitualment per retornar
 * informació de l’usuari autenticat (login) sense exposar dades sensibles.
 * 
 * Exemple d'ús:
 * - LoginResponse
 * - Validació de token
 * - Navegació bàsica a la mobile app
 * 
 * @author carlo
 */
public class UserDTO {

    /** Correu electrònic de l’usuari (clau principal) */
    private String mail;

    /** Nom de l’usuari */
    private String name;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

