package com.saleem.admingecarparking.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
object Glide {
    fun loadImage(context: Context, imageUrl: String, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .into(imageView)
    }

    fun loadImageWithPlaceholder(
        context: Context,
        imageUrl: String,
        imageView: ImageView,
        placeholder: Int
    ) {
        Glide.with(context)
            .load(imageUrl)
            .placeholder(placeholder)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .into(imageView)
    }
}