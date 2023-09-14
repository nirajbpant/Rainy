package com.example.rainy.api

import com.example.rainy.data.api.WeatherApi
import com.example.rainy.data.models.WeatherResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var weatherApi: WeatherApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApi = retrofit.create(WeatherApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetWeatherByCity() = runBlocking{
        val jsonResponse = """
            {
                "coord": {
                    "lon": 151.2073,
                    "lat": -33.8679
                },
                "weather": [
                    {
                        "id": 800,
                        "main": "Clear",
                        "description": "clear sky",
                        "icon": "01d"
                    }
                ],
                "base": "stations",
                "main": {
                    "temp": 295.15,
                    "feels_like": 294.79,
                    "temp_min": 291.88,
                    "temp_max": 300.68,
                    "pressure": 1025,
                    "humidity": 53
                },
                "visibility": 10000,
                "wind": {
                    "speed": 4.63,
                    "deg": 70
                },
                "clouds": {
                    "all": 0
                },
                "dt": 1694671554,
                "sys": {
                    "type": 2,
                    "id": 2018875,
                    "country": "AU",
                    "sunrise": 1694635027,
                    "sunset": 1694677516
                },
                "timezone": 36000,
                "id": 2147714,
                "name": "Sydney",
                "cod": 200
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse))

        val response: Response<WeatherResponse> = weatherApi.getWeatherByCity("SampleCity", "your_api_key")

        assert(response.isSuccessful)

        val weatherResponse = response.body()

        assertEquals("Sydney", weatherResponse?.name)
        assertEquals("Clear", weatherResponse?.weather?.get(0)?.main)
        assertEquals(295.15, weatherResponse?.main?.temp)
        assertEquals(53, weatherResponse?.main?.humidity)
        assertEquals(4.63, weatherResponse?.wind?.speed)
    }
}
