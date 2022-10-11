package co.edu.umb.mapsapplication.application.ui.screens

import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.edu.umb.mapsapplication.R
import co.edu.umb.mapsapplication.application.util.LocationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*

@Composable
fun MapScreen(fusedLocationProviderClient: FusedLocationProviderClient) {

  var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }

  val cameraPositionState = rememberCameraPositionState()
  cameraPositionState.position = CameraPosition.fromLatLngZoom(
    LocationUtils.getPosition(currentLocation),
    12f
  )

  var requestLocationUpdate by remember { mutableStateOf(true) }

  MyGoogleMap(
    currentLocation,
    cameraPositionState,
    onGpsIconClick = {
      requestLocationUpdate = true
    }
  )

  if (requestLocationUpdate) {
    LocationPermissionsAndSettingDialogs(
      updateCurrentLocation = {
        requestLocationUpdate = false
        LocationUtils.requestLocationResultCallback(fusedLocationProviderClient) { locationResult ->

          locationResult.lastLocation?.let { location ->
            currentLocation = location
          }

        }
      }
    )
  }
}

@Composable
private fun MyGoogleMap(
  currentLocation: Location,
  cameraPositionState: CameraPositionState,
  onGpsIconClick: () -> Unit
) {
  val mapUiSettings by remember {
    mutableStateOf(
      MapUiSettings(zoomControlsEnabled = true)
    )
  }

  GoogleMap(
    modifier = Modifier.fillMaxSize(),
    cameraPositionState = cameraPositionState,
    uiSettings = mapUiSettings
  ) {
    Marker(
      state = MarkerState(
        position = LocationUtils.getPosition(currentLocation)
      ),
      title = stringResource(R.string.current_position)
    )
  }

  GpsIconButton(onIconClick = onGpsIconClick)

  DebugOverlay(cameraPositionState)

}

@Composable
fun GpsIconButton(
  onIconClick: () -> Unit
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Bottom
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.End
    ) {
      IconButton(onClick = onIconClick) {
        Icon(
          modifier = Modifier.padding(bottom = 100.dp, end = 20.dp),
          painter = painterResource(id = R.drawable.ic_gps_fixed),
          contentDescription = "Gps Icon image"
        )
      }
    }
  }
}

@Composable
fun DebugOverlay(
  cameraPositionState: CameraPositionState,
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Bottom,
  ) {
    val moving =
      if (cameraPositionState.isMoving) "moving" else "not moving"
    Text(
      text = "Camera is $moving",
      fontWeight = FontWeight.Bold,
      color = Black
    )
    Text(
      text = "Camera position is ${cameraPositionState.position}",
      fontWeight = FontWeight.Bold,
      color = Black
    )
  }
}