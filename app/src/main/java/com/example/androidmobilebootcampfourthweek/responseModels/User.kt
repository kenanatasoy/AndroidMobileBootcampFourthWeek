package com.example.androidmobilebootcampfourthweek.responseModels

import com.google.gson.annotations.SerializedName

class User() {

    @SerializedName("name")
    var fullName: String?= null
    var age : Int ?= null
    var email : String ?= null
    var token : String ?= null

    companion object{

        var user : User?= null

        fun getCurrentInstance() : User {

            if(user == null){
                user = User()
            }

            return user!!
        }
    }


    fun setUser(registeredUser: User){
        user?.age = registeredUser.age
        user?.fullName = registeredUser.fullName
        user?.email = registeredUser.email
        user?.token = token
    }



}