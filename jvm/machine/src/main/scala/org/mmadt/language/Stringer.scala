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

package org.mmadt.language

import org.mmadt.language.obj.`type`.Type
import org.mmadt.language.obj.value.{RecValue, StrValue, Value}
import org.mmadt.language.obj.{Inst, Obj, TQ}
import org.mmadt.processor.Traverser
import org.mmadt.storage.obj._

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
object Stringer {

  def qString(x: TQ): String = x match {
    case `qOne` => ""
    case `qZero` => "{0}"
    case `qMark` => "{?}"
    case `qPlus` => "{+}"
    case `qStar` => "{*}"
    case (x, y) if (x == y) => "{" + x + "}"
    case (x, y) if (y == int(Long.MaxValue)) => "{" + x + ",}"
    case (x, y) if (x == int(Long.MinValue)) => "{," + y + "}"
    case _ => "{" + x._1.value() + "," + x._2.value() + "}"
  }

  def traverserString(trav: Traverser[_]): String = {
    "[" + trav.obj() + "|" + trav.state().foldRight("")((x, string) => string + x._1.toString.replace("'", "") + "->" + x._2 + ",").dropRight(1) + "]"
  }

  def typeString(t: Type[_]): String = {
    if (t.insts().nonEmpty && t.insts().head._2.op().equals(Tokens.choose)) // TODO: ghetto union type specification
      return t.insts().head._2.arg[RecValue[_, _]]().value().foldRight("[")((x, string) => string + (if (x._1.equals(x._2)) x._1 else x._1 + ":" + x._2) + "|").dropRight(1) + "]"

    val range = Tokens.symbol(t) + qString(t.q())
    val domain = if (t.insts().isEmpty) "" else
      Tokens.symbol(t.insts().head._1) + qString(t.insts().head._1.q())
    (if (domain.equals("") || range.equals(domain)) range else range + "<=" + domain) +
      t.insts().map(_._2.toString()).fold("")((a, b) => a + b)
  }

  def valueString(v: Value[_]): String = v match {
    case x: RecValue[_, _] => (if (Tokens.named(x.name)) x.name else "") + x.value().foldRight("[")((x, string) => string + x._1 + ":" + x._2 + ",").dropRight(1) + "]"
    case x: StrValue => "'" + v.value() + "'" + qString(x.q())
    case _ => v.value() + qString(v.q())
  }


  def instString(inst: Inst): String = {
    inst.op() match {
      // case Tokens.to => "~" + inst.arg[StrValue]()
      case _ => inst.args() match {
        case Nil => "[" + inst.op() + "]"
        case args: List[Obj] => "[" + inst.op() + "," + args.map(x => x.toString + ",").fold("")((a, b) => a + b).dropRight(1) + "]"
      }
    }
  }
}
