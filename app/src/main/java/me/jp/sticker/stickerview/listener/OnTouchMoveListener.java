package me.jp.sticker.stickerview.listener;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by congwiny on 2016/12/5.
 */

public class OnTouchMoveListener implements View.OnTouchListener {
    //位移view触摸的位置
    private Point mTouchViewPoint;

    private int mTouchViewLastLeft;
    private int mTouchViewLastTop;

    private int mMoveViewLastLeft;
    private int mMoveViewLastTop;

    RelativeLayout.LayoutParams mTouchViewLp;
    RelativeLayout.LayoutParams mMoveViewLp;

    //触摸移动的view
    private View mMoveView;

    public OnTouchMoveListener(View moveView) {
        this.mMoveView = moveView;
    }

    @Override
    public boolean onTouch(View touchView, MotionEvent event) {

        switch (event.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                if (mMoveViewLp==null){
                    mMoveViewLp = (RelativeLayout.LayoutParams) mMoveView.getLayoutParams();
                }
                mMoveViewLastLeft = mMoveViewLp.leftMargin;
                mMoveViewLastTop = mMoveViewLp.topMargin;

                if (mTouchViewLp==null){
                    mTouchViewLp = (RelativeLayout.LayoutParams) touchView.getLayoutParams();
                }
                mTouchViewLastLeft = mTouchViewLp.leftMargin;
                mTouchViewLastTop = mTouchViewLp.topMargin;
                mTouchViewPoint = getRawPoint(event);

                break;
            case MotionEvent.ACTION_MOVE:
                Point newTouchViewPoint = getRawPoint(event);
                float moveX = newTouchViewPoint.x-mTouchViewPoint.x;
                float moveY = newTouchViewPoint.y - mTouchViewPoint.y;

                mMoveViewLp.leftMargin = (int) (mMoveViewLastLeft+moveX);
                mMoveViewLp.topMargin = (int) (mMoveViewLastTop+moveY);
                mMoveView.setLayoutParams(mMoveViewLp);
                break;
        }

        return true;
    }

    private Point getRawPoint(MotionEvent event) {
        return new Point((int) event.getRawX(), (int) event.getRawY());
    }
}
