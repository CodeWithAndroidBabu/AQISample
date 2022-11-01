package co.android.helper.hkaqi.data

import co.android.helper.hkaqi.data.model.MAURITIUS

object AppConstants {
    interface Network{
        companion object{
            /*https://api.waqi.info//feed/geo:22.3193;114.1694 /?token=243ea5d54e013e56c9eb9c6ead975c1c561ec2ba*/
            const val AQI_BY_CITY = "feed/{city}/"
            const val AQI_BY_SEARCH = "search/"
            const val AQI_BY_GEO = "feed/geo:{lat};{lng}/"
        }
    }

    interface Keys{
        companion object{
            const val SERIALIZABLE_DATA = "SERIALIZABLE_DATA"
            const val COUNTRY = "COUNTRY"
            const val MAURITIUS_PLACE_NAME = "MAURITIUS_PLACE_NAME"
        }
    }

    interface Value{
        companion object{
            const val MAURITIUS_PORT_LOUIS = "Mauritius Port Louis"
            const val MAURITIUS_QUATRE_BORNES = "Mauritius Quatre Bornes"
            const val AFRICA = "Africa"
        }
    }


}