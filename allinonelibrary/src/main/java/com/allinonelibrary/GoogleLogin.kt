package com.allinonelibrary

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.allinonelibrary.utills.Constant
import com.allinonelibrary.model.UserData
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener


class GoogleLogin {
    private var mGoogleApiClient: GoogleApiClient? = null
    private var context: Context? = null
    private var googleSignInCode: Int = 0

    fun initializeGoogleLogin(context: Context, googleSignInCode: Int) {
        this.context = context
        this.googleSignInCode = googleSignInCode

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(context)
            .enableAutoManage(
                context as AppCompatActivity, OnConnectionFailedListener {
                    context.finish()
                })
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    fun googleLogin() {
        if (mGoogleApiClient?.hasConnectedApi(Auth.GOOGLE_SIGN_IN_API)!!) {
            mGoogleApiClient!!.clearDefaultAccountAndReconnect()
        }
        var signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        (context as AppCompatActivity).startActivityForResult(signInIntent, googleSignInCode)
    }

    fun handleSignInResult(data: Intent?) {
        var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

        if (result.isSuccess) {
            val acct = result.signInAccount
            val personName = acct!!.displayName
            val email = acct.email
            val id = acct.id

            var userData = UserData()
            userData.name = personName
            userData.email = email
            userData.googleId = id

            val intent = Intent(Constant.SOCIAL_LOGIN)
            intent.putExtra(Constant.LOGIN_TYPE, Constant.LoginType.Facebook.name)
            intent.putExtra(Constant.USER_DATA, userData)
            context?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(intent) }
        }
    }
}