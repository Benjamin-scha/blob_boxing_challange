package com.example.blobboxingchallange

import android.view.View

class GameItem(var transform: Transform,var isfound:Boolean) {
    public fun setObject(){
        this.transform.setTransformAttributes()
        this.transform.view.visibility = if(isfound) View.VISIBLE else View.INVISIBLE
    }
}