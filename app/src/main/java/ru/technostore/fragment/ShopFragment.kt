package ru.technostore.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_shop.*
import ru.technostore.R
import ru.technostore.databinding.FragmentShopBinding
import ru.technostore.viewmodel.ProductViewModel

@AndroidEntryPoint
class ShopFragment : Fragment() {

    private val binding by lazy { FragmentShopBinding.inflate(layoutInflater) }

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
        setDateFromAPI()

        binding.llColor1.setOnClickListener {
            binding.ivChm1.isVisible = true
            binding.ivChm2.isVisible = false
        }

        binding.llColor2.setOnClickListener {
            binding.ivChm1.isVisible = false
            binding.ivChm2.isVisible = true
        }

        binding.cvCapacity1.setOnClickListener {
            changeBackgroundColorOfFirstCapacityButton()
        }

        binding.cvCapacity2.setOnClickListener {
            changeBackgroundColorOfSecondCapacityButton()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setDateFromAPI() {
        productViewModel.getProductResponseList()
        productViewModel.productResponseList.observe(requireActivity()) {
            binding.tvCamera.text = it?.camera
            binding.tvSsd.text = it?.ssd
            binding.tvSd.text = it?.sd
            binding.tvCapacity1.text = it?.capacity!![0] + " GB"
            binding.tvCapacity2.text = it.capacity[1] + " GB"
            binding.tvPrice.text = it.price.toString() + ".00"
            binding.llColor1.background.setTint(Color.parseColor(it.color!![0]))
            binding.llColor2.background.setTint(Color.parseColor(it.color[1]))
        }
    }

    private fun changeBackgroundColorOfFirstCapacityButton() {
        binding.cvCapacity1.setCardBackgroundColor(Color.parseColor("#FF6E4E"))
        binding.cvCapacity1.background.setTint(Color.parseColor("#FF6E4E"))
        binding.tvCapacity1.setTextColor(resources.getColor(R.color.white))

        binding.cvCapacity2.setCardBackgroundColor(Color.parseColor("#00000000"))
        binding.cvCapacity2.background.setTint(Color.parseColor("#00000000"))
        binding.tvCapacity2.setTextColor(resources.getColor(R.color.black))
    }

    private fun changeBackgroundColorOfSecondCapacityButton() {
        binding.cvCapacity2.setCardBackgroundColor(Color.parseColor("#FF6E4E"))
        binding.cvCapacity2.background.setTint(Color.parseColor("#FF6E4E"))
        binding.tvCapacity2.setTextColor(resources.getColor(R.color.white))

        binding.cvCapacity1.setCardBackgroundColor(Color.parseColor("#00000000"))
        binding.cvCapacity1.background.setTint(Color.parseColor("#00000000"))
        binding.tvCapacity1.setTextColor(resources.getColor(R.color.black))
    }

}