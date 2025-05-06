/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cat.copernic.backend.repository;

/**
 *
 * @author carlo
 */

import cat.copernic.backend.entity.GpsPoint;
import cat.copernic.backend.entity.Route;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GpsPointRepo extends JpaRepository<GpsPoint, Long> {
    
        List<GpsPoint> findByRoute(Route route);
}
