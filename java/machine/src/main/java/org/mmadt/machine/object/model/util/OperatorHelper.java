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

package org.mmadt.machine.object.model.util;

import org.mmadt.language.compiler.Tokens;
import org.mmadt.machine.object.impl.atomic.TBool;
import org.mmadt.machine.object.impl.composite.TInst;
import org.mmadt.machine.object.model.Obj;
import org.mmadt.machine.object.model.composite.Inst;
import org.mmadt.util.IteratorUtils;

import java.util.function.Supplier;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class OperatorHelper {

    private OperatorHelper() {
        // static helper class
    }

    public static <A extends Inst> A operation(final String operator, final A lhs, final A rhs) {
        switch (operator) {
            case ("*"):
                return (A) lhs.mult(rhs);
            case ("+"):
                return (A) lhs.plus(rhs);
            case ("&"):
                return (A) lhs.and(rhs);
            case ("|"):
                return (A) lhs.or(rhs);
            case ("-"):
                return (A) lhs.minus(rhs);
            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }
    }

    public static <A extends Obj, B extends Obj> B binary(final String opcode, final Supplier<B> operator, final A objA, final A objB) {
        if (objA.isInstance())
            return operator.get();
        else
            return objA.access(objA.access().mult(TInst.of(opcode, objB)));//.domainAndRange(objA.access((TInst)null),objA.access((TInst)null))));

    }

    public static <A extends Obj, B extends Obj> B binary2(final String opcode, final Supplier<B> operator, final A objA, final A objB) {
        if (objA.isInstance() & objB.isInstance())
            return operator.get();
        else
            return TBool.some().access(objA.access().mult(TInst.of(opcode, objB).domainAndRange(objB,TBool.some())));

    }

    public static <A extends Obj> A unary(final String opcode, final Supplier<A> operator, final A objA) {
        if (objA.isInstance() || objA.isType()) // TODO: on the unary requires type (this will not be true when AND/OR move up the stack
            return operator.get();
        else
            return objA.access(objA.access().mult(TInst.of(opcode)));
    }

    public static Object tryCatch(final Supplier function, final Object failValue) {
        try {
            return function.get();
        } catch (final ArithmeticException e) {
            return failValue;
        }
    }
}
