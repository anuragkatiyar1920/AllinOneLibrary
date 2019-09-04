package com.allinonelibrary.model

import java.io.Serializable

class UserData : Serializable {
    var name: String? = null
    var email: String? = null
    var profilePic: String? = null
    var facebookId: String? = null
    var googleId: String? = null
    var accessToken: String? = null
    var gender: String? = null
}
