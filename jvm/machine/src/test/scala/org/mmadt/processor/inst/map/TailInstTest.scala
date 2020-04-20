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

package org.mmadt.processor.inst.map

import org.mmadt.language.LanguageException
import org.mmadt.language.obj.value.StrValue
import org.mmadt.language.obj.{Lst, Obj}
import org.mmadt.storage.StorageFactory._
import org.scalatest.FunSuite
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor2}

class TailInstTest extends FunSuite with TableDrivenPropertyChecks {

  test("[tail] w/ values") {
    val check: TableFor2[Lst[_], Obj] =
      new TableFor2(("list", "tail"),
        (vlst[StrValue]("a"), vlst()),
        (vlst[StrValue]("a", "b"), vlst(str("b"))),
        (vlst[StrValue]("a", "b", "c"), vlst(str("b"), str("c"))),
        (vlst[StrValue]("d", "b", "c"), vlst(str("b"), str("c"))),
      )
    forEvery(check) { (left, right) => {
      assertResult(right)(left.tail())
    }
    }
  }

  test("[tail] exception") {
    assertThrows[LanguageException] {
      vlst().tail()
    }
  }
}