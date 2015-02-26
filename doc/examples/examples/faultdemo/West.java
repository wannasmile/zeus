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

public class West {
   protected static void version() {
      System.err.println("ZeusAgent - West version: 1.02");
      System.exit(0);
   }

   protected static void usage() {
      System.err.println("Usage: java West -s <dns_file> -o <ontology_file> [-gui ViewerProg] [-e <ExternalProg>] [-r ResourceProg] [-debug] [-h] [-v]");
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
      Bindings b = new Bindings("West");
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
            Core.setDebuggerOutputFile("West.log");
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

         agent = new ZeusAgent("West",ontology_file,nameservers,2,30,true,true);

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
         info = ZeusParser.protocolInfo(db,"(:name \"zeus.actors.graphs.ContractNetRespondent\" :type Respondent :constraints ((:fact (:type ZeusFact :id var119 :modifiers 1) :type 0 :strategy \"zeus.actors.graphs.LinearRespondentEvaluator\" :parameters (\"step.default\" \"2\" \"noquibble.range\" \"2\" \"max.percent\" \"120\" \"min.percent\" \"105\"))))");
         if ( info.resolve(b) )
            agent.addProtocol(info);
         info = ZeusParser.protocolInfo(db,"(:name \"zeus.actors.graphs.ContractNetInitiator\" :type Initiator :constraints ((:fact (:type ZeusFact :id var118 :modifiers 1) :type 0 :strategy \"zeus.actors.graphs.LinearInitiatorEvaluator\" :parameters (\"step.default\" \"2\" \"noquibble.range\" \"2\" \"max.percent\" \"100\" \"min.percent\" \"85\"))))");
         if ( info.resolve(b) )
            agent.addProtocol(info);

/*
         Initialising TaskDb
*/
         AbstractTask t;
         stream = new FileInputStream("Reactor.clp");
         t = ZeusParser.reteKB(db,stream);
         stream.close();
         if ( t.resolve(b) )
            agent.addTask(t);
         t = ZeusParser.primitiveTask(db,"(:Primitive Type1Repair :time (1) :cost (17.5) :consumed_facts ((:type Engineer :id engineer :modifiers 25 :attributes ((skill >=?repair.type)(unit_cost ?var60)(number 1)))) :produced_facts ((:type Repair :id repair :modifiers 1 :attributes ((type 1)(fault ?var64)(id ?var65)))))");
         if ( t.resolve(b) )
            agent.addTask(t);
         t = ZeusParser.primitiveTask(db,"(:Primitive Type3Repair :time (1) :cost (37.5) :consumed_facts ((:type Repair :id repair1 :modifiers 1 :attributes ((type 1)(fault ?repair3.fault)(id ?var45)))(:type Repair :id repair2 :modifiers 1 :attributes ((type 2)(fault ?repair3.fault)(id ?var46)))(:type Engineer :id engineer :modifiers 25 :attributes ((skill >=?repair3.type)(unit_cost ?var48)(number 1)))) :produced_facts ((:type Repair :id repair3 :modifiers 1 :attributes ((type 3)(fault ?var40)(id ?var41)))))");
         if ( t.resolve(b) )
            agent.addTask(t);
         t = ZeusParser.primitiveTask(db,"(:Primitive Type4Repair :time (1) :cost (50) :consumed_facts ((:type Engineer :id engineer :modifiers 25 :attributes ((skill ?repair.type)(unit_cost ?var32)(number 1)))(:type Repair :id prep :modifiers 1 :attributes ((type 3)(fault ?repair.fault)(id ?var37)))) :produced_facts ((:type Repair :id repair :modifiers 1 :attributes ((type 4)(fault ?var28)(id ?var29)))))");
         if ( t.resolve(b) )
            agent.addTask(t);
/*
         Initialising OrganisationalDb
*/
         AbilityDbItem item;
/*
         Initialising ResourceDb
*/
         Fact f1;
         f1 = ZeusParser.fact(db,"(:type Engineer :id engineer1 :modifiers 0 :attributes ((skill 1)(unit_cost 10)(number 2)))");
         if ( f1.resolve(b) )
            agent.addFact(f1);
         f1 = ZeusParser.fact(db,"(:type Engineer :id engineer2 :modifiers 0 :attributes ((skill 2)(unit_cost 15)(number 3)))");
         if ( f1.resolve(b) )
            agent.addFact(f1);
         f1 = ZeusParser.fact(db,"(:type Engineer :id engineer3 :modifiers 0 :attributes ((skill 3)(unit_cost 20)(number 2)))");
         if ( f1.resolve(b) )
            agent.addFact(f1);
         f1 = ZeusParser.fact(db,"(:type Engineer :id engineer4 :modifiers 0 :attributes ((skill 4)(unit_cost 25)(number 2)))");
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
         Activating Rete Engine
*/
         context.ReteEngine().run();

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
