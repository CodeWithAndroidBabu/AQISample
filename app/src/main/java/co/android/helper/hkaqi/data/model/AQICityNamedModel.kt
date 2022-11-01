package co.android.helper.hkaqi.data.model


import com.google.gson.annotations.SerializedName

data class AQICityNamedModel(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("status")
    val status: String = ""
) {
    data class Data(
        @SerializedName("aqi")
        val aqi: Int = 0,
        @SerializedName("attributions")
        val attributions: List<Attribution> = listOf(),
        @SerializedName("city")
        val city: City = City(),
        @SerializedName("debug")
        val debug: Debug = Debug(),
        @SerializedName("dominentpol")
        val dominentpol: String = "",
        @SerializedName("forecast")
        val forecast: Forecast = Forecast(),
        @SerializedName("iaqi")
        val iaqi: Iaqi = Iaqi(),
        @SerializedName("idx")
        val idx: Int = 0,
        @SerializedName("time")
        val time: Time = Time()
    ) {
        data class Attribution(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("url")
            val url: String = ""
        )

        data class City(
            @SerializedName("geo")
            val geo: List<Double> = listOf(),
            @SerializedName("location")
            val location: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("url")
            val url: String = ""
        )

        data class Debug(
            @SerializedName("sync")
            val sync: String = ""
        )

        data class Forecast(
            @SerializedName("daily")
            val daily: Daily = Daily()
        ) {
            data class Daily(
                @SerializedName("o3")
                val o3: List<O3> = listOf(),
                @SerializedName("pm10")
                val pm10: List<Pm10> = listOf(),
                @SerializedName("pm25")
                val pm25: List<Pm25> = listOf(),
                @SerializedName("uvi")
                val uvi: List<Uvi> = listOf()
            ) {
                data class O3(
                    @SerializedName("avg")
                    val avg: Int = 0,
                    @SerializedName("day")
                    val day: String = "",
                    @SerializedName("max")
                    val max: Int = 0,
                    @SerializedName("min")
                    val min: Int = 0
                )

                data class Pm10(
                    @SerializedName("avg")
                    val avg: Int = 0,
                    @SerializedName("day")
                    val day: String = "",
                    @SerializedName("max")
                    val max: Int = 0,
                    @SerializedName("min")
                    val min: Int = 0
                )

                data class Pm25(
                    @SerializedName("avg")
                    val avg: Int = 0,
                    @SerializedName("day")
                    val day: String = "",
                    @SerializedName("max")
                    val max: Int = 0,
                    @SerializedName("min")
                    val min: Int = 0
                )

                data class Uvi(
                    @SerializedName("avg")
                    val avg: Int = 0,
                    @SerializedName("day")
                    val day: String = "",
                    @SerializedName("max")
                    val max: Int = 0,
                    @SerializedName("min")
                    val min: Int = 0
                )
            }
        }

        data class Iaqi(
            @SerializedName("co")
            val co: Co = Co(),
            @SerializedName("h")
            val h: H = H(),
            @SerializedName("no2")
            val no2: No2 = No2(),
            @SerializedName("o3")
            val o3: O3 = O3(),
            @SerializedName("p")
            val p: P = P(),
            @SerializedName("pm10")
            val pm10: Pm10 = Pm10(),
            @SerializedName("pm25")
            val pm25: Pm25 = Pm25(),
            @SerializedName("so2")
            val so2: So2 = So2(),
            @SerializedName("t")
            val t: T = T(),
            @SerializedName("w")
            val w: W = W()
        ) {
            data class Co(
                @SerializedName("v")
                val v: Double = 0.0
            )

            data class H(
                @SerializedName("v")
                val v: Float = 0f
            )

            data class No2(
                @SerializedName("v")
                val v: Double = 0.0
            )

            data class O3(
                @SerializedName("v")
                val v: Double = 0.0
            )

            data class P(
                @SerializedName("v")
                val v: Float = 0f
            )

            data class Pm10(
                @SerializedName("v")
                val v: Float = 0f
            )

            data class Pm25(
                @SerializedName("v")
                val v: Float = 0f
            )

            data class So2(
                @SerializedName("v")
                val v: Double = 0.0
            )

            data class T(
                @SerializedName("v")
                val v: Float = 0f
            )

            data class W(
                @SerializedName("v")
                val v: Float = 0f
            )
        }

        data class Time(
            @SerializedName("iso")
            val iso: String = "",
            @SerializedName("s")
            val s: String = "",
            @SerializedName("tz")
            val tz: String = "",
            @SerializedName("v")
            val v: Int = 0
        )
    }
}