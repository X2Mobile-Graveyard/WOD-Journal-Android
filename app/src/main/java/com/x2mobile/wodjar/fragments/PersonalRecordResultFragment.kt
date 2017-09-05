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
import com.x2mobile.wodjar.data.model.PersonalRecordResult
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.toast

class PersonalRecordResultFragment : ResultFragment<PersonalRecordResult>(), DatePickerDialog.OnDateSetListener {

    private val personalRecord: PersonalRecord by lazy {
        savedArguments?.get(NavigationConstants.KEY_PERSONAL_RECORD) as PersonalRecord? ?:
                arguments?.get(NavigationConstants.KEY_PERSONAL_RECORD) as PersonalRecord? ?: PersonalRecord(getString(R.string.personal_record))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbarDelegate.title = personalRecord.name!!
        if (result.id == Constants.ID_NA) {
            toolbarDelegate.enableTitleChange()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(NavigationConstants.KEY_PERSONAL_RECORD, personalRecord)
    }

    override fun createResult(): PersonalRecordResult {
        val personalRecordResult = PersonalRecordResult()
        personalRecordResult.personalRecordId = personalRecord.id
        personalRecordResult.type = personalRecord.type
        return personalRecordResult
    }

    override fun saveResult(result: PersonalRecordResult) = if (result.id == Constants.ID_NA) {
        if (personalRecord.id == Constants.ID_NA) {
            Service.savePersonalRecordResult(personalRecord.name!!, result)
        } else {
            Service.savePersonalRecordResult(result)
        }
    } else {
        Service.updatePersonalRecordResult(result)
    }

    override fun deleteResult(result: PersonalRecordResult) = Service.deletePersonalRecordResult(result.id)

    override fun prepareShareText(result: PersonalRecordResult): String = personalRecord.name + "\n\n" + super.prepareShareText(result)

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTitleSet(event: TitleSetEvent) {
        //This can happen only when new personal record is created
        personalRecord.name = toolbarDelegate.title
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordResultAdded(requestResponseEvent: AddPersonalRecordResultRequestEvent) {
        progress?.dismiss()
        if (requestResponseEvent.response.body() != null) {
            activity.setResult(Activity.RESULT_OK, context.intentFor<Any>(NavigationConstants.KEY_RESULT to requestResponseEvent.response.body()))
            activity.finish()
        } else {
            toast(R.string.error_occurred)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordResultUpdated(requestResponseEvent: UpdatePersonalRecordResultRequestEvent) {
        progress?.dismiss()
        activity.setResult(Activity.RESULT_OK, context.intentFor<Any>(NavigationConstants.KEY_RESULT to result))
        activity.finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordResultDeleted(requestResponseEvent: DeletePersonalRecordResultRequestEvent) {
        progress?.dismiss()
        activity.finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordResultAddFailed(requestFailureEvent: AddPersonalRecordResultRequestFailureEvent) {
        progress?.dismiss()
        handleRequestFailure(requestFailureEvent.throwable)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordResultUpdateFailed(requestFailureEvent: UpdatePersonalRecordResultRequestFailureEvent) {
        progress?.dismiss()
        handleRequestFailure(requestFailureEvent.throwable)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordResultDeleteFailed(requestFailureEvent: DeletePersonalRecordResultRequestFailureEvent) {
        progress?.dismiss()
        handleRequestFailure(requestFailureEvent.throwable)
    }
}