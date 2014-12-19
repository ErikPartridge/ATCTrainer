package com.erikpartridge.general

import java.security.InvalidParameterException

import com.erikpartridge.graphics.Home

class Weather(val wind: Int, val heading: Int) {
  //START CONSTRUCTOR
  if(heading > 360 || heading < 0)
    throw new InvalidParameterException("Heading was invalid")
  else if(wind > 99 || wind < 0)
    throw new InvalidParameterException("Wind must be between 00-99, inclusive")
  //END CONSTRUCTOR

  /**
   * @return a string in form "[xx]x@yy"
   */
  override def toString: String ={
    heading + "@" + wind
  }

}


object Weather {

  var weather:Weather = new Weather(0,0)

  /**
   *
   * @return the current weather
   */
  def getWeather: Weather ={
    weather
  }

  /**
   * Sets the weather to the parsed contents of the string
   * @param input the string input in format "xxx@yy"
   * @return Unit
   */
  def generateWeather(input: String): Unit ={
    //From a string of say 290@15, get heading 290 and wind 15
    val heading = Integer.parseInt(input.split("@"){0})
    val wind = Integer.parseInt(input.split("@"){1})
    val taf = new Weather(wind, heading)
    weather = taf
  }

}

