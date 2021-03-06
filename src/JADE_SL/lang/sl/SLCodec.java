/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * 
 * GNU Lesser General Public License
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package JADE_SL.lang.sl;

import JADE_SL.onto.Ontology;
import JADE_SL.abs.*;
import JADE_SL.lang.StringCodec;
import sl.ISO8601;
import java.util.Iterator;
import JADE_SL.CaseInsensitiveString;
import JADE_SL.FIPANames;

import java.util.Date;
import java.util.Vector;
import java.io.StringReader;
import java.io.BufferedReader; // only for debugging purposes in the main
import java.io.InputStreamReader; // only for debugging purposes in the main

/**  
 * The codec class for the <b><i>FIPA-SL</i>n</b> languages. This class
 * implements the <code>Codec</code> interface and allows converting
 * back and forth between strings and frames, according to the SL
 * grammar.
 * By default the class implements full SL grammar, otherwise the proper
 * value must be used in the constructor.
 * @author Fabio Bellifemine - TILAB 
 * @version $Date: 2003/10/09 12:55:36 $ $Revision: 1.1.1.1 $
 */
public class SLCodec extends StringCodec {

    private SLParser parser;

    /**
     * Construct a Codec object for the full SL-language (FIPA-SL).
     */
    public SLCodec() {
    	this(3);
    }
    
    /**
     * Construct a Codec object for the given profile of SL-language.
     * @parameter slType specify 0 for FIPA-SL0, 1 for FIPA-SL1, 2 for FIPA-SL2, any other value can be used for full FIPA-SL
     */
    public SLCodec(int slType) {
	super((slType==0 ? FIPANames.ContentLanguage.FIPA_SL0 :
	       (slType==1 ? FIPANames.ContentLanguage.FIPA_SL1 :
		(slType==2 ? FIPANames.ContentLanguage.FIPA_SL2 :
		 FIPANames.ContentLanguage.FIPA_SL ))));
	if ((slType < 0) || (slType > 2)) {
	    slType = 3;
	    vectorOfPredefinedFunctionals = FullSLFunctionals;
	} else
	    vectorOfPredefinedFunctionals = SL0Functionals;
	parser = new SLParser(new StringReader(""));
	parser.setSLType(slType); 
    }


    /**
     * Encodes a content into a String.
     * @param content the content as an abstract descriptor.
     * @return the content as a String.
     * @throws CodecException
     */
    public String encode(AbsContentElement content) throws CodecException {
	return encode(null, content);
    }

    /**
     * Encodes a content into a String.
     * @param ontology the ontology 
     * @param content the content as an abstract descriptor.
     * @return the content as a String.
     * @throws CodecException
     */
    public String encode(Ontology ontology, AbsContentElement content) throws CodecException {
	StringBuffer str = new StringBuffer("(");
	if (content instanceof AbsContentElementList) {
	    for (Iterator i=((AbsContentElementList)content).iterator(); i.hasNext(); ) {
		AbsObject o = (AbsObject)i.next();
		str.append(toString(o));
		str.append(" ");
	    }
	} else str.append(toString(content));
	str.append(")");
	/*try {
	    return str.toString().getBytes("US-ASCII");
	} catch (java.io.UnsupportedEncodingException e) {
	    e.printStackTrace();
	    return str.toString().getBytes();
	}*/
	return str.toString();
    }

  /** 
   * Take a java String and quote it to form a legal FIPA SL0 string.
   * Add quotation marks to the beginning/end and escape any 
   * quotation marks inside the string.
   * This must be the exact inverse of the procedure in
   * the parser (SLParser.jj) when it encounters a quoted string.
   */
  private String quotedString(String s)
  {
      // Make the stringBuffer a little larger than strictly
      // necessary in case we need to insert any additional
      // characters.  (If our size estimate is wrong, the
      // StringBuffer will automatically grow as needed).
      StringBuffer result = new StringBuffer(s.length()+20);
      result.append("\"");
      for( int i=0; i<s.length(); i++)
          if( s.charAt(i) == '"' ) 
              result.append("\\\"");
          else 
              result.append(s.charAt(i));
      result.append("\"");
      return result.toString();
  }



    /**
     * Test if the given string is a legal SL0 word using the FIPA XC00008D spec.
     * In addition to FIPA's restrictions, place the additional restriction 
     * that a Word can not contain a '\"', that would confuse the parser at
     * the other end.
     */
    private boolean isAWord( String s) {
	// This should permit strings of length 0 to be encoded.
	if( s==null || s.length()==0 )
	    return false; // words must have at least one character

	String illegalFirstChar = new String("#0123456789:-?");
     
	if ( illegalFirstChar.indexOf(s.charAt(0)) >= 0 )
	    return false;
      
	for( int i=0; i< s.length(); i++)
	    if( s.charAt(i) == '"' || s.charAt(i) == '(' || 
		s.charAt(i) == ')' || s.charAt(i) <= 0x20 )
		return false;
	return true;
    }


    /**
     * Encode a string, taking care of quoting separated words and
     * escaping strings, if necessary
     **/
    private String encode(String val) {
	// if the slotName is a String of words then quote it
	if (isAWord(val)) 
	    return val;
	else
	    return quotedString(val);
    }


    /**
     * this method is used by all the toString methods and it exploits
     * the common AbsObject implementation
     * @param encodeSlotNames if true and the name of the slot does not
     * start with <code>Codec.UNNAMEDPREFIX</code>, then the slotName is
     * also encoded, otherwise it is skipped.
     **/
    private String encode(AbsObject val, boolean encodeSlotNames) throws CodecException {
	StringBuffer str = new StringBuffer("(");
	str.append(encode(val.getTypeName()));
	String[] slotNames = val.getNames();
	// check if there is a slot name that starts With Codec.UNNAMEDPREFIX
	// FIXME. This can be improved because it might lower performance!
	if (encodeSlotNames && (slotNames != null)) {
	    for (int i=0; i<slotNames.length; i++)
		if (slotNames[i].startsWith(UNNAMEDPREFIX)) {
		    encodeSlotNames = false;
		    break;
		}
	}
	if (slotNames != null) 
	    for (int i=0; i<slotNames.length; i++) {
		if (encodeSlotNames) {
		    str.append(" :");
		    str.append(encode(slotNames[i]));
		}
		str.append(" ");
		str.append(toString(val.getAbsObject(slotNames[i])));
	    }
	str.append(")");
	return str.toString();
    }


    private String toString(AbsPredicate val) throws CodecException {
	if (val.getCount() > 0)
	    return encode(val, false);
	else
	    return encode(val.getTypeName()); // a proposition does not require parenthesis
    }

    private String toString(AbsIRE val) throws CodecException {
	return "(" + encode(val.getTypeName()) + " " + toString(val.getVariable()) + " " + toString(val.getProposition()) + ")"; 
    }

    private String toString(AbsVariable val) throws CodecException {
	String var = val.getName();
	if (!var.startsWith("?"))
	    return "?"+encode(var);
	else
	    return "?"+encode(var.substring(1));
    }

    /** Constant needed to create an <code>AbsIRE(SLCodec.IOTA)</code> **/
    public final static String IOTA = "IOTA";
    // these 2 constants, set and sequence, are also used by SLParser.jj
    final static String SET = "set";
    final static String SEQUENCE = "sequence";
    /** Vector of all the functionals which have been pre-defined by FIPA
     * and whose slots should not be encoded */
    private Vector vectorOfPredefinedFunctionals;
    private static Vector SL0Functionals = new Vector(5); 
    private static Vector FullSLFunctionals = new Vector(17); 
    static {
	SL0Functionals.addElement(SET); 
	SL0Functionals.addElement(SEQUENCE);
	SL0Functionals.addElement("action");
	SL0Functionals.addElement("|");
	SL0Functionals.addElement(";");
	FullSLFunctionals.addElement(SL0Functionals.elementAt(0));
	FullSLFunctionals.addElement(SL0Functionals.elementAt(1));
 	FullSLFunctionals.addElement(SL0Functionals.elementAt(2));
  FullSLFunctionals.addElement(SL0Functionals.elementAt(3));
	FullSLFunctionals.addElement("cons");
	FullSLFunctionals.addElement("first");
	FullSLFunctionals.addElement("rest");
	FullSLFunctionals.addElement("nth");
	FullSLFunctionals.addElement("append");
	FullSLFunctionals.addElement("union");
	FullSLFunctionals.addElement("intersection");
	FullSLFunctionals.addElement("difference");
	FullSLFunctionals.addElement("+");
	FullSLFunctionals.addElement("-");
	FullSLFunctionals.addElement("/");
	FullSLFunctionals.addElement("%");
    }

    /* if this is one of the FIPA-defined functionals, i.e. "+","-",...
     * case do not add the slotName
     */
    private boolean requiresSlotNames(String conceptName) {
	return !vectorOfPredefinedFunctionals.contains(conceptName.toLowerCase());
    }

    private String toString(AbsConcept val) throws CodecException {
	return encode(val, requiresSlotNames(val.getTypeName()));
    }


    private String toString(AbsAggregate val) throws CodecException {
    	StringBuffer str = new StringBuffer("(");
			str.append(encode(val.getTypeName()));
			for (Iterator i=val.iterator(); i.hasNext(); ) {
				str.append(" ");
				str.append(toString((AbsObject)i.next()));
			}
	    str.append(")");
	    return str.toString();
    }

/*
    private String toString(AbsAgentAction val) throws CodecException {
    	if ( CaseInsensitiveString.equalsIgnoreCase("action",val.getTypeName()) ||
      		 CaseInsensitiveString.equalsIgnoreCase("|",val.getTypeName()) ||
     		 	 CaseInsensitiveString.equalsIgnoreCase(";",val.getTypeName()))
 				return encode(val, false);
 			else
 				//throw new CodecException("SLEncoderRequiresTheSLActionOperator_insteadOf_"+val.getTypeName());
    }*/

    private String toString(AbsPrimitive val) throws CodecException {
	Object v = val.getObject();
	if (v instanceof Date)
	    return ISO8601.toString((Date)v);
	else if (v instanceof Number)
		  return v.toString();
  else if (v instanceof byte[])
  	throw new CodecException("SL_does_not_allow_encoding_sequencesOfBytes");
  else	
	  return encode(v.toString());
    }

    private String toString(AbsObject val) throws CodecException {
	if (val instanceof AbsPrimitive) return toString( (AbsPrimitive)val);
	if (val instanceof AbsPredicate) return toString( (AbsPredicate)val);
	if (val instanceof AbsIRE) return toString( (AbsIRE)val);
	if (val instanceof AbsVariable) return toString( (AbsVariable)val);
//	if (val instanceof AbsAgentAction) return toString( (AbsAgentAction)val);
	if (val instanceof AbsAggregate) return toString( (AbsAggregate)val);
	if (val instanceof AbsConcept) return toString( (AbsConcept)val);
	throw new CodecException("SLCodec cannot encode this object "+val);
    }




    /**
     * Decodes the content to an abstract description.
     * @param content the content as a String.
     * @return the content as an abstract description.
     * @throws CodecException
     */
    public AbsContentElement decode(String content) throws CodecException {
	return decode(null, content); 
    }

    /**
     * Decodes the content to an abstract description.
     * @param ontology the ontology.
     * @param content the content as a String.
     * @return the content as an abstract description.
     * @throws CodecException
     */
    public AbsContentElement decode(Ontology ontology, String content) throws CodecException {
	try {
	    return parser.parse(ontology,content);
	}  catch(Throwable e) { // both ParseException and TokenMgrError
	    throw new CodecException("Parse exception", e);
	}
    }


    public static void main(String[] args) {
	SLCodec codec = null;
	try {
	    codec = new SLCodec(Integer.parseInt(args[0]));
	} catch (Exception e) {
	    System.out.println("usage: SLCodec SLLevel\n  where SLLevel can be 0 for SL0, 1 for SL1, 2 for SL2, 3 or more for full SL");
	    System.exit(0);
	}

	while (true) {
	    try {
		System.out.println("insert an SL expression to parse (all the expression on a single line!): ");
		BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
		String str = buff.readLine();
		System.out.println("\n\n");
		//AbsContentElement result = codec.decode(str.getBytes("US-ASCII"));
		AbsContentElement result = codec.decode(str);
		System.out.println("DUMP OF THE DECODE OUTPUT:");
		result.dump();
		System.out.println("\n\n");
		System.out.println("AFTER ENCODE:");
		//System.out.println(new String(codec.encode(result),"US-ASCII"));
		System.out.println(codec.encode(result));
		System.out.println("\n\n");
	    } catch(Exception pe) {
		pe.printStackTrace();
		//System.exit(0);
	    }
	}
    }


    /**
     * @return the ontology containing the schemas of the operator
     * defined in this language
     */
    public Ontology getInnerOntology() {
    	return SLOntology.getInstance();
    }
}

