package cl.duoc.mpazpro.repository

import cl.duoc.mpazpro.model.PostExterno
import cl.duoc.mpazpro.remote.ExternalApiService
import cl.duoc.mpazpro.remote.ExternalRetrofitInstance

class NovedadesRepository(
    private val api: ExternalApiService = ExternalRetrofitInstance.apiService
) {
    suspend fun obtenerNovedades(): Result<List<PostExterno>> = runCatching {
        api.obtenerPublicaciones(limite = 15)
    }
}