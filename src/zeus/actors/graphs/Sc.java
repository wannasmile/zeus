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



package zeus.actors.graphs;

import java.util.*;
import zeus.util.*;
import zeus.concepts.*;
import zeus.actors.*;
import zeus.actors.rtn.*;
import zeus.actors.rtn.util.*;

public class Sc extends Node {
   public Sc() {
      super("Sc");
   }
   protected int exec() {
      Engine engine = context.Engine();
      Planner table = context.Planner();

      GraphStruct gs = (GraphStruct)input;
      Goal g;
      DelegationStruct ds;
      SuppliedDb db;
      AuditTable audit = engine.getAuditTable();

      for(int i = 0; i < gs.selection.size(); i++ ) {
         ds = (DelegationStruct)gs.selection.elementAt(i);
         g = (Goal)ds.goals.elementAt(0);
         audit.add(g,ds.key,g.getCost(),false,false,ds.agent,
                   context.whoami(),g.getEndTime());
         engine.continue_dialogue(ds.key,ds.agent,"accept-proposal",ds.goals);
/*
         if ( (db = g.getSuppliedDb()) != null ) {
            reservations = db.getReservations(context.whoami(),ds.agent);
            audit.add(context.whoami(),reservations);
         }
*/
      }
      PlannerQueryStruct bind_data = table.clear_bind(gs.goal);
      table.reconfirmParentOf(gs.goal);

      output = gs;
      return OK;
   }
   protected void reset() {
      // reset any state changed by exec()
   }
}
