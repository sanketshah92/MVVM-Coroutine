package com.example.samplemvvmkotlin.detailsmodule.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.samplemvvmkotlin.R
import com.example.samplemvvmkotlin.databinding.FragmentDashboardBinding
import com.example.samplemvvmkotlin.databinding.FragmentDetailsBinding
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {
    lateinit var binding: FragmentDetailsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            val args = DetailsFragmentArgs.fromBundle(it)
            detailsWebView.loadUrl(args.detailsData.url)
            binding.data = args.detailsData
        }
    }
}