package com.x2mobile.wodjar.fragments

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.RadioButton
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.bumptech.glide.Glide
import com.x2mobile.wodjar.business.Preference
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.ImageViewer
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.business.FileUtil
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.network.Service
import com.x2mobile.wodjar.data.event.*
import com.x2mobile.wodjar.data.model.PersonalRecord
import com.x2mobile.wodjar.databinding.PersonalRecordBinding
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.fragments.dialog.TimePickerDialog
import com.x2mobile.wodjar.ui.binding.model.PersonalRecordViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.*
import java.util.*


class PersonalRecordFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {

    val REQUEST_CODE_PICK_IMAGE = 13

    val REQUEST_CODE_STORAGE = 97

    val DIALOG_DATE_PICKER = "date_picker"

    val amazonS3: AmazonS3Client by lazy {
        AmazonS3Client(CognitoCachingCredentialsProvider(context.applicationContext,
                Constants.IDENTIFY_POLL_ID, Regions.EU_WEST_2))
    }

    var viewModel: PersonalRecordViewModel? = null

    var binding: PersonalRecordBinding? = null

    var personalRecord: PersonalRecord? = null

    var progress: ProgressDialog? = null

    val windowRect: Rect by lazy {
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        rect
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        personalRecord = (savedInstanceState?.get(NavigationConstants.KEY_PERSONAL_RECORD) ?: (arguments?.get(NavigationConstants.KEY_PERSONAL_RECORD)
                ?: PersonalRecord())) as PersonalRecord
        if (personalRecord!!.name == null) {
            personalRecord!!.name = getString(R.string.personal_records)
        }

        viewModel = PersonalRecordViewModel(context, personalRecord!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbarDelegate.title = personalRecord!!.name!!
        if (personalRecord!!.id == Constants.ID_NA) {
            toolbarDelegate.enableTitleChange()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<PersonalRecordBinding>(inflater, R.layout.personal_record, container, false)
        binding!!.viewModel = viewModel
        return binding!!.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(context).load(personalRecord!!.imageUri).override(windowRect.width(), windowRect.height()).into(binding!!.image)

        binding!!.timeSpent.onClick {
            TimePickerDialog.newInstance(personalRecord!!.resultTime).show(fragmentManager, null)
        }

        binding!!.timeSpent.onFocusChange { _, focus ->
            if (focus) {
                TimePickerDialog.newInstance(personalRecord!!.resultTime).show(fragmentManager, null)
            }
        }

        binding!!.addImage.onClick {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                addPicture()
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE)
            }
        }

        binding!!.removeImage.onClick {
            personalRecord!!.imageUri = null
            viewModel!!.notifyImageChange()
        }

        binding!!.image.onClick {
            val imageContainer = binding!!.imageContainer

            val intent = context.intentFor<ImageViewer>()
            intent.putExtra(ImageViewer.KEY_URI, personalRecord!!.imageUri)
            intent.putExtra(ImageViewer.KEY_RECT, Rect(imageContainer.left, imageContainer.top, imageContainer.right, imageContainer.bottom))
            startActivity(intent)
        }

        binding!!.date.onClick {
            val calendar = Calendar.getInstance()
            calendar.time = personalRecord!!.date
            DatePickerDialog.newInstance(this@PersonalRecordFragment, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show(fragmentManager, DIALOG_DATE_PICKER)
        }

        binding!!.deleteRecord.onClick {
            Service.deletePersonalRecord(personalRecord!!.id)
            activity.setResult(NavigationConstants.RESULT_DELETED, context.intentFor<Any>(NavigationConstants.KEY_PERSONAL_RECORD to personalRecord))
            activity.finish()
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
        inflater.inflate(R.menu.menu_done, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done_menu -> {
                if (isInputValid()) {
                    progress = context.indeterminateProgressDialog(R.string.saving)
                    doAsync {
                        if (personalRecord!!.imageUri != null) {
                            val data = FileUtil.prepareForUpload(context, personalRecord!!.imageUri!!)
                            if (data != null && data.second > 0) {
                                val metadata = ObjectMetadata()
                                metadata.contentLength = data.second
                                val fileName = Constants.IMAGE_NAME.format(Preference.getUserId(context), System.currentTimeMillis())

                                val response = amazonS3.putObject(PutObjectRequest(Constants.BUCKET, fileName, data.first, metadata))
                                if (response != null) {
                                    personalRecord!!.imageUri = Uri.parse(Constants.BUCKET_IMAGE_URL.format(fileName))
                                } else {
                                    uiThread {
                                        context.toast(R.string.image_upload_failed)
                                    }
                                }
                            }
                        }

                        if (personalRecord!!.id == Constants.ID_NA) {
                            Service.savePersonalRecord(personalRecord!!)
                        } else {
                            Service.updatePersonalRecord(personalRecord!!)
                        }
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(NavigationConstants.KEY_PERSONAL_RECORD, personalRecord!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && intent != null) {
            personalRecord!!.imageUri = intent.data
            viewModel!!.notifyImageChange()

            Glide.with(context).load(intent.data).override(windowRect.width(), windowRect.height()).into(binding!!.image)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addPicture()
            } else {
                context.longToast(R.string.storage_permission_denied)
            }
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        personalRecord!!.date = calendar.time
        viewModel!!.notifyDateChange()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTimeSet(event: TimeSetEvent) {
        personalRecord!!.resultTime = event.time
        viewModel!!.notifyTimeResultChange()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTitleChange(event: TitleChangedEvent) {
        personalRecord!!.name = toolbarDelegate.title
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordAdded(requestResponseEvent: AddPersonalRecordRequestEvent) {
        progress?.dismiss()
        if (requestResponseEvent.response != null && requestResponseEvent.response.isSuccessful &&
                requestResponseEvent.response.body() != null) {
            activity.setResult(Activity.RESULT_OK, context.intentFor<Any>(NavigationConstants.KEY_PERSONAL_RECORD to requestResponseEvent.response.body()))
            activity.finish()
        } else {
            context.toast(R.string.error_occurred)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPersonalRecordUpdated(requestResponseEvent: UpdatePersonalRecordRequestEvent) {
        progress?.dismiss()
        activity.setResult(Activity.RESULT_OK, context.intentFor<Any>(NavigationConstants.KEY_PERSONAL_RECORD to personalRecord))
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

    private fun isInputValid(): Boolean {
        if (!isInputValid(binding!!.weight, binding!!.weightLifted)) {
            return false
        }
        if (!isInputValid(binding!!.reps, binding!!.repsCompleted)) {
            return false
        }
        if (!isInputValid(binding!!.time, binding!!.timeSpent)) {
            return false
        }
        return true
    }

    private fun isInputValid(radioButton: RadioButton, editText: EditText): Boolean {
        if (radioButton.isEnabled && radioButton.isChecked && TextUtils.isEmpty(editText.text)) {
            editText.error = getString(R.string.field_mandatory)
            return false
        }
        return true
    }

    private fun addPicture() {
        val pickIntent = Intent(Intent.ACTION_GET_CONTENT)
        pickIntent.addCategory(Intent.CATEGORY_OPENABLE)
        pickIntent.type = "image/*"
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(Intent.createChooser(pickIntent, getString(R.string.add_picture)).putExtra(Intent.EXTRA_INITIAL_INTENTS,
                arrayOf(takePhotoIntent)), REQUEST_CODE_PICK_IMAGE)
    }
}