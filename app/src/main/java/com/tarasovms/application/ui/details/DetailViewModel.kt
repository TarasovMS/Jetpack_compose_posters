package com.tarasovms.application.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarasovms.application.data.db.PosterLocal
import com.tarasovms.application.data.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: DetailRepository
) : ViewModel() {

    private val posterIdSharedFlow: MutableSharedFlow<Long> = MutableStateFlow(value = 0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val posterDetailsByIdFlow = posterIdSharedFlow.flatMapLatest {
        detailRepository.getPosterById(it)
    }

    fun loadPosterById(id: Long) = posterIdSharedFlow.tryEmit(id)

    fun updatePosterLike(posterLocal: PosterLocal) {
        viewModelScope.launch {
            detailRepository.updatePoster(posterLocal)
        }
    }
}
