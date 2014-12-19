package com.erikpartridge.general

import com.javadocmd.simplelatlng.LatLng

/**
  * @see LatLng
 *      A simple serialization wrapper for Java LatLng
 */
case class SLatLng(latitude: Double, longitude: Double) {
  def getLatLng: LatLng ={
    new LatLng(latitude, longitude)
  }
}

object SLatLng{

  def getRandom: SLatLng ={
    val ll =LatLng.random()
    new SLatLng(ll.getLatitude, ll.getLongitude)
  }
}