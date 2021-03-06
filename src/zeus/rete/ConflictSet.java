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



package zeus.rete;

import java.util.*;
import java.io.*;
import zeus.util.*;
import zeus.concepts.*;
import zeus.concepts.fn.*;
import zeus.actors.AgentContext;
import zeus.actors.ResourceDb;
import zeus.rete.action.*;


public class ConflictSet extends Thread {
   protected ReteEngine engine = null;
   protected AgentContext context = null;
   protected OntologyDb db = null;
   protected boolean running = true;
   protected Hashtable writers = new Hashtable();
   protected Hashtable readers = new Hashtable();
   public int cleanUpInterval = 1000;

   Queue queue = new Queue(Rule.MAX_SALIENCE+1);

   ConflictSet(ReteEngine engine, OntologyDb db) {
      this.engine = engine;
      this.db = db;
      //this.setPriority(NORM_PRIORITY+2);
      createWriter("t",System.out);
      createReader("t",System.in);
   }


   ConflictSet(AgentContext context) {
      this.context = context;
      this.engine = context.ReteEngine();
      //this.setPriority(NORM_PRIORITY+2);
      createWriter("t",System.out);
      createReader("t",System.in);
   }
   
   
   /**
    return the hashtable where the output writers are stored for the print
    and println actions 
    */
   public Hashtable getWriters() {
    return this.writers; 
   }
   
   
   /**
     return the tables where the readers are stored for the read and readln 
     actions 
     */
   public Hashtable getReaders() { 
    return this.readers; 
   }
   
   
   /**
    set the hashtable of readers for the rete actions - it is 
    unlikely that agent developers will need this method
    */
   public void setReaders(Hashtable readers) { 
    this.readers = readers; 
   }
   
   
   /**
    set the hashtable of writers for the rete actions - unlikely 
    to be of much use to agent developers 
    */
   public void setWriters (Hashtable writers ) { 
    this.writers = writers; 
   }
   

/** 
    createWriter method is used by the ReteEngine to open a write stream
    */
   public void createWriter(String logicalName, OutputStream os) {
      if ( writers.containsKey(logicalName) ) {
         Core.USER_ERROR("Output writer with logical name \'" + logicalName +
            " is already defined");
         return;
      }

      PrintWriter out = new PrintWriter(os,true);
      writers.put(logicalName,out);
   }



/** 
    createWriter method is used by the ReteEngine to open a write stream
    */
   public void createWriter(String logicalName, String filename, boolean append) {
      if ( writers.containsKey(logicalName) ) {
         Core.USER_ERROR("Output writer with logical name \'" + logicalName +
            " is already defined");
         return;
      }

      try {
         PrintWriter out = new PrintWriter(new FileWriter(filename,append),true);
         writers.put(logicalName,out);
      }
      catch(IOException e) {
         Core.USER_ERROR("Cannot create file " + filename);
      }
   }

   
   /** 
    createReader method is used by the ReteEngine to open a read stream
    */
   public  void createReader(String logicalName, String filename) {
      if ( readers.containsKey(logicalName) ) {
         Core.USER_ERROR("Input reader with logical name \'" + logicalName +
            " is already defined");
         return;
      }

      try {
         DataReader r = new DataReader(new FileReader(filename));
         readers.put(logicalName,r);
      }
      catch(FileNotFoundException e) {
         Core.USER_ERROR("File not found: " + filename + ". Cannot create reader");
      }
   }


/** 
    createReader method is used by the ReteEngine to open a read stream
    */
   public void createReader(String logicalName, InputStream in) {
      if ( readers.containsKey(logicalName) ) {
         Core.USER_ERROR("Input reader with logical name \'" + logicalName +
            " is already defined");
         return;
      }
      DataReader r = new DataReader(new InputStreamReader(in));
      readers.put(logicalName,r);
   }

    /** 
        superceeded by getOntologyDb()
        */
   OntologyDb ontology() {
      return (context == null) ? db : context.OntologyDb();
   }


   void update(String path, int tag, Vector input,
               Bindings bindings, ActionNode node) {
      Info info;
      String diagnostic;

//      Thread me = Thread.currentThread();
      switch(tag) {
         case Node.ADD:
         //     System.out.println("in ConflictSet.update()... attempting to Q event for processing"); 
              info = new Info();
              info.path = path;
              info.input = input;
              info.bindings = bindings;
              info.node = node;
              diagnostic = "==> Activation: " + node.rule_name + ": " + getFactList(input);
              engine.fireEvent(ReteEngine.RULE_ACTIVATED,node,diagnostic);
              Core.DEBUG(3,diagnostic);
//            Core.DEBUG(3,diagnostic + " [" + me.getName() + "]");
              queue.enqueue(info,Rule.MAX_SALIENCE-node.salience);
              break;

         case Node.REMOVE:
              Enumeration enum = queue.elements();
              while( enum.hasMoreElements() ) {
                 info = (Info)enum.nextElement();
                 if ( info.path.equals(path) && info.node == node &&
                      sameInput(info.input,input) ) {
                    diagnostic = "<== Activation: " + node.rule_name + ": " + getFactList(input);
                    engine.fireEvent(ReteEngine.RULE_DEACTIVATED,node,diagnostic);
                    Core.DEBUG(3,diagnostic);
//                  Core.DEBUG(3,diagnostic + " [" + me.getName() + "]");
                    queue.remove(info);
                 }
              }
              break;
      }
   }


   public void reset() {
      queue.clear();
   }


   public void addDB(Fact f1) {
      if ( context == null ) // i.e. a local engine
         engine.update(Node.ADD,f1);
      else
         context.ResourceDb().add(f1);
   }


   public void retract(Fact f1) {
      if ( context == null )
         engine.update(Node.REMOVE,f1);
      else
         context.ResourceDb().del(f1);
   }


   public void run() {
      Action a;
      Info info;
      String diagnostic;

      int count = 0; 

      while(running) {
        
         Core.DEBUG(5,this);
        // System.out.println("in ConflictSet.run()... waiting for next event to be enqueued"); 
	     info = (Info)queue.dequeue();
	    // System.out.println("in ConflictSet.run()... processing rule");

//       Thread me = Thread.currentThread();
         diagnostic = "FIRE [" + info.node.rule_name + "; " +
         info.input.size() + " facts] " + getFactList(info.input);
         engine.fireEvent(ReteEngine.RULE_FIRED,info.node,diagnostic);

         Core.DEBUG(2,diagnostic);
//       Core.DEBUG(2,diagnostic + " [" + me.getName() + "]");

	     for(int i = 0; i < info.node.actions.size(); i++ ) {
            a = (Action)info.node.actions.elementAt(i);
            executeAction(a,info);
         }
         yield();
         if (count>cleanUpInterval) {
           System.gc();
           System.runFinalization();
           count = 0;
           }
           count++;
      }
   }


    /** 
       * executeAction is what causes a rule action to be executed when a rule has been 
       * matched to. 
       * This uses an AbstractFactory to get the action and then asks it to execute itself
       *@author Simon Thompson
       *.@since 1.2 (complete rewrite)
         */
   protected void executeAction(Action a, Info info) {
      Fact f1;
      boolean found;
      ValueFunction var, type_var, value, value1;
      ReteFact token;
      String attribute, output, str;
      Enumeration enum;
      Action b;
      PrintWriter out;
      DataReader in;

      AbstractActionFactory actionFact = new AbstractActionFactory(); 
      ActionFactory factory  = actionFact.getActionFactory (); 
      try {
      //  System.out.println("generating action");
        BasicAction action = factory.getAction(a.type);
        action.setActuators(this,context);
        //System.out.println("Action was : " + action.toString()); 
        action.executeAction (a,info); }
        catch (Exception e) { 
            e.printStackTrace(); }
        

   }


   private String getFactList(Vector input) {
      String s = "";
      Fact f;
      for(int i = 0; i < input.size(); i++ ) {
         f = (Fact)input.elementAt(i);
         s += f.getId();
         if ( i+1 < input.size() ) s += ", ";
      }
      return s;
   }
    /**
      * store and input contain an ordered list of (ground) facts
      * The method getIdString() of the Fact class can be used to
      *  compare the lists
      */
   private boolean sameInput(Vector store, Vector input) {
      String s1 = getFactList(store);
      String s2 = getFactList(input);
      return s1.equals(s2);
   }


   public String toString() {
      String s = "BEGIN CONFLICT SET\n";
      Info info;
      Enumeration enum = queue.elements();
      while( enum.hasMoreElements() ) {
         info = (Info)enum.nextElement();
         s += info.node.rule_name + ":  " + getFactList(info.input) + "\n";
      }
      s += "END CONFLICT SET";
      return s;
   }

 
}
