package cl.duoc.mpazpro.remote

import cl.duoc.mpazpro.model.PostExterno
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ExternalApiService {

    @GET("posts")
    suspend fun obtenerPublicaciones(@Query("_limit") limite: Int = 15): List<PostExterno>
}

object ExternalRetrofitInstance {

    private const val EXTERNAL_BASE_URL = "https://jsonplaceholder.typicode.com/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val apiService: ExternalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(EXTERNAL_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExternalApiService::class.java)
    }
}