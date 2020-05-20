package com.yousef.simpleflightinfo.utils

import android.content.Context
import com.yousef.simpleflightinfo.model.AirportLatLongPOJO
import com.yousef.simpleflightinfo.model.AirportsPOJO
import com.yousef.simpleflightinfo.model.LuftSchedulesPOJO
import com.yousef.simpleflightinfo.model.TokenPOJO
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

object Const {
    //<-------------------------------------------------------------------------------->\\
    //Basic constants
    //<-------------------------------------------------------------------------------->\\
    private const val MAIN_BASE = "https://api.lufthansa.com/"
    @JvmField
    var BASE_URL = MAIN_BASE + "v1/"
    @JvmField
    var dstLat = "dstLat"
    @JvmField
    var dstLong = "dstLong"
    @JvmField
    var orgLat = "orgLat"
    @JvmField
    var orgLong = "orgLong"
    @JvmField
    var ORIGIN = "ORIGIN"
    @JvmField
    var DESTINATION = "DESTINATION"
    private const val AccessToken = "AccessToken"
    var AM = " AM"
    var PM = " PM"

    //<-------------------------------------------------------------------------------->\\
    //TinyDB read and write
    //<-------------------------------------------------------------------------------->\\
    @JvmStatic
    fun setAccessToken(mContext: Context?, data: String?) {
        TinyDB(mContext).putString(AccessToken, data)
    }

    @JvmStatic
    fun setOrigin(mContext: Context?, data: String?) {
        TinyDB(mContext).putString(ORIGIN, data)
    }

    @JvmStatic
    fun setDestination(mContext: Context?, data: String?) {
        TinyDB(mContext).putString(DESTINATION, data)
    }

    private fun getAccessToken(mContext: Context): String? {
        return TinyDB(mContext).getString(AccessToken)
    }

    private fun getOrigin(mContext: Context): String? {
        return TinyDB(mContext).getString(ORIGIN)
    }

    private fun getDestination(mContext: Context): String? {
        return TinyDB(mContext).getString(DESTINATION)
    }

    //<-------------------------------------------------------------------------------->\\
    //All api's methods
    //<-------------------------------------------------------------------------------->\\
    @JvmStatic
    fun requestAccessToken(mContext: Context): Call<TokenPOJO?>? {
        val map = HashMap<String?, String?>()
        map["client_id"] = LocalUtils.client_id
        map["client_secret"] = LocalUtils.client_secret
        map["grant_type"] = "client_credentials"
        return Utils.requestApiDefault(true, getAccessToken(mContext)).requestAccessTokenPost("/v1/oauth/token", map)
    }

    @JvmStatic
    fun requestSchedules(mContext: Context): Call<LuftSchedulesPOJO?>? {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return Utils.requestApiDefault(false, getAccessToken(mContext)).requestLuftSchedules(
                "/v1/operations/schedules/" + getOrigin(mContext) + "/" + getDestination(mContext) + "/" + date +
                        "?directFlights=1&limit=100")
    }

    @JvmStatic
    fun requestAirports(mContext: Context): Call<AirportsPOJO?>? {
        return Utils.requestApiDefault(false, getAccessToken(mContext)).requestAirports(
                "/v1/references/airports/?lang=en&limit=100&LHoperated=1&offset=410")
    }

    @JvmStatic
    fun requestOriginLatLong(mContext: Context): Call<AirportLatLongPOJO?>? {
        return Utils.requestApiDefault(false, getAccessToken(mContext)).requestOriginLatLong(
                "/v1/references/airports/" + getOrigin(mContext) + "?lang=en&limit=100&LHoperated=1&offset=410")
    }

    @JvmStatic
    fun requestDestinationLatLong(mContext: Context): Call<AirportLatLongPOJO?>? {
        return Utils.requestApiDefault(false, getAccessToken(mContext)).requestDestinationLatLong(
                "/v1/references/airports/" + getDestination(mContext) + "?lang=en&limit=100&LHoperated=1&offset=410")
    }
}