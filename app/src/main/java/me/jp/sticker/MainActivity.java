package me.jp.sticker;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import me.jp.sticker.adapter.GalleryAdapter;
import me.jp.sticker.model.StickerModel;
import me.jp.sticker.widget.edit.EditStickerIconTextView;
import me.jp.sticker.widget.edit.EditStickerIconView;
import me.jp.sticker.widget.edit.EditStickerView;


public class MainActivity extends AppCompatActivity {
    //effect picture save absolute path
    private final String EFFECT_PICTURE = Environment
            .getExternalStorageDirectory() + File.separator + "Sticker" + File.separator + "effect_picture.jpg";

    Toolbar mToolbar;

    ImageView mImageView;
    RecyclerView mRecyclerView;
    GalleryAdapter mGalleryAdapter;
    ImageView mEffectImg;

    ImageView myImageVew;

    int mStatusBarHeight;
    int mToolBarHeight;

    List<EditStickerIconTextView> mStickers = new ArrayList<>();
    int[] mResIds = new int[]{R.mipmap.ic_sticker_01, R.mipmap.ic_sticker_02, R.mipmap.ic_sticker_03, R.mipmap.ic_sticker_04, R.mipmap.ic_sticker_05, R.mipmap.ic_sticker_06, R.mipmap.ic_sticker_07, R.mipmap.ic_sticker_08};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();

    }

    private void initEvent() {
        mGalleryAdapter.setOnItemClickListener(new GalleryAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int resId) {
                addStickerItem(resId);
            }
        });
    }

    private void addStickerItem(int resId) {
        resetStickersFocus();
        EditStickerIconTextView stickerEditView = new EditStickerIconTextView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.image);
        params.addRule(RelativeLayout.ALIGN_TOP, R.id.image);
        ((ViewGroup) mImageView.getParent()).addView(stickerEditView, params);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        stickerEditView.setWaterMark(bitmap);
        mStickers.add(stickerEditView);
        stickerEditView.setOnStickerDeleteListener(new EditStickerView.OnStickerDeleteListener() {
            @Override
            public void onDelete(EditStickerView stickerEditView) {
                if (mStickers.contains(stickerEditView))
                    mStickers.remove(stickerEditView);
            }
        });
    }

    private void resetStickersFocus() {
        for (EditStickerIconTextView stickerEditView : mStickers) {
            stickerEditView.setFocusable(false);
        }
    }

    private void initView() {
        mStatusBarHeight = getStatusBarHeight();
        mToolBarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpToolbar();
        mImageView = (ImageView) findViewById(R.id.image);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mImageView.setImageBitmap(getImageFromAssetsFile("photo.jpg"));
        mEffectImg = (ImageView) findViewById(R.id.effect_image);

        mGalleryAdapter = new GalleryAdapter(mResIds);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mGalleryAdapter);

        myImageVew = (ImageView) findViewById(R.id.loading_image);


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadGlideImage();
    }

    private void loadGlideImage() {

        final RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(3000);
        rotate.setRepeatCount(Animation.INFINITE);
        myImageVew.setAnimation(rotate);
        rotate.start();

        // your loading code from above
        Glide.with(this)
                .load("http://i7.umei.cc//img2012/2016/07/12/012BT1320/0030.jpg")
                .centerCrop()
                .placeholder(R.drawable.sticker_loading)
                .crossFade()
                // new listener to stop unnecessary animation when the placeholder is not visible any more
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.e("load ", "onexception" + e);
                        myImageVew.clearAnimation();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.e("load ", "load ready");
                        myImageVew.clearAnimation();
                        return false;
                    }
                })
                .into(myImageVew);
    }

    private void setUpToolbar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("Sticker");
        setSupportActionBar(mToolbar);//remember set theme to NoActionBar
        mToolbar.setOnMenuItemClickListener(OnMenuItemListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    Toolbar.OnMenuItemClickListener OnMenuItemListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_reset:
                    resetDisplay();
                    break;
                case R.id.action_save:
                    saveEffectBitmap();
                    break;
                case R.id.action_ok:
                    //start activity
                    StickerModel stickerParam = mStickers.get(0).getStickerParam();
                    Intent intent = StickerEffectActivity.getIntent(MainActivity.this);
                    intent.putExtra("sticker_param", stickerParam);
                    startActivity(intent);
                    break;
            }
            return true;
        }
    };

    private void resetDisplay() {
        mEffectImg.setVisibility(View.GONE);
        mStickers.clear();

        ViewGroup viewGroup = (ViewGroup) mImageView.getParent();
        int childCount = viewGroup.getChildCount();
        if (childCount > 1)
            viewGroup.removeViews(1, childCount - 1);

    }

    /**
     * save image with stickers
     */
    public void saveEffectBitmap() {

        mImageView.setDrawingCacheEnabled(true);
        mImageView.buildDrawingCache();
        Bitmap bmBg = mImageView.getDrawingCache();//get background bitmap
        bmBg = Bitmap.createBitmap(bmBg, 0, 0, bmBg.getWidth(), bmBg.getHeight());//create bitmap with size
        mImageView.destroyDrawingCache();
        Canvas canvas = new Canvas(bmBg);//create canvas with background bitmap size
        canvas.drawBitmap(bmBg, 0, 0, null);

        //draw stickers on canvas
        for (EditStickerIconTextView stickerEditView : mStickers) {
            Bitmap bmSticker = stickerEditView.getBitmap();
            canvas.drawBitmap(bmSticker, 0, 0, null);
        }

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        //save effect bitmap into sd-card
//        BitmapUtil.saveBitmap(bmBg, EFFECT_PICTURE);
        mEffectImg.setVisibility(View.VISIBLE);
        mEffectImg.setImageBitmap(bmBg);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) ev.getX();
            //calculate action point Y apart from Container layout origin
            int y = (int) ev.getY() - mStatusBarHeight - mToolBarHeight;
            for (EditStickerIconTextView stickerEditView : mStickers) {
                // dispatch focus to the sticker based on Coordinate
                boolean isContains = stickerEditView.getContentRect().contains(x, y);
                if (isContains) {
                    resetStickersFocus();
                    stickerEditView.setFocusable(true);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
