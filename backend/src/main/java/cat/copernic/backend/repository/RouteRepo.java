/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cat.copernic.backend.repository;

/**
 *
 * @author carlo
 */
import cat.copernic.backend.entity.Route;
import cat.copernic.backend.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepo extends JpaRepository<Route, Long> {

    Route findFirstByUserMailOrderByStartDateDesc(String mail);

    List<Route> findByUser(User user);

}
