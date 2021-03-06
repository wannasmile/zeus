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
package JADE_SL.onto;

import JADE_SL.*;
import JADE_SL.abs.*;
import JADE_SL.schema.*;
import java.util.Iterator;
import java.util.List;
import java.lang.reflect.*;

/**
 * @author Federico Bergenti - Universita` di Parma
 */
public class ReflectiveIntrospector implements Introspector {

    /**
     * Translate an object of a class representing an element in an
     * ontology into a proper abstract descriptor 
     * @param onto The ontology that uses this Introspector.
     * @param referenceOnto The reference ontology in the context of
     * this translation i.e. the most extended ontology that extends 
     * <code>onto</code> (directly or indirectly). 
     * @param obj The Object to be translated
     * @return The Abstract descriptor produced by the translation 
		 * @throws UnknownSchemaException If no schema for the object to be
		 * translated is defined in the ontology that uses this Introspector
		 * @throws OntologyException If some error occurs during the translation
     */
    public AbsObject externalise(Ontology onto, Ontology referenceOnto, Object obj) 
    				throws UnknownSchemaException, OntologyException {
        try {
            Class        javaClass = obj.getClass();            
            ObjectSchema schema = onto.getSchema(javaClass);
            if (schema == null) {
            	throw new UnknownSchemaException();
            }
            //DEBUG 
            //System.out.println("Schema is: "+schema);
            AbsObject    abs = schema.newInstance();
            
            Method[]     methods = javaClass.getMethods();
            String[]     names = schema.getNames();

            //FIXME: The correct way to do this would be to loop on
            // slot names and call related get methods.
            for (int i = 0; i < methods.length; i++) {
            	Method m = methods[i];
              String methodName = m.getName();

              if (methodName.startsWith("get")) {
                String attributeName = (methodName.substring(3, methodName.length())).toUpperCase();

                if (schema.containsSlot(attributeName)) {
            			//DEBUG 
                	//System.out.println("Handling attribute "+attributeName);
                  AbsObject attributeValue = invokeGetMethod(referenceOnto, m, obj);
            			//DEBUG 
                  //System.out.println("Attribute value is: "+attributeValue);

                  if (attributeValue != null) {
                  	Ontology.setAttribute(abs, attributeName, attributeValue);
                  } 
                } 
              } 
            } 

            return abs;
        } 
        catch (OntologyException oe) {
            throw oe;
        } 
        catch (Throwable t) {
            throw new OntologyException("Schema and Java class do not match", t);
        } 
    } 

    private AbsObject invokeGetMethod(Ontology onto, Method method, 
                                      Object obj) throws OntologyException {
        Object result = null;
        try {
            result = method.invoke(obj, null);

            if (result == null) {
                return null;
            } 

            return onto.fromObject(result);
        } 
        catch (OntologyException oe) {
        		// Forward the exception
            throw oe;
        } 
        catch (Exception e) {
            throw new OntologyException("Error invoking get method");
        } 
    } 

    /**
     * Translate an abstract descriptor into an object of a proper class 
     * representing an element in an ontology 
     * @param onto The ontology that uses this Introspector.
     * @param referenceOnto The reference ontology in the context of
     * this translation i.e. the most extended ontology that extends 
     * <code>onto</code> (directly or indirectly). 
     * @param abs The abstract descriptor to be translated
     * @return The Java object produced by the translation 
     * @throws UngroundedException If the abstract descriptor to be translated 
     * contains a variable
		 * @throws UnknownSchemaException If no schema for the abstract descriptor
		 * to be translated is defined in the ontology that uses this Introspector
     * @throws OntologyException If some error occurs during the translation
     */
    public Object internalise(Ontology onto, Ontology referenceOnto, AbsObject abs) 
            throws UngroundedException, UnknownSchemaException, OntologyException {

        try {
        		String type = abs.getTypeName();
        		//DEBUG System.out.println("Abs is "+abs);
        		// Retrieve the schema
            ObjectSchema schema = onto.getSchema(type, false);
            if (schema == null) {
            	throw new UnknownSchemaException();
            }
            //DEBUG System.out.println("Schema is: "+schema);
        		if (schema instanceof IRESchema || schema instanceof VariableSchema) {
        			throw new UngroundedException();
        		}
            
            Class        javaClass = onto.getClassForElement(type);
        		if (javaClass == null) {
        			throw new OntologyException("No java class associated to type "+type);
        		}
            //DEBUG System.out.println("Class is: "+javaClass.getName());
            
            Object       obj = javaClass.newInstance();
            //DEBUG System.out.println("Object created");
            
            Method[]     methods = javaClass.getMethods();
            String[]     names = schema.getNames();

            for (int i = 0; i < methods.length; i++) {
            	Method m = methods[i];
              String methodName = m.getName();

              if (methodName.startsWith("set")) {
                String attributeName = (methodName.substring(3, methodName.length())).toUpperCase();

                if (schema.containsSlot(attributeName)) {
            			//DEBUG System.out.println("Handling attribute "+attributeName);
                	AbsObject attributeValue = abs.getAbsObject(attributeName);
            			//DEBUG System.out.println("Attribute value is: "+attributeValue);

                  if (attributeValue != null) {
                  	invokeSetMethod(referenceOnto, m, obj, attributeValue);
                  } 
                } 
              } 
            } 

            return obj;
        } 
        catch (OntologyException oe) {
            throw oe;
        } 
        catch (Throwable t) {
            throw new OntologyException("Schema and Java class do not match", t);
        } 
    } 

    private void invokeSetMethod(Ontology onto, Method method, Object obj, 
                                 AbsObject value) throws OntologyException {
        try {
            Object objValue = onto.toObject(value);

            if (objValue == null) {
                return;
            } 

            Object[] params = new Object[] {
                objValue
            };
						
            try {
	            method.invoke(obj, params);
            }
        		catch (IllegalArgumentException iae) {
        			// Maybe the method required an int argument and we supplied 
        			// a Long. Similarly maybe the the method required a float and 
        			// we supplied a Double. Try these possibilities
        			if (objValue instanceof Long) {
        				Integer i = new Integer((int) ((Long) objValue).longValue());
        				params[0] = i;
        			}
        			//__CLDC_UNSUPPORTED__BEGIN
        			else if (objValue instanceof Double) {
        				Float f = new Float((float) ((Double) objValue).doubleValue());
        				params[0] = f;
        			}
        			//__CLDC_UNSUPPORTED__END
        			method.invoke(obj, params);
        		}
        } 
        catch (OntologyException oe) {
        		// Forward the exception
            throw oe;
        } 
        catch (Exception e) {
            throw new OntologyException("Error invoking set method");
        } 
    } 

    /**
       Check the structure of a java class associated to an ontological element 
       to ensure that translations to/from abstract descriptors and java objects
       (instances of that class) can be accomplished by this introspector.
       @param schema The schema of the ontological element
       @param javaClass The java class associated to the ontologcal element
       @throws OntologyException if the java class does not have the correct 
       structure
     */
    public void checkClass(ObjectSchema schema, Class javaClass) throws OntologyException {
    	// FIXME: Not yet implemented
    }
}

