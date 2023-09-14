package com.example.rainy.presentation.search

sealed class SearchEvent{
    data class Search(val searchQuery: String) : SearchEvent()
}
