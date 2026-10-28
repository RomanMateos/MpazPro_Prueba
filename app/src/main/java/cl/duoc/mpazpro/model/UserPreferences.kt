package cl.duoc.mpazpro.model

data class UserPreferences(
    val rut: String = "",
    val nombre: String = "",
    val correo: String = "",
    val rol: String = "Estudiante",
    val sesionActiva: Boolean = false
)