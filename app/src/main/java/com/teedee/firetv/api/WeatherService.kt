// WeatherService.kt
package com.teedee.firetv.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json

import android.util.Log

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


@Serializable
data class IpApiResponse(
    @SerialName("ip_address") val ipAddress: String?, // Changed from 'query'
    @SerialName("city") val city: String?,
    @SerialName("city_geoname_id") val cityGeonameId: Int?,
    @SerialName("region") val region: String?,          // Mapped from 'region' directly
    @SerialName("region_iso_code") val regionIsoCode: String?,
    @SerialName("region_geoname_id") val regionGeonameId: Int?,
    @SerialName("postal_code") val postalCode: String?, // Changed from 'zip'
    @SerialName("country") val country: String?,
    @SerialName("country_code") val countryCode: String?,
    @SerialName("country_geoname_id") val countryGeonameId: Int?,
    @SerialName("country_is_eu") val countryIsEu: Boolean?,
    @SerialName("continent") val continent: String?,
    @SerialName("continent_code") val continentCode: String?,
    @SerialName("continent_geoname_id") val continentGeonameId: Int?,
    @SerialName("longitude") val longitude: Double?,  // Changed from 'lon'
    @SerialName("latitude") val latitude: Double?,    // Changed from 'lat'
    @SerialName("security") val security: Security?,
    @SerialName("timezone") val timezone: Timezone?,    // This was the main culprit
    @SerialName("flag") val flag: Flag?,
    @SerialName("currency") val currency: Currency?,
    @SerialName("connection") val connection: Connection?
)

@Serializable
data class Security(
    @SerialName("is_vpn") val isVpn: Boolean?
)

@Serializable
data class Timezone(
    @SerialName("name") val name: String?,
    @SerialName("abbreviation") val abbreviation: String?,
    @SerialName("gmt_offset") val gmtOffset: Int?,
    @SerialName("current_time") val currentTime: String?,
    @SerialName("is_dst") val isDst: Boolean?
)

@Serializable
data class Flag(
    @SerialName("emoji") val emoji: String?,
    @SerialName("unicode") val unicode: String?,
    @SerialName("png") val png: String?,
    @SerialName("svg") val svg: String?
)

@Serializable
data class Currency(
    @SerialName("currency_name") val currencyName: String?,
    @SerialName("currency_code") val currencyCode: String?
)

@Serializable
data class Connection(
    @SerialName("autonomous_system_number") val autonomousSystemNumber: Int?,
    @SerialName("autonomous_system_organization") val autonomousSystemOrganization: String?,
    @SerialName("connection_type") val connectionType: String?,
    @SerialName("isp_name") val ispName: String?, // This maps to your old 'isp'
    @SerialName("organization_name") val organizationName: String? // This maps to your old 'org'
)

object IpLocationService {
    private val client = HttpClient {
        install(ContentNegotiation) { json(Json {
            ignoreUnknownKeys = true // Add this line
            prettyPrint = true
            isLenient = true
        }) }
    }

    suspend fun fetchCity(): String? {
        return try {
            // Construct the URL with your API key
            val apiUrl = "https://ipgeolocation.abstractapi.com/v1/?api_key=7e3621cbbcaa434d8fdfa2789785be49"

            // Make the GET request and get the HttpResponse
            val httpResponse: HttpResponse = client.get(apiUrl)

            // Check if the HTTP request was successful (e.g., 200 OK)
            if (httpResponse.status.isSuccess()) {
                val response: IpApiResponse = httpResponse.body()
                response.city // Directly return the city if the request was successful
            } else {
                // Log the error if the HTTP status code indicates a problem
                // You might want to parse the error body here for more detail if the API provides it
                val errorBody = httpResponse.body<String>() // Get the raw error body
                Log.e("IpLocationService", "HTTP Error fetching IP location: ${httpResponse.status}. Body: $errorBody")
                null
            }
        } catch (e: Exception) {
            // Catch any network or parsing exceptions
            Log.e("IpLocationService", "Error fetching IP location: ${e.message}", e)
            null
        }
    }
}
