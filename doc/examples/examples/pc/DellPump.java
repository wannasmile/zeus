import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import zeus.util.*;
import zeus.concepts.*;
import zeus.agents.*;
import zeus.actors.*;

public class DellPump extends JFrame implements ZeusExternal {
   protected AgentContext context = null;

   public DellPump() {
      JButton startBtn = new JButton("Next");
      JPanel contentPane = (JPanel) getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.add(startBtn,BorderLayout.CENTER);
      startBtn.addActionListener(new SymAction());
      setVisible(false);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            System.exit(0);
         }
      });
   }

   public void exec(AgentContext context) {
      this.context = context;
      setVisible(true);
      pack();
   }

   protected class SymAction implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         if ( context == null ) return;

         Fact fact = context.OntologyDb().getFact(
            Fact.VARIABLE,"Computer");
         fact.setValue("cpu_speed","400");
         fact.setValue("kb_type","UK");
         fact.setValue("printer_type","laser");
         fact.setValue("monitor_type","svga");
         fact.setNumber(1);
         int now = (int)context.now();
         Goal g = new Goal(context.newId("goal"), fact, now+8, 0,
            context.whoami(), now+5);
         context.Engine().achieve(g);
      }
   }
}
