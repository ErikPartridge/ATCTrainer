package com.erikpartridge.json

import com.erikpartridge.aircraft.{Aircraft, Flightplan}
import com.erikpartridge.airports.{Parking, Airport, Runway, Taxiway}
import com.erikpartridge.general.SLatLng
import com.erikpartridge.graphics.Main
import com.erikpartridge.navaids._
import com.fasterxml.jackson.annotation.JsonValue
import play.api.libs.json._
import play.api.libs.functional.syntax._
import collection.immutable.Seq
import collection.immutable.List


/**
 * Created by erik on 12/12/14.
 */
object Reader {

  implicit val sLatLngReads : Reads[SLatLng] = (
      (JsPath \ "latitude").read[Double] and
      (JsPath \ "longitude").read[Double]
    )(SLatLng.apply _)

  implicit val parkingReads : Reads[Parking] = (
    (JsPath \ "identifier").read[String] and
      (JsPath \ "latLng").read[SLatLng]
    )(Parking.apply _)
  implicit val runwayReads : Reads[Runway] = (
    (JsPath \ "identifier").read[String] and
      (JsPath \ "bearing").read[Int] and
      (JsPath \ "touchdown").read[SLatLng]
    )(Runway.apply _)

  implicit val taxiwayReads: Reads[Taxiway] = (
    (JsPath \ "identifier").read[String] and
      (JsPath \ "points").read[List[SLatLng]]
    )(Taxiway.apply _)


  implicit val navaidReads: Reads[Navaid] = (
      (JsPath \ "identifier").read[String] and
        (JsPath \ "latLng").read[SLatLng]
    )(Navaid.apply _)

  implicit val rnavFixReads: Reads[RnavFix] = (
    (JsPath \ "identifier").read[String] and
      (JsPath \ "altitude").read[Int] and
      (JsPath \ "speed").read[Int]
    )(RnavFix.apply _)

  implicit val transitionReads: Reads[Transition] = (
    (JsPath \ "identifier").read[String] and
      (JsPath \ "rnavFixes").read[List[RnavFix]]
    )(Transition.apply _)

  implicit val rnavSidReads: Reads[RnavSid] = (
    (JsPath \ "identifier").read[String] and
    (JsPath \ "runwayTransitions").read[List[Transition]] and
      (JsPath \ "coreFixes").read[List[Navaid]] and
      (JsPath \ "fixTransitions").read[List[Transition]]
    )(RnavSid.apply _)

  implicit val rnavStarReads: Reads[RnavStar] = (
    (JsPath \ "identifier").read[String] and
      (JsPath \ "fixTransitions").read[List[Transition]] and
      (JsPath \ "coreFixes").read[List[Navaid]] and
      (JsPath \ "runwayTransitions").read[List[Transition]]
    )(RnavStar.apply _)

  implicit val airportReads : Reads[Airport] = (
    (JsPath \ "icao").read[String] and
      (JsPath \ "runways").read[List[Runway]] and
      (JsPath \ "taxiways").read[List[Taxiway]] and
      (JsPath\ "parkings").read[List[Parking]] and
      (JsPath\ "sids").read[List[RnavSid]] and
      (JsPath \ "stars").read[List[RnavStar]] and
      (JsPath \ "patternHeight").read[Int] and
      (JsPath \ "patternLength").read[Double]
    )(Airport.apply _)

  implicit val flightplanReads: Reads[Flightplan] = (
    (JsPath \ "departs").read[String] and
      (JsPath \ "arrives").read[String] and
      (JsPath \ "route").read[String] and
      (JsPath \ "remarks").read[String] and
      (JsPath \ "cruise").read[Int]
    )(Flightplan.apply _)
  /**
  implicit val aircraftReads: Reads[Aircraft] = new Reads[Aircraft] {
    override def reads(json: JsValue): JsResult[Aircraft] = {
      Json.obj(
        "callsign" -> (json \ "callsign").as[String],
        "flightplan" -> (json \ "flightplan").as[Map[String, String]],
        "altitude" -> (json \ "altitude").as[Int],
        "latitude" -> (json \ "latitude").as[Double],
        "longitude" -> (json \ "longitude").as[Double],
        "speed" -> (json \ "speed").as[Int],
        "heading" -> (json \ "heading").as[Double]
      ).as[JsResult[Aircraft]]
    }
  }
    */

  def getSLatLng(json : JsValue): SLatLng ={
    val success = json.validate(sLatLngReads).isSuccess
    if(!success) {
      Main.createErrorMessage("There was a problem loading a lat/lon coordinate")
      new SLatLng(0,0)
    }
    else{
      json.as(sLatLngReads)
    }
  }

  def getRunway(json : JsValue): Runway ={
    val success = json.validate(runwayReads).isSuccess
    if(!success) {
      Main.createErrorMessage("There was a problem loading a runway")
      new Runway("!", 0, new SLatLng(0,0))
    }
    else{
      json.as(runwayReads)
    }
  }

  def getTaxiway(json : JsValue): Taxiway ={
    val success = json.validate(taxiwayReads).isSuccess
    if(!success) {
      Main.createErrorMessage("There was a problem loading a taxiway")
      new Taxiway("&",List(new SLatLng(0,0)))
    }
    else{
      json.as(taxiwayReads)
    }
  }

  def getAirport(json : JsValue): Airport ={
    val success = json.validate(airportReads).isSuccess
    if(!success) {
      null
    }
    else{
      json.as(airportReads)
    }
  }

  def getNavaids(json : JsValue): List[Navaid] ={
    val response = json.validate[List[Navaid]].get
    response
  }

  def getAircraft(json: JsValue): Aircraft = {
    val response = json.validate[Map[String, String]].get
    val fp = new Flightplan(response.get("departs").get, response.get("arrives").get, response.get("route").get, response.get("remarks").get, response.get("cruise").get.toInt)
    val call = response.get("callsign").get
    val latlng = new SLatLng(response.get("latitude").get.toDouble, response.get("longitude").get.toDouble)
    val altitude = response.get("altitude").get.toInt
    val speed = response.get("speed").get.toInt
    val heading = response.get("heading").get.toDouble
    new Aircraft(call, fp,altitude, latlng, speed, heading)
  }
}
