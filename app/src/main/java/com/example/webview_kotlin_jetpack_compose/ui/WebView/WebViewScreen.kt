package com.example.webview_kotlin_jetpack_compose.ui.WebView

import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(url: String){
    val context = LocalContext.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val webView = remember {
        android.webkit.WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true

            webViewClient = android.webkit.WebViewClient()

            loadUrl(url)
        }
    }

    DisposableEffect(webView) {
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed(){
                if(webView.canGoBack()){
                    webView.goBack()
                }else{
                    isEnabled = false
                    onBackPressedDispatcher?.onBackPressed()
                }
            }
        }
        onBackPressedDispatcher?.addCallback(callback)
        onDispose { callback.remove() }
    }

    AndroidView(
        factory = {webView},
        modifier = Modifier.fillMaxSize()
    )
}