package cl.duoc.mpazpro.data.remote

import retrofit2.http.*

data class UsuarioDto(
    val id: Long? = null,
    val nombre: String,
    val correo: String
)

interface BackendApiService {
    @GET("api/usuarios")
    suspend fun getUsuarios(): List<UsuarioDto>

    @POST("api/usuarios")
    suspend fun crearUsuario(@Body usuario: UsuarioDto): UsuarioDto

    @PUT("api/usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Long, @Body usuario: UsuarioDto): UsuarioDto

    @DELETE("api/usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Long)
}