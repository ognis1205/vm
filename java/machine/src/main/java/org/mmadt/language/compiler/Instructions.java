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

package org.mmadt.language.compiler;

import org.mmadt.machine.object.impl.composite.inst.barrier.DedupInst;
import org.mmadt.machine.object.impl.composite.inst.branch.BranchInst;
import org.mmadt.machine.object.impl.composite.inst.branch.ChooseInst;
import org.mmadt.machine.object.impl.composite.inst.filter.IdInst;
import org.mmadt.machine.object.impl.composite.inst.filter.IsInst;
import org.mmadt.machine.object.impl.composite.inst.initial.StartInst;
import org.mmadt.machine.object.impl.composite.inst.map.AInst;
import org.mmadt.machine.object.impl.composite.inst.map.AccessInst;
import org.mmadt.machine.object.impl.composite.inst.map.AndInst;
import org.mmadt.machine.object.impl.composite.inst.map.DivInst;
import org.mmadt.machine.object.impl.composite.inst.map.EqInst;
import org.mmadt.machine.object.impl.composite.inst.map.GetInst;
import org.mmadt.machine.object.impl.composite.inst.map.GtInst;
import org.mmadt.machine.object.impl.composite.inst.map.GteInst;
import org.mmadt.machine.object.impl.composite.inst.map.InvInst;
import org.mmadt.machine.object.impl.composite.inst.map.LtInst;
import org.mmadt.machine.object.impl.composite.inst.map.LteInst;
import org.mmadt.machine.object.impl.composite.inst.map.MapInst;
import org.mmadt.machine.object.impl.composite.inst.map.MinusInst;
import org.mmadt.machine.object.impl.composite.inst.map.ModelInst;
import org.mmadt.machine.object.impl.composite.inst.map.MultInst;
import org.mmadt.machine.object.impl.composite.inst.map.NegInst;
import org.mmadt.machine.object.impl.composite.inst.map.NeqInst;
import org.mmadt.machine.object.impl.composite.inst.map.OneInst;
import org.mmadt.machine.object.impl.composite.inst.map.OrInst;
import org.mmadt.machine.object.impl.composite.inst.map.PlusInst;
import org.mmadt.machine.object.impl.composite.inst.map.QInst;
import org.mmadt.machine.object.impl.composite.inst.map.ZeroInst;
import org.mmadt.machine.object.impl.composite.inst.reduce.CountInst;
import org.mmadt.machine.object.impl.composite.inst.reduce.GroupCountInst;
import org.mmadt.machine.object.impl.composite.inst.reduce.SumInst;
import org.mmadt.machine.object.impl.composite.inst.sideeffect.DropInst;
import org.mmadt.machine.object.impl.composite.inst.sideeffect.ExplainInst;
import org.mmadt.machine.object.impl.composite.inst.sideeffect.PutInst;
import org.mmadt.machine.object.model.Obj;
import org.mmadt.machine.object.model.composite.Inst;
import org.mmadt.machine.object.model.type.PList;

import static org.mmadt.language.compiler.Tokens.A;
import static org.mmadt.language.compiler.Tokens.ACCESS;
import static org.mmadt.language.compiler.Tokens.AND;
import static org.mmadt.language.compiler.Tokens.BRANCH;
import static org.mmadt.language.compiler.Tokens.CHOOSE;
import static org.mmadt.language.compiler.Tokens.COUNT;
import static org.mmadt.language.compiler.Tokens.DB;
import static org.mmadt.language.compiler.Tokens.DEDUP;
import static org.mmadt.language.compiler.Tokens.DIV;
import static org.mmadt.language.compiler.Tokens.DROP;
import static org.mmadt.language.compiler.Tokens.EQ;
import static org.mmadt.language.compiler.Tokens.EXPLAIN;
import static org.mmadt.language.compiler.Tokens.GET;
import static org.mmadt.language.compiler.Tokens.GROUPCOUNT;
import static org.mmadt.language.compiler.Tokens.GT;
import static org.mmadt.language.compiler.Tokens.GTE;
import static org.mmadt.language.compiler.Tokens.ID;
import static org.mmadt.language.compiler.Tokens.INV;
import static org.mmadt.language.compiler.Tokens.IS;
import static org.mmadt.language.compiler.Tokens.LT;
import static org.mmadt.language.compiler.Tokens.LTE;
import static org.mmadt.language.compiler.Tokens.MAP;
import static org.mmadt.language.compiler.Tokens.MINUS;
import static org.mmadt.language.compiler.Tokens.MULT;
import static org.mmadt.language.compiler.Tokens.NEG;
import static org.mmadt.language.compiler.Tokens.NEQ;
import static org.mmadt.language.compiler.Tokens.ONE;
import static org.mmadt.language.compiler.Tokens.OR;
import static org.mmadt.language.compiler.Tokens.PLUS;
import static org.mmadt.language.compiler.Tokens.PUT;
import static org.mmadt.language.compiler.Tokens.Q;
import static org.mmadt.language.compiler.Tokens.START;
import static org.mmadt.language.compiler.Tokens.SUM;
import static org.mmadt.language.compiler.Tokens.ZERO;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class Instructions {

    private Instructions() {
        // static helper class
    }

    public static Inst compile(final Inst inst) {
        final PList<Obj> args = new PList<>();
        args.addAll(inst.<PList<Obj>>get());
        // model handling
        final String opcode = args.get(0).get();
        if (opcode.startsWith(Tokens.EQUALS))
            return ModelInst.create(opcode.substring(1));
        args.remove(0); // drop the opcode
        switch (opcode) {
            case A:
                return AInst.create(args.get(0));
            case ACCESS:
                return AccessInst.create();
            case AND:
                return AndInst.create(args.toArray(new Object[]{}));
            case BRANCH:
                return BranchInst.create(args.toArray(new Object[]{}));
            case COUNT:
                return CountInst.create();
            case CHOOSE:
                return ChooseInst.create(args.toArray(new Object[]{}));
            case DEDUP:
                return DedupInst.create(args.toArray(new Object[]{}));
            case DIV:
                return DivInst.create(args.get(0));
            case DROP:
                return DropInst.create(args.get(0));
            case EQ:
                return EqInst.create(args.get(0));
            case EXPLAIN:
                return ExplainInst.create(args.get(0));
            case GET:
                return GetInst.create(args.get(0));
            case GT:
                return GtInst.create(args.get(0));
            case GTE:
                return GteInst.create(args.get(0));
            case GROUPCOUNT:
                return GroupCountInst.create(args.get(0));
            case ID:
                return IdInst.create();
            case INV:
                return InvInst.create();
            case IS:
                return IsInst.create(args.get(0));
            case LT:
                return LtInst.create(args.get(0));
            case LTE:
                return LteInst.create(args.get(0));
            case MAP:
                return MapInst.create(args.get(0));
            case MINUS:
                return MinusInst.create(args.get(0));
            case MULT:
                return MultInst.create(args.get(0));
            case NEG:
                return NegInst.create();
            case NEQ:
                return NeqInst.create(args.get(0));
            case ONE:
                return OneInst.create();
            case OR:
                return OrInst.create(args.toArray(new Object[]{}));
            case PLUS:
                return PlusInst.create(args.get(0));
            case PUT:
                return PutInst.create(args.get(0), args.get(1));
            case Q:
                return QInst.create();
            case SUM:
                return SumInst.create();
            case START:
                return StartInst.create(args.toArray(new Object[]{}));
            case ZERO:
                return ZeroInst.create();
            case DB:
                return IdInst.create();
            default:
                throw new RuntimeException("Unknown instruction: " + opcode);
        }
    }
}
