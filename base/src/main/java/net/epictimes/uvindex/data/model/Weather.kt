package net.epictimes.uvindex.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

data class Weather(

        @SerializedName("wind_spd")
        val windSpeed: Double,

        @SerializedName("wind_dir")
        val windDirection: Int,

        @SerializedName("wind_cdir")
        val windDirectionAbbreviated: String,

        @SerializedName("wind_cdir_full")
        val windDirFull: String,

        // percent
        @SerializedName("rh")
        val relativeHumidity: Double,

        @SerializedName("dewpt")
        val dewPoint: Double,

        // percent
        @SerializedName("clouds")
        val cloudCoverage: Int,

        // d = day / n = night
        @SerializedName("pod")
        val partOfDay: String,

        // mb
        @SerializedName("pres")
        val pressure: Double,

        @SerializedName("slp")
        val seaLevelPressure: Double,

        @SerializedName("timezone")
        val timezone: String,

        @SerializedName("ts")
        val lastObservationTimestamp: Int,

        // default to km
        @SerializedName("vis")
        val visibility: Double,

        @SerializedName("uv")
        val uvIndex: Double,

        // degrees
        @SerializedName("elev_angle")
        val solarElevationAngle: Int,

        // degrees
        @SerializedName("h_angle")
        val solarHourAngle: Int,

        // format: YYYY-MM-DD:HH
        @SerializedName("datetime")
        val datetime: Date,

        // default to mm
        @SerializedName("precip")
        val precipitation: Double,

        // format: HH:MM
        @SerializedName("sunset")
        val sunsetTime: String,

        // format: HH:MM
        @SerializedName("sunrise")
        val sunriseTime: String,

        @SerializedName("temp")
        val temperature: Double,

        // default to Celcius
        @SerializedName("app_temp")
        val apparentTemp: Double

) : Parcelable {
    constructor(source: Parcel) : this(
            source.readDouble(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readInt(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readString(),
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readInt(),
            source.readInt(),
            source.readSerializable() as Date,
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDouble(windSpeed)
        writeInt(windDirection)
        writeString(windDirectionAbbreviated)
        writeString(windDirFull)
        writeDouble(relativeHumidity)
        writeDouble(dewPoint)
        writeInt(cloudCoverage)
        writeString(partOfDay)
        writeDouble(pressure)
        writeDouble(seaLevelPressure)
        writeString(timezone)
        writeInt(lastObservationTimestamp)
        writeDouble(visibility)
        writeDouble(uvIndex)
        writeInt(solarElevationAngle)
        writeInt(solarHourAngle)
        writeSerializable(datetime)
        writeDouble(precipitation)
        writeString(sunsetTime)
        writeString(sunriseTime)
        writeDouble(temperature)
        writeDouble(apparentTemp)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Weather> = object : Parcelable.Creator<Weather> {
            override fun createFromParcel(source: Parcel): Weather = Weather(source)
            override fun newArray(size: Int): Array<Weather?> = arrayOfNulls(size)
        }
    }
}