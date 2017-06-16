package com.x2mobile.wodjar.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.PersonalRecordActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.PersonalRecordsRequestEvent
import com.x2mobile.wodjar.data.event.PersonalRecordsRequestFailureEvent
import com.x2mobile.wodjar.data.event.TitleSetEvent
import com.x2mobile.wodjar.data.model.PersonalRecord
import com.x2mobile.wodjar.data.model.PersonalRecordType
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.ui.adapter.PersonalRecordAdapter
import com.x2mobile.wodjar.ui.callback.PersonalRecordListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

class PersonalRecordListFragment : BaseFragment(), PersonalRecordListener {

    val REQUEST_CODE_PERSONAL_RECORD = 9

    var personalRecordType: PersonalRecordType? = null

    var personalRecordIds: List<Int>? = null

    val adapter: PersonalRecordAdapter by lazy { PersonalRecordAdapter(context, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        personalRecordType = arguments!!.get(NavigationConstants.KEY_PERSONAL_RECORD_TYPE) as PersonalRecordType

        Service.getPersonalRecords(personalRecordType!!.name!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.enableTitleChange()
        toolbarDelegate.title = personalRecordType!!.name!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.personal_records, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        val add = view.findViewById(R.id.add)
        add.onClick {
            startActivityForResult(context.intentFor<PersonalRecordActivity>(NavigationConstants.KEY_RESULT
                    to PersonalRecord(personalRecordType!!)), REQUEST_CODE_PERSONAL_RECORD)
        }
    }

    override fun onStart() {
        super.onStart()

        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()

        EventBus.getDefault().unregister(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_delete, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_menu -> {
                adapter.clearItems()
                Service.deletePersonalRecords(personalRecordIds!!)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PERSONAL_RECORD) {
            val personalRecord = data?.getParcelableExtra<PersonalRecord>(NavigationConstants.KEY_RESULT)
            when (resultCode) {
                Activity.RESULT_OK -> {
                    var position = adapter.getItemPosition(personalRecord!!)
                    if (position >= 0) {
                        adapter.removeItem(position)
                    } else {
                        position = 0
                    }
                    adapter.addItem(personalRecord, position)
                }
                NavigationConstants.RESULT_DELETED -> adapter.removeItem(personalRecord!!)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPersonalRecordClicked(personalRecord: PersonalRecord) {
        startActivityForResult(context.intentFor<PersonalRecordActivity>(NavigationConstants.KEY_RESULT
                to personalRecord), REQUEST_CODE_PERSONAL_RECORD)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTitleSet(event: TitleSetEvent) {
        personalRecordType!!.name = toolbarDelegate.title
        Service.updatePersonalRecordType(personalRecordIds!!, personalRecordType!!.name!!)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordsResponse(requestResponseEvent: PersonalRecordsRequestEvent) {
        if (requestResponseEvent.response.body() != null) {
            val personalRecords = requestResponseEvent.response.body()!!.personalRecords
            personalRecordIds = personalRecords!!.map { it.id }
            adapter.setItems(personalRecords.sortedBy(PersonalRecord::date).asReversed().toMutableList())
        } else {
            context.toast(R.string.error_occurred)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordsFailure(requestFailureEvent: PersonalRecordsRequestFailureEvent) {
        handleRequestFailure(requestFailureEvent.throwable)
    }

    companion object {
        val KEY_PERSONAL_RECORD_TYPE = "personal_record_type"
    }
}
