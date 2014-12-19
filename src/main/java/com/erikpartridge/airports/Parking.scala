package com.erikpartridge.airports

import com.erikpartridge.general.SLatLng


case class Parking(identifier: String, location: SLatLng) {
  if(identifier == "")
    throw new IllegalArgumentException("Identifier for parking spots cannot be null")
  if(location == null)
    throw new NullPointerException("The location for a parking spot cannot be null")
}
