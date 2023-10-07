package one.two.plinko

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivityViewModel: ViewModel() {
    private val ballPrice = MutableStateFlow(10)
    private val wealth = MutableStateFlow(4620)

    fun getStateFlow(type: String): StateFlow<Int> {
        return when(type) {
            BALL_PRICE -> ballPrice
            WEALTH -> wealth
            else -> throw IllegalArgumentException("Invalid type of state flow.")
        }
    }

    fun increasePrice() {
        ballPrice.value += 10
    }
    fun decreasePrice() {
        if(ballPrice.value >= 11) {
            ballPrice.value -= 10
        }
        else {
            ballPrice.value = 10
        }
    }

    fun payForBall(): Boolean {
        return if(wealth.value >= ballPrice.value) {
            wealth.value -= ballPrice.value
            true
        }
        else false
    }


    fun increaseWealth(value: Int) {
        wealth.value += value
    }

    fun setPrice(value: Int) {
        ballPrice.value = value
    }

    companion object {
        const val BALL_PRICE = "ballPrice"
        const val WEALTH = "wealth"
    }
}