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
import org.mmadt.language.obj._
import org.mmadt.language.obj.`type`.Type
import org.mmadt.language.obj.branch._
import org.mmadt.language.obj.op.BranchInstruction
import org.mmadt.storage.StorageFactory._
import org.mmadt.storage.obj.value.VInst

trait SplitOp {
  this: Obj =>
  def split[A <: Obj](coproduct: Coprod[A]): Branching[A] = {
    val branches: Coprod[A] = coproduct.clone(value = coproduct.value.map(x => if(this.test(x))Inst.resolveArg(this, x) else obj.q(0) ))
    var qTest = qOne
    branches.clone(value = branches.value.map(x => Option(
      if (this.test(x)) Inst.resolveArg(this, x)
      else obj.q(0)).filter(x => x.alive()).map(x => x.q(multQ(x.q, qTest))).map(x => {qTest = qZero; x }).getOrElse(obj.q(0)))).via(this, SplitOp(coproduct))
  }

  def split[A <: Obj](product: Prod[A]): Branching[A] = {
    val branches: Prod[A] = product.clone(value = product.value.map(x => Inst.resolveArg(this, x)))
    branches.via(this, SplitOp(branches))
  }
}

object SplitOp {
  def apply[A <: Obj](branches: Branching[A]): SplitInst[A] = new SplitInst[A](branches)

  class SplitInst[A <: Obj](branches: Branching[A], q: IntQ = qOne) extends VInst[A, Branching[A]]((Tokens.split, List(branches)), q) with BranchInstruction {
    override def q(quantifier: IntQ): this.type = new SplitInst[A](branches, quantifier).asInstanceOf[this.type]
    override def exec(start: A): Branching[A] = {
      (branches match {
        case coprod: Coprod[A] => start.split(Type.resolve(this, coprod))
        case prod: Prod[A] => start.split(Type.resolve(this, prod))
      }).via(start, this)
    }
  }

}
