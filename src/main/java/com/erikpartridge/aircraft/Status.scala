package com.erikpartridge.aircraft

/**
 * Created by erik on 12/13/14.
 */
object Status extends Enumeration{
  type Status = Value
  val Taxiing, TakeoffRnav, TakeoffHeading, Landing, Vnav, Flightplan, Approach, Vectors = Value
}

object FlightRules extends Enumeration{
  type FlightRules = Value
  val IFR, VFR = Value
  val SVFR, DVFR = VFR
}