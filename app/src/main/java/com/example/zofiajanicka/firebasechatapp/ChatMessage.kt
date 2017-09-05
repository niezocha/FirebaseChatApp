package com.example.zofiajanicka.firebasechatapp

class ChatMessage {

    var sender: String = "sender"
    var receiver: String = "receiver"
    var text: String = "trolollo"
    var date: String = "today"

    constructor()
    constructor(sender: String, receiver: String, text: String, date: String) {
        this.sender = sender
        this.receiver = receiver
        this.text = text
        this.date = date
    }
}