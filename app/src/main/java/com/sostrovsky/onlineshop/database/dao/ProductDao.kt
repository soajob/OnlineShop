package com.sostrovsky.onlineshop.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sostrovsky.onlineshop.database.ProductDTO

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductDTO>)

    @Query("SELECT COUNT(*) FROM product")
    fun getProductsAmount(): Int

    @Query("SELECT * FROM product")
    fun getProducts(): PagingSource<Int, ProductDTO>

    @Query("SELECT * FROM product WHERE id = :productId")
    fun getProductById(productId: Int): ProductDTO?

    @Query("SELECT * FROM product pr, favorites fav WHERE pr.id = fav.product_id AND fav.favorite_state = 1")
    fun getFavoriteProducts(): List<ProductDTO>
}