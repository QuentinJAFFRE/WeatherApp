package com.startup.weatherapp

import android.content.Context
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private var homeRL: RelativeLayout? = null
    private var loadingPB: ProgressBar? = null
    private var cityNameTV: TextView? = null
    private var temperatureTV: TextView? = null
    private var conditionTV: TextView? = null
    private var cityEdt: TextInputEditText? = null
    private var backIV: ImageView? = null
    private var iconIV: ImageView? = null
    private var searchIV: ImageView? = null
    private var weatherRV: RecyclerView? = null

    private var weatherRVModalArrayList: ArrayList<WeatherRVModal>? = null;
    private var weatherRVAdapter: WeatherRVAdapter? = null

    private var locationManager: LocationManager? = null;
    private var PERMISSION_CODE: Int =  1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the status bar.
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_main)
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.idTVTemperature);
        conditionTV = findViewById(R.id.idTVCondition);
        cityEdt = findViewById(R.id.idEdtCity);
        backIV = findViewById(R.id.idIVBack);
        iconIV = findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearch);
        weatherRV = findViewById(R.id.idRVWeather);
        
        weatherRVModalArrayList = ArrayList<WeatherRVModal>();
        weatherRVAdapter = WeatherRVAdapter(this, weatherRVModalArrayList!!);

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?;

    }

    private fun getWeatherInfo(cityName: String) {
        val API_KEY: String = "41bb9c94c9c5435fbee150942223012"
        val url: String = "https://api.weatherapi.com/v1/forecast.json?key=" + API_KEY + "&q=" + cityName + "&days=3&aqi=no&alerts=no";
    }
}