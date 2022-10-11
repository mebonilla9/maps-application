package co.edu.umb.mapsapplication.application.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

object LocationUtils {

  fun getDefaultLocation(): Location {
    val location = Location(LocationManager.GPS_PROVIDER)
    val cartagena = LatLng(10.4231546, -75.5547012)
    location.latitude = cartagena.latitude
    location.longitude = cartagena.longitude
    return location
  }

  fun getPosition(location: Location): LatLng {
    return LatLng(
      location.latitude,
      location.longitude
    )
  }

  @SuppressLint("MissingPermission")
  fun requestLocationResultCallback(
    fusedLocationProviderClient: FusedLocationProviderClient,
    locationResultCallback: (LocationResult) -> Unit
  ) {

    val locationCallback = object : LocationCallback() {

      override fun onLocationResult(locationResult: LocationResult) {
        super.onLocationResult(locationResult)
        locationResultCallback(locationResult)
        fusedLocationProviderClient.removeLocationUpdates(this)
      }
    }

    val locationRequest = LocationRequest.create().apply {
      interval = 0
      fastestInterval = 0
      priority = Priority.PRIORITY_HIGH_ACCURACY
    }

    Looper.myLooper()?.let { looper ->
      fusedLocationProviderClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        looper
      )
    }

  }

  fun isLocationPermissionGranted(context: Context): Boolean {
    return (ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED)
  }

}
