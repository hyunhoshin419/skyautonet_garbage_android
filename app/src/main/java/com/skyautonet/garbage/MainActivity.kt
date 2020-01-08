package com.skyautonet.garbage

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val URL = "http://stg.webscan-fleet.com/garbage/index.html"
//    private val URL = "https://www.sky-net.co.kr/garbage/index.html"
    private var isAlreadyCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.run {
            settings.javaScriptEnabled = true // 자바스크립트 허용
            settings.domStorageEnabled = true
            settings.allowFileAccess = true

            settings.loadWithOverviewMode = true // 매타태크

            settings.setAppCacheEnabled(true)
            webChromeClient = WebChromeClient()
        }
        WebSettings::class.java.getMethod("setLightTouchEnabled", java.lang.Boolean.TYPE)
        WebView.setWebContentsDebuggingEnabled(true)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                showErrorDialog("Error", "인터넷 연결이 원활하지 않습니다.", this@MainActivity)
            }
        }
        webView.loadUrl(URL)

    }

    override fun onResume() {
        super.onResume()
        if(isAlreadyCreated && !isNetworkAvailable()){
            isAlreadyCreated = false
        }
    }

    private fun isNetworkAvailable() : Boolean{
        val connectionManager =
            this@MainActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.isDefaultNetworkActive

        return networkInfo != null && networkInfo
    }

    private fun showErrorDialog(title : String, message: String, context:Context){
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setNegativeButton("취소"){_,_ ->
            this@MainActivity.finish()
        }
        dialog.setPositiveButton("재시도"){_, _ ->
            this@MainActivity.recreate()
        }
        dialog.create().show()
    }


}
