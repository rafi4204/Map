package com.example.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DirectionAPiService {
    @GET("json?origin={clatitude},{clongitude}&destination={dlatitude},{dlongitude}&key=AIzaSyBEIDRtKl5v7y-IwW19pp_KtbxasguSYFY")
    fun requestForRoute(@Path("clatitude") clatitude: Double,
                        @Path("clongitude") clongitude: Double,
                        @Path("dlatitude") dlatitude: Double,
                        @Path("dlongitude") dlongitude: Double): Call<RouteResponse>

}