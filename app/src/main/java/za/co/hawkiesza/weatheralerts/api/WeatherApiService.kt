package za.co.hawkiesza.weatheralerts.api

import retrofit2.Response
import retrofit2.http.GET
import za.co.hawkiesza.weatheralerts.models.WeatherEventResponse

interface WeatherApiService {
    @GET("https://api.weather.gov/alerts/active?status=actual&message_type=alert")
    suspend fun getWeatherEvents() : Response<WeatherEventResponse>
}