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

import org.mmadt.language.Tokens
import org.mmadt.language.obj.`type`.IntType
import org.mmadt.language.obj.op.initial.StartOp.StartInst
import org.mmadt.language.obj.{DomainInst,Int,IntQ,base,_}
import org.mmadt.storage.StorageFactory._

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
class TInt(name:String,quantifier:IntQ,via:DomainInst[Int]) extends AbstractTObj[Int](name,quantifier,via) with IntType {
  def this() = this(Tokens.int,qOne,base())
  def this(name:String) = this(name,qOne,base())
  override def hardQ(quantifier:IntQ):this.type = new TInt(name,quantifier,via).asInstanceOf[this.type]
  override def q(quantifier:IntQ):this.type ={
    this.via._2 match {
      case _:StartInst[_] => new TInt(name,quantifier,via).asInstanceOf[this.type]
      case x:Inst[Obj,Obj] if x.op() == "get" => new TInt(name,quantifier,via).asInstanceOf[this.type]
      case _ => new TInt(name,multQ(via._1,quantifier),(via._1,via._2.q(quantifier))).asInstanceOf[this.type]
    }
  }
}
