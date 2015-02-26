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

public class Bewag {
   protected static void version() {
      System.err.println("ZeusAgent - Bewag version: 1.1");
      System.exit(0);
   }

   protected static void usage() {
      System.err.println("Usage: java Bewag -s <dns_file> -o <ontology_file> [-gui ViewerProg] [-e <ExternalProg>] [-r ResourceProg] [-name <AgentName>] [-debug] [-h] [-v]");
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
      String name = new String ("Bewag");
      Bindings b = new Bindings("Bewag");
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
            Core.setDebuggerOutputFile("Bewag.log");
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

         agent = new ZeusAgent(name,ontology_file,nameservers,7,20,true,true);

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
         info = ZeusParser.protocolInfo(db,"(:name \"zeus.actors.graphs.ContractNetRespondent\" :type Respondent :constraints ((:fact (:type ZeusFact :id var399 :modifiers 1) :type 0 :strategy \"zeus.actors.graphs.DefaultRespondentEvaluator\") (:fact (:type ZeusFact :id var358 :modifiers 1) :type 0 :strategy \"zeus.actors.graphs.LinearRespondentEvaluator\" :parameters (\"no.quibblerange\" \"2\" \"step.default\" \"2\" \"max.percent\" \"104\" \"min.percent\" \"95\"))))");
         if ( info.resolve(b) )
            agent.addProtocol(info);
         info = ZeusParser.protocolInfo(db,"(:name \"zeus.actors.graphs.ContractNetInitiator\" :type Initiator :constraints ((:fact (:type ZeusFact :id var398 :modifiers 1) :type 0 :strategy \"zeus.actors.graphs.DefaultInitiatorEvaluator\") (:fact (:type ZeusFact :id var357 :modifiers 1) :type 0 :strategy \"zeus.actors.graphs.LinearInitiatorEvaluator\" :parameters (\"step.default\" \"2\" \"no.quibblerange\" \"2\" \"max.percent\" \"130\" \"min.percent\" \"96\"))))");
         if ( info.resolve(b) )
            agent.addProtocol(info);

/*
         Initialising TaskDb
*/
         AbstractTask t;
         t = ZeusParser.primitiveTask(db,"(:Primitive MakeLoadForecast :time (1) :cost (0) :consumed_facts ((:type JOLoadForecast :id joLoadForecast :modifiers 5 :attributes ((status ?var661)(name ?knownLoad.name)(className ?var663)(expectedLoad ?var664)(toTime >=?knownLoad.time)(fromTime <=?knownLoad.time)))(:type KnownLoad :id var484 :modifiers 3 :attributes ((name ?knownLoad.name)(time ?knownLoad.time)(quantity ?var487)))) :produced_facts ((:type KnownLoad :id knownLoad :modifiers 1 :attributes ((name ?var561)(time ?var563)(quantity ?var142)))))");
         if ( t.resolve(b) )
            agent.addTask(t);
         t = ZeusParser.primitiveTask(db,"(:Primitive MakeMargCost :time (1) :cost (0) :consumed_facts ((:type JOMarginalCostMatrix :id joMarginalCost :modifiers 5 :attributes ((status ?var615)(matrix ?var616)(name ?myName.name)(NumberOfRows ?var618)(className ?var619)(toTime >=?newMarginalCost.time)(isQuadratic ?var621)(fromTime <=?newMarginalCost.time)))(:type myName :id myName :modifiers 21 :attributes ((name ?var445)))) :produced_facts ((:type MarginalCost :id newMarginalCost :modifiers 1 :attributes ((fromQuantity ?var133)(name ?myName.name)(toQuantity ?var135)(time ?var581)(cost ?var582)))))");
         if ( t.resolve(b) )
            agent.addTask(t);
         t = ZeusParser.primitiveTask(db,"(:Primitive MakePriceForecast :time (1) :cost (0) :produced_facts ((:type PriceForecast :id priceForecast :modifiers 1 :attributes ((expectedPrice ?var128)(name ?var557)(time ?var559)))))");
         if ( t.resolve(b) )
            agent.addTask(t);
         t = ZeusParser.primitiveTask(db,"(:Primitive MakeSupply :time (1) :cost (0) :consumed_facts ((:type Capacity :id capacity :modifiers 21 :attributes ((name ?supplyContract.name)(toTime >=?supplyContract.time)(quantity >=((?knownLoad.quantity + ?tradingQuantity.quantity) + ?supplyContract.quantity))(fromTime <=?supplyContract.time)))(:type KnownLoad :id knownLoad :modifiers 5 :attributes ((name ?supplyContract.name)(time ?supplyContract.time)(quantity ?var85)))(:type TradingQuantity :id tradingQuantity :modifiers 17 :attributes ((name ?supplyContract.name)(time ?supplyContract.time)(quantity ?var90)(avgPrice ?var459)))(:type MarginalCost :id marginalCost :modifiers 5 :attributes ((fromQuantity <=(?knownLoad.quantity + ?tradingQuantity.quantity))(name ?supplyContract.name)(toQuantity >=((?knownLoad.quantity + ?tradingQuantity.quantity) + ?supplyContract.quantity))(time ?supplyContract.time)(cost ?var176)))(:type Interest :id interest :modifiers 21 :attributes ((rate ?var623)(toTime >=?supplyContract.time)(fromTime <=?supplyContract.time)))(:type Risk :id risk :modifiers 21 :attributes ((adding ?var627)(maxVaR ?var628)(minPaR ?var631)))(:type PriceForecast :id priceForecast :modifiers 5 :attributes ((expectedPrice ?var112)(name ?supplyContract.name)(time ?supplyContract.time)))) :produced_facts ((:type SupplyContract :id supplyContract :modifiers 1 :attributes ((name ?var389)(time ?var554)(quantity ?var119)))(:type TradingQuantity :id newTradingQuantity :modifiers 33 :attributes ((name ?supplyContract.name)(time ?supplyContract.time)(quantity (?tradingQuantity.quantity + ?supplyContract.quantity))(avgPrice ?var553)))))");
         if ( t.resolve(b) )
            agent.addTask(t);
         t = ZeusParser.primitiveTask(db,"(:Primitive MakeJOPriceForecast :time (1) :cost (0) :produced_facts ((:type JOPriceForecast :id newJOPriceForecast :modifiers 1 :attributes ((status ?var378)(expectedPrice ?var379)(name ?var380)(className ?var381)(toTime ?var382)(fromTime ?var383)))))");
         if ( t.resolve(b) )
            agent.addTask(t);
         stream = new FileInputStream("RequestImprovement.clp");
         t = ZeusParser.reteKB(db,stream);
         stream.close();
         if ( t.resolve(b) )
            agent.addTask(t);
         t = ZeusParser.primitiveTask(db,"(:Primitive MakeGebot :time (1) :cost (0) :consumed_facts ((:type myName :id myName :modifiers 21 :attributes ((name ?g.name)))(:type JOLoadForecast :id joLoadForecast :modifiers 5 :attributes ((status ?var592)(name ?gebot.name)(className ?var594)(expectedLoad ?var595)(toTime >=?g.LastTimeUnit)(fromTime <=?g.FirstTimeUnit)))(:type JOPriceForecast :id joPriceForecast :modifiers 5 :attributes ((status ?var599)(expectedPrice ?var600)(name ?gebot.name)(className ?var602)(toTime >=?g.LastTimeUnit)(fromTime <=?g.FirstTimeUnit)))(:type Risk :id risk :modifiers 21 :attributes ((adding ?var332)(maxVaR ?var333)(minPaR ?var334)))(:type Interest :id interest :modifiers 21 :attributes ((rate ?var336)(toTime ?var337)(fromTime ?var338)))(:type Capacity :id capacity :modifiers 5 :attributes ((name ?myName.name)(toTime >=?g.LastTimeUnit)(quantity ?var342)(fromTime <=?g.FirstTimeUnit)))(:type JOMarginalCostMatrix :id var402 :modifiers 5 :attributes ((status ?var403)(matrix ?var404)(name ?myName.name)(NumberOfRows ?var406)(className ?var407)(toTime >=?g.LastTimeUnit)(isQuadratic ?var409)(fromTime <=?g.FirstTimeUnit)))) :produced_facts ((:type Gebot :id g :modifiers 1 :attributes ((tag ?var1038)(verkauf_leistung ?var583)(name ?myName.name)(kauf_preis ?var585)(FirstTimeUnit ?var1042)(kauf_leistung ?var586)(LastTimeUnit ?var1044)(verkauf_preis ?var587)))))");
         if ( t.resolve(b) )
            agent.addTask(t);
         stream = new FileInputStream("Handel.clp");
         t = ZeusParser.reteKB(db,stream);
         stream.close();
         if ( t.resolve(b) )
            agent.addTask(t);
         stream = new FileInputStream("Registrierung.clp");
         t = ZeusParser.reteKB(db,stream);
         stream.close();
         if ( t.resolve(b) )
            agent.addTask(t);
         stream = new FileInputStream("OTCBuying.clp");
         t = ZeusParser.reteKB(db,stream);
         stream.close();
         if ( t.resolve(b) )
            agent.addTask(t);
         stream = new FileInputStream("OTCSelling.clp");
         t = ZeusParser.reteKB(db,stream);
         stream.close();
         if ( t.resolve(b) )
            agent.addTask(t);
         stream = new FileInputStream("OTCSteuerung.clp");
         t = ZeusParser.reteKB(db,stream);
         stream.close();
         if ( t.resolve(b) )
            agent.addTask(t);
/*
         Initialising OrganisationalDb
*/
         AbilityDbItem item;
         agent.addRelation("RWE","peer");
         item = ZeusParser.abilityDbItem(db,"(:agent RWE :ability (:fact (:type OTCContract :id var359 :modifiers 1 :attributes ((status ?var132)(name ?var360)(demandId ?var142)(price ?var361)(time ?var362)(quantity ?var363)(supplyId ?var146))) :time 0 :cost 0.0))");
         if ( item.resolve(b) )
            agent.addAbility(item);

         agent.addRelation("Eon","peer");
         item = ZeusParser.abilityDbItem(db,"(:agent Eon :ability (:fact (:type OTCContract :id var364 :modifiers 1 :attributes ((status ?var142)(name ?var365)(demandId ?var154)(price ?var366)(time ?var367)(quantity ?var368)(supplyId ?var158))) :time 0 :cost 0.0))");
         if ( item.resolve(b) )
            agent.addAbility(item);

/*
         Initialising ResourceDb
*/
         Fact f1;


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
 catch (ClassNotFoundException cnfe) { 
     System.out.println("Java cannot find some of the classes that are needed to run this agent. Please ensure that you have the followingin your classpath : zeus_install_dir\\lib\\zeus.jar, zeus_install_dir\\lib\\gnu-regexp.jar, java_install_dir\\jre\\rt.jar  Where zeus_install_dir is the directory that you have installed  Zeus in , and java_install_dir is the directory that you have  installed Java in");
   cnfe.printStackTrace();}
      catch(Exception e) {
         e.printStackTrace();
      }
   }
}