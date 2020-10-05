package com.sostrovsky.onlineshop.ui.product.list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sostrovsky.onlineshop.domain.Product
import com.sostrovsky.onlineshop.domain.ProductFavority

class ProductListAdapter :
    PagingDataAdapter<Product, RecyclerView.ViewHolder>(PRODUCT_COMPARATOR) {

    var itemClick: ((Int) -> Unit)? = null
    var favoritesClick: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductListViewHolder.create(parent).apply {
            itemClick = { productId ->
                this@ProductListAdapter.itemClick?.invoke(productId)
            }

            favoritesClick = { product ->
                this@ProductListAdapter.favoritesClick?.invoke(product)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val product = getItem(position)
        if (product != null) {
            (holder as ProductListViewHolder).bindView(product)
        }
    }

    fun updateItem(productFavority: ProductFavority) {
        val adapter = this@ProductListAdapter

        for (itemPosition in 0 until adapter.itemCount) {
            val listProduct = adapter.peek(itemPosition)

            listProduct?.let {
                if (listProduct.id == productFavority.product_id) {
                    listProduct.isFavorite = productFavority.isFavorite
                    notifyItemChanged(itemPosition)
                }
            }
        }
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }
}