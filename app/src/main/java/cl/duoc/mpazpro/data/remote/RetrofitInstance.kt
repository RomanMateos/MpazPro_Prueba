package cl.duoc.mpazpro.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BACKEND_BASE_URL = "http://10.0.2.2:8080/"
    private const val EXTERNAL_BASE_URL = "https://jsonplaceholder.typicode.com/"

    val backendApi: BackendApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendApiService::class.java)
    }

    val externalApi: ExternalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(EXTERNAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExternalApiService::class.java)
    }
}