package cat.copernic.frontend.profile_management.management

import cat.copernic.frontend.core.models.LoginResponse
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
    @GET("/api/user/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): Response<User>
}