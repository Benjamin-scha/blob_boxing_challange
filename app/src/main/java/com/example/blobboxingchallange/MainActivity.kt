package com.example.blobboxingchallange

import android.animation.ObjectAnimator
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {



    var Attempts:Int = 0

    lateinit var viewPosition: TextView

    lateinit var playersTurn:TextView

    lateinit var blobImage: ImageView

    lateinit var blob: Transform
    lateinit var trap1: Transform
    lateinit var trap2: Transform

    enum class whoseTurn {
        PLAYER1, PLAYER2
    }

    enum class TurnStage {
        PLACING_BOX, PLACING_TRAP, SEARCHING
    }

    lateinit var turn: whoseTurn
    lateinit var stage: TurnStage


    lateinit var button1: Button

    lateinit var rightButton: Button
    lateinit var leftButton: Button
    lateinit var upButton: Button
    lateinit var downButton: Button

    var blobSpeed: Float = 250f
    lateinit var Box1: Transform
    lateinit var Box2: Transform
    lateinit var box: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Attempts = 3

        playersTurn = findViewById(R.id.which_player)
        playersTurn.text = "PLAYER 1"


        turn = whoseTurn.PLAYER1
        stage = TurnStage.PLACING_BOX



        //setText
        viewPosition = findViewById(R.id.textView)


        //set speed
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        blobSpeed = displayMetrics.widthPixels.toFloat() / 5







        blobImage = findViewById(R.id.main_character)


        blob = Transform(blobImage, vector2(0f, 0f), vector2(1f, 1f), spaceNums = vector2(0f, 0f))
        trap1 = Transform(findViewById(R.id.trap_dot), vector2(0f, 0f), vector2(1f, 1f), spaceNums = vector2(0f, 0f))
        trap2 = Transform(findViewById(R.id.trap_dot), vector2(0f, 0f), vector2(1f, 1f), spaceNums = vector2(0f, 0f))


        ///// setup buttons
        rightButton = findViewById(R.id.right_button)
        leftButton = findViewById(R.id.left_button)
        upButton = findViewById(R.id.up_button)
        downButton = findViewById(R.id.down_button)

        rightButton.setOnClickListener {
            if (blob.spaceNums.x < 2) blob.Translate(blobSpeed, 0f, func = { this.checkForTraps() })
            viewPosition.text = "${blob.spaceNums.x}, ${blob.spaceNums.y}"
        }

        leftButton.setOnClickListener {
            if (blob.spaceNums.x > -2) blob.Translate(-blobSpeed, 0f, func = { this.checkForTraps() })
            viewPosition.text = "${blob.spaceNums.x}, ${blob.spaceNums.y}"
        }

        upButton.setOnClickListener {
            if (blob.spaceNums.y < 3) blob.Translate(0f, blobSpeed, func = { this.checkForTraps() })
            viewPosition.text = "${blob.spaceNums.x}, ${blob.spaceNums.y}"
        }

        downButton.setOnClickListener {
            if (blob.spaceNums.y > -2) blob.Translate(0f, -blobSpeed, func = { this.checkForTraps() })
            viewPosition.text = "${blob.spaceNums.x}, ${blob.spaceNums.y}"
        }

        button1 = findViewById(R.id.button)
        button1.setOnClickListener {



            when (stage) {


                TurnStage.PLACING_BOX -> {
                    setBoxToMe(blob)
                    toost("BOX SET TO ME")
                    stage = TurnStage.PLACING_TRAP

                    button1.text = "Place Trap"
                }

                TurnStage.PLACING_TRAP -> {
                    setTrapToMe(blob)
                    toost("TRAP IS SET")


                    button1.text = "check if box is here"


                    setBlobToCenter()

                    when(turn){
                        whoseTurn.PLAYER1->{
                            turn = whoseTurn.PLAYER2
                            stage = TurnStage.PLACING_BOX
                            Box2.pos = vector2(0f,0f,)
                            trap2.pos = vector2(0f,0f)
                            Box2.setTransformAttributes()
                            trap2.setTransformAttributes()
                            MakeViewVisible(box)
                            MakeViewVisible(findViewById(R.id.trap_dot))
                        }
                        whoseTurn.PLAYER2->{
                            turn = whoseTurn.PLAYER1
                            makeViewInvisible(box)
                            makeViewInvisible(findViewById(R.id.trap_dot))
                            stage = TurnStage.SEARCHING
                        }
                    }






                }


                TurnStage.SEARCHING -> {

                    when(turn){
                        whoseTurn.PLAYER1->{
                            Attempts--
                            if (areYouWhereBoxIs()&&Attempts>0) {
                                MakeViewVisible(box)
                                button1.text = "Place Box"
                                MakeViewVisible(findViewById(R.id.trap_dot))
                                togglePlayer()
                                stage = TurnStage.PLACING_BOX
                            }



                            if(Attempts<1){
                                togglePlayer()
                                stage = TurnStage.PLACING_BOX
                            }
                        }
                        whoseTurn.PLAYER2->{

                        }
                    }


                    Attempts--
                    if (areYouWhereBoxIs()&&Attempts>0) {
                        MakeViewVisible(box)
                        button1.text = "Place Box"
                        MakeViewVisible(findViewById(R.id.trap_dot))
                        togglePlayer()
                        stage = TurnStage.PLACING_BOX
                    }



                    if(Attempts<1){
                        togglePlayer()
                        stage = TurnStage.PLACING_BOX
                    }





                }

            }
            when(turn){
                whoseTurn.PLAYER1->{
                    playersTurn.text = "PLAYER 1"
                }
                whoseTurn.PLAYER2->{
                    playersTurn.text = "PLAYER 2"
                }
            }

        }




        box = findViewById(R.id.box_)

        Box1 = Transform(box, scale = vector2(1f, 1f), pos = vector2(0f, 100f), spaceNums = vector2(0f, 0f))
        Box1.setTransformAttributes()

        Box2 = Transform(box, scale = vector2(1f, 1f), pos = vector2(0f, 100f), spaceNums = vector2(0f, 0f))
        Box2.setTransformAttributes()



        blob.scale = vector2(.25f, .25f)
        setupBackAndForthAnimaion(blob.view, "ScaleY", blob.scale.y * 1f, blob.scale.y * 1.1f, 1000)
        setupBackAndForthAnimaion(blob.view, "ScaleX", blob.scale.x * 1.01f, blob.scale.x * .98f, 1000)
        trap1.scale = vector2(1f, 1f)

    }


    private fun setBoxToMe(me: Transform) {
        when (turn) {
            whoseTurn.PLAYER1 -> {
                Box1.pos = me.pos
                Box1.spaceNums = me.spaceNums
                Box1.setTransformAttributes()
            }
            whoseTurn.PLAYER2 -> {
                Box2.pos = me.pos
                Box2.spaceNums = me.spaceNums
                Box2.setTransformAttributes()
            }
        }


    }

    private fun setTrapToMe(me: Transform) {
        when (turn) {
            whoseTurn.PLAYER1 -> {
                trap1.pos = me.pos
                trap1.spaceNums = me.spaceNums
                trap1.setTransformAttributes()
            }
            whoseTurn.PLAYER2 -> {
                trap2.pos = me.pos
                trap2.spaceNums = me.spaceNums
                trap2.setTransformAttributes()
            }
        }

    }


    private fun makeViewInvisible(view: ImageView) {
        view.visibility = ImageView.INVISIBLE
    }

    private fun MakeViewVisible(view: ImageView) {
        view.visibility = ImageView.VISIBLE
    }

    private fun setupBackAndForthAnimaion(view: ImageView, translation: String, From: Float, To: Float, _duration: Long) {
        ObjectAnimator.ofFloat(view, translation, From, To).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = _duration
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }


    private fun areYouWhereBoxIs(): Boolean {
        when (turn) {
            whoseTurn.PLAYER1 -> {
                if (blob.pos.isthisEqualToThis(Box1.pos)) {
                    toost("YES PLAYER ONE IS WHERE BOX IS")
                    return true
                }
            }

            whoseTurn.PLAYER2 -> {
                if (blob.pos.isthisEqualToThis(Box2.pos)) {
                    toost("YES PLAYER TWO IS WHERE BOX IS")
                    return true
                }
            }

        }
        toost("NOPE!")
        return false

    }


    private fun checkForTraps() {
        if (stage == TurnStage.SEARCHING) {
            areYouWhereTrapIs()
        }
    }

    private fun areYouWhereTrapIs() {

        when (turn){
            whoseTurn.PLAYER1->{
                if (blob.pos.isthisEqualToThis(trap1.pos)) {
                    toost("YES I AM WHERE TRAP IS")
                }
            }
            whoseTurn.PLAYER2->{
                if (blob.pos.isthisEqualToThis(trap2.pos)) {
                    toost("YES I AM WHERE TRAP IS")
                }
            }
        }

    }


    private fun isViewOverlapping(v1: View, v2: View): Boolean {

        val rect1 = Rect()
        v1.getHitRect(rect1)


        val rect2 = Rect()
        v2.getHitRect(rect2)
        return Rect.intersects(rect1, rect2)
    }


    private fun setBlobToCenter() {
        blob.pos = vector2(0f, 0f)
        blob.spaceNums = vector2(0f, 0f)
        blob.setTransformAttributes()
    }


    private fun emptyfun() {}


    private fun ifOnOtherSlimeGoBack(view: BlobClass, x: Float, y: Float, _duration: Long) {

        toost("hiiiii")
        // if (isViewOverlapping(view.view, blob2.view)) {

        // }

    }


    private fun toost(string: String) {
        Toast.makeText(this@MainActivity, string, Toast.LENGTH_SHORT).show()
    }


    private fun togglePlayer(){
        if (turn == whoseTurn.PLAYER1) {
            playersTurn.text = "PLAYER 2"
            turn = whoseTurn.PLAYER2
        }else if (turn == whoseTurn.PLAYER2) {
            turn = whoseTurn.PLAYER1
            playersTurn.text = "PLAYER 1"
        }
        Attempts = 3

    }

}