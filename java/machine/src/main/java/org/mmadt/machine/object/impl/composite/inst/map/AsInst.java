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

package org.mmadt.machine.object.impl.composite.inst.map;

import org.mmadt.language.compiler.Tokens;
import org.mmadt.machine.object.impl.TObj;
import org.mmadt.machine.object.impl.composite.TInst;
import org.mmadt.machine.object.impl.composite.TLst;
import org.mmadt.machine.object.impl.composite.TRec;
import org.mmadt.machine.object.model.Model;
import org.mmadt.machine.object.model.Obj;
import org.mmadt.machine.object.model.composite.Lst;
import org.mmadt.machine.object.model.composite.Rec;
import org.mmadt.machine.object.model.composite.inst.MapInstruction;
import org.mmadt.machine.object.model.composite.util.PList;
import org.mmadt.machine.object.model.composite.util.PMap;
import org.mmadt.processor.util.FastProcessor;
import org.mmadt.util.IteratorUtils;

import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class AsInst<S extends Obj> extends TInst<S, S> implements MapInstruction<S, S> {

    private AsInst(final Object arg) {
        super(PList.of(Tokens.AS, arg));
    }

    public S apply(final S obj) {
        return this.quantifyRange(obj.as(this.<S>argument(0).mapArg(obj)));
    }

    public static <S extends Obj> AsInst<S> create(final Object arg) {
        return new AsInst<>(arg);
    }

    public static <S extends Obj> S compute(final S from, final S to) {
        if (to.isSym()) {
            return from.label(to.label());
        } else if (from.isReference()) {
            return AsInst.<S>create(to).attach(from, to.label() == null ?
                    from :
                    fakeLabel(to.label(), from.model(from.model().write(to.access(from.access())))));
        } else if (from instanceof Lst && to instanceof Lst && null != from.get() && null != to.get()) {  // TODO: test()/match()/as() need to all become the same method!
            final Lst<Obj> fromList = (Lst<Obj>) from;
            final Lst<Obj> toList = (Lst<Obj>) to;
            if (toList.java().size() < fromList.java().size())
                return toList.kill();
            final PList<Obj> temp = new PList<>();
            Model model = from.model();
            for (int i = 0; i < toList.java().size(); i++) {
                //final Obj obj = IteratorUtils.orElse(FastProcessor.process(fromList.get(i).mapTo(toList.get(i))), TObj.none()); // TODO: get rid of this (the problem is we have to decide when a variable is bound in a reference
                final Obj obj = from.model().readOrGet(fromList.get(i), fromList.get(i).as(toList.get(i)));
                if (obj.q().isZero())
                    return toList.kill();
                model = model.write(obj);
                temp.add(obj);
            }
            return (S) TLst.of(temp).model(model).label(to.label());
        } else if (from instanceof Rec && to instanceof Rec && null != from.get() && null != to.get()) {  // TODO: test()/match()/as() need to all become the same method!
            final Rec<Obj, Obj> fromRec = (Rec<Obj, Obj>) from;
            final Rec<Obj, Obj> toRec = (Rec<Obj, Obj>) to;
            final PMap<Obj, Obj> temp = new PMap<>();
            Model model = from.model();
            for (final Map.Entry<Obj, Obj> toEntry : toRec.java().entrySet()) {
                final Map.Entry<Obj, Obj> fromEntry = fromRec.java().entrySet().stream()
                        .map(x -> Map.of(
                                from.model().<Obj>readOrGet(x.getKey(), x.getKey().model(from.model()).as(toEntry.getKey())),
                                from.model().<Obj>readOrGet(x.getValue(), x.getValue().model(from.model()).as(toEntry.getValue()))).entrySet().iterator().next())
                        .filter(x -> null != x.getKey() && !TObj.none().equals(x.getKey()) && null != x.getValue())
                        .findFirst()
                        .orElse(null);
                if (null == fromEntry)
                    return toRec.kill();
                model = model.write(fromEntry.getKey()).write(fromEntry.getValue());
                temp.put(fromEntry.getKey(), fromEntry.getValue());
            }
            return (S) TRec.of(temp).model(model).label(to.label());
        } else if (!to.test(from))
            return to.kill();
        else
            return (from.isType() ? from.set(to.get()) : from).symbol(to.symbol()).access(to.access()).label(to.label());
    }

    private static <S extends Obj> S fakeLabel(final String label, final S obj) { // TODO: this is lame
        final TObj temp = (TObj) obj.clone();
        temp.type = temp.type.label(label);
        return (S) temp;
    }
}