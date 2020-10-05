package com.sostrovsky.onlineshop.database.dao

import androidx.room.*
import com.sostrovsky.onlineshop.database.FavoriteDTO

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<FavoriteDTO>)

    @Query("SELECT * FROM favorites")
    fun getFavorites(): List<FavoriteDTO>

    @Query("SELECT * FROM favorites WHERE product_id = :productId")
    fun getFavoritesById(productId: Int): FavoriteDTO?

    @Query("UPDATE favorites SET favorite_state = :favoriteState WHERE product_id = :productId")
    suspend fun updateFavoriteState(productId: Int, favoriteState: Int)

    @Update
    suspend fun update(obj: FavoriteDTO)
}