package com.sostrovsky.onlineshop.ui.product.favorites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_favorite_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.sostrovsky.onlineshop.R
import com.sostrovsky.onlineshop.domain.Product
import com.sostrovsky.onlineshop.ui.SecondaryFragment

class FavoriteListFragment : SecondaryFragment(R.layout.fragment_favorite_list) {
    private var getFavoritesJob: Job? = null

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFavorites()
    }

    @ExperimentalCoroutinesApi
    private fun getFavorites() {
        getFavoritesJob?.cancel()
        getFavoritesJob = lifecycleScope.launch {
            viewModel.getFavorites().let {
                if (it.isNotEmpty()) {
                    showFavorites(it)
                } else {
                    showEmptyMessage()
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun showFavorites(list: List<Product>) {
        val adapter = FavoriteListAdapter(list as ArrayList<Product>)
        rvFavorites.adapter = adapter.apply {
            favoritesClick = { product, listIsEmpty ->
                product.invertFavoriteState()
                saveFavoriteState(product)

                if (listIsEmpty) showEmptyMessage()
            }
        }
        adapter.notifyItemInserted(list.size - 1)
    }

    private fun showEmptyMessage() {
        tvEmptyList.visibility = View.VISIBLE
    }

    @ExperimentalCoroutinesApi
    private fun saveFavoriteState(product: Product) {
        super.saveFavoriteState(product.id, product.isFavorite)
    }
}