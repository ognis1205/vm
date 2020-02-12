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

import org.mmadt.language.obj.Str
import org.mmadt.language.obj.op._
import org.mmadt.language.obj.value.{BoolValue, StrValue}


/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
trait StrType extends Str
  with Type[StrType] {

  override def to(label: StrValue): StrType = this.push(ToOp(label)) //
  override def plus(other: StrType): StrType = this.push(PlusOp(other)) //
  override def plus(other: StrValue): StrType = this.push(PlusOp(other)) //
  override def gt(other: StrType): BoolType = this.bool(GtOp(other)) //
  override def gt(other: StrValue): BoolType = this.bool(GtOp(other)) //
  override def gt(): BoolType = this.bool(GtOp(this)) //
  override def is(bool: BoolType): StrType = this.push(IsOp(bool)).q(0, q()._2) //
  override def is(bool: BoolValue): StrType = this.push(IsOp(bool)).q(0, q()._2) //
}
