/*
	This software was produced as a part of research
	activities. It is not intended to be used as commercial
	or industrial software by any organisation. Except as
	explicitly stated, no guarantees are given as to its
	reliability or trustworthiness if used for purposes other
	than those for which it was originally intended.
 
	(c) British Telecommunications plc 1999.
*/

(:Rulebase CollectorNotifier
   (Rule1
      ?matched <- (Super (superAtt ?var31))
      =>
      (retract ?matched)
      (assert (Entity (unit_cost ?var5) (number ?var6)))
      (send_message (type inform) (content ?matched) (receiver Agent1))
   )
)
