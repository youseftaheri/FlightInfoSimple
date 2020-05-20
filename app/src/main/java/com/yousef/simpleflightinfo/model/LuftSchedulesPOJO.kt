package com.yousef.simpleflightinfo.model

import com.google.gson.annotations.SerializedName

class LuftSchedulesPOJO {
    @JvmField
    @SerializedName("ScheduleResource")
    var ScheduleResource: ScheduleResources? = null

    class ScheduleResources {
        @JvmField
        @SerializedName("Schedule")
        var Schedules: List<Schedule>? = null
    }

    class Schedule : Comparable<Schedule> {
        @SerializedName("TotalJourney")
        var TotalJourney: TotalJourney? = null

        @JvmField
        @SerializedName("Flight")
        var Flight: Flight? = null
        override fun compareTo(schedule: Schedule): Int {
            return schedule.Flight!!.Departure!!.ScheduledTimeLocal!!.DateTime!!.compareTo(Flight!!.Departure!!.ScheduledTimeLocal!!.DateTime!!, ignoreCase = true)
        }
    }

    class TotalJourney {
        @SerializedName("Duration")
        var Duration: String? = null
    }

    class Flight {
        @JvmField
        @SerializedName("Departure")
        var Departure: Dep_Arr? = null

        @JvmField
        @SerializedName("Arrival")
        var Arrival: Dep_Arr? = null

        @SerializedName("Details")
        var Details: Details? = null

        @JvmField
        @SerializedName("MarketingCarrier")
        var MarketingCarrier: MarketingCarrier? = null
    }

    class MarketingCarrier {
        @JvmField
        @SerializedName("FlightNumber")
        var FlightNumber: String? = null
    }

    class Dep_Arr {
        @SerializedName("AirportCode")
        var AirportCode: String? = null

        @JvmField
        @SerializedName("ScheduledTimeLocal")
        var ScheduledTimeLocal: ScheduledTimeLocal? = null

        @SerializedName("Terminal")
        var Terminal: Terminal? = null
    }

    class ScheduledTimeLocal {
        @JvmField
        @SerializedName("DateTime")
        var DateTime: String? = null
    }

    class Terminal {
        @SerializedName("Name")
        var Name: String? = null
    }

    class Details {
        @SerializedName("Stops")
        var Stops: Stops? = null
    }

    class Stops {
        @SerializedName("StopQuantity")
        var StopQuantity: String? = null
    }
}