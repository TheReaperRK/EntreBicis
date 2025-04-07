package cat.copernic.frontend.rewards_management.management

import cat.copernic.frontend.core.models.Reward
import retrofit2.Response
import retrofit2.http.GET

interface RewardApiRest {
    @GET("api/rewards/list")
    suspend fun getAllRewards(): Response<List<Reward>>
}