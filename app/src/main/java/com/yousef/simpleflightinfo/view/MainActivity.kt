package com.yousef.simpleflightinfo.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.snackbar.Snackbar
import com.yousef.simpleflightinfo.R
import com.yousef.simpleflightinfo.R2
import com.yousef.simpleflightinfo.model.AirportsPOJO
import com.yousef.simpleflightinfo.network.Events.GetAirports
import com.yousef.simpleflightinfo.network.Events.RequestAirports
import com.yousef.simpleflightinfo.utils.Const.requestAirports
import com.yousef.simpleflightinfo.utils.Const.setDestination
import com.yousef.simpleflightinfo.utils.Const.setOrigin
import com.yousef.simpleflightinfo.utils.Utils.gotoNextActivityAnimation
import com.yousef.simpleflightinfo.view.MainActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class MainActivity : AppCompatActivity() {
    @BindView(R.id.etOrigin)
    @JvmField var etOrigin: EditText? = null

    @BindView(R.id.etDestination)
    @JvmField var etDestination: EditText? = null

    @BindView(R.id.root)
    @JvmField var coordinatorLayout: CoordinatorLayout? = null
    var airportNameStr: MutableList<String>? = null
    var airports: List<AirportsPOJO.Airport>? = null
    var airportIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        airportsApi
    }

    @OnClick(R.id.btShowSchedules)
    fun btShowSchedulesClick() {
        if (etOrigin!!.text.toString().isEmpty()) showError(getString(R.string.enter_origin)) else if (etDestination!!.text.toString().isEmpty()) showError(getString(R.string.enter_destination)) else {
            airportIndex = find(airportNameStr, etOrigin!!.text.toString())
            if (airportIndex > -1) setOrigin(this, airports!![airportIndex].AirportCode) else setOrigin(this, etOrigin!!.text.toString())
            airportIndex = find(airportNameStr, etDestination!!.text.toString())
            if (airportIndex > -1) setDestination(this, airports!![airportIndex].AirportCode) else setDestination(this, etDestination!!.text.toString())
            startActivity(Intent(applicationContext, SchedulesActivity::class.java))
            gotoNextActivityAnimation(this@MainActivity)
        }
    }

    public override fun onStart() {
        super.onStart()
        registerEventBus()
        airportsApi
    }

    public override fun onResume() {
        super.onResume()
        registerEventBus()
        airportsApi
    }

    override fun onDestroy() {
        unRegisterEventBus()
        super.onDestroy()
    }

    public override fun onStop() {
        unRegisterEventBus()
        super.onStop()
    }

    val airportsApi: Unit
        get() {
            EventBus.getDefault().post(RequestAirports(this, requestAirports(this)))
        }

    @OnClick(R.id.ivClearOrigin)
    fun ivClearOriginClick() {
        etOrigin!!.setText("")
    }

    @OnClick(R.id.ivClearDestination)
    fun ivClearDestinationClick() {
        etDestination!!.setText("")
    }

    @SuppressLint("NewApi")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getAirportsResult(data: GetAirports) {
        airports = data.data!!.AirportResource!!.Airports!!.Airport
        setAutoStrings()
    }

    fun setAutoStrings() {
        airportNameStr = ArrayList()
        for (airport in airports!!) {
            (airportNameStr as ArrayList<String>).add(airport.Names!!.Name!!.fullName + " - " + airport.CityCode + " - " + airport.CountryCode)
        }
        val tvOrigin = findViewById<AutoCompleteTextView>(R.id.etOrigin)
        val originAdp = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, airportNameStr as ArrayList<String>)
        tvOrigin.threshold = 1
        tvOrigin.setAdapter(originAdp)
        val tvDestination = findViewById<AutoCompleteTextView>(R.id.etDestination)
        val destinationAdp = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, airportNameStr as ArrayList<String>)
        tvDestination.threshold = 1
        tvDestination.setAdapter(destinationAdp)
    }

    fun find(a: List<String>?, target: String): Int {
        for (i in a!!.indices) if (target == a[i]) return i
        return -1
    }

    /**
     * Snackbar shows error
     */
    private fun showError(msg: String) {
        val snackbar = Snackbar
                .make(coordinatorLayout!!, msg, Snackbar.LENGTH_LONG)
        val sbView = snackbar.view
        val textView = sbView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(Color.YELLOW)
        snackbar.show()
    }

    fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    fun unRegisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }
}