/**
 * Created by IntelliJ IDEA.
 * User: João
 * Date: 28/02/2008
 * Time: 17:13:41
 * To change this template use File | Settings | File Templates.
 */
public class Queue {
     private ListObject queueList;

     // no-argument constructor
     public Queue()
     {
        queueList = new ListObject( "queue" );
     } // end Queue no-argument constructor

     // add object to queue
     public void enqueue( Object object )
     {
        queueList.insertAtBack( object  );
     } // end method enqueue

     // remove object from queue
     public Object dequeue() throws EmptyListException
     {
        return queueList.removeFromFront();
     } // end method dequeue

     // determine if queue is empty
     public boolean isEmpty()
     {
        return queueList.isEmpty();
     } // end method isEmpty

     // output queue contents
     public void print()
     {
        queueList.print();
     } // end method print

    // methods by João
   /*
    public int next(){
       return queueList.getLastNodeData();
    }

   public int queueLenght(){
      return queueList.listLength();
   }

  public int getQueueElement(int index){
    return queueList.getNodeData(index);  
  }
 */


  } // end class Queue
