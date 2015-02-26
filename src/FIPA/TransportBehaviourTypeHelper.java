package FIPA;


/**
* FIPA/TransportBehaviourTypeHelper.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from FIPA.idl
* 09 March 2001 16:39:32 o'clock GMT
*/

abstract public class TransportBehaviourTypeHelper
{
  private static String  _id = "IDL:FIPA/TransportBehaviourType:1.0";

  public static void insert (org.omg.CORBA.Any a, FIPA.Property[] that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static FIPA.Property[] extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = FIPA.PropertyHelper.type ();
      __typeCode = org.omg.CORBA.ORB.init ().create_sequence_tc (0, __typeCode);
      __typeCode = org.omg.CORBA.ORB.init ().create_alias_tc (FIPA.TransportBehaviourTypeHelper.id (), "TransportBehaviourType", __typeCode);
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static FIPA.Property[] read (org.omg.CORBA.portable.InputStream istream)
  {
    FIPA.Property value[] = null;
    int _len0 = istream.read_long ();
    value = new FIPA.Property[_len0];
    for (int _o1 = 0;_o1 < value.length; ++_o1)
      value[_o1] = FIPA.PropertyHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, FIPA.Property[] value)
  {
    ostream.write_long (value.length);
    for (int _i0 = 0;_i0 < value.length; ++_i0)
      FIPA.PropertyHelper.write (ostream, value[_i0]);
  }

}