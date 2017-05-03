package com.x2mobile.wodjar.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.model.PersonalRecordType
import com.x2mobile.wodjar.ui.adapter.base.BaseViewHolder
import com.x2mobile.wodjar.ui.adapter.base.FilterableBaseAdapter
import com.x2mobile.wodjar.ui.callback.PersonalRecordTypeListener
import org.jetbrains.anko.onClick

class PersonalRecordTypeAdapter(val context: Context, val listener: PersonalRecordTypeListener) : FilterableBaseAdapter<PersonalRecordType, PersonalRecordTypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalRecordTypeViewHolder {
        return PersonalRecordTypeViewHolder(LayoutInflater.from(context).inflate(R.layout.record_type_item, parent, false), listener)
    }
}

class PersonalRecordTypeViewHolder(itemView: View, val listener: PersonalRecordTypeListener) : BaseViewHolder<PersonalRecordType>(itemView) {

    val name: TextView by lazy { itemView.findViewById(R.id.name) as TextView }
    val status: View by lazy { itemView.findViewById(R.id.status) }

    override fun bindData(item: PersonalRecordType) {
        itemView.onClick { listener.onRecordTypeClicked(item) }
        name.text = item.name
        status.visibility = if (item.present) View.VISIBLE else View.GONE
    }

}