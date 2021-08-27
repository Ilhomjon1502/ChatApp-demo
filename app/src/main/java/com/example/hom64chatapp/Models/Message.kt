package com.example.hom64chatapp.Models

class Message {
    var fromUid:String? = null
    var toUid:String? = null
    var text:String? = null
    var date:String? = null

    constructor(fromUid: String?, toUid: String?, text: String?, date: String?) {
        this.fromUid = fromUid
        this.toUid = toUid
        this.text = text
        this.date = date
    }

    constructor(fromUid: String?, text: String?, date: String?) {
        this.fromUid = fromUid
        this.text = text
        this.date = date
    }

    constructor()

}