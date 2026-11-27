package cl.duoc.mpazpro.repository

import cl.duoc.mpazpro.model.Unidad
import cl.duoc.mpazpro.remote.*

class UnidadRepository(private val api: ApiService = RetrofitInstance.apiService) {
    suspend fun listar() = runCatching { api.listarUnidades() }
    suspend fun obtenerPorId(id: Long) = runCatching { api.obtenerUnidad(id) }
    suspend fun crear(u: Unidad) = runCatching { api.crearUnidad(u) }
    suspend fun actualizar(id: Long, u: Unidad) = runCatching { api.actualizarUnidad(id, u) }
    suspend fun eliminar(id: Long) = runCatching {
        val r = api.eliminarUnidad(id)
        if (!r.isSuccessful) throw Exception("Error HTTP ${r.code()}")
    }
}
