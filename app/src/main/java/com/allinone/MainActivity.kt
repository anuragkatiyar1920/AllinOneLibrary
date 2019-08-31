package com.allinone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.allinonelibrary.ToastMessage
import android.util.Log
import com.allinonelibrary.FacebookLogin
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.BroadcastReceiver
import android.content.Context
import com.allinonelibrary.model.UserData

class MainActivity : AppCompatActivity() {
    private var facebook: FacebookLogin? = null
    private var toastMessage: ToastMessage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toastMessage = ToastMessage()

        facebook = FacebookLogin()
        facebook!!.facebookLogin(this)
        facebook!!.printKeyHash(this)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mMessageReceiver, IntentFilter("FacebookLogin"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebook!!.activityResult(requestCode, resultCode, data)
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val userData = intent.getSerializableExtra("USER_DATA") as? UserData
            toastMessage!!.showToast(application, userData?.name.toString())
            //Log.d("receiver", "Got message: " + message!!)
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onDestroy()
    }
}


