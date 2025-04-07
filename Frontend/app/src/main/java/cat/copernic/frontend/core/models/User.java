/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.frontend.core.models;

import cat.copernic.frontend.core.models.Reward;
import cat.copernic.frontend.core.models.Route;
import cat.copernic.frontend.core.models.enums.Role;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carlo
 */

public class User {

    private String mail;

    private String name;

    private String surnames;

    private String population;

    private String phone_number;

    private Role role;
    
    private String word;

    private int balance;

    private byte[] image;

    private byte[] observations;

    private List<Reward> reward = new ArrayList<>();

    private List<Route> route = new ArrayList<>();

    public Role getRole() {
        return role;
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

    public void setRole(Role role) {
        this.role = role;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getObservations() {
        return observations;
    }

    public void setObservations(byte[] observations) {
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
}
