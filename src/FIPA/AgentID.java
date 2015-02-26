package FIPA;


/**
* FIPA/AgentID.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from FIPA.idl
* 09 March 2001 16:39:32 o'clock GMT
*/

public final class AgentID implements org.omg.CORBA.portable.IDLEntity
{

  // Agent Identifier
  public String name = null;
  public String addresses[] = null;
  public FIPA.AgentID resolvers[] = null;
  public FIPA.Property userDefinedProperties[] = null;

  public AgentID ()
  {
  } // ctor

  public AgentID (String _name, String[] _addresses, FIPA.AgentID[] _resolvers, FIPA.Property[] _userDefinedProperties)
  {
    name = _name;
    addresses = _addresses;
    resolvers = _resolvers;
    userDefinedProperties = _userDefinedProperties;
  } // ctor

} // class AgentID
