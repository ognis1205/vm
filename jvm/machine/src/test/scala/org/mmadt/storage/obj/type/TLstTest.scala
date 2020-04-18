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

package org.mmadt.storage.obj.`type`

import org.mmadt.language.obj.value.{IntValue, StrValue}
import org.mmadt.storage.StorageFactory._
import org.scalatest.FunSuite
import org.scalatest.prop.TableDrivenPropertyChecks

class TLstTest extends FunSuite with TableDrivenPropertyChecks {

  test("...") {
    println((Nil ++ "a" ++ "b" ++ "c").tail)
    println(lst[StrValue].append("a").append("b").append("c").tail())
  }

  test("lst head/tal toString") {
    println(List(1, 2, 3).head)
    println(List(1, 2, 3).tail)
    println(lst[IntValue].append(1).append(2).append(3).tail())
  }

}