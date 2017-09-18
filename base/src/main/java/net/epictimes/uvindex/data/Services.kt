package net.epictimes.uvindex.data

import net.epictimes.uvindex.data.response.GetObservationResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Services {
    companion object {
        val API_KEY = "816aa929f11845f9aed2198dc7b0f2f2"
        val BASE_URL = "https://api.weatherbit.io/"
    }

    @GET("/v2.0/current")
    fun getObservationByLatLon(@Query("lat") latitude: String,
                               @Query("lon") longitude: String,
                               @Query("lang") language: String?,
                               @Query("units") units: String?): Call<GetObservationResponse>

}
