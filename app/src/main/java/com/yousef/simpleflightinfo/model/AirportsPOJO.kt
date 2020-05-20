package com.yousef.simpleflightinfo.model

import com.google.gson.annotations.SerializedName

class AirportsPOJO {
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
        var Airport: List<Airport>? = null
    }

    class Airport {
        @JvmField
        @SerializedName("AirportCode")
        var AirportCode: String? = null

        @JvmField
        @SerializedName("CityCode")
        var CityCode: String? = null

        @JvmField
        @SerializedName("CountryCode")
        var CountryCode: String? = null

        @SerializedName("Position")
        var Position: Position? = null

        @JvmField
        @SerializedName("Names")
        var Names: Name? = null
    }

    class Position {
        @SerializedName("Coordinate")
        var Coordinate: Coordinate? = null
    }

    class Coordinate {
        @SerializedName("Latitude")
        var Latitude: String? = null

        @SerializedName("Longitude")
        var Longitude: String? = null
    }

    class Name {
        @JvmField
        @SerializedName("Name")
        var Name: AirportName? = null
    }

    class AirportName {
        @JvmField
        @SerializedName("$")
        var fullName: String? = null
    }
}