/*
	This software was produced as a part of research
	activities. It is not intended to be used as commercial
	or industrial software by any organisation. Except as
	explicitly stated, no guarantees are given as to its
	reliability or trustworthiness if used for purposes other
	than those for which it was originally intended.
 
	(c) British Telecommunications plc 1999.
*/

(:Rulebase SendMessage
   (Rule0
      ?var171 <- (Computer (kb_type ?var172) (cpu_speed ?var174) (unit_cost ?var173) (printer_type ?var175) (number ?var176) (monitor_type ?var177))
      =>
      (send_message (type inform) (content ?var171) (receiver PrinterMaker0))
   )
)
