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
This code was automatically generated by ZeusAgentGenerator version 1.02
                           DO NOT MODIFY!!
*/

import java.util.*;
import java.io.*;
import zeus.util.*;
import zeus.concepts.*;
import zeus.actors.*;
import zeus.agents.*;

public class SupplyBot {
   protected static void version() {
      System.err.println("ZeusAgent - SupplyBot version: 1.02");
      System.exit(0);
   }

   protected static void usage() {
      System.err.println("Usage: java SupplyBot -s <dns_file> -o <ontology_file> [-gui ViewerProg] [-e <ExternalProg>] [-r ResourceProg] [-debug] [-h] [-v]");
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
      Bindings b = new Bindings("SupplyBot");
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
            Core.setDebuggerOutputFile("SupplyBot.log");
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

         agent = new ZeusAgent("SupplyBot",ontology_file,nameservers,1,20,false,false);

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
         info = ZeusParser.protocolInfo(db,"(:name \"zeus.actors.graphs.ContractNetRespondent\" :type Respondent :constraints ((:fact (:type ZeusFact :id var178 :modifiers 1) :type 0 :strategy \"zeus.actors.graphs.LinearRespondentEvaluator\" :parameters (\"noquibble.factor\" \"0.3\" \"max.percent\" \"125\" \"min.percent\" \"105\"))))");
         if ( info.resolve(b) )
            agent.addProtocol(info);
         info = ZeusParser.protocolInfo(db,"(:name \"zeus.actors.graphs.ContractNetInitiator\" :type Initiator :constraints ((:fact (:type ZeusFact :id var176 :modifiers 1) :type 0 :strategy \"zeus.actors.graphs.LinearInitiatorEvaluator\" :parameters (\"noquibble.factor\" \"0.2\" \"max.percent\" \"120\" \"min.percent\" \"70\"))))");
         if ( info.resolve(b) )
            agent.addProtocol(info);

/*
         Initialising OrganisationalDb
*/
         AbilityDbItem item;
/*
         Initialising ResourceDb
*/
         Fact f1;
         f1 = ZeusParser.fact(db,"(:type apple :id appleStock :modifiers 0 :attributes ((unit_cost 5)(number 30)))");
         if ( f1.resolve(b) )
            agent.addFact(f1);
         f1 = ZeusParser.fact(db,"(:type orange :id orangeStock :modifiers 0 :attributes ((unit_cost 5)(number 30)))");
         if ( f1.resolve(b) )
            agent.addFact(f1);
         f1 = ZeusParser.fact(db,"(:type pear :id pearStock :modifiers 0 :attributes ((unit_cost 5)(number 10)))");
         if ( f1.resolve(b) )
            agent.addFact(f1);
         f1 = ZeusParser.fact(db,"(:type Money :id cash :modifiers 0 :attributes ((amount 500)))");
         if ( f1.resolve(b) )
            agent.addFact(f1);
         f1 = ZeusParser.fact(db,"(:type melon :id melonStock :modifiers 0 :attributes ((unit_cost 10)(number 20)))");
         if ( f1.resolve(b) )
            agent.addFact(f1);
         f1 = ZeusParser.fact(db,"(:type banana :id bananaStock :modifiers 0 :attributes ((unit_cost 8)(number 20)))");
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
      catch(Exception e) {
         e.printStackTrace();
         System.exit(0);
      }
   }
}
