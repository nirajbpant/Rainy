package com.example.rainy.presentation.search
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.core.graphics.drawable.toBitmap
import coil.request.ImageResult
import com.example.rainy.data.models.resource.Resource
import com.example.rainy.domain.usecases.GetWeatherIcons


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {

    val splashState by viewModel.splashState.collectAsState()
    val weatherState by viewModel.state.collectAsState()

    var query by remember { mutableStateOf("") }

    //Todo Need to Refactor into Different Composables
    when(splashState){
        is SplashState.Loading->{
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                color = Color.White.copy(alpha = 0.8f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading Icons...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }

        is SplashState.Loaded->{
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
                        var icon : ImageResult ?= null
                        for ((key, value) in viewModel.imageMap) {
                            if (key.contains(data!!.weather[0].icon)) {
                                icon = viewModel.imageMap[key]
                                break // Exit the loop after finding the first matching element
                            }
                        }

                        WeatherPanel(result = data!!, imageBitmap = icon?.drawable?.toBitmap()?.asImageBitmap())
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
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator()
}

@Composable
fun WeatherPanel(result: WeatherResponse, imageBitmap: ImageBitmap?) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            GetWeatherIcon(imageBitmap)
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
        }
    }
}

@Composable
fun GetWeatherIcon(imageBitmap: ImageBitmap?) {
    if(imageBitmap!=null) {
        Image(
            painter = BitmapPainter(imageBitmap),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(200.dp)
        )
    }else{
        Text(text = "Error Loading Icon")
    }
}

