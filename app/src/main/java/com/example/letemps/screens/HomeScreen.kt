package com.example.letemps.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letemps.R
import com.example.letemps.weather.data.LocationService
import com.example.letemps.weather.viewmodel.WeatherViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: WeatherViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val context = LocalContext.current
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    // Fetch current location once
    LaunchedEffect(Unit) {
        val locService = LocationService(context)
        location = locService.getCurrentLocation()
        location?.let { (lat, lon) ->
            viewModel.fetchWeatherByCoordinates(lat, lon)
        }
    }

    val weatherState by viewModel.weatherState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    var lastRefreshTime by remember { mutableStateOf(0L) }

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
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    isRefreshing = true
                    lastRefreshTime = System.currentTimeMillis()
                    location?.let { (lat, lon) ->
                        viewModel.fetchWeatherByCoordinates(lat, lon)
                    }
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                weatherState?.let { weather ->
                    // Stop refreshing when weatherState updates after a refresh was triggered
                    LaunchedEffect(weatherState, lastRefreshTime) {
                        if (isRefreshing && lastRefreshTime > 0) {
                            isRefreshing = false
                        }
                    }

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
                        "sun" in condition.lowercase(Locale.getDefault()) || "clear" in condition.lowercase(
                            Locale.getDefault()
                        ) -> R.drawable.sunny

                        "storm" in condition.lowercase(Locale.getDefault()) -> R.drawable.stormy
                        else -> R.drawable.weather_icon
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Top Header Section with white text
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Welcome to SkyCast",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 22.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "👋",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 22.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Single Unified Weather Card with glass morphism effect
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
                                containerColor = Color.White.copy(alpha = 0.98f)
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(20.dp)
                            ) {

                                // Date, Day & Time Section - All in one row
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    // Date, Day of Week, and Time pill - Combined in one row
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
                                            // Date section
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
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

                                            // Time section
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
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

                                    // Location pill - Separate row
                                    Surface(
                                        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                                        color = Color(0xFFFCE7F3)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = 20.dp,
                                                vertical = 4.dp
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

                                // Weather Icon - Larger and more prominent
                                Image(
                                    painter = painterResource(id = weatherImageRes),
                                    contentDescription = "Weather Icon",
                                    modifier = Modifier
                                        .size(350.dp)
                                )

                                // Weather Condition with accent color - Changed to Black
                                Text(
                                    text = condition.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                                        else it.toString()
                                    },
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.Black,
                                        fontSize = 28.sp,
                                        letterSpacing = 0.5.sp
                                    ),
                                    textAlign = TextAlign.Center
                                )

                                // Temperature & Humidity in one row - Same color as date row
                                Surface(
                                    modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                                    color = Color(0xFFE0E7FF)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 0.5.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // Temperature section
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
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

                                        // Humidity section
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
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

                                // Refresh Button
//                                Button(
//                                    onClick = {
//                                        isRefreshing = true
//                                        lastRefreshTime = System.currentTimeMillis()
//                                        location?.let { (lat, lon) ->
//                                            viewModel.fetchWeatherByCoordinates(lat, lon)
//                                        }
//                                    },
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(top = 16.dp),
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = Color(0xFFE0E7FF)
//                                    ),
//                                    shape = RoundedCornerShape(20.dp)
//                                ) {
//                                    Text(
//                                        text = "Refresh to see current weather",
//                                        style = MaterialTheme.typography.titleMedium.copy(
//                                            fontFamily = FontFamily.SansSerif,
//                                            fontWeight = FontWeight.Bold,
//                                            color = Color(0xFF4338CA),
//                                            fontSize = 16.sp
//                                        ),
//                                        modifier = Modifier.padding(vertical = 8.dp)
//                                    )
//                                }
                            }
                        }
                    }


                } ?: Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(56.dp),
                            color = Color.White,
                            strokeWidth = 5.dp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Loading weather data...",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        )
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
}