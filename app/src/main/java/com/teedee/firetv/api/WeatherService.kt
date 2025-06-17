// WeatherService.kt
package com.teedee.firetv.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object WeatherService {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun fetchWeather(city: String, apiKey: String): WeatherResponse {
        val response: HttpResponse = client.get("https://api.openweathermap.org/data/2.5/weather") {
            url {
                parameters.append("q", city)
                parameters.append("appid", apiKey)
                parameters.append("units", "metric")
            }
        }
        return response.body()
    }
}

@Serializable
data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)

@Serializable
data class Main(val temp: Double)

@Serializable
data class Weather(val icon: String, val description: String)
