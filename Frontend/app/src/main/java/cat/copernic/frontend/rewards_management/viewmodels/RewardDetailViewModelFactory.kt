package cat.copernic.frontend.rewards_management.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.frontend.rewards_management.management.RewardRepo
import cat.copernic.frontend.rewards_management.ui.viewmodels.RewardsViewModel

class RewardDetailViewModelFactory(private val repo: RewardRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RewardsViewModel(repo) as T
    }
}
