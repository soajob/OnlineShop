package com.sostrovsky.onlineshop.ui

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.sostrovsky.onlineshop.R

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavController()
        setToolBar()
        setOfflineBar()
    }

    private fun setNavController() {
        navController = this.findNavController(R.id.myNavHostFragment)
    }

    private fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setToolbarButtons()
    }

    private fun setToolbarButtons() {
        toolbarBackButton = findViewById(R.id.flToolbarBackButton)
        toolbarFavoritesButton = findViewById(R.id.flToolbarFavoritesButton)
    }

    private fun setOfflineBar() {
        offlineBar = findViewById(R.id.tvOfflineText)
    }
}
