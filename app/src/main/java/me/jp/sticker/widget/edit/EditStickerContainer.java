package me.jp.sticker.widget.edit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by congwiny on 2016/7/11.
 */
public class EditStickerContainer extends RelativeLayout {
    public EditStickerContainer(Context context) {
        this(context,null);
    }

    public EditStickerContainer(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EditStickerContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }
}
