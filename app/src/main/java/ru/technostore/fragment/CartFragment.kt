package ru.technostore.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.technostore.R
import ru.technostore.adapter.CartAdapter
import ru.technostore.databinding.FragmentCartBinding
import ru.technostore.viewmodel.CartViewModel

@AndroidEntryPoint
class CartFragment : Fragment() {

    private val binding by lazy { FragmentCartBinding.inflate(layoutInflater) }

    private val cartAdapter by lazy { CartAdapter() }

    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        cartViewModel.getCartList()
        cartViewModel.cartList.observe(requireActivity()) {
            cartAdapter.submitList(it!!.basket)

            binding.tvTotal.text = "$${it.total.toString()} us"
            binding.tvDelivery.text = it.delivery
        }

        binding.rvCart.adapter = cartAdapter

        binding.llBackArrow.setOnClickListener { findNavController().navigate(R.id.productDetailsFragment) }

    }

}