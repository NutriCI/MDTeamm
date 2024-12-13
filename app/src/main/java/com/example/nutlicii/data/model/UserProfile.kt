package com.example.nutlicii.data.model


data class UserProfile(
    val name :String,
    val email:String,
    val age: Int,
    val gender: String,
    val height: Int,
    val weight: Int,
    val photoUrl: String?=null
)

