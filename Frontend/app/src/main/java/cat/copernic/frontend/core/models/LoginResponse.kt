package cat.copernic.frontend.core.models

data class LoginResponse(
    val email: String,
    val sessionKey: String
)
