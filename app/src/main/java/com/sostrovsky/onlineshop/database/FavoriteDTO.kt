package com.sostrovsky.onlineshop.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteDTO(
    @PrimaryKey @field:ColumnInfo(name = "product_id") val product_id: Int,
    @field:ColumnInfo(name = "favorite_state") var favorite_state: Int = 0
) {
    override fun toString(): String {
        return "FavoritesDTO(product_id=$product_id, favorite_state=$favorite_state)"
    }
}