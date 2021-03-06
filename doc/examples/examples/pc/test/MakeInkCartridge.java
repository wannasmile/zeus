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
This stub file was automatically generated by ZeusAgentGenerator version 1.2.2
*/

import java.util.*;
import zeus.util.*;
import zeus.concepts.*;
import zeus.actors.TaskContext;
import zeus.actors.ZeusTask;

public class MakeInkCartridge extends ZeusTask {
/** getDescription was automatically generated by the Zeus Agent Generator tool
*it provides a Service description for the task written in DAML-S (0.6) which
*can be used by other agents that speak DAML to understand what the task does
**/
public String getDescription() {

	 String serviceDescription = new String(); 

	serviceDescription += "<?xml version='1.0' encoding='ISO-8859-1'?> \n <!DOCTYPE uridef[ \n  <!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns\">\n <!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema\">\n  <!ENTITY daml \"http://www.daml.org/2001/03/daml+oil\">\n  <!ENTITY process \"http://www.daml.org/services/daml-s/2001/10/Process\">\n  <!ENTITY service \"http://www.daml.org/services/daml-s/2001/10/Service\">\n  <!ENTITY profile \"http://www.daml.org/services/daml-s/2001/10/Profile\"> \n  <!ENTITY country \"http://www.daml.ri.cmu.edu/ont/Country.daml\">\n  <!ENTITY concepts \"http://www.daml.ri.cmu.edu/ont/DAML-S/concepts.daml\">\n  <!ENTITY profile \"http://www.daml.org/services/daml-s/2001/10/Profile\"> \n  <!ENTITY time \"http://www.ai.sri.com/daml/ontologies/sri-basic/1-0/Time.daml\">\n  <!ENTITY xsd \"http://www.w3.org/2000/10/XMLSchema.xsd\">\n  <!ENTITY DEFAULT \"http:"+context.whereAmI() + "\\services\\MakeInkCartridge.daml\">]> \n";
	serviceDescription += "<rdf:RDF \n xmlns:rdf =\"&rdf;#\" \n xmlns:profile=\"&profile;#\"\n xmlns:rdfs =    \"&rdfs;#\" \n xmlns:xsd =    \"&xsd;#\"\n  xmlns:daml =    \"&daml;#\"\n  xmlns:service = \"&service;#\"\n  xmlns:process = \"&process;#\">\n";
	serviceDescription += "<daml:ontology rdf:about=\"http://"+context.whereAmI() + "/ontologies/" + context.getOntologyDb().getOntologyName().replace('.','_') + ".daml" +"\"/>\n";
	serviceDescription += "<daml:versionInfo></daml:versionInfo>\n";
	serviceDescription += "<profile:Profile rdf:ID=\"MakeInkCartridge\">\n";
	serviceDescription += "<profile:textDescription>\"\"</profile:textDescription>\n";
	serviceDescription += "<profile:providedBy><profile:ServiceProvider rdf:ID=\"" +context.whoami() + "\">\n";
	serviceDescription += "<profile:name> MakeInkCartridge</profile:name>\n";
	serviceDescription += "<profile:phone></profile:phone>\n";
	serviceDescription += "<profile:fax></profile:fax>\n";
	serviceDescription += "<profile:email></profile:email>\n";
	serviceDescription += "<profile:physicalAddress></profile:physicalAddress>\n";
	serviceDescription += "<profile:webURL>\" http://"+context.whereAmI() + "/services/MakeInkCartridge.daml\"</profile:webURL>\n";
	serviceDescription += "</profile:ServiceProvider>\n</profile:providedBy>\n";
	serviceDescription += "<profile:geographicRadius></profile:geographicRadius>\n";
	serviceDescription += " <daml:imports rdf:resource=\"http://www.w3.org/1999/02/22-rdf-syntax-ns\" /> \n  <daml:imports rdf:resource=\"http://www.w3.org/2000/01/rdf-schema\" />\n  <daml:imports rdf:resource=\"http://www.daml.org/2001/03/daml+oil\" /> \n  <daml:imports rdf:resource=\"http://www.daml.org/services/daml-s/2001/10/Service\" />\n  <daml:imports rdf:resource=\"http://www.daml.org/services/daml-s/2001/10/Process\" /> \n  <daml:imports rdf:resource=\"http://www.daml.org/services/daml-s/2001/10/Profile\" />\n";
	serviceDescription += "<profile:postcondition>\n";
	serviceDescription += "<profile:ParameterDescription rdf:ID=\"InkCartridge\">\n";
	serviceDescription += "<profile:parameterName>?ink</profile:parameterName>\n";
	serviceDescription += "<profile:type>InkCartridge</profile:type>\n";
	serviceDescription += "<profile:range>\"http://"+context.whereAmI()+"/ontologies/"+context.getOntologyDb().getOntologyName() +"#InkCartridge:unit_cost=?var198\"</profile:range>;
\n";
	serviceDescription += "<profile:range>\"http://"+context.whereAmI()+"/ontologies/"+context.getOntologyDb().getOntologyName() +"#InkCartridge:number=?var199\"</profile:range>;
\n";
	serviceDescription += "</profile:ParameterDescription>\n";
	serviceDescription += "</profile:postcondition>\n";
	serviceDescription += "</profile:Profile>\n</rdf:RDF>\n";

	return serviceDescription;
}



   protected void exec() {
      /*
      Add the task execution code here. The following variables are defined:
         protected Fact[][]  inputArgs; 
         protected Fact[][]  outputArgs; 
      Before exec() is called inputArgs will contain the input
      Facts consumed by the task. After execution, set outputArgs to 
      contain the output Facts produced by the task.
      */

      // The Input Facts:


      // The Output Facts:

      Fact[] _ink;	// InkCartridge

      /* USER CODE STARTS */

      System.out.println("-Expected Input-");
      for(int i = 0; i < expInputArgs.length; i++ )
         System.out.println(expInputArgs[i].pprint());

      System.out.println("-Input-");
      for(int j = 0; j < inputArgs.length; j++) {
         System.out.println("Input Fact["+j+"]");
         for(int i = 0; i < inputArgs[j].length; i++)
            System.out.println(inputArgs[j][i].pprint());
      }

      System.out.println("-Expected Output-");
      for(int i = 0; i < expOutputArgs.length; i++ )
         System.out.println(expOutputArgs[i].pprint());

      System.out.println("-Output-");
      _ink = new Fact[1];
      _ink[0] = new Fact(Fact.FACT,expOutputArgs[0]);
      System.out.println(_ink[0].pprint());

      /* USER CODE ENDS */
      outputArgs = new Fact[1][];
      outputArgs[0] = _ink;
   }
}
