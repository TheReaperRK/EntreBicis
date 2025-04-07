package cat.copernic.frontend.profile_management.management

import cat.copernic.frontend.core.models.User
import retrofit2.Response

class UserRepo(private val api: UserApiRest) {

    suspend fun getUserByEmail(email: String): Response<User> {
        return api.getUserByEmail(email)
    }
}
