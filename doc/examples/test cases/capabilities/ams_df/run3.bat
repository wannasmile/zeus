REM This script runs the non-root servers and other utility agents
start /min java zeus.agents.Facilitator Facilitator2 -o .\test.ont -s dns.db -t 5.0
start /min java zeus.visualiser.Visualiser Visualiser2 -s dns.db -o .\test.ont -quick
