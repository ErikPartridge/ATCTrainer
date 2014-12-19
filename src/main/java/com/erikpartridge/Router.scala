package com.erikpartridge

import akka.actor.Actor
import com.erikpartridge.aircraft.Aircraft

import scala.concurrent.Future


class Router{

  def receive(message: String): Unit ={
      if(!Aircraft.getAircraft(message.split(","){0}).isEmpty){
        val acf = Aircraft.getAircraft(message.split(","){0})
        acf.get.receive(message)
      }else if(message.startsWith("RESPONSE-")){
        Network.write(message.replaceAll("RESPONSE-", ""))
      }else{
        Network.write("ERROR: command " + message + " not found")
      }
  }
}

object Router{

  var paused = false

  val router: Router = new Router


  def run(): Future[Unit] ={
    var count:Int = 0
    while(true){
      if(!paused){
        for(acf <- Aircraft.aircraft.values){
          acf.step()
        }
        count += 1
      }
      if(count == 5){
        updateAircraftTable()
      }
      Thread sleep 1000
    }
    null
  }

  def updateAircraftTable(): Future[Unit] ={
    null
  }


}
