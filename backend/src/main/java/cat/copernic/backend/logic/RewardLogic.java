package cat.copernic.backend.logic;

import cat.copernic.backend.entity.Reward;
import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.enums.RewardStatus;
import cat.copernic.backend.repository.RewardRepo;
import cat.copernic.backend.repository.UserRepo;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardLogic {

    @Autowired
    private RewardRepo rewardRepo;

    @Autowired
    private UserRepo userRepo;

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

    public List<Reward> getAvailableRewards() {
        return rewardRepo.findByEstat(RewardStatus.AVAILABLE);
    }

    public void rewardRequest(Long id, User user) {
        Reward reward = rewardRepo.getById(id);

        user.setBalance(user.getBalance() - reward.getPreu());
        reward.setEstat(RewardStatus.PENDING);
        reward.setUser(user);

        rewardRepo.save(reward);
        userRepo.save(user);
    }
}
