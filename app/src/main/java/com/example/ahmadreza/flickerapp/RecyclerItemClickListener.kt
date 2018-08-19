package com.example.ahmadreza.flickerapp

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by ahmadreza on 8/19/18.
 */
class RecyclerItemClickListener(val context: Context, val recyclerView: RecyclerView, val listener: OnRecyclerClicklistenr, val mGestureDetCom: GestureDetectorCompat? = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener(){
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        println("RecyclerItemClickListener.onSingleTapUp")
        val view = recyclerView.findChildViewUnder(e!!.x, e.y)
        if (view != null && listener != null){
            listener.onItemClick(view, recyclerView.getChildAdapterPosition(view))
        }
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        println("RecyclerItemClickListener.onLongPress")
        val view = recyclerView.findChildViewUnder(e!!.x, e.y)
        if (view != null){
            listener.onItemLongClick(view, recyclerView.getChildAdapterPosition(view))
        }
    }
})) : RecyclerView.SimpleOnItemTouchListener() {

    interface OnRecyclerClicklistenr{
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        println("RecyclerItemClickListener.onInterceptTouchEvent start")
        if (mGestureDetCom != null){
            var result = mGestureDetCom.onTouchEvent(e)
            println("result = ${result}")
            return result
        }
        else{
            println("return false")
            return false
        }
    }
}