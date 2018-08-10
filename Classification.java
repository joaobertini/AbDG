

import java.io.*;
import java.io.IOException;


public class Classification
{

/*
    public static void main( String []args ) {

        IncrementalAlgorithms inca = new IncrementalAlgorithms();
        //  SEA sea = new SEA();

    }

*/
    public static void main( String []args ) {

         File file;
         FileReader stream;
         int numChRead;

         if ( args.length != 1 &&  args.length != 2 )  {
             System.out.println("Usage:\n   java Neural input ");
             System.out.println("input is the data file");
                  }
         else {
            file = new File(args[0]);
            if ( ! file.exists() || ! file.canRead() ) {
              System.out.println("Either the file " + args[0] + " does not exist or it cannot be read");
              createResult("0");
              return ;
            }
            try {
              stream = new FileReader(file);
             } catch ( FileNotFoundException e ) {
                 System.out.println("Something wrong: file does not exist anymore");
                 createResult("0");
                 throw new RuntimeException();
             }

             char []input = new char[ (int ) file.length() + 1 ];

             try {
               numChRead = stream.read( input, 0, (int ) file.length() );
             } catch ( IOException e ) {
                 System.out.println("Error reading file " + args[0]);
                 createResult("0");
                 return ;
             }

             if ( numChRead != file.length() ) {
                 System.out.println("Read error");
                 createResult("0");
                 return ;
             }
             try {
               stream.close();
             } catch ( IOException e ) {
                 createResult("0");
                 System.out.println("Error in handling the file " + args[0]);
                 return ;
             }


            String outputFileName;

         //     DecisionNetwork net = new DecisionNetwork(input);


         //   DecisionNetwork net = new DecisionNetwork(input, args[0]);

        //      DecisionNetwork net = new DecisionNetwork();
           // ID3Like tree = new ID3Like(input);
          //  OutrosMetodosImputacao others = new OutrosMetodosImputacao(input, args[0]);

         //  IncrementalAlgorithms inca = new IncrementalAlgorithms();
          // SEA sea = new SEA();

           IncrementalAlgorithms inca = new IncrementalAlgorithms(input, args[0]);

         }
     }

//*/

        // #################################### main 2 arquivos #########################
 /*

   public static void main( String []args ) {

        File file1;
        File file2;

        FileReader stream1;
        FileReader stream2;

        int numChRead1;
        int numChRead2;



        if ( (args.length != 1 && args.length != 2) ){
            System.out.println("Usage:\n   java Quimica input(s) ");
            System.out.println("input is the data file");
                 }
        else {
           file1 = new File(args[0]);
           file2 = new File(args[1]);

           if ( (! file1.exists() || ! file1.canRead()) && (! file2.exists() || ! file2.canRead()) ){
             System.out.println("Either the file " + args[0] + " does not exist or it cannot be read");
             System.out.println("Or the file "+ args[1] + " does not exist or it cannot be read");
             createResult("0");
             return;
           }

            // arquivo 1
           try {
             stream1 = new FileReader(file1);
            } catch ( FileNotFoundException e ) {
                System.out.println("Something wrong: file does not exist anymore");
                createResult("0");
                throw new RuntimeException();
            }

            char []input1 = new char[ (int ) file1.length() + 1 ];

            try {
              numChRead1 = stream1.read( input1, 0, (int ) file1.length() );
            } catch ( IOException e ) {
                System.out.println("Error reading file " + args[0]);
                createResult("0");
                return ;
            }

            if ( numChRead1 != file1.length() ) {
                System.out.println("Read error");
                createResult("0");
                return ;
            }
            try {
              stream1.close();
            } catch ( IOException e ) {
                createResult("0");
                System.out.println("Error in handling the file " + args[0]);
                return ;
            }

            // arquivo 2

             try {
             stream2 = new FileReader(file2);
            } catch ( FileNotFoundException e ) {
                System.out.println("Something wrong: file does not exist anymore");
                createResult("0");
                throw new RuntimeException();
            }

            char []input2 = new char[ (int ) file2.length() + 1 ];

            try {
              numChRead2 = stream2.read( input2, 0, (int ) file2.length() );
            } catch ( IOException e ) {
                System.out.println("Error reading file " + args[1]);
                createResult("0");
                return ;
            }

            if ( numChRead2 != file2.length() ) {
                System.out.println("Read error");
                createResult("0");
                return ;
            }
            try {
              stream2.close();
            } catch ( IOException e ) {
                createResult("0");
                System.out.println("Error in handling the file " + args[1]);
                return ;
            }


                DecisionNetwork net = new DecisionNetwork(input1,input2);

             //   DecisionTree net = new DecisionTree(input1,input2);
            
      }
   }

   //

  */


    public static boolean createResult(String contents) {
       PrintWriter out;
       FileOutputStream outputStream = null;
       try {
          outputStream = new FileOutputStream ("result.txt");
           } catch ( java.io.IOException e) {
          System.out.println("Could not create result.txt");
          createResult("0");
          return false;
       }
       out = new PrintWriter(outputStream);

       out.println(contents);
       out.close();
       return true;
    }

 }




