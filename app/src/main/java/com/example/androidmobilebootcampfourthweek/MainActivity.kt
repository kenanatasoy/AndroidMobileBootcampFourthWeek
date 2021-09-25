package com.example.androidmobilebootcampfourthweek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.androidmobilebootcampfourthweek.fragments.HomeFragment
import com.example.androidmobilebootcampfourthweek.fragments.adapter.TodosAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentState = intent.getStringExtra("state")

        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment)
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.fragment_navigations)

        if(currentState == "home"){
            graph.startDestination = R.id.homeFragment
        }
        else if(currentState == "login"){
            graph.startDestination = R.id.loginFragment
        }

        navHostFragment.navController.graph = graph

    }


}