package com.example.blobboxingchallange

import android.animation.ObjectAnimator
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.animation.doOnEnd

class MainActivity : AppCompatActivity() {


    lateinit var  posText:TextView




   lateinit var blobImage:ImageView

   lateinit var blob : Transform
    lateinit var blob2:Transform

   //


    lateinit var button1 :Button

    lateinit var rightButton: Button
    lateinit var leftButton: Button
    lateinit var upButton:Button
    lateinit var downButton:Button

    var blobSpeed:Float = 250f
    lateinit var Box:Transform
    lateinit var box:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        posText = findViewById(R.id.textView)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        blobSpeed = displayMetrics.widthPixels.toFloat()/5




        blobImage = findViewById(R.id.main_character)


        blob = Transform(blobImage, vector2(0f, 0f), vector2(1f, 1f))
        blob2 = Transform(findViewById(R.id.second_character), vector2(0f, 0f), vector2(1f, 1f))




        ///// setup buttons
        rightButton = findViewById(R.id.right_button)
        leftButton = findViewById(R.id.left_button)
        upButton = findViewById(R.id.up_button)
        downButton = findViewById(R.id.down_button)

        rightButton.setOnClickListener {
            blob.Translate(blobSpeed, 0f,func ={arePosEqual()})
            posText.text = "${blob.pos.x}, ${blob.pos.y}"
        }

        leftButton.setOnClickListener {
            blob.Translate(-blobSpeed, 0f,func ={arePosEqual()})
            posText.text = "${blob.pos.x}, ${blob.pos.y}"
        }

        upButton.setOnClickListener {
            blob.Translate(0f,blobSpeed,func ={arePosEqual()})
            posText.text = "${blob.pos.x}, ${blob.pos.y}"
        }

        downButton.setOnClickListener {
            blob.Translate(0f,-blobSpeed, func ={arePosEqual()})
            posText.text = "${blob.pos.x}, ${blob.pos.y}"
        }

        button1 = findViewById(R.id.button)
        button1.setOnClickListener {


        }




        box = findViewById(R.id.box_)

        Box = Transform(box, scale = vector2(1f, 1f), pos = vector2(0f,100f))


        Box.setTransformAttributes()



        blob.scale = vector2(.25f, .25f)
        setupBackAndForthAnimaion(blob.view, "ScaleY", blob.scale.y * 1f, blob.scale.y * 1.1f, 1000)
        setupBackAndForthAnimaion(blob.view, "ScaleX", blob.scale.x * 1.01f, blob.scale.x * .98f, 1000)
        blob2.scale = vector2(.25f, .25f)
        setupBackAndForthAnimaion(blob2.view, "ScaleY", blob2.scale.y * 1f, blob2.scale.y * 1.1f, 1000)
        setupBackAndForthAnimaion(blob2.view, "ScaleX", blob2.scale.x * 1.01f, blob2.scale.x * .98f, 1000)
    }



    private fun setupBackAndForthAnimaion(view: ImageView, translation:String, From:Float, To:Float, _duration: Long){
        ObjectAnimator.ofFloat(view, translation, From, To).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = _duration
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }


    private fun arePosEqual(){
        toost("things happen")
        if(blob.pos.isthisEqualToThis(blob2.pos)){
            toost("SAME PLACE")
        }
    }

    private fun isViewOverlapping(v1: View, v2:View):Boolean{

        val rect1 = Rect()
        v1.getHitRect(rect1)


        val rect2 = Rect()
        v2.getHitRect(rect2)
        return Rect.intersects(rect1,rect2)
    }




//    private fun Translateblob(blob: BlobClass, x:Float, y:Float, _duration: Long,func: ()-> Unit ={emptyfun()}){
//        GoTOAnimation(blob.view,"translationX", blob.pos.x,blob.pos.x+x,_duration) { func }
//        GoTOAnimation(blob.view,"translationY", blob.pos.y,blob.pos.y-y,_duration)
//        blob.pos = vector2(blob.pos.x+x,blob.pos.y-y)
//    }



//    private fun Translateblob(blob: BlobClass, x:Float, y:Float, _duration: Long){
//        GoTOAnimation(blob.view,"translationX", blob.pos.x,blob.pos.x+x,_duration)
//        GoTOAnimation(blob.view,"translationY", blob.pos.y,blob.pos.y-y,_duration)
//        blob.pos = vector2(blob.pos.x+x,blob.pos.y-y)
//    }

    private fun emptyfun(){}


    private fun ifOnOtherSlimeGoBack(view: BlobClass, x:Float, y: Float, _duration: Long){

        toost("hiiiii")
        if(isViewOverlapping(view.view,blob2.view)){

        }

    }







    private fun toost(string: String){
        Toast.makeText(this@MainActivity,string,Toast.LENGTH_SHORT).show()
    }

}