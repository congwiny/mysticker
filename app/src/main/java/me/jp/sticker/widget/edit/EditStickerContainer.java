package me.jp.sticker.widget.edit;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jp.sticker.R;
import me.jp.sticker.adapter.StickerIconAdapter;
import me.jp.sticker.util.DialogUtil;
import me.jp.sticker.util.ItemClickSupport;
import me.jp.sticker.util.KeyBoardUtils;

/**
 * Created by congwiny on 2016/7/11.
 */
public class EditStickerContainer extends RelativeLayout implements View.OnClickListener, EditStickerView.OnStickerDeleteListener {

    private static final String TAG = EditStickerContainer.class.getSimpleName();
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
    private StickerInputView mStickerInputView;

    @BindView(R.id.ed_sticker_input)
    private EditText mStickerEditText;

    @BindView(R.id.ll_sticker_toolbar)
    private LinearLayout mStickerToolbar;

    @BindView(R.id.rl_edit_sticker)
    private RelativeLayout mEditStickerRlyt;
    @BindView(R.id.view_blank_click)
    private View mBlankClickView;

    private StickerIconAdapter mStickerIconAdapter;

    public EditStickerContainer(Context context) {
        this(context, null);
    }

    public EditStickerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_sticker_edit_container, this, true);
        initView();
        initData();
    }

    private void initData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mStickerRv.setLayoutManager(gridLayoutManager);
        mStickerIconAdapter = new StickerIconAdapter(getContext());
        mStickerRv.setAdapter(mStickerIconAdapter);

        ItemClickSupport.addTo(mStickerRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.e(TAG, "onItemclicked position=" + position);
                int resId = mStickerIconAdapter.getStickerResId(position);
                //加载图片，传给下一个view
                Glide.with(getContext())
                        .load("http://s3-us-west-2.amazonaws.com/solomedia/test/9/a764444bedc844646efb7f1194bfe56f.png")
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                mStickerRv.setVisibility(GONE);
                                mStickerDoneTv.setVisibility(VISIBLE);
                                initIconSticker(resource);
                            }
                        });
            }
        });
    }


    @OnClick(R.id.tv_sticker_cancel)
    public void cancelSticker(View view) {
        setVisibility(GONE);
    }

    @OnClick(R.id.tv_sticker_done)
    public void applySticker(View view) {

    }

    @OnClick(R.id.iv_sticker_icon)
    public void editIconSticker(View view) {

    }

    @OnClick(R.id.iv_sticker_text)
    public void editTextSticker(View view) {

    }


    private void initView() {

        mStickerCancelTv = (TextView) findViewById(R.id.tv_sticker_cancel);
        mStickerDoneTv = (TextView) findViewById(R.id.tv_sticker_done);
        mStickerIconIv = (ImageView) findViewById(R.id.iv_sticker_icon);
        mStickerTextIv = (ImageView) findViewById(R.id.iv_sticker_text);
        mStickerRv = (RecyclerView) findViewById(R.id.rv_sticker_list);


        mStickerInputView = (StickerInputView) findViewById(R.id.layout_sticker_input);
        mStickerEditText = (EditText) findViewById(R.id.ed_sticker_input);
        mStickerToolbar = (LinearLayout) findViewById(R.id.ll_sticker_toolbar);

        mEditStickerRlyt = (RelativeLayout) findViewById(R.id.rl_edit_sticker);
        mBlankClickView = findViewById(R.id.view_blank_click);
        mBlankClickView.setVisibility(GONE);
        mStickerDoneTv.setVisibility(GONE);

        mStickerCancelTv.setOnClickListener(this);
        mStickerDoneTv.setOnClickListener(this);
        mStickerIconIv.setOnClickListener(this);
        mStickerTextIv.setOnClickListener(this);
        mStickerToolbar.setOnClickListener(this);
        mBlankClickView.setOnClickListener(this);

    }

    public void addSticker() {

    }

    //出场属性动画
    public void enter() {

    }


    public boolean onBackPressed() {
        if (mStickerInputView.isShown()) {
            mStickerInputView.setVisibility(GONE);
            mStickerToolbar.setVisibility(VISIBLE);
            mStickerRv.setVisibility(VISIBLE);
            return true;
        }
        if (mStickerList.size() > 0) {
            if (mStickerList.get(0).isShown()) {
                showQuitEditDialog();
                return true;
            }
        }
        return false;
    }

    private void showQuitEditDialog() {
        AlertDialog dialog = DialogUtil.createConfirmDialog(getContext(), "tip", "Are you sure quit?", "Yes", "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mStickerList.size() > 0) {
                            mEditStickerRlyt.removeView(mStickerList.get(0));
                        }
                        setVisibility(GONE);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, DialogUtil.NO_ICON);
        dialog.show();
    }

    private void doCancel() {
        if (mStickerList.size() > 0) {
            showQuitEditDialog();
        } else {
            setVisibility(GONE);
        }
    }

    /**
     * 完成，保存
     */
    private void doDone() {
        EditStickerView stickerView = mStickerList.get(0);
        if (stickerView != null) {
            stickerView.applySticker();
        }
        mStickerToolbar.setVisibility(INVISIBLE);
        mBlankClickView.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sticker_cancel:
                doCancel();
                break;
            case R.id.tv_sticker_done:
                doDone();
                break;
            case R.id.iv_sticker_icon:
                if (mStickerList.size() > 0) {
                    Toast.makeText(getContext(), "must one sticker", Toast.LENGTH_SHORT).show();
                } else {
                    mStickerRv.setVisibility(VISIBLE);
                    mStickerDoneTv.setVisibility(INVISIBLE);
                }
                break;
            case R.id.iv_sticker_text:
                if (mStickerList.size() > 0) {
                    Toast.makeText(getContext(), "must one sticker", Toast.LENGTH_SHORT).show();
                } else {
                    mStickerInputView.setVisibility(VISIBLE);
                    mBlankClickView.setVisibility(VISIBLE);
                    mStickerDoneTv.setVisibility(VISIBLE);

                    mStickerToolbar.setVisibility(GONE);
                    mStickerRv.setVisibility(GONE);
                    KeyBoardUtils.openKeybord(mStickerEditText, getContext());
                    mStickerEditText.requestFocus();
                }
                break;
            case R.id.view_blank_click:
                KeyBoardUtils.closeKeybord(mStickerEditText, getContext());
                mStickerToolbar.setVisibility(VISIBLE);
                mStickerInputView.setVisibility(GONE);
                initTextSticker();
                break;
        }
    }

    private void initTextSticker() {
        EditStickerTextView editStickerTextView = new EditStickerTextView(getContext());
        editStickerTextView.setOnStickerDeleteListener(this);
        if (mStickerList.size() == 0) {
            mStickerList.add(editStickerTextView);

            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mEditStickerRlyt.addView(editStickerTextView, rlp);
            editStickerTextView.setTextSticker();

        } else {

        }
    }

    private void initIconSticker(Bitmap bitmap) {
        EditStickerIconView editStickerIconView = new EditStickerIconView(getContext());
        editStickerIconView.setOnStickerDeleteListener(this);
        if (mStickerList.size() == 0) {
            mStickerList.add(editStickerIconView);

            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mEditStickerRlyt.addView(editStickerIconView, rlp);
            editStickerIconView.setWaterMark(bitmap);

        } else {

        }
    }

    @Override
    public void onDelete(EditStickerView stickerEditView) {
        if (mStickerList.contains(stickerEditView)) {
            mStickerList.remove(stickerEditView);
        }
        mBlankClickView.setVisibility(GONE);
    }
}
