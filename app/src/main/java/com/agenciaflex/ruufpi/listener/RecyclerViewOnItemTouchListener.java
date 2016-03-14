package com.agenciaflex.ruufpi.listener;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.agenciaflex.ruufpi.MainActivity;
import com.agenciaflex.ruufpi.SobreActivity;
import com.agenciaflex.ruufpi.UnidadesActivity;
import com.agenciaflex.ruufpi.util.RuUfpiHelper;

/**
 * Created by alanssantos on 4/15/15.
 */
public class RecyclerViewOnItemTouchListener implements RecyclerView.OnItemTouchListener {

    private android.view.GestureDetector mGestureDetector;
    private DrawerLayout Drawer;
    private Context context;
    private int index = 0;

    public RecyclerViewOnItemTouchListener(Context context, DrawerLayout Drawer, int index){
        mGestureDetector = new android.view.GestureDetector(context, new com.agenciaflex.ruufpi.listener.GestureDetector());
        this.Drawer = Drawer;
        this.context = context;
        this.index = index;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

        if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
            if(recyclerView.getChildLayoutPosition(child) > 0 && recyclerView.getChildLayoutPosition(child) != index)
                Drawer.closeDrawers();

            if(recyclerView.getChildLayoutPosition(child) != index) {
                if (recyclerView.getChildLayoutPosition(child) == RuUfpiHelper.CARDAPIO_INDEX) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }

                else if (recyclerView.getChildLayoutPosition(child) == RuUfpiHelper.UNIDADE_INDEX_) {
                    Intent intent = new Intent(context, UnidadesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

                else if (recyclerView.getChildLayoutPosition(child) == RuUfpiHelper.SOBRE_INDEX) {
                    Intent intent = new Intent(context, SobreActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            return true;

        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

    }

}
