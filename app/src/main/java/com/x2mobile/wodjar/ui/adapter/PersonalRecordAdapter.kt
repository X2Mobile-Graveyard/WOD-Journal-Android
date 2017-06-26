package com.x2mobile.wodjar.ui.adapter

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.data.model.PersonalRecord
import com.x2mobile.wodjar.data.model.ResultType
import com.x2mobile.wodjar.data.model.UnitType
import com.x2mobile.wodjar.ui.adapter.base.BaseAdapter
import com.x2mobile.wodjar.ui.adapter.base.BaseViewHolder
import com.x2mobile.wodjar.ui.callback.PersonalRecordListener
import com.x2mobile.wodjar.util.MathUtil
import com.x2mobile.wodjar.util.TimeUtil

class PersonalRecordAdapter(val context: Context, val listener: PersonalRecordListener) : BaseAdapter<PersonalRecord, PersonalRecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalRecordViewHolder {
        return PersonalRecordViewHolder(LayoutInflater.from(context).inflate(R.layout.personal_record_item, parent, false), listener)
    }
}

class PersonalRecordViewHolder(itemView: View, val listener: PersonalRecordListener) : BaseViewHolder<PersonalRecord>(itemView) {

    val info: TextView by lazy { itemView.findViewById(R.id.info) as TextView }
    val date: TextView by lazy { itemView.findViewById(R.id.date) as TextView }
    val rx: View by lazy { itemView.findViewById(R.id.rx) }

    override fun bindData(item: PersonalRecord) {
        itemView.setOnClickListener { listener.onPersonalRecordClicked(item) }
        val context = itemView.context
        date.text = DateFormat.getMediumDateFormat(context).format(item.date)
        when (item.type) {
            ResultType.WEIGHT -> info.text = context.getString(R.string.weight_prefix,
                    context.getString(if (Preference.getUnitType(context) == UnitType.METRIC) R.string.kg_suffix else R.string.lb_suffix,
                            MathUtil.convertWeight(item.resultWeight, UnitType.METRIC, Preference.getUnitType(context))))
            ResultType.REPETITION -> info.text = context.getString(R.string.reps_prefix, item.resultReps)
            ResultType.TIME -> info.text = context.getString(R.string.time_prefix, TimeUtil.formatTime(item.resultTime.toLong()))
            else -> {
                throw NotImplementedError()
            }
        }
        rx.visibility = if (item.rx) View.VISIBLE else View.GONE
    }

}