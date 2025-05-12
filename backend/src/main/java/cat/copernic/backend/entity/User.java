/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.entity;

import cat.copernic.backend.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entitat que representa un usuari dins del sistema.
 * Els usuaris poden ser ciclistes (rol USER) o administradors (rol ADMIN),
 * i poden estar associats a rutes, recompenses, imatges de perfil i altra informació personal.
 *
 * També es fa servir per processos d’autenticació, registre i gestió de sessions.
 *
 * L’atribut `mail` actua com identificador únic (primary key).
 * 
 * @author carlo
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    /**
     * Correu electrònic de l’usuari. És la clau primària.
     */
    @Id
    @Column(unique = true, nullable = false)
    private String mail;

    /**
     * Nom de l’usuari.
     */
    private String name;

    /**
     * Cognoms de l’usuari.
     */
    private String surnames;

    /**
     * Població de residència de l’usuari.
     */
    private String population;

    /**
     * Telèfon de contacte.
     */
    private String phone_number;

    /**
     * Rol de l’usuari (USER o ADMIN).
     */
    private Role role;

    /**
     * Contrasenya codificada.
     */
    private String word;

    /**
     * Saldo actual de punts de l’usuari.
     */
    private double balance;

    /**
     * Token temporal per restablir contrasenya.
     */
    private String resetToken;

    /**
     * Imatge de perfil guardada com array de bytes.
     */
    @Lob
    private byte[] image;

    /**
     * Observacions opcionals sobre l’usuari.
     */
    @Lob
    private String observations;

    /**
     * Llista de recompenses associades a aquest usuari.
     * Relació OneToMany amb Reward.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Reward> reward = new ArrayList<>();

    /**
     * Llista de rutes realitzades per aquest usuari.
     * Relació OneToMany amb Route.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Route> route = new ArrayList<>();

    // Getters & Setters

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getImageBase64() {
        return image != null ? Base64.getEncoder().encodeToString(image) : null;
    }

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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
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

    public List<Route> getRoute() {
        return route;
    }

    public void setRoute(List<Route> route) {
        this.route = route;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
}

