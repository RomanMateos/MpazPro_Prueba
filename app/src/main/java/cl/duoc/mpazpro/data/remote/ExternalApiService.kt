package cl.duoc.mpazpro.data.remote

import retrofit2.http.GET

data class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

interface ExternalApiService {
    @GET("posts")
    suspend fun getPosts(): List<PostDto>
}