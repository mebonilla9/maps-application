package co.edu.umb.mapsapplication.application.ui.screens

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import co.edu.umb.mapsapplication.application.util.LocationUtils

@Composable
fun LocationPermissionsAndSettingDialogs(
  updateCurrentLocation: () -> Unit,
) {
  var requestLocationSetting by remember { mutableStateOf(false) }

  if (LocationUtils.isLocationPermissionGranted(LocalContext.current)) {
    SideEffect {
      requestLocationSetting = true
    }
  } else {
    LocationPermissionsDialog(
      onPermissionGranted = { requestLocationSetting = true },
      onPermissionDenied = { requestLocationSetting = true }
    )
  }

  if (requestLocationSetting) {
    LocationSettingDialog(
      onSuccess = {
        requestLocationSetting = false
        updateCurrentLocation()
      },
      onFailure = {
        requestLocationSetting = false
        updateCurrentLocation()
      }
    )
  }
}