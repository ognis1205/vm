/*
 * Copyright (c) 2019-2029 RReduX,Inc. [http://rredux.com]
 *
 * This file is part of mm-ADT.
 *
 *  mm-ADT is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Affero General Public License as published by the
 *  Free Software Foundation, either version 3 of the License, or (at your option)
 *  any later version.
 *
 *  mm-ADT is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 *  License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with mm-ADT. If not, see <https://www.gnu.org/licenses/>.
 *
 *  You can be released from the requirements of the license by purchasing a
 *  commercial license from RReduX,Inc. at [info@rredux.com].
 */

package org.mmadt.language.obj.op.branch

import org.mmadt.language.Tokens
import org.mmadt.language.obj.`type`.RecType
import org.mmadt.language.obj.{Inst, OType, OValue, Obj}
import org.mmadt.storage.obj._
import org.mmadt.storage.obj.`type`.TObj
import org.mmadt.storage.obj.value.VInst

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
trait ChooseOp {
  this:Obj with ChooseOp =>

  def choose[IT <: OType,OT <: Obj](branches:(IT,OT)*):OT = this.choose(trec(branches.toMap))

  def choose[IT <: OType,OT <: Obj](branches:RecType[IT,OT]):OT ={
    this match {
      case atype:OType => atype.compose(generalType(branches.value().values),ChooseOp[IT,OT](branches))
      case avalue:OValue =>
        branches.value().find(p => asType(avalue).canonical().equals(p._1.canonical()) &&
                                   (avalue ===> p._1).hasNext).
          map(_._2).getOrElse(avalue.q(qZero))
        match {
          case btype:OType => (avalue ==> btype).asInstanceOf[OT]
          case bvalue:OT => bvalue.q(avalue.q())
        }
    }
  }

  private def generalType[OT <: Obj](outs:Iterable[OT]):OT ={
    val types = outs.map{
      case atype:OType => atype.range().asInstanceOf[OT]
      case avalue:OT => avalue
    }.toSet
    types.size match {
      case 1 => types.head
      case _ => new TObj().asInstanceOf[OT]
    }
  }
}

object ChooseOp {
  def apply[IT <: OType,OT <: Obj](branches:RecType[IT,OT]):Inst = new VInst((Tokens.choose,List(branches)),qOne,(a:Obj,b:List[Obj]) => a.choose(branches))
}