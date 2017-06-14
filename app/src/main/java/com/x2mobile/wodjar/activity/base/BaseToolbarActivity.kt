package com.x2mobile.wodjar.activity.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.x2mobile.wodjar.R
import com.x2mobile.wodjar.business.callback.ToolbarDelegate
import com.x2mobile.wodjar.data.event.TitleSetEvent
import com.x2mobile.wodjar.databinding.RootBinding
import com.x2mobile.wodjar.ui.binding.model.RootViewModel
import org.greenrobot.eventbus.EventBus

open class BaseToolbarActivity : AppCompatActivity(), ToolbarDelegate {

    private var rootViewModel: RootViewModel? = null

    private var rootLayoutBinding: RootBinding? = null

    override var title: String
        get() = rootViewModel!!.title
        set(value) {
            rootViewModel!!.title = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootViewModel = RootViewModel(getString(R.string.app_name),
                View.OnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        finishTitleChange()
                    }
                },
                TextView.OnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        finishTitleChange()
                        return@OnEditorActionListener true
                    }
                    false
                })
        rootViewModel!!.title = getString(R.string.app_name)

        rootLayoutBinding = DataBindingUtil.inflate<RootBinding>(layoutInflater, R.layout.root, null, false)
        rootLayoutBinding!!.viewModel = rootViewModel

        setSupportActionBar(rootLayoutBinding!!.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, rootLayoutBinding!!.root as ViewGroup, false))
    }

    override fun setContentView(view: View) {
        val rootView = rootLayoutBinding!!.root as ViewGroup
        rootView.addView(view)
        super.setContentView(rootView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showIndeterminateLoading(show: Boolean) {
        rootLayoutBinding!!.indeterminateProgressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun enableTitleChange() {
        rootViewModel!!.titleChangeEnabled = true
    }

    override fun startTitleChange() {
        rootViewModel!!.titleChangeStarted = true
    }

    private fun finishTitleChange() {
        if (rootViewModel!!.titleChangeStarted) {
            rootViewModel!!.titleChangeStarted = false
            EventBus.getDefault().post(TitleSetEvent())
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(rootLayoutBinding!!.root.windowToken, 0)
        }
    }

}
