# AllinOneLibrary
A Sample library to use facebook and Google login in Kotlin. Add dependencies in app level build.gradle file

        implementation 'com.github.anuragkatiyar1920:AllinOneLibrary:0.1.1'
        
Add maven in project level build.gradle file:

       allprojects {
            repositories {
                maven { url 'https://jitpack.io' }
            }
       }
# For Facebook login follow the below steps:
1) First create the App in https://developers.facebook.com/
1) Intialze facebook object in onCreate() method:

        facebook = FacebookLogin()
2) Now login facebook from clickListener:

        facebook!!.facebookLogin(this) 
3) Now in onActivityResult() add below code:

        facebook!!.activityResult(requestCode, resultCode, data)  
4) Get keyhash from below method:

        facebook!!.printKeyHash(this)         
5) Now Register Local BraodcastReciever in onCreate() method:

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, IntentFilter(Constant.SOCIAL_LOGIN))
        
        private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val loginType = intent.getStringExtra(Constant.LOGIN_TYPE).toString()
            val userData = intent.getSerializableExtra(Constant.USER_DATA) as? UserData

            if (loginType.equals(Constant.LoginType.Facebook.name)) {
                //TODO("Do Work For Facebook")
            }
            toastMessage!!.showToast(application, userData?.name.toString())
        }
    }
6)  Destroy Local Broadcast Reciever in onDestroy() method:

          LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
7) Add Meta-Data in menifest file:
    
          <meta-data
                android:name="com.facebook.sdk.ApplicationId"
                android:value="@string/facebook_app_id" />
        
