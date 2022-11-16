package ru.technostore.fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.technostore.R
import ru.technostore.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {

    private val binding by lazy { FragmentSplashBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        initViews()
        return binding.root
    }

    private fun initViews() {
         countDownTimer()
    }

    private fun countDownTimer() {
        object : CountDownTimer(20,20) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                findNavController().navigate(R.id.homeFragment)
            }
        }.start()
    }


}