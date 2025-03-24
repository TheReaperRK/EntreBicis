/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.backend.logic;

import cat.copernic.backend.entity.SystemParameters;
import cat.copernic.backend.repository.SystemParametersRepo;
import org.springframework.stereotype.Service;

/**
 *
 * @author carlo
 */
@Service
public class SystemParametersLogic {
    private final SystemParametersRepo repository;

    public SystemParametersLogic(SystemParametersRepo repository) {
        this.repository = repository;
    }

    public SystemParameters getParameters() {
        return repository.findById("default").orElseGet(() -> {
            SystemParameters defaults = new SystemParameters();
            repository.save(defaults);
            return defaults;
        });
    }

    public void updateParameters(SystemParameters updated) {
        updated.setNameConfig("default"); // Forzamos la clave
        repository.save(updated);
    }
}
