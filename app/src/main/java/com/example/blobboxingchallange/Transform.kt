package com.example.blobboxingchallange

import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.widget.ImageView
import androidx.core.animation.doOnEnd

class Transform(var view: ImageView,  var pos: vector2 = vector2(0f,0f), var scale:vector2= vector2(0f,0f), var rot: Float = 0f,var spaceNums:vector2) {

    fun setTransformAttributes(){

        ObjectAnimator.ofFloat(this.view, "translationX", this.pos.x, this.pos.x).apply {
            interpolator = AnticipateInterpolator()
            duration = 10
            start()
        }
        ObjectAnimator.ofFloat(this.view, "translationY", this.pos.y, this.pos.y).apply {
            interpolator = AnticipateInterpolator()
            duration = 10
            start()
        }

        ObjectAnimator.ofFloat(this.view, "scaleX", this.scale.x, this.scale.x).apply {
            interpolator = AnticipateInterpolator()
            duration = 10
            start()
        }
        ObjectAnimator.ofFloat(this.view, "scaleY", this.scale.y, this.scale.y).apply {
            interpolator = AnticipateInterpolator()
            duration = 10
            start()
        }
        ObjectAnimator.ofFloat(this.view, "rotation", this.rot, this.rot).apply {
            interpolator = AnticipateInterpolator()
            duration = 10
            start()
        }

    }


    fun Translate(x:Float,y:Float, _duration: Long = 2000,func: ()-> Unit ={emptyfun()}){

        GoTOAnimation(this.view,"translationX", this.pos.x,this.pos.x+x,_duration, func= { func() })
        GoTOAnimation(this.view,"translationY", this.pos.y,this.pos.y-y,_duration)
        this.pos = vector2(this.pos.x+x,this.pos.y-y)
        if(x>0){
            this.spaceNums.x++
        }else if(x<0){
            this.spaceNums.x--
        }
        if(y>0){
            this.spaceNums.y++
        }else if(y<0){
            this.spaceNums.y--
        }

    }

    private fun emptyfun(){}


    private fun GoTOAnimation(view: ImageView, translation:String, From:Float, To:Float, _duration: Long ,func: ()-> Unit ={emptyfun()} ){
        ObjectAnimator.ofFloat(view, translation, From, To).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = _duration
            doOnEnd {


                func()

            }

            start()
        }
    }

}