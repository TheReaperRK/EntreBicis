package cat.copernic.frontend.profile_management.management

import cat.copernic.frontend.core.models.DTO.ProfileDTO
import cat.copernic.frontend.core.models.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class UserRepo(private val api: UserApiRest) {

    suspend fun getUserByEmail(email: String): Response<ProfileDTO> {
        return api.getUserByEmail(email)
    }

    suspend fun updateUser(
        email: String,
        name: String,
        surnames: String,
        population: String,
        phone: String,
        observations: String,
        image: MultipartBody.Part? = null
    ): Response<Void> {
        return api.updateUser(
            email.toRequestBody("text/plain".toMediaTypeOrNull()),
            name.toRequestBody("text/plain".toMediaTypeOrNull()),
            surnames.toRequestBody("text/plain".toMediaTypeOrNull()),
            population.toRequestBody("text/plain".toMediaTypeOrNull()),
            phone.toRequestBody("text/plain".toMediaTypeOrNull()),
            observations.toRequestBody("text/plain".toMediaTypeOrNull()),
            image
        )
    }
}
