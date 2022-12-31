package com.startup.weatherapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherRVAdapter(
    private val context: Context,
    private val weatherRVModalArrayList: ArrayList<WeatherRVModal>) :
    RecyclerView.Adapter<WeatherRVAdapter.ViewHolder>() {


    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return ViewHolder(view);
    }

    @Override
    override fun onBindViewHolder(holder: WeatherRVAdapter.ViewHolder, position: Int) {
        val modal: WeatherRVModal = weatherRVModalArrayList[position];
        holder.tempereratureTV.text = modal.temperature + "°C";
        holder.windTV.text = modal.windSpeed + "km/h";
        val iconURL = "http:" + modal.icon;
        Picasso.get().load(iconURL).into(holder.conditionIV);
        val input: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm");
        val output: SimpleDateFormat = SimpleDateFormat("HH:mm aa");
        try {
            val date: Date = input.parse(modal.time);
            val timeToDisplay = output.format(date);
            holder.timeTV.text = timeToDisplay;
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    @Override
    override fun getItemCount(): Int {
        return weatherRVModalArrayList.size;
    }

    class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val windTV: TextView = itemView.findViewById(R.id.idTVWindSpeed);
        val tempereratureTV: TextView = itemView.findViewById(R.id.idTVTemperature);
        val timeTV: TextView = itemView.findViewById(R.id.idTVTime);
        val conditionIV: ImageView = itemView.findViewById(R.id.idIVCondition);

    }
}