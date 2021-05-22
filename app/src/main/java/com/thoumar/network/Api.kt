package com.thoumar.network
import com.thoumar.database.models.User
import retrofit2.Call
import retrofit2.http.GET

interface API {

    @GET("/login")
    fun login(): Call<User>

}