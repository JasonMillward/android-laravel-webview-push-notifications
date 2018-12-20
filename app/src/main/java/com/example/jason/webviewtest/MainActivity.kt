package com.example.jason.webviewtest

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {


        // Generic Android view creation
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Get the WebView
        val mainWebView: WebView = this.findViewById(R.id.webview)

        // Enable Javascript
        mainWebView.settings.javaScriptEnabled = true


        mainWebView.settings.builtInZoomControls = false

        mainWebView.settings.mediaPlaybackRequiresUserGesture = false

        mainWebView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        // Override the function we want to hook into
        mainWebView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.e("onPageStarted::", url)
                // This is where we prevent people from visiting other domains
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                // This is where we check to see if the auth route has just finished loading
                // So we can get the JWT token from the local storage
                if (url!!.endsWith("projects/")) {
                    Log.e("endsWith::", url)
                    mainWebView.loadUrl("javascript:(function() {  console.log( window.localStorage['token'] );  })()")
                }
            }
        }

        // And create a new chrome client so we can hook into the console
        mainWebView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {

                // This is the part that will listen for the JWT being console logged
                // And ignore anything else that doesn't match
                Log.e("Console::", consoleMessage.message())

                return true
            }
        }


        // Load The root URL for the web wrapper
        mainWebView.loadUrl("https://laravel.com/")

    }
}
