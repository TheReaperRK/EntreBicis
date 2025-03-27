/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.frontend.core.models;

import cat.copernic.backend.entity.enums.Role;
import cat.copernic.frontend.core.models.Reward;
import cat.copernic.frontend.core.models.Route;
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
}
