/**
 * Created by IntelliJ IDEA.
 * User: João
 * Date: 09/05/2007
 * Time: 08:57:01
 * To change this template use File | Settings | File Templates.
 */
// Class EmptyListException definition.
//  package com.deitel.jhtp6.ch17;

  public class EmptyListException extends RuntimeException
  {
     // no-argument constructor
     public EmptyListException()
     {
           this( "List" ); // call other EmptyListException constructor
        } // end EmptyListException no-argument constructor

        // one-argument constructor
        public EmptyListException( String name )
        {
           super( name + " is empty" ); // call superclass constructor
        } // end EmptyListException one-argument constructor
     } // end class EmptyListException

