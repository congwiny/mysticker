package me.jp.sticker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import me.jp.sticker.model.StickerModel;
import me.jp.sticker.widget.display.DisplayStickerTextView;

public class StickerTextActivity extends AppCompatActivity {

    private DisplayStickerTextView mDisplayStickerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_text);

        initView();
        initData();
    }


    private void initView() {
        mDisplayStickerTv = (DisplayStickerTextView) findViewById(R.id.display_sticker);

    }

    private void initData() {


        Glide.with(this).load("http://s3-us-west-2.amazonaws.com/solomedia/test/9/575a066d3ec8c82e920efc623ce16734.png")
                .asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                StickerModel param = (StickerModel) getIntent().getSerializableExtra("sticker_param");
                mDisplayStickerTv.setSticker(param,resource);
            }
        });

    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, StickerTextActivity.class);
        return intent;
    }
}
