package com.x2mobile.wodjar.business.callback

interface ToolbarDelegate {
    var title: String
    fun showIndeterminateLoading(show: Boolean)
    fun enableTitleChange()
    fun startTitleChange()
}
