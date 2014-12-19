package com.erikpartridge.approaches

import com.erikpartridge.navaids.Navaid

import scala.collection.immutable.List

/**
 * Created by erik on 12/5/14.
 */
class RnavApproach(val fixes: List[Navaid]){

  this:Approach => def getPath: List[Navaid] = { fixes }

  def getMaximumInterceptAngle: Double ={
    180.0d
  }

}
