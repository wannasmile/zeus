package FIPA;

/**
* FIPA/AgentIDHolder.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from FIPA.idl
* 09 March 2001 16:39:32 o'clock GMT
*/

public final class AgentIDHolder implements org.omg.CORBA.portable.Streamable
{
  public FIPA.AgentID value = null;

  public AgentIDHolder ()
  {
  }

  public AgentIDHolder (FIPA.AgentID initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = FIPA.AgentIDHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    FIPA.AgentIDHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return FIPA.AgentIDHelper.type ();
  }

}