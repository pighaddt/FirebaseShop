package com.angus.shop

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.angus.shop.model.Item
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row.view.*

class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val titleText = itemView.item_title
    val priceText = itemView.item_price
    val imageView = itemView.item_image
    val countText = itemView.item_count

    fun bindTo(item: Item){
        titleText.text = item.title
        priceText.text = item.price.toString()
        Glide.with(itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.com_facebook_button_icon)
            .apply(RequestOptions().override(120))
            .into(imageView)
        countText.text = item.viewCount.toString()
        countText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.eye, 0, 0, 0)
    }
}