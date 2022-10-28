package com.example.news.ui.webpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.news.databinding.FragmentWebViewBinding
import com.example.news.ui.MainViewModel

class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebViewBinding
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebViewBinding.inflate(inflater)

        viewModel.webViewURL?.let { binding.webView.loadUrl(it) }

        return binding.root
    }

}