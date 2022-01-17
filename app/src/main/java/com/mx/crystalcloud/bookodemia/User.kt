package com.mx.crystalcloud.bookodemia

class User(
    var name: String = "",
    var email: String = "",
    var password: String = ""
) {
    fun getUserData() {
        println("Name: $name")
        println("Email: $email")
        println("Password: $password")
    }
}