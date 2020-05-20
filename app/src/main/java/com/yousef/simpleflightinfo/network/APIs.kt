package com.yousef.simpleflightinfo.network

import com.yousef.simpleflightinfo.model.AirportLatLongPOJO
import com.yousef.simpleflightinfo.model.AirportsPOJO
import com.yousef.simpleflightinfo.model.LuftSchedulesPOJO
import com.yousef.simpleflightinfo.model.TokenPOJO
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface APIs {
    @FormUrlEncoded
    @POST
    fun requestAccessTokenPost(@Url url_part: String?, @FieldMap map: HashMap<String?, String?>?): Call<TokenPOJO?>?

    @GET
    fun requestLuftSchedules(@Url url_part: String?): Call<LuftSchedulesPOJO?>?

    @GET
    fun requestAirports(@Url url_part: String?): Call<AirportsPOJO?>?

    @GET
    fun requestOriginLatLong(@Url url_part: String?): Call<AirportLatLongPOJO?>?

    @GET
    fun requestDestinationLatLong(@Url url_part: String?): Call<AirportLatLongPOJO?>?
}