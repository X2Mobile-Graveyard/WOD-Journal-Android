package com.x2mobile.wodjar.fragments

import android.app.Activity
import android.os.Bundle
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.model.PersonalRecord
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class PersonalRecordFragment : ResultFragment<PersonalRecord>(), DatePickerDialog.OnDateSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (result.name == null) {
            result.name = getString(R.string.personal_records)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbarDelegate.title = result.name!!
        if (result.id == Constants.ID_NA) {
            toolbarDelegate.enableTitleChange()
        }
    }

    override fun createResult(): PersonalRecord {
        return PersonalRecord()
    }

    override fun saveResult(result: PersonalRecord) {
        if (result.id == Constants.ID_NA) {
            Service.savePersonalRecord(result)
        } else {
            Service.updatePersonalRecord(result)
        }
    }

    override fun deleteResult(result: PersonalRecord) {
        Service.deletePersonalRecord(result.id)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTitleChange(event: TitleChangedEvent) {
        result.name = toolbarDelegate.title
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordAdded(requestResponseEvent: AddPersonalRecordRequestEvent) {
        progress?.dismiss()
        if (requestResponseEvent.response != null && requestResponseEvent.response.isSuccessful &&
                requestResponseEvent.response.body() != null) {
            activity.setResult(Activity.RESULT_OK, context.intentFor<Any>(NavigationConstants.KEY_RESULT to requestResponseEvent.response.body()))
            activity.finish()
        } else {
            context.toast(R.string.error_occurred)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordUpdated(requestResponseEvent: UpdatePersonalRecordRequestEvent) {
        progress?.dismiss()
        activity.setResult(Activity.RESULT_OK, context.intentFor<Any>(NavigationConstants.KEY_RESULT to result))
        activity.finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordDeleted(requestResponseEvent: DeletePersonalRecordRequestEvent) {
        progress?.dismiss()
        activity.finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordAddFailed(requestFailureEvent: AddPersonalRecordRequestFailureEvent) {
        progress?.dismiss()
        handleRequestFailure(requestFailureEvent.throwable)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordUpdateFailed(requestFailureEvent: UpdatePersonalRecordRequestFailureEvent) {
        progress?.dismiss()
        handleRequestFailure(requestFailureEvent.throwable)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordDeleteFailed(requestFailureEvent: DeletePersonalRecordRequestFailureEvent) {
        progress?.dismiss()
        handleRequestFailure(requestFailureEvent.throwable)
    }
}