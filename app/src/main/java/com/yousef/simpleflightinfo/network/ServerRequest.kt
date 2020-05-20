package com.yousef.simpleflightinfo.network

import android.content.Context
import com.yousef.simpleflightinfo.R
import com.yousef.simpleflightinfo.model.TokenPOJO
import com.yousef.simpleflightinfo.utils.CustomProgressDialog
import com.yousef.simpleflightinfo.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ServerRequest<T> internal constructor(mContext: Context?, call: Call<T>?) {
    abstract fun onCompletion(response: Response<T>?)

    init {
        if (Utils.isNetworkConnected(mContext)) {
            val pd = CustomProgressDialog(mContext)
            pd.show()
            call!!.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    try {
                        if (pd.isShowing) pd.dismiss()
                        if (response.isSuccessful) {
                            onCompletion(response)
                        } else {
                            Utils.showMessageDialog(mContext!!, mContext.getString(R.string.empty_schedule), null)
                        }
                    } catch (e: Exception) {
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    try {
                        if (pd.isShowing) pd.dismiss()
                        Utils.showLog("Exp : " + t.message)
                        Utils.showMessageDialog(mContext!!, mContext.getString(R.string.empty_schedule), null)
                    } catch (e: Exception) {
                    }
                }
            })
        } else {
            Utils.showMessageDialog(mContext!!, mContext.getString(R.string.no_internet_message), null)
        }
    }
}