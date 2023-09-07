package za.co.hawkiesza.weatheralerts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import za.co.hawkiesza.weatheralerts.repository.WeatherRepository
import za.co.hawkiesza.weatheralerts.utils.Constants.BASE_IMAGE_URL
import za.co.hawkiesza.weatheralerts.utils.Resource
import za.co.hawkiesza.weatheralerts.utils.Status
import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import javax.inject.Inject

data class WeatherEvent(
    val imageUrl: String = "",
    val eventName: String = "",
    val effectiveDate: OffsetDateTime? = null,
    val ends: OffsetDateTime? = null,
    val senderName: String = "",
    val duration: Duration = if (effectiveDate != null && ends != null) {
        Duration.between(effectiveDate, ends)
    } else {
        Duration.ZERO
    }
)

data class MainState(
    val weatherEvents: List<WeatherEvent> = listOf()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<Resource<MainState>>(Resource.loading(null))
    val uiState: StateFlow<Resource<MainState>> = _uiState.asStateFlow()

    init {
        getWeatherEvents()
    }

    private fun getWeatherEvents() = viewModelScope.launch {
        weatherRepository.getWeatherEvents().let { response ->
            if (response.isSuccessful) {
                val weatherEvents = mutableListOf<WeatherEvent>()
                response.body()?.features?.let { features ->
                    val featuresCount = features.count()
                    val randomList = (0..featuresCount * 10).shuffled().take(featuresCount)
                    features.forEachIndexed { index, feature ->
                        weatherEvents.add(
                            WeatherEvent(
                                imageUrl = "${BASE_IMAGE_URL}${randomList[index]}",
                                eventName = feature.properties?.event ?: "",
                                effectiveDate = feature.properties?.effective?.let { OffsetDateTime.parse(it) },
                                ends = feature.properties?.ends?.let { OffsetDateTime.parse(it) },
                                senderName = feature.properties?.senderName ?: ""
                            )
                        )
                    }
                }
                _uiState.emit(Resource.success(MainState(weatherEvents)))
            } else {
                _uiState.emit(Resource.error(response.errorBody().toString(), null))
            }
        }
    }
}