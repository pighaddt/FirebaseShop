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

    fun bindTo(Item: Item){
        titleText.text = Item.title
        priceText.text = Item.price.toString()
        Glide.with(itemView.context)
            .load(Item.imageUrl)
            .placeholder(R.drawable.com_facebook_button_icon)
            .apply(RequestOptions().override(120))
            .into(imageView)
    }
}