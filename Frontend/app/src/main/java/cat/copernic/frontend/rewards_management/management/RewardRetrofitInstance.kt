package cat.copernic.frontend.rewards_management.management

import android.content.Context
import cat.copernic.frontend.auth_management.data.management.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RewardRetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    fun getInstance(context: Context): RewardApiRest {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(context)) // 🔐 Interceptor para el token
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RewardApiRest::class.java)
    }
}
