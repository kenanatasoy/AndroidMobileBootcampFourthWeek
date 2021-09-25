package com.example.androidmobilebootcampfourthweek.base

interface BaseResponseHandlerInterface<T> {

    fun onSuccess(data :  T)
    fun onFailure()

}