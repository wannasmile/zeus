package FIPA;

/**
* FIPA/FipaMessageHolder.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from FIPA.idl
* 09 March 2001 16:39:32 o'clock GMT
*/

public final class FipaMessageHolder implements org.omg.CORBA.portable.Streamable
{
  public FIPA.FipaMessage value = null;

  public FipaMessageHolder ()
  {
  }

  public FipaMessageHolder (FIPA.FipaMessage initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = FIPA.FipaMessageHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    FIPA.FipaMessageHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return FIPA.FipaMessageHelper.type ();
  }

}
