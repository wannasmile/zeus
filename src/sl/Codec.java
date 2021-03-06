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


import java.util.List;

/**
   Interface for Content Language encoders/decoders. This interface
   declares two methods that must convert between text and frame
   representations according to a specific content language.

  @author Giovanni Rimassa - Universita` di Parma
  @version $Date: 2003/10/09 12:55:36 $ $Revision: 1.1.1.1 $
 */
public interface Codec {


  /**
     Encodes a t-uple of <code>Frame</code> objects into a Java
     <code>String</code>, according to this Content Language and Ontology by
     looking up the given ontology for the role played by symbols
     (i.e. whether they are concepts, actions or predicates).
     @param f The List of frames to encode as a t-uple.
     @param o The ontology to use to lookup the roles for the various
     symbols.
     @return A Java string, representing the given frame according to
     this content language.
   */
  String encode(List v, Ontology o);

  /**
     Decodes a given <code>String</code>, according to the given
     Content Language and obtains a t-uple of <code>Frame</code> objects.
     This method can use the given ontology to distinguish among the different
     kinds of roles a symbol can play (i.e. Concept vs. Action
     vs. Predicate).
     @param s A string containing the representation of an ontological
     element, encoded according to this content language.
     @param o The ontology to use to lookup the roles for the various
     symbols.
     @return A List of frame, representing the given ontological elements.
  */
  List decode(String s, Ontology o) throws CodecException;

}
