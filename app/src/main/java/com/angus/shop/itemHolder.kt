package com.angus.shop

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row.view.*

class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val titleText = itemView.item_title
    val priceText = itemView.item_price
    val imageView = itemView.item_image

    fun bindTo(item: item){
        titleText.text = item.title
        priceText.text = item.price.toString()
        Glide.with(itemView.context)
            .load(item.imageUrl)
            .apply(RequestOptions().override(120))
            .into(imageView)
    }
}