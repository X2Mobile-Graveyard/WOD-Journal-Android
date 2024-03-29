package com.x2mobile.wodjar.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.RadioButton
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.NavigationConstants
import com.x2mobile.wodjar.business.network.AmazonService
import com.x2mobile.wodjar.data.event.ImageSetEvent
import com.x2mobile.wodjar.data.event.TimeSetEvent
import com.x2mobile.wodjar.data.model.Result
import com.x2mobile.wodjar.databinding.ResultBinding
import com.x2mobile.wodjar.fragments.base.BaseFragment
import com.x2mobile.wodjar.fragments.dialog.TimePickerDialog
import com.x2mobile.wodjar.ui.binding.model.ResultViewModel
import com.x2mobile.wodjar.ui.helper.ImagePicker
import com.x2mobile.wodjar.ui.helper.ImageViewer
import com.x2mobile.wodjar.ui.helper.ShareHelper
import com.x2mobile.wodjar.ui.helper.UIHelper
import com.x2mobile.wodjar.util.isUrl
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import java.util.*


abstract class ResultFragment<T : Result> : BaseFragment(), DatePickerDialog.OnDateSetListener {

    private val DIALOG_DATE_PICKER = "date_picker"

    @Suppress("UNCHECKED_CAST")
    val result: T by lazy {
        savedArguments?.get(NavigationConstants.KEY_RESULT) as T? ?: arguments?.get(NavigationConstants.KEY_RESULT) as T? ?: createResult()
    }

    lateinit var binding: ResultBinding

    val viewModel: ResultViewModel by lazy { ResultViewModel(context, result) }

    private val imagePicker: ImagePicker by lazy { ImagePicker(this, savedArguments) }

    private val imageViewer: ImageViewer by lazy { ImageViewer(this, binding.image) }

    private val shareHelper: ShareHelper by lazy { ShareHelper(this) }

    var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.result, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewer.imageUri = result.imageUri

        binding.timeSpent.setOnClickListener {
            TimePickerDialog.newInstance(result.resultTime).show(fragmentManager, null)
        }

        binding.timeSpent.setOnFocusChangeListener { _, focus ->
            if (focus) {
                TimePickerDialog.newInstance(result.resultTime).show(fragmentManager, null)
            }
        }

        binding.addImage.setOnClickListener {
            imagePicker.addPicture(this)
        }

        binding.removeImage.setOnClickListener {
            result.imageUri = null
            viewModel.notifyImageChange()
        }

        binding.image.setOnClickListener {
            imageViewer.popup(binding.imageContainer)
        }

        binding.date.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = result.date
            DatePickerDialog.newInstance(this@ResultFragment, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show(fragmentManager, DIALOG_DATE_PICKER)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_result, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.delete_menu).isVisible = result.id >= 0
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.done_menu -> {
                if (isInputValid()) {
                    progress = context.indeterminateProgressDialog(R.string.saving)
                    doAsync {
                        if (result.imageUri != null && !result.imageUri!!.isUrl()) {
                            AmazonService.upload(this, result.imageUri!!, { uri ->
                                result.imageUri = uri
                                saveResult(result)
                            })
                        } else {
                            saveResult(result)
                        }
                    }
                }
                return true
            }

            R.id.share_menu -> {
                Glide.with(this).asBitmap().load(result.imageUri).into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                        shareHelper.share(prepareShareText(result), resource)
                    }

                })
            }

            R.id.delete_menu -> {
                confirmDeleteAlert {
                    deleteResult(result)
                    activity.setResult(NavigationConstants.RESULT_DELETED, context.intentFor<Any>(NavigationConstants.KEY_RESULT to result))
                    activity.finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        imagePicker.onSaveInstance(outState)
        outState?.putParcelable(NavigationConstants.KEY_RESULT, result)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        imagePicker.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onImageSet(event: ImageSetEvent) {
        result.imageUri = event.uri
        imageViewer.imageUri = result.imageUri
        viewModel.notifyImageChange()
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        result.date = calendar.time
        viewModel.notifyDateChange()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTimeSet(event: TimeSetEvent) {
        result.resultTime = event.time
        viewModel.notifyTimeResultChange()
    }

    protected abstract fun createResult(): T

    protected abstract fun saveResult(result: T)

    protected abstract fun deleteResult(result: T)

    protected open fun prepareShareText(result: T): String = UIHelper.formatResult(context, result.result, result.type)!!

    private fun isInputValid(): Boolean {
        if (!isInputValid(binding.weight, binding.weightLifted)) {
            return false
        }
        if (!isInputValid(binding.reps, binding.repsCompleted)) {
            return false
        }
        if (!isInputValid(binding.time, binding.timeSpent)) {
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
}