package cat.copernic.frontend.rewards_management.viewmodels

import androidx.lifecycle.ViewModel
import cat.copernic.frontend.rewards_management.management.RewardRepo
import cat.copernic.frontend.rewards_management.ui.viewmodels.RewardsViewModel

class RewardsViewModelFactory(private val repo: RewardRepo) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RewardsViewModel(repo) as T
    }
}
