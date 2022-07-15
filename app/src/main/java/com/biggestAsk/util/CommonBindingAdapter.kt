package com.biggestAsk.util

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter


@BindingAdapter("setImage")
fun setImage(imageView: AppCompatImageView, url: String?) {
    url?.let {
        imageView.setImage(url)
    }
}