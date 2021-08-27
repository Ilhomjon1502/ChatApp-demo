package com.example.hom64chatapp.Models

import java.io.Serializable

class User :Serializable{
    var uid:String? = null
    var email:String? = null
    var displayName:String? = null
    var phoneNumber:String? = null
    var photoUrl:String? = null
    var isOnline:Boolean? = null
    var lastTime:String? = null



    constructor()
    constructor(
        uid: String?,
        email: String?,
        displayName: String?,
        phoneNumber: String?,
        photoUrl: String?,
        isOnline: Boolean?
    ) {
        this.uid = uid
        this.email = email
        this.displayName = displayName
        this.phoneNumber = phoneNumber
        this.photoUrl = photoUrl
        this.isOnline = isOnline
    }
}