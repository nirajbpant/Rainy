package com.example.rainy.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rainy.data.models.WeatherResponse
import com.example.rainy.data.models.resource.Resource
import com.example.rainy.domain.usecases.WeatherUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import coil.request.ImageResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val weatherUseCases: WeatherUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<WeatherResponse>>(Resource.Initial())
    val state: StateFlow<Resource<WeatherResponse>> = _state

    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState: StateFlow<SplashState> = _splashState

    val imageMap = HashMap<String, ImageResult?>()

    init {
    viewModelScope.launch {
        val images = weatherUseCases.getWeatherIcons()
        if(!images.isNullOrEmpty())
            for(image in images){
                imageMap[image.request.toString()] = image
            }
        _splashState.value = SplashState.Loaded
        _state.value = Resource.Loading()
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