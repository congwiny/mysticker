package me.jp.sticker;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by congwiny on 2016/8/2.
 */
public class StickerApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }
}
