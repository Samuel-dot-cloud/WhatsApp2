package com.studiofive.whatsapp2;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageViewerActivity extends AppCompatActivity {
    @BindView(R.id.imageViewer)
    ImageView mFullScreenImage;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        ButterKnife.bind(this);

        imageUrl = getIntent().getStringExtra("url");

        Picasso.get().load(imageUrl).into(mFullScreenImage);

    }
}