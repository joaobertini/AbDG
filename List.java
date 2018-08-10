/**
 * Created by IntelliJ IDEA.
 * User: João
 * Date: 09/05/2007
 * Time: 08:53:13
 * To change this template use File | Settings | File Templates.
 */
// Fig. 17.3: List.java
   // ListNode and List class definitions.
//   package com.deitel.jhtp6.ch17;

   // class to represent one node in a list
   class ListNode<T>
    {
      // package access members; List can access these directly
      T data;
      ListNode nextNode;

        // constructor creates a ListNode that refers to object
         ListNode( T object )
         {
            this ( object, null);
         } // end ListNode one-argument constructor

        // constructor creates ListNode that refers to
        // Object and to next ListNode
         ListNode( T object, ListNode node )
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
         ListNode getNext()
         {
            return nextNode; // get next node
         } // end method getNext
      } // end class ListNode

      // class List definition
      public class List <T>
      {
         private ListNode firstNode;
         private ListNode lastNode;
         private String name; // string like "list" used in printing
         private int vertice;

         // constructor creates empty List with "list" as the name
         public List()
         {
            this( "list" );
         } // end List no-argument constructor

         // constructor creates an empty List with a name
         public List( String listName )
         {
             name = listName;
             firstNode = lastNode = null;
         } // end List one-argument constructor

          // cosntructor by Joao
           public List(int vertice )
         {
             this.vertice = vertice;
             firstNode = lastNode = null;
         }

         // insert Object at front of List
         public void insertAtFront( T insertItem )
         {
            if ( isEmpty() ) // firstNode and lastNode refer to same object
               firstNode = lastNode = new ListNode( insertItem );
            else // firstNode refers to new node
               firstNode = new ListNode( insertItem, firstNode );
         } // end method insertAtFront

         // insert Object at end of List
         public void insertAtBack( T insertItem )
         {
            if ( isEmpty() ) // firstNode and lastNode refer to same Object
               firstNode = lastNode = new ListNode( insertItem );
            else  // lastNode's nextNode refers to new node
                lastNode = lastNode.nextNode = new ListNode( insertItem );
         } // end method insertAtBack

           public void insertAtPosition( T insertItem, int posicao)
         {
            int cont = 1;
            ListNode leftNode = firstNode;
            ListNode rightNode;

            if ( listLength() == 1 ){
               if(posicao > 1)
                   insertAtBack(insertItem);
                else
                   insertAtFront(insertItem);
            }
           else{  // lastNode's nextNode refers to new node
                ListNode newNode = new ListNode(insertItem);
                while(cont < posicao-1){
                   leftNode = leftNode.nextNode;
                   cont++;
                }

                rightNode = leftNode.nextNode;
                leftNode.nextNode = newNode;
                newNode.nextNode = rightNode;

            }
         } // end method insertAtBack

           // remove first node from List
         public T returnElement(int element) throws EmptyListException
         {
            ListNode auxNode = firstNode;
             if ( isEmpty() ) // throw exception if List is empty
               throw new EmptyListException( name );

            int cont = 1;

               while(cont < element){
                 auxNode = auxNode.nextNode;
                 cont++;
               }

            return (T)auxNode.data; // return removed node data
         } // end method removeFromFront

         // remove first node from List
         public T removeFromFront() throws EmptyListException
         {
            if ( isEmpty() ) // throw exception if List is empty
               throw new EmptyListException( name );

            T removedItem = (T)firstNode.data; // retrieve data being removed

            // update references firstNode and lastNode
            if ( firstNode == lastNode )
               firstNode = lastNode = null;
            else
               firstNode = firstNode.nextNode;

            return removedItem; // return removed node data
         } // end method removeFromFront

         // remove last node from List
         public T removeFromBack() throws EmptyListException
         {
            if ( isEmpty() ) // throw exception if List is empty
               throw new EmptyListException( name );

           T removedItem = (T)lastNode.data; // retrieve data being removed

           // update references firstNode and lastNode
           if ( firstNode == lastNode )
              firstNode = lastNode = null;
           else // locate new last node
           {
              ListNode current = firstNode;

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
           ListNode current = firstNode;

           // while not at end of list, output current node's data
           while ( current != null )
           {
              System.out.printf( "%s ", current.data );
              current = current.nextNode;
           } // end while

           System.out.println( "\n" );
        } // end method print


          // method by João
          public boolean search(T toFind)
        {
           boolean find = false;
           if ( isEmpty() )
           {
              //System.out.printf( "Empty %s\n", name );
              return false;
           } // end if

           //System.out.printf( "The %s is: ", name );
           ListNode current = firstNode;

           // while not at end of list, output current node's data
           while ( current != null )
           {
              if(current.data == toFind)
                  find = true;
               //System.out.printf( "%s ", current.data );
              current = current.nextNode;
           } // end while

          return find;
        } // end method search


          public int listLength(){

              int nnodes = 0;
           if ( isEmpty() )
           {
              //System.out.printf( "Empty %s\n", name );
              return 0;
           } // end if

           ListNode current = firstNode;

           // while not at end of list, output current node's data
           while ( current != null )
           {
               nnodes++;
               current = current.nextNode;
           } // end while

          return nnodes;

       }

          public T getNodeData(int node){

              int aux = 0;
                                // primeiro no é o zero
            if ( isEmpty() )
           {
              System.out.printf( "Empty %s\n", name );
              return null;
           } // end if

          ListNode current = firstNode;

           // while not at end of list, output current node's data
           while ( aux < node)
           {
              current = current.nextNode;
              aux++;
           } // end while


          return (T)current.data;

      } // end method search


// remove last node from List
   public void emptyList() {

     firstNode = lastNode = null;

   } // end method removeFromBack



   } // end class List

