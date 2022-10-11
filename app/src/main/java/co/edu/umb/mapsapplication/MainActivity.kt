package co.edu.umb.mapsapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import co.edu.umb.mapsapplication.application.ui.screens.MapScreen
import co.edu.umb.mapsapplication.application.ui.theme.MapsApplicationTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

  private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    setContent {
      MainScreen(fusedLocationProviderClient = fusedLocationProviderClient)
    }
  }
}

@Composable
fun MainScreen(
  fusedLocationProviderClient: FusedLocationProviderClient
) {
  MapsApplicationTheme {
    MapScreen(fusedLocationProviderClient = fusedLocationProviderClient)
  }
}