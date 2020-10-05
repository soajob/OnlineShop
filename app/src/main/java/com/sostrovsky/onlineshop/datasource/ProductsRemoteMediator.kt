package com.sostrovsky.onlineshop.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.sostrovsky.onlineshop.database.*
import com.sostrovsky.onlineshop.remote.ProductApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProductsRemoteMediator(
    private val api: ProductApi,
    private val database: OnlineShopDatabase
) : RemoteMediator<Int, ProductDTO>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductDTO>
    ): MediatorResult {
        val offset = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> 0
            LoadType.APPEND -> getOffsetForAppend()
        }

        try {
            val products = mutableListOf<ProductDTO>()

            withContext(Dispatchers.IO) {
                if (loadType == LoadType.REFRESH || loadType == LoadType.APPEND) {
                    val response = api.getProductsAsync(offset, state.config.pageSize).await()

                    if (response.isSuccessful) {
                        products.addAll(response.body()!!)
                        database.productDao().insertAll(products)

                        database.favoritesDao().insertAll(products.map {
                            it.asFavoritesDTO()
                        })
                    }
                }
            }
            return MediatorResult.Success(endOfPaginationReached = products.isEmpty())
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getOffsetForAppend(): Int {
        val result = mutableListOf<Int>()

        withContext(Dispatchers.IO) {
            result.add(database.productDao().getProductsAmount())
        }

        return result[0]
    }
}