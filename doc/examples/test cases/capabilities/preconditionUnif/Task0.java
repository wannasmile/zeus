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
This stub file was automatically generated by ZeusAgentGenerator version 1.1
*/

import java.util.*;
import zeus.util.*;
import zeus.concepts.*;
import zeus.actors.TaskContext;
import zeus.actors.ZeusTask;

public class Task0 extends ZeusTask {
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

      Fact[] _var33 = inputArgs[0];	// FactForTesting

      // The Output Facts:

      Fact[] _var38;	// FactForTesting

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
      _var38 = new Fact[1];
      _var38[0] = new Fact(Fact.FACT,expOutputArgs[0]);
      System.out.println(_var38[0].pprint());

      /* USER CODE ENDS */
      outputArgs = new Fact[1][];
      outputArgs[0] = _var38;
   }
}
