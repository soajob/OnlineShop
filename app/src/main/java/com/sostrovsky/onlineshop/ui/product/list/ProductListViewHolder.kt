package com.sostrovsky.onlineshop.ui.product.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.product_short_data.view.*
import com.sostrovsky.onlineshop.R
import com.sostrovsky.onlineshop.UiComponentsUtil
import com.sostrovsky.onlineshop.domain.Product

class ProductListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var itemClick: ((Int) -> Unit)? = null
    var favoritesClick: ((Product) -> Unit)? = null

    fun bindView(product: Product) {
        Glide.with(itemView.ivProductImage.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(itemView.ivProductImage)
        itemView.cbFavorite.isChecked = product.isFavorite
        itemView.tvProductTitle.text = product.title
        itemView.tvProductShortDescription.text = product.shortDescription
        itemView.tvProductNewPrice.text = product.newPrice
        UiComponentsUtil.strikeLineThrough(itemView.tvProductOldPrice, product.oldPrice)

        itemView.setOnClickListener {
            itemClick?.invoke(product.id)
        }

        itemView.cbFavorite.setOnClickListener {
            favoritesClick?.invoke(product)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ProductListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_short_data, parent, false)
            return ProductListViewHolder(view)
        }
    }
}