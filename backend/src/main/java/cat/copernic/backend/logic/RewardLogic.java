package cat.copernic.backend.logic;

import cat.copernic.backend.entity.Reward;
import cat.copernic.backend.entity.User;
import cat.copernic.backend.entity.enums.RewardStatus;
import cat.copernic.backend.repository.RewardRepo;
import cat.copernic.backend.repository.UserRepo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe de lògica de negoci per gestionar les recompenses de l'aplicació.
 * Conté la lògica per llistar, crear, eliminar i canviar l'estat de les recompenses.
 * Interactua amb els repositoris de Reward i User.
 * 
 * Aquesta classe és utilitzada tant per controladors web com APIs.
 * 
 * @author carlo
 */
@Service
public class RewardLogic {

    @Autowired
    private RewardRepo rewardRepo;

    @Autowired
    private UserRepo userRepo;

    /**
     * Retorna totes les recompenses disponibles a la base de dades.
     * 
     * @return llista de totes les recompenses
     */
    public List<Reward> getAllRewards() {
        return rewardRepo.findAll();
    }

    /**
     * Retorna totes les recompenses associades a un usuari concret.
     *
     * @param user usuari del qual volem consultar les recompenses
     * @return llista de recompenses d'aquest usuari
     */
    public List<Reward> getAllUserRewards(User user) {
        return rewardRepo.findByUser(user);
    }

    /**
     * Busca una recompensa pel seu identificador.
     * 
     * @param id identificador de la recompensa
     * @return objecte Reward o null si no existeix
     */
    public Reward getRewardById(Long id) {
        return rewardRepo.findById(id).orElse(null);
    }

    /**
     * Desa o actualitza una recompensa a la base de dades.
     * 
     * @param reward objecte Reward a desar
     */
    public void saveReward(Reward reward) {
        rewardRepo.save(reward);
    }

    /**
     * Comprova si una recompensa pot ser esborrada (només si està disponible).
     * 
     * @param id identificador de la recompensa
     * @return true si és esborrable, false en cas contrari
     */
    public boolean deleteable(Long id) {
        Reward reward = rewardRepo.getById(id);
        return reward.getEstat() == RewardStatus.AVAILABLE;
    }

    /**
     * Esborra una recompensa pel seu identificador.
     * 
     * @param id identificador de la recompensa
     */
    public void deleteReward(Long id) {
        rewardRepo.deleteById(id);
    }

    /**
     * Retorna totes les recompenses amb estat AVAILABLE.
     * 
     * @return llista de recompenses disponibles
     */
    public List<Reward> getAvailableRewards() {
        return rewardRepo.findByEstat(RewardStatus.AVAILABLE);
    }

    /**
     * Processa la petició d'una recompensa per part d'un usuari.
     * Assigna l'usuari i canvia l'estat a PENDING.
     * 
     * @param id identificador de la recompensa
     * @param user usuari que sol·licita la recompensa
     */
    public void rewardRequest(Long id, User user) {
        Reward reward = rewardRepo.getById(id);
        reward.setEstat(RewardStatus.PENDING);
        reward.setUser(user);
        rewardRepo.save(reward);
    }

    /**
     * Accepta una recompensa pendent, descomptant el saldo de l'usuari.
     * 
     * @param id identificador de la recompensa
     * @param user usuari que accepta la recompensa
     */
    public void rewardAccept(Long id, User user) {
        Reward reward = rewardRepo.getById(id);

        user.setBalance(user.getBalance() - reward.getPreu());
        reward.setEstat(RewardStatus.ACCEPTED);
        reward.setUser(user);

        rewardRepo.save(reward);
        userRepo.save(user);
    }

    /**
     * Marca una recompensa com a recollida, afegint la data de recollida.
     * 
     * @param id identificador de la recompensa
     * @param user usuari que recull la recompensa
     */
    public void rewardTake(Long id, User user) {
        Reward reward = rewardRepo.getById(id);

        reward.setEstat(RewardStatus.COLLECTED);
        reward.setDataRecollida(LocalDateTime.now());

        rewardRepo.save(reward);
    }
}
