package me.jp.sticker.stickerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.jp.sticker.R;

public class StickerShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_show);
    }

    public static void getStartIntent(){
        Intent intent = new Intent();

    }
}
