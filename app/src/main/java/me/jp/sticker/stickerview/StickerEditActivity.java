package me.jp.sticker.stickerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import me.jp.sticker.R;
import me.jp.sticker.stickerview.view.StickerEditView;

public class StickerEditActivity extends AppCompatActivity {

    private StickerEditView mStickerEditView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_edit);

        mStickerEditView = (StickerEditView) findViewById(R.id.sticker_edit_view);
        mStickerEditView.editSticker();

        mEditText = (EditText) findViewById(R.id.edit_input);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    mStickerEditView.setStickerText(s);
            }
        });

        findViewById(R.id.btn_reedit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStickerEditView.reeditSticker();
            }
        });

        findViewById(R.id.btn_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStickerEditView.applySticker();
            }
        });

        findViewById(R.id.btn_gonext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
