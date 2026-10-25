package cl.duoc.mpazpro.model

// Estado adaptado estrictamente al caso MPAZ PRO (Roles y datos ficticios)
data class UsuarioUiState(
    val rut: String = "",
    val nombre: String = "",
    val correo: String = "",
    val rolSeleccionado: String = "Estudiante", // Estudiante, Docente, UTP
    val errores: Map<String, String> = emptyMap(),
    val esValido: Boolean = false
)