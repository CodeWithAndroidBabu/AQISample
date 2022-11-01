package co.android.helper.hkaqi.data.model


import com.google.gson.annotations.SerializedName

data class AQISearchModel(
    @SerializedName("data")
    val `data`: List<Data> = listOf(),
    @SerializedName("status")
    val status: String = ""
) {
    data class Data(
        @SerializedName("aqi")
        val aqi: String = "",
        @SerializedName("station")
        val station: Station = Station(),
        @SerializedName("time")
        val time: Time = Time(),
        @SerializedName("uid")
        val uid: Int = 0
    ) {
        data class Station(
            @SerializedName("country")
            val country: String = "",
            @SerializedName("geo")
            val geo: List<Double> = listOf(),
            @SerializedName("name")
            val name: String = "",
            @SerializedName("url")
            val url: String = ""
        )

        data class Time(
            @SerializedName("stime")
            val stime: String = "",
            @SerializedName("tz")
            val tz: String = "",
            @SerializedName("vtime")
            val vtime: Int = 0
        )
    }
}