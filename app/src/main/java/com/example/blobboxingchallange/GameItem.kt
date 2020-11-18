package com.example.blobboxingchallange

import android.view.View

class GameItem(var transform: Transform,var isVisible:Boolean, var isfound:Boolean) {
    public fun setObject(){
        this.transform.setTransformAttributes()
        this.transform.view.visibility = if(isVisible) View.VISIBLE else View.INVISIBLE
    }

    fun toggleVisibility(){
        isVisible = !isVisible
        this.transform.view.visibility = if(isVisible) View.VISIBLE else View.INVISIBLE
    }

    fun declareHiden(){
        isfound = false
        isVisible = false
        setObject()
    }

    fun declareFound(){
        isfound = true
        isVisible = true
        setObject()
    }

}