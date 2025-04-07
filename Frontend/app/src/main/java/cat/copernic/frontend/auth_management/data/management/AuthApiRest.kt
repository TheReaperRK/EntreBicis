package cat.copernic.frontend.auth_management.data.management

import cat.copernic.frontend.core.models.LoginResponse
import cat.copernic.frontend.core.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AuthApiRest {
    @POST("api/auth/loginMobile")
    suspend fun login(
        @Query("email") email: String,
        @Query("word") word: String
    ): Response<User>

    @Multipart
    @POST("register")
    suspend fun register(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<User>
}



