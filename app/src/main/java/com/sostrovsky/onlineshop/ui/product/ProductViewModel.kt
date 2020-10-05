package com.sostrovsky.onlineshop.ui.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.sostrovsky.onlineshop.datasource.ProductRepository
import com.sostrovsky.onlineshop.domain.Product
import com.sostrovsky.onlineshop.domain.ProductFavority

class ProductViewModel @ExperimentalCoroutinesApi constructor(private val repository: ProductRepository) :
    ViewModel() {

    @ExperimentalCoroutinesApi
    fun getProducts(): Flow<PagingData<Product>> {
        return repository.getProducts().cachedIn(viewModelScope)
    }

    @ExperimentalCoroutinesApi
    suspend fun getSelectedProduct(productId: Int): Product? {
        return repository.getProductBy(productId)
    }

    val changeFavoriteStateTo = MutableLiveData<ProductFavority>()

    @ExperimentalCoroutinesApi
    suspend fun saveFavoriteState(productId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.invertFavoriteState(productId)
            changeFavoriteStateTo.value = ProductFavority(productId, isFavorite)
        }
    }

    suspend fun getFavorites(): List<Product> {
        return repository.getFavorites()
    }
}