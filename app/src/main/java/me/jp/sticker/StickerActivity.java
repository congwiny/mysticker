package me.jp.sticker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import me.jp.sticker.widget.edit.EditStickerContainer;

public class StickerActivity extends AppCompatActivity {
    private static final String TAG = StickerActivity.class.getSimpleName();
    private EditStickerContainer mStickerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);
        ButterKnife.bind(this);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.sticker_container);

        mStickerContainer = new EditStickerContainer(this);

        frameLayout.addView(mStickerContainer);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e(TAG, "onBackPressed");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mStickerContainer.onBackPressed()) {
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
