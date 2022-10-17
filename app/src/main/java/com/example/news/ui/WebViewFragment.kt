package com.example.news.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.news.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {

    lateinit var binding: FragmentWebViewBinding
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebViewBinding.inflate(inflater)

        viewModel.webViewURL?.let { binding.webView.loadUrl(it) }
        binding.webView.progress

        return binding.root
    }

}