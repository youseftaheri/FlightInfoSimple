package com.yousef.simpleflightinfo.model

import com.google.gson.annotations.SerializedName

class AirportLatLongPOJO {
    @JvmField
    @SerializedName("AirportResource")
    var AirportResource: AirportResources? = null

    class AirportResources {
        @JvmField
        @SerializedName("Airports")
        var Airports: Airports? = null
    }

    class Airports {
        @JvmField
        @SerializedName("Airport")
        var Airport: Airport? = null
    }

    class Airport {
        @JvmField
        @SerializedName("Position")
        var Position: Position? = null
    }

    class Position {
        @JvmField
        @SerializedName("Coordinate")
        var Coordinate: Coordinate? = null
    }

    class Coordinate {
        @JvmField
        @SerializedName("Latitude")
        var Latitude: String? = null

        @JvmField
        @SerializedName("Longitude")
        var Longitude: String? = null
    }
}