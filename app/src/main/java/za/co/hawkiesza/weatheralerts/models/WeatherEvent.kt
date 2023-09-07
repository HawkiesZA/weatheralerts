package za.co.hawkiesza.weatheralerts.models

data class WeatherEvent(
    val event: String? = "",
    val effective: String? = "",
    val ends: String? = "",
    val senderName: String? = "",
)
