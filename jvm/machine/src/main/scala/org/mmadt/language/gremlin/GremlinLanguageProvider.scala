/*
 * Copyright (c) 2019-2029 RReduX,Inc. [http://rredux.com]
 *
 * This file is part of mm-ADT.
 *
 * mm-ADT is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * mm-ADT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with mm-ADT. If not, see <https://www.gnu.org/licenses/>.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license from RReduX,Inc. at [info@rredux.com].
 */

package org.mmadt.language.gremlin
import java.util.Optional

import javax.script.{Bindings, ScriptEngineManager, SimpleBindings}
import org.mmadt.language.LanguageProvider
import org.mmadt.language.jsr223.mmADTScriptEngine

class GremlinLanguageProvider extends LanguageProvider {
  override val name: String = GremlinLanguageProvider._name
  override def getEngine: Optional[mmADTScriptEngine] = Optional.of(GremlinLanguageProvider.scriptEngine())
}

object GremlinLanguageProvider {
  private val _name: String = "gremlin"
  private lazy val scriptEngineManager: ScriptEngineManager = {
    val manager: ScriptEngineManager = new ScriptEngineManager() // want to constrain the manager to only accessing mmADTScriptEngines
    manager
  }
  private def scriptEngine(): mmADTScriptEngine = scriptEngineManager.getEngineByName(_name).asInstanceOf[GremlinScriptEngine]

  private def bindings(pairs: Tuple2[String, Any]*): Bindings = {
    val bindings: Bindings = new SimpleBindings()
    pairs.foreach(s => bindings.put(s._1, s._2))
    bindings
  }
}

