package co.android.helper.hkaqi.data.model

import android.os.Parcelable

enum class MAURITIUS{
    PORT_LOUIS, QUATRE_BORNES, NOTHING
}

class AQIDetailsModel: java.io.Serializable{
    var cityName: String = ""
    var aqi: String = ""
    var defaultCityName: String = MAURITIUS.NOTHING.name
    override fun toString(): String {
        return "AQIDetailsModel(cityName='$cityName', aqi='$aqi', defaultCityName='$defaultCityName')"
    }

}