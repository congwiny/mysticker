package me.jp.sticker.stickerview.listener;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import me.jp.sticker.stickerview.view.StickerEditView;

/**
 * Created by congwiny on 2016/12/5.
 */

public class ResizeOnTouchListener implements View.OnTouchListener {
    //需要被放大缩小的view，后面可以加个集合什么的，现在先对图片处理
    private StickerEditView mStickerView;
    private final String TAG = ResizeOnTouchListener.class.getSimpleName();

    private PointF mLastTouchRawPoint;

    private PointF mStickerCenter;
    private float mStickerLastAngle;

    private float mStickerScaleSize = 1.0f;
    public static final float MAX_SCALE_SIZE = 1.5f;
    public static final float MIN_SCALE_SIZE = 0.5f;

    private int mLastStickerContentW;
    private int mLastStickerContentH;


    private RelativeLayout.LayoutParams mStickerViewLp;
    private RelativeLayout.LayoutParams mStickerImgContentLp;
    private int mLastStickerImgLeft;
    private int mLastStickerImgTop;

    private int mLastStickerViewLeft;
    private int mLastStickerViewTop;

    private float mOriginLength;

    public ResizeOnTouchListener(StickerEditView stickerEditView) {
        this.mStickerView = stickerEditView;
        mStickerCenter = new PointF();
        mLastTouchRawPoint = new PointF();
    }


    @Override
    public boolean onTouch(View touchView, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                mStickerViewLp = (RelativeLayout.LayoutParams) mStickerView.getLayoutParams();
                mStickerImgContentLp = (RelativeLayout.LayoutParams) mStickerView.getImageContentView().getLayoutParams();

                mStickerCenter.x = mStickerViewLp.leftMargin + mStickerView.getWidth() / 2;
                mStickerCenter.y = mStickerViewLp.topMargin + mStickerView.getHeight() / 2;
                Log.e(TAG, "centerX=" + mStickerCenter.x + ",centerY=" + mStickerCenter.y
                        + ",width=" + mStickerView.getWidth()
                        + ",height=" + mStickerView.getHeight());
                mLastTouchRawPoint.x = event.getRawX();
                mLastTouchRawPoint.y = event.getRawY();
                mStickerLastAngle = mStickerView.getRotation();
                mLastStickerContentW = mStickerView.getImageContentView().getWidth();
                mLastStickerContentH = mStickerView.getImageContentView().getHeight();
                mLastStickerImgLeft = mStickerViewLp.leftMargin;
                mLastStickerImgTop = mStickerViewLp.topMargin;

                mLastStickerViewLeft = mStickerViewLp.leftMargin;
                mLastStickerViewTop = mStickerViewLp.topMargin;

                if (mOriginLength <= 0) {
                    //计算原始值
                    mOriginLength = calculateLength(mLastStickerViewLeft, mLastStickerViewTop);
                }
                break;
            case MotionEvent.ACTION_MOVE:

                float degree = rotation(event);
                if (Math.abs(degree) > 1.0f) {
                    //设置旋转角度
                    mStickerView.setRotation(mStickerLastAngle + degree);
                    mStickerView.getDeleteView().setRotation(-(mStickerLastAngle + degree));
                    mStickerView.getResizeView().setRotation(-(mStickerLastAngle + degree));

                }

                //不断更新
                float nowLength = calculateLength(mLastTouchRawPoint.x, mLastTouchRawPoint.y);
                float touchLength = calculateLength(event.getRawX(), event.getRawY());

                float scale = touchLength / nowLength;

                int newWidth = (int) (mLastStickerContentW * scale);
                int newHeight = (int) (mLastStickerContentH * scale);
                //计算对角线,坑死爹用了好久
                float newLength = (float) (Math.sqrt(newWidth * newWidth + newHeight * newHeight) / 2);
                float rateScale = newLength / mOriginLength;
                if (rateScale >= MIN_SCALE_SIZE && rateScale < MAX_SCALE_SIZE) {
                    mStickerImgContentLp.width = newWidth;
                    mStickerImgContentLp.height = newHeight;
                    //设置sticker内容缩放
                    mStickerView.getImageContentView().setLayoutParams(mStickerImgContentLp);
                    //设置外部边框
                    mStickerViewLp = (RelativeLayout.LayoutParams) mStickerView.getLayoutParams();
                    mStickerViewLp.leftMargin = mLastStickerViewLeft
                            - ((newWidth - mLastStickerContentW) / 2);
                    mStickerViewLp.topMargin = mLastStickerViewTop
                            - ((newHeight - mLastStickerContentH) / 2);
                    mStickerView.setLayoutParams(mStickerViewLp);
                    mStickerScaleSize = rateScale;
                }

                break;
        }
        return true;
    }

    private float rotation(MotionEvent event) {
        float originDegree = calculateDegree(mLastTouchRawPoint.x, mLastTouchRawPoint.y);
        float nowDegree = calculateDegree(event.getRawX(), event.getRawY());
        return nowDegree - originDegree;
    }

    private float calculateDegree(float x, float y) {
        double delta_x = x - mStickerCenter.x;
        double delta_y = y - mStickerCenter.y;
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }


    private float calculateLength(float x, float y) {
        float ex = x - mStickerCenter.x;
        float ey = y - mStickerCenter.y;
        return (float) Math.sqrt(ex * ex + ey * ey);
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    private RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }
}
