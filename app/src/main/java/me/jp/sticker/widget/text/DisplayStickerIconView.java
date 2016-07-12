package me.jp.sticker.widget.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import me.jp.sticker.R;
import me.jp.sticker.model.StickerModel;
import me.jp.sticker.util.DisplayUtil;

/**
 * Created by congwiny on 2016/7/8.
 */
public class DisplayStickerIconView extends View {

    private static final String TAG = DisplayStickerIconView.class.getSimpleName();
    private StickerModel mStickerParam;
    private float[] mOriginPoints;
    private float[] mPoints;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private Paint mPaint;

    public DisplayStickerIconView(Context context) {
        this(context, null);
    }

    public DisplayStickerIconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DisplayStickerIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4.0f);
        mPaint.setColor(Color.WHITE);
    }


    public void setSticker(StickerModel stickerParam) {

        float displayWidth = DisplayUtil.getDisplayWidthPixels(getContext());
        float displayHeight = DisplayUtil.getDisplayheightPixels(getContext());

        mStickerParam = stickerParam;

        try {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sticker_01);

            float px = mBitmap.getWidth();
            float py = mBitmap.getHeight();
            float originDiagonal = (float) Math.sqrt(px * px + py * py);

            float bmpWidth = mStickerParam.getScaleWidth() * displayWidth;
            float bmpHeight = bmpWidth / mStickerParam.getAspectRatio();
            float scaleDiagonal = (float) Math.sqrt(bmpWidth * bmpWidth + bmpHeight * bmpHeight);

            mMatrix = new Matrix();
            mPoints = new float[10];
            mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};

            float stickerScale = scaleDiagonal / originDiagonal;
            //镜像
            mMatrix.postScale(stickerScale, stickerScale, mOriginPoints[8], mOriginPoints[9]);

            mMatrix.mapPoints(mPoints, mOriginPoints);

            float centerX = mStickerParam.getZoomCenterX() * displayWidth;
            float centerY = mStickerParam.getZoomCenterY() * displayHeight;
            //对称点翻转
            mMatrix.postTranslate(centerX - mPoints[8], centerY - mPoints[9]);
            //应使用坐标没变的时候的点。。pre。如果是post的话位置就发生变了
            mMatrix.preRotate(mStickerParam.getDegree(), mPoints[8], mPoints[9]);
            postInvalidate();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
        }

    }

    /**
     * 镜像
     *
     * @param stickerParam
     */
    public void setStickerParam2(StickerModel stickerParam) {

        float displayWidth = DisplayUtil.getDisplayWidthPixels(getContext());
        float displayHeight = DisplayUtil.getDisplayheightPixels(getContext());

        mStickerParam = stickerParam;

        try {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sticker_02);

            float px = mBitmap.getWidth();
            float py = mBitmap.getHeight();
            float originDiagonal = (float) Math.sqrt(px * px + py * py);

            float bmpWidth = mStickerParam.getScaleWidth() * displayWidth;
            float bmpHeight = bmpWidth / mStickerParam.getAspectRatio();
            float scaleDiagonal = (float) Math.sqrt(bmpWidth * bmpWidth + bmpHeight * bmpHeight);

            mMatrix = new Matrix();
            mPoints = new float[10];
            mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};

            float stickerScale = scaleDiagonal / originDiagonal;
            //镜像
            mMatrix.setScale(-1, 1);
            mMatrix.postTranslate(px, 0);
            mMatrix.postScale(stickerScale, stickerScale, mOriginPoints[8], mOriginPoints[9]);

            mMatrix.mapPoints(mPoints, mOriginPoints);

            float centerX = mStickerParam.getZoomCenterX() * displayWidth;
            float centerY = mStickerParam.getZoomCenterY() * displayHeight;
            //对称点翻转
            mMatrix.postTranslate(displayWidth - centerX - mPoints[8], centerY - mPoints[9]);
            //应使用坐标没变的时候的点。。pre。如果是post的话位置就发生变了
            mMatrix.preRotate(mStickerParam.getDegree(), mPoints[8], mPoints[9]);
            postInvalidate();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null || mMatrix == null) {
            return;
        }
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }
}
