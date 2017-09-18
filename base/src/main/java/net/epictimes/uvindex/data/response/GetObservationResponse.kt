package net.epictimes.uvindex.data.response

import com.google.gson.annotations.SerializedName
import net.epictimes.uvindex.data.model.Weather


class GetObservationResponse {

    @SerializedName("data")
    var weatherList: List<Weather>? = null

    @SerializedName("count")
    var count: Int? = null

}