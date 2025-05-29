/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity.DTO;

import cat.copernic.backend.entity.Reward;
import cat.copernic.backend.entity.enums.Role;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO utilitzat per enviar o rebre la informació completa del perfil d’un usuari
 * dins de l’aplicació Android.
 *
 * Inclou tots els camps rellevants del model `User`, com també la imatge (en base64)
 * i la llista de recompenses vinculades.
 * 
 * Aquest DTO s’utilitza quan l’usuari obre o edita el seu perfil dins de l’app.
 * 
 * ⚠️ Nota: El camp `word` pot ometre’s a la resposta si no s’utilitza per seguretat.
 * 
 * @author carlo
 */
public class ProfileDTO {

    /** Correu electrònic de l’usuari (clau principal) */
    private String mail;

    /** Nom de l’usuari */
    private String name;

    /** Cognoms de l’usuari */
    private String surnames;

    /** Població de residència */
    private String population;

    /** Número de telèfon mòbil */
    private String phone_number;

    /** Rol dins del sistema (USER o ADMIN) */
    private Role role;

    /** Contrasenya de l’usuari (s’aconsella no exposar-la fora del backend) */
    private String word;

    /** Saldo en punts acumulats */
    private double balance;

    /** Imatge de perfil codificada en base64 */
    private String image;

    /** Observacions addicionals guardades per l’usuari */
    private String observations;

    /** Llista de recompenses assignades o sol·licitades per l’usuari */
    private List<Reward> reward = new ArrayList<>();

    // Getters i setters

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

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public List<Reward> getReward() {
        return reward;
    }

    public void setReward(List<Reward> reward) {
        this.reward = reward;
    }
}

