package com.orbitalsonic.bottomsheetwebviewdialog

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.orbitalsonic.bottomsheetwebviewdialog.databinding.ActivityMainBinding
import com.orbitalsonic.bottomsheetwebviewdialog.databinding.DialogWebviewBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        binding.btnWebview.setOnClickListener {
            webviewDialog("https://www.amazon.com/")
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun webviewDialog(dietUrl:String) {
        val dialogBinding: DialogWebviewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this), R.layout.dialog_webview, null, false
        )
        val mDialog = BottomSheetDialog(this)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(dialogBinding.root)
        mDialog.setCanceledOnTouchOutside(true)

        val parentLayout =
            mDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        parentLayout?.let { it_ ->
            val behaviour = BottomSheetBehavior.from(it_)
            setupFullHeight(it_)
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }


        dialogBinding.webview.settings.setSupportZoom(true)
        dialogBinding.webview.settings.builtInZoomControls = true
        dialogBinding.webview.settings.domStorageEnabled = true
        dialogBinding.webview.settings.loadsImagesAutomatically = true
        dialogBinding.webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        dialogBinding.webview.settings.javaScriptEnabled = true
        dialogBinding.webview.settings.loadWithOverviewMode = true
        dialogBinding.webview.settings.useWideViewPort = true
        dialogBinding.webview.setInitialScale(1)
        dialogBinding.webview.clearCache(true)
        dialogBinding.webview.clearHistory()

        dialogBinding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                dialogBinding.progressBar.visibility = View.GONE
                dialogBinding.webview.visibility = View.VISIBLE
            }
        }
        dialogBinding.webview.loadUrl(dietUrl)

        mDialog.show()
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = (ScreenUtils.getScreenHeight(this) * .85).toInt()
        bottomSheet.layoutParams = layoutParams
    }
}