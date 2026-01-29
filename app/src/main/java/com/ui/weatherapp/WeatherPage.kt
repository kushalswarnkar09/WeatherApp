package com.ui.weatherapp


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ui.weatherapp.Api.WeatherModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherUI(viewModel: WeatherViewModel) {
    val isNight = false
    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                .shadow(10.dp)
                .height(170.dp)

        ) {
            if (!isNight) {
                Image(
                    painter = painterResource(R.drawable.map),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.nightworld),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
            Text(
                "Weather Today",
                color = if (!isNight) {
                    colorResource(R.color.Ocean)
                } else {
                    Color.White
                },
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
                    .align(Alignment.BottomStart)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = {
                    city = it
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                ),

                placeholder = { Text("Search Location", color = Color.Gray) },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(16.dp, top = 8.dp, end = 8.dp)
                    .weight(1f)
                    .focusRequester(focusRequester),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }),
                singleLine = true

            )

            IconButton(
                onClick = {

                    viewModel.getData(city)

                },
                modifier = Modifier
                    .height(60.dp)
                    .padding(top = 8.dp, end = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(R.color.Ocean)),
                ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }


        when (val result = weatherResult.value) {
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }

            is NetworkResponse.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(
                        color = Color.LightGray, trackColor = colorResource(R.color.Ocean)
                    )
                }
            }

            is NetworkResponse.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { Text(text = result.message, color = Color.Black) }
            }

            null -> {}
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDetails(data: WeatherModel) {
    val isNight = false
    var textSize by remember { mutableStateOf(24.sp) }
    Spacer(modifier = Modifier.height(8.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = if (isNight) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color.White,
                            colorResource(R.color.Ocean),
                            colorResource(R.color.DarkNight),
                            Color.Black
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color.White,
                            colorResource(R.color.LightDay),
                            colorResource(R.color.Ocean)
                        )
                    )
                }
            )
            .padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .clip(RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "Location",
                tint = colorResource(R.color.Brownish),
                modifier = Modifier.size(38.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                data.location.name,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )

            Text(
                ", " + data.location.country,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Card(
            border = BorderStroke(3.dp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                if (!isNight) Image(
                    painter = painterResource(R.drawable.weatherpic), null
                ) else Image(painter = painterResource(R.drawable.nightpic), null)
                Column(
                    modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(140.dp)
                            .padding(top = 16.dp),
                        model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                        contentDescription = "Condition"
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 0.dp, end = 8.dp)
                            .padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.Absolute.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "${data.current.temp_c}°C",
                            color = Color.DarkGray,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(", ")
                        Text(
                            data.current.condition.text,
                            color = Color.DarkGray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                    HorizontalDivider(modifier = Modifier
                        .width(150.dp)
                        .padding(end = 4.dp), color = Color.Gray)
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "Location",
                            tint = colorResource(R.color.Brownish),
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            data.location.name,
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )

                        Text(
                            ", " + data.location.country,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(8.dp)){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column( modifier = Modifier.padding(top=16.dp)) {
                        Row {
                            Icon(Icons.Filled.WaterDrop, null, tint = Color.Blue)
                            Text("Humidity", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 23.sp)
                        }
                        Text(
                            "${data.current.humidity}%",
                            color = Color.White,
                            fontSize = 20.sp, // limit max lines if you want
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier
                            .height(70.dp)
                            .padding(8.dp)
                    )
                    Column ( modifier = Modifier.padding(top=16.dp)){
                        Text("Precipitation", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 23.sp)
                        Text("${data.current.precip_mm}mm", color = Color.White,fontSize = 20.sp,)
                    }
                }
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(8.dp)){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                ),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column ( modifier = Modifier.padding(top=16.dp)){
                        Row {
                            Icon(Icons.Filled.WaterDrop, null, tint = Color.Blue)
                            Text("Wind", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 23.sp)
                        }
                        Text(
                            "${data.current.wind_kph}km/h",
                            color = Color.White,
                            fontSize = 20.sp, // limit max lines if you want
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier
                            .height(70.dp)
                            .padding(8.dp)
                    )
                    Column( modifier = Modifier.padding(top=16.dp)){
                        Text("Nature", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 23.sp)
                        Text("${data.current.condition.text}mm", color = Color.White,fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun isNightTime(): Boolean {
    val hour = java.time.LocalTime.now().hour
    return hour !in 6..<18
}