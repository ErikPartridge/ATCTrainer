package com.erikpartridge.graphics

import com.erikpartridge.aircraft.Aircraft
import com.erikpartridge.airports.{Airport, Runway, Taxiway}
import com.erikpartridge.general.SLatLng
import com.erikpartridge.json.{Reader, Writer}
import com.erikpartridge.navaids.{Navaid, Navaids}
import org.controlsfx.dialog.Dialogs
import play.api.libs.json.Json

import collection.mutable
import concurrent.Future
import util.Random


object ScalaUtils {

  def loadAirport(json: String): Future[Object] ={
    val airport = Reader.getAirport(Json.parse(json))
    if(airport == null)
      Main.createErrorMessage("There was a problem loading airport")
    else
      Airport.putAirport(airport)
    null
  }

  def loadAircraft(json: String): Future[Object] ={
    val jval = Json.parse(json)
    Aircraft.putAircraft(Reader.getAircraft(jval))
    null
  }

  def loadNav(json: String): Future[Object]={
    val jVal = Json.parse(json)
    Navaids.putAllNavaids(Reader.getNavaids(jVal))
    null
  }

  def test(): Future[Object]={
    val rand = new Random
    var count = 0
    var navaids : mutable.MutableList[Navaid] = new mutable.MutableList()
    while(count < 20){
      val ident = getRandomFixName
      val nav = new Navaid(ident, SLatLng.getRandom)
      navaids += nav
      count += 1
    }
    val json = Writer.getNavaidsJson(navaids.toList)
    System.out.println(Json.prettyPrint(json))
    null
  }

  private def getRandomFixName: String={
    var result = ""
    var count = 0
    val rand = new Random
    while(count < 5){
      val c = (rand.nextInt(26) + 65).toChar
      result += c
      count += 1
    }
    result
  }


}
