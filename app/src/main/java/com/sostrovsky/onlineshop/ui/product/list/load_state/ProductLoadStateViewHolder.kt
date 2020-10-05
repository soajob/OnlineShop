package com.sostrovsky.onlineshop.ui.product.list.load_state

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.sostrovsky.onlineshop.R
import com.sostrovsky.onlineshop.databinding.LoadDataErrorBlockBinding

class ProductLoadStateViewHolder(private val binding: LoadDataErrorBlockBinding,
                                 retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.btnRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        binding.btnRetry.isVisible = loadState !is LoadState.Loading
        binding.tvErrorText.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): ProductLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_data_error_block, parent, false)
            val binding = LoadDataErrorBlockBinding.bind(view)
            return ProductLoadStateViewHolder(
                binding,
                retry
            )
        }
    }
}