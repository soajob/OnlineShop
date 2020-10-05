package com.sostrovsky.onlineshop.ui.product.details

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.product_short_data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.sostrovsky.onlineshop.R
import com.sostrovsky.onlineshop.UiComponentsUtil
import com.sostrovsky.onlineshop.domain.Product
import com.sostrovsky.onlineshop.ui.SecondaryFragment

class ProductDetailsFragment : SecondaryFragment(R.layout.fragment_product_details) {
    private val passedArgs: ProductDetailsFragmentArgs by navArgs()

    private var getSelectedProductJob: Job? = null
    private var selectedProduct: Product? = null
    private var favoriteStateChanged = false

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSelectedProduct()
    }

    @ExperimentalCoroutinesApi
    private fun getSelectedProduct() {
        getSelectedProductJob?.cancel()
        getSelectedProductJob = lifecycleScope.launch {
            viewModel.getSelectedProduct(passedArgs.productId)?.let { product ->
                selectedProduct = product
                showProduct()
            }
        }
    }

    private fun showProduct() {
        selectedProduct?.let { product ->
            Glide.with(ivProductImage.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(ivProductImage)
            cbFavorite.apply {
                isChecked = product.isFavorite

                setOnClickListener {
                    favoriteStateChanged = !favoriteStateChanged
                    product.invertFavoriteState()
                }
            }
            tvProductTitle.text = product.title
            tvProductShortDescription.text = product.shortDescription
            tvProductNewPrice.text = product.newPrice
            UiComponentsUtil.strikeLineThrough(tvProductOldPrice, product.oldPrice)
            lblDescriptionBody.text = product.details
        }
    }

    @ExperimentalCoroutinesApi
    override fun onDestroyView() {
        super.onDestroyView()
        if (favoriteStateChanged) {
            super.saveFavoriteState(selectedProduct!!.id, selectedProduct!!.isFavorite)
        }
    }
}