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
package JADE_SL.abs;

import JADE_SL.onto.*;
import JADE_SL.*;
import JADE_SL.schema.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import JADE_SL.AID;
import JADE_SL.OntoAID;
import JADE_SL.CaseInsensitiveString;
import JADE_SL.ACLMessage;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Federico Bergenti - Universita` di Parma
 */
public class AbsHelper {
    /**
     * Converts a <code>List</code> into a <code>AbsAggregate</code> using
     * the specified ontology.
     * @param obj the <code>List</code>
     * @param onto the ontology.
     * @return the abstract descriptor.
     * @throws OntologyException
     */
    public static AbsAggregate externaliseList(List obj, Ontology onto) throws OntologyException {
        AbsAggregate ret = new AbsAggregate(BasicOntology.SEQUENCE);
        try {
        	for (int i = 0; i < obj.size(); i++) {
            ret.add((AbsTerm) (onto.fromObject(obj.get(i))));
        	}
        } catch (Exception e) {
        	throw new OntologyException("Non term object in aggregate");}

        return ret;
    } 

    /**
     * Converts an <code>Iterator</code> into a <code>AbsAggregate</code> using
     * the specified ontology.
     * @param obj the <code>Iterator</code>
     * @param onto the ontology.
     * @return the abstract descriptor.
     * @throws OntologyException
     */
    public static AbsAggregate externaliseIterator(Iterator obj, Ontology onto) throws OntologyException {
        AbsAggregate ret = new AbsAggregate(BasicOntology.SEQUENCE);

        try {
        	while(obj.hasNext())
            ret.add((AbsTerm) (onto.fromObject(obj.next())));
        }
        catch (ClassCastException cce) {
        	throw new OntologyException("Non term object in aggregate");
        }
        return ret;
    }

    /**
     * Converts an <code>AID</code> into an <code>AbsConcept</code> 
     * representing an AID 
     * @param obj the <code>AID</code>
     * @return the abstract descriptor.
     */
    public static AbsConcept externaliseAID(AID obj) {
    	AbsConcept aid = new AbsConcept(BasicOntology.AID);
      // Name
      aid.set(BasicOntology.AID, obj.getName());
      
      // Addresses
			AbsAggregate addresses = new AbsAggregate(BasicOntology.SEQUENCE);
			for(Iterator i = obj.getAllAddresses(); i.hasNext(); ) {
				String addr = (String) i.next();
	    			addresses.add(AbsPrimitive.wrap(addr));
			}
			aid.set(BasicOntology.FIPA_AID_Address_ADDRESSES, addresses);

			// Resolvers
			AbsAggregate resolvers = new AbsAggregate(BasicOntology.SEQUENCE);
			for(Iterator i = obj.getAllResolvers(); i.hasNext(); ) {
				AID res = (AID) i.next();
	    	resolvers.add(externaliseAID(res));
			}
			aid.set(BasicOntology.FIPA_AID_Address_RESOLVERS, resolvers);
			
      return aid;
    } 

    /**
     * Converts a <code>ContentElementList</code> into an
     * <code>AbsContentElementList</code> using
     * the specified ontology.
     * @param obj the <code>ContentElementList</code>
     * @param onto the ontology.
     * @return the abstract descriptor.
     * @throws OntologyException
     */
    public static AbsContentElementList externaliseContentElementList(ContentElementList obj, Ontology onto) throws OntologyException {
        AbsContentElementList ret = new AbsContentElementList();

        try {
        	for (int i = 0; i < obj.size(); i++) {
            ret.add((AbsContentElement) (onto.fromObject(obj.get(i))));
        	}
        }
        catch (ClassCastException cce) {
        	throw new OntologyException("Non content element object in content element list");
        }

        return ret;
    } 

    /**
     * Converts an <code>ACLMessage</code> into an
     * <code>AbsAgentAction</code> using
     * the specified ontology.
     * @param obj the <code>ACLMessage</code>
     * @param onto the ontology.
     * @return the abstract descriptor.
     * @throws OntologyException
     */
    public static AbsAgentAction externaliseACLMessage(ACLMessage obj, Ontology onto) throws OntologyException {
  		try {
  			AbsAgentAction absMsg =  new AbsAgentAction(BasicOntology.ACLMSG);
  			
  			absMsg.set(BasicOntology.ACLMSG_PERFORMATIVE, AbsPrimitive.wrap(obj.getPerformative()));
  			absMsg.set(BasicOntology.ACLMSG_SENDER, (AbsTerm) onto.fromObject(obj.getSender()));
  			// Receivers
  			AbsAggregate recvs = new AbsAggregate(BasicOntology.SEQUENCE);
  			Iterator it = obj.getAllReceiver();
  			while (it.hasNext()) {
  				recvs.add((AbsTerm) onto.fromObject(it.next()));
  			}
  			absMsg.set(BasicOntology.ACLMSG_RECEIVERS, recvs);
  			// Reply_to
  			AbsAggregate repls = new AbsAggregate(BasicOntology.SEQUENCE);
  			it = obj.getAllReceiver();
  			while (it.hasNext()) {
  				repls.add((AbsTerm) onto.fromObject(it.next()));
  			}
  			absMsg.set(BasicOntology.ACLMSG_REPLY_TO, repls);
  		
  			absMsg.set(BasicOntology.ACLMSG_LANGUAGE, obj.getLanguage());
  			absMsg.set(BasicOntology.ACLMSG_ONTOLOGY, obj.getOntology());
  			absMsg.set(BasicOntology.ACLMSG_PROTOCOL, obj.getProtocol());
  			absMsg.set(BasicOntology.ACLMSG_IN_REPLY_TO, obj.getInReplyTo());
  			absMsg.set(BasicOntology.ACLMSG_REPLY_WITH, obj.getReplyWith());
  			absMsg.set(BasicOntology.ACLMSG_CONVERSATION_ID, obj.getConversationId());
  			absMsg.set(BasicOntology.ACLMSG_REPLY_BY, obj.getReplyByDate());
  			// Content
  			if (obj.hasByteSequenceContent()) {
	  			absMsg.set(BasicOntology.ACLMSG_BYTE_SEQUENCE_CONTENT, obj.getByteSequenceContent());
  			}
  			else {
	  			absMsg.set(BasicOntology.ACLMSG_CONTENT, obj.getContent());
  			}
  			absMsg.set(BasicOntology.ACLMSG_ENCODING, obj.getEncoding());
  			
  			return absMsg;
  		}
  		catch (Exception e) {
  			throw new OntologyException("Error externalising ACLMessage", e);
  		}
  	}

            
    /**
     * Converts an <code>AbsAggregate</code> into a List using the 
     * specified ontology.
     * @param onto the ontology
     * @return the List
     * @throws OntologyException
     */
    public static List internaliseList(AbsAggregate aggregate, Ontology onto) throws OntologyException {
        List ret = new ArrayList();

        for (int i = 0; i < aggregate.size(); i++) {
        	Object element = onto.toObject(aggregate.get(i));
        	// Check if the element is a Term, a primitive an AID or a List
        	Ontology.checkIsTerm(element);
          ret.add(element);
        }

        return ret;
    } 

    /**
     * Converts an <code>AbsConcept</code> representing an AID 
     * into an OntoAID 
     * @return the OntoAID
     * @throws OntologyException if <code>aid</code> does not 
     * represent a valid AID
     */
    public static OntoAID internaliseAID(AbsConcept aid) throws OntologyException {
        OntoAID ret = new OntoAID();

        try {
	        // Name
					ret.setName(aid.getString(BasicOntology.AID));

      	  // Addresses
					AbsAggregate addresses = (AbsAggregate) aid.getAbsObject(BasicOntology.FIPA_AID_Address_ADDRESSES);
					for (int i = 0; i < addresses.size(); ++i) {
						String addr = ((AbsPrimitive) addresses.get(i)).getString();
	    			ret.addAddresses(addr);
					}
				
        	// Resolvers
					AbsAggregate resolvers = (AbsAggregate) aid.getAbsObject(BasicOntology.FIPA_AID_Address_RESOLVERS);
					for (int i = 0; i < resolvers.size(); ++i) {
						OntoAID res = internaliseAID((AbsConcept) resolvers.get(i));
	    			ret.addResolvers(res);
					}

        	return ret;
        }
        catch (Exception e) {
        	throw new OntologyException(aid+" is not a valid AID");
        }
    } 

    /**
     * Converts to an <code>AbsContentElementList</code> into a 
     * ContentElementList using the 
     * specified ontology.
     * @param onto the ontology
     * @return the ContentElementList
     * @throws OntologyException
     */
    public static ContentElementList internaliseContentElementList(AbsContentElementList l, Ontology onto) throws OntologyException {
        ContentElementList ret = new ContentElementList();

        try {
        	for (int i = 0; i < l.size(); i++) {
        		ContentElement element = (ContentElement) onto.toObject(l.get(i));
          	ret.add(element);
        	}
        }
        catch (ClassCastException cce) {
        	throw new OntologyException("Non content element object in content element list");
        }

        return ret;
    } 

    /**
     * Converts to an <code>AbsAgentAction</code> representing an ACLMessage
     * into an OntoACLMessage using the specified ontology.
     * @param onto the ontology
     * @return the OntoACLMessage
     * @throws OntologyException
     */
    public static OntoACLMessage internaliseACLMessage(AbsAgentAction absMsg, Ontology onto) throws OntologyException {
      OntoACLMessage ret = new OntoACLMessage();

    	try {
  			ret.setPerformative(absMsg.getInteger(BasicOntology.ACLMSG_PERFORMATIVE)); 
  			ret.setSender((AID) onto.toObject(absMsg.getAbsObject(BasicOntology.ACLMSG_SENDER))); 
  			// Receivers
  			ret.clearAllReceiver();
  			List l = (List) onto.toObject(absMsg.getAbsObject(BasicOntology.ACLMSG_RECEIVERS));
  			Iterator it = l.iterator();
  			while (it.hasNext()) {
  				ret.addReceiver((AID) it.next());
  			}
  			// ReplyTo
  			ret.clearAllReplyTo();
  			l = (List) onto.toObject(absMsg.getAbsObject(BasicOntology.ACLMSG_REPLY_TO));
  			it = l.iterator();
  			while (it.hasNext()) {
  				ret.addReplyTo((AID) it.next());
  			}
  			ret.setLanguage(absMsg.getString(BasicOntology.ACLMSG_LANGUAGE)); 
  			ret.setOntology(absMsg.getString(BasicOntology.ACLMSG_ONTOLOGY)); 
  			ret.setProtocol(absMsg.getString(BasicOntology.ACLMSG_PROTOCOL)); 
  			ret.setInReplyTo(absMsg.getString(BasicOntology.ACLMSG_IN_REPLY_TO)); 
  			ret.setReplyWith(absMsg.getString(BasicOntology.ACLMSG_REPLY_WITH)); 
  			ret.setConversationId(absMsg.getString(BasicOntology.ACLMSG_CONVERSATION_ID)); 
  			ret.setReplyByDate(absMsg.getDate(BasicOntology.ACLMSG_REPLY_BY)); 
  			String c = absMsg.getString(BasicOntology.ACLMSG_CONTENT);
  			if (c != null) {
  				ret.setContent(c);
  			}
  			else {
	  			byte[] bsc = absMsg.getByteSequence(BasicOntology.ACLMSG_BYTE_SEQUENCE_CONTENT);
	  			if (bsc != null) {
	  				ret.setByteSequenceContent(bsc);
	  			}
  			}
  			ret.setEncoding(absMsg.getString(BasicOntology.ACLMSG_ENCODING)); 
  			
  			return ret;
  		}
  		catch (Exception e) {
  			throw new OntologyException("Error internalising OntoACLMessage", e);
  		}
  	}
  	
    public static String toString(AbsObject abs) {
			ByteArrayOutputStream str = new ByteArrayOutputStream();
			((AbsObjectImpl) abs).dump(0, new PrintStream(str));
			return new String(str.toByteArray());
    
    }    
}

