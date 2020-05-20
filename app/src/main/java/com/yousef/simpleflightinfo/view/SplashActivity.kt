package com.yousef.simpleflightinfo.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yousef.simpleflightinfo.R
import com.yousef.simpleflightinfo.network.Events.GetAccessToken
import com.yousef.simpleflightinfo.network.Events.RequestAccessToken
import com.yousef.simpleflightinfo.utils.Const.requestAccessToken
import com.yousef.simpleflightinfo.utils.Const.setAccessToken
import com.yousef.simpleflightinfo.utils.Utils.gotoNextActivityAnimation
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        tokenApi
    }

    public override fun onResume() {
        super.onResume()
        registerEventBus()
        tokenApi
    }

    public override fun onStop() {
        unRegisterEventBus()
        super.onStop()
    }

    val tokenApi: Unit
        get() {
            EventBus.getDefault().post(RequestAccessToken(this, requestAccessToken(this)))
        }

    @SuppressLint("NewApi")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getTokenResult(data: GetAccessToken) {
        setAccessToken(this, data.data!!.access_token)
        startActivity(Intent(applicationContext, MainActivity::class.java))
        gotoNextActivityAnimation(this@SplashActivity)
    }

    fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    fun unRegisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }
}