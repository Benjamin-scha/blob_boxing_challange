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
    lateinit var trap1: GameItem
   // lateinit var trap2: GameItem

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
    lateinit var Box1: GameItem
    lateinit var Box2: GameItem
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
        trap1 = GameItem( Transform(findViewById(R.id.trap_dot), vector2(0f, 0f), vector2(1f, 1f), spaceNums = vector2(0f, 0f)),true,true)
       // trap2 = GameItem( Transform(findViewById(R.id.trap_dot), vector2(0f, 0f), vector2(1f, 1f), spaceNums = vector2(0f, 0f)),true,true)

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

                            setBoxToMe(Box1.transform)
                            Box1.declareHiden()
                            stage = TurnStage.PLACING_TRAP

                        }
                        TurnStage.PLACING_TRAP -> {
                            setTrapToMe(trap1.transform)
                            trap1.declareHiden()

                            when (turn){
                                whoseTurn.PLAYER1->{playersTurn.text = "PLAYER1"}
                                whoseTurn.PLAYER2->{playersTurn.text = "PLAYER2"}
                            }
                            stage = TurnStage.SEARCHING
                            setBlobToCenter()
                        }
                        TurnStage.SEARCHING -> {

                            if(Attempts>0){
                                if(areYouWhereBoxIs()){
                                    incrementScore()
                                    Box1.declareFound()
                                    trap1.declareFound()

                                }
                            }
                        }


                    }





        }




        box = findViewById(R.id.box_)

        Box1 = GameItem(Transform(box, scale = vector2(1f, 1f), pos = vector2(0f, 100f), spaceNums = vector2(0f, 0f)),true,true)
        Box1.setObject()

        Box2 = GameItem(Transform(box, scale = vector2(1f, 1f), pos = vector2(0f, 100f), spaceNums = vector2(0f, 0f)),true,true)
        Box2.setObject()



        blob.scale = vector2(.25f, .25f)
        setupBackAndForthAnimaion(blob.view, "ScaleY", blob.scale.y * 1f, blob.scale.y * 1.1f, 1000)
        setupBackAndForthAnimaion(blob.view, "ScaleX", blob.scale.x * 1.01f, blob.scale.x * .98f, 1000)
        trap1.transform.scale = vector2(1f, 1f)

    }


    private fun setBoxToMe(me: Transform) {

        Box1.transform.pos = me.pos
        Box1.transform.spaceNums = me.spaceNums
        Box1.setObject()



    }

    private fun setTrapToMe(me: Transform) {


        trap1.transform.pos = me.pos
        trap1.transform.spaceNums = me.spaceNums
        trap1.setObject()




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

        if (blob.pos.isthisEqualToThis(Box1.transform.pos)) {

            toost("YES PLAYER ONE IS WHERE BOX IS")
            return true
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

      if (blob.pos.isthisEqualToThis(trap1.transform.pos)) {
          trap1.declareFound()
          toost("YES I AM WHERE TRAP IS")
      }
    }

    private  fun incrementScore(){
        if(turn == whoseTurn.PLAYER1){
            score1++
        }else{
            score2++
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


    private fun setItemsTodefault(){
        Box1.transform.pos = vector2(0f,-2f)
        trap1.transform.pos = vector2(0f,-2f)
        Box1.isVisible = true
        trap1.isVisible = true
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

     var score1:Int = 0
     var score2:Int = 0


    private fun StartGameSequence(){
        turn = whoseTurn.PLAYER1
        score1 = 0
        score2 = 0

    }

}