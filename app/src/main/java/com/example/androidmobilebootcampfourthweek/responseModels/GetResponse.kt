package com.example.androidmobilebootcampfourthweek.responseModels

data class GetResponse(
    val count: Int,
    val `data`: ArrayList<Todo>
)