package com.allinonelibrary

import android.content.Context
import android.widget.Toast

class ToastMessage {

    fun showToast(c: Context, message: String) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show()

    }
}

