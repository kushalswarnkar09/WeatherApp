package com.ui.weatherapp


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.util.CoilUtils.result
import com.ui.weatherapp.Api.WeatherModel

@Composable
fun WeatherUI(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("Dungarpur") }
    val weatherResult = viewModel.weatherResult.observeAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                .background(Color.Black)


        ) {
            Text(
                "Weather App",
                color = Color.White,
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 40.dp, start = 16.dp)
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
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    disabledContainerColor = Color.Black,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),

                placeholder = { Text("Search Location", color = Color.Gray) },
                modifier = Modifier
                    .padding(16.dp, top = 8.dp, end = 8.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
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
                    .background(Color.Black),

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
                CircularProgressIndicator(
                    color = Color.LightGray, trackColor = Color.Black
                )
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

@Composable
fun WeatherDetails(data: WeatherModel) {
    var textSize by remember { mutableStateOf(24.sp) }
    Spacer(modifier = Modifier.height(8.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "Location",
                tint = colorResource(R.color.white),
                modifier = Modifier.size(38.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                data.location.name,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )

            Text(
                "," + data.location.country,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
        Card(
            border = BorderStroke(3.dp, color = Color.Black),
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray,
                contentColor = Color.Yellow
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),


            ) {

            AsyncImage(
                modifier = Modifier
                    .size(140.dp)
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                contentDescription = "Condition"
            )
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, top = 0.dp, end = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "${data.current.temp_c}°C",
                    color = Color.DarkGray,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(",")
                Text(
                    data.current.condition.text,
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W200
                )
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
        Card(modifier = Modifier
            .width(300.dp)
            .height(200.dp)) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text("Humidity")
                        Text("${ data.current.humidity }%", fontSize = textSize ,
                            maxLines = 2, // limit max lines if you want
                            onTextLayout = { layoutResult ->
                                // This is where we detect the number of lines
                                val lineCount = layoutResult.lineCount
                                textSize = when (lineCount) {
                                    1 -> 30.sp
                                    2 -> 20.sp
                                    else -> 16.sp
                                } })
                    }
                    Column {
                        Text("Precip")
                        Text("${ data.current.precip_mm }mm")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text("Wind")
                        Text("${ data.current.wind_kph }kph")
                    }
                    Column {
                        Text("Nature")
                        Text(data.current.condition.text)
                    }
                }
            }
        }
    }
}