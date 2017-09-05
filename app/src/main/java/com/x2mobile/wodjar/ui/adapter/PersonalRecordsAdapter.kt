package com.x2mobile.wodjar.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.model.PersonalRecord
import com.x2mobile.wodjar.ui.adapter.base.BaseViewHolder
import com.x2mobile.wodjar.ui.adapter.base.FilterableBaseAdapter
import com.x2mobile.wodjar.ui.callback.PersonalRecordListener
import com.x2mobile.wodjar.ui.helper.UIHelper

class PersonalRecordsAdapter(val context: Context, val listener: PersonalRecordListener) : FilterableBaseAdapter<PersonalRecord, PersonalRecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalRecordViewHolder = PersonalRecordViewHolder(LayoutInflater.from(context).inflate(R.layout.record_type_item, parent, false), listener)
}

class PersonalRecordViewHolder(itemView: View, val listener: PersonalRecordListener) : BaseViewHolder<PersonalRecord>(itemView) {

    val name: TextView by lazy { itemView.findViewById<TextView>(R.id.name) }
    val result: TextView by lazy { itemView.findViewById<TextView>(R.id.best_result) }

    override fun bindData(item: PersonalRecord) {
        val context = itemView.context
        itemView.setOnClickListener { listener.onPersonalRecordClicked(item) }
        name.text = item.name
        result.text = UIHelper.formatResult(context, item.bestResult, item.type)
    }

}