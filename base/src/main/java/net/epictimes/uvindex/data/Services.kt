package net.epictimes.uvindex.data

import net.epictimes.uvindex.data.response.GetForecastResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Services {
    companion object {
        const val API_KEY = "put-your-api-key-here"
        const val BASE_URL = "https://api.weatherbit.io/"
    }

    @GET("/v2.0/forecast/hourly")
    fun getForecastByLatLon(@Query("lat") latitude: Double,
                            @Query("lon") longitude: Double,
                            @Query("lang") language: String?,
                            @Query("units") units: String?,
                            @Query("hours") hours: Int?): Call<GetForecastResponse>

}
