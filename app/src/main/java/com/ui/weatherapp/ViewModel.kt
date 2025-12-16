package com.ui.weatherapp

import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ui.weatherapp.Api.Constant
import com.ui.weatherapp.Api.RetrofitInstance
import com.ui.weatherapp.Api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherResponse = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = weatherResponse
   private val weatherApi = RetrofitInstance.weatherApi


    fun getData(city : String){
        weatherResponse.value = NetworkResponse.Loading
        viewModelScope.launch{
            try{
                val response = weatherApi.getWeather(Constant.apikey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        weatherResponse.value = NetworkResponse.Success(it)
                    }
                } else {
                    weatherResponse.value = NetworkResponse.Error("Data Can't Be Loaded")
                }
            }
            catch (e: Exception){
                weatherResponse.value = NetworkResponse.Error("Data Can't Be Loaded")
            }
        }
    }
}