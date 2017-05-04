package com.x2mobile.wodjar.ui.callback

import com.x2mobile.wodjar.data.model.PersonalRecordType

interface PersonalRecordTypeListener {
    fun onRecordTypeClicked(personalRecordType: PersonalRecordType)
    fun onRecordTypeRemoved(position: Int)
}
