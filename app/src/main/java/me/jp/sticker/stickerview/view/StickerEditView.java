package me.jp.sticker.stickerview.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import me.jp.sticker.R;
import me.jp.sticker.stickerview.listener.OnTouchMoveListener;
import me.jp.sticker.stickerview.listener.OnTouchResizeListener;

/**
 * Created by congwiny on 2016/12/5.
 */

public class StickerEditView extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = StickerEditView.class.getSimpleName();

    private ImageView mDeleteImageView;
    private ImageView mResizeImageView;

    private ImageView mStickerImageView;

    private FitTextView mStickerTextView;

    public StickerEditView(Context context) {
        this(context, null);
    }

    public StickerEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    private void initView() {
        mDeleteImageView = new ImageView(getContext());
        mDeleteImageView.setId(R.id.sticker_edit_delete);
        LayoutParams deleteLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDeleteImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_sticker_delete));
        mDeleteImageView.setLayoutParams(deleteLayoutParams);
        mDeleteImageView.setOnClickListener(this);

        mResizeImageView = new ImageView(getContext());
        mResizeImageView.setId(R.id.sticker_edit_resize);
        LayoutParams resizeLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mResizeImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_sticker_resize));
        resizeLayoutParams.addRule(BELOW, R.id.sticker_edit_image);
        resizeLayoutParams.addRule(RIGHT_OF, R.id.sticker_edit_image);
        resizeLayoutParams.topMargin = -mResizeImageView.getDrawable().getIntrinsicHeight() / 2;
        resizeLayoutParams.leftMargin = -mResizeImageView.getDrawable().getIntrinsicWidth() / 2;
        mResizeImageView.setLayoutParams(resizeLayoutParams);
        mResizeImageView.setOnTouchListener(new OnTouchResizeListener(this));

        mStickerImageView = new ImageView(getContext());
        mStickerImageView.setId(R.id.sticker_edit_image);
        mStickerImageView.setBackgroundResource(R.drawable.shape_imageview_border);
        LayoutParams stickerImageParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        stickerImageParams.topMargin = -mDeleteImageView.getDrawable().getIntrinsicHeight() / 2;
        stickerImageParams.leftMargin = -mDeleteImageView.getDrawable().getIntrinsicWidth() / 2;
        stickerImageParams.addRule(BELOW, R.id.sticker_edit_delete);
        stickerImageParams.addRule(RIGHT_OF, R.id.sticker_edit_delete);

        mStickerImageView.setLayoutParams(stickerImageParams);
        mStickerImageView.setOnTouchListener(new OnTouchMoveListener(this));
    }


    public void editSticker() {

        mStickerImageView.setImageDrawable(getResources().getDrawable(R.drawable.artboard_1));
        addView(mStickerImageView);
        addView(mDeleteImageView);
        addView(mResizeImageView);

        mStickerTextView = new FitTextView(getContext());
        mStickerTextView.setTextSize(14);
        mStickerTextView.setMinTextSize(1);
        mStickerTextView.setMaxTextSize(60);
        mStickerTextView.setTextColor(Color.CYAN);
        mStickerTextView.setBackgroundResource(R.drawable.shape_textview_border);

        int width = getDimension(70);
        int height = getDimension(70);
        LayoutParams lp = new LayoutParams(width, height);
        lp.addRule(CENTER_IN_PARENT);
        lp.rightMargin = getDimension(100);
        lp.bottomMargin = getDimension(100);
        addView(mStickerTextView, lp);
    }

    public View getImageContentView() {
        return mStickerImageView;
    }

    public View getDeleteView() {
        return mDeleteImageView;
    }

    public View getResizeView() {
        return mResizeImageView;
    }

    public View getStickerTextView() {
        return mStickerTextView;
    }

    public void setStickerText(Editable text) {
        mStickerTextView.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sticker_edit_delete:
                Log.e(TAG, "delete button onclick");
                //  removeAllViews();
                break;
            case R.id.sticker_edit_image:
                break;
            /**
             * apply
             * mDeleteImageView.setVisibility(INVISIBLE);
             mResizeImageView.setVisibility(INVISIBLE);
             mStickerImageView.setBackgroundResource(0);
             */
        }
    }

    public void applySticker() {
        mDeleteImageView.setVisibility(INVISIBLE);
        mResizeImageView.setVisibility(INVISIBLE);
        mStickerImageView.setBackgroundResource(0);
        mStickerImageView.setEnabled(false);
        mStickerTextView.setBackgroundResource(0);

    }

    public void reeditSticker() {
        mDeleteImageView.setVisibility(VISIBLE);
        mResizeImageView.setVisibility(VISIBLE);
        mStickerImageView.setEnabled(true);
        mStickerImageView.setBackgroundResource(R.drawable.shape_imageview_border);
        mStickerTextView.setBackgroundResource(R.drawable.shape_textview_border);
    }

    private int getDimension(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
    }

    //TODO 自定义view onMesure ,onLayout放置view的位置
}
