package com.example.letemps.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.letemps.R
import com.example.letemps.weather.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: WeatherViewModel = viewModel()) {
    var cityName by remember { mutableStateOf("") }
    val weatherData by viewModel.weatherState.collectAsState()
    val isDarkMode = isSystemInDarkTheme()

    Scaffold(
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E3A8A), // Deep blue
                            Color(0xFF3B82F6), // Bright blue
                            Color(0xFF60A5FA)  // Light blue
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search Weather",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 22.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🔍",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 22.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Input Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.weight(0.15f)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.98f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = cityName,
                            onValueChange = { cityName = it },
                            label = { Text("Enter city name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4338CA),
                                focusedLabelColor = Color(0xFF4338CA)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (cityName.isNotBlank()) {
                                    viewModel.fetchWeatherByCity(cityName)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4338CA)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            enabled = cityName.isNotBlank()
                        ) {
                            Text(
                                text = "Search City Name",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                              //  modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Weather Display Card
                if (weatherData != null) {
                    val weather = weatherData!!

                    // Date, Time & Day - Using CURRENT time with weather location's timezone
                    val currentUtcSeconds = System.currentTimeMillis() / 1000L
                    val localTimeMillis = (currentUtcSeconds + weather.timezone) * 1000L
                    val date = Date(localTimeMillis)

                    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                        time = date
                    }

                    val dayNumber = utcCalendar.get(Calendar.DAY_OF_MONTH)
                    val daySuffix = getDayOfMonthSuffix(utcCalendar)

                    val monthFormatter = SimpleDateFormat("MMMM", Locale.getDefault()).apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                    }
                    val monthName = monthFormatter.format(date)

                    val dayOfWeekFormatter = SimpleDateFormat("EEEE", Locale.getDefault()).apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                    }
                    val dayOfWeek = dayOfWeekFormatter.format(date)

                    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault()).apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                    }
                    val time = timeFormatter.format(date)

                    // Weather image and condition
                    val condition = weather.weather[0].description
                    val weatherImageRes = when {
                        "cloud" in condition.lowercase(Locale.getDefault()) -> R.drawable.cloudy
                        "rain" in condition.lowercase(Locale.getDefault()) -> R.drawable.rainy
                        "sun" in condition.lowercase(Locale.getDefault()) || "clear" in condition.lowercase(Locale.getDefault()) -> R.drawable.sunny
                        "storm" in condition.lowercase(Locale.getDefault()) -> R.drawable.stormy
                        else -> R.drawable.weather_icon
                    }

                    // Dark mode colors
                    val cardBackgroundColor = if (isDarkMode) Color(0xFF1F1F1F) else Color.White.copy(alpha = 0.98f)
                    val textColor = if (isDarkMode) Color.White else Color.Black

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                           .weight(1f)
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(32.dp),
                                ambientColor = Color.Black.copy(alpha = 0.2f)
                            ),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardBackgroundColor
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                              //  .verticalScroll(rememberScrollState())
                        ) {
                            // Date, Day & Time Section
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Date, Day of Week, and Time pill
                                Surface(
                                    modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                                    color = Color(0xFFE0E7FF)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "$dayOfWeek, $dayNumber$daySuffix $monthName",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontFamily = FontFamily.SansSerif,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF4338CA),
                                                    fontSize = 16.sp
                                                )
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = time,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontFamily = FontFamily.SansSerif,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF4338CA),
                                                    fontSize = 16.sp
                                                )
                                            )
                                        }
                                    }
                                }

                                // Location pill
                                Surface(
                                    modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                                    color = Color(0xFFFCE7F3)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(
                                            horizontal = 20.dp,
                                            vertical = 1.dp
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "📍",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontSize = 20.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = weather.name,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontFamily = FontFamily.SansSerif,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFC026D3),
                                                fontSize = 16.sp
                                            )
                                        )
                                    }
                                }
                            }

                            // Weather Icon
                            Image(
                                painter = painterResource(id = weatherImageRes),
                                contentDescription = "Weather Icon",
                                modifier = Modifier.size(150.dp)
                            )

                            // Weather Condition
                            Text(
                                text = condition.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                                    else it.toString()
                                },
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = textColor,
                                    fontSize = 28.sp,
                                    letterSpacing = 0.5.sp
                                ),
                                textAlign = TextAlign.Center
                            )

                            // Temperature & Humidity
                            Surface(
                                modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                                color = Color(0xFFE0E7FF)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "🌡️",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontSize = 24.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "${weather.main.temp.toInt()}°C",
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontFamily = FontFamily.SansSerif,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF4338CA),
                                                fontSize = 16.sp
                                            )
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "💧",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontSize = 24.sp
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "${weather.main.humidity}%",
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontFamily = FontFamily.SansSerif,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF4338CA),
                                                fontSize = 16.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Empty state card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(32.dp)
                            ),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.98f)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🌤️",
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        fontSize = 80.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Search for a city to see weather",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Gray,
                                        fontSize = 16.sp
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getDayOfMonthSuffix(cal: Calendar): String {
    return when (val day = cal.get(Calendar.DAY_OF_MONTH)) {
        1, 21, 31 -> "st"
        2, 22 -> "nd"
        3, 23 -> "rd"
        else -> "th"
    }
}