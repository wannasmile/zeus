/*
	This software was produced as a part of research
	activities. It is not intended to be used as commercial
	or industrial software by any organisation. Except as
	explicitly stated, no guarantees are given as to its
	reliability or trustworthiness if used for purposes other
	than those for which it was originally intended.
 
	(c) British Telecommunications plc 1999.
*/

(:Rulebase Supplier
   (Rule2
      ?var32 <- (Entity (unit_cost ?var33) (number ?var34))
      =>
      (assert (Sub (superAtt ?var36) (subAtt ?var37)))
      (retract ?var32)
   )
)
