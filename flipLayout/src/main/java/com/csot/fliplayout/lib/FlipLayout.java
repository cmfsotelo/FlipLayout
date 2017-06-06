package com.csot.fliplayout.lib;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.csot.fliplayout.R;

//public class FlipLayout extends ConstraintLayout {
public class FlipLayout extends FrameLayout {
    public static final int FADE = 0;
    public static final int FLIP_X = 1;
    public static final int FLIP_Y = 2;
    public static final int TRANSLATE_X = 3;
    public static final int TRANSLATE_Y = 4;
    public static final int FLIP_Z1 = 5;
    public static final int FLIP_Z2 = 6;
    int visibleChild = 0;
    int transitionDuration = 200;
    int transition = FLIP_Y;
    Interpolator interpolator;
    boolean isAnimating;
    ViewPropertyAnimator fromViewPropertyAnimator;
    ViewPropertyAnimator toViewPropertyAnimator;

    public FlipLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public FlipLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlipLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            //GET XML ATTRIBUTES
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlipLayout, defStyleAttr, 0);
            visibleChild = a.getInteger(R.styleable.FlipLayout_startingChild, visibleChild);
            transitionDuration = a.getInteger(R.styleable.FlipLayout_transitionDuration, transitionDuration);
            transition = a.getInteger(R.styleable.FlipLayout_transition, transition);
            a.recycle();
        }
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        int i;
        if (index == -1) {
            i = getChildCount() - 1;
        } else {
            i = index;
        }
        child.setVisibility(i == visibleChild ? VISIBLE : INVISIBLE);
        child.setClipToOutline(false);
    }

    /**
     * Shows the child next to the one being shown, animating it. If the current child is the last one, the first child shown.
     */
    public void showNextChild() {
        if (!isAnimating() && getChildCount() > 1) {
            int childIt = (visibleChild + 1 > getChildCount() - 1) ? 0 : visibleChild + 1;
            View vFrom = getChildAt(visibleChild);
            View vTo = getChildAt(childIt);
//            if ((vFrom.getAnimation() != null && !vFrom.getAnimation().hasEnded()) ||
//                    (vTo.getAnimation() != null && !vTo.getAnimation().hasEnded())) {
//                return;
//            }
            playTransitAnimation(vFrom, vTo, false);
            visibleChild = childIt;
        }
    }

    /**
     * Shows the child previous to the one being shown, animating it. If the current child is the first one, the last child shown.
     */
    public void showPreviousChild() {
        if (!isAnimating() && getChildCount() > 1) {
            int childIt = (visibleChild - 1 < 0) ? getChildCount() - 1 : visibleChild - 1;
            playTransitAnimation(getChildAt(visibleChild), getChildAt(childIt), true);
            visibleChild = childIt;
        }
    }

    /**
     * Sets the target child as the visible one. Can be animated or not. When animated, it animates from the current visible child immediately to the target one.
     *
     * @param targetChild - the target child
     * @param animate     - flag to make the transition animating it or not
     */
    public void showChild(int targetChild, boolean animate) {
        if (animate && !isAnimating() && getChildCount() > 1) {
            View vFrom = getChildAt(visibleChild);
            View vTo = getChildAt(targetChild);
            playTransitAnimation(vFrom, vTo, false);
            visibleChild = targetChild;
        } else {
            cancelAnimations();
            setVisibleChild(targetChild);
        }
    }

    private void cancelAnimations() {
        if (toViewPropertyAnimator != null) {
            toViewPropertyAnimator.cancel();
        }
        if (fromViewPropertyAnimator != null) {
            fromViewPropertyAnimator.cancel();
        }
    }

    private boolean isAnimating() {
        return isAnimating;
    }

    private void playTransitAnimation(final View fromView, final View toView, int transition, final boolean inverse) {
        fromView.setVisibility(VISIBLE);
        toView.setVisibility(INVISIBLE);
        final int rot = inverse ? 90 : -90;
        cancelAnimations();
        switch (transition) {
            case FADE:
                fromView.setAlpha(1);
                toView.setAlpha(0);
                toView.setVisibility(VISIBLE);
                fromViewPropertyAnimator = fromView.animate();
                fromViewPropertyAnimator.alpha(0).setDuration(transitionDuration).setInterpolator(interpolator).setListener(new ViewAnimation(toView, INVISIBLE));
                toViewPropertyAnimator = toView.animate();
                toViewPropertyAnimator.alpha(1).setDuration(transitionDuration).setInterpolator(interpolator).setListener(new ViewAnimation(toView, VISIBLE));
                break;
            case FLIP_X:
                fromView.setRotationX(0);
                toView.setRotationX(-rot);
                fromViewPropertyAnimator = fromView.animate();
                fromViewPropertyAnimator.rotationX(rot).setDuration(transitionDuration / 2).setInterpolator(interpolator).setListener(new ViewAnimation(fromView, INVISIBLE) {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        toViewPropertyAnimator = toView.animate();
                        toViewPropertyAnimator.rotationX(0).setDuration(transitionDuration / 2).setInterpolator(interpolator).setListener(new ViewAnimation(toView, VISIBLE));
                    }
                });
                break;
            case FLIP_Y:
                fromView.setRotationY(0);
                toView.setRotationY(-rot);
                fromViewPropertyAnimator = fromView.animate();
                fromViewPropertyAnimator.rotationY(rot).setDuration(transitionDuration / 2).setInterpolator(interpolator).setListener(new ViewAnimation(fromView, INVISIBLE) {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        toViewPropertyAnimator = toView.animate();
                        toViewPropertyAnimator.rotationY(0).setDuration(transitionDuration / 2).setInterpolator(interpolator).setListener(new ViewAnimation(toView, VISIBLE));
                    }
                });
                break;
            case FLIP_Z1:
                fromView.setPivotY(0 - fromView.getPaddingTop());
                fromView.setRotation(0);
                toView.setPivotY(0 - toView.getPaddingTop());
                toView.setRotation(-rot * 2);
                fromViewPropertyAnimator = fromView.animate();
                fromViewPropertyAnimator.rotation(rot * 2).setDuration(transitionDuration / 2).setInterpolator(interpolator).setListener(new ViewAnimation(fromView, INVISIBLE) {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        toViewPropertyAnimator = toView.animate();
                        toViewPropertyAnimator.rotation(0).setDuration(transitionDuration / 2).setInterpolator(interpolator).setListener(new ViewAnimation(toView, VISIBLE));
                    }
                });
                break;
            case FLIP_Z2:
                fromView.setPivotY(fromView.getHeight() / 2);
                fromView.setPivotX(0 - fromView.getPaddingStart() - fromView.getWidth() / 2);
                fromView.setRotation(0);
                toView.setPivotY(toView.getHeight() / 2);
                toView.setPivotX(0 - toView.getPaddingStart() - toView.getWidth() / 2);
                toView.setRotation(-rot);
                fromViewPropertyAnimator = fromView.animate();
                fromViewPropertyAnimator.rotation(rot).setDuration(transitionDuration).setInterpolator(interpolator).setListener(new ViewAnimation(fromView, INVISIBLE));
                toViewPropertyAnimator = toView.animate();
                toViewPropertyAnimator.rotation(0).setDuration(transitionDuration).setInterpolator(interpolator).setListener(new ViewAnimation(toView, VISIBLE));
                break;
            case TRANSLATE_X:
                final float width = !inverse ? -getWidth() : getWidth();
                fromView.setTranslationX(0);
                toView.setTranslationX(-width);
                fromViewPropertyAnimator = fromView.animate();
                fromViewPropertyAnimator.translationXBy(width).setDuration(transitionDuration).setInterpolator(interpolator).setListener(new ViewAnimation(fromView, INVISIBLE));
                toViewPropertyAnimator = toView.animate();
                toViewPropertyAnimator.translationXBy(width).setDuration(transitionDuration).setInterpolator(interpolator).setListener(new ViewAnimation(toView, VISIBLE));
                break;
            case TRANSLATE_Y:
                final float height = !inverse ? -getHeight() : getHeight();
                fromView.setTranslationY(0);
                toView.setTranslationY(-height);
                fromViewPropertyAnimator = fromView.animate();
                fromViewPropertyAnimator.translationYBy(height).setDuration(transitionDuration).setInterpolator(interpolator).setListener(new ViewAnimation(fromView, INVISIBLE));
                toViewPropertyAnimator = toView.animate();
                toViewPropertyAnimator.translationYBy(height).setDuration(transitionDuration).setInterpolator(interpolator).setListener(new ViewAnimation(toView, VISIBLE));
                break;
        }
    }

    private void playTransitAnimation(final View fromView, final View toView, final boolean inverse) {
        playTransitAnimation(fromView, toView, transition, inverse);
    }

    public int getVisibleChild() {
        return visibleChild;
    }

    public void setVisibleChild(int visibleChild) {
        this.visibleChild = visibleChild;
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(i == visibleChild ? VISIBLE : INVISIBLE);
        }
    }

    public int getTransitionDuration() {
        return transitionDuration;
    }

    public void setTransitionDuration(int transitionDuration) {
        this.transitionDuration = transitionDuration;
    }

    public int getTransition() {
        return transition;
    }

    public void setTransition(int transition) {
        this.transition = transition;
    }

    private class ViewAnimation implements Animator.AnimatorListener {
        View v;
        int endVisibility;

        public ViewAnimation(View v, int endVisibility) {
            this.v = v;
            this.endVisibility = endVisibility;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            v.setVisibility(VISIBLE);
            isAnimating = true;
        }

        private void restoreView(View v) {
            v.setVisibility(endVisibility);
            v.setPivotY(v.getHeight() / 2);
            v.setPivotX(v.getWidth() / 2);
            v.setRotation(0);
            v.setRotationX(0);
            v.setRotationY(0);
            v.setTranslationX(0);
            v.setTranslationY(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                v.setTranslationZ(0);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            restoreView(v);
            isAnimating = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            restoreView(v);
            isAnimating = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
