# AllinOneLibrary
A Sample library to use facebook and Google login in Kotlin. Add dependencies in app level build.gradle file

        implementation 'com.github.anuragkatiyar1920:AllinOneLibrary:0.1.1'
        
Add maven in project level build.gradle file:

       allprojects {
            repositories {
                maven { url 'https://jitpack.io' }
            }
       }
# For Facebook/Google login follow the below steps:
1) For Facebook create the App in https://developers.facebook.com/ and for Google create app in https://console.firebase.google.com/ and download google-services.json and paste inside app folder

1) Initialize global variable:

           private var facebook: FacebookLogin? = null
           private var googleLogin: GoogleLogin? = null
           private var googleSignInCode = 7
2) Intialze facebook/Google object in onCreate() method:

        facebook = FacebookLogin()
        googleLogin = GoogleLogin()
        googleLogin!!.initializeGoogleLogin(this, googleSignInCode)
3) Now call facebookLogin() method from clickListener:

        facebook!!.facebookLogin(this)
  Call googleLogin() method from clickListener
  
        googleLogin!!.googleLogin()
4) Now in onActivityResult() add below code:

        if (requestCode == googleSignInCode) {
            googleLogin!!.handleSignInResult(data)
        } else {
            facebook!!.activityResult(requestCode, resultCode, data)
        }  
5) Get keyhash from below method:

        facebook!!.printKeyHash(this)         
6) Now Register Local BraodcastReciever from onCreate() method:

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, IntentFilter(Constant.SOCIAL_LOGIN))
        
        private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val loginType = intent.getStringExtra(Constant.LOGIN_TYPE).toString()
            val userData = intent.getSerializableExtra(Constant.USER_DATA) as? UserData

            if (loginType.equals(Constant.LoginType.Facebook.name)) {
                //TODO("Do Work For Facebook")
            } else if (loginType.equals(Constant.LoginType.Google.name)) {
                //TODO("Do Work For Google")
            }
          }
        }
7)  unregisterReceiver Local Broadcast Reciever from onDestroy() method:

          LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
8) Add Meta-Data in menifest file:
    
          <meta-data
                android:name="com.facebook.sdk.ApplicationId"
                android:value="@string/facebook_app_id" />
        
