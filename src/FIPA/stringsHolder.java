package FIPA;


/**
* FIPA/stringsHolder.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from FIPA.idl
* 09 March 2001 16:39:32 o'clock GMT
*/

public final class stringsHolder implements org.omg.CORBA.portable.Streamable
{
  public String value[] = null;

  public stringsHolder ()
  {
  }

  public stringsHolder (String[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = FIPA.stringsHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    FIPA.stringsHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return FIPA.stringsHelper.type ();
  }

}
