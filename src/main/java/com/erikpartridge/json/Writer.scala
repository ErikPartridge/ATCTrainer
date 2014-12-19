package com.erikpartridge.json

import com.erikpartridge.airports.{Airport, Runway, Taxiway}
import com.erikpartridge.general.SLatLng
import com.erikpartridge.navaids._
import play.api.libs.json.{JsValue, Json, Writes}

/**
 * Writes to .json format
 */
object Writer {

  implicit val sLatLngWrites = new Writes[SLatLng] {
    def writes(latLng: SLatLng) = Json.obj(
      "latitude" -> latLng.latitude,
      "longitude" -> latLng.longitude
    )
  }

  implicit val runwayWrites = new Writes[Runway] {
    def writes(rwy: Runway) = Json.obj(
      "identifier" -> rwy.identifier,
      "bearing" -> rwy.bearing,
      "touchdown" -> rwy.touchdown
    )
  }

  implicit val taxiwayWrites = new Writes[Taxiway] {
    def writes(taxi: Taxiway) = Json.obj(
      "identifier" -> taxi.identifier,
      "points" -> taxi.points
    )
  }

  implicit val navaidWrites = new Writes[Navaid] {
    def writes(nav: Navaid) = Json.obj(
      "identifier" -> nav.identifier,
      "latLng" -> nav.latLng
    )
  }

  implicit val rnavFixWrites = new Writes[RnavFix] {
    def writes(rnavFix: RnavFix) = Json.obj(
      "identifier" -> rnavFix.identifier,
      "altitude" -> rnavFix.altitude,
      "speed" -> rnavFix.speed
    )
  }
  implicit val transitionWrites = new Writes[Transition] {
    def writes(trans : Transition) = Json.obj(
      "identifier" -> trans.identifier,
      "fixes" -> trans.fixes
    )
  }

  implicit val starWrites = new Writes[RnavStar] {
    def writes(rnav : RnavStar) = Json.obj(
      "identifier" -> rnav.identifier,
      "fixTransitions" -> rnav.fixTransitions,
      "core" -> rnav.core,
      "runwayTransitions" -> rnav.runwayTransitions
    )
  }

  implicit val sidWrites = new Writes[RnavSid] {
    def writes(sid: RnavSid) = Json.obj(
      "identifier" -> sid.identifier,
      "runwayTransitions" -> sid.runwayTransitions,
      "core" -> sid.core,
      "fixTransitions" -> sid.fixTransitions
    )
  }

  implicit val airportWrites = new Writes[Airport] {
    def writes(apt: Airport) = Json.obj(
      "icao" -> apt.icao,
      "runways" -> apt.runways,
      "taxiways" -> apt.taxiways
    )
  }


  def getAirportJson(apt : Airport): JsValue = {
    Json.toJson(apt)
  }

  def getNavaidsJson(navaids: List[Navaid]): JsValue = {
    Json.toJson(navaids)
  }

}
