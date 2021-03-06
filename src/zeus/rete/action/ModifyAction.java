/*
* The contents of this file are subject to the BT "ZEUS" Open Source 
* Licence (L77741), Version 1.0 (the "Licence"); you may not use this file 
* except in compliance with the Licence. You may obtain a copy of the Licence
* from $ZEUS_INSTALL/licence.html or alternatively from
* http://www.labs.bt.com/projects/agents/zeus/licence.htm
* 
* Except as stated in Clause 7 of the Licence, software distributed under the
* Licence is distributed WITHOUT WARRANTY OF ANY KIND, either express or 
* implied. See the Licence for the specific language governing rights and 
* limitations under the Licence.
* 
* The Original Code is within the package zeus.*.
* The Initial Developer of the Original Code is British Telecommunications
* public limited company, whose registered office is at 81 Newgate Street, 
* London, EC1A 7AJ, England. Portions created by British Telecommunications 
* public limited company are Copyright 1996-9. All Rights Reserved.
* 
* THIS NOTICE MUST BE INCLUDED ON ANY COPY OF THIS FILE
*/


package zeus.rete.action;
import zeus.rete.*;
import zeus.concepts.fn.*; 
import zeus.concepts.*;
import zeus.util.*;
import java.util.*;
/** 
    
    @author Simon Thompson
    @since 1.1
    */
public class  ModifyAction extends ReteAction{
    
    /**
        An action that gets a fact and then modifies it before putting it back
        @see ReteAction
        */
    public void executeAction (Action a, Info info) { 
              ValueFunction type_var = ((VarFn)a.head).resolve(info.getBindings());
              Assert.notFalse(type_var.getID() == ValueFunction.TYPE);
              boolean found = false;
              for(int k = 0; !found && k < info.getInput().size(); k++ ) {
                 Fact f1 = (Fact)info.getInput().elementAt(k);
                 found = (f1.functor()).equals(type_var);
                 if ( found ) {
                    Core.DEBUG(2," <== " + f1);
                    retract(f1);
                    Enumeration enum = a.table.keys();
                    while( enum.hasMoreElements() ) {
                       String attribute = (String)enum.nextElement();
                       ValueFunction value = (ValueFunction)a.table.get(attribute);
                       value = value.resolve(info.getBindings());
                       f1.setValue(attribute,value);
                    }
                    Core.DEBUG(2," ==> " + f1);
                    addDB(f1);
                 }
              }
              Assert.notFalse(found);
    }
    
    
  private void retract(Fact f1) {
      if ( context == null )
         engine.update(Node.REMOVE,f1);
      else
         context.ResourceDb().del(f1);
   }
    
    
   public void addDB(Fact f1) {
      if ( context == null ) // i.e. a local engine
         engine.update(Node.ADD,f1);
      else
         context.ResourceDb().add(f1);
   }
}