import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: João
 * Date: 19/03/2015
 * Time: 09:10:03
 * To change this template use File | Settings | File Templates.
 */
public class OutrosMetodosImputacao {
    // Classe reune métodos de imputação simples para comparação com proposta de imputação do AbDG
    // referencias em: 'On the choice of the best imputation methods for missing
    //   values considering three groups of classification methods'


     public OutrosMetodosImputacao(char[] inputFile, String filename){  // deals Heterogeneous Attributes


        int  tokenPos = 0;
        int  tkPos = 0 ;
        int  tkPos1 = 0;
        int  lineNumber = 1;        // conta numero de linhas
        int  collNumber = 0;
        char ch,ln,cl;

        // calcula o numero de linhas.
        while (  (ln = inputFile[tkPos]) != '\0') {
            if( ln == '\n'){
                lineNumber++;
            }
            tkPos++;
        }
        // calcula o numero de colunas
        while((cl = inputFile[tkPos1]) != '\n') {  // adicionado para comecar com -
            while(Character.isLetter(inputFile[tkPos1])){
                 collNumber++;
                 tkPos1++;
                }
               tkPos1++;

       }
              //aloca matriz de dados
        double matriz[][];
        matriz = new double[lineNumber-1][collNumber];


        int i = 0;
        int j = 0;
        int k = 0;
        StringBuffer number = new StringBuffer();
        attributeType = new char[collNumber];
        double[] runningAvg = new double[collNumber];      // para calcular media acumulativa - usada para atribuir valores a ? numericos
        tkPos1 = 0;

           while((cl = inputFile[tkPos1]) != '\n') {
               while(Character.isLetter(inputFile[tkPos1])){
                 collNumber++;
                 attributeType[k] = inputFile[tkPos1];
                 tkPos1++;
                 k++;
                }
               tkPos1++;
          }

        // constroi matriz de dados
         tokenPos = tkPos1 + 1;

        while (  (ch = inputFile[tokenPos]) != '\0') {

            while((ch = inputFile[tokenPos]) != '\n' ) {
                if(Character.isDigit( inputFile[tokenPos]) || (ch = inputFile[tokenPos]) == '-' || (ch = inputFile[tokenPos]) == '?' ){  // primeira coluna
                    while ( Character.isDigit( inputFile[tokenPos])||(ch = inputFile[tokenPos]) == '.'||
                            (ch = inputFile[tokenPos]) == '-') {
                        number.append(inputFile[tokenPos]);
                        tokenPos++;
                    }
                    if((inputFile[tokenPos]) == '?'){
                   //     if(attributeType[j] == 'c'){
                           matriz[i][j] = emptyValue;  // valor diferente para atr categorico
                  //      }                                                                                        ####### Running average
                 //       else{
                   //         if(i > 1)
                    //           matriz[i][j] = runningAvg[j]/(double)(i-1);
                 //          else
                 //             matriz[i][j] = 0;
                   //     }

                       j++;
                       tokenPos++;
                    }
                    else{
                       matriz[i][j] = Double.valueOf(number.toString()).doubleValue();
                       runningAvg[j] += matriz[i][j];
                       j++;
                       number.delete(0, number.length());
                    }
                }
                else
                    tokenPos++;

                if ((ch = inputFile[tokenPos]) == '\0'){
                    tokenPos--;
                    break;
                }
            }
            tokenPos++;
            i++;
            j = 0;

        }


        int line = matriz.length;     // numero de elementos
        int coll = matriz[0].length;  // numero de atrbutos
        numAtr = coll;
        double[] tempClass = new double[line];


        int cont = 0;
        boolean newClass = true;


        // descobre quantas classes possui E
        for(int p = 0; p < line; p++){
            newClass = true;
            for(int q = 0; q < cont; q++)
                if(matriz[p][coll - 1] == tempClass[q])
                    newClass = false;

            if(newClass){
                tempClass[cont] = matriz[p][coll - 1];
                cont++;
            }
        }

        Classes = new double[cont]; //declara vetor de classes
        // em Classes estao as classes originais
        for(int c = 0; c < cont; c++)
            Classes[c] = tempClass[c];


        nroClasses = cont;
        double[][] oneClassTrain;

        if(nroClasses == 2){
            for(int n = 0; n < line; n++)
                if(matriz[n][coll-1] == -1 || matriz[n][coll-1] == 0)
                    matriz[n][coll-1] = 2;

            if(Classes[0] == -1 || Classes[0] == 0)
                Classes[0] = 2;

            if(Classes[1] == -1 || Classes[1] == 0)
                Classes[1] = 2;
        }

        int classe1 = 0, classe2 = 0;

        for(int r = 0; r < line; r++)
           if(matriz[r][coll-1] == 1)
               classe1++;
           else
               classe2++;

      //   System.out.println("1 " + classe1 + " 2 " + classe2);


        int noTXT = filename.length() - 4;
	    FileName = filename.substring(0, noTXT);



        // imputação    ##########################################

      // fileProbMissingTreinoeTeste(matriz);     //  cria treino e teste refinando ambos

         FigureMissingTreinoAbDG(matriz,"imputed");


    }

    public void fileProbMissingTreinoeTeste(double[][] matriz){   // escreve dataset com NA para atributo faltante

          int line = matriz.length;
          int coll = matriz[0].length;
         // int noTXT = fileName.length() - 4;
        //  String auxFileName = fileName.substring(0, noTXT);
           double[] probMiss = {0.05, 0.1, 0.2, 0.3, 0.4, 0.5};
           int fold = 10, cont = 1;
           int contTreino = 0, contTeste = 0, it = 1;
           double[][] matrizTreino;
           double[][] matrizTeste;



           for(int d = 0; d < probMiss.length; d++){

              cont = 101;
              for(int f = 1; f <= 8; f++) {        //  numero de cross-validations


                 it = 1;
                 matriz = shuffle(matriz);
                 int[] stratification = indicesEstratificados(matriz,fold);


                while(it <= fold){


                    coll = matriz[0].length;
                    contTreino = 0;
                    contTeste = 0;
                    for(int i = 0; i < line; i++)
                       if(stratification[i] == it)
                          contTeste++;

                    matrizTreino = new double[line - contTeste][coll];
                    matrizTeste = new double[contTeste][coll];

                 contTeste = 0;
                 contTreino = 0;
                 for(int h = 0; h < line; h++)
                     if(stratification[h] != it){
                        for(int y = 0; y < coll; y++ )
                           matrizTreino[contTreino][y] = matriz[h][y];  //  matrizTreino[contTreino][y] = matriz[h][y];
                        contTreino++;
                      }
                     else{
                        for(int y = 0; y < coll; y++ )
                           matrizTeste[contTeste][y] = matriz[h][y];
                         contTeste++;
                        }


                matrizTeste = normalizacaoMaiorMenor(matrizTeste);


                                  //TESTE

                PrintWriter out1;
                FileOutputStream outputStream1 = null;
                try{
                    outputStream1 = new FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\simpleImputation\\MEAN\\" + FileName + "\\" + (int)(probMiss[d]*100) + "\\" +  cont +  "TE.txt");
                } catch (java.io.IOException e){
                    System.out.println("Could not create result.txt");
                }

                out1 = new PrintWriter(outputStream1);


                for(int i = 0; i < matrizTeste.length; i++){
                    for(int j = 0; j < coll; j++)
                        if(j < coll-1)
                           out1.print(matrizTeste[i][j] + " ");
                        else {
                            if(i < matrizTeste.length - 1)
                               out1.println(matrizTeste[i][j]);
                            else
                               out1.print(matrizTeste[i][j]);
                         }

                }

                out1.close();


                             //TREINO


                    for (int i = 0; i < matrizTreino.length; i++) {
                        for (int j = 0; j < coll - 1; j++) {
                            if (probMiss[d] > Math.random())
                                matrizTreino[i][j] = emptyValue;

                          }

                        }


                    matrizTreino = normalizacaoMaiorMenorMissing(matrizTreino);

                    matrizTreino = CMC(matrizTreino);


                  PrintWriter out;
                  FileOutputStream outputStream = null;
                  try {
                      outputStream = new FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\simpleImputation\\MEAN\\" + FileName + "\\" + (int)(probMiss[d]*100) + "\\"+ cont + ".txt");
                      //outputStream = new FileOutputStream("C:\\Experimentos\\Raw\\" + auxFileName + "\\" + (int) (probMiss[d] * 100) + "\\" + f + ".txt");
                  } catch (java.io.IOException e) {
                      System.out.println("Could not create result.txt");
                  }

                  out = new PrintWriter(outputStream);


                  for (int i = 0; i < matrizTreino.length; i++) {
                      for (int j = 0; j < coll; j++)
                         if(j < coll-1)
                           out.print(matrizTreino[i][j] + " ");
                        else {
                            if(i < matrizTreino.length - 1)
                               out.println(matrizTreino[i][j]);
                            else
                               out.print(matrizTreino[i][j]);
                         }

                  }

                  out.close();

                  it++;
                  cont++;
              }
              }

//   System.out.println();                
           }


          }
    

    public void criaTreinoeTesteImputation(double[][] matriz){

        int line = matriz.length;
        int coll = matriz[0].length;
        double[][] matrizTreino;
        double[][] matrizTeste;
        double[][] matrizToImput = new double[line][coll];
        int fold = 8;
        int contTreino = 0, contTeste = 0, it = 1;
        double rand;
        probMiss = 0.3; // ########################################################
        char[] vetChar = {'a', 'b', 'c', 'd', 'r'};

        DecimalFormat show = new DecimalFormat("0.00");

        for(int ncv = 0; ncv < 2; ncv++) {


            for(int i = 0; i < line; i++){
                for(int j = 0; j < coll-1; j++){
                    rand = Math.random();
                    if(rand < probMiss){                                 // atributo escolhido para ter valor apagado
                        matrizToImput[i][j] = emptyValue;
                    }
                    else
                        matrizToImput[i][j] = matriz[i][j];

                    matrizToImput[i][coll-1] = matriz[i][coll-1];
                }

            }


            it = 1;
            int[] stratification = indicesEstratificados(matriz,fold);


            while(it <= fold){              // separa matriz em partições

                coll = matriz[0].length;
                contTreino = 0;
                contTeste = 0;
                for(int i = 0; i < line; i++)
                    if(stratification[i] == it)
                        contTeste++;

                matrizTreino = new double[line - contTeste][coll];
                matrizTeste = new double[contTeste][coll];

                contTeste = 0;
                contTreino = 0;
                for(int h = 0; h < line; h++)
                    if(stratification[h] != it){
                        for(int y = 0; y < coll; y++ )
                            matrizTreino[contTreino][y] = matrizToImput[h][y]; 
                        contTreino++;
                    }
                    else{
                        for(int y = 0; y < coll; y++ )
                            matrizTeste[contTeste][y] = matrizToImput[h][y];
                        contTeste++;
                    }


                // chama métodos para imputação nas partições criadas
                // imputação será diferente para treino e teste

                Sample(matrizTreino, matrizTeste, vetChar[ncv], it);









                it++;

            }  // cross-valid

        }  // num de cross-valid
    }


    public void Sample(double[][] matrizTreino, double[][] matrizTeste, char cv, int particao){
       // atribui valor aleatório no intervalo do atributo faltante


        int line = matrizTreino.length;
        int coll = matrizTreino[0].length;
        double[] vetMaior = new double[coll];
        double[] vetMenor = new double[coll];
        double maior, menor, rand;
        int lineTe = matrizTeste.length;
        double[][] matrizTreinoImputed = new double[line][coll];
        double[][] matrizTesteImputed = new double[lineTe][coll];



        for(int j = 0; j < coll; j++){        //  tratar empty value

        maior = -20000;
        menor = 20000;

       for(int i = 0; i < line; i++)  {
          if(matrizTreino[i][j] != emptyValue) {
           if(matrizTreino[i][j] > maior)
             maior = matrizTreino[i][j];

           if(matrizTreino[i][j] < menor)
             menor = matrizTreino[i][j];
          }
        }

        vetMaior[j] = maior;
        vetMenor[j] = menor;
    }

                                                    // treino
        for(int i = 0; i < line; i++)
           for(int j = 0; j < coll; j++){
              if(matrizTreino[i][j] == emptyValue){
                  rand = Math.random();
                  matrizTreinoImputed[i][j] = (rand*(vetMaior[j] - vetMenor[j]) + vetMenor[j]);
               }
               else
                  matrizTreinoImputed[i][j] = matrizTreino[i][j];
           }



         for(int i = 0; i < lineTe; i++)          // teste
           for(int j = 0; j < coll; j++){
              if(matrizTeste[i][j] == emptyValue){
                  rand = Math.random();
                  matrizTesteImputed[i][j] = (rand*(vetMaior[j] - vetMenor[j]) + vetMenor[j]);
               }
               else
                  matrizTesteImputed[i][j] = matrizTeste[i][j];
           }



                                 // TREINO
         PrintWriter out;
         FileOutputStream outputStream = null;
         try {
            outputStream = new FileOutputStream ("C:\\Users\\João\\Documents\\MATLAB\\simpleImputation\\Sample\\" + FileName + "\\" +  (int)(probMiss*100) + "\\" + cv + particao + ".txt");
             } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");
        }

         out = new PrintWriter(outputStream);

           for(int i = 0; i < line; i++){
             for(int j = 0; j < coll; j++)
                   out.print(matrizTreinoImputed[i][j] + " ");          // deve usar pois java sempre passa por referencia

            out.println();
           }
         out.close();
     

                                  // TESTE
         PrintWriter out1;
         FileOutputStream outputStream1 = null;
         try {
            outputStream1 = new FileOutputStream ("C:\\Users\\João\\Documents\\MATLAB\\simpleImputation\\Sample\\" + FileName + "\\" +
                    (int)(probMiss*100) + "\\" + cv + particao + "Te.txt");
             } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");
        }

         out1 = new PrintWriter(outputStream1);

           for(int i = 0; i < lineTe; i++){
             for(int j = 0; j < coll; j++)
                   out1.print(matrizTesteImputed[i][j] + " ");          // deve usar pois java sempre passa por referencia

            out1.println();
           }
         out1.close();



      }


     public double[][] CMC(double[][] matrizTreino){
       // atribui média aos atributos faltantes / usa informação da classe no conjunto de treino 


         int line = matrizTreino.length;
         int coll = matrizTreino[0].length;
         double somaTR, soma = 0, rand, cont = 0, media;
         double[][] matrizTreinoImputed = new double[line][coll];

         for(int c = 0; c < nroClasses; c++){ // treino

             for(int j = 0; j < coll; j++){
                 soma = 0;
                 cont = 0;
                 for(int i = 0; i < line; i++) {
                     if(matrizTreino[i][j] != emptyValue && matrizTreino[i][coll - 1] == Classes[c]) {
                         soma += matrizTreino[i][j];
                         cont++;
                     }
                 }

                 media = soma/cont;

                 for(int i = 0; i < line; i++)
                     if(matrizTreino[i][j] == emptyValue && matrizTreino[i][coll - 1] == Classes[c]){
                         matrizTreino[i][j] = media;
                     }
                     else
                         matrizTreino[i][j] = matrizTreino[i][j];

             }

       //   System.out.println();
         }

            return matrizTreino;

      }


    public void FigureMissingTreinoAbDG(double[][] matriz, String filename){   // AbDG

                  int line = matriz.length;
                  int coll = matriz[0].length;
               //   int noTXT = filename.length() - 4;
               //   String FileName = filename.substring(0, noTXT);
                //   double[] probMiss = {0.05, 0.1, 0.2, 0.3, 0.4, 0.5};
                   int fold = 10, cont = 1;
                   int contTreino = 0, contTeste = 0, it = 1;
                   double[][] matrizTreino;
                   double[][] matrizTeste;



              //     for(int d = 0; d < probMiss.length; d++){

                   //   cont = 101;
              //        for(int f = 1; f <= 8; f++) {        //  numero de cross-validations


                   //      it = 1;
                         matriz = shuffle(matriz);
                      //   int[] stratification = indicesEstratificados(matriz,fold);


              //          while(it <= fold){


                            coll = matriz[0].length;



                                          //  Cada instancia tem prob de ter um de seus atributos faltando.

                            for (int i = 0; i < matriz.length; i++) {
                                   if (Math.random() < 0.8)
                                      if(Math.random() < 0.5)
                                           matriz[i][0] = emptyValue;
                                      else
                                           matriz[i][1] = emptyValue;

                                }


                       /*      for (int i = 0; i < matriz.length; i++) {
                                      if (Math.random() < 0.8)
                                        for(int j = 0; j < coll- 1; j++)
                                            if(Math.random() < 0.38)
                                                 matriz[i][j] = emptyValue;
                             }
                     */


                       //     matriz = normalizacaoMaiorMenorMissing(matriz);

                        // AbDG
                            matriz = CMC(matriz);

                          PrintWriter out;
                          FileOutputStream outputStream = null;
                          try {
                              outputStream = new FileOutputStream("C:\\Java\\Datasets\\tests\\" + filename + ".txt");
                              //outputStream = new FileOutputStream("C:\\Experimentos\\Raw\\" + auxFileName + "\\" + (int) (probMiss[d] * 100) + "\\" + f + ".txt");
                          } catch (java.io.IOException e) {
                              System.out.println("Could not create result.txt");
                          }

                          out = new PrintWriter(outputStream);


                          for (int i = 0; i < matriz.length; i++) {
                              for (int j = 0; j < coll; j++)
                                 if(j < coll-1)
                                   out.print(matriz[i][j] + " ");
                                else {
                                    if(i < matriz.length - 1)
                                       out.println(matriz[i][j]);
                                    else
                                       out.print(matriz[i][j]);
                                 }

                          }

                          out.close();

                          it++;
                          cont++;
            //          }
                //      }

//   System.out.println();
               //    }


                  }



    // ###### Métodos auxiliares #####

    public int[] indicesEstratificados(double[][] matriz, int folds){

           int line = matriz.length;
           int coll = matriz[0].length;
           int[] indices = new int[line];
           int[] classes = new int[nroClasses+1]; // +1 pois não existe classe 0
           double[] proportions = new double[nroClasses+1];
           int foldLen = line/folds;
           int restFold = line % folds;    // restantes
           int aux = 0, cont = 0, nextInd;
           int contFold = 0;

           for(int i = 0; i < line; i++){
              indices[i] = 0;               // zera vetor de indices
              classes[(int)matriz[i][coll-1]]++;    // conta numero de el. por classe
           }

           for(int j = 1; j <= nroClasses; j++)
              proportions[j] = (double)classes[j]/(double)line;   // calcula proporção de instancias no conjunto total

           for(int i = 1; i <= folds; i++){
               contFold = 0;
               for(int j = 1; j <= nroClasses; j++){
                   aux = (int)Math.floor(proportions[j]*foldLen);
                   cont = 0;
                   for(int l = 0; l < line && cont < aux; l++)
                         if(indices[l] == 0 && matriz[l][coll-1] == (double)j){
                             indices[l] = i;
                             cont++;
                             contFold++;
                         }

               }


           //   System.out.println("Fold 1 " + contFold);
           }

           nextInd = 1;
           for(int i = 0; i < line; i++){
               if(indices[i] == 0){
                  indices[i] = nextInd;
                  nextInd++;
                  if(nextInd > folds)
                     nextInd = 1;
               }

           }


         int[] contV = new int[folds+1];
           for(int j = 0; j < line; j++)                   // imprime para verificação
               contV[indices[j]]++;

          // for(int kk = 1; kk <= folds; kk++)
          //       System.out.println( kk + " " + contV[kk]);

       return indices;

       }

    public double[][] shuffle(double[][] E){

          int line = E.length;
          int coll = E[0].length;
          int aux = 0, cont = 0, aux1 = 0;
          double pChange = 0.9, prob = 0;
          Random randomNumbers = new Random();

          int[] mask = new int[line];

          for(int h = 0; h < line; h++)
              mask[h] = h;

          for(int i = 0; i < line; i++){
              prob = Math.random();
              if(prob < pChange){
                  aux = randomNumbers.nextInt(line-1);
                  aux1 = mask[i];
                  mask[i] = mask[aux];
                  mask[aux] = aux1;
              }
          }

          double[][] newE = new double[line][coll];

          for(int j = 0; j < line; j++){
              for(int k = 0; k < coll; k++)
                  newE[cont][k] = E[mask[j]][k];
              cont++;
          }


          // mostra newE

/*        for(int j = 0; j < line; j++){
       //     System.out.print(mask[j] + "   ");
           for(int k = 0; k < coll; k++)
               System.out.print(newE[j][k] + " ");
            System.out.println();
         }
 */

          return newE;
      }


    public double[][] normalizacaoMaiorMenorMissing(double[][] A){
        double normMatriz[][];
        double maior, menor;
        int line = A.length;
        int coll = A[0].length;
        normMatriz = new double[line][coll];

        for(int q = 0; q < coll - 1; q++) {
            maior = -99999999;
            menor = 100000000;
            for(int p = 0; p < line; p++){
               if(A[p][q] != emptyValue && A[p][q] > maior)
                  maior = A[p][q];
               if(A[p][q] != emptyValue && A[p][q] < menor)
                  menor = A[p][q];
            }

            if(maior == menor) {
                for(int r = 0; r < line; r++)
                  normMatriz[r][q] = 0;
            }
            else{
               for(int r = 0; r < line; r++)
                  if(A[r][q] != emptyValue)
                     normMatriz[r][q] = (A[r][q] - menor) / (maior - menor);
                  else
                     normMatriz[r][q] = emptyValue;
               }
        }
        for(int c = 0; c < line; c++)
           normMatriz[c][coll - 1] = A[c][coll -1];   // coloca a classe

        return normMatriz;
    }

      public double[][] normalizacaoMaiorMenor(double[][] A){
        double normMatriz[][];
        double maior, menor;
        int line = A.length;
        int coll = A[0].length;
        normMatriz = new double[line][coll];

        for(int q = 0; q < coll - 1; q++) {
            maior = A[0][q];
            menor = A[0][q];
            for(int p = 0; p < line; p++){
               if(A[p][q] > maior)
                  maior = A[p][q];
               if(A[p][q] < menor)
                  menor = A[p][q];
            }

            if(maior == menor) {
                for(int r = 0; r < line; r++)
                  normMatriz[r][q] = 0;
            }
            else{
               for(int r = 0; r < line; r++)
                   normMatriz[r][q] = (A[r][q] - menor) / (maior - menor);

               }
        }
        for(int c = 0; c < line; c++)
           normMatriz[c][coll - 1] = A[c][coll -1];   // coloca a classe

        return normMatriz;
    }


     private double emptyValue = -10000;
     private double[] Classes;
     private char[] attributeType;
     private int numAtr, nroClasses;
     private String FileName;
     private double probMiss;

}
