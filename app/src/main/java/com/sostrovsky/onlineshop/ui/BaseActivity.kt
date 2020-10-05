package com.sostrovsky.onlineshop.ui

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import io.reactivex.disposables.Disposable
import com.sostrovsky.onlineshop.R
import com.sostrovsky.onlineshop.network.connection.NetworkConnection

open class BaseActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var toolbarBackButton: FrameLayout
    lateinit var toolbarFavoritesButton: FrameLayout
    lateinit var offlineBar : TextView

    private var networkDisposable: Disposable? = null

    override fun onResume() {
        super.onResume()
        networkStateSubscribe()
        checkOffline(NetworkConnection.isAvailable)
    }

    override fun onPause() {
        super.onPause()
        networkStateUnsubscribe()
    }

    @SuppressLint("CheckResult")
    private fun networkStateSubscribe() {
        networkDisposable = NetworkConnection.stateObservable.subscribe { it ->
            checkOffline(it)
        }
    }

    private fun checkOffline(isOffline: Boolean) {
        when (isOffline) {
            true -> hideOfflineBar()
            false -> showOfflineBar()
        }
    }

    private fun networkStateUnsubscribe() {
        networkDisposable?.dispose()
    }

    private fun showOfflineBar() {
        offlineBar.visibility = View.VISIBLE
    }

    private fun hideOfflineBar() {
        offlineBar.visibility = View.GONE
    }

    fun backButtonEnable(callback: OnBackPressedCallback) {
        toolbarBackButton.apply {
            visibility = View.VISIBLE
            isClickable = true
            setOnClickListener {
                callback.handleOnBackPressed()
                backButtonDisable()
            }
        }
    }

    fun backButtonDisable() {
        toolbarBackButton.apply {
            visibility = View.INVISIBLE
            isClickable = false
            setOnClickListener(null)
        }
    }

    fun favoritesButtonEnable(callback: View.OnClickListener? = null) {
        toolbarFavoritesButton.apply {
            visibility = View.VISIBLE
            isClickable = true
            setOnClickListener(callback)
        }
    }

    fun favoritesButtonDisable() {
        toolbarFavoritesButton.apply {
            visibility = View.INVISIBLE
            isClickable = false
            setOnClickListener(null)
        }
    }

    fun addFragmentToBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.myNavHostFragment, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }
}
