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

package org.mmadt.language.obj.op.map

import org.mmadt.language.obj.Inst.Func
import org.mmadt.language.obj._
import org.mmadt.language.obj.`type`.__
import org.mmadt.language.obj.value.strm.Strm
import org.mmadt.language.obj.value.{RecValue, Value}
import org.mmadt.language.{LanguageException, Tokens}
import org.mmadt.storage.StorageFactory._
import org.mmadt.storage.obj.value.VInst

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
trait PlusOp[O <: Obj] {
  this: O =>
  def plus(anon: __): this.type = PlusOp(anon).exec(this).asInstanceOf[this.type]
  def plus(arg: O): this.type = PlusOp(arg).exec(this).asInstanceOf[this.type]
  final def +(anon: __): this.type = this.plus(anon)
  final def +(arg: O): this.type = this.plus(arg)
}

object PlusOp extends Func[Obj, Obj] {
  def apply[O <: Obj](obj: Obj): Inst[Obj, Obj] = new VInst[Obj, Obj](g = (Tokens.plus, List(obj)), func = this)
  override def apply(start: Obj, inst: Inst[Obj, Obj]): Obj = {
    try {
      (start match {
        case _: Strm[_] => start
        case _: Value[_] => start match {
          case aint: Int => aint.clone(g = aint.g + inst.arg0[Int].g)
          case areal: Real => areal.clone(g = areal.g + inst.arg0[Real].g)
          case astr: Str => astr.clone(g = astr.g + inst.arg0[Str].g)
          case arec: RecValue[Value[Value[Obj]], Obj] => arec.clone(g = (arec.g._1, arec.gmap ++ inst.arg0[RecValue[Value[Obj], Value[Obj]]].gmap))
          case arec: ORecType => arec.clone(g = arec.gmap ++ inst.arg0[ORecType]().gmap)
          //////// EXPERIMENTAL
          case multA: Poly[Obj] if multA.isSerial => inst.arg0[Poly[Obj]] match {
            case multB: Poly[Obj] if multB.isSerial => multA `,` multB
            case plusB: Poly[Obj] if plusB.isPlus => lst(plusB.gsep, multA, plusB)
          }
          case plusA: Poly[Obj] if plusA.isPlus => inst.arg0[Poly[Obj]] match {
            case multB: Poly[Obj] if multB.isSerial => if (multB.isEmpty) plusA else lst(plusA.gsep, plusA, multB)
            case plusB: Poly[Obj] if plusB.isPlus => plusA.clone(g = (plusA.gsep, (plusA.glist ++ plusB.glist).toList))
          }
        }
        case _ => start
      }).via(start, inst)
    } catch {
      case _: ClassCastException => throw LanguageException.typingError(start, asType(inst.arg0[Obj])) // TODO: type check at VInst
    }
  }
}
