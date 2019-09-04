package com.allinone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.allinonelibrary.ToastMessage
import com.allinonelibrary.FacebookLogin
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Button
import com.allinonelibrary.GoogleLogin
import com.allinonelibrary.utills.Constant
import com.allinonelibrary.model.UserData

class MainActivity : AppCompatActivity() {
    private var facebook: FacebookLogin? = null
    private var googleLogin: GoogleLogin? = null
    private var toastMessage: ToastMessage? = null
    private var googleSignInCode = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toastMessage = ToastMessage()
        googleLogin = GoogleLogin()
        facebook = FacebookLogin()
        googleLogin!!.initializeGoogleLogin(this, googleSignInCode)

        val btFacebookLogin = findViewById<Button>(R.id.btFacebookLogin)
        btFacebookLogin.setOnClickListener {
            facebook!!.facebookLogin(this)
            facebook!!.printKeyHash(this)
        }

        val btGoogleLogin = findViewById<Button>(R.id.btGoogleLogin)
        btGoogleLogin.setOnClickListener {
            googleLogin!!.googleLogin()
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mMessageReceiver, IntentFilter(Constant.SOCIAL_LOGIN))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == googleSignInCode) {
            googleLogin!!.handleSignInResult(data)
        } else {
            facebook!!.activityResult(requestCode, resultCode, data)
        }
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val loginType = intent.getStringExtra(Constant.LOGIN_TYPE).toString()
            val userData = intent.getSerializableExtra(Constant.USER_DATA) as? UserData

            if (loginType.equals(Constant.LoginType.Facebook.name)) {
                //TODO("Do Work For Facebook")
            } else if (loginType.equals(Constant.LoginType.Google.name)) {
                //TODO("Do Work For Google")
            }
            toastMessage!!.showToast(application, userData?.name.toString())
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onDestroy()
    }
}


