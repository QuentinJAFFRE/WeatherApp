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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
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
                val cityName: String = cityEdt?.text.toString();
                if (cityName == "") {
                    Toast.makeText(this@MainActivity, "Please enter city name", Toast.LENGTH_SHORT).show();
                } else {
                    cityNameTV?.text = "";
                    getWeatherInfo(cityName);
                }
            }
        })
    }

    // enable to share the good or bad permissions to the user
    @Override
    public override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    // enable to display a first city by locating the user
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
        cityNameTV?.setText(cityName);
        val requestQueue: RequestQueue = Volley.newRequestQueue(this);
        val responseListener = Response.Listener<JSONObject> { response ->
            loadingPB?.setVisibility(View.GONE);
            homeRL?.setVisibility(View.VISIBLE);
            weatherRVModalArrayList?.clear()
            try {
                val temperature: String = response.getJSONObject("current").getString("temp_c");
                temperatureTV?.setText(temperature + "°C");
                val isDay: Int = response.getJSONObject("current").getInt("is_day");
                val condition: String = response.getJSONObject("current")
                    .getJSONObject("condition")
                    .getString("text");
                val conditionIcon: String = response.getJSONObject("current")
                    .getJSONObject("condition")
                    .getString("icon");
                val iconUrl: String = "http:" + conditionIcon;
                Picasso.get().load(iconUrl).into(iconIV);
                conditionTV?.setText(condition);
                if (isDay == 1) {
                    val url: String = "https://wallpaperaccess.com/full/1653751.jpg"
                    Picasso.get().load(url).into(backIV);
                } else {
                    val url: String = "https://i.pinimg.com/736x/2f/d5/06/2fd5064bdc6123d004153c6a1d3ea8f7--night-sky-stars-night-skies.jpg"
                    Picasso.get().load(url).into(backIV);
                }
                val forecastObject: JSONObject = response.getJSONObject("forecast");
                val forecastArray: JSONObject = forecastObject
                    .getJSONArray("forecastday")
                    .getJSONObject(0);
                val hourArray: JSONArray = forecastArray.getJSONArray("hour");
                for (hour in 0..hourArray.length()) {
                    val hourObject: JSONObject = hourArray.getJSONObject(hour);
                    val time: String = hourObject.getString("time");
                    val temperature: String = hourObject.getString("temp_c");
                    val conditionImg: String = hourObject
                        .getJSONObject("condition")
                        .getString("temp_c");
                    val wind: String = hourObject.getString("wind_kph");
                    weatherRVModalArrayList?.add(WeatherRVModal(time, temperature, conditionImg, wind));
                }
                weatherRVAdapter?.notifyDataSetChanged();

            } catch (e: JSONException) {
                e.printStackTrace();
            }
        }
        val errorListener = Response.ErrorListener { error ->
            Toast.makeText(this, "Please enter valid city name...", Toast.LENGTH_SHORT).show();
        }
        val jsonRequest: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            responseListener,
            errorListener);

        requestQueue.add(jsonRequest);
    }
}