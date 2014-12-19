package com.erikpartridge.approaches

import com.erikpartridge.navaids.Navaid

import scala.collection.immutable.List

/**
 * Created by erik on 12/5/14.
 */
trait Approach {

  def getPath(): List[Navaid]

}
