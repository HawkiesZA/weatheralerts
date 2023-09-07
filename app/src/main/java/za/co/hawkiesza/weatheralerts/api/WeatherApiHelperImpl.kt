package za.co.hawkiesza.weatheralerts.api

import retrofit2.Response
import za.co.hawkiesza.weatheralerts.models.WeatherEventResponse
import javax.inject.Inject

class WeatherApiHelperImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
) : WeatherApiHelper {
    override suspend fun getWeatherEvents(): Response<WeatherEventResponse> = weatherApiService.getWeatherEvents()
}