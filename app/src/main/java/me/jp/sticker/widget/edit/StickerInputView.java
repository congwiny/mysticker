package me.jp.sticker.widget.edit;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import me.jp.sticker.R;

/**
 * Created by congwiny on 2016/7/12.
 */
public class StickerInputView extends LinearLayout implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = StickerInputView.class.getSimpleName();
    private PopupWindow mStickerStylePop;
    private Point mStickerPopPoint;
    private OnStickerTextBGChangedListener mListener;

    private ImageView mStickerStyleIv;
    private EditText mStickerInputEdt;
    private RadioGroup mStickerBarRGP;

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
        initView();
    }

    private void initView() {
        mStickerStyleIv = (ImageView) findViewById(R.id.iv_sticker_style);
        mStickerInputEdt = (EditText) findViewById(R.id.ed_sticker_input);
        mStickerStyleIv.setOnClickListener(this);
    }

    private void init() {

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onlayout changed" + changed + "b=" + b);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_sticker_style:
                //显示popwindow
                if (mStickerStylePop != null) {
                    if (mStickerStylePop.isShowing()) {
                        // mStickerStylePop.dismiss();
                    } else {
                        //show
                        calculatePosition(this);
                        mStickerStylePop.showAtLocation(this, Gravity.NO_GRAVITY, mStickerPopPoint.x, mStickerPopPoint.y);
                    }
                } else {
                    //init and show
                    initStickerPopWindow(this);
                    mStickerStylePop.showAtLocation(this, Gravity.NO_GRAVITY, mStickerPopPoint.x, mStickerPopPoint.y);
                }
                break;
        }

    }

    private void initStickerPopWindow(View view) {
        View stickerPopView = LayoutInflater.from(getContext()).inflate(R.layout.layout_sticker_pop, null);
        stickerPopView.setBackgroundResource(R.drawable.text_sticker_pop_bg);
        mStickerBarRGP = (RadioGroup) stickerPopView.findViewById(R.id.rg_sticker_bar);
        mStickerBarRGP.setOnCheckedChangeListener(this);
        mStickerStylePop = new PopupWindow(stickerPopView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 响应返回键必须的语句。
        mStickerStylePop.setBackgroundDrawable(new BitmapDrawable());
        mStickerPopPoint = new Point();
        calculatePosition(view);
    }

    private void calculatePosition(View view) {
        int[] screen_pos = new int[2];
        view.getLocationOnScreen(screen_pos);
        Rect anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0] + view.getWidth(), screen_pos[1] + view.getHeight());
        int position_y = anchor_rect.top - anchor_rect.height();
        mStickerStyleIv.getLocationInWindow(screen_pos);
        mStickerPopPoint.x = screen_pos[0];
        mStickerPopPoint.y = position_y;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int bgIndex = 0;
        switch (checkedId) {
            case R.id.rb_sticker_black:
                mStickerStyleIv.setImageResource(R.drawable.selector_text_sticker_black);
                setBackgroundResource(R.color.bg_text_sticker_black);
                mStickerStylePop.dismiss();
                bgIndex = 0;
                break;
            case R.id.rb_sticker_red:
                mStickerStyleIv.setImageResource(R.drawable.selector_text_sticker_red);
                setBackgroundResource(R.color.bg_text_sticker_red);
                mStickerStylePop.dismiss();
                bgIndex = 1;
                break;
            case R.id.rb_sticker_yellow:
                mStickerStyleIv.setImageResource(R.drawable.selector_text_sticker_yellow);
                setBackgroundResource(R.color.bg_text_sticker_yellow);
                mStickerStylePop.dismiss();
                bgIndex = 2;
                break;
            case R.id.rb_sticker_green:
                mStickerStyleIv.setImageResource(R.drawable.selector_text_sticker_green);
                setBackgroundResource(R.color.bg_text_sticker_green);
                mStickerStylePop.dismiss();
                bgIndex = 3;
                break;
            case R.id.rb_sticker_blue:
                mStickerStyleIv.setImageResource(R.drawable.selector_text_sticker_blue);
                setBackgroundResource(R.color.bg_text_sticker_blue);
                mStickerStylePop.dismiss();
                bgIndex = 4;
                break;
            case R.id.rb_sticker_pink:
                mStickerStyleIv.setImageResource(R.drawable.selector_text_sticker_pink);
                setBackgroundResource(R.color.bg_text_sticker_pink);
                mStickerStylePop.dismiss();
                bgIndex = 5;
                break;
        }
        if (mListener != null) {
            mListener.onStickerTextBGChanged(bgIndex);
        }
    }

    public void setOnStickerTextBGChangedListener(OnStickerTextBGChangedListener listener) {
        mListener = listener;
    }

    public interface OnStickerTextBGChangedListener {
        void onStickerTextBGChanged(int bgIndex);
    }

    public void reset() {
        mStickerInputEdt.getText().clear();
        if (mStickerBarRGP != null) {
            mStickerBarRGP.check(R.id.rb_sticker_black);
        }
    }
}
