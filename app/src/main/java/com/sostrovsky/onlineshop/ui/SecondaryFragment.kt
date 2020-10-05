package com.sostrovsky.onlineshop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.sostrovsky.onlineshop.ui.product.ProductViewModel
import com.sostrovsky.onlineshop.ui.product.ProductViewModelInjection

open class SecondaryFragment(private val layoutId: Int) : Fragment() {
    lateinit var viewModel: ProductViewModel

    private lateinit var backButtonCallback: OnBackPressedCallback
    private var saveFavoriteStateJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackButtonCallback()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
    }

    @ExperimentalCoroutinesApi
    private fun setViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(),
            ProductViewModelInjection.provideViewModelFactory(requireContext())
        ).get(ProductViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setToolbarButtons()
    }

    private fun setBackButtonCallback() {
        backButtonCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backButtonCallback)
    }

    private fun setToolbarButtons() {
        (activity as MainActivity).apply {
            backButtonEnable(callback = backButtonCallback)
            favoritesButtonDisable()
        }
    }

    @ExperimentalCoroutinesApi
    protected fun saveFavoriteState(productId: Int, favoriteState: Boolean) {
        saveFavoriteStateJob?.cancel()
        saveFavoriteStateJob = lifecycleScope.launch {
            viewModel.saveFavoriteState(productId, favoriteState)
        }
    }
}