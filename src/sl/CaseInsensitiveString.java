/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/
package sl;

import java.io.Serializable;

/**
  A name string, with case insensitive comparison and equality operations.
  This class holds a <code>String</code> inside, preserving the case; however,
  all the equality and comparision operations are performed in a case
  insensitive fashion.

  @author Giovanni Rimassa - Universita` di Parma
  @version $Date: 2003/10/09 12:55:36 $ $Revision: 1.1.1.1 $
*/
public class CaseInsensitiveString implements Serializable {

	/**
	@serial
	*/
  String s;

  /**
    Create a new <code>Name</code> object.
    @param name The string that will be kept inside this object.
  */
  public CaseInsensitiveString(String name) {

    s = name;

  }


  /**
    Converts the <code>Name</code> object into a string.
    @return The string stored inside by the constructor.
  */
  public String toString() {

    return s.toString();

  }

  /**
    Equality operation. This method compares a <code>Name</code> object with
    another or with a Java <code>String</code>. The comparison is case
    insensitive.
    @param o The Java object to compare this <code>Name</code> to.
    @return <code>true</code> if the strings contained within the two objects
    are equal, apart from case.
  */
  public boolean equals(Object o) {

    if(o instanceof String) {
      return s.equalsIgnoreCase((String)o);
    }
    try {
      CaseInsensitiveString sn = (CaseInsensitiveString)o;
      return s.equalsIgnoreCase(sn.s);
    }
    catch(ClassCastException cce) {
      return false;
    }

  }


  /**
    Hash code. This method returns an hash code in such a way that two
    <code>Name</code> objects differing only in case have the same hash code.
    @return The hash code for this <code>Name</code> object.
  */
  public int hashCode() {
    return s.toLowerCase().hashCode();
  }



}