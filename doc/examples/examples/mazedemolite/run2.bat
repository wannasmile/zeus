REM This script runs the task agents
start /min java Environment -o .\mazedemo.ont -s dns.db -gui zeus.agentviewer.AgentViewer -e mazeControl
start /min java Navigator -o .\mazedemo.ont -s dns.db -gui zeus.agentviewer.AgentViewer -e navGUI
