/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cat.copernic.backend.repository;

import cat.copernic.backend.entity.Reward;
import cat.copernic.backend.entity.enums.RewardStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author carlo
 */
public interface RewardRepo extends JpaRepository<Reward, Long> {
    
    List<Reward> findByEstat(RewardStatus estat);
}
