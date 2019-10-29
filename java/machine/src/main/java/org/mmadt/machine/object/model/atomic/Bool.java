/*
 * Copyright (c) 2019-2029 RReduX,Inc. [http://rredux.com]
 *
 * This file is part of mm-ADT.
 *
 * mm-ADT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mm-ADT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mm-ADT. If not, see <https://www.gnu.org/licenses/>.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license from RReduX,Inc. at [info@rredux.com].
 */

package org.mmadt.machine.object.model.atomic;

import org.mmadt.machine.object.impl.TObj;
import org.mmadt.machine.object.impl.atomic.TBool;
import org.mmadt.machine.object.model.Obj;
import org.mmadt.machine.object.model.type.algebra.WithCommutativeRing;
import org.mmadt.processor.util.MinimalProcessor;
import org.mmadt.util.IteratorUtils;

import java.util.List;

/**
 * A Java representation of the {@code bool} object in mm-ADT.
 * A {@code bool} is a commutative ring with unity.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface Bool extends WithCommutativeRing<Bool> {

    ////////////////////////////////
    //// BOOL AS THE EXPERIMENT ////
    ////////////////////////////////
    // CLONE ALGEBRA METHODS THROUGH TYPE INTERFACES IF THIS REDUCES TYPECASTING //
    @Override
    public Bool one();

    @Override
    public Bool zero();

    @Override
    public Bool mult(final Bool object);

    @Override
    public Bool plus(final Bool object);

    @Override
    public default Bool minus(final Bool object) {
        return this.plus(object.neg());
    }

    @Override
    public Bool neg();
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public default Boolean java() {
        return (Boolean) this.get();
    }

    public default Bool java(final Boolean bool) {
        return this.set(bool);
    }

    public default Bool and(final Bool bool) {
        return this.constant() ? this.java(this.java() && bool.java()) : (Bool) this.and((Obj) bool); // TODO: Bool.Type class with respective overloading
    }

    public default Bool or(final Bool bool) {
        return this.constant() ? this.java(this.java() || bool.java()) : (Bool) this.or((Obj) bool); // TODO: Bool.Type class with respective overloading
    }

    @Override
    public default Iterable<Bool> iterable() {
        return this.isInstance() ? List.of(this) : IteratorUtils.list(new MinimalProcessor<Bool, Bool>(this.access()).iterator(TBool.none()));
    }

}
