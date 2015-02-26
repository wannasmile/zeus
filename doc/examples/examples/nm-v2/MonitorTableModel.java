import javax.swing.table.*;
import java.util.*;
import zeus.concepts.*;
import zeus.actors.event.*;
import zeus.concepts.*;
import zeus.actors.*;


public class MonitorTableModel  extends AbstractTableModel
             implements FactMonitor {

      static final int NODE  = 0;
      static final int NNI_PORT     = 1;
      static final int VCID = 2;
      static final int PROBLEM_STATUS = 3;
      static final int TRUNK_UTILISATION = 4;

      private String[] header = { "node", "nni_port", "vcid","problem_status","trunk_utilisation" };
      private Vector   data ;
      public ResourceDb resDB;


      public MonitorTableModel(AgentContext context){
        data = new Vector();
        resDB = context.ResourceDb();
        resDB.addFactMonitor(this, FactEvent.ADD_MASK ,true);
      }


       public int getRowCount() { return data.size(); }

       public int getColumnCount() { return header.length; }

       public String getColumnName(int col) { return header[col]; }

       public Object getValueAt(int row, int col) {
           Fact f = (Fact)data.elementAt(row);
	   switch(col) {
              case NODE:
                   return  f.getValue(header[NODE]);
              case NNI_PORT:
                   return f.getValue(header[NNI_PORT]);
              case VCID:
                   return f.getValue(header[VCID]);
              case PROBLEM_STATUS:
                   return f.getValue(header[PROBLEM_STATUS]);
              case TRUNK_UTILISATION:
                   return f.getValue(header[TRUNK_UTILISATION]);

           }
           return null;
       }

       public boolean isCellEditable(int row, int col) {
          return false;
       }

       public void factAddedEvent(FactEvent e) {
         Fact f = e.getFact();
         if ( f.getType().equals("NetworkElementStatus") ) {
          String node = f.getValue("node");
          String vcid = f.getValue("vcid");

          Fact f2;

          for(int i = 0; i < data.size(); i++ ) {
             f2 = (Fact)data.elementAt(i);
             if ( node.equals(f2.getValue("node")) &&
                  vcid.equals(f2.getValue("vcid")) )
                data.removeElementAt(i--);
          }

	  data.addElement(f);
         }
	 fireTableStructureChanged();
       }

       public void factModifiedEvent(FactEvent event) {
       }

      public void factDeletedEvent(FactEvent event) {
      }

      public void factAccessedEvent(FactEvent event) {}


}
