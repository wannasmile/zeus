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
This code was automatically generated by ZeusAgentGenerator version 1.0_b1
                           DO NOT MODIFY!!
*/

import java.util.*;
import java.io.*;
import zeus.util.*;
import zeus.concepts.*;
import zeus.actors.*;
import zeus.agents.*;

public class VCW {
   protected static void version() {
      System.err.println("VCW version: 1.0_b1");
      System.exit(0);
   }

   protected static void usage() {
      System.err.println("Usage: java VCW -s <dns_file> -o <ontology_file> [-gui ViewerProg] [-e <ExternalProg>] [-r ResourceProg] [-debug] [-h] [-v]");
      System.exit(0);
   }

   public static void main(String[] arg) {
      ZeusAgent agent;
      String external = null;
      String dns_file = null;
      String resource = null;
      String gui = null;
      String ontology_file = null;
      Vector nameservers = null;
      Bindings b = new Bindings();
      FileInputStream stream = null;


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
            Core.setDebuggerOutputFile("VCW.log");
         }
         else if ( arg[j].equals("-v") )
            version();
         else if ( arg[j].equals("-h") )
            usage();
         else
            usage();
      }

      if ( ontology_file == null ) {
         System.err.println("Ontology Database file must be specified with -o option");
         usage();
      }
      if ( dns_file == null ) {
         System.err.println("Domain nameserver file must be specified with -s option");
         usage();
      }

      try {
         nameservers = ZeusParser.addressList(new FileInputStream(dns_file));
         if ( nameservers == null || nameservers.isEmpty() ) 
            throw new IOException();

         agent = new ZeusAgent("VCW",ontology_file,nameservers,1,20);

         AgentContext context = agent.getAgentContext();
         OntologyDb db = context.OntologyDb();

/*
         Initialising ResourceDb
*/
         Fact f1;

/*
         Initialising OrganisationalDb
*/
         AbilityDbItem item;
         agent.addRelation("FAA","peer");

/*
         Initialising ProtocolDb
*/
         ProtocolInfo info;

/*
         Initialising TaskDb
*/
         AbstractTask t;
         t = ZeusParser.primitiveTask(db,"(:Primitive LookupVCPath :time (1) :cost (0) :produced_facts ((:type VCPath :id path :modifiers 1 :attributes ((element_4 ?var376)(element_3 ?var377)(element_2 ?var378)(element_1 ?var379)(id ?var380)))))");
         if ( t.resolve(b) )
            agent.addTask(t);
         stream = new FileInputStream("VCW_monitor.clp");
         t = ZeusParser.reteKB(db,stream);
         stream.close();
         if ( t.resolve(b) )
            agent.addTask(t);

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
         if ( external != null ) {
            c = Class.forName(external);
            ZeusExternal user_prog = (ZeusExternal) c.newInstance();
            context.set(user_prog);
            user_prog.exec(context);
         }

/*
         Activating Rete Engine
*/
         context.ReteEngine().run();
      }
      catch(Exception e) {
         e.printStackTrace();
         System.exit(0);
      }
   }
}
