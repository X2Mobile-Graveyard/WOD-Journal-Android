package com.x2mobile.wodjar.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.PersonalRecordResultActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.PersonalRecordResultsRequestEvent
import com.x2mobile.wodjar.data.event.PersonalRecordResultsRequestFailureEvent
import com.x2mobile.wodjar.data.event.TitleSetEvent
import com.x2mobile.wodjar.data.model.PersonalRecord
import com.x2mobile.wodjar.data.model.PersonalRecordResult
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.ui.adapter.PersonalRecordResultAdapter
import com.x2mobile.wodjar.ui.callback.PersonalRecordResultListener
import com.x2mobile.wodjar.ui.helper.DeleteListener
import com.x2mobile.wodjar.ui.helper.DeleteTouchHelperCallback
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.toast

class PersonalRecordFragment : BaseFragment(), PersonalRecordResultListener, DeleteListener {

    private val REQUEST_CODE_PERSONAL_RECORD = 9

    private val personalRecord: PersonalRecord by lazy { arguments!!.get(NavigationConstants.KEY_PERSONAL_RECORD) as PersonalRecord }

    val adapter: PersonalRecordResultAdapter by lazy { PersonalRecordResultAdapter(context, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        Service.getPersonalRecordResults(personalRecord.id)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.enableTitleChange()
        toolbarDelegate.title = personalRecord.name!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.personal_record, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        ItemTouchHelper(DeleteTouchHelperCallback(context, this)).attachToRecyclerView(recyclerView)

        val add = view.findViewById<FloatingActionButton>(R.id.add)
        add.setOnClickListener {
            startActivityForResult(context.intentFor<PersonalRecordResultActivity>(NavigationConstants.KEY_PERSONAL_RECORD to personalRecord),
                    REQUEST_CODE_PERSONAL_RECORD)
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
                confirmDeleteAlert {
                    Service.deletePersonalRecord(personalRecord.id)
                    activity.finish()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PERSONAL_RECORD) {
            val personalRecordResult = data?.getParcelableExtra<PersonalRecordResult>(NavigationConstants.KEY_RESULT)
            when (resultCode) {
                Activity.RESULT_OK -> {
                    var position = adapter.getItemPosition(personalRecordResult!!)
                    if (position >= 0) {
                        adapter.removeItem(position)
                    } else {
                        position = 0
                    }
                    adapter.addItem(personalRecordResult, position)
                }
                NavigationConstants.RESULT_DELETED -> adapter.removeItem(personalRecordResult!!)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPersonalRecordResultClicked(personalRecordResult: PersonalRecordResult) = startActivityForResult(context.intentFor<PersonalRecordResultActivity>(NavigationConstants.KEY_PERSONAL_RECORD to personalRecord,
            NavigationConstants.KEY_RESULT to personalRecordResult), REQUEST_CODE_PERSONAL_RECORD)

    override fun onItemRemoved(position: Int) {
        val result = adapter.getItem(position)
        adapter.removeItem(position)
        Service.deletePersonalRecordResult(result.id)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTitleSet(event: TitleSetEvent) {
        personalRecord.name = toolbarDelegate.title
        Service.updatePersonalRecord(personalRecord)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordResultsResponse(requestResponseEvent: PersonalRecordResultsRequestEvent) = if (requestResponseEvent.response.body() != null) {
        val personalRecords = requestResponseEvent.response.body()!!
        adapter.setItems(personalRecords.sortedBy(PersonalRecordResult::date).asReversed().toMutableList())
    } else {
        toast(R.string.error_occurred)
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordResultsFailure(requestFailureEvent: PersonalRecordResultsRequestFailureEvent) = handleRequestFailure(requestFailureEvent.throwable)

    companion object {
        val KEY_PERSONAL_RECORD_TYPE = "personal_record_type"
    }
}
