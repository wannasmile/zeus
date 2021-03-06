/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * 
 * GNU Lesser General Public License
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package JADE_SL.abs;

/**
 * @author Federico Bergenti - Universita` di Parma
 */
public class AbsAgentAction extends AbsConcept implements AbsContentElement {

    /**
     * Construct an Abstract descriptor to hold an agent action of
     * the proper type, e.g. SELL, BUY....
     * @param typeName The name of the type of the agent action held by 
     * this abstract descriptor.
     */
    public AbsAgentAction(String typeName) {
        super(typeName);
    }

    /**
     * Set an attribute of the agent action held by this
     * abstract descriptor.
     * @param name The name of the attribute to be set.
     * @param value The new value of the attribute.
     */
    public void set(String name, AbsPredicate value) {
        super.set(name, value);
    } 

}

