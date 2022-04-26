package com.tarasovms.application.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tarasovms.application.data.db.PosterLocal
import com.tarasovms.application.data.repository.PosterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    posterRepository: PosterRepository
) : ViewModel() {

    var postersLocal: Flow<List<PosterLocal>> = posterRepository.getAllPoster(
        onStart = { _isLoading.value = false },
        onCompletion = { _isLoading.value = true },
        onError = { Log.e("ERROR", it) }
    )

    var postersLocalLike: Flow<List<PosterLocal?>> = posterRepository.getPosterLike(
        onStart = { _isLoading.value = false },
        onCompletion = { _isLoading.value = true }
    )

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: Flow<Boolean> get() = _isLoading

    private val _selectedTab: MutableState<Int> = mutableStateOf(0)
    val selectedTab: State<Int> get() = _selectedTab

    fun selectTab(@StringRes tab: Int) {
        _selectedTab.value = tab
    }
}
