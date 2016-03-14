package com.agenciaflex.ruufpi.listener;

import android.view.MotionEvent;

/**
 * Created by alanssantos on 4/15/15.
 */
public class GestureDetector extends android.view.GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }
}
