package com.sostrovsky.onlineshop.remote

import kotlinx.coroutines.Deferred
import com.sostrovsky.onlineshop.database.ProductDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {
    @GET("products")
    fun getProductsAsync(@Query("offset") offset: Int, @Query("limit") limit: Int):
            Deferred<Response<List<ProductDTO>>>

    @GET("product")
    fun getProductAsync(@Query("id") id: Int): Deferred<Response<ProductDTO>>
}