package me.jp.sticker.widget.display;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import me.jp.sticker.model.StickerModel;
import me.jp.sticker.util.DisplayUtil;
import me.jp.sticker.util.NinePatchChunk;

/**
 * Created by congwiny on 2016/7/11.
 */
public class DisplayStickerTextView extends DisplayStickerView {

    private static final String TAG = DisplayStickerTextView.class.getSimpleName();
    private StickerModel mStickerParam;
    private float[] mOriginPoints;
    private float[] mPoints;
    private Matrix mMatrix;
    private Paint mPaint;

    private TextView mTextView;

    private int mDisplayWidth;
    private int mDisplayHeight;


    public DisplayStickerTextView(Context context) {
        this(context, null);
    }

    public DisplayStickerTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DisplayStickerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDisplayWidth = DisplayUtil.getDisplayWidthPixels(getContext());
        mDisplayHeight = DisplayUtil.getDisplayheightPixels(getContext());
    }

    public void setSticker(StickerModel stickerParam,Bitmap bitmap) {
        mStickerParam = stickerParam;
        float stickerWidth = mStickerParam.getScaleWidth() * mDisplayWidth;
        float stickerHeight = stickerWidth / mStickerParam.getAspectRatio();
        float scaleDiagonal = (float) Math.sqrt(stickerWidth * stickerWidth + stickerHeight * stickerHeight);

        mTextView = new TextView(getContext());


        mTextView.setText("abcdefgaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        mTextView.setTextSize(16);
        mTextView.setMaxWidth((int) (mDisplayWidth * 0.8 + 0.5));

       // mTextView.setBackgroundDrawable(loadNinePatch("/storage/emulated/0/SpeedSoftware/Extracted/txt_bg_pink.9.png", getContext()));
        mTextView.setBackgroundDrawable(loadNinePatch(bitmap, getContext()));
        mTextView.setTextColor(Color.WHITE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        mTextView.measure(widthSpec, heightSpec);

        int width = mTextView.getMeasuredWidth();
        int height = mTextView.getMeasuredHeight();
        int px = mTextView.getMeasuredWidth();
        int py = mTextView.getMeasuredHeight();

        mTextView.layout(0, 0, width, height);

        float originDiagonal = (float) Math.sqrt(px * px + py * py);


        mMatrix = new Matrix();
        mPoints = new float[10];
        mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};

        float stickerScale = scaleDiagonal / originDiagonal;

        mMatrix.postScale(stickerScale, stickerScale, mOriginPoints[8], mOriginPoints[9]);

        mMatrix.mapPoints(mPoints, mOriginPoints);

        float centerX = mStickerParam.getZoomCenterX() * mDisplayWidth;
        float centerY = mStickerParam.getZoomCenterY() * mDisplayHeight;
        //对称点翻转
        mMatrix.postTranslate(centerX - mPoints[8], centerY - mPoints[9]);
        //应使用坐标没变的时候的点。。pre。如果是post的话位置就发生变了
        mMatrix.preRotate(mStickerParam.getDegree(), mPoints[8], mPoints[9]);
        postInvalidate();
    }

    private Drawable loadNinePatch(String path, Context context) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        byte[] chunk = bitmap.getNinePatchChunk();
        if (NinePatch.isNinePatchChunk(chunk)) {
            Log.e(TAG,"load nine patch true");
            return new NinePatchDrawable(context.getResources(), bitmap, chunk,
                    NinePatchChunk.deserialize(bitmap.getNinePatchChunk()).mPaddings, null);
        } else return new BitmapDrawable(bitmap);
    }
    private Drawable loadNinePatch(Bitmap bitmap, Context context) {
        byte[] chunk = bitmap.getNinePatchChunk();
        if (NinePatch.isNinePatchChunk(chunk)) {
            Log.e(TAG,"load nine patch true");
            return new NinePatchDrawable(context.getResources(), bitmap, chunk,
                    NinePatchChunk.deserialize(bitmap.getNinePatchChunk()).mPaddings, null);
        } else return new BitmapDrawable(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTextView != null) {
            canvas.setMatrix(mMatrix);
            mTextView.draw(canvas);
        }
    }
}
