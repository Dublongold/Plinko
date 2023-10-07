package one.two.plinko.entities

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.delay
import one.two.plinko.R
import kotlin.math.abs
import kotlin.math.pow
import kotlin.random.Random

class Ball {
    private var positionX = 0f
    private var positionY = 0f

    private var positionHorizontal = 0
    private var positionVertical = 0

    var ballPositionX
        get() = positionX
        set(value) {
            positionX = value
            ball?.run{
                translationX = value * context.resources.displayMetrics.density
            }
        }

    var ballPositionY
        get() = positionY
        set(value) {
            positionY = value
            ball?.run{
                translationY = value * context.resources.displayMetrics.density
            }
        }

    private var ball: ImageView? = null

    fun createBall(context: Context): ImageView {
        return ImageView(context).apply {
            id = R.id.ball
            val dp: (Float) -> Float = {
                it * context.resources.displayMetrics.density
            }
            layoutParams = ConstraintLayout.LayoutParams(
                dp(16f).toInt(), dp(16f).toInt()
            ).apply {
                startToStart = R.id.gameField
                topToTop = R.id.gameField
            }
            setImageResource(R.drawable.ball)
            ball = this
        }
    }

    fun setStartPosition() {
        when (Random.nextInt(0, 5)) {
            0 -> {
                ballPositionX = 124f
                ballPositionY = -16f
            }
            1 -> {
                ballPositionX = 139f
                ballPositionY = 20f
            }
            2 -> {
                ballPositionX = 155f
                ballPositionY = -16f
            }
            3 -> {
                ballPositionX = 170f
                ballPositionY = 20f

            }
            4 -> {
                ballPositionX = 186f
                ballPositionY = -16f
            }
        }
    }

    fun getPosition(): Int {
        return positionHorizontal
    }

    suspend fun move() {
        val startPositionX = ballPositionX
        val startPositionY = ballPositionY
        val random1 = listOf(-1.0, 1.0).random()
        val random2 = listOf(-7.5, -22.5).random() * random1
        val random3 = if(random2 == 7.5) 14 else 126
        for(i in 1..(if(abs(random2) == 7.5) 15 else 45)) {
            val x = (i * random1).toFloat()
            ballPositionX = startPositionX + x
            ballPositionY = startPositionY - (4 - (((x + random2).pow(2.0))  / random3))
                .toFloat()
            delay(if(abs(random2) == 7.5) 10 else 5)
        }
        ballPositionY = startPositionY
        positionHorizontal = ((ballPositionX - 4.0) / 15).toInt() + 1
        do {
            val newStartPositionY = ballPositionY
            positionVertical = ((ballPositionY + 16.0) / 36).toInt() + 1
            for (i in 1..12) {
                ballPositionY = newStartPositionY + i * 3
                delay(2)
            }
        }
        while(positionVertical < 10 && listForPositionVertical[positionVertical].none { it == positionHorizontal })
        Log.i(TAG, "Position: $positionHorizontal. Random2: $random2. Start: x - $startPositionX, y - $startPositionY. Current: x - $ballPositionX, y - $ballPositionY")
    }

    fun moveNext() {

    }

    fun checkIfFinish(): Boolean {
        return if(ballPositionY >= 308f) {
            Log.i(TAG, "Finished. Position: $positionHorizontal")
            true
        }
        else false
    }

    companion object {
        const val TAG = "Ball"
        val listForPositionVertical = listOf(
            listOf(9,11,13),
            listOf(8,10,12,14),
            listOf(7,9,11,13,15),
            listOf(6,8,10,12,14,16),
            listOf(5,7,9,11,13,15,17),
            listOf(4,6,8,10,12,14,16,18),
            listOf(3,5,7,9,11,13,15,17,19),
            listOf(2,4,6,8,10,12,14,16,18,20),
            listOf(1,3,5,7,9,11,13,15,17,19,21),
            listOf(0,2,4,6,8,10,12,14,16,18,20,22),
        )
    }
}

/*
<ImageView
        android:id="@+id/ball"
        android:layout_width="16dp"
        android:layout_height="16dp"
        android:translationX="159dp"
        android:translationY="-20dp"
        app:layout_constraintStart_toStartOf="@+id/gameField"
        app:layout_constraintTop_toTopOf="@+id/gameField"
        app:srcCompat="@drawable/ball" />
 */