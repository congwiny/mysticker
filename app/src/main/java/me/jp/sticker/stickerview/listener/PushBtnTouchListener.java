package me.jp.sticker.stickerview.listener;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PushBtnTouchListener implements View.OnTouchListener {

    private final String TAG = PushBtnTouchListener.class.getSimpleName();
    Point pushPoint;
    int lastResizeImgWidth;
    int lastResizeImgHeight;
    int lastResizeImgLeft;
    int lastResizeImgTop;
    int lastResizeImgAngle;
    double lastComAngle;

    int pushImgWidth;
    int pushImgHeight;

    int lastPushBtnLeft;
    int lastPushBtnTop;

    private View mResizeView;
    private Point mResizeViewCenter;
    private float mRotateDegree;
    private static final double PI = 3.14159265359;

    public PushBtnTouchListener(View mView) {
        this.mResizeView = mView;
    }

    private RelativeLayout.LayoutParams pushBtnLP;
    private RelativeLayout.LayoutParams contentImgLP;
    float lastX = -1;
    float lastY = -1;

    private float mTextSize = 0;

    @Override
    public boolean onTouch(View pushView, MotionEvent event) {
        Log.i(this.toString(), "onTouch action = " + event.getAction());
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 主点按下
            case MotionEvent.ACTION_DOWN:
                pushBtnLP = (RelativeLayout.LayoutParams) pushView
                        .getLayoutParams();
                contentImgLP = (RelativeLayout.LayoutParams) mResizeView.getLayoutParams();

                pushPoint = getPushPoint(pushBtnLP, event);
                lastResizeImgWidth = contentImgLP.width;
                lastResizeImgHeight = contentImgLP.height;
                lastResizeImgLeft = contentImgLP.leftMargin;
                lastResizeImgTop = contentImgLP.topMargin;
                lastResizeImgAngle = (int) mResizeView.getRotation();

                lastPushBtnLeft = pushBtnLP.leftMargin;
                lastPushBtnTop = pushBtnLP.topMargin;

                pushImgWidth = pushBtnLP.width;
                pushImgHeight = pushBtnLP.height;
                lastX = event.getRawX();
                lastY = event.getRawY();
                refreshImageCenter();
                break;
            // 副点按下
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_UP: {
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                if (lastX != -1) {
                    if (Math.abs(rawX - lastX) < 5
                            && Math.abs(rawY - lastY) < 5) {
                        return false;
                    }
                }
                lastX = rawX;
                lastY = rawY;

                Point O = mResizeViewCenter,
                        A = pushPoint,
                        B = getPushPoint(pushBtnLP, event);
                float dOA = getDistance(O, A);
                float dOB = getDistance(O, B);
                float f = dOB / dOA;

                int newWidth = (int) (lastResizeImgWidth * f);
                int newHeight = (int) (lastResizeImgHeight * f);

                contentImgLP.leftMargin = lastResizeImgLeft
                        - ((newWidth - lastResizeImgWidth) / 2);
                contentImgLP.topMargin = lastResizeImgTop
                        - ((newHeight - lastResizeImgHeight) / 2);
                contentImgLP.width = newWidth;
                contentImgLP.height = newHeight;
                mResizeView.setLayoutParams(contentImgLP);
                if (mResizeView instanceof TextView){
                    TextView tv = (TextView) mResizeView;
                    float textSize = tv.getTextSize();
                    tv.setTextSize(12*f);
                }

                float fz = (((A.x - O.x) * (B.x - O.x)) + ((A.y - O.y) * (B.y - O.y)));
                float fm = dOA * dOB;
                double comAngle = (180 * Math.acos(fz / fm) / PI);
                if (Double.isNaN(comAngle)) {
                    comAngle = (lastComAngle < 90 || lastComAngle > 270)
                            ? 0
                            : 180;
                } else if ((B.y - O.y) * (A.x - O.x) < (A.y - O.y)
                        * (B.x - O.x)) {
                    comAngle = 360 - comAngle;
                }
                lastComAngle = comAngle;

                float angle = (float) (lastResizeImgAngle + comAngle);
                angle = angle % 360;
                mRotateDegree = angle;
                mResizeView.setRotation(angle);
                Log.e(TAG,"rotate angle="+angle);

                //right bottom
                Point imageRB = new Point(mResizeView.getLeft() + mResizeView.getWidth(),
                        mResizeView.getTop() + mResizeView.getHeight());

                Point anglePoint = getAnglePoint(O, imageRB, angle);

                pushBtnLP.leftMargin = (int) (anglePoint.x - pushImgWidth / 2);
                pushBtnLP.topMargin = (int) (anglePoint.y - pushImgHeight / 2);
                pushView.setLayoutParams(pushBtnLP);
                break;
        }
        return true;
    }

    private void refreshImageCenter() {
        int x = mResizeView.getLeft() + mResizeView.getWidth() / 2;
        int y = mResizeView.getTop() + mResizeView.getHeight() / 2;
        mResizeViewCenter = new Point(x, y);
    }

    private Point getPushPoint(RelativeLayout.LayoutParams lp, MotionEvent event) {
        return new Point(lp.leftMargin + (int) event.getX(), lp.topMargin
                + (int) event.getY());
    }

    private float getDistance(Point a, Point b) {
        float v = ((a.x - b.x) * (a.x - b.x)) + ((a.y - b.y) * (a.y - b.y));
        return ((int) (Math.sqrt(v) * 100)) / 100f;
    }

    private Point getAnglePoint(Point O, Point A, float angle) {
        int x, y;
        float dOA = getDistance(O, A);
        double p1 = angle * PI / 180f;
        double p2 = Math.acos((A.x - O.x) / dOA);
        x = (int) (O.x + dOA * Math.cos(p1 + p2));

        double p3 = Math.acos((A.x - O.x) / dOA);
        y = (int) (O.y + dOA * Math.sin(p1 + p3));
        return new Point(x, y);
    }

    public double getRotateDegree() {
        return mRotateDegree;
    }

}
