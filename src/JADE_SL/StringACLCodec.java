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

package JADE_SL;

import java.io.*;
import JADE_SL.AID;
import JADE_SL.CaseInsensitiveString;

import java.util.*;
import java.util.Date;

import JADE_SL.Base64;
import zeus.concepts.FIPAParser;
import zeus.concepts.FIPAPerformative;

/**
  This class implements the FIPA String codec for ACLMessages.
  Notice that it is not possible to convey 
  a sequence of bytes over a StringACLCodec because the bytes with
  the 8th bit ON cannot properly converted into a char.
 @author Fabio Bellifemine - CSELT S.p.A.
 @version $Date: 2003/10/09 12:55:35 $ $Revision: 1.1.1.1 $
 **/
public class StringACLCodec{

  public static final String NAME ="STRING"; 

    /** Key of the user-defined parameter used to signal the automatic JADE
	conversion of the content into Base64 encoding  **/
    private static final String BASE64ENCODING_KEY = new String("JADE-Encoding");
    /** Value of the user-defined parameter used to signal the automatic JADE
	conversion of the content into Base64 encoding  **/
    private static final String BASE64ENCODING_VALUE = new String("Base64");

  private static final String SENDER          = new String(" :sender ");
  private static final String RECEIVER        = new String(" :receiver ");
  private static final String CONTENT         = new String(" :content "); 
  private static final String REPLY_WITH      = new String(" :reply-with ");
  private static final String IN_REPLY_TO     = new String(" :in-reply-to ");
  private static final String REPLY_TO        = new String(" :reply-to ");
  private static final String LANGUAGE        = new String(" :language ");
  private static final String ENCODING        = new String(" :encoding ");
  private static final String ONTOLOGY        = new String(" :ontology ");
  private static final String REPLY_BY        = new String(" :reply-by ");
  private static final String PROTOCOL        = new String(" :protocol ");
  private static final String CONVERSATION_ID = new String(" :conversation-id ");

  FIPAParser parser;
  Writer out;

  /**
   * constructor for the codec.
   * The standard input is used as an input stream of ACL messages.
   * The standard output is used to write encoded ACL messages.
   */
  public StringACLCodec() {
    parser = new FIPAParser(System.in);
    out = new OutputStreamWriter(System.out);
  }


  /**
   * constructor for the codec.
   * @parameter r is the input stream for the ACL Parser (pass 
   * <code>new InputStreamReader(System.in)</code> 
   * if you want to use the standard input)
   * @parameter w is the writer to write encoded ACL messages (pass 
   * <code>new OutputStreamWriter(System.out)</code> if you want to 
   * use the standard output)
   */
  public StringACLCodec(Reader r, Writer w) {
    parser = new FIPAParser(r);
    out = w;
  }


    /**
     * if there was an automatical Base64 encoding, then it performs
     * automatic decoding.
     **/
    private void checkBase64Encoding(ACLMessage msg) {
	String encoding = msg.getUserDefinedParameter(BASE64ENCODING_KEY);
	if (CaseInsensitiveString.equalsIgnoreCase(BASE64ENCODING_VALUE,encoding)) {
	    try { // decode Base64
		String content = msg.getContent();
		if ((content != null) && (content.length() > 0)) {
		    char[] cc = new char[content.length()];
		    content.getChars(0,content.length(),cc,0);
		    msg.setByteSequenceContent(Base64.decode(cc));
		    msg.removeUserDefinedParameter(BASE64ENCODING_KEY); // reset the slot value for encoding
		}
	    } catch(java.lang.StringIndexOutOfBoundsException e){
		e.printStackTrace();
	    } catch(java.lang.NullPointerException e2){
		e2.printStackTrace();
	    } catch(java.lang.NoClassDefFoundError jlncdfe) {
		System.err.println("\t\t===== E R R O R !!! =======\n");
		System.err.println("Missing support for Base64 conversions");
		System.err.println("Please refer to the documentation for details.");
		System.err.println("=============================================\n\n");
		try {
		    Thread.currentThread().sleep(3000);
		}catch(InterruptedException ie) {
		}
	    }
	} //end of if CaseInsensitiveString
    }

  /**
   * decode and parses the next message from the Reader passed in the 
   * constructor.
   * @return the ACLMessage
   * @throws ACLCodec.CodecException if any Exception occurs during the 
   * parsing/reading operation
   */
  public FIPAPerformative decode() throws Exception {
    try {
      FIPAPerformative msg = parser.Message();
      return msg;
    }
    catch (Exception e) {
        throw e; }

  }

  /**
   * encodes the message and writes it into the Writer passed in the 
   * constructor.
   * Notice that this method does not call <code>flush</code> on the writer.
   @ param msg is the ACLMessage to encode and write into
   */
  public void write(ACLMessage msg) {
      try {
	  out.write(toString(msg));
      } catch (IOException ioe) {
	  ioe.printStackTrace();
      }
  }


  static private String escape(String s) {
    // Make the stringBuffer a little larger than strictly
    // necessary in case we need to insert any additional
    // characters.  (If our size estimate is wrong, the
    // StringBuffer will automatically grow as needed).
    StringBuffer result = new StringBuffer(s.length()+20);
    for( int i=0; i<s.length(); i++)
      if( s.charAt(i) == '"' ) 
	result.append("\\\"");
      else 
	result.append(s.charAt(i));
    return result.toString();
  }


    /**
     * @return a String encoded message
     * @see ACLMessage#toString()
     **/
    static String toString(ACLMessage msg) {
      StringBuffer str = new StringBuffer("(");
      str.append(msg.getPerformative(msg.getPerformative()) + "\n");
      AID sender = msg.getSender();
      if (sender != null) 
	str.append(SENDER + " "+ sender.toString()+"\n");
      Iterator it = msg.getAllReceiver();
      if (it.hasNext()) {
	str.append(RECEIVER + " (set ");
	while(it.hasNext()) 
	  str.append(it.next().toString()+" ");
	str.append(")\n");
      }
      it = msg.getAllReplyTo();
      if (it.hasNext()) {
	str.append(REPLY_TO + " (set \n");
	while(it.hasNext()) 
	  str.append(it.next().toString()+" ");
	str.append(")\n");
      }
      if (msg.hasByteSequenceContent()) {
	  str.append(":X-"+ BASE64ENCODING_KEY + " " + BASE64ENCODING_VALUE + "\n");
	  try {
	      String b64 = new String(Base64.encode(msg.getByteSequenceContent()));
	      str.append(CONTENT + " \"" + b64 + "\" \n");
	  } catch(java.lang.NoClassDefFoundError jlncdfe) {
	      System.err.println("\n\t===== E R R O R !!! =======\n");
	      System.err.println("Missing support for Base64 conversions");
	      System.err.println("Please refer to the documentation for details.");
	      System.err.println("=============================================\n\n");
	      System.err.println("");
	      try {
		  Thread.currentThread().sleep(3000);
	      } catch(InterruptedException ie) {
	      }
	  }
      } else {
	  String content = msg.getContent();
	  if (content != null) {
	      content = content.trim();
	      if (content.length() > 0)
		  str.append(CONTENT + " \"" + escape(content) + "\" \n");
	  }
      }
      String tmp = msg.getReplyWith();
      if (tmp != null) {
	  tmp = tmp.trim();
	  if (tmp.length() > 0)
	      str.append(REPLY_WITH + " " + tmp + "\n");
      }
      tmp = msg.getInReplyTo();
      if (tmp != null) {
	  tmp = tmp.trim();
	  if (tmp.length() > 0)
	      str.append(IN_REPLY_TO + " " + tmp + "\n");
      }
      tmp = msg.getEncoding();
      if (tmp != null) {
	  tmp = tmp.trim();
	  if (tmp.length() > 0)
	      str.append(ENCODING + " " + tmp + "\n");
      }
      tmp = msg.getLanguage();
      if (tmp != null) {
	  tmp = tmp.trim();
	  if (tmp.length() > 0)
	      str.append(LANGUAGE + " " + tmp + "\n");
      }
      tmp = msg.getOntology();
      if (tmp != null) {
	  tmp = tmp.trim();
	  if (tmp.length() > 0)
	      str.append(ONTOLOGY + " " + tmp + "\n");
      }
      Date d = msg.getReplyByDate();
      if (d != null)
	  str.append(REPLY_BY + " " + ISO8601.toString(d) + "\n");
      tmp = msg.getProtocol();
      if (tmp != null) {
	  tmp = tmp.trim();
	  if (tmp.length() > 0)
	      str.append(PROTOCOL + " " + tmp + "\n");
      }
      tmp = msg.getConversationId();
      if (tmp != null) {
	  tmp = tmp.trim();
	  if (tmp.length() > 0)
	      str.append(CONVERSATION_ID + " " + tmp + "\n");
      }
      Properties userDefProps = msg.getAllUserDefinedParameters();
      Enumeration e = userDefProps.propertyNames();
      while (e.hasMoreElements()) {
	String key = ((String)e.nextElement()).trim();
	String value = userDefProps.getProperty(key);
	if (value != null) {
	    value = value.trim();
	    if (value.length()>0)
		str.append(" :X-" + key + " " + value + "\n");
	}
      }
      str.append(")");

      return str.toString();
    }

  /**
   * If the content of the message is a byteSequence, then this
   * method encodes the content in Base64 and automatically sets the value
   * of the encoding slot.
   * @see ACLCodec#encode(ACLMessage msg)
   */
  public byte[] encode(ACLMessage msg) {
    try {
	return toString(msg).getBytes("US-ASCII");
    }
    catch(IOException ioe) {
      ioe.printStackTrace();
      return new byte[0];
    }
  }

  /**
   * @see ACLCodec#decode(byte[] data)
   */
/*  public FIPAPerformative decode(byte[] data) throws Exception {
    try {
      FIPAPerformative msg = ACLParser.Message(new InputStreamReader(new ByteArrayInputStream(data)));
      checkBase64Encoding(msg);
      return msg;
    } catch (jade.lang.acl.TokenMgrError e1) {
      throw new ACLCodec.CodecException(getName()+" ACLMessage decoding token exception",e1);
    } catch (Exception e2) {
      throw new ACLCodec.CodecException(getName()+" ACLMessage decoding exception",e2);
    }
  }*/

  /**
   * @return the name of this encoding according to the FIPA specifications
   */
  public String getName() {
    return NAME;
  }
}
