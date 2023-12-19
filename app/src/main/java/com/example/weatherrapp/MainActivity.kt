package com.example.weatherrapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherrapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//d6d47ab5551def789a2e9831c8c03adf
class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Jaipur" )

    }

    private fun fetchWeatherData(cityName:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(cityName, "d6d47ab5551def789a2e9831c8c03adf", units = "metric")
        response.enqueue(object : Callback<WeatherApp> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()


                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed= responseBody.wind.speed
                    val condition= responseBody.weather.firstOrNull()?.main?:"unknown"
                    val maxTemp= responseBody.main.temp_max
                    val minTemp= responseBody.main.temp_min
                 val sunRise = responseBody.sys.sunrise
                    val sunSet = responseBody.sys.sunset
                    val seaLevel = responseBody.main.pressure


                    binding.temp.text= "$temperature °C"
                    binding.weather.text= condition
                    binding.maxTemp.text= "Max Temp: $maxTemp °C"
                    binding.minTemp.text= "Min Temp: $minTemp °C"
                    binding.humidity.text="$humidity %"
                   binding.windSpeed.text="$windSpeed m/s"
                  binding.sunRise.text="$sunRise"
                    binding.sunSet.text="$sunSet"
                    binding.sea.text="$seaLevel hPa"
                    binding.condition.text= condition
                    binding.day.text= dayName(System.currentTimeMillis())
                        binding.date.text= date()
                        binding.cityName.text= " $cityName   "




                          Log.d("TAG", "onResponse: maxTemp")
                }
                else {
                    // Handle unsuccessful response
                    Log.e("TAG", "Error in onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                // Handle failure, e.g., log the error
                Log.e("TAG", "onFailure: ${t.message}", t)
            }
        })
    }

    private fun date(): String{
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))

    }


    }
fun dayName(timestamp:Long) : String{

    val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
    return sdf.format((Date()))
}