package me.jp.sticker.widget.edit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;

import me.jp.sticker.R;
import me.jp.sticker.model.StickerModel;
import me.jp.sticker.util.DisplayUtil;


/**
 * Created by congwiny on 2016/7/11.
 */
public class EditStickerIconView extends EditStickerView {

    private static final String TAG = EditStickerIconView.class.getSimpleName();

    public static final float MAX_SCALE_SIZE = 2.0f;
    public static final float MIN_SCALE_SIZE = 0.5f;

    private float[] mOriginPoints;
    private float[] mPoints;
    private float[] mOriginCenterPoints;
    private RectF mOriginContentRect;
    private RectF mContentRect;
    private RectF mViewRect;

    private float mLastPointX, mLastPointY;

    private Bitmap mBitmap;
    private Matrix mMatrix;
    private Paint mPaint, mBorderPaint;
    private Bitmap mControllerBitmap, mDeleteBitmap;
    private float mControllerWidth, mControllerHeight, mDeleteWidth, mDeleteHeight;
    private boolean mInController, mInMove;

    private boolean mDrawController = true;
    //private boolean mCanTouch;
    private float mStickerScaleSize = 1.0f;

    private boolean mInEdit = true;

    private OnStickerDeleteListener mOnStickerDeleteListener;

    public EditStickerIconView(Context context) {
        this(context, null);
    }

    public EditStickerIconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditStickerIconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public int getStickerEditType() {
        return EDIT_TYPE_ICON;
    }

    @Override
    public void applySticker() {
        mInEdit = false;
        setFocusable(false);
        setBackgroundColor(getResources().getColor(R.color.sticker_mask_transparent));
        invalidate();
    }

    @Override
    public void editSticker(StickerModel stickerModel, boolean isInitial) {
        mInEdit = true;
        setFocusable(true);
        invalidate();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4.0f);
        mPaint.setColor(Color.WHITE);

        mBorderPaint = new Paint(mPaint);
        mBorderPaint.setColor(Color.parseColor("#99ffffff"));
        //mBorderPaint.setShadowLayer(DisplayUtil.dip2px(getContext(), 2.0f), 0, 0, Color.parseColor("#33000000"));

        mControllerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_resize);
        mControllerWidth = mControllerBitmap.getWidth();
        mControllerHeight = mControllerBitmap.getHeight();

        mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_delete);
        mDeleteWidth = mDeleteBitmap.getWidth();
        mDeleteHeight = mDeleteBitmap.getHeight();

        setBackgroundColor(getResources().getColor(R.color.sticker_mask_dark));

    }

    public void setWaterMark(@NonNull Bitmap bitmap) {
        mBitmap = bitmap;
        mStickerScaleSize = 1.0f;


        setFocusable(true);
        try {


            float px = mBitmap.getWidth();
            float py = mBitmap.getHeight();


            //mOriginPoints = new float[]{px, py, px + bitmap.getWidth(), py, bitmap.getWidth() + px, bitmap.getHeight() + py, px, py + bitmap.getHeight()};
            mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};
            mOriginContentRect = new RectF(0, 0, px, py);
            mOriginCenterPoints = new float[]{px / 2, py / 2};

            mPoints = new float[10];
            mContentRect = new RectF();

            mMatrix = new Matrix();
            float transtLeft = ((float) DisplayUtil.getDisplayWidthPixels(getContext()) - mBitmap.getWidth()) / 2;
            float transtTop = ((float) DisplayUtil.getDisplayWidthPixels(getContext()) - mBitmap.getHeight()) / 2;
            //图片通过矩阵进行坐标平移
            mMatrix.postTranslate(transtLeft, transtTop);

        } catch (Exception e) {
            e.printStackTrace();
        }
        postInvalidate();

    }

    public Matrix getMarkMatrix() {
        return mMatrix;
    }

    public StickerModel getStickerParam() {
        StickerModel param = new StickerModel();

        param.setDegree(calculateLastDegree());

        double stickerWidth = Math.sqrt(Math.pow(mPoints[2] - mPoints[0], 2) + Math.pow(mPoints[3] - mPoints[1], 2));
        double stickerHeight = Math.sqrt(Math.pow(mPoints[4] - mPoints[2], 2) + Math.pow(mPoints[5] - mPoints[3], 2));
        param.setScaleWidth((float) (stickerWidth / DisplayUtil.getDisplayWidthPixels(getContext())));
        param.setAspectRatio((float) (stickerWidth / stickerHeight));

        param.setZoomCenterX(mPoints[8] / DisplayUtil.getDisplayWidthPixels(getContext()));
        param.setZoomCenterY(mPoints[9] / DisplayUtil.getDisplayheightPixels(getContext()));
        param.setStickerUrl("");
        return param;
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null || mMatrix == null) {
            return;
        }

        //变换后的点
        mMatrix.mapPoints(mPoints, mOriginPoints);


        mMatrix.mapRect(mContentRect, mOriginContentRect);

        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        float array[] = new float[9];
        mMatrix.getValues(array);
        Log.e(TAG, "mMatrix" + Arrays.toString(array));
        if (mDrawController && isFocusable()) {
            canvas.drawLine(mPoints[0], mPoints[1], mPoints[2], mPoints[3], mBorderPaint);
            canvas.drawLine(mPoints[2], mPoints[3], mPoints[4], mPoints[5], mBorderPaint);
            canvas.drawLine(mPoints[4], mPoints[5], mPoints[6], mPoints[7], mBorderPaint);
            canvas.drawLine(mPoints[6], mPoints[7], mPoints[0], mPoints[1], mBorderPaint);
            canvas.drawBitmap(mControllerBitmap, mPoints[4] - mControllerWidth / 2, mPoints[5] - mControllerHeight / 2, mBorderPaint);
            canvas.drawBitmap(mDeleteBitmap, mPoints[0] - mDeleteWidth / 2, mPoints[1] - mDeleteHeight / 2, mBorderPaint);
        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mDrawController = false;
        draw(canvas);
        mDrawController = true;
        canvas.save();
        return bitmap;
    }

    public void setShowDrawController(boolean show) {
        mDrawController = show;
    }


    private boolean isInController(float x, float y) {
        int position = 4;
        //while (position < 8) {
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mControllerWidth / 2,
                ry - mControllerHeight / 2,
                rx + mControllerWidth / 2,
                ry + mControllerHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        //   position += 2;
        //}
        return false;

    }

    private boolean isInDelete(float x, float y) {
        int position = 0;
        //while (position < 8) {
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mDeleteWidth / 2,
                ry - mDeleteHeight / 2,
                rx + mDeleteWidth / 2,
                ry + mDeleteHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        //   position += 2;
        //}
        return false;

    }


    private boolean mInDelete = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isFocusable()) {
            return super.dispatchTouchEvent(event);
        }
        if (mViewRect == null) {
            mViewRect = new RectF(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInController(x, y)) {
                    mInController = true;
                    mLastPointY = y;
                    mLastPointX = x;
                    break;
                }

                if (isInDelete(x, y)) {
                    mInDelete = true;
                    break;
                }

                PointF p = new PointF(x, y);
                if (isPointInMatrix(p, mPoints)) {
                    mLastPointY = y;
                    mLastPointX = x;
                    mInMove = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isInDelete(x, y) && mInDelete) {
                    doDeleteSticker();
                }
            case MotionEvent.ACTION_CANCEL:
                mLastPointX = 0;
                mLastPointY = 0;
                mInController = false;
                mInMove = false;
                mInDelete = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mInController) {

                    mMatrix.postRotate(rotation(event), mPoints[8], mPoints[9]);
                    float nowLenght = caculateLength(mPoints[0], mPoints[1]);
                    float touchLenght = caculateLength(event.getX(), event.getY());
                    if (FloatMath.sqrt((nowLenght - touchLenght) * (nowLenght - touchLenght)) > 0.0f) {
                        float scale = touchLenght / nowLenght;
                        float nowsc = mStickerScaleSize * scale;
                        if (nowsc >= MIN_SCALE_SIZE && nowsc <= MAX_SCALE_SIZE) {
                            mMatrix.postScale(scale, scale, mPoints[8], mPoints[9]);
                            mStickerScaleSize = nowsc;
                        }
                    }

                    invalidate();
                    mLastPointX = x;
                    mLastPointY = y;
                    break;

                }

                if (mInMove == true) { //拖动的操作
                    float cX = x - mLastPointX;
                    float cY = y - mLastPointY;
                    mInController = false;
                    //Log.i("MATRIX_OK", "ma_jiaodu:" + a(cX, cY));

                    if (FloatMath.sqrt(cX * cX + cY * cY) > 2.0f && canStickerMove(cX, cY)) {
                        //Log.i("MATRIX_OK", "is true to move");
                        mMatrix.postTranslate(cX, cY);
                        postInvalidate();
                        mLastPointX = x;
                        mLastPointY = y;
                    }
                    break;
                }


                return true;

        }
        return true;
    }


    private float getCross(PointF p1, PointF p2, PointF p) {
        return (p2.x - p1.x) * (p.y - p1.y) - (p.x - p1.x) * (p2.y - p1.y);
    }

    private boolean isPointInMatrix(PointF p, float[] target) {
        PointF p1 = new PointF(target[0], target[1]);
        PointF p2 = new PointF(target[6], target[7]);
        PointF p3 = new PointF(target[4], target[5]);
        PointF p4 = new PointF(target[2], target[3]);
        return getCross(p1, p2, p) * getCross(p3, p4, p) >= 0 && getCross(p2, p3, p) * getCross(p4, p1, p) >= 0;
    }

    private void doDeleteSticker() {
        setVisibility(View.GONE);
        if (mOnStickerDeleteListener != null) {
            mOnStickerDeleteListener.onDelete(this);
        }
    }

    private boolean canStickerMove(float cx, float cy) {
        float px = cx + mPoints[8];
        float py = cy + mPoints[9];
        if (mViewRect.contains(px, py)) {
            return true;
        } else {
            return false;
        }
    }


    private float caculateLength(float x, float y) {
        float ex = x - mPoints[8];
        float ey = y - mPoints[9];
        return FloatMath.sqrt(ex * ex + ey * ey);
    }


    private float rotation(MotionEvent event) {
        float originDegree = calculateDegree(mLastPointX, mLastPointY);
        float nowDegree = calculateDegree(event.getX(), event.getY());
        return nowDegree - originDegree;
    }

    private float calculateDegree(float x, float y) {
        double delta_x = x - mPoints[8];
        double delta_y = y - mPoints[9];
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private float calculateLastDegree() {
        double delta_x = mPoints[5] - mPoints[3];
        double delta_y = mPoints[2] - mPoints[4];
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public RectF getContentRect() {
        return mContentRect;
    }

    @Override
    public void setOnStickerDeleteListener(OnStickerDeleteListener listener) {
        mOnStickerDeleteListener = listener;
    }
}
