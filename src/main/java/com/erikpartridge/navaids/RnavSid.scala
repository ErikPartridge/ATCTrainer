package com.erikpartridge.navaids


case class RnavSid(identifier: String, runwayTransitions: List[Transition], core: List[Navaid], fixTransitions: List[Transition])
