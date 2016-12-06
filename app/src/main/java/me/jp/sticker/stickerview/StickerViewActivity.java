package me.jp.sticker.stickerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.jp.sticker.R;
import me.jp.sticker.stickerview.view.StickerEditView;

public class StickerViewActivity extends AppCompatActivity {

    private StickerEditView mStickerEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_view);

        mStickerEditView = (StickerEditView) findViewById(R.id.sticker_edit_view);
        mStickerEditView.editSticker();
    }
}
