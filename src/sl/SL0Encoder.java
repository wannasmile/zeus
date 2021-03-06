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

import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;

import java.util.*;

/**
  
  @author Giovanni Rimassa - Universita` di Parma
  @version $Date: 2003/10/09 12:55:37 $ $Revision: 1.1.1.1 $
 */
class SL0Encoder {

  public String encode(Frame f) {
    StringWriter out = new StringWriter();
    try {
      if (f.size()<=0) //if is a PropositionSymbol,then just write the symbol
	out.write(f.getName());
      else // else write the frame
	writeFrame(f, out);
      out.flush();
    }
    catch(IOException ioe) {
      ioe.printStackTrace();
    }
    catch(OntologyException oe) {
      oe.printStackTrace();
    }
    catch(CodecException ce) {
      ce.printStackTrace();
    }
    return out.toString();
  }


  private void writeFrame(Frame f, Writer w) throws IOException, OntologyException, CodecException {
  	w.write("(" + f.getName() + " ");
		for (int i=0; i<f.size(); i++ ) {
			String slotName = f.getSlotName(i); 
			Object slotValue = f.getSlot(i);
			if (!slotName.startsWith(Frame.UNNAMEDPREFIX)) {
	  		// if the slotName does not start with ':' then add ':'
	  		if (!slotName.startsWith(":"))
	    		w.write(":");
	  		// if the slotName is a String of words then quote it
	  		if (slotName.indexOf(" ")>-1) 
	    		w.write("\""+slotName+"\"");
	  		else
	    		w.write(slotName);
	  		w.write(" ");
			}
			if (isFrame(slotValue))
	  		writeFrame((Frame)slotValue, w);
			else if (slotValue.getClass().equals(java.util.Date.class))
	  		// if it is a Date then write a DateTimetoken
	  		// I wanted to use an SLDate that extends Date but, if I did
	  		// then the ontology would no more be language-independent!
	  		w.write(ISO8601.toString((java.util.Date)slotValue));
	  	else if (slotValue.getClass().equals(java.lang.Byte[].class))
	  		throw new CodecException("SL0 does not support bynary fields", null);
			else {
	  		// If the stringified slot value is a String of words or is the empty string then quote it.
	  		String stringifiedValue = slotValue.toString();
	  		if (mustBeQuoted(stringifiedValue))
			  w.write("\""+stringifiedValue+"\"");
			else
			  w.write(stringifiedValue);
			}
			w.write(" ");
		}
    w.write(")");
  }

  private boolean mustBeQuoted(String s) {
    s.trim();
    if ((s.charAt(0) == '\"') && (s.charAt(s.length()-1) == '\"'))
      return false;
    else if (s.indexOf(" ") > -1 || s.equals("") || isAToken(s))
      return true;
    else
      return false;
  }

  /**
    If this is a token of the language, then it must be quoted. 
    **/
  private boolean isAToken(String str) {
    return str.equalsIgnoreCase(SL0Codec.NAME_OF_ACTION_FRAME);  
  }

   private boolean isFrame(Object f) {
     return (f.getClass()==Frame.class);
   }
}
