package me.jp.sticker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import me.jp.sticker.model.StickerModel;
import me.jp.sticker.widget.display.DisplayStickerIconView;
import me.jp.sticker.widget.edit.EditStickerTextView;

public class StickerEffectActivity extends AppCompatActivity {

    private RelativeLayout mStickerContainer;
    private EditStickerTextView mStickerTextView;
    private Button mStartNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_effect);

        initView();
        initData();
    }

    private void initView() {
        mStickerContainer = (RelativeLayout) findViewById(R.id.sticker_container);
        mStickerTextView = (EditStickerTextView) findViewById(R.id.sticker_textView);

        mStickerTextView.setTextSticker();

        mStartNextBtn = (Button) findViewById(R.id.startNext);
        mStartNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = StickerTextActivity.getIntent(StickerEffectActivity.this);
                StickerModel sm = mStickerTextView.getStickerParam();
                intent.putExtra("sticker_param", sm);
                startActivity(intent);
            }
        });
    }


    private void initData() {
        StickerModel param = (StickerModel) getIntent().getSerializableExtra("sticker_param");
        DisplayStickerIconView stickerView = new DisplayStickerIconView(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mStickerContainer.addView(stickerView, lp);
        stickerView.setSticker(param);

    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, StickerEffectActivity.class);
        return intent;
    }
}
