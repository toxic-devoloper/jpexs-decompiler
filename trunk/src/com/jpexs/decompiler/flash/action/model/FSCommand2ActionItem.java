/*
 *  Copyright (C) 2010-2013 JPEXS
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.decompiler.flash.action.model;

import com.jpexs.decompiler.flash.action.flashlite.ActionFSCommand2;
import com.jpexs.decompiler.flash.action.swf4.ActionPush;
import com.jpexs.decompiler.graph.GraphSourceItem;
import com.jpexs.decompiler.graph.GraphSourceItemPos;
import com.jpexs.decompiler.graph.GraphTargetItem;
import com.jpexs.decompiler.graph.SourceGenerator;
import java.util.ArrayList;
import java.util.List;

public class FSCommand2ActionItem extends ActionItem {

    public String target;
    public List<GraphTargetItem> arguments;
    public GraphTargetItem command;

    @Override
    public List<GraphTargetItem> getAllSubItems() {
        List<GraphTargetItem> ret = new ArrayList<>();
        ret.addAll(arguments);
        ret.add(command);
        return ret;
    }

    public FSCommand2ActionItem(GraphSourceItem instruction, GraphTargetItem command, List<GraphTargetItem> arguments) {
        super(instruction, PRECEDENCE_PRIMARY);
        this.command = command;
        this.arguments = arguments;
    }

    @Override
    public String toString(boolean highlight, ConstantPool constants) {
        String paramStr = "";
        for (int t = 0; t < arguments.size(); t++) {
            paramStr += hilight(",", highlight);
            paramStr += arguments.get(t).toString(highlight, constants);
        }
        return hilight("FSCommand2(", highlight) + command.toString(highlight, constants) + paramStr + hilight(")", highlight);
    }

    @Override
    public List<GraphSourceItemPos> getNeededSources() {
        List<GraphSourceItemPos> ret = super.getNeededSources();
        ret.addAll(command.getNeededSources());
        for (GraphTargetItem ti : arguments) {
            ret.addAll(ti.getNeededSources());
        }
        return ret;
    }

    @Override
    public List<GraphSourceItem> toSource(List<Object> localData, SourceGenerator generator) {
        List<GraphSourceItem> ret = new ArrayList<>();
        for (GraphTargetItem a : arguments) {
            ret.addAll(a.toSource(localData, generator));
        }
        ret.addAll(command.toSource(localData, generator));
        ret.add(new ActionPush((Long) (long) arguments.size()));
        ret.add(new ActionFSCommand2());
        return ret;
    }

    @Override
    public boolean hasReturnValue() {
        return true; //FIXME ?
    }
}
