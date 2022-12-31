package com.startup.weatherapp

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import kotlin.collections.ArrayList

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

        val hasFineLocationPermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        val hasCoarseLocationPermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED
            && hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {

            val permissions = arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);
            //BOF : this -> MainActivity.this in tutorial
            ActivityCompat.requestPermissions( this, permissions, PERMISSION_CODE);

        }

        val location: Location =
            locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!;
        val cityName: String = getCityName(location.longitude, location.latitude);
        getWeatherInfo(cityName);

        searchIV?.setOnClickListener( View.OnClickListener {
            @Override
            fun onClick(v: View?) {
                val cityName: String = cityEdt?.getText().toString();
                if (cityName == "") {
                    Toast.makeText(this@MainActivity, "Please enter city name", Toast.LENGTH_SHORT).show();
                } else {
                    cityNameTV?.text = "";
                    getWeatherInfo(cityName);
                }
            }
        })
    }

    

    private fun getCityName(longitude: Double, latitude: Double) : String {
        var cityName: String = "Not found";
        val gcd: Geocoder = Geocoder(baseContext, Locale.getDefault());
        var addresses: List<Address> = ArrayList<Address>();
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 10);
        } catch (e: Exception) {
            e.printStackTrace();
        }
        for (address in addresses) {
            if (address == null) {
                continue;
            }
            var cityAddress = address.locality;
            if (cityAddress != null && cityAddress.length > 0) {
                cityName = cityAddress;
                break;
            }
            Toast.makeText(this, "City not found", Toast.LENGTH_SHORT).show();
        }
        return cityName;
    }

    private fun getWeatherInfo(cityName: String) {
        val API_KEY: String = "41bb9c94c9c5435fbee150942223012"
        val url: String = "https://api.weatherapi.com/v1/forecast.json?key=" + API_KEY + "&q=" + cityName + "&days=3&aqi=no&alerts=no";
    }
}