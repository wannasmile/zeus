/*
* The contents of this file are subject to the BT "ZEUS" Open Source 
* Licence (L77741), Version 1.0 (the "Licence"); you may not use this file 
* except in compliance with the Licence. You may obtain a copy of the Licence
* from $ZEUS_INSTALL/licence.html or alternatively from
* http://www.labs.bt.com/projects/agents/zeus/licence.htm
* 
* Except as stated in Clause 7 of the Licence, software distributed under the
* Licence is distributed WITHOUT WARRANTY OF ANY KIND, either express or 
* implied. See the Licence for the specific language governing rights and 
* limitations under the Licence.
* 
* The Original Code is within the package zeus.*.
* The Initial Developer of the Original Code is British Telecommunications
* public limited company, whose registered office is at 81 Newgate Street, 
* London, EC1A 7AJ, England. Portions created by British Telecommunications 
* public limited company are Copyright 1996-9. All Rights Reserved.
* 
* THIS NOTICE MUST BE INCLUDED ON ANY COPY OF THIS FILE
*/



/*
 * @(#)ZeusAgentContext.java 1.00
 */

package zeus.actors;

import java.util.*;
import zeus.util.*;
import zeus.concepts.*;
import zeus.rete.ReteEngine;
import zeus.actors.rtn.*;
import zeus.agents.ZeusExternal;
import zeus.agents.BasicAgent;
import zeus.agents.BasicAgentUI;
import zeus.actors.outtrays.*;
import zeus.actors.factories.*;
import java.net.*;


/**
  Every Zeus Agent possesses one implementation of the AgentContext interface
  whose role is to hold references to the objects that implement the agent's internal components.
  <p>
  
  ZeusAgentContext is the default implementation that is used for normal agents 
  generated by the AgentGenerator. All of the components of an agent that we have 
  implemented so far are expected to be present in an agent of this sort.
 *Changes
 *Simon adds whereAmI() to allow service descriptions to be done 22/9/02
 
 */

public class ZeusAgentContext implements AgentContext
{
  public  boolean share_plan, execute_earliest;
  public  double  registration_timeout, facilitator_timeout, address_timeout,
                  accept_timeout, addressbook_refresh, facilitator_refresh,
                  replan_period;


  /** Stores the agent's own list of known Name Servers */
  protected Vector           nameservers = new Vector();

  /** Stores the agent's own list of known Facilitators */
  protected Vector           facilitators = new Vector();
  

  /** Stores agent identities and the location the agent believes they currenty
      reside at  */
  protected AddressBook      addressBook = null;

  /** The component that implements the agent's communication mechanism*/
  protected MailBox          mbox = null;

  /** The component that determines what actions are performed on the receipt
      of messages */
  protected MsgHandler       msgHandler = null;

  /** A rule engine based on the classic <a href="http://www.haley.com/framed/ReteAlgorithm.html"> Rete Algorithm</a>*/
  protected ReteEngine       reteEngine = null;

  /** The co-ordination engine component, this drives the agent's social and
      problem solving behaviour */
  protected Engine           engine = null;

  /** This component monitors the state of the tasks executed by the agent */
  protected ExecutionMonitor monitor = null;

  /** This component maintains and enacts the agent's diary of schedules goals */
  protected Planner          planner = null;

  /** This stores the organisational relationships believed by the agent */
  protected OrganisationDb   organisationDb = null;

  /** This stores the definitions of the tasks the agent knows it is capable of
      performing */
  protected TaskDb           taskDb = null;

  /** This stores the resource objects currently owned by the agent */
  protected ResourceDb       resourceDb = null;

  /** This holds the agents local copy of the concepts that form the 
      application ontology */
  protected OntologyDb       ontologyDb = null;

  /** A reference to an implementation of wrapper class that connects the
      agent with an external data source (like a database) */
  protected ExternalDb       externalDb = null;

  /** A reference to the class that implements the agent's external interface,
      this is typically used to connect GUI front-ends to the agent. */
  protected ZeusExternal     zeusExternal = null;

  /** A reference to a class implementing some shared agent methods */
  protected BasicAgent       agent = null;

  /** A reference to the AgentViewer class that provides a visual depiction 
      of the state of the agent's components */
  protected BasicAgentUI     agentUI = null;

  /** This stores the protocols and strategies known the agent */
  protected ProtocolDb       protocolDb = null;

  /** The agent's local unique identifier generator */
  protected GenSym           genSym = null;

  /** The agent's local time-keeping model */
  protected Clock            clock = null;

  /** The agent's identity - this should be unique */
  protected String           name = null;

  /** The agent's functional type: one of "Nameserver", "Facilitator", "Visualiser",
      "DbProxy" or "Agent" */
  protected String           type = null;

  /** Internal variable used by co-ordination engine components */
  protected Hashtable        queryTable = new Hashtable();
  
 /** 
    class constructor <p> 
    @param name is a string containing the name of the agent (ie. "myAgent") which must be 
    unique in this naming space
    @param type is the type of the agent - usually something like "ANServer","Facilitator" or 
    "FIPA_IIOP"<p>
    If either of these two parameters are null an IllegalArguementException will be thrown.
    <p> 
    TO DO: the IllegalArguementException should be part of the method declaration.
    */
  public ZeusAgentContext(String name, String type) {
     Assert.notNull(name);
     Assert.notNull(type);

     this.name = name;
     this.type = type;

     share_plan           = SystemProps.getState("share.plan",true);
     execute_earliest     = SystemProps.getState("execute.earliest",true);
     registration_timeout = SystemProps.getDouble("registration.timeout",3.0);
     facilitator_timeout  = SystemProps.getDouble("facilitator.timeout",0.5);
     address_timeout      = SystemProps.getDouble("address.timeout",0.5);
     accept_timeout       = SystemProps.getDouble("accept.timeout",1.5);
     addressbook_refresh  = SystemProps.getDouble("addressbook.refresh",5.0);
     facilitator_refresh  = SystemProps.getDouble("facilitator.refresh",5.0);
     replan_period        = SystemProps.getDouble("replan.period",2.0);
     genSym = new GenSym(name,true);
  }
  

  public boolean getSharePlan() {
        return share_plan;}
        
        
  public boolean getExecuteEarliest() {
        return execute_earliest; }
  
  
  public double getAddressBookRefresh() { 
        return addressbook_refresh; }
        
        
  public double getAddressTimeout () {
        return address_timeout;}   
        
        
  public double getReplanPeriod () {
        return replan_period; } 
    
    
  public double getRegistrationTimeout() {
        return registration_timeout; }
        
        
  public double getFacilitatorTimeout() {
        return facilitator_timeout; } 
        
                
  public double getAcceptTimeout() { 
        return accept_timeout; }
        
        
  public double getFacilitatorRefresh() {
        return facilitator_refresh; }


  public void setSharePlan(boolean share_plan) { 
        this.share_plan = share_plan; }
        
        
  public void setExecuteEarliest(boolean execute_earliest) { 
        this.execute_earliest = execute_earliest; }
  
  
  public void setAddressBookRefresh(double val) { 
        this.addressbook_refresh = val; }
        
        
  public void setAddressTimeout (double val) {
        this.address_timeout = val; }
        
        
  public void setReplanPeriod (double val) { 
        this.replan_period = val; }
        
        
  public void setRegistrationTimeout(double val) {
        this.registration_timeout = val;}
        
        
  public void setFacilitatorTimeout(double val) {
        this.facilitator_timeout = val; }
    
    
  public void setAcceptTimeout(double val) { 
        this.accept_timeout = val; }
        
        
  public void setFacilitatorRefresh(double val) {
        this.facilitator_refresh = val; } 
  

  public void setFacilitators( Vector input ) {
    Assert.notNull(input);
    facilitators.removeAllElements();
    for(int i = 0; i < input.size(); i++ )
       addFacilitator((String)input.elementAt(i));
  }
  
  
  public void addFacilitator(String agent) {
     Assert.notNull(agent);
     if ( !facilitators.contains(agent) )
        facilitators.addElement(agent);
  }
  
  
  public void removeFacilitator(String agent) {
     Assert.notNull(agent);
     facilitators.removeElement(agent);
  }


  public void setNameservers(Vector input) {
    Assert.notNull(input);
    nameservers.removeAllElements();
    for(int i = 0; i < input.size(); i++ )
       addNameserver((Address)input.elementAt(i));
  }
  
  
  public void addNameserver(Address address) {
     Assert.notNull(address);
     if ( !nameservers.contains(address) )
        nameservers.addElement(address);
  }
  
  
  public void removeNameserver(String address) {
     Assert.notNull(address);
     nameservers.removeElement(address);
  }


  public void set(AddressBook addressBook) {
    Assert.notNull(addressBook);
    this.addressBook = addressBook;
  }


  public void set(ProtocolDb protocolDb) {
     Assert.notNull(protocolDb);
     this.protocolDb = protocolDb;
  }


  public void set(MailBox mbox) {
    Assert.notNull(mbox);
    this.mbox = mbox;
  }


  public void set(MsgHandler msgHandler) {
    Assert.notNull(msgHandler);
    this.msgHandler = msgHandler;
  }


  public void set(Engine engine) {
    Assert.notNull(engine);
    this.engine = engine;
  }


  public void set(ReteEngine reteEngine) {
    Assert.notNull(reteEngine);
    this.reteEngine = reteEngine;
  }


  public void set(ExecutionMonitor monitor) {
    Assert.notNull(monitor);
    this.monitor = monitor;
  }


  public void set(Planner planner) {
    Assert.notNull(planner);
    this.planner = planner;
  }


  public void set(OrganisationDb db) {
    Assert.notNull(db);
    this.organisationDb = db;
  }


  public void set(TaskDb taskDb) {
    Assert.notNull(taskDb);
    this.taskDb = taskDb;
  }


  public void set(ResourceDb resourceDb) {
    Assert.notNull(resourceDb);
    this.resourceDb = resourceDb;
  }


  public void set(OntologyDb ontologyDb) {
    Assert.notNull(ontologyDb);
    this.ontologyDb = ontologyDb;
  }


  public void set(BasicAgent agent) {
    Assert.notNull(agent);
    this.agent = agent;
  }


  public void set(BasicAgentUI agentUI) {
    Assert.notNull(agentUI);
    this.agentUI = agentUI;
  }


  public void set(ZeusExternal zeusExternal) {
    Assert.notNull(zeusExternal);
    this.zeusExternal = zeusExternal;
  }


  public void set(ExternalDb externalDb) {
    Assert.notNull(externalDb);
    this.externalDb = externalDb;
  }


  public void set(Clock clock) {
    Assert.notNull(clock);
    this.clock = clock;
  }
  
  
  

  public double           now()              { return clock != null ? clock.currentTime().getTime() : -1; }
  public Time             currentTime()      { return clock != null ? clock.currentTime() : null; }
  public Time             time(long ctm)     { return clock != null ? clock.time(ctm) : null; }
  public long             getClockStep()     { return clock != null ? clock.getIncrement() : -1; }

  public String           newId()            { return genSym.newId();  }
  public String           newId(String tag)  { return genSym.newId(tag);  }

  public String           whatami()          { return type;  }
  public String           whoami()           { return name;  }

   
  public Hashtable        queryTable()       { return queryTable; }
  public Vector           facilitators()     { return facilitators; }
  public Vector           nameservers()      { return nameservers; }
  public AddressBook      AddressBook()      { return addressBook; }
  public MailBox          MailBox()          { return mbox; }
  public MsgHandler       MsgHandler()       { return msgHandler; }
  public ReteEngine       ReteEngine()       { return reteEngine; }
  public Engine           Engine()           { return engine; }
  public ExecutionMonitor ExecutionMonitor() { return monitor; }
  public Planner          Planner()          { return planner; }
  public OrganisationDb   OrganisationDb()   { return organisationDb; }
  public TaskDb           TaskDb()           { return taskDb; }
  public ResourceDb       ResourceDb()       { return resourceDb; }
  public OntologyDb       OntologyDb()       { return ontologyDb; }
  public ExternalDb       ExternalDb()       { return externalDb; }
  public ProtocolDb       ProtocolDb()       { return protocolDb; }
  public ZeusExternal     ZeusExternal()     { return zeusExternal; }
  public BasicAgentUI     AgentUI()          { return agentUI; }
  public BasicAgent       Agent()            { return agent; }
  public GenSym           GenSym()           { return genSym; }
  public Clock            Clock()            { return clock; }


  public Hashtable        getQueryTable()       { return queryTable; }
  public Vector           getFacilitators()     { return facilitators; }
  public Vector           getNameservers()      { return nameservers; }
  public AddressBook      getAddressBook()      { return addressBook; }
  public MailBox          getMailBox()          { return mbox; }
  public MsgHandler       getMsgHandler()       { return msgHandler; }
  public ReteEngine       getReteEngine()       { return reteEngine; }
  public Engine           getEngine()           { return engine; }
  public ExecutionMonitor getExecutionMonitor() { return monitor; }
  public Planner          getPlanner()          { return planner; }
  public OrganisationDb   getOrganisationDb()   { return organisationDb; }
  public TaskDb           getTaskDb()           { return taskDb; }
  public ResourceDb       getResourceDb()       { return resourceDb; }
  public OntologyDb       getOntologyDb()       { return ontologyDb; }
  public ExternalDb       getExternalDb()       { return externalDb; }
  public ProtocolDb       getProtocolDb()       { return protocolDb; }
  public ZeusExternal     getZeusExternal()     { return zeusExternal; }
  public BasicAgentUI     getAgentUI()          { return agentUI; }
  public BasicAgent       getAgent()            { return agent; }
  public GenSym           getGenSym()           { return genSym; }
  public Clock            getClock()            { return clock; }


  /** 
    use the getTransportFactory to get a transportFactory that your agent 
    can use to get a message transport for a particular address that it 
    wants to send a message to. <p>
    Once you have the TransportFactory you will be able to use it to get the specific
    transport (OutTray) that you need 
    @see zeus.actors.OutTray
    @see zeus.actors.service.TransportFactory
    @see zeus.actors.service.IIOP_Z_HTTP_TransportFactory 
    */
  public TransportFactory getTransportFactory () {
    IIOP_Z_HTTP_TransportFactory fact = new IIOP_Z_HTTP_TransportFactory(); 
    fact.setContext(this); 
    return (TransportFactory) fact;
  }
  
  
  /**
    return the inTray for this agent 
    @since 1.1
    @returns the object responsible for receiving messages
    */
  public InTray getInTray() { 
    return mbox.getInTray(); 
  }

  /**  whereAmI should return the deployed address of the agent -
   * implemented as a TCP/IP address ....
   * would be better implemented from a config file
   *this is used to generate service descriptions 
   */
  public String whereAmI() {
      try {

        InetAddress ip = InetAddress.getLocalHost();
        String host = ip.getHostAddress();
        return (host); }
      catch (Exception e) {
          return (new String ("host.discovery.failed.here")); 
      }
  }  
  

  

}
