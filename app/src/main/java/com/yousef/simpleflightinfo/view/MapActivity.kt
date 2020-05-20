package com.yousef.simpleflightinfo.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.yousef.simpleflightinfo.R
import com.yousef.simpleflightinfo.network.Events.*
import com.yousef.simpleflightinfo.utils.Const
import com.yousef.simpleflightinfo.utils.Const.DESTINATION
import com.yousef.simpleflightinfo.utils.Const.ORIGIN
import com.yousef.simpleflightinfo.utils.Const.requestDestinationLatLong
import com.yousef.simpleflightinfo.utils.Const.requestOriginLatLong
import com.yousef.simpleflightinfo.utils.TinyDB
import com.yousef.simpleflightinfo.utils.Utils.gotoNextActivityAnimation
import com.yousef.simpleflightinfo.view.MapActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

class MapActivity : FragmentActivity(), OnMapReadyCallback {
    @BindView(R.id.tvOrigin)
    @JvmField var tvOrigin: TextView? = null

    @BindView(R.id.tvDestination)
    @JvmField var tvDestination: TextView? = null
    private var mMap: GoogleMap? = null
    var circleDrawable: Drawable? = null
    var markerIcon: BitmapDescriptor? = null
    var dblOrgLat: Double? = null
    var dblOrgLong: Double? = null
    var dblDstLat: Double? = null
    var dblDstLong: Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        ButterKnife.bind(this)
        tvOrigin!!.text = TinyDB(this).getString(ORIGIN)
        tvDestination!!.text = TinyDB(this).getString(DESTINATION)
    }

    public override fun onStart() {
        super.onStart()
        registerEventBus()
        originLatLongApi
    }

    public override fun onResume() {
        super.onResume()
        registerEventBus()
        originLatLongApi
    }

    override fun onDestroy() {
        unRegisterEventBus()
        super.onDestroy()
    }

    public override fun onStop() {
        unRegisterEventBus()
        super.onStop()
    }

    val originLatLongApi: Unit
        get() {
            EventBus.getDefault().post(RequestOriginLatLong(this, requestOriginLatLong(this)))
        }

    fun showMarkers() {
        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = (supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        circleDrawable = resources.getDrawable(R.drawable.map_marker)
        markerIcon = getMarkerIconFromDrawable(circleDrawable)
        mMap!!.uiSettings.isZoomControlsEnabled = true
        dblOrgLat = TinyDB(this).getString(Const.orgLat)!!.toDouble()
        dblOrgLong = TinyDB(this).getString(Const.orgLong)!!.toDouble()
        dblDstLat = TinyDB(this).getString(Const.dstLat)!!.toDouble()
        dblDstLong = TinyDB(this).getString(Const.dstLong)!!.toDouble()
        // Add a marker move the camera
        val origin = LatLng(dblOrgLat!!, dblOrgLong!!)
        val destination = LatLng(dblDstLat!!, dblDstLong!!)
        mMap!!.addMarker(MarkerOptions().position(origin).icon(markerIcon)
                .draggable(false).visible(true).title("Origin: " + TinyDB(this).getString(ORIGIN)))
        mMap!!.addMarker(MarkerOptions().position(destination).icon(markerIcon)
                .draggable(false).visible(true).title("Destination: " + TinyDB(this).getString(DESTINATION))).showInfoWindow()
        val builder = LatLngBounds.Builder()
        //the include method will calculate the min and max bound.
        builder.include(origin)
        builder.include(destination)
        val bounds = builder.build()
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = ((if(height < width) width else height) * 0.25).toInt()
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding))

        //Polyline options
        val options = PolylineOptions()
        val pattern = listOf(Dash(30F), Gap(10F))
        mMap!!.addPolyline(options.width(10f).color(R.color.colorPrimary).geodesic(false).pattern(pattern).add(origin).add(destination))
    }

    @OnClick(R.id.btBack)
    fun btBackClick() {
        startActivity(Intent(applicationContext, SchedulesActivity::class.java))
        gotoNextActivityAnimation(this@MapActivity)
    }

    @SuppressLint("NewApi")
    @Subscribe
    fun getOriginLatLongResult(data: GetOriginLatLong) {
        TinyDB(this).putString(Const.orgLat, data.data!!.AirportResource!!.Airports!!.Airport!!.Position!!.Coordinate!!.Latitude)
        TinyDB(this).putString(Const.orgLong, data.data!!.AirportResource!!.Airports!!.Airport!!.Position!!.Coordinate!!.Longitude)
        EventBus.getDefault().post(RequestDestinationLatLong(this, requestDestinationLatLong(this)))
    }

    @SuppressLint("NewApi")
    @Subscribe
    fun getDestinationLatLongResult(data: GetDestinationLatLong) {
        TinyDB(this).putString(Const.dstLat, data.data!!.AirportResource!!.Airports!!.Airport!!.Position!!.Coordinate!!.Latitude)
        TinyDB(this).putString(Const.dstLong, data.data!!.AirportResource!!.Airports!!.Airport!!.Position!!.Coordinate!!.Longitude)
        showMarkers()
    }

    private fun getMarkerIconFromDrawable(drawable: Drawable?): BitmapDescriptor {
        val height = 150
        val width = 100
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable!!.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    fun unRegisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }
}