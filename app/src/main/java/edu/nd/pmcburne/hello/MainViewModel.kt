package edu.nd.pmcburne.hello

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hello.data.AppDatabase
import edu.nd.pmcburne.hello.data.LocationEntity
import edu.nd.pmcburne.hello.data.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUIState(
    val selectedTag: String = "core",
    val locations: List<LocationEntity> = emptyList(),
    val availableTags: List<String> = emptyList()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LocationRepository
    
    private val _selectedTag = MutableStateFlow("core")
    val selectedTag = _selectedTag.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = LocationRepository(database.locationDao())
        
        viewModelScope.launch {
            repository.refreshLocations()
        }
    }

    val uiState: StateFlow<MainUIState> = combine(
        repository.allLocations,
        _selectedTag
    ) { locations, selectedTag ->
        val tags = locations.flatMap { it.tags }.distinct().sorted()
        val filteredLocations = locations.filter { it.tags.contains(selectedTag) }
        MainUIState(
            selectedTag = selectedTag,
            locations = filteredLocations,
            availableTags = tags
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUIState()
    )

    fun onTagSelected(tag: String) {
        _selectedTag.update { tag }
    }
}
