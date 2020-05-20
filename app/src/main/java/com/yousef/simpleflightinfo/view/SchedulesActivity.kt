package com.yousef.simpleflightinfo.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.yousef.simpleflightinfo.R
import com.yousef.simpleflightinfo.model.LuftSchedulesPOJO.Schedule
import com.yousef.simpleflightinfo.network.Events.GetLuftSchedules
import com.yousef.simpleflightinfo.network.Events.RequestLuftSchedules
import com.yousef.simpleflightinfo.utils.Const.DESTINATION
import com.yousef.simpleflightinfo.utils.Const.ORIGIN
import com.yousef.simpleflightinfo.utils.Const.requestSchedules
import com.yousef.simpleflightinfo.utils.TinyDB
import com.yousef.simpleflightinfo.utils.Utils.gotoNextActivityAnimation
import com.yousef.simpleflightinfo.utils.Utils.showMessageDialog
import com.yousef.simpleflightinfo.view.SchedulesActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

class SchedulesActivity : AppCompatActivity() {
    @BindView(R.id.recyclerView)
    @JvmField var recyclerView: RecyclerView? = null

    @BindView(R.id.swipeRefresh)
    @JvmField var swipeRefresh: SwipeRefreshLayout? = null

    @BindView(R.id.tvOrigin)
    @JvmField var tvOrigin: TextView? = null

    @BindView(R.id.tvDestination)
    @JvmField var tvDestination: TextView? = null
    var schedulesList: MutableList<Schedule> = ArrayList()
    var schedulesAdapter: SchedulesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedules)
        ButterKnife.bind(this)
        iniUI()
        setData()
    }

    fun iniUI() {
        tvOrigin!!.text = TinyDB(this).getString(ORIGIN)
        tvDestination!!.text = TinyDB(this).getString(DESTINATION)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        schedulesAdapter = SchedulesAdapter(this, schedulesList)
        swipeRefresh!!.setOnRefreshListener {
            swipeRefresh!!.isRefreshing = false
            schedulesApi
        }
    }

    public override fun onResume() {
        super.onResume()
        registerEventBus()
        schedulesApi
    }

    public override fun onStop() {
        super.onStop()
        unRegisterEventBus()
    }

    val schedulesApi: Unit
        get() {
            EventBus.getDefault().post(RequestLuftSchedules(this, requestSchedules(this)))
        }

    fun setData() {
        recyclerView!!.adapter = schedulesAdapter
    }

    @SuppressLint("NewApi")
    @Subscribe
    fun getSchedulesResult(data: GetLuftSchedules) {
        schedulesList.clear()
        val schedules = data.data!!.ScheduleResource!!.Schedules
        Collections.sort(schedules)
        schedulesList.addAll(schedules!!)
        if (schedulesList.size == 0) {
            showMessageDialog(this, getString(R.string.empty_schedule), null)
            btBackClick()
        } else schedulesAdapter!!.notifyDataSetChanged()
    }

    @OnClick(R.id.btBack)
    fun btBackClick() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        gotoNextActivityAnimation(this@SchedulesActivity)
    }

    fun registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    fun unRegisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }
}