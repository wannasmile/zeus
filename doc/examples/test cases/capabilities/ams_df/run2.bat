REM This script runs the task agents
start /min java dummy -o .\test.ont -s dns.db -gui zeus.agentviewer.AgentViewer -e dummyExternal
start /min java scheduler -o .\test.ont -s dns.db -gui zeus.agentviewer.AgentViewer -e schedulerExternal

