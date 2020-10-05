package com.sostrovsky.onlineshop.domain

import kotlin.math.roundToInt

data class Product(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val imageUrl: String,
    val price: Int,
    val salePercent: Int = 0,
    val details: String,
    var isFavorite: Boolean = false
) {

    val newPrice: String
        get() = "$ $price${if (salePercent > 0) ",-" else ""}"


    val oldPrice: String
        get() = if (salePercent > 0) "$ ${getOldPrice()},-" else ""


    private fun getOldPrice(): Int {
        val percent = (price.toDouble() / 100)
        return (price + (percent * salePercent)).roundToInt()
    }

    fun invertFavoriteState() {
        isFavorite = !isFavorite
    }

    override fun toString(): String {
        return "Product(id=$id, title='$title', shortDescription='$shortDescription', " +
                "imageUrl='$imageUrl', price=$price, salePercent=$salePercent, " +
                "details='$details', isFavorite=$isFavorite)"
    }
}