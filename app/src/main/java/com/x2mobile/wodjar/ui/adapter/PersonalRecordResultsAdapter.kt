package com.x2mobile.wodjar.ui.adapter

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.model.PersonalRecordResult
import com.x2mobile.wodjar.ui.adapter.base.BaseAdapter
import com.x2mobile.wodjar.ui.adapter.base.BaseViewHolder
import com.x2mobile.wodjar.ui.callback.PersonalRecordResultListener
import com.x2mobile.wodjar.ui.helper.UIHelper

class PersonalRecordResultAdapter(val context: Context, val listener: PersonalRecordResultListener) : BaseAdapter<PersonalRecordResult, PersonalRecordResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalRecordResultViewHolder {
        return PersonalRecordResultViewHolder(LayoutInflater.from(context).inflate(R.layout.personal_record_item, parent, false), listener)
    }
}

class PersonalRecordResultViewHolder(itemView: View, val listener: PersonalRecordResultListener) : BaseViewHolder<PersonalRecordResult>(itemView) {

    val info: TextView by lazy { itemView.findViewById(R.id.info) as TextView }
    val date: TextView by lazy { itemView.findViewById(R.id.date) as TextView }
    val rx: View by lazy { itemView.findViewById(R.id.rx) }

    override fun bindData(item: PersonalRecordResult) {
        itemView.setOnClickListener { listener.onPersonalRecordResultClicked(item) }
        val context = itemView.context
        date.text = DateFormat.getMediumDateFormat(context).format(item.date)
        info.text = UIHelper.formatResult(context, item.result, item.type)
        rx.visibility = if (item.rx) View.VISIBLE else View.GONE
    }

}