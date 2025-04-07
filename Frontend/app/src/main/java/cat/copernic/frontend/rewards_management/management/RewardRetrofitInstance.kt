package cat.copernic.frontend.rewards_management.management

import cat.copernic.frontend.profile_management.management.UserApiRest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RewardRetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080/" //

    // Logging para depurar peticiones/respuestas
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .followRedirects(true)
        .followSslRedirects(true)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val rewardApi: RewardApiRest by lazy {
        retrofit.create(RewardApiRest::class.java)
    }
}