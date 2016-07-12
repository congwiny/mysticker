package me.jp.sticker.widget.edit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jp.sticker.R;

/**
 * Created by congwiny on 2016/7/11.
 */
public class EditStickerContainer extends RelativeLayout {

    private ArrayList<EditStickerView> mStickerList = new ArrayList<>();
    @BindView(R.id.tv_sticker_cancel)
    private TextView mStickerCancelTv;
    @BindView(R.id.tv_sticker_done)
    private TextView mStickerDoneTv;
    @BindView(R.id.iv_sticker_icon)
    private ImageView mStickerIconIv;
    @BindView(R.id.iv_sticker_text)
    private ImageView mStickerTextIv;
    @BindView(R.id.rv_sticker_list)
    private RecyclerView mStickerRv;
    @BindView(R.id.layout_sticker_input)
    private RelativeLayout mStickerInputLayout;

    @BindView(R.id.ed_sticker_input)
    private EditText mStickerEditText;

    public EditStickerContainer(Context context) {
        this(context, null);
    }

    public EditStickerContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditStickerContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @OnClick(R.id.tv_sticker_cancel)
    public void cancelSticker(View view){
        setVisibility(GONE);
    }

    @OnClick(R.id.tv_sticker_done)
    public void applySticker(View view){

    }

    @OnClick(R.id.iv_sticker_icon)
    public void editIconSticker(View view){

    }

    @OnClick(R.id.iv_sticker_text)
    public void editTextSticker(View view){
        mStickerInputLayout.setVisibility(VISIBLE);
        mStickerEditText.requestFocus();
        //弹出键盘

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_sticker_edit_container, this, true);
        ButterKnife.bind(this);
    }

    public void addSticker() {

    }

    //出场属性动画
    public void enter(){

    }
}
