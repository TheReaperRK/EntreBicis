package cat.copernic.frontend.core.models;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import cat.copernic.backend.entity.enums.RewardStatus;
;

import java.time.LocalDateTime;

/**
 *
 * @author carlo
 */



public class Reward {


    private Long id;

    private User user;

    private double preu;

    private String nom;

    private String comerc;
    private String direccio;

    private String descripcio;

    private LocalDateTime dataSolicitud;


    private LocalDateTime dataRecollida;

    private RewardStatus estat;

    public Reward(Long id, User user, double preu, String nom, String direccio, String descripcio, LocalDateTime dataSolicitud, LocalDateTime dataRecollida, RewardStatus estat) {
        this.id = id;
        this.user = user;
        this.preu = preu;
        this.nom = nom;
        this.direccio = direccio;
        this.descripcio = descripcio;
        this.dataSolicitud = dataSolicitud;
        this.dataRecollida = dataRecollida;
        this.estat = estat;
    }

    public Reward() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getPreu() {
        return preu;
    }

    public void setPreu(double preu) {
        this.preu = preu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getComerc() {
        return comerc;
    }

    public void setComerc(String comerc) {
        this.comerc = comerc;
    }

    public String getDireccio() {
        return direccio;
    }

    public void setDireccio(String direccio) {
        this.direccio = direccio;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public LocalDateTime getDataSolicitud() {
        return dataSolicitud;
    }

    public void setDataSolicitud(LocalDateTime dataSolicitud) {
        this.dataSolicitud = dataSolicitud;
    }

    public LocalDateTime getDataRecollida() {
        return dataRecollida;
    }

    public void setDataRecollida(LocalDateTime dataRecollida) {
        this.dataRecollida = dataRecollida;
    }

    public RewardStatus getEstat() {
        return estat;
    }

    public void setEstat(RewardStatus estat) {
        this.estat = estat;
    }
}