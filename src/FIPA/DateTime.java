package FIPA;


/**
* FIPA/DateTime.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from FIPA.idl
* 09 March 2001 16:39:32 o'clock GMT
*/


// to the local timezone.
public final class DateTime implements org.omg.CORBA.portable.IDLEntity
{
  public short year = (short)0;

  // year (e.g. 2000)
  public short month = (short)0;

  // between 1 and 12
  public short day = (short)0;

  // between 1 and 31
  public short hour = (short)0;

  // between 0 and 23
  public short minutes = (short)0;

  // between 0 and 59
  public short seconds = (short)0;

  // between 0 and 59
  public short milliseconds = (short)0;

  // between 0 and 999
  public char typeDesignator = (char)0;

  public DateTime ()
  {
  } // ctor

  public DateTime (short _year, short _month, short _day, short _hour, short _minutes, short _seconds, short _milliseconds, char _typeDesignator)
  {
    year = _year;
    month = _month;
    day = _day;
    hour = _hour;
    minutes = _minutes;
    seconds = _seconds;
    milliseconds = _milliseconds;
    typeDesignator = _typeDesignator;
  } // ctor

} // class DateTime
