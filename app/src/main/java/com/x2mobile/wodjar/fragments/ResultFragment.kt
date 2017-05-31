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
import com.bumptech.glide.Glide
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.activity.ImageViewer
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.Preference
import com.x2mobile.wodjar.business.network.AmazonService
import com.x2mobile.wodjar.data.event.TimeSetEvent
import com.x2mobile.wodjar.data.model.Result
import com.x2mobile.wodjar.databinding.ResultBinding
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.fragments.dialog.TimePickerDialog
import com.x2mobile.wodjar.ui.binding.model.ResultViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.*
import java.util.*


abstract class ResultFragment<T : Result> : BaseFragment(), DatePickerDialog.OnDateSetListener {

    val REQUEST_CODE_PICK_IMAGE = 13

    val REQUEST_CODE_STORAGE = 97

    val DIALOG_DATE_PICKER = "date_picker"

    var viewModel: ResultViewModel? = null

    var binding: ResultBinding? = null

    var result: T? = null

    var progress: ProgressDialog? = null

    val windowRect: Rect by lazy {
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        rect
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        result = if (arguments?.containsKey(NavigationConstants.KEY_RESULT) ?: false) arguments[NavigationConstants.KEY_RESULT]
                as T else createResult()

        viewModel = ResultViewModel(context, result!!)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ResultBinding>(inflater, R.layout.result, container, false)
        binding!!.viewModel = viewModel
        return binding!!.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(context).load(result!!.imageUri).override(windowRect.width(), windowRect.height()).into(binding!!.image)

        binding!!.timeSpent.onClick {
            TimePickerDialog.newInstance(result!!.resultTime).show(fragmentManager, null)
        }

        binding!!.timeSpent.onFocusChange { _, focus ->
            if (focus) {
                TimePickerDialog.newInstance(result!!.resultTime).show(fragmentManager, null)
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
            result!!.imageUri = null
            viewModel!!.notifyImageChange()
        }

        binding!!.image.onClick {
            val imageContainer = binding!!.imageContainer

            val intent = context.intentFor<ImageViewer>()
            intent.putExtra(ImageViewer.KEY_URI, result!!.imageUri)
            intent.putExtra(ImageViewer.KEY_RECT, Rect(imageContainer.left, imageContainer.top, imageContainer.right, imageContainer.bottom))
            startActivity(intent)
        }

        binding!!.date.onClick {
            val calendar = Calendar.getInstance()
            calendar.time = result!!.date
            DatePickerDialog.newInstance(this@ResultFragment, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show(fragmentManager, DIALOG_DATE_PICKER)
        }

        binding!!.deleteRecord.onClick {
            deleteResult(result!!)
            activity.setResult(NavigationConstants.RESULT_DELETED, context.intentFor<Any>(NavigationConstants.KEY_RESULT to result))
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
                        if (result!!.imageUri != null) {
                            val fileName = Constants.IMAGE_NAME.format(Preference.getUserId(context), System.currentTimeMillis())
                            val response = AmazonService.upload(fileName, result!!.imageUri!!)
                            if (response != null) {
                                result!!.imageUri = Uri.parse(Constants.BUCKET_IMAGE_URL.format(fileName))
                            } else {
                                uiThread {
                                    context.toast(R.string.image_upload_failed)
                                }
                            }

                        }

                        saveResult(result!!)
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(NavigationConstants.KEY_RESULT, result!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && intent != null) {
            result!!.imageUri = intent.data
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
        result!!.date = calendar.time
        viewModel!!.notifyDateChange()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTimeSet(event: TimeSetEvent) {
        result!!.resultTime = event.time
        viewModel!!.notifyTimeResultChange()
    }

    protected abstract fun createResult(): T

    protected abstract fun saveResult(result: T)

    protected abstract fun deleteResult(result: T)

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