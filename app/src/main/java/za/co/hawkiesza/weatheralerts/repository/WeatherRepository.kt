package za.co.hawkiesza.weatheralerts.repository

import za.co.hawkiesza.weatheralerts.api.WeatherApiHelper
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiHelper: WeatherApiHelper
) {
    suspend fun getWeatherEvents() = apiHelper.getWeatherEvents()
}