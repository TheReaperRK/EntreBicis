package cat.copernic.backend.logic;

import cat.copernic.backend.entity.Reward;
import cat.copernic.backend.repository.RewardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardLogic {

    @Autowired
    private RewardRepo rewardRepo;

    public List<Reward> getAllRewards() {
        return rewardRepo.findAll();
    }

    public Reward getRewardById(Long id) {
        return rewardRepo.findById(id).orElse(null);
    }

    public void saveReward(Reward reward) {
        rewardRepo.save(reward);
    }

    public void deleteReward(Long id) {
        rewardRepo.deleteById(id);
    }
}
