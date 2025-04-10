package cat.copernic.frontend.profile_management.management

import android.content.Context
import android.util.Log
import cat.copernic.frontend.auth_management.data.management.AuthApiRest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserRetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/" //

    // Logging para depurar peticiones/respuestas
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
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

    val userApi: UserApiRest by lazy {
        retrofit.create(UserApiRest::class.java)
    }

    fun getApi(context: Context): UserApiRest {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val token = prefs.getString("jwt_token", null)
                val requestBuilder = chain.request().newBuilder()
                token?.let {
                    requestBuilder.addHeader("Authorization", "Bearer $it")
                }
                Log.d("AUTH", "Token en header: Bearer $token")
                chain.proceed(requestBuilder.build())
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiRest::class.java)
    }

}
