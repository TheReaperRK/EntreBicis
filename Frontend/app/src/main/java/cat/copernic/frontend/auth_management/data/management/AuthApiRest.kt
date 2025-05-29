package cat.copernic.frontend.auth_management.data.management

import cat.copernic.frontend.core.models.DTO.LoginResponse
import cat.copernic.frontend.core.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApiRest {
    @POST("api/auth/loginMobile")
    suspend fun login(
        @Query("email") email: String,
        @Query("word") word: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("/api/auth/recover")
    suspend fun recoverMailSender(
        @Field("email") email: String,
    ): Response<ResponseBody>

    @Multipart
    @POST("/api/auth/reset")
    suspend fun resetPassword(
        @Part("email") email: RequestBody,
        @Part("token") token: RequestBody,
        @Part("word") word: RequestBody
    ): Response<ResponseBody>
}



