/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.ddi_tarea7_200668.presentation

import com.example.ddi_tarea7_200668.R
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.util.Log

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var clockTextView: TextView
    private lateinit var saludoTextView: TextView
    private lateinit var temperaturaTextView: TextView
    private lateinit var handler: Handler
    private lateinit var updateTimeRunnable: Runnable

    companion object {
        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        private const val API_KEY = "c0127b836a5f08702496e2850e340546"
        private const val REQUEST_LOCATION_PERMISSION = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activityhora)

        clockTextView = findViewById(R.id.clockTextView)
        saludoTextView = findViewById(R.id.saludar)
        temperaturaTextView = findViewById(R.id.temperaturaTextView)

        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val saludo: String = when(hourOfDay) {
            in 6..11 -> "Buen día"
            in 12..18 -> "Buena tarde"
            else -> "Buena noche"
        }
        saludoTextView.text = saludo

        handler = Handler()
        updateTimeRunnable = object : Runnable {
            override fun run() {
                val currentTime = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val formattedTime = dateFormat.format(currentTime)
                clockTextView.text = formattedTime

                obtenerTemperatura()

                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        handler.post(updateTimeRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateTimeRunnable)
    }

    private fun obtenerTemperatura() {

        val latitud = 32.5644
        val longitud = -115.3533


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(WeatherService::class.java)

        val call = apiService.getWeather(latitud, longitud, API_KEY)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    val temperatura = weatherResponse?.main?.temp
                    temperaturaTextView.text = "$temperatura °C"
                } else {
                    Log.e("API_ERROR", "Error en la respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error en la solicitud: ${t.message}")
            }
        })
    }
}