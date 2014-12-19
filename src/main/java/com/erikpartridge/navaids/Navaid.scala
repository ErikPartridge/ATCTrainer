package com.erikpartridge.navaids

import com.erikpartridge.general.SLatLng
import com.javadocmd.simplelatlng.util.LengthUnit
import com.javadocmd.simplelatlng.{LatLng, LatLngTool}

import scala.collection.mutable.HashMap

/**
 */
case class Navaid(identifier: String, latLng: SLatLng) {

  def getBearing(position: LatLng): Int = {
    val bearing = LatLngTool.initialBearing(position, latLng.getLatLng)
    Math.round(bearing).toInt
  }

  def getDistance(position: LatLng): Double = {
    LatLngTool.distance(position, latLng.getLatLng, LengthUnit.NAUTICAL_MILE)
  }

}

object Navaids{

  val navaids = new HashMap[String, Navaid]()

  def putNavaid(navaid: Navaid): Unit ={
    navaids.put(navaid.identifier, navaid)
    return
  }

  def putAllNavaids(navs: Seq[Navaid]): Unit ={
    for(nav:Navaid <- navs)
      navaids.put(nav.identifier, nav)
    return
  }
}