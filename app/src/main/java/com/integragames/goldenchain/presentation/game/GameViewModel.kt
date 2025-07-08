package com.integragames.goldenchain.presentation.game

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.integragames.goldenchain.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
@Keep
class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameState())
    val uiState = _uiState.asStateFlow()
    private var state: GameState
        get() = _uiState.value
        set(newState) {
            _uiState.update { newState }
        }

    fun load(isLoading: Boolean = true) {
        if (isLoading && !state.isLoading) return
        state = state.copy(
            isLoading = false,
            player = Character(),
            animationDuration = AnimationDuration.DEFAULT,
            enemy = Character(),
            isResult = false,
            roundScore = 0f,
            titleRes = 0
        )
    }

    fun selectDeck(deck: Int) {
        state = state.copy()
        state = when (deck) {
            -1 -> {
                if (state.player.leftDeck == CardType.entries.size - 1) {
                    state.copy(player = state.player.copy(leftDeck = 0))
                } else {
                    state.copy(player = state.player.copy(leftDeck = state.player.leftDeck.plus(1)))
                }
            }

            0 -> {
                if (state.player.centerDeck == CardType.entries.size - 1) {
                    state.copy(player = state.player.copy(centerDeck = 0))
                } else {
                    state.copy(
                        player = state.player.copy(
                            centerDeck = state.player.centerDeck.plus(
                                1
                            )
                        )
                    )
                }
            }

            else -> {
                if (state.player.rightDeck == CardType.entries.size - 1) {
                    state.copy(player = state.player.copy(rightDeck = 0))
                } else {
                    state.copy(player = state.player.copy(rightDeck = state.player.rightDeck.plus(1)))
                }
            }

        }
    }

    fun onFight() {
        viewModelScope.launch(Dispatchers.IO + Job()) {
            state = state.copy(
                enemy = state.enemy.copy(
                    leftDeck = CardType.entries.indices.random(),
                    centerDeck = CardType.entries.indices.random(),
                    rightDeck = CardType.entries.indices.random()
                ),
                isResult = true,
                roundScore = 0f,
                animationDuration = AnimationDuration.FAST
            )
            val botPick = state.enemy

            delay(1000L)

            calculate(botPick)
            state = when (state.roundScore) {
                2f -> {
                    state.copy(
                        titleRes = R.mipmap.sup
                    )
                }

                3f -> {
                    state.copy(
                        titleRes = R.mipmap.epic
                    )
                }

                else -> {
                    state.copy(
                        titleRes = R.mipmap.big
                    )
                }
            }
            delay(3000L)
            state = state.copy(
                isResult = false,
                isLoading = false,
                titleRes = 0,
                animationDuration = AnimationDuration.DEFAULT
            )
        }
    }

    private fun calculate(botPick: Character) {
        val left = CardType.entries[state.player.leftDeck]
        val center = CardType.entries[state.player.centerDeck]
        val right = CardType.entries[state.player.rightDeck]
        val leftBot = CardType.entries[botPick.leftDeck]
        val centerBot = CardType.entries[botPick.centerDeck]
        val rightBot = CardType.entries[botPick.rightDeck]

        val winLeft = left.beats(leftBot)
        val winCenter = center.beats(centerBot)
        val winRight = right.beats(rightBot)
        state = if (winLeft) {
            state.copy(
                roundScore = state.roundScore + 1,
                enemy = state.enemy.copy(health = state.enemy.health - 10)
            )
        } else if (left == leftBot) {
            state.copy(
                player = state.player.copy(health = state.player.health - 5),
                roundScore = state.roundScore + 0.5f,
                enemy = state.enemy.copy(health = state.enemy.health - 5)
            )
        } else {
            state.copy(
                player = state.player.copy(health = state.player.health - 10)
            )
        }
        state = if (winCenter) {
            state.copy(
                roundScore = state.roundScore + 1,
                enemy = state.enemy.copy(health = state.enemy.health - 10)
            )
        } else if (center == centerBot) {
            state.copy(
                player = state.player.copy(health = state.player.health - 5),
                roundScore = state.roundScore + 0.5f,
                enemy = state.enemy.copy(health = state.enemy.health - 5)
            )
        } else {
            state.copy(
                player = state.player.copy(health = state.player.health - 10)
            )
        }
        state = if (winRight) {
            state.copy(
                roundScore = state.roundScore + 1,
                enemy = state.enemy.copy(health = state.enemy.health - 10)
            )
        } else if (right == rightBot) {
            state.copy(
                player = state.player.copy(health = state.player.health - 5),
                roundScore = state.roundScore + 0.5f,
                enemy = state.enemy.copy(health = state.enemy.health - 5)
            )
        } else {
            state.copy(
                player = state.player.copy(health = state.player.health - 10)
            )
        }
    }
}

@Keep
data class GameState(
    val isLoading: Boolean = true,
    val player: Character = Character(),
    val animationDuration: AnimationDuration = AnimationDuration.DEFAULT,
    val enemy: Character = Character(),
    val isResult: Boolean = false,
    val roundScore: Float = 0f,
    val titleRes: Int = 0,
)
@Keep
data class Character(
    val health: Int = 100,
    val leftDeck: Int = 0,
    val centerDeck: Int = 0,
    val rightDeck: Int = 0,
)
@Keep
enum class CardType(val resId: Int) {
    ROCK(R.mipmap.rock),
    PAPER(R.mipmap.papper),
    SCISSORS(R.mipmap.scissors),
    LIZARD(R.mipmap.lizard),
    SPOCK(R.mipmap.spock);

    fun beats(bot: CardType): Boolean {
        return when (this) {
            ROCK -> bot == SCISSORS || bot == LIZARD
            PAPER -> bot == ROCK || bot == SPOCK
            SCISSORS -> bot == PAPER || bot == LIZARD
            LIZARD -> bot == SPOCK || bot == PAPER
            SPOCK -> bot == SCISSORS || bot == ROCK
        }
    }
}
@Keep
enum class AnimationDuration(val speed: Int) {
    FAST(1500),
    DEFAULT(3000),
}