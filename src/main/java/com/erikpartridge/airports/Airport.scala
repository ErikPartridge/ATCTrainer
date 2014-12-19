package com.erikpartridge.airports


import com.erikpartridge.navaids.{RnavSid, RnavStar}

import scala.collection.mutable

case class Airport(icao:String, runways: List[Runway], taxiways: List[Taxiway], parkings: List[Parking], sids: List[RnavSid], stars: List[RnavStar], patternHeight:Int, patternLength:Double){
  if(icao.length == 0)
    throw new IllegalArgumentException("An airport's icao code cannot be null")
  else if(runways.length == 0)
    throw new IllegalArgumentException("An airport must have at least one runway!")
  else if(taxiways.length == 0)
    throw new IllegalArgumentException("An airport must have at least one taxiway")
}

object Airport{
  val airports = new mutable.HashMap[String, Airport]

  def putAirport(apt: Airport): Unit={
    val result = airports.put(apt.icao, apt)
  }

  def getAirport(icao: String): Option[Airport] ={
    airports get icao
  }

}

