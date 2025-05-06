package cat.copernic.frontend.rewards_management.management

import cat.copernic.frontend.core.models.Reward
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RewardApiRest {
    @GET("api/rewards/list")
    suspend fun getAllRewards(): Response<List<Reward>>

    @GET("api/rewards/{id}")
    suspend fun getRewardById(@Path("id") id: Long): Reward

    @POST("api/rewards/{id}/request")
    suspend fun solicitarRecompensa(@Path("id") id: Long): Response<Unit>

    @POST("api/rewards/{id}/take")
    suspend fun recollirRecompensa(@Path("id") id: Long): Response<Unit>

}