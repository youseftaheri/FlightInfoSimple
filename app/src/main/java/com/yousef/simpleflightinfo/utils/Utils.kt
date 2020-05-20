package com.yousef.simpleflightinfo.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.yousef.simpleflightinfo.R
import com.yousef.simpleflightinfo.network.APIs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Utils {
    fun requestApiDefault(isTokenAPI: Boolean, token: String?): APIs {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().connectTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .addInterceptor { chain: Interceptor.Chain ->
                    val request: Request
                    request = if (isTokenAPI) chain.request().newBuilder().build() else chain.request().newBuilder().addHeader("Accept", "application/json")
                            .addHeader("Authorization", "Bearer $token").build()
                    chain.proceed(request)
                }
                .addInterceptor(interceptor)
                .build()
        val retrofit = Retrofit.Builder().baseUrl(Const.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(APIs::class.java)
    }

    /**
     * Check internet availabilty
     *
     * @param mContext Context of activity or fragment
     * @return Returns true is internet connected and false if no internet connected
     */
    fun isNetworkConnected(mContext: Context?): Boolean {
        val cm = mContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm?.activeNetworkInfo != null
    }

    /**
     * Goto next Activity with animation
     *
     * @param mContext Context of the Activity.
     */
    @JvmStatic
    fun gotoNextActivityAnimation(mContext: Context) {
        (mContext as Activity).overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }

    /**
     * Show Log
     *
     * @param message Message that want to show into Log
     */
    fun showLog(message: String) {
        Log.e("Log Message", "" + message)
    }

    /**
     * Show message dialog
     *
     * @param mContext Context of activity o fragment
     * @param message  Message that shows on Dialog
     * @param listener Set action that you want to performon OK click
     * @return dialog
     */
    @JvmStatic
    fun showMessageDialog(mContext: Context, message: String?, listener: DialogInterface.OnClickListener?): AlertDialog.Builder {
        val dialog = AlertDialog.Builder(mContext)
        dialog.setMessage(message)
        dialog.setCancelable(false)
        dialog.setNegativeButton(mContext.getString(R.string.ok), listener)
        try {
            dialog.show()
        } catch (ignored: Exception) {
        }
        return dialog
    }
}