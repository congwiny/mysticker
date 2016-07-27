package me.jp.sticker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.jp.sticker.widget.edit.EditStickerAnimView;

public class StickerAnimActivity extends AppCompatActivity {

    private EditStickerAnimView stickerAnimView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sticker_anim);

        stickerAnimView = (EditStickerAnimView) findViewById(R.id.sticker_anim_view);
    }
}
