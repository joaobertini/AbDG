/**
 * Created by IntelliJ IDEA.
 * User: Jo�o
 * Date: 23/09/2008
 * Time: 10:25:15
 * To change this template use File | Settings | File Templates.
 */
class TreeNode
   {
      // package access members
      TreeNode leftNode; // left node
      TreeNode[] linkNodes;
      int data; // node value
      int attribute;
      double threshold;
      double[] vetThreshold;
      double classe = -1;
      TreeNode rightNode; // right node


      // constructor initializes data and makes this a leaf node
      public TreeNode( int nodeData )
      {
         data = nodeData;
         leftNode = rightNode = null; // node has no children
      } // end TreeNode no-argument constructor

        public TreeNode( int attribute, double threshold )
      {
         this.attribute = attribute;
         this.threshold = threshold;
         leftNode = rightNode = null; // node has no children
      }

        public TreeNode(int attribute, double[] threshold )
      {
         this.attribute = attribute;
         this.vetThreshold = threshold;
         linkNodes = new TreeNode[threshold.length - 1];   // ponteiros para os intervalos
      }

       public TreeNode(double c){
          classe = c;
          leftNode = rightNode = null;
       }

      // locate insertion point and insert new node; ignore duplicate values
      public void insert( int insertValue )
      {
         // insert in left subtree
         if ( insertValue < data )
         {
            // insert new TreeNode
           if ( leftNode == null )
               leftNode = new TreeNode( insertValue );
            else // continue traversing left subtree
               leftNode.insert( insertValue );
         } // end if
         else if ( insertValue > data ) // insert in right subtree
         {
            // insert new TreeNode
            if ( rightNode == null )
               rightNode = new TreeNode( insertValue );
            else // continue traversing right subtree
               rightNode.insert( insertValue );
         } // end else if
      } // end method insert

       public void insertAtLeft(TreeNode node){
           leftNode = node;
       }

       public void insertAtRight(TreeNode node){
           rightNode = node;
       }

        public void insertAtPosition(TreeNode node, int pos){
           linkNodes[pos] = node;
       }


       public TreeNode findPath(double value){

        int atrLen = vetThreshold.length;
        int intervalo = -1;

        for(int i = 0; i < atrLen-1; i++)
           if(value >=  vetThreshold[i] && value <  vetThreshold[i+1])
           intervalo = i;

        if(intervalo == -1)
           if(value <   vetThreshold[0])
              intervalo = 0;
           else if (value >=  vetThreshold[atrLen-1])
                   intervalo = atrLen-2;

        return linkNodes[intervalo];


       }

       public void setClasse(double c){
           classe = c;
       }

       public double getThreshold(){
           return threshold;
       }

         public double[] getVetThreshold(){
           return vetThreshold;
       }

       public int getAttribute(){
           return attribute;
       }

       public double getClasse(){
           return classe;
       }

   } // end class TreeNode

   // class Tree definition
   public class Tree
   {
      private TreeNode root;

      // constructor initializes an empty Tree of integers
      public Tree()
      {
         root = null;
      } // end Tree no-argument constructor

       public Tree(TreeNode root){
         this.root = root;  
       }

      // insert a new node in the binary search tree
      public void insertNode( int insertValue )
      {
         if ( root == null )
            root = new TreeNode( insertValue ); // create the root node here
         else
            root.insert( insertValue ); // call the insert method
      } // end method insertNode

      // begin preorder traversal
      public void preorderTraversal()
      {
         preorderHelper( root );
      } // end method preorderTraversal

      // recursive method to perform preorder traversal
      private void preorderHelper( TreeNode node )
      {
         if ( node == null )
            return;

         System.out.print(" threshold " +  node.threshold  + " attribute " + node.attribute + " classe " +  node.classe); // output node data
         preorderHelper( node.leftNode );       // traverse left subtree
         preorderHelper( node.rightNode );      // traverse right subtree
      } // end method preorderHelper

      // begin inorder traversal
      public void inorderTraversal()
      {
         inorderHelper( root );
      } // end method inorderTraversal

      // recursive method to perform inorder traversal
      private void inorderHelper( TreeNode node )
      {
         if ( node == null )
            return;

         inorderHelper( node.leftNode );        // traverse left subtree
         System.out.printf( "%d ", node.data ); // output node data
         inorderHelper( node.rightNode );       // traverse right subtree
      } // end method inorderHelper

      // begin postorder traversal
      public void postorderTraversal()
      {
        postorderHelper( root );
     } // end method postorderTraversal

     // recursive method to perform postorder traversal
     private void postorderHelper( TreeNode node )
     {
        if ( node == null  )
           return;

        postorderHelper( node.leftNode );      // traverse left subtree
        postorderHelper( node.rightNode );     // traverse right subtree
        System.out.printf( "%d ", node.data ); // output node data
     } // end method postorderHelper


    // methods by Jo�o
       public void setRootProperties(int a, double t){
        root.attribute = a;
        root.threshold = t;
     }

       public TreeNode getRoot(){
           return root;
       }

       // para o C4.5
       public void setAcc(double W){
           acc = W;
       }

       public void updateClfWeight(double[] weights){

           double erroMBoost = 0;

           for(int i = 0; i < weights.length; i++)
               erroMBoost += weights[i] * labels[i];

           Weight = erroMBoost/(1-erroMBoost); //Math.log((1-erroMBoost)/erroMBoost);

       }

       public double getAcc(){
           return acc;
       }

       public double getWeight(){
           return Weight;
       }

       public void setLabels(double[] labels){
           this.labels = labels;
       }

       private double acc;
       double Weight = 1;
       double[] labels;

  } // end class Tree

