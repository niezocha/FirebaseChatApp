package com.example.zofiajanicka.firebasechatapp

class ChatMessage {

    var text: String = "lol lol"
    var user: String = "meh"
    var date: String = "blah"

    constructor()
    constructor(user: String, text: String, date: String) {
        this.text = text
        this.user = user
        this.date = date
    }
}