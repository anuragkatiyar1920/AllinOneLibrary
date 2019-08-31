package com.allinonelibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.allinonelibrary.model.UserData
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class FacebookLogin {
    private var callbackManager: CallbackManager? = null

    fun facebookLogin(context: Context) {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(
            (context as Activity),
            Arrays.asList("public_profile", "email")
        )
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("LoginActivity", "Facebook token: " + loginResult.accessToken.token)

                    val accessToken = loginResult.accessToken
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,email,gender,picture.type(large)")

                    GraphRequest(
                        accessToken,
                        "me",
                        parameters,
                        null,

                        GraphRequest.Callback { response: GraphResponse? ->
                            var name = response?.jsonObject?.getString("name")
                            val emailID = response?.jsonObject?.optString("email")
                            val facebookID = response?.jsonObject?.optString("id")
                            val gender = response?.jsonObject?.optString("gender")

                            var profilePicUrl: String? = null
                            if (response?.jsonObject?.has("picture")!!) {
                                profilePicUrl =
                                    response?.jsonObject?.getJSONObject("picture")
                                        ?.getJSONObject("data")
                                        ?.getString("url")
                            }

                            var userData = UserData()
                            userData.name = name
                            userData.facebookId = facebookID
                            userData.email = emailID
                            userData.gender = gender
                            userData.accessToken = accessToken.token
                            userData.profilePic = profilePicUrl

                            Log.e("Response", response.toString())
                            val intent = Intent("FacebookLogin")
                            intent.putExtra("USER_DATA", userData)
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

                        }).executeAsync()
                }

                override fun onCancel() {
                    Log.d("FacebookLogin", "Facebook onCancel.")

                }

                override fun onError(error: FacebookException) {
                    Log.d("FacebookLogin", "Facebook onError.")

                }
            })
    }

    fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    fun printKeyHash(context: Context): String? {
        val packageInfo: PackageInfo
        var key: String? = null
        try {
            val packageName = context.packageName
            packageInfo =
                context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            Log.e("Package Name=", packageName)
            for (signature in packageInfo.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                key = String(Base64.encode(md.digest(), 0))
                Log.e("Key Hash=", key)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("Name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("No such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }
        return key
    }
}
