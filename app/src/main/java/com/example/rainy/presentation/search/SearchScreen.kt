package com.example.rainy.presentation.search
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rainy.data.models.WeatherResponse
import androidx.compose.runtime.*
import com.example.rainy.data.models.resource.Resource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {

    val weatherState by viewModel.state.collectAsState()

    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = query,
            onValueChange = {
                query = it
            },
            label = { Text("Search") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if(query.isNotEmpty())
                        viewModel.onEvent(SearchEvent.Search(query))
                }
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            query = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        Button(
            onClick = {
                if(query.isNotEmpty())
                    viewModel.onEvent(SearchEvent.Search(query))
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Search")
        }

        when(weatherState){
            is Resource.Success->{
                val data = (weatherState as Resource.Success).data
                WeatherPanel(result = data!!)
            }
            is Resource.Loading->{
                LoadingIndicator()
            }
            is Resource.Error -> {
                val message = (weatherState as Resource.Error).message
                Text("Error: $message")
            }
            else->{

            }
        }


    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator()
}

@Composable
fun WeatherPanel(result: WeatherResponse) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = result.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Weather: ${result.weather[0]?.main}",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Temperature: ${result.main.temp}°C",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Humidity: ${result.main.humidity}%",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Wind Speed: ${result.wind.speed} m/s",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
//            GlideImage(
//                data = "https://openweathermap.org/img/w/${result.weather[0].icon}.png",
//                contentDescription = null,
//                modifier = Modifier.size(100.dp)
//            )
        }
    }
}

