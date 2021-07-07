package com.ateam.mannajob;

import android.graphics.Bitmap;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public interface ImageFormToServer {
    public void getImageToServer(CircleImageView imageView, String imageName);
}
