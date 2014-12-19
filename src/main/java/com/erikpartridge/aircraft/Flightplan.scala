package com.erikpartridge.aircraft

/**
 * The flight plan of the aircraft, contains departure & arrival fields, cruise altitude, route, IFR/VFR, and lastly, remarks
 */
case class Flightplan(departs: String, arrives: String, route: String, remarks:String, cruise:Int) {

  def setDeparts(dep: String): Flightplan ={
    new Flightplan(dep, arrives,route, remarks, cruise)
  }

  def setArrives(arr: String): Flightplan = {
    new Flightplan(departs, arr,route, remarks,cruise)
  }

  def setScratchpad(sp : String): Flightplan = {
    new Flightplan(departs, arrives,route, remarks,cruise)
  }

  def setSquawk(code : Array[Int]): Flightplan = {
    if(code.length != 4){
      throw new IllegalArgumentException
    }else{
      for(c <- code){
        if(c < 0 || c > 7){
          throw new IllegalArgumentException
        }
      }
      new Flightplan(departs, arrives, route, remarks,cruise)
    }
  }
}
