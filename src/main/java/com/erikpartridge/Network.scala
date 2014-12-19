package com.erikpartridge


object Network {

  var cid :String = "0000000"

  var password:Array[Char] = new Array[Char](5)

  var connected:Boolean = false

  var commandFrequency = "199.997"

  def setCommandFrequency(newFreq:String) = {
    commandFrequency = newFreq
  }

  def write(message: String) = {
    //TODO
  }

  def connect = {
      //TODO
      connected = true
  }

  def disconnect = {
      //TODO

      connected = false
  }

  def setCid(controllerId: String): Unit ={
    cid = controllerId
  }

  def setPassword(pswd: String): Unit ={
    password = pswd.toCharArray
  }

}
