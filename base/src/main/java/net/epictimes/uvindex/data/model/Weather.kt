package net.epictimes.uvindex.data.model

import com.google.gson.annotations.SerializedName


class Weather {

    @SerializedName("lat")
    var latitude: String? = null

    @SerializedName("lon")
    var longitude: String? = null

    @SerializedName("wind_spd")
    var windSpeed: Double? = null

    @SerializedName("wind_dir")
    var windDirection: Int? = null

    @SerializedName("wind_cdir")
    var windDirectionAbbreviated: String? = null

    @SerializedName("wind_cdir_full")
    var windDirFull: String? = null

    // percent
    @SerializedName("rh")
    var relativeHumidity: Double? = null

    @SerializedName("dewpt")
    var dewPoint: Double? = null

    // percent
    @SerializedName("clouds")
    var cloudCoverage: Int? = null

    // d = day / n = night
    @SerializedName("pod")
    var partOfDay: String? = null

    // mb
    @SerializedName("pres")
    var pressure: Double? = null

    @SerializedName("slp")
    var seaLevelPressure: Double? = null

    @SerializedName("timezone")
    var timezone: String? = null

    // format: YYYY-MM-DD HH:MM
    @SerializedName("ob_time")
    var lastObservationTime: String? = null

    @SerializedName("ts")
    var lastObservationTimestamp: Int? = null

    @SerializedName("country_code")
    var countryCode: String? = null

    @SerializedName("state_code")
    var stateCode: String? = null

    @SerializedName("city_name")
    var cityName: String? = null

    // default to km
    @SerializedName("vis")
    var visibility: Int? = null

    @SerializedName("uv")
    var uvIndex: Int? = null

    @SerializedName("station")
    var sourceStationId: String? = null

    // degrees
    @SerializedName("elev_angle")
    var solarElevationAngle: Int? = null

    // degrees
    @SerializedName("h_angle")
    var solarHourAngle: Int? = null

    // format: YYYY-MM-DD:HH
    @SerializedName("datetime")
    var datetime: String? = null

    // default to mm
    @SerializedName("precip")
    var precipitation: Int? = null

    // format: HH:MM
    @SerializedName("sunset")
    var sunsetTime: String? = null

    // format: HH:MM
    @SerializedName("sunrise")
    var sunriseTime: String? = null

    @SerializedName("temp")
    var temperature: Double? = null

    // default to Celcius
    @SerializedName("app_temp")
    var apparentTemp: Double? = null

}