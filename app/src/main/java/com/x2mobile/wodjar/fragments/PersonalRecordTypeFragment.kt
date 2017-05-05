package com.x2mobile.wodjar.fragments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.LoginActivity
import com.x2mobile.wodjar.activity.PersonalRecordActivity
import com.x2mobile.wodjar.activity.PersonalRecordListActivity
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.model.PersonalRecord
import com.x2mobile.wodjar.data.model.PersonalRecordCategory
import com.x2mobile.wodjar.data.model.PersonalRecordType
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.ui.adapter.PersonalRecordTypeAdapter
import com.x2mobile.wodjar.ui.callback.PersonalRecordTypeListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class PersonalRecordTypeFragment : BaseFragment(), PersonalRecordTypeListener {

    val adapter by lazy { PersonalRecordTypeAdapter(context, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        EventBus.getDefault().register(this)

        if (Preference.isLoggedIn(context)) {
            Service.getPersonalRecordTypes()
        } else {
            val personalRecordTypeNames = resources.getStringArray(R.array.personal_record_type_names)
            val personalRecordTypeCategories = resources.getIntArray(R.array.personal_record_type_categories)
            assert(personalRecordTypeNames.size == personalRecordTypeCategories.size)

            val personalRecordTypes = ArrayList<PersonalRecordType>()

            personalRecordTypeNames.forEachIndexed { index, name ->
                personalRecordTypes.add(PersonalRecordType(name,
                        PersonalRecordCategory.values()[personalRecordTypeCategories[index]]))
            }

            adapter.setItems(personalRecordTypes)
        }
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

        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
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
                    startActivity(context.intentFor<PersonalRecordActivity>())
                } else {
                    showLoginAlert()
                }
                return true
            }
            R.id.search_menu -> return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRecordTypeClicked(personalRecordType: PersonalRecordType) {
        if (Preference.isLoggedIn(context)) {
            if (personalRecordType.present) {
                startActivity(context.intentFor<PersonalRecordListActivity>(NavigationConstants.KEY_PERSONAL_RECORD_TYPE to personalRecordType))
            } else {
                startActivity(context.intentFor<PersonalRecordActivity>(NavigationConstants.KEY_PERSONAL_RECORD to PersonalRecord(personalRecordType)))
            }
        } else {
            showLoginAlert()
        }
    }

    override fun onRecordTypeRemoved(position: Int) {
        val personalRecordType = adapter.getItem(position)
        adapter.removeItem(position)
        Service.deletePersonalRecordType(personalRecordType.name!!)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoggedIn(event: LoggedInEvent) {
        Service.getPersonalRecordTypes()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onRecordTypesResponse(requestResponseEvent: PersonalRecordTypesRequestEvent) {
        if (requestResponseEvent.response != null && requestResponseEvent.response.isSuccessful &&
                requestResponseEvent.response.body() != null) {
            adapter.setItems(requestResponseEvent.response.body().personalRecordTypes!!.sortedBy(PersonalRecordType::updated)
                    .asReversed().toMutableList())
        } else {
            context.toast(R.string.error_occurred)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRecordTypesFailure(requestFailureEvent: PersonalRecordTypesRequestFailureEvent) {
        handleRequestFailure(requestFailureEvent.throwable)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordAdded(event: AddPersonalRecordRequestEvent) {
        Service.getPersonalRecordTypes()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordUpdated(event: UpdatePersonalRecordRequestEvent) {
        Service.getPersonalRecordTypes()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordDeleted(event: DeletePersonalRecordRequestEvent) {
        Service.getPersonalRecordTypes()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordTypeUpdated(event: UpdatePersonalRecordTypeRequestEvent) {
        Service.getPersonalRecordTypes()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordTypeDeleted(event: DeletePersonalRecordTypeRequestEvent) {
        Service.getPersonalRecordTypes()
    }

    private fun showLoginAlert() {
        context.alert(R.string.login_to_continue) {
            positiveButton(getString(R.string.login)) { startActivity(context.intentFor<LoginActivity>()) }
            cancelButton { }
        }.show()
    }

    class DeleteTouchHelperCallback(context: Context, val callback: PersonalRecordTypeListener) : ItemTouchHelper.Callback() {

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
            callback.onRecordTypeRemoved(viewHolder.adapterPosition)
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