package cat.copernic.frontend.profile_management.management

import cat.copernic.frontend.core.models.DTO.LoginResponse
import cat.copernic.frontend.core.models.DTO.ProfileDTO
import cat.copernic.frontend.core.models.DTO.UserDTO
import cat.copernic.frontend.core.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiRest {
    @GET("/api/auth/user/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): Response<ProfileDTO>

    @GET("/api/auth/userDTO/{email}")
    suspend fun getUserByEmailDTO(@Path("email") email: String): Response<UserDTO>

    @Multipart
    @POST("/api/auth/user/update")
    suspend fun updateUser(
        @Part("email") email: RequestBody,
        @Part("name") name: RequestBody,
        @Part("surnames") surnames: RequestBody,
        @Part("population") population: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("observations") observations: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): Response<Void>
}

