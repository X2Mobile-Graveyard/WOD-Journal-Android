package com.x2mobile.wodjar.ui.binding.adapter;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageViewBindingAdapter {

    @BindingAdapter("android:url")
    public static void setImageUrl(ImageView view, String url) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(view.getContext()).load(url).fitCenter().into(view);
        }
    }

}
