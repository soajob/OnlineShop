package com.sostrovsky.onlineshop.ui.product

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.sostrovsky.onlineshop.database.OnlineShopDatabase
import com.sostrovsky.onlineshop.datasource.ProductRepository
import com.sostrovsky.onlineshop.remote.Remote

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object ProductViewModelInjection {
    private fun provideProductRepository(context: Context): ProductRepository {
        return ProductRepository(
            Remote.productApi,
            OnlineShopDatabase.getInstance(context)
        )
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ProductViewModelFactory(provideProductRepository(context))
    }
}