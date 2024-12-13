package data.model

data class RegisterRequest(
    val username: String,
    val password: String,
    val repeatPassword: String,
    val name: String,
    val email: String
)

