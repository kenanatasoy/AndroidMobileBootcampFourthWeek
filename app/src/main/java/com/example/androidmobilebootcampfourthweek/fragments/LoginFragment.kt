package com.example.androidmobilebootcampfourthweek.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.androidmobilebootcampfourthweek.R
import com.example.androidmobilebootcampfourthweek.responseModels.LoginRequest
import com.example.androidmobilebootcampfourthweek.responseModels.LoginResponse
import com.example.androidmobilebootcampfourthweek.service.ServiceConnector
import com.example.androidmobilebootcampfourthweek.utils.USER_TOKEN
import kotlinx.android.synthetic.main.fragment_login.*
import com.example.androidmobilebootcampfourthweek.base.BaseCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmobilebootcampfourthweek.MainActivity
import com.example.androidmobilebootcampfourthweek.fragments.adapter.TodosAdapter
import com.example.androidmobilebootcampfourthweek.responseModels.GetResponse
import com.example.androidmobilebootcampfourthweek.responseModels.User
import com.example.androidmobilebootcampfourthweek.utils.changeStatusBarColor
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        changeStatusBarColor(R.color.light_pink_2)

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginButton.setOnClickListener{
            val emailToSend = email.text.toString()
            val passwordToSend = password.text.toString()

            if(allFieldsAreValid(emailToSend, passwordToSend)){

                val loginRequest = LoginRequest(emailToSend, passwordToSend)

                ServiceConnector.restInterface.login(loginRequest).enqueue(object: BaseCallback<LoginResponse>(){

                    override fun onSuccess(loginResponse: LoginResponse) {
                        super.onSuccess(loginResponse)
                        val token: String? = loginResponse.token
                        User.getCurrentInstance().token = token
                        saveToken(token!!)
                        Toast.makeText(requireContext(), "Giriş Başarılı", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                    }

                    override fun onFailure() {
                        super.onFailure()
                        Toast.makeText(requireContext(), "Giriş başarısız, e-posta veya şifre hatalı", Toast.LENGTH_LONG).show()
                    }

                })

            }
            else{
                Toast.makeText(requireContext(), "Lütfen doğru formatta email adresi ve şifre girin", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun allFieldsAreValid(
        email: String,
        password: String
    ): Boolean {

        var allFieldsAreValid = true

        if (email.isEmpty() || !isValidEmail(email)) allFieldsAreValid = false

        if (password.isEmpty() || password.length < 3) allFieldsAreValid = false

        return allFieldsAreValid
    }


    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun saveToken(token: String){
        val sharedPreferences = this.activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.apply {
            putString(USER_TOKEN, token)
        }?.apply()

    }

}
