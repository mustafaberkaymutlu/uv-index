package net.epictimes.uvindex.data.response

import com.google.gson.annotations.SerializedName
import net.epictimes.uvindex.data.model.Weather

data class GetForecastResponse(

        @SerializedName("data")
        val weatherList: List<Weather>,

        @SerializedName("lat")
        val latitude: Double,

        @SerializedName("lon")
        val longitude: Double,

        @SerializedName("timezone")
        val timezone: String,

        @SerializedName("city_name")
        val cityName: String,

        @SerializedName("state_code")
        val stateCode: String,

        @SerializedName("country_code")
        val countryCode: String

)