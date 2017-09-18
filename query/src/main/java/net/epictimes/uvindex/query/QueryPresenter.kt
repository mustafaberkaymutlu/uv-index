package net.epictimes.uvindex.query


import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import net.epictimes.uvindex.data.Services
import net.epictimes.uvindex.data.response.GetObservationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QueryPresenter constructor(private val services: Services) : MvpBasePresenter<QueryView>() {

    fun getUvIndex(latitude: String, longitude: String, language: String?, units: String?){
        services.getObservationByLatLon(latitude, longitude, language, units)
                .enqueue(object : Callback<GetObservationResponse>{
                    override fun onResponse(call: Call<GetObservationResponse>?, response: Response<GetObservationResponse>?) {
                        val asd: Int? = response?.body()?.weatherList?.get(0)?.uvIndex
                    }

                    override fun onFailure(call: Call<GetObservationResponse>?, t: Throwable?) {

                    }

                })
    }

}