package me.jp.sticker.widget.edit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import me.jp.sticker.model.StickerModel;

/**
 * Created by congwiny on 2016/7/11.
 */
public abstract class EditStickerView extends View {

    public static final int EDIT_TYPE_ICON = 1;
    public static final int EDIT_TYPE_TEXT = 2;

    public EditStickerView(Context context) {
        this(context, null);
    }

    public EditStickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditStickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract int getStickerEditType();

    public abstract void applySticker();

    public abstract void editSticker(StickerModel stickerModel);

    public abstract void setOnStickerDeleteListener(OnStickerDeleteListener listener);

    public interface OnStickerDeleteListener {
        void onDelete(EditStickerView stickerEditView);
    }
}
