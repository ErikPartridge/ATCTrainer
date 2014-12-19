package com.erikpartridge.navaids


case class RnavStar(identifier: String, fixTransitions: List[Transition], core: List[Navaid], runwayTransitions: List[Transition])
