package ru.technostore.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.technostore.R
import ru.technostore.adapter.VPProductAdapter
import ru.technostore.databinding.FragmentProductDetailsBinding
import ru.technostore.viewmodel.ProductViewModel

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val binding by lazy { FragmentProductDetailsBinding.inflate(layoutInflater) }
    private val vpProductAdapter by lazy {
        VPProductAdapter(getTabLayoutTitles(),
            childFragmentManager)
    }

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        initViews()
        return binding.root
    }

    private fun initViews() {
        setUpDateFromAPI()
        addFragmentToVPAdapter()

        binding.vpProduct.adapter = vpProductAdapter
        binding.tabLProduct.setupWithViewPager(binding.vpProduct)

        binding.llBackArrow.setOnClickListener { findNavController().navigate(R.id.homeFragment) }

        binding.llCart.setOnClickListener { findNavController().navigate(R.id.cartFragment) }
    }

    private fun setUpDateFromAPI() {
        productViewModel.getProductResponseList()
        productViewModel.productResponseList.observe(requireActivity()) {
            binding.apply {
                tvTitle.text = it!!.title
                llIsFavourite.isVisible = it.isFavorites == true
                ratingBar.rating = it.rating!!.toFloat()
            }
        }
    }

    private fun getTabLayoutTitles(): ArrayList<String> {
        val titles = ArrayList<String>()

        titles.add(resources.getString(R.string.str_shop))
        titles.add(resources.getString(R.string.str_details))
        titles.add(resources.getString(R.string.features))

        return titles
    }

    private fun addFragmentToVPAdapter() {
        vpProductAdapter.addFragment(ShopFragment())
        vpProductAdapter.addFragment(DetailsFragment())
        vpProductAdapter.addFragment(FeaturesFragment())
    }

}