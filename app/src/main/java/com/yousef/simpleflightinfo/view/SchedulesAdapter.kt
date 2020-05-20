package com.yousef.simpleflightinfo.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.yousef.simpleflightinfo.R
import com.yousef.simpleflightinfo.model.LuftSchedulesPOJO.Schedule
import com.yousef.simpleflightinfo.utils.Utils.gotoNextActivityAnimation

class SchedulesAdapter(private val mContext: Context, private val list: List<Schedule>) : RecyclerView.Adapter<SchedulesAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_schedule, parent, false)
        return Holder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]
        holder.tvDepartureDate!!.text = item.Flight!!.Departure!!.ScheduledTimeLocal!!.DateTime!!.replace("T", "  ")
        holder.tvArrivalDate!!.text = item.Flight!!.Arrival!!.ScheduledTimeLocal!!.DateTime!!.replace("T", "  ")
        holder.tvFlightNumber!!.text = "FlightNumber: " + item.Flight!!.MarketingCarrier!!.FlightNumber
        holder.tvViewMap!!.setOnClickListener { v: View? ->
            mContext.startActivity(Intent(mContext, MapActivity::class.java))
            gotoNextActivityAnimation(mContext)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        @BindView(R.id.tvFlightNumber)
        @JvmField var tvFlightNumber: TextView? = null

        @BindView(R.id.tvDepartureDate)
        @JvmField var tvDepartureDate: TextView? = null

        @BindView(R.id.tvArrivalDate)
        @JvmField var tvArrivalDate: TextView? = null

        @BindView(R.id.tvViewMap)
        @JvmField var tvViewMap: TextView? = null

        @BindView(R.id.requestCard)
        @JvmField var requestCard: CardView? = null

        init {
            ButterKnife.bind(this, itemView!!)
        }
    }

}