package com.alivc.utilssample.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by qianhao on 2017/8/1.
 */

public class UnderView extends View {
    private float mStartX = 0;
    private View     mMoveView;
    private Activity activity;

    public UnderView(Context context) {
        super(context);
    }

    public UnderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UnderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindView(View v, Activity ac) {
        this.mMoveView = v;
        this.activity = ac;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float nx = event.getX();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = nx;
                onAnimationEnd();
            case MotionEvent.ACTION_MOVE:
                handleMoveView(nx);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                doTriggerEvent(nx);
                break;
        }
        return true;
    }

    private void handleMoveView(float x) {
        float movex = x - mStartX;
        if (movex < 0) movex = 0;

        mMoveView.setTranslationX(movex);

        float mWidthFloat = getWidth();
        if (getBackground() != null) {
            getBackground().setAlpha((int) ((mWidthFloat - mMoveView.getTranslationX()) / mWidthFloat * 200));
        }
    }

    private void doTriggerEvent(float x) {
        float movex = x - mStartX;
        if (movex > (movex * 0.4)) {
            moveMoveView(getWidth() - mMoveView.getLeft(), true);
        } else {
            moveMoveView(-mMoveView.getLeft(), false);
        }
    }

    private void moveMoveView(float to, boolean exit) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mMoveView, "translationX", to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (getBackground() != null) {
                    getBackground().setAlpha((int) ((getWidth() - mMoveView.getTranslationX()) / getWidth() * 200));
                }
            }
        });
        animator.setDuration(250).start();
        if (exit) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    activity.finish();

                    super.onAnimationEnd(animation);
                }
            });
        }

    }

}
