package com.x2mobile.wodjar.fragments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.PersonalRecordResultActivity
import com.x2mobile.wodjar.activity.PersonalRecordActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.model.PersonalRecord
import com.x2mobile.wodjar.data.model.ResultType
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.ui.adapter.PersonalRecordsAdapter
import com.x2mobile.wodjar.ui.callback.PersonalRecordListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class PersonalRecordsFragment : BaseFragment(), PersonalRecordListener {

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
        return inflater.inflate(R.layout.personal_record_type, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        ItemTouchHelper(DeleteTouchHelperCallback(context, this)).attachToRecyclerView(recyclerView)
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_personal_records, menu)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_menu -> {
                if (Preference.isLoggedIn(context)) {
                    startActivity(context.intentFor<PersonalRecordResultActivity>())
                } else {
                    showLoginAlert()
                }
                return true
            }
            R.id.search_menu -> return true
        }
        return super.onOptionsItemSelected(item)
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

    override fun onPersonalRecordRemoved(position: Int) {
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
            context.toast(R.string.error_occurred)
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

    class DeleteTouchHelperCallback(context: Context, val callback: PersonalRecordListener) : ItemTouchHelper.Callback() {

        var deleteIcon: Drawable? = null

        val backgroundPaint: Paint = Paint()

        init {
            deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)

            backgroundPaint.style = Paint.Style.FILL
            backgroundPaint.color = Color.RED
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            return ItemTouchHelper.Callback.makeMovementFlags(0, ItemTouchHelper.END)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            callback.onPersonalRecordRemoved(viewHolder.adapterPosition)
        }

        override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                 dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            val itemView = viewHolder.itemView

            canvas.drawRect(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat(), backgroundPaint)

            if (dX >= deleteIcon!!.intrinsicWidth * 1.5f) {
                val left = itemView.left + (dX - deleteIcon!!.intrinsicWidth) / 2
                val bottom = Math.round((itemView.height - deleteIcon!!.intrinsicHeight) / 2f)
                deleteIcon!!.setBounds(Math.round(left), itemView.top + bottom, Math.round(left) + deleteIcon!!.intrinsicWidth, itemView.bottom - bottom)
                deleteIcon!!.draw(canvas)
            }
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }
    }

}