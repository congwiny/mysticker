package me.jp.sticker.widget.edit;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import me.jp.sticker.R;
import me.jp.sticker.model.StickerModel;
import me.jp.sticker.util.DisplayUtil;
import me.jp.sticker.util.NinePatchChunk;

/**
 * Created by congwiny on 2016/7/11.
 */
public class EditStickerTextView extends EditStickerView implements StickerInputView.OnStickerTextBGChangedListener {

    private static final String TAG = EditStickerTextView.class.getSimpleName();

    private TextView mTextView;

    private Bitmap mControllerBitmap, mDeleteBitmap;
    private float mControllerWidth, mControllerHeight, mDeleteWidth, mDeleteHeight;

    private Paint mPaint, mBorderPaint;
    private int mToolBarHeight;

    private static final int STICKER_GAP = 50;

    private float[] mOriginPoints;
    private float[] mOriginOuterPoints;
    private float[] mOuterPoints;
    private float[] mPoints;
    private RectF mOriginContentRect;
    private RectF mContentRect;
    private RectF mViewRect;

    private int mScreenWidth;
    private int mScreenHeight;

    private float mLastPointX, mLastPointY;
    private float mInitPointX, mInitPointY;

    private boolean mInController, mInMove;

    private boolean mDrawController = true;

    private float mStickerScaleSize = 1.0f;

    private Matrix mMatrix;

    private boolean mInDelete = false;

    private boolean mInEdit = true;

    public static final float MAX_SCALE_SIZE = 2.0f;
    public static final float MIN_SCALE_SIZE = 0.5f;

    private float mTouchSlop;

    private Paint mContentPaint;

    private int[] STICKER_TEXT_BG = new int[]{
            R.drawable.shape_textview_border,
            R.drawable.txt_bg_black,
            R.drawable.txt_bg_red,
            R.drawable.txt_bg_yellow,
            R.drawable.txt_bg_green,
            R.drawable.txt_bg_blue,
            R.drawable.txt_bg_pink
    };

    private int mStickerTextBGIndex;
    private String mStickerText;

    private OnStickerDeleteListener mOnStickerDeleteListener;

    private OnEditStickerTextClickListener mOnEditStickerTextClickListener;

    public EditStickerTextView(Context context) {
        this(context, null);
    }

    public EditStickerTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditStickerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public int getStickerEditType() {
        return EDIT_TYPE_TEXT;
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
        mStickerText = stickerModel.getStickerText();
        setTextSticker(isInitial);
    }

    @Override
    public void setOnStickerDeleteListener(OnStickerDeleteListener listener) {
        mOnStickerDeleteListener = listener;
    }

    public void setOnEditStickerTextClickListener(OnEditStickerTextClickListener listener) {
        mOnEditStickerTextClickListener = listener;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        mStickerScaleSize = 1.0f;
        mMatrix = new Matrix();
        mContentRect = new RectF();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4.0f);
        mPaint.setColor(Color.WHITE);

        mContentPaint = new Paint(mPaint);
        mContentPaint.setColor(Color.GREEN);

        mBorderPaint = new Paint(mPaint);
        mBorderPaint.setColor(Color.parseColor("#99ffffff"));
        //mBorderPaint.setShadowLayer(DisplayUtil.dip2px(getContext(), 2.0f), 0, 0, Color.parseColor("#33000000"));

        mControllerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_resize);
        mControllerWidth = mControllerBitmap.getWidth();
        mControllerHeight = mControllerBitmap.getHeight();

        mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_delete);
        mDeleteWidth = mDeleteBitmap.getWidth();
        mDeleteHeight = mDeleteBitmap.getHeight();

        mScreenWidth = DisplayUtil.getDisplayWidthPixels(getContext());
        mScreenHeight = DisplayUtil.getDisplayheightPixels(getContext());
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setBackgroundColor(getResources().getColor(R.color.sticker_mask_dark));
    }


    private void setTextSticker(boolean isInitial) {

        setFocusable(true);
        mTextView = new TextView(getContext());
        mTextView.setText(mStickerText);
        mTextView.setTextSize(16);
        mTextView.setMaxWidth((int) (mScreenWidth * 0.8 + 0.5));

        mTextView.setBackgroundDrawable(getResources()
                .getDrawable(STICKER_TEXT_BG[mStickerTextBGIndex]));
        mTextView.setTextColor(Color.WHITE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        mTextView.measure(widthSpec, heightSpec);

        int width = mTextView.getMeasuredWidth();
        int height = mTextView.getMeasuredHeight();
        int px = mTextView.getMeasuredWidth() + 2 * STICKER_GAP;
        int py = mTextView.getMeasuredHeight() + 2 * STICKER_GAP;

        mTextView.layout(0, 0, width, height);

        mOriginContentRect = new RectF(-STICKER_GAP, -STICKER_GAP, px - STICKER_GAP, py - STICKER_GAP);

        mPoints = new float[10];
        mOriginPoints = new float[]{0, 0,
                width, 0,
                width, height,
                0, height,
                width / 2, height / 2};

        mOuterPoints = new float[10];
        mOriginOuterPoints = new float[]{-STICKER_GAP, -STICKER_GAP,
                px - STICKER_GAP, -STICKER_GAP,
                px - STICKER_GAP, py - STICKER_GAP,
                -STICKER_GAP, py - STICKER_GAP,
                width / 2, height / 2};

        if (isInitial) {
            float transtLeft = ((float) mScreenWidth - width) / 2;
            float transtTop = ((float) mScreenHeight - height) / 2;
            //图片通过矩阵进行坐标平移
            mMatrix.postTranslate(transtLeft, transtTop);
        }

        postInvalidate();
    }


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
                mInitPointX = x;
                mInitPointY = y;
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
                if (isPointInMatrix(p, mOuterPoints)) {
                    mLastPointY = y;
                    mLastPointX = x;
                    mInMove = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isInDelete(x, y) && mInDelete) {
                    doDeleteSticker();
                }
                PointF pointF = new PointF(x, y);
                //执行点击事件
                if (isPointInMatrix(pointF, mPoints)) {
                    if (caculateLength(x, y, mInitPointX, mInitPointY) <= mTouchSlop) {
                        if (mOnEditStickerTextClickListener != null) {
                            mOnEditStickerTextClickListener.onEditStickerTextClick(this);
                        }
                    }
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

                    mMatrix.postRotate(rotation(event), mOuterPoints[8], mOuterPoints[9]);
                    float nowLenght = caculateLength(mOuterPoints[0], mOuterPoints[1], mOuterPoints[8], mOuterPoints[9]);
                    float touchLenght = caculateLength(event.getX(), event.getY(), mOuterPoints[8], mOuterPoints[9]);

                    if (Math.sqrt((nowLenght - touchLenght) * (nowLenght - touchLenght)) > 0.0f) {

                        float scale = touchLenght / nowLenght;
                        float nowsc = mStickerScaleSize * scale;
                        if (nowsc >= MIN_SCALE_SIZE && nowsc <= MAX_SCALE_SIZE) {
                            mMatrix.postScale(scale, scale, mOuterPoints[8], mOuterPoints[9]);
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

                    if (Math.sqrt(cX * cX + cY * cY) > 2.0f && canStickerMove(cX, cY)) {
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

    public StickerModel getStickerParam() {
        StickerModel param = new StickerModel();

        param.setDegree(calculateLastDegree());
        double stickerWidth = Math.sqrt(Math.pow(mPoints[2] - mPoints[0], 2) + Math.pow(mPoints[3] - mPoints[1], 2));
        double stickerHeight = Math.sqrt(Math.pow(mPoints[4] - mPoints[2], 2) + Math.pow(mPoints[5] - mPoints[3], 2));
        param.setScaleWidth((float) (stickerWidth / mScreenWidth));
        param.setAspectRatio((float) (stickerWidth / stickerHeight));

        param.setZoomCenterX(mPoints[8] / mScreenWidth);
        param.setZoomCenterY(mPoints[9] / mScreenHeight);
        param.setStickerUrl("");
        param.setStickerText("i love you");
        return param;
    }

    private float calculateLastDegree() {
        double delta_x = mPoints[5] - mPoints[3];
        double delta_y = mPoints[2] - mPoints[4];
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }


    private void doDeleteSticker() {
        setVisibility(View.GONE);
        if (mOnStickerDeleteListener != null) {
            mOnStickerDeleteListener.onDelete(this);
        }
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

    private float caculateLength(float x1, float y1, float x2, float y2) {
        float ex = x1 - x2;
        float ey = y1 - y2;
        return (float) Math.sqrt(ex * ex + ey * ey);
    }

    private float rotation(MotionEvent event) {
        float originDegree = calculateDegree(mLastPointX, mLastPointY);
        float nowDegree = calculateDegree(event.getX(), event.getY());
        return nowDegree - originDegree;
    }


    private float calculateDegree(float x, float y) {
        double delta_x = x - mOuterPoints[8];
        double delta_y = y - mOuterPoints[9];
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private boolean canStickerMove(float cx, float cy) {
        float px = cx + mOuterPoints[8];
        float py = cy + mOuterPoints[9];
        if (mViewRect.contains(px, py)) {
            return true;
        } else {
            return false;
        }
    }


    //button.setBackgroundDrawable(loadNinePatch("/data/data/your.app/files/button_background.9.png", context));

    private Drawable loadNinePatch(String path, Context context) {
        // Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.txt_bg_pink);
        byte[] chunk = bitmap.getNinePatchChunk();
        if (NinePatch.isNinePatchChunk(chunk)) {
            return new NinePatchDrawable(context.getResources(), bitmap, chunk,
                    NinePatchChunk.deserialize(bitmap.getNinePatchChunk()).mPaddings, null);
        } else return new BitmapDrawable(bitmap);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int count = canvas.save();
        canvas.setMatrix(mMatrix);
        mMatrix.mapRect(mContentRect, mOriginContentRect);
        mMatrix.mapPoints(mPoints, mOriginPoints);
        mMatrix.mapPoints(mOuterPoints, mOriginOuterPoints);
        mTextView.draw(canvas);
        canvas.restoreToCount(count);

        if (mInEdit) {
            canvas.drawLine(mOuterPoints[0], mOuterPoints[1],
                    mOuterPoints[2], mOuterPoints[3],
                    mBorderPaint);

            canvas.drawLine(mOuterPoints[0], mOuterPoints[1],
                    mOuterPoints[2], mOuterPoints[3],
                    mBorderPaint);

            canvas.drawLine(mOuterPoints[2], mOuterPoints[3],
                    mOuterPoints[4], mOuterPoints[5],
                    mBorderPaint);

            canvas.drawLine(mOuterPoints[4], mOuterPoints[5],
                    mOuterPoints[6], mOuterPoints[7],
                    mBorderPaint);

            canvas.drawLine(mOuterPoints[6], mOuterPoints[7],
                    mOuterPoints[0], mOuterPoints[1],
                    mBorderPaint);

            canvas.drawBitmap(mDeleteBitmap,
                    mOuterPoints[0] - mControllerWidth / 2,
                    mOuterPoints[1] - mControllerHeight / 2,
                    mBorderPaint);

            canvas.drawBitmap(mControllerBitmap,
                    mOuterPoints[4] - mControllerWidth / 2,
                    mOuterPoints[5] - mControllerHeight / 2,
                    mBorderPaint);
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private boolean isInDelete(float x, float y) {
        int position = 0;
        //while (position < 8) {
        float rx = mOuterPoints[position];
        float ry = mOuterPoints[position + 1];
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

    private boolean isInController(float x, float y) {
        int position = 4;
        //while (position < 8) {
        float rx = mOuterPoints[position];
        float ry = mOuterPoints[position + 1];
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

    @Override
    public void onStickerTextBGChanged(int bgIndex) {
        mInEdit = true;
        mStickerTextBGIndex = bgIndex;
    }

    public interface OnEditStickerTextClickListener {
        void onEditStickerTextClick(EditStickerTextView editStickerTextView);
    }
}
