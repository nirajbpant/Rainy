package com.example.rainy.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rainy.data.models.WeatherResponse
import com.example.rainy.data.models.resource.Resource
import com.example.rainy.domain.usecases.WeatherUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val weatherUseCases: WeatherUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<WeatherResponse>>(Resource.Initial())
    val state: StateFlow<Resource<WeatherResponse>> = _state

    init {
        viewModelScope.launch {
           var result = weatherUseCases.getCookieWeatherData()
            when(result){
                is Resource.Success ->{
                    _state.value = result
                }
                is Resource.Error ->{
                    _state.value = result
                }
                else -> {}
            }
        }
    }

    fun initializeLocationWeather(currentCity: String){
        Log.d("Checking", "Is this called")
        viewModelScope.launch {
            _state.value = Resource.Loading()
            var result =  weatherUseCases.getWeatherByCity(currentCity)
            when(result){
                is Resource.Success ->{
                    _state.value = result
                }
                is Resource.Error ->{
                    _state.value = result
                }
                else -> {}
            }

        }
    }

    fun onEvent(event: SearchEvent) {
        when(event){
            is SearchEvent.Search ->{
                viewModelScope.launch {
                    _state.value = Resource.Loading()
                   var result =  weatherUseCases.getWeatherByCity(event.searchQuery)
                    when(result){
                        is Resource.Success ->{
                            _state.value = result
                        }
                        is Resource.Error ->{
                            _state.value = result
                        }
                        else -> {}
                    }

                }
            }
        }
    }
}