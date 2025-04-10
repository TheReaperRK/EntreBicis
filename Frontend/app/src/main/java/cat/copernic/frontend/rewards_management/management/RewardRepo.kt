package cat.copernic.frontend.rewards_management.management

import cat.copernic.frontend.core.models.Reward
import cat.copernic.frontend.core.models.User
import retrofit2.Response

class RewardRepo(private val api: RewardApiRest) {

    suspend fun fetchRewards(): List<Reward> {
        val response = api.getAllRewards()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error ${response.code()}: ${response.message()}")
        }
    }

    suspend fun getRewardById(id: Long): Reward {
        return api.getRewardById(id)
    }

    suspend fun solicitarRecompensa(id: Long): Response<Unit> {
        return api.solicitarRecompensa(id)
    }
}