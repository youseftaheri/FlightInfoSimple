package com.yousef.simpleflightinfo.network

import android.annotation.SuppressLint
import android.content.Context
import com.yousef.simpleflightinfo.model.AirportLatLongPOJO
import com.yousef.simpleflightinfo.model.AirportsPOJO
import com.yousef.simpleflightinfo.model.LuftSchedulesPOJO
import com.yousef.simpleflightinfo.model.TokenPOJO
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

@SuppressLint("NewApi")
class Events {
    class RequestAccessToken(mContext: Context?, call: Call<TokenPOJO?>?) {
        init {
            object : ServerRequest<TokenPOJO?>(mContext, call) {
                override fun onCompletion(response: Response<TokenPOJO?>?) {
                    if (response != null) {
                        EventBus.getDefault().post(GetAccessToken(response.body()))
                    }
                }
            }
        }
    }

    class GetAccessToken internal constructor(var data: TokenPOJO?)

    class RequestLuftSchedules(mContext: Context?, call: Call<LuftSchedulesPOJO?>?) {
        init {
            object : ServerRequest<LuftSchedulesPOJO?>(mContext, call) {
                override fun onCompletion(response: Response<LuftSchedulesPOJO?>?) {
                    if (response != null) {
                        EventBus.getDefault().post(GetLuftSchedules(response.body()))
                    }
                }
            }
        }
    }

    class GetLuftSchedules internal constructor(var data: LuftSchedulesPOJO?)

    class RequestAirports(mContext: Context?, call: Call<AirportsPOJO?>?) {
        init {
            object : ServerRequest<AirportsPOJO?>(mContext, call) {
                override fun onCompletion(response: Response<AirportsPOJO?>?) {
                    if (response != null) {
                        EventBus.getDefault().post(GetAirports(response.body()))
                    }
                }
            }
        }
    }

    class GetAirports internal constructor(var data: AirportsPOJO?)

    class RequestOriginLatLong(mContext: Context?, call: Call<AirportLatLongPOJO?>?) {
        init {
            object : ServerRequest<AirportLatLongPOJO?>(mContext, call) {
                override fun onCompletion(response: Response<AirportLatLongPOJO?>?) {
                    if (response != null) {
                        EventBus.getDefault().post(GetOriginLatLong(response.body()))
                    }
                }
            }
        }
    }

    class GetOriginLatLong internal constructor(var data: AirportLatLongPOJO?)

    class RequestDestinationLatLong(mContext: Context?, call: Call<AirportLatLongPOJO?>?) {
        init {
            object : ServerRequest<AirportLatLongPOJO?>(mContext, call) {
                override fun onCompletion(response: Response<AirportLatLongPOJO?>?) {
                    if (response != null) {
                        EventBus.getDefault().post(GetDestinationLatLong(response.body()))
                    }
                }
            }
        }
    }

    class GetDestinationLatLong internal constructor(var data: AirportLatLongPOJO?)
}