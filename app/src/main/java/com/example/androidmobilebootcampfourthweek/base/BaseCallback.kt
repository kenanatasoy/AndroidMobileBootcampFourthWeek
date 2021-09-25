package com.example.androidmobilebootcampfourthweek.base

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseCallback<T> : Callback<T>, BaseResponseHandlerInterface<T>{
    override fun onResponse(call: Call<T>, response: Response<T>) {

        if(response.isSuccessful){
            response.body()?.let {
                onSuccess(it)
            } ?: run {
                onFailure()
            }
        } else{
            onFailure()
        }

    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure()
    }


    override fun onSuccess(data: T) {
        Log.d("on success ", "on triggered")
    }


    override fun onFailure() {
        Log.d("on failure ", "on triggered")
    }


}