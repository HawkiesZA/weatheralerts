package za.co.hawkiesza.weatheralerts.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import za.co.hawkiesza.weatheralerts.utils.Constants
import za.co.hawkiesza.weatheralerts.api.WeatherApiHelper
import za.co.hawkiesza.weatheralerts.api.WeatherApiHelperImpl
import za.co.hawkiesza.weatheralerts.api.WeatherApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService = retrofit.create(WeatherApiService::class.java)

    @Provides
    @Singleton
    fun provideWeatherApiHelper(weatherApiHelper: WeatherApiHelperImpl): WeatherApiHelper = weatherApiHelper
}