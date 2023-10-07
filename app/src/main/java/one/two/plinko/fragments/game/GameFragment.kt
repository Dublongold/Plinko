package one.two.plinko.fragments.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import one.two.plinko.MainActivityViewModel
import one.two.plinko.R
import one.two.plinko.entities.Ball
import one.two.plinko.fragments.FragmentCompat
import one.two.plinko.navigation.NavigationController

class GameFragment: FragmentCompat() {
    private lateinit var ball: Ball
    override val currentDestination = NavigationController.DESTINATION_GAME

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return (inflater to container).inflate(R.layout.fragment_game)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonsOnClickListeners<ImageButton>(
            listOf(R.id.backToHomeButton, R.id.decrease, R.id.increase, R.id.sendBall),
            listOf( {
                getNavigationController().navigate(
                    type = NavigationController.TYPE_POP_BACK_STACK
                )
            }, {
                viewModel?.decreasePrice()
            }, {
                viewModel?.increasePrice()
            }, {
                viewModel?.let { vm ->
                    if (vm.payForBall()) {
                        val price = vm.getStateFlow(MainActivityViewModel.BALL_PRICE).value
                        ball = Ball()
                        view as ViewGroup
                        view.findViewById<ImageView?>(R.id.ball)?.let {
                            view.removeView(it)
                        }
                        val ballView = ball.createBall(requireContext())
                        view.addView(ballView)
                        ball.setStartPosition()
                        it.isEnabled = false
                        viewLifecycleOwner.lifecycleScope.launch {
                            while (!ball.checkIfFinish()) {
                                ball.move()
                            }
                            val position = ball.getPosition()
                            if (position in 2..20) {
                                val multiplier = positionToMultiplier(position)
                                vm.increaseWealth((price * multiplier).toInt())
                            }
                            else {
                                view.removeView(ballView)
                            }
                            it.isEnabled = true
                        }
                    }
                }
            })
        )

        viewModel?.run {
            setButtonsOnClickListeners<AppCompatButton>(
                listOf(
                    R.id.fastPrice50,
                    R.id.fastPrice100,
                    R.id.fastPrice200,
                    R.id.fastPrice1000,
                ),
                listOf(
                    { setPrice(50) },
                    { setPrice(100) },
                    { setPrice(200) },
                    { setPrice(1000) }
                )
            )
            setObservingOnTextViews(
                listOf(R.id.wealthOfUser, R.id.ballPrice),
                listOf(
                    getStateFlow(MainActivityViewModel.WEALTH),
                    getStateFlow(MainActivityViewModel.BALL_PRICE),
                )
            )
        }
    }

    private fun positionToMultiplier(position: Int) = when(position) {
        2 -> 12.0
        4 -> 6.0
        6 -> 2.0
        8 -> 3.0
        10 -> 4.0
        12 -> 1.2
        13 -> 2.3
        16 -> 1.75
        18 -> 5.0
        20 -> 10.0
        else -> 0.0
    }
}