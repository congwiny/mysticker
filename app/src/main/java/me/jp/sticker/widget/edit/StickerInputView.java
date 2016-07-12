package me.jp.sticker.widget.edit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import butterknife.ButterKnife;
import me.jp.sticker.R;

/**
 * Created by congwiny on 2016/7/12.
 */
public class StickerInputView extends LinearLayout {

    PopupWindow mStickerStylePop;

    public StickerInputView(Context context) {
        this(context, null);
    }

    public StickerInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_sticker_input, this, true);
        ButterKnife.bind(this);
    }

    private void init() {

    }
}