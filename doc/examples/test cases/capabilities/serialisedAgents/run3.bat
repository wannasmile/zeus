REM This script runs the non-root servers and other utility agents
start /min java zeus.agents.Facilitator Facilitator1 -o .\sendRec.ont -s dns.db -t 5.0
start /min java zeus.visualiser.Visualiser Visualiser1 -s dns.db -o .\sendRec.ont -quick
