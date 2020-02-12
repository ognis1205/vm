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

package org.mmadt.language.obj.value

import org.mmadt.language.obj.Int
import org.mmadt.language.obj.`type`.{BoolType, IntType}
import org.mmadt.storage.obj.`type`.TInt
import org.mmadt.storage.obj.value.VInt

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
trait IntValue extends Int
  with Value[IntValue] {
  this: IntValue =>
  override def value(): Long //
  override def start(): IntType //

  override def to(label: StrValue): IntType = this.start().to(label) //
  override def plus(other: IntType): IntType = this.start().plus(other) //
  override def plus(other: IntValue): IntValue = Int.longToInt(this.value() + other.value()).as(name) //
  override def mult(other: IntType): IntType = this.start().mult(other) //
  override def mult(other: IntValue): IntValue = this.value() * other.value() //
  override def neg(): IntValue = -this.value() //
  override def gt(other: IntType): BoolType = this.start().gt(other) //
  override def gt(other: IntValue): BoolValue = this.value() > other.value() //
  override def gt(): BoolType = new TInt().gt(this) //
  override def is(bool: BoolType): IntType = this.start().is(bool) //
  override def is(bool: BoolValue): IntValue = if (bool.value()) this else this.q(0) //
}

object IntValue {
  implicit def longToInt(java: Long): IntValue with Int = new VInt(java) //
}
