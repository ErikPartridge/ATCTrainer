package com.erikpartridge.approaches

import com.erikpartridge.airports.Runway


class LocApproach(val runway:Runway) {

  def heading(): Int = {
    runway.bearing
  }
}
