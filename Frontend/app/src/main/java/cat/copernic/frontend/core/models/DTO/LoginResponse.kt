package cat.copernic.frontend.core.models.DTO

import cat.copernic.frontend.core.models.User

data class LoginResponse (
    val token: String,
    val user: UserDTO
)