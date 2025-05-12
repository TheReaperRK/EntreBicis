/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.DTO;


/**
 * DTO de resposta utilitzat després d’un login correcte a l’API.
 * 
 * Aquesta classe encapsula el token JWT que permet fer peticions autenticades
 * i un objecte `UserDTO` amb dades bàsiques de l’usuari que ha iniciat sessió.
 * 
 * S’utilitza principalment al login des de l'app Android.
 * 
 * Exemple d’ús:
 * ```json
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "user": {
 *     "mail": "usuari@exemple.cat",
 *     "name": "Anna"
 *   }
 * }
 * ```
 * 
 * @author carlo
 */
public class LoginResponse {

    /**
     * Token JWT generat pel servidor després d’un login correcte.
     */
    private String token;

    /**
     * Informació bàsica de l’usuari autenticat.
     */
    private UserDTO user;

    /**
     * Constructor que rep el token i les dades de l’usuari.
     * 
     * @param token Token JWT
     * @param user Informació bàsica de l’usuari
     */
    public LoginResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}

