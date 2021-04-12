package com.angus.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    companion object{
        private val TAG = DetailActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val item = intent.getParcelableExtra<Item>("ITEM")
        web.settings.javaScriptEnabled = true
        web.loadUrl("https://www.google.com/search?q=%E7%BE%A4%E5%89%B5%E9%85%8D%E6%81%AF2021&rlz=1C1VDKB_zh-TWTW946TW946&oq=%E7%BE%A4%E5%89%B5+%E9%85%8D&aqs=chrome.2.69i57j0i324l2j0l2.6295j0j7&sourceid=chrome&ie=UTF-8")
    }
}