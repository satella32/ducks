package com.example.ducks.api
import com.example.ducks.DuckResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DuckApi {
    @GET("v2/random")
    suspend fun getRandomDuck(): DuckResponse

    @GET("v2/http/{code}")
    fun getDuckByHttp(@Path("code") code: String): Void

    @GET("v2/{id}.jpg")
    fun getDuckJpg(@Path("id") id: String): Void

    @GET("v2/{id}.gif")
    fun getDuckGif(@Path("id") id: String): Void



}
