package me.jp.sticker.widget.edit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by congwiny on 2016/7/26.
 */
public class EditStickerAnimView extends View{

    public EditStickerAnimView(Context context) {
        this(context,null);
    }

    public EditStickerAnimView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EditStickerAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
