/*
	This software was produced as a part of research
	activities. It is not intended to be used as commercial
	or industrial software by any organisation. Except as
	explicitly stated, no guarantees are given as to its
	reliability or trustworthiness if used for purposes other
	than those for which it was originally intended.
 
	(c) British Telecommunications plc 1999.
*/

/*
This code was automatically generated by ZeusAgentGenerator version 1.1
                           DO NOT MODIFY!!
*/

import java.util.*;
import java.io.*;
import zeus.util.*;
import zeus.concepts.*;
import zeus.actors.*;
import zeus.agents.*;

public class Agent1 {
   protected static void version() {
      System.err.println("ZeusAgent - Agent1 version: 1.1");
      System.exit(0);
   }

   protected static void usage() {
      System.err.println("Usage: java Agent1 -s <dns_file> -o <ontology_file> [-gui ViewerProg] [-e <ExternalProg>] [-r ResourceProg] [-name <AgentName>] [-debug] [-h] [-v]");
      System.exit(0);
   }

   public static void main(String[] arg) {
   try { 
      ZeusAgent agent;
      String external = null;
      String dns_file = null;
      String resource = null;
      String gui = null;
      String ontology_file = null;
      Vector nameservers = null;
      String name = new String ("Agent1");
      Bindings b = new Bindings("Agent1");
      FileInputStream stream = null;
      ZeusExternal user_prog = null;


      for( int j = 0; j < arg.length; j++ ) {
         if ( arg[j].equals("-s") &&  ++j < arg.length )
            dns_file = arg[j];
         else if ( arg[j].equals("-e") &&  ++j < arg.length )
            external = arg[j];
         else if ( arg[j].equals("-r") &&  ++j < arg.length )
            resource = arg[j];
         else if ( arg[j].equals("-o") &&  ++j < arg.length )
            ontology_file = arg[j];
         else if ( arg[j].equals("-gui") &&  ++j < arg.length )
            gui = arg[j];
         else if ( arg[j].equals("-debug") ) {
            Core.debug = true;
            Core.setDebuggerOutputFile("Agent1.log");
         }
         else if ( arg[j].equals("-v") )
            version();
         else if ( arg[j].equals("-name")  && ++j < arg.length )
             name = name + "_" + arg[j];
         else if ( arg[j].equals("-h") )
            usage();
         else
            usage();
      }

       b = new Bindings(name);
      if ( ontology_file == null ) {
         System.err.println("Ontology Database file must be specified with -o option");
         usage();
      }
      if ( dns_file == null ) {
         System.err.println("Domain nameserver file must be specified with -s option");
         usage();
      }

         nameservers = ZeusParser.addressList(new FileInputStream(dns_file));
         if ( nameservers == null || nameservers.isEmpty() ) 
            throw new IOException();

         agent = new ZeusAgent(name,ontology_file,nameservers,1,20,true,false);

         AgentContext context = agent.getAgentContext();
         OntologyDb db = context.OntologyDb();


/*
         Initialising Extensions
*/
         Class c;

         if ( resource != null ) {
            c = Class.forName(resource);
            ExternalDb oracle = (ExternalDb) c.newInstance();
            context.set(oracle);
            oracle.set(context);
         }
         if ( gui != null ) {
            c = Class.forName(gui);
            ZeusAgentUI ui = (ZeusAgentUI)c.newInstance();
            context.set(ui);
            ui.set(context);
         }

/*
         Initialising ProtocolDb
*/
         ProtocolInfo info;
         info = ZeusParser.protocolInfo(db,"(:name \"zeus.actors.graphs.ContractNetRespondent\" :type Respondent :constraints ((:fact (:type ZeusFact :id var49 :modifiers 1) :type 0 :strategy \"zeus.actors.graphs.DefaultRespondentEvaluator\")))");
         if ( info.resolve(b) )
            agent.addProtocol(info);

/*
         Initialising TaskDb
*/
         AbstractTask t;
         t = ZeusParser.primitiveTask(db,"(:Primitive Task1 :time (1) :cost (0) :consumed_facts ((:type FactForTesting :id var18 :modifiers 1 :attributes ((att2 ?varx)(att1 ?vary)))) :produced_facts ((:type FactForTesting :id var21 :modifiers 1 :attributes ((att2 ?varx)(att1 ?vary)))))");
         if ( t.resolve(b) )
            agent.addTask(t);
/*
         Initialising OrganisationalDb
*/
         AbilityDbItem item;
         agent.addRelation("Agent0","superior");

/*
         Initialising ResourceDb
*/
         Fact f1;
         f1 = ZeusParser.fact(db,"(:type FactForTesting :id fact43 :modifiers 0 :attributes ((att2 9)(att1 11)))");
         if ( f1.resolve(b) )
            agent.addFact(f1);


/*
         Initialising External User Program
*/

         if ( external != null ) {
            c = Class.forName(external);
            user_prog = (ZeusExternal) c.newInstance();
            context.set(user_prog);
         }


/*
         Activating External User Program
*/

         if ( user_prog != null )
            user_prog.exec(context);

      }
 catch (ClassNotFoundException cnfe) { 
     System.out.println("Java cannot find some of the classes that are needed to run this agent. Please ensure that you have the followingin your classpath : zeus_install_dir\\lib\\zeus.jar, zeus_install_dir\\lib\\gnu-regexp.jar, java_install_dir\\jre\\rt.jar  Where zeus_install_dir is the directory that you have installed  Zeus in , and java_install_dir is the directory that you have  installed Java in");
   cnfe.printStackTrace();}
      catch(Exception e) {
         e.printStackTrace();
      }
   }
}
