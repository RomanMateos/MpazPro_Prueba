package cl.duoc.mpazpro.model

data class UsuarioUiState(
    val rut: String = "",
    val nombre: String = "",
    val correo: String = "",
    val rolSeleccionado: String = "Estudiante",
    val fotoUri: String = "",
    val errores: Map<String, String> = emptyMap(),
    val esValido: Boolean = false,
    val guardando: Boolean = false
)