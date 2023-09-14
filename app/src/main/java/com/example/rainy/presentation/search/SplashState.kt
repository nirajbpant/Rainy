package com.example.rainy.presentation.search

sealed class SplashState {
    object Loading : SplashState()
    object Loaded : SplashState()
}
