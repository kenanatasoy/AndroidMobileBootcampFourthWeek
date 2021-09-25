package com.example.androidmobilebootcampfourthweek

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.androidmobilebootcampfourthweek.base.BaseCallback
import com.example.androidmobilebootcampfourthweek.responseModels.User
import com.example.androidmobilebootcampfourthweek.service.ServiceConnector
import com.example.androidmobilebootcampfourthweek.utils.USER_TOKEN
import com.example.androidmobilebootcampfourthweek.utils.gone
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    private var token : String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.window.statusBarColor = resources.getColor(R.color.blue)

        if(isLoggedIn()){

            User.getCurrentInstance().token = token

            ServiceConnector.restInterface.getMe().enqueue(object : BaseCallback<User>(){

                override fun onSuccess(data: User) {

                    super.onSuccess(data)
                    progressBarIndicator.gone()
                    User.getCurrentInstance().setUser(data)

                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.putExtra("state", "home")
                    startActivity(intent)
                    finish()

                }

                override fun onFailure() {
                    super.onFailure()
                    Log.e("error: ", "something went wrong")
                }
            })
        }

        else{
            progressBarIndicator.gone()
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.putExtra("state", "login")
            startActivity(intent)
            finish()
        }
    }


    private fun isLoggedIn() : Boolean{
        val token = getToken()
        return token.isNotEmpty()
    }

    private fun getToken() : String{
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        token = sharedPreferences.getString(USER_TOKEN, "")!!
        return token!!
    }

}
