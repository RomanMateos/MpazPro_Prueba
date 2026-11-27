package cl.duoc.mpazpro.remote

import cl.duoc.mpazpro.model.Unidad
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/unidades")
    suspend fun listarUnidades(): List<Unidad>

    @GET("api/unidades/{id}")
    suspend fun obtenerUnidad(@Path("id") id: Long): Unidad

    @POST("api/unidades")
    suspend fun crearUnidad(@Body unidad: Unidad): Unidad

    @PUT("api/unidades/{id}")
    suspend fun actualizarUnidad(@Path("id") id: Long, @Body unidad: Unidad): Unidad

    @DELETE("api/unidades/{id}")
    suspend fun eliminarUnidad(@Path("id") id: Long): Response<Unit>
}
