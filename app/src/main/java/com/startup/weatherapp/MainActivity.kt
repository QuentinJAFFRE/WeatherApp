package com.startup.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private var homeRL: RelativeLayout = findViewById(R.id.idRLHome);
    private var loadingPB: ProgressBar = findViewById(R.id.idPBLoading);
    private var cityNameTV: TextView = findViewById(R.id.idTVCityName);
    private var temperatureTV: TextView = findViewById(R.id.idTVTemperature);
    private var conditionTV: TextView? = findViewById(R.id.idTVCondition);
    private var cityEdt: TextInputEditText? = findViewById(R.id.idEdtCity);
    private var backIV: ImageView? = findViewById(R.id.idIVBack);
    private var iconIV: ImageView? = findViewById(R.id.idIVIcon);
    private var searchIV: ImageView? = findViewById(R.id.idIVSearch);
    private var weatherRV: RecyclerView? = findViewById(R.id.idRVWeather);



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}