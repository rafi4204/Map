package com.example.map

import android.util.Log
import com.example.map.model.RouteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCall {

    fun getRetroObject( clatlon:String,dlatlon:String,key:String){
        var retrofit =
            Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/directions/").addConverterFactory(
                GsonConverterFactory.create()
            ).build()


        var api: DirectionAPiService = retrofit.create(DirectionAPiService::class.java)

        Log.d("2", "https://maps.googleapis.com/maps/api/directions/"+"json?"+"origin="+clatlon+"&destination="+dlatlon+"&key="+key)
        val str = api.requestForRoute(clatlon,dlatlon,key).enqueue(object : Callback<RouteResponse> {
            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                Log.d("2", t.message)
            }

            /**
             * Invoked for a received HTTP response.
             *
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(call: Call<RouteResponse>,response: Response<RouteResponse>) {
                Log.d("2", response?.body()?.routes?.size.toString())
            }


        })
    }

}