package cat.copernic.frontend.auth_management.data.source

import cat.copernic.frontend.auth_management.data.management.AuthApiRest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthRetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8080/" // ✅ base limpia sin /api/auth

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

    val authApi: AuthApiRest by lazy {
        retrofit.create(AuthApiRest::class.java)
    }
}
