package FIPA;

/**
* FIPA/ReceivedObjectHolder.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from FIPA.idl
* 09 March 2001 16:39:32 o'clock GMT
*/

public final class ReceivedObjectHolder implements org.omg.CORBA.portable.Streamable
{
  public FIPA.ReceivedObject value = null;

  public ReceivedObjectHolder ()
  {
  }

  public ReceivedObjectHolder (FIPA.ReceivedObject initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = FIPA.ReceivedObjectHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    FIPA.ReceivedObjectHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return FIPA.ReceivedObjectHelper.type ();
  }

}
