package com.example.greenspot.ui.fragments.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.greenspot.R
import com.example.greenspot.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    private lateinit var binding: FragmentWebViewBinding
    private lateinit var webView: WebView
    private val URL = "https://identify.plantnet.org"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWebViewBinding.bind(view)

        init()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init(){

        binding.apply {
            loader.visibility = View.VISIBLE
            webView.loadUrl(URL)

            webView.settings.allowFileAccess = true
            webView.settings.allowContentAccess = true

            webView.settings.javaScriptEnabled = true
            webView.settings.setSupportZoom(true)

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    loader.visibility = View.GONE
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    super.onReceivedError(view, request, error)
                    loader.visibility = View.GONE
                }
            }
        }


    }


}