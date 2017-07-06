package com.x2mobile.wodjar.fragments

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.PersonalRecordActivity
import com.x2mobile.wodjar.activity.PersonalRecordResultActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.model.PersonalRecord
import com.x2mobile.wodjar.data.model.ResultType
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.ui.adapter.PersonalRecordsAdapter
import com.x2mobile.wodjar.ui.callback.PersonalRecordListener
import com.x2mobile.wodjar.ui.helper.DeleteListener
import com.x2mobile.wodjar.ui.helper.DeleteTouchHelperCallback
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.toast

class PersonalRecordsFragment : BaseFragment(), PersonalRecordListener, DeleteListener {

    val adapter by lazy { PersonalRecordsAdapter(context, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        EventBus.getDefault().register(this)

        retrievePersonalRecords()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarDelegate.title = getString(R.string.personal_records)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.personal_record, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        ItemTouchHelper(DeleteTouchHelperCallback(context, this)).attachToRecyclerView(recyclerView)

        val add = view.findViewById(R.id.add)
        add.setOnClickListener {
            if (Preference.isLoggedIn(context)) {
                startActivity(context.intentFor<PersonalRecordResultActivity>())
            } else {
                showLoginAlert()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        val searchView = MenuItemCompat.getActionView(menu.findItem(R.id.search_menu)) as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)
                return true
            }
        })
    }

    override fun onPersonalRecordClicked(personalRecord: PersonalRecord) {
        if (Preference.isLoggedIn(context)) {
            if (personalRecord.present) {
                startActivity(context.intentFor<PersonalRecordActivity>(NavigationConstants.KEY_PERSONAL_RECORD to personalRecord))
            } else {
                startActivity(context.intentFor<PersonalRecordResultActivity>(NavigationConstants.KEY_PERSONAL_RECORD to personalRecord))
            }
        } else {
            showLoginAlert()
        }
    }

    override fun onItemRemoved(position: Int) {
        val personalRecord = adapter.getItem(position)
        adapter.removeItem(position)
        Service.deletePersonalRecord(personalRecord.id)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoggedIn(event: LoggedInEvent) {
        retrievePersonalRecords()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoggedOut(event: LoggedOutEvent) {
        retrievePersonalRecords()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onPersonalRecordsResponse(requestResponseEvent: PersonalRecordsRequestEvent) {
        if (requestResponseEvent.response.body() != null) {
            adapter.setItems(requestResponseEvent.response.body()!!.sortedBy(PersonalRecord::name).toMutableList())
        } else {
            toast(R.string.error_occurred)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordsFailure(requestFailureEvent: PersonalRecordsRequestFailureEvent) {
        if (requestFailureEvent.default) {
            val personalRecordNames = resources.getStringArray(R.array.personal_record_type_names)
            val resultTypes = resources.getIntArray(R.array.personal_record_result_types)
            assert(personalRecordNames.size == resultTypes.size)

            val personalRecords = ArrayList<PersonalRecord>()

            personalRecordNames.forEachIndexed { index, name ->
                personalRecords.add(PersonalRecord(name, ResultType.values()[resultTypes[index]]))
            }

            adapter.setItems(personalRecords)
        } else {
            handleRequestFailure(requestFailureEvent.throwable)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordUpdated(event: UpdatePersonalRecordRequestEvent) {
        Service.getPersonalRecords()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordDeleted(event: DeletePersonalRecordRequestEvent) {
        Service.getPersonalRecords()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordResultAdded(event: AddPersonalRecordResultRequestEvent) {
        Service.getPersonalRecords()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordResultUpdated(event: UpdatePersonalRecordResultRequestEvent) {
        Service.getPersonalRecords()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordResultDeleted(event: DeletePersonalRecordResultRequestEvent) {
        Service.getPersonalRecords()
    }

    private fun retrievePersonalRecords() {
        if (Preference.isLoggedIn(context)) {
            Service.getPersonalRecords()
        } else {
            Service.getDefaultPersonalRecords()
        }
    }

}