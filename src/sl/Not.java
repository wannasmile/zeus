/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

The updating of this file to JADE 2.0 has been partially supported by the IST-1999-10211 LEAP Project

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

/**
   @author Giovanni Caire - CSELT S.p.A.
   @version $Date: 2003/10/09 12:55:37 $ $Revision: 1.1.1.1 $
*/

/**
   This class represents the fact that a predicate is currently false
*/
public class Not {
	private Object predicate;
	
	/**
  	 Sets the predicate that is asserted to be false.
	*/
	public void set_0(Object p) { predicate = p; }
	
	/**
  	 Gets the predicate that is asserted to be false.
	*/
	public Object get_0() { return predicate; }
}