package com.x2mobile.wodjar.fragments.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.NumberPicker
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.data.event.TimeSetEvent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.bundleOf
import java.util.concurrent.TimeUnit

class TimePickerDialog : DialogFragment(), DialogInterface.OnClickListener {

    lateinit var hoursPicker: NumberPicker
    lateinit var minutesPicker: NumberPicker
    lateinit var secondsPicker: NumberPicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_number_picker, null)

        val time = arguments.getLong(KEY_TIME, 0)
        val hours = TimeUnit.SECONDS.toHours(time)
        val minutes = TimeUnit.SECONDS.toMinutes(time - TimeUnit.HOURS.toSeconds(hours))
        val seconds = time - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes)

        hoursPicker = view.findViewById(R.id.hours) as NumberPicker
        hoursPicker.minValue = 0
        hoursPicker.maxValue = 23
        hoursPicker.value = hours.toInt()

        minutesPicker = view.findViewById(R.id.minutes) as NumberPicker
        minutesPicker.minValue = 0
        minutesPicker.maxValue = 59
        minutesPicker.value = minutes.toInt()

        secondsPicker = view.findViewById(R.id.seconds) as NumberPicker
        secondsPicker.minValue = 0
        secondsPicker.maxValue = 59
        secondsPicker.value = seconds.toInt()

        builder.setView(view)
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.set, this)
        builder.setNegativeButton(R.string.cancel, this)
        return builder.create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                val time = TimeUnit.HOURS.toSeconds(hoursPicker.value.toLong()) + TimeUnit.MINUTES.toSeconds(minutesPicker.value.toLong()) +
                        secondsPicker.value.toLong()
                EventBus.getDefault().post(TimeSetEvent(time.toInt()))
            }
        }
    }

    companion object {
        val KEY_TIME = "time"

        fun newInstance(time: Int): TimePickerDialog {
            val dialog = TimePickerDialog()
            dialog.arguments = bundleOf(Pair(KEY_TIME, time.toLong()))
            return dialog
        }
    }
}