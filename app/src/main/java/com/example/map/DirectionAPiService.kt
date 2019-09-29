package com.example.map


import com.example.map.model.RouteResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DirectionAPiService {
    @GET("json")
    fun requestForRoute(@Query("origin") clatlon: String,@Query("destination") dlatlon: String,@Query("key") key: String): Call<RouteResponse>

}