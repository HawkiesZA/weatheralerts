package za.co.hawkiesza.weatheralerts.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import za.co.hawkiesza.weatheralerts.R
import za.co.hawkiesza.weatheralerts.ui.theme.WeatherAlertsTheme
import za.co.hawkiesza.weatheralerts.utils.Status
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

val CUSTOM_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect {
                    setContent {
                        WeatherAlertsTheme {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                when(it.status) {
                                    Status.SUCCESS -> {
                                        if (it.data == null) {
                                            FullScreenText(message = getString(R.string.no_weather_events_found))
                                        } else {
                                            WeatherAlertList(state = it.data)
                                        }
                                    }
                                    Status.ERROR -> {
                                        FullScreenText(message = getString(R.string.something_went_wrong))
                                    }
                                    Status.LOADING -> {
                                        LoadingIndicator()
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FullScreenText(message: String) {
    Column (
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FullScreenTextPreview() {
    WeatherAlertsTheme {
        FullScreenText(stringResource(id = R.string.something_went_wrong))
    }
}

@Composable
fun LoadingIndicator() {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingIndicatorPreview() {
    WeatherAlertsTheme {
        LoadingIndicator()
    }
}

@Composable
fun WeatherAlert(event: WeatherEvent) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        AsyncImage(
            model = event.imageUrl,
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = stringResource(R.string.picsum_image),
            modifier = Modifier
                .clip(CircleShape)
                .height(64.dp)
                .width(64.dp)
        )
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = event.eventName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = event.senderName,
                fontSize = 14.sp
            )
            if (event.effectiveDate != null && event.ends != null) {
                Row {
                    Text(
                        text = event.effectiveDate.format(CUSTOM_FORMATTER),
                        fontSize = 12.sp
                    )
                    Text(
                        text = " - ",
                        fontSize = 12.sp
                    )
                    Text(
                        text = event.ends.format(CUSTOM_FORMATTER),
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = stringResource(
                        R.string.days_hours_minutes,
                        event.duration.toDaysPart(),
                        event.duration.toHoursPart(),
                        event.duration.toMinutesPart()
                    ),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherAlertPreview() {
    WeatherAlertsTheme {
        WeatherAlert(
            WeatherEvent(
                imageUrl = "",
                eventName = "Special Weather Statement",
                effectiveDate = OffsetDateTime.now(),
                ends = OffsetDateTime.now(),
                senderName = "NWS Green Bay WI",
            )
        )
    }
}

@Composable
fun WeatherAlertList(state: MainState, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(state.weatherEvents) { alert ->
            WeatherAlert(alert)
        }
    }
}