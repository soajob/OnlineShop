package com.sostrovsky.onlineshop.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.sostrovsky.onlineshop.database.OnlineShopDatabase
import com.sostrovsky.onlineshop.database.ProductDTO
import com.sostrovsky.onlineshop.domain.Product
import com.sostrovsky.onlineshop.remote.ProductApi


class ProductRepository(
    private val api: ProductApi,
    private val database: OnlineShopDatabase
) {
    fun getProducts(): Flow<PagingData<Product>> {
        val productDTOs = getProductDTOs()

        return productDTOs.map { pagingData ->
            pagingData.map {
                asProduct(it)
            }
        }
    }

    private suspend fun asProduct(productDTO: ProductDTO): Product {
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

    private fun getProductDTOs(): Flow<PagingData<ProductDTO>> {
        val pagingSourceFactory = { database.productDao().getProducts() }

        return Pager(
            config = PagingConfig(
                initialLoadSize = INITIAL_LOAD_SIZE,
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = ProductsRemoteMediator(
                api,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getProductBy(id: Int): Product? {
        var product: Product? = null
        val dataSource: DataSource<Product> =
            ProductSource(
                api,
                database,
                id
            )

        withContext(Dispatchers.IO) {
            product = dataSource.getData()
        }

        return product
    }

    suspend fun invertFavoriteState(productId: Int) {
        withContext(Dispatchers.IO) {
            database.favoritesDao().getFavoritesById(productId)?.let {
                it.favorite_state = if (it.favorite_state == 1) 0 else 1
                database.favoritesDao().update(it)
            }
        }
    }

    suspend fun getFavorites(): List<Product> {
        val favorites = mutableListOf<Product>()

        withContext(Dispatchers.IO) {
            favorites.addAll(database.productDao().getFavoriteProducts().map {
                asProduct(it)
            })
        }

        return favorites
    }

    companion object {
        private const val INITIAL_LOAD_SIZE = 1
        private const val PAGE_SIZE = 10
    }
}