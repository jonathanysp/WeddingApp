package com.jonathanysp.weddingapp;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ImageCache extends LruCache<String, Bitmap> {

	public ImageCache(int maxSize) {
		super(maxSize);
	}

}
