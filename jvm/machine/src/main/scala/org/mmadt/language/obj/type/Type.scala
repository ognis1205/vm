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

package org.mmadt.language.obj.`type`

import org.mmadt.language.obj.branch.Branching
import org.mmadt.language.obj.op.model.ModelOp
import org.mmadt.language.obj.op.sideeffect.AddOp
import org.mmadt.language.obj.op.traverser.ExplainOp
import org.mmadt.language.obj.value.Value
import org.mmadt.language.obj.{eqQ, _}
import org.mmadt.language.{LanguageException, LanguageFactory}
import org.mmadt.storage.StorageFactory._

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
trait Type[+T <: Obj] extends Obj
  with AddOp
  with ModelOp
  with ExplainOp {
  this: T =>

  // type signature properties and functions
  def range: this.type = this.isolate
  def domain[D <: Obj](): Type[D] = if (this.root) this.asInstanceOf[Type[D]] else asType[Obj](this.via._1).asInstanceOf[Type[D]].domain[D]()
  def <=[D <: Obj](domainType: Type[D]): this.type = {
    LanguageException.testDomainRange(this, domainType)
    domainType.compute(this).hardQ(this.q).asInstanceOf[this.type]
  }
  // pattern matching methods
  override def test(other: Obj): Boolean = other match {
    case argValue: Value[_] => TypeChecker.matchesTV(this, argValue)
    case argType: Type[_] => TypeChecker.matchesTT(this, argType)
  }
  // standard Java implementations
  override def toString: String = LanguageFactory.printType(this)
  override lazy val hashCode: scala.Int = this.name.hashCode ^ this.q.hashCode() ^ this.lineage.hashCode()
  override def equals(other: Any): Boolean = other match {
    case atype: Type[_] => atype.name.equals(this.name) && eqQ(atype, this) && ((this.root && atype.root) || (this.via == atype.via))
    case _ => false
  }

  // obj-level operations TODO: remove
  override def add[O <: Obj](obj: O): O = asType(obj).asInstanceOf[O].via(this, AddOp(obj))
}

object Type {
  // domain/range specifies anonymous types
  def resolve[R <: Obj](objA: Obj, objB: R): R = {
    objB match {
      case x: __ => x(objA)
//      case x: Branching[Obj] => x.clone(value = x.value.map(a => resolve(asType(objA).range, a))).asInstanceOf[R]
      case x: LstType[Obj] => tlst(name = x.name, value = x.value.map(a => resolve(objA, a)), q = x.q, via = x.via).asInstanceOf[R]
      case x: RecType[Obj, Obj] => trec(name = x.name, value = x.value.map(a => resolve(objA, a._1) -> resolve(objA, a._2)), q = x.q, via = x.via).asInstanceOf[R]
      case _ => objB
    }
  }
}
