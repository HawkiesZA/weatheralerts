package za.co.hawkiesza.weatheralerts.api

import retrofit2.Response
import za.co.hawkiesza.weatheralerts.models.WeatherEventResponse

interface WeatherApiHelper {
    suspend fun getWeatherEvents() : Response<WeatherEventResponse>
}