package com.erikpartridge.aircraft

import javafx.beans.property.{SimpleStringProperty, SimpleDoubleProperty, StringProperty, SimpleIntegerProperty}

import akka.actor.{ActorRef, Actor}
import com.erikpartridge.Router
import com.erikpartridge.approaches.LocApproach
import com.erikpartridge.general.{SLatLng, Weather}
import com.erikpartridge.navaids.{RnavFix, Navaid}
import com.javadocmd.simplelatlng.util.LengthUnit
import com.javadocmd.simplelatlng.{LatLng, LatLngTool}
import sun.plugin.dom.exception.InvalidStateException

import collection.mutable.HashMap
import collection.mutable.MutableList
import collection.immutable.Seq
import runtime.BoxedUnit
import scala.collection.mutable
import concurrent.Future

/**
 * This class represents an airplane, and extends the Akka Actor class to be notified by things
 */
sealed class Aircraft(val callsign: String, var flightplan: Flightplan, var altitude: Int, latLng: SLatLng, var speed: Int, var heading: Double){

  val BankAngle = 0.436332313d

  val DescentRate = .333333333d

  val ClimbRate = .45d

  var verticalSpeed = 0

  var assignedHeading = heading

  var assignedAltitude:Int = altitude

  var descentRate = 0

  var fixes = new MutableList[Navaid]

  var currentApproach = null

  var assignedSpeed = speed

  var turning = false

  var altChanging = false

  var forceLeft: Int = 2

  //ACTOR STUFF
  //
  //
  /**
   * Takes a String, and processes the command (cleans up and trims, then sends to parseMessage)
   * @return null
   */
  def receive(input: String): Unit = {
      if(input.startsWith(callsign)){
        val message = input.replaceAll(callsign, "").replace(",", "").trim
        val result = "RESPONSE-" + parseMessage(message)
        Router.router.receive(result)
      }
      BoxedUnit.UNIT
  }

  /**
   * @param message the message for the airplane, trimmed and cleaned up
   * @return a string response to the message
   */
  private def parseMessage(message: String): String ={
    //TODO
    ""
  }

  //END ACTOR
  //START GENERIC EXECUTION
  //
  def step(): Future[Unit] ={
    null
  }


  /**
   * @param approach the LocApproach to attempt to intercept
   * @return whether or not the capture was successful
   */
  private def interceptLoc(approach : LocApproach): Future[Boolean] = {
    //Make sure you can actually intercept the thing...
    if(!interceptable(approach)) Future.failed(new InvalidStateException("Was not in a position to intercept the localizer"))
    Future.successful(true)
    //TODO
  }


  /**
   *
   * @param left if the aircraft should turn left
   * @return a null future. Fully asynchronous
   */
  private def turn(left : Boolean): Future[Object] = {
    if(!turning) {
      turning = true
      while (!closeEnough(heading, assignedHeading, 3.0d)) {
        var newHeading = heading + turnAmount(1.0, left)
        //Calibrate
        if (newHeading > 360)
          newHeading -= 360
        //Calibrate
        else if (newHeading < 0)
          newHeading += 360
        heading = newHeading
        //Wait for the next click
        Thread sleep 1000
      }
      heading = assignedHeading
      turning = false
      forceLeft = 2
    }
    null
  }

  /**
   *
   * @param one the first double
   * @param two the second double
   * @param precision how close they need to be
   * @return if they are within the given precision
   */
  private def closeEnough(one: Double, two: Double, precision: Double): Boolean ={
    val radOne = Math.toRadians(one)
    val radTwo = Math.toRadians(two)
    val difference = Math.abs(Math.toDegrees(radOne - radTwo))
    difference < precision
  }

  /**
   * @return the difference in degrees between two headings -180 < x <= 180, left is negative
   */
  private def headingDifference: Double ={
    val diff = assignedHeading - heading
    val absDiff = Math.abs(diff)
    if(absDiff == 180.0d || absDiff == -180.0d)
      180
    else if(assignedHeading > heading)
      absDiff - 360
    else
      360 - absDiff
  }

  /**
   * @param seconds the number of seconds of the turn
   * @param left whether or not the aircraft is turning left
   * @param factor the factor for the bank angle
   * @return the number of degrees to turn, left is negative
   */
  private def turnAmount(seconds: Double, left: Boolean, factor: Double = 1): Double ={
    val difference = Math.abs(heading - assignedHeading)
    val radius = Math.pow(getTrueAirspeed, 2) / (68759 * BankAngle)
    val distance = 2 * Math.PI * radius / (difference / 360)
    val time = 3600.0d * distance / getTrueAirspeed
    val rate = difference / time
    if(left)
      -rate
    else
      rate
  }

  /**
   *
   * @return whether or not the aircraft should turn left (minimum distance)
   */
  private def turnLeft: Boolean={
    headingDifference < 0
  }

  /**
   *
   * @return the current true airspeed of the airplane
   */
  private def getTrueAirspeed: Int = {
    Math.round(speed + .02 * altitude / 1000.0).toInt
  }

  /**
   * A method that calculates whether or not this airplane can intercept the localizer
   * @param approach the approach that contains a localizer
   * @return if this airplane can intercept the localizer
   */
  private def interceptable(approach: LocApproach): Boolean = {
    val bearing = LatLngTool.initialBearing(approach.runway.touchdown.getLatLng, latLng.getLatLng)
    val north = bearing < 271 && bearing > 90
    //If you are south of the localizer, as in heading north, your current heading minus ILS' is positive
    val interceptableNorth = heading - approach.heading < 36 && heading - approach.heading > 0 && !north
    //If you are north of the localizer, your current heading minus the ILS should be negative
    val interceptableSouth = heading - approach.heading > -36 && heading - approach.heading < 0 && north
    interceptableNorth || interceptableSouth
  }

  /**
   * Calculates the next position for a given number of seconds, using current position, speed, and heading. Called post-turns or speed reductions.
   * @param time the number of simulated seconds that will pass
   * @return the new position
   */
  private def nextPosition(time: Double): LatLng = {
    LatLngTool.travel(latLng.getLatLng, heading, speedPerSecond() * time, LengthUnit.NAUTICAL_MILE)
  }


  /**
   *
   * @return the distance in nautical miles that this airplane travels each second
   */
  def speedPerSecond(): Double = {
    groundSpeed / 3600.0d
  }

  /**
   *
   * @return the ground speed in knots
   */
  def groundSpeed(): Double={
    val weather = Weather.getWeather
    val headingDiff = heading - weather.heading
    val windBonus = 1 - Math.cos(Math.toRadians(headingDiff)) * weather.wind
    speed - windBonus
  }

  /**
   *
   * @param other the other object
   * @return if the other object is an aircraft
   */
  def canEqual(other: Any): Boolean = other.isInstanceOf[Aircraft]

  def getLatLng(): SLatLng = latLng

  /**
   *
   * @param other the other aircraft to check equality with
   * @return if they are equal (LatLng and callsign same)
   */
  override def equals(other: Any): Boolean = other match {
    case that: Aircraft =>
      (that canEqual this) &&
        callsign == that.callsign &&
        latLng == that.getLatLng
    case _ => false
  }

  /**
   * @return the hashcode for this object
   */
  override def hashCode(): Int = {
    val state = Seq(callsign, latLng)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  def status(): String = {
    ""
  }
}

case class AircraftFX(callsign:SimpleStringProperty, altitude:SimpleIntegerProperty, latitude:SimpleDoubleProperty, longitude:SimpleDoubleProperty, speed: SimpleIntegerProperty, heading: SimpleIntegerProperty, status:SimpleStringProperty)


object Aircraft {


  /**
   * The hashmap that stores all aircraft
   */
  val aircraft = new HashMap[String, Aircraft]()

  def putAircraft(acf: Aircraft): Unit ={
    val result = aircraft.put(acf.callsign, acf)
  }

  def getAircraft(callsign: String): Option[Aircraft] ={
    aircraft.get(callsign)
  }

  def getAircraftFX(callsign: String): Option[AircraftFX] ={
    val acfNull = aircraft.get(callsign)
    if(acfNull.isEmpty){
      Option.empty
    }else{
      val acf = acfNull.get
      val callsign = new SimpleStringProperty(acf.callsign)
      val altitude = new SimpleIntegerProperty(acf.altitude)
      val latitude = new SimpleDoubleProperty(acf.getLatLng.getLatLng.getLatitude)
      val longitude = new SimpleDoubleProperty(acf.getLatLng.getLatLng.getLongitude)
      val speed = new SimpleIntegerProperty(acf.speed)
      val heading = new SimpleIntegerProperty(acf.heading.toInt)
      val status = "Assigned heading = " + acf.assignedHeading + ", altitude will be " + acf.assignedAltitude + " and speed to " + acf.assignedSpeed
      val acfFx = new AircraftFX(callsign,altitude,latitude,longitude,speed, heading, new SimpleStringProperty(status))
      Option.apply(acfFx)
    }
  }

}
