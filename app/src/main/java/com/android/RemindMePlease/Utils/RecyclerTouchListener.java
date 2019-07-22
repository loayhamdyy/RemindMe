package com.android.RemindMePlease.Utils;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;



public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    //class properties
    private GestureDetector gestureDetector;//Detects various gestures and events using the supplied MotionEvents.
    private ClickListener clickListener;

    //Constructor
    /*
    Context:
    Interface to global information about an application environment.
    This is an abstract class whose implementation is provided by the Android system.
    It allows access to application-specific resources and classes,
     as well as up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc.
    */
    public RecyclerTouchListener(Context context, final RecyclerView recyclerView,final ClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}