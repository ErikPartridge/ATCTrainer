package com.erikpartridge.airports

import java.security.InvalidParameterException

import com.erikpartridge.general.SLatLng


case class Taxiway(identifier : String, points: List[SLatLng]) {

  if(identifier.length == 0)
    throw new InvalidParameterException("One of the taxiways has no name")
  else if(points.length == 0)
    throw new InvalidParameterException("Taxiway with identifier " + identifier + " has no coordinate list")

}
