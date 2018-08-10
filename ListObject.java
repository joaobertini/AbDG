/**
 * Created by IntelliJ IDEA.
 * User: João
 * Date: 12/03/2008
 * Time: 10:03:05
 * To change this template use File | Settings | File Templates.
 */
// Fig. 17.3: List.java
   // ListNode and List class definitions.
  // package com.deitel.jhtp6.ch17;

   // class to represent one node in a list
   class ListNodeObj
   {
      // package access members; List can access these directly
      Object data;
      ListNodeObj nextNode;

         // constructor creates a ListNode that refers to object
         ListNodeObj( Object object )
        {
            this ( object, null );
         } // end ListNode one-argument constructor

         // constructor creates ListNode that refers to
         // Object and to next ListNode
         ListNodeObj( Object object, ListNodeObj node )
         {
            data = object;
            nextNode = node;
         } // end ListNode two-argument constructor

         // return reference to data in node
         Object getObject()
         {
            return data; // return Object in this node
         } // end method getObject

         // return reference to next node in list
         ListNodeObj getNext()
         {
            return nextNode; // get next node
         } // end method getNext
      } // end class ListNode

      // class List definition
      public class ListObject
      {
         private ListNodeObj firstNode;
         private ListNodeObj lastNode;
         private String name; // string like "list" used in printing

         // constructor creates empty List with "list" as the name
         public ListObject()
         {
            this( "list" );
         } // end List no-argument constructor

         // constructor creates an empty List with a name
         public ListObject( String listName )
         {
            name = listName;
            firstNode = lastNode = null;
         } // end List one-argument constructor

         // insert Object at front of List
         public void insertAtFront( Object insertItem )
         {
            if ( isEmpty() ) // firstNode and lastNode refer to same object
               firstNode = lastNode = new ListNodeObj( insertItem );
            else // firstNode refers to new node
               firstNode = new ListNodeObj( insertItem, firstNode );
         } // end method insertAtFront

         // insert Object at end of List
         public void insertAtBack( Object insertItem )
         {
            if ( isEmpty() ) // firstNode and lastNode refer to same Object
               firstNode = lastNode = new ListNodeObj( insertItem );
            else // lastNode's nextNode refers to new node
               lastNode = lastNode.nextNode = new ListNodeObj( insertItem );
         } // end method insertAtBack

         // remove first node from List
         public Object removeFromFront() throws EmptyListException
         {
            if ( isEmpty() ) // throw exception if List is empty
               throw new EmptyListException( name );

            Object removedItem = firstNode.data; // retrieve data being removed

            // update references firstNode and lastNode
            if ( firstNode == lastNode )
               firstNode = lastNode = null;
            else
               firstNode = firstNode.nextNode;

            return removedItem; // return removed node data
         } // end method removeFromFront

         // remove last node from List
         public Object removeFromBack() throws EmptyListException
         {
            if ( isEmpty() ) // throw exception if List is empty
               throw new EmptyListException( name );

           Object removedItem = lastNode.data; // retrieve data being removed

           // update references firstNode and lastNode
           if ( firstNode == lastNode )
              firstNode = lastNode = null;
           else // locate new last node
           {
              ListNodeObj current = firstNode;

              // loop while current node does not refer to lastNode
              while ( current.nextNode != lastNode )
                 current = current.nextNode;

              lastNode = current; // current is new lastNode
              current.nextNode = null;
           } // end else

           return removedItem; // return removed node data
        } // end method removeFromBack

        // determine whether list is empty
        public boolean isEmpty()
        {
           return firstNode == null; // return true if List is empty
        } // end method isEmpty

        // output List contents
        public void print()
        {
           if ( isEmpty() )
           {
              System.out.printf( "Empty %s\n", name );
              return;
           } // end if

           System.out.printf( "The %s is: ", name );
           ListNodeObj current = firstNode;

           // while not at end of list, output current node's data
           while ( current != null )
           {
              System.out.printf( "%s ", current.data );
              current = current.nextNode;
           } // end while

           System.out.println( "\n" );
        } // end method print
     } // end class List

