package net.epictimes.uvindex.data.interactor

import net.epictimes.uvindex.data.Services
import net.epictimes.uvindex.data.response.GetObservationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkCurrentInteractor constructor(private val services: Services) : CurrentInteractor {

    override fun getCurrent(latitude: Double, longitude: Double, language: String?, units: String?,
                            getCurrentCallback: CurrentInteractor.GetCurrentCallback) {
        services.getObservationByLatLon(latitude, longitude, language, units)
                .enqueue(object : Callback<GetObservationResponse> {
                    override fun onResponse(call: Call<GetObservationResponse>?, response: Response<GetObservationResponse>?) {
                        response?.body()?.weatherList?.get(0)?.let {
                            getCurrentCallback.onSuccessGetCurrent(it)
                        } ?: run {
                            getCurrentCallback.onFailGetCurrent()
                        }
                    }

                    override fun onFailure(call: Call<GetObservationResponse>?, t: Throwable?) {
                        getCurrentCallback.onFailGetCurrent()
                    }
                })
    }

}