package FIPA;


/**
* FIPA/OptTransportBehaviourTypeHolder.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from FIPA.idl
* 09 March 2001 16:39:32 o'clock GMT
*/

public final class OptTransportBehaviourTypeHolder implements org.omg.CORBA.portable.Streamable
{
  public FIPA.Property value[][] = null;

  public OptTransportBehaviourTypeHolder ()
  {
  }

  public OptTransportBehaviourTypeHolder (FIPA.Property[][] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = FIPA.OptTransportBehaviourTypeHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    FIPA.OptTransportBehaviourTypeHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return FIPA.OptTransportBehaviourTypeHelper.type ();
  }

}
