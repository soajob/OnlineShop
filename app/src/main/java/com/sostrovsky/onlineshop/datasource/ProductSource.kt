package com.sostrovsky.onlineshop.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.sostrovsky.onlineshop.database.OnlineShopDatabase
import com.sostrovsky.onlineshop.database.ProductDTO
import com.sostrovsky.onlineshop.domain.Product
import com.sostrovsky.onlineshop.remote.ProductApi
import com.sostrovsky.onlineshop.remote.response.safeApiCall

class ProductSource(
    private val api: ProductApi,
    private val database: OnlineShopDatabase,
    private val productId: Int) :
    DataSource<Product> {

    override suspend fun getData(): Product? {
        return getProductFromDB() ?: getProductFromRemote()
    }

    private suspend fun getProductFromDB(): Product? {
        val product: Product?

        withContext(Dispatchers.IO) {
            product = database.productDao().getProductById(productId)?.let {
                asProduct(it)
            }
        }

        return product
    }

    private suspend fun getProductFromRemote() : Product? {
        return safeApiCall(
            call = {
                api.getProductAsync(productId).await()
            },
            error = "Error fetching product"
        )?.let {
            asProduct(it)
        }
    }

    private suspend fun asProduct(productDTO: ProductDTO) : Product {
        val isFavorite = (getProductFavoriteState(productDTO.id) == 1)

        return Product(
            id = productDTO.id,
            title = productDTO.title,
            shortDescription = productDTO.short_description,
            imageUrl = productDTO.image_url,
            price = productDTO.price,
            salePercent = productDTO.sale_percent,
            details = productDTO.details,
            isFavorite = isFavorite
        )
    }

    private suspend fun getProductFavoriteState(id: Int): Int {
        var result = 0;

        withContext(Dispatchers.IO) {
            result = database.favoritesDao().getFavoritesById(id)?.favorite_state ?: 0
        }

        return result
    }
}