package com.integragames.goldenchain.presentation.splash

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.integragames.goldenchain.data.repository.GameManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.net.URLEncoder
import java.util.UUID


class SplashViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SplashState())
    val uiState = _uiState.asStateFlow()
    private var state: SplashState
        get() = _uiState.value
        set(newState) {
            _uiState.update { newState }
        }

    fun load(remoteManager: GameManager, sharedPreferences: SharedPreferences) {

        state = state.copy(remoteManager = remoteManager)



    }
}
private fun String.encodeUtf(): String {
    return try {
        URLEncoder.encode(this, "UTF-8")
    } catch (e: Exception) {
        "null"
    }
}
data class SplashState(
    val isLoading: Boolean = true,
    val remoteManager: GameManager? = null,
    val data: String = "",
    val userId: String = UUID.randomUUID().toString(),
)