/**
 * Created by IntelliJ IDEA.
 * User: Jo�o
 * Date: 09/05/2007
 * Time: 08:59:26
 * To change this template use File | Settings | File Templates.
 */
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.io.*;
import java.util.Stack;
import java.text.DecimalFormat;

public class DecisionNetwork {


    public DecisionNetwork(char[] inputFile){


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
            if(Character.isDigit( inputFile[tkPos1]) || (cl = inputFile[tkPos1]) == '-'){
                while ( Character.isDigit( inputFile[tkPos1])||(cl = inputFile[tkPos1]) == '.'||
                        (cl = inputFile[tkPos1]) == '-'){
                    tkPos1++;
                }
                collNumber++;
            }
            else
                tkPos1++;

            if ((cl = inputFile[tkPos1]) == '\0') // for 1 line data
                break;
        }

        //aloca matriz de dados
        double matriz[][];
        matriz = new double[lineNumber][collNumber];

        int i = 0;
        int j = 0;
        StringBuffer number = new StringBuffer();

        // constroi matriz de dados
        while (  (ch = inputFile[tokenPos]) != '\0') {

            while((ch = inputFile[tokenPos]) != '\n' ) {
                if(Character.isDigit( inputFile[tokenPos]) || (ch = inputFile[tokenPos]) == '-'){
                    while ( Character.isDigit( inputFile[tokenPos])||(ch = inputFile[tokenPos]) == '.'||
                            (ch = inputFile[tokenPos]) == '-'){
                        number.append(inputFile[tokenPos]);
                        tokenPos++;
                    }
                   // System.out.println(i);
                    matriz[i][j] = Double.valueOf(number.toString()).doubleValue();
                    j++;
                    number.delete(0, number.length());
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
                if(matriz[p][coll - 1] == tempClass[q]){
                    newClass = false;
                }

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


      //  stratifiedCrossValidationGeneticallyDefinedStructure(matriz,0);  // Algoritmo genético

        // particionaAtributo(matriz);

        /* networks = new Networks[nroClasses];

          for(int b = 0; b < nroClasses; b++) {   // para experimento com 3 classe  - anda em classes
              oneClassTrain = criaTreinamento(matriz,b+1);
              networks[b] = new Networks(oneClassTrain);
              networks[b].learn();
           }

        */
        // classification
        //     for(int b = 0; b < nroClasses; b++)
        //       networks[b].probClass(matrizteste);

   //    matriz = shuffle(matriz);

    //  validation(matriz);                   // com nova rede que conecta todos os atributos


   //   mostraRede(matriz);   // mostra rede com pesos

 //   matriz = normalizaMediaEDesvio(matriz);

      stratifiedCrossValidation(matriz,0);

   //   stratifiedCrossValidationFull(matriz);           // ainda com rede antiga

    }

    public DecisionNetwork(){

   /*     int[] elements = {1,2,3,4,5,6,7,8};

        int[] indices;

        CombinationGenerator x = new CombinationGenerator (elements.length, 4);
        StringBuffer combination;
        while (x.hasMore ()) {
             combination = new StringBuffer ();
            indices = x.getNext ();
            for (int i = 0; i < indices.length; i++) {
               // System.out.print(indices[i]);
                combination.append(elements[indices[i]]);
            }
            System.out.println( combination.toString ());
        }
     */
        complexityExp();
         // artificialData();
        //artificialDataHighDimension();
    }


    public DecisionNetwork(char[] inputFile, String filename){  // deals Heterogeneous Attributes


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






    //  criaMatrizMissingAttrubute(matriz,filename);

   //    criaMatrizMissingAttrubuteNA(matriz,filename);        // atualmente rodando com prob = 0

      matriz = normalizacaoMaiorMenor(matriz);    // normaliza��o para AbDG


      //exemploAbDG(matriz); // constroi um AbDG com matriz

   //   criaMatrizMissingAttrubuteNALinXCol(matriz,filename);       // gera as 5 versoes com prob 0.1 a 0.5

     //  criaMatrizMissingAttrubuteNALinXColForAmelia(matriz,filename);
   //

        // imputa��o    ##########################################

       // 'iris', 'teac', 'wine', 'vehi', 'wdpc', 'wdbc', 'habe', 'pima', 'wave', 'sona', 'hspe', 'bala',	'hear', 'live'
       String f = "imputed";

    //   fileProbMissingTreinoeTeste(matriz,f);            // CPP-AbDG

     //  fileProbMissingTreinoeTesteAbDG(matriz,f);



        // figura distor��o imputa��o
       // FigureMissingTreinoAbDG(matriz,f);

        // n�o sao esses -- V ver
     //  criaDominioImputadoCppAbDG(matriz,filename);             // cria dominio imputado por CPP-AbDG

   //    criaDominioImputadoAbDG(matriz,filename);


     //   criaDominioRefinadoCppAbDG(matriz,filename, 0,1);      // para data cleaning - data quality




  //      matriz = normalizacaoMaiorMenor(matriz);    // normaliza��o para AbDG

    //   criaTreinoeTesteRefining(matriz, filename);     //  cria treino e teste (nao alterado) refinando ambos

     //   criaTreinoeTesteRefiningExtension(matriz,filename);  // cria treino e teste com ruido


       //      simulateRefiningAttributeRandom(matriz, filename);


  //      matriz = normalizacaoReescala(matriz,0,10);  // rescale, matriz e novo intervalo

  //     matriz = normalizacaoMaiorMenor(matriz);    // normaliza��o para AbDG

     //   matriz = normalizacaoMaiorMenorMissing(matriz);




   //     twoWayImputationAbDGClassifier(matriz);         //       AbDG  - mecanismo de imputa��o interno

  //     twoWayImputationAbDGClassifierFull(matriz);     //      CpP-AbDG  - mecanismo de imputa��o interno

        
       // matriz = shuffle(matriz);


//      stratifiedCrossValidation(matriz,0);           // ordenado

     // stratifiedCrossValidationFull(matriz,0);    //  full-connected

   //     stratifiedCrossValidationGeneticallyDefinedStructure(matriz,0);    //  geneticamente definido

     // histogramas
 /*    for(int v = 3; v < 12; v+=2){
           System.out.println("    " +  v +  "     ");
           stratifiedCrossValidationFull(matriz,v);
     }
  */
     // stratifiedDoubleRepeatedCVFull(matriz);


   //   criaConjuntosTreinoeTeste(matriz);   // cria conjuntos de treino com atr faltante e de teste sem atr faltantes

 /*     PrintWriter out;
       FileOutputStream outputStream = null;
       try {
          outputStream = new FileOutputStream ("C:\\Java\\Datasets\\" + filename + ".txt");
           } catch ( java.io.IOException e) {
          System.out.println("Could not create result.txt");
      }

       out = new PrintWriter(outputStream);

         for(int ia = 0; ia < line; ia++){
           for(int jb = 0; jb < coll; jb++)
                 out.print(matriz[ia][jb] + " ");

          out.println();
         }
       out.close();
   */

    }

    public double[][] adjustFile(char[] inputFile){

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
            if(Character.isDigit( inputFile[tkPos1]) || (cl = inputFile[tkPos1]) == '-'){
                while ( Character.isDigit( inputFile[tkPos1])||(cl = inputFile[tkPos1]) == '.'||
                        (cl = inputFile[tkPos1]) == '-'){
                    tkPos1++;
                }
                collNumber++;
            }
            else
                tkPos1++;

            if ((cl = inputFile[tkPos1]) == '\0') // for 1 line data
                break;
        }

        //aloca matriz de dados
        double matriz[][];
        matriz = new double[lineNumber][collNumber];

        int i = 0;
        int j = 0;
        StringBuffer number = new StringBuffer();

        // constroi matriz de dados
        while (  (ch = inputFile[tokenPos]) != '\0') {

            while((ch = inputFile[tokenPos]) != '\n' ) {
                if(Character.isDigit( inputFile[tokenPos]) || (ch = inputFile[tokenPos]) == '-'){
                    while ( Character.isDigit( inputFile[tokenPos])||(ch = inputFile[tokenPos]) == '.'||
                            (ch = inputFile[tokenPos]) == '-'){
                        number.append(inputFile[tokenPos]);
                        tokenPos++;
                    }

                    matriz[i][j] = Double.valueOf(number.toString()).doubleValue();
                    j++;
                    number.delete(0, number.length());
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
     return matriz;
    }

   public int[] indicesEstratificados(double[][] matriz, int folds){

        int line = matriz.length;
        int coll = matriz[0].length;
        int[] indices = new int[line];
        int[] classes = new int[nroClasses+1]; // +1 pois n�o existe classe 0
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
           proportions[j] = (double)classes[j]/(double)line;   // calcula propor��o de instancias no conjunto total

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
        for(int j = 0; j < line; j++)                   // imprime para verifica��o
            contV[indices[j]]++;

  //      for(int kk = 1; kk <= folds; kk++)
  //            System.out.println( kk + " " + contV[kk]);

    return indices;

    }


    public void stratifiedCrossValidation(double[][] matriz, int v){

              int line = matriz.length;
              int coll = matriz[0].length;
              double[][] matrizTreino;
              double[][] matrizTeste;
              int fold = 10;
              int contTreino = 0, contTeste = 0, it = 1;
              int compAux = 0;
              double[] mainClassification, mainClassification1;
              double[] somaQuadClassification = new double[12];
              double[] somaClassification = new double[12];
              double[] desvio = new double[12];
              double[] mediaClass = new double[12];

              double[] somaQuadClassification1 = new double[12];
              double[] somaClassification1 = new double[12];
              double[] desvio1 = new double[12];
              double[] mediaClass1 = new double[12];
              double newDesvio = 0, newMediaClass = 0;
              double[] classDesvio = new double[2];
              double[][] oneClassTrain;
         //     networks = new Networks[nroClasses];

        DecimalFormat show = new DecimalFormat("0.00");

           for(int ncv = 0; ncv < 10; ncv++) {

              it = 1;
              matriz = shuffle(matriz);
              int[] stratification = indicesEstratificados(matriz,fold);


             while(it <= fold){

                 networks = new Networks[nroClasses];
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
               it++;

               particionaAtributo(matrizTreino);

               int[] auxAtr = new int[vetAtrHandler.length];
               int[] ordem;
               for(int o = 0; o < auxAtr.length; o++)
                  auxAtr[o] = 0;
               int contAtrZero = 0;
                                                             // if-else adicionado para diferenciar atributos numerico e categorico
                for(int a = 0; a < coll - 1; a++){
                    if(attributeType[a] == 'n')         // atributo real - numerico
                       vetAtrHandler[a].histogramPart(13); ////  vetAtrHandler[a].MDLP(); // vetAtrHandler[a].EDADB(nroClasses);//  vetAtrHandler[a].EDADB(nroClasses);  //      //        // usa metodo EDA-DB para obter intervalos
                    else
                       vetAtrHandler[a].Categorical();
                    //  vetAtrHandler[a].MDLP();                                 // define intervalos - recursivamente - segundo criterio MDLPC - fayyad
                  //  vetAtrHandler[a].optmizeIntervals();
                   //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                   vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
                   if(vetAtrHandler[a].getIntervalGain() == 0){
                       auxAtr[a] = 1;
                       contAtrZero++;
                   }

              //    System.out.println(a + "  " + vetAtrHandler[a].getIntervalGain());
                     vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
                 //  vetAtrHandler[a].intervalWeightsEntropy();
               }

               // atualiza matriz - retira atributos com ganho 0           // ************ tirar atrs com 0 de ganho
             if(contAtrZero != 0 && contAtrZero != (coll-1)){
                   matrizTreino = retiraAtributos(auxAtr, contAtrZero, matrizTreino);
                   matrizTeste = retiraAtributosTeste(auxAtr, contAtrZero, matrizTeste);
                   coll = matrizTreino[0].length;
               }
      

                   ordem = ordenaVetorDeAtributos(coll);                // ordena atributos por ganho de informa��o
                   matrizTreino = ordenaAtributosPorGanho(matrizTreino,ordem);
                   matrizTeste = ordenaAtributosPorGanho(matrizTeste,ordem);


               for(int i = 0; i < nroClasses; i++) {   //  anda em classes
                 //  ordem = ordenaAtributos(Classes[i]);
                   oneClassTrain = criaTreinamento(matrizTreino,Classes[i]);
                   networks[i] = new Networks(oneClassTrain, Classes[i], matrizTreino.length);
                   networks[i].learn(vetAtrHandler);

               }

               normalizeCorrelations(coll-1);


               normalizeIntervalGain(coll-1);


              // mainClassification = classificationAccuracyTest(matrizTeste);
               //mainClassification1 = DnoClassifier(matrizTeste); // - varios classificadores para teste


              matrizTeste = addNoise(matrizTeste,0.1);

          //    mainClassification = DnoClassifier(matrizTeste); // - varios classificadores para teste
              mainClassification1 = DnoClassifierFuzzy(matrizTeste); // - varios classificadores para teste

              for(int i = 0; i < mainClassification1.length; i++){
              //   somaQuadClassification[i] += Math.pow(mainClassification[i],2);
               //  somaClassification[i] += mainClassification[i];

                 somaQuadClassification1[i] += Math.pow(mainClassification1[i],2);
                 somaClassification1[i] += mainClassification1[i];

              }

              networks = null;
              vetAtrHandler = null;


           } // fim do while it < 10        -- cross validation --

       }

        /*   System.out.println("Normal");
          for(int i = 0; i < desvio.length; i++){
              desvio[i] = Math.sqrt((somaQuadClassification[i] - (Math.pow(somaClassification[i],2)/100))/99);
              mediaClass[i] = somaClassification[i]/100;
              System.out.println(show.format(i*0.1) + "  " + show.format(mediaClass[i]) + "   " + show.format(desvio[i]));
          }
         */

            System.out.println("Fuzzy");
            for(int i = 0; i < desvio1.length; i++){
               desvio1[i] = Math.sqrt((somaQuadClassification1[i] - (Math.pow(somaClassification1[i],2)/100))/99);
               mediaClass1[i] = somaClassification1[i]/100;
               System.out.println(show.format(i*0.1) + "  " + show.format(mediaClass1[i]) + "   " + show.format(desvio1[i]));
              //  System.out.println(i*0.1 + "  " + mediaClass1[i] + "   " + desvio1[i]);
          }

         // System.out.println(mediaClass + "  " + desvio);// + "  " + mediaCenter + "  " + desvioCenter + "  " +  newMediaClass + "  " + newDesvio);


        }



   public void stratifiedCrossValidationFull(double[][] matriz, int v){

           int line = matriz.length;
           int coll = matriz[0].length;
           double[][] matrizTreino;
           double[][] matrizTeste;
           int fold = 10;
           int contTreino = 0, contTeste = 0, it = 1;
           int compAux = 0;
           double[] mainClassification, mainClassification1;
           double[] somaQuadClassification = new double[3];
           double[] somaClassification = new double[3];
           double[] desvio = new double[3];
           double[] mediaClass = new double[3];

           double[] somaQuadClassification1 = new double[12];
           double[] somaClassification1 = new double[12];
           double[] desvio1 = new double[12];
           double[] mediaClass1 = new double[12];
           double newDesvio = 0, newMediaClass = 0;
           double[] classDesvio = new double[2];
           double[][] oneClassTrain;
          // networks = new Networks[nroClasses];

        DecimalFormat show = new DecimalFormat("0.00");

        for(int ncv = 0; ncv < 10; ncv++) {

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
            it++;

            particionaAtributo(matrizTreino);

            int[] auxAtr = new int[vetAtrHandler.length];
            int[] ordem;
            for(int o = 0; o < auxAtr.length; o++)
               auxAtr[o] = 0;
            int contAtrZero = 0;


              /* #### retirar this.intervals = thrs de AttributeHandler  #### */
             for(int a = 0; a < coll - 1; a++){
                 if(attributeType[a] == 'n')         // atributo real - numerico
                       vetAtrHandler[a].MDLP();   // vetAtrHandler[a].EDADB(nroClasses);// vetAtrHandler[a].histogramPart(v); // vetAtrHandler[a].MDLP();//          // usa metodo EDA-DB para obter intervalos
                    else
                       vetAtrHandler[a].Categorical();

                                                            //  vetAtrHandler[a].optmizeIntervals();
                //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
                if(vetAtrHandler[a].getIntervalGain() == 0){
                    auxAtr[a] = 1;
                    contAtrZero++;
                }

           //    System.out.println(a + "  " + vetAtrHandler[a].getIntervalGain());
                  vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
             //  vetAtrHandler[a].intervalWeightsEntropy();
            }


     // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   FULL   %%%%%%%%%%%%%%%%

/*          if(contAtrZero != 0 && contAtrZero != (coll-1)){
                   matrizTreino = retiraAtributos(auxAtr, contAtrZero, matrizTreino);
                   matrizTeste = retiraAtributosTeste(auxAtr, contAtrZero, matrizTeste);
                   coll = matrizTreino[0].length;
               }
*/
              //#############################################



      //   for(int i = 0; i < nroClasses; i++) {   // para experimento com 3 classe  - anda em classes
      //      oneClassTrain = criaTreinamento(matrizTreino,Classes[i]);
      //      networksFull[i] = new NetworkFull(oneClassTrain, Classes[i], matrizTreino.length);
      //      networksFull[i].learnFullConection(vetAtrHandler);
      //  }

        networksFull = new NetworkFull(matrizTreino, nroClasses, vetAtrHandler);
        networksFull.learnFullConection();

        int numAttr = coll - 1;
        int numEdges = numAttr + (numAttr*(numAttr-3))/2;

        networksFull.normalizeCorrelationsFullVersion();  // normaliza correlações de arestas

        normalizeIntervalGain(coll-1);


    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


     /*
            // atualiza matriz - retira atributos com ganho 0           // ************ tirar atrs com 0 de ganho
          if(contAtrZero != 0){
                matrizTreino = retiraAtributos(auxAtr, contAtrZero, matrizTreino);
                matrizTeste = retiraAtributosTeste(auxAtr, contAtrZero, matrizTeste);
                coll = matrizTreino[0].length;
            }

 // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$  p-partido

                ordem = ordenaVetorDeAtributos(coll);                // ordena atributos por ganho de informa��o 
                matrizTreino = ordenaAtributosPorGanho(matrizTreino,ordem);
                matrizTeste = ordenaAtributosPorGanho(matrizTeste,ordem);


             // vetor que armazena correla��es de instancias independentes de suas classes
            AttributeCorrelation[] vetAtrCorrAllClass = classIndependentCorrs(vetAtrHandler,matrizTreino);


            for(int i = 0; i < nroClasses; i++) {   //  anda em classes
              //  ordem = ordenaAtributos(Classes[i]);
                oneClassTrain = criaTreinamento(matrizTreino,Classes[i]);
                networks[i] = new Networks(oneClassTrain, Classes[i], matrizTreino.length);
                networks[i].learn(vetAtrHandler);

            }

            normalizeCorrelations(coll-1);

            normalizeIntervalGain(coll-1);


              */

       //    matrizTeste = retiraAtributosTeste(auxAtr, contAtrZero, matrizTeste);
              
           // mainClassification = classificationAccuracyTest(matrizTeste);
           mainClassification1 = DnoClassifierFull(matrizTeste);         // varios classificadores para teste





           for(int i = 0; i < mainClassification1.length; i++){
           //   somaQuadClassification[i] += Math.pow(mainClassification[i],2);
           //   somaClassification[i] += mainClassification[i];

              somaQuadClassification1[i] += Math.pow(mainClassification1[i],2);
              somaClassification1[i] += mainClassification1[i];

           }

           networksFull = null;
           vetAtrHandler = null;


        } // fim do while it < 10        -- cross validation --

    }

      //  System.out.println();
  /*     for(int i = 0; i < desvio.length; i++){
           desvio[i] = Math.sqrt((somaQuadClassification[i] - (Math.pow(somaClassification[i],2)/100))/99);
           mediaClass[i] = somaClassification[i]/100;
           System.out.println(mediaClass[i] + "   " + desvio[i]);
       }
   */
         for(int i = 0; i < desvio1.length; i++){
            desvio1[i] = Math.sqrt((somaQuadClassification1[i] - (Math.pow(somaClassification1[i],2)/100))/99);
            mediaClass1[i] = somaClassification1[i]/100;
            System.out.println(show.format(i*0.1) + "  " + show.format(mediaClass1[i]) + "   " + show.format(desvio1[i]));
       }

      // System.out.println(mediaClass + "  " + desvio);// + "  " + mediaCenter + "  " + desvioCenter + "  " +  newMediaClass + "  " + newDesvio);


     }

    public void stratifiedDoubleRepeatedCVFull(double[][] matriz){

        int line = matriz.length;
        int coll = matriz[0].length;
        double[][] matrizTreino;
        double[][] matrizTeste;
        double[][] matrizValidacao;
        double[][] matrizTrVal;           //  matriz usada para treinar classificador qdo descoberto melhor parametro
        int fold = 10;
        int contTreino = 0, contTeste = 0, contValidacao = 0, it = 1, te = 1;
        int idMaior = 0;
        double maior = 0;
        double[] mainClassification;
        double[] somaQuadClassification = new double[12];
        double[] somaClassification = new double[12];
        double desvio = 0;
        double mediaClass = 0;
        double testClassification, somaTestClassification = 0;
        double somaQuadTestClassification = 0;
        double[][] oneClassTrain;
      

        for(int ncv = 0; ncv < 10; ncv++) {    // repeti��o para o numero de rdcv ser�o realizados

            te = 1;
            matriz = shuffle(matriz);
            int[] stratification = indicesEstratificados(matriz,fold);


            while(te <= fold){               // Separa teste do conjunto cv
                // separa um conjunto para teste
                contTeste = 0;
                contTreino = 0;
                for(int i = 0; i < line; i++)
                    if(stratification[i] == te)
                        contTeste++;

                matrizTeste = new double[contTeste][coll];
                matrizTrVal = new double[line - contTeste][coll];
                contTeste = 0;

                for(int h = 0; h < line; h++)
                    if(stratification[h] == te){
                        for(int y = 0; y < coll; y++)
                            matrizTeste[contTeste][y] = matriz[h][y];
                        contTeste++;
                    }
                     else{
                        for(int y = 0; y < coll; y++)
                            matrizTrVal[contTreino][y] = matriz[h][y];
                        contTreino++;
                   }

                it = 1;

                while(it <= fold){                  // CV da valida��o - escolhe o modelo

                    if(it != te){         // pois te � conjunto de teste e nao pode ser incluido nesta fase

                       // networksFull = new NetworkFull[nroClasses];
                        contTreino = 0;
                        contValidacao = 0;
                        for(int i = 0; i < line; i++)
                            if(stratification[i] == it)
                                contValidacao++;

                        matrizTreino = new double[line - (contValidacao + contTeste)][coll];
                        matrizValidacao = new double[contValidacao][coll];

                        contValidacao = 0;
                        contTreino = 0;
                        for(int h = 0; h < line; h++)
                            if(stratification[h] != it && stratification[h] != te){
                                for(int y = 0; y < coll; y++ )
                                    matrizTreino[contTreino][y] = matriz[h][y];  //  matrizTreino[contTreino][y] = matriz[h][y];
                                contTreino++;
                            }
                            else if(stratification[h] != te){
                                for(int y = 0; y < coll; y++ )
                                    matrizValidacao[contValidacao][y] = matriz[h][y];
                                contValidacao++;
                            }


                        particionaAtributo(matrizTreino);

                        int[] auxAtr = new int[vetAtrHandler.length];
                        for(int o = 0; o < auxAtr.length; o++)
                            auxAtr[o] = 0;
                        int contAtrZero = 0;


                        /* #### retirar this.intervals = thrs de AttributeHandler // roda com histograma #### */
                        for(int a = 0; a < coll - 1; a++){
                            if(attributeType[a] == 'n')         // atributo real - numerico
                                vetAtrHandler[a].histogramPart(5);// vetAtrHandler[a].MDLP();       // usa metodo EDA-DB para obter intervalos   // vetAtrHandler[a].EDADB(nroClasses);
                            else
                                vetAtrHandler[a].Categorical();

                            //  vetAtrHandler[a].optmizeIntervals();
                            //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                            vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
                            if(vetAtrHandler[a].getIntervalGain() == 0){
                                auxAtr[a] = 1;
                                contAtrZero++;
                            }

                            //    System.out.println(a + "  " + vetAtrHandler[a].getIntervalGain());
                            //   vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
                            vetAtrHandler[a].intervalWeightsEntropy();
                        }


                        // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   FULL   %%%%%%%%%%%%%%%%

                        if(contAtrZero != 0 && contAtrZero != (coll-1)){
                            matrizTreino = retiraAtributos(auxAtr, contAtrZero, matrizTreino);
                            matrizValidacao = retiraAtributosTeste(auxAtr, contAtrZero, matrizValidacao);
                            coll = matrizTreino[0].length;
                        }

                        //#############################################

                        networksFull = new NetworkFull(matrizTreino, nroClasses, vetAtrHandler);
                        networksFull.learnFullConection();

                     /*   for(int i = 0; i < nroClasses; i++) {   // para experimento com 3 classe  - anda em classes
                            oneClassTrain = criaTreinamento(matrizTreino,Classes[i]);
                            networksFull[i] = new NetworkFull(oneClassTrain, Classes[i], matrizTreino.length);
                            networksFull[i].learnFullConection(vetAtrHandler);
                        }

                    */
                        int numAttr = coll - 2;
                        int numEdges = numAttr + (numAttr*(numAttr-3))/2;

                        //  printNetwork(coll-1);

                        networksFull.normalizeCorrelationsFullVersion();

                        normalizeIntervalGain(coll-1);

                        mainClassification = DnoClassifierFull(matrizValidacao);         // varios classificadores para teste


                        for(int i = 0; i < mainClassification.length; i++){
                            somaQuadClassification[i] += Math.pow(mainClassification[i],2);
                            somaClassification[i] += mainClassification[i];

                        }

                        networksFull = null;
                        vetAtrHandler = null;

                    } // te != it

                    it++;

                } // fim do while it < 10        -- cross validation --

                // correto pegar valor de neta correspondente � melhor media e usar para criar outro modelo
                // codigo abaixo - falta treinamento do novo modelo
                maior = somaClassification[0];
                idMaior = 0;
                for(int i = 1; i < somaClassification.length; i++){
                    if(maior < somaClassification[i]){
                        maior = somaClassification[i];
                        idMaior = i;
                    }
                }

                double neta = ((double)idMaior)*0.1;

                // %%%%%%%%%%%%%%%%% cria nova rede


              //  networksFull = new NetworkFull[nroClasses];
                particionaAtributo(matrizTrVal);

                                      int[] auxAtr = new int[vetAtrHandler.length];
                                      for(int o = 0; o < auxAtr.length; o++)
                                          auxAtr[o] = 0;
                                      int contAtrZero = 0;


                                      /* #### retirar this.intervals = thrs de AttributeHandler // roda com histograma #### */
                                      for(int a = 0; a < coll - 1; a++){
                                          if(attributeType[a] == 'n')         // atributo real - numerico
                                              vetAtrHandler[a].histogramPart(5);// vetAtrHandler[a].MDLP();       // usa metodo EDA-DB para obter intervalos   // vetAtrHandler[a].EDADB(nroClasses);
                                          else
                                              vetAtrHandler[a].Categorical();

                                          //  vetAtrHandler[a].optmizeIntervals();
                                          //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                                          vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
                                          if(vetAtrHandler[a].getIntervalGain() == 0){
                                              auxAtr[a] = 1;
                                              contAtrZero++;
                                          }

                                          //    System.out.println(a + "  " + vetAtrHandler[a].getIntervalGain());
                                          //   vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
                                          vetAtrHandler[a].intervalWeightsEntropy();
                                      }


                                      // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   FULL   %%%%%%%%%%%%%%%%

                                      if(contAtrZero != 0 && contAtrZero != (coll-1)){
                                          matrizTrVal = retiraAtributos(auxAtr, contAtrZero, matrizTrVal);
                                          matrizTeste = retiraAtributosTeste(auxAtr, contAtrZero, matrizTeste);
                                          coll = matrizTrVal[0].length;
                                      }

                                      //#############################################

                                        networksFull = new NetworkFull(matrizTrVal, nroClasses, vetAtrHandler);
                                        networksFull.learnFullConection();
                                  /*    for(int i = 0; i < nroClasses; i++) {   // para experimento com 3 classe  - anda em classes
                                          oneClassTrain = criaTreinamento(matrizTrVal,Classes[i]);
                                          networksFull[i] = new NetworkFull(oneClassTrain, Classes[i], matrizTrVal.length);
                                          networksFull[i].learnFullConection(vetAtrHandler);
                                      }
                                    */
                                      int numAttr = coll - 2;
                                      int numEdges = numAttr + (numAttr*(numAttr-3))/2;

                                      //  printNetwork(coll-1);

                                      networksFull.normalizeCorrelationsFullVersion();

                                      normalizeIntervalGain(coll-1);

                              // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%





                testClassification = DnoClassifierFull(matrizTeste,neta);
                somaTestClassification += testClassification;
                somaQuadTestClassification += Math.pow(testClassification,2);



                te++;
            }


        }


            desvio = Math.sqrt((somaQuadTestClassification - (Math.pow(somaTestClassification,2)/100))/99);
            mediaClass = somaTestClassification/100;
            System.out.println( "  " + mediaClass + "   " + desvio);


        // System.out.println(mediaClass + "  " + desvio);// + "  " + mediaCenter + "  " + desvioCenter + "  " +  newMediaClass + "  " + newDesvio);


    }


    public void stratifiedCrossValidationGeneticallyDefinedStructure(double[][] matriz, int v){

        int line = matriz.length;
        int coll = matriz[0].length;
        double[][] matrizTreino;
        double[][] matrizTeste;
        int fold = 10;
        int contTreino = 0, contTeste = 0, it = 1;
        int cont = 0;
        double[] mainClassification;
        double somaQuadClassification = 0;
        double somaClassification = 0;
        double desvio = 0;
        double[] mediaClass = new double[3];

        double[] somaQuadClassification1 = new double[12];
        double[] somaClassification1 = new double[12];
        double[] mainClassification1 = new double[12];
        double[] desvio1 = new double[12];
        double[] mediaClass1 = new double[12];
        double newDesvio = 0, newMediaClass = 0;
        double[] classDesvio = new double[2];
        double[][] oneClassTrain;
        // networks = new Networks[nroClasses];

        DecimalFormat show = new DecimalFormat("0.00");

        for(int ncv = 0; ncv < 1; ncv++) {

            it = 1;
            matriz = shuffle(matriz);
            int[] stratification = indicesEstratificados(matriz,fold);


            while(it <= fold){

               // networksFull = new NetworkFull[nroClasses];
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
                it++;

                particionaAtributo(matrizTreino);

                int[] auxAtr = new int[vetAtrHandler.length];
                int[] ordem;
                for(int o = 0; o < auxAtr.length; o++)
                    auxAtr[o] = 0;
                int contAtrZero = 0;


              /* #### retirar this.intervals = thrs de AttributeHandler  #### */
                for(int a = 0; a < coll - 1; a++){
                    if(attributeType[a] == 'n')         // atributo real - numerico
                        vetAtrHandler[a].MDLP();   // vetAtrHandler[a].EDADB(nroClasses);// vetAtrHandler[a].histogramPart(v); // vetAtrHandler[a].MDLP();//          // usa metodo EDA-DB para obter intervalos
                    else
                        vetAtrHandler[a].Categorical();

                    //  vetAtrHandler[a].optmizeIntervals();
                    //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                    vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
                    if(vetAtrHandler[a].getIntervalGain() == 0){
                        auxAtr[a] = 1;
                        contAtrZero++;
                    }

                    //    System.out.println(a + "  " + vetAtrHandler[a].getIntervalGain());
                    vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
                    //  vetAtrHandler[a].intervalWeightsEntropy();
                }


                // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   FULL   %%%%%%%%%%%%%%%%

     /*           if(contAtrZero != 0 && contAtrZero != (coll-1)){
                    matrizTreino = retiraAtributos(auxAtr, contAtrZero, matrizTreino);
                    matrizTeste = retiraAtributosTeste(auxAtr, contAtrZero, matrizTeste);
                    coll = matrizTreino[0].length;
                }
*/
                //#############################################


                networksFull = new NetworkFull(matrizTreino, nroClasses, vetAtrHandler);
                networksFull.learnFullConection();

                int numAttr = coll - 1;
                int numEdges = numAttr + (numAttr*(numAttr-3))/2;

                //  printNetwork(coll-1);

                networksFull.normalizeCorrelationsFullVersion();

                normalizeIntervalGain(coll-1);


                // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


     /*
            // atualiza matriz - retira atributos com ganho 0           // ************ tirar atrs com 0 de ganho
          if(contAtrZero != 0){
                matrizTreino = retiraAtributos(auxAtr, contAtrZero, matrizTreino);
                matrizTeste = retiraAtributosTeste(auxAtr, contAtrZero, matrizTeste);
                coll = matrizTreino[0].length;
            }

 // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$  p-partido

                ordem = ordenaVetorDeAtributos(coll);                // ordena atributos por ganho de informa��o
                matrizTreino = ordenaAtributosPorGanho(matrizTreino,ordem);
                matrizTeste = ordenaAtributosPorGanho(matrizTeste,ordem);


             // vetor que armazena correla��es de instancias independentes de suas classes
            AttributeCorrelation[] vetAtrCorrAllClass = classIndependentCorrs(vetAtrHandler,matrizTreino);


            for(int i = 0; i < nroClasses; i++) {   //  anda em classes
              //  ordem = ordenaAtributos(Classes[i]);
                oneClassTrain = criaTreinamento(matrizTreino,Classes[i]);
                networks[i] = new Networks(oneClassTrain, Classes[i], matrizTreino.length);
                networks[i].learn(vetAtrHandler);

            }

            normalizeCorrelations(coll-1);

            normalizeIntervalGain(coll-1);


              */



                Evolution ea; // = new Evolution(networksFull, vetAtrHandler,matrizTeste,30);

                for(int n = 0; n < 11; n++ ){
                    ea = null;
                    ea = new Evolution(networksFull, vetAtrHandler,matrizTeste,30,(double)(n*0.1));
                    mainClassification1[n] = ea.getAcc() * 100;

                    somaQuadClassification1[n] += Math.pow(mainClassification1[n],2);
                    somaClassification1[n] += mainClassification1[n];

                }

                cont++;
             //   somaClassification += ea.getAcc();
              //  somaQuadClassification += ea.getAcc();


                networksFull = null;
                vetAtrHandler = null;


            } // fim do while it < 10        -- cross validation --

        }

        //  System.out.println();
      for(int i = 0; i < desvio1.length; i++){
           desvio1[i] = Math.sqrt((somaQuadClassification1[i] - (Math.pow(somaClassification1[i],2)/cont))/(cont-1));
           mediaClass1[i] = somaClassification1[i]/cont;
           System.out.println(show.format(i*0.1) + "  " + show.format(mediaClass1[i]) + "   " + show.format(desvio1[i]));

       }


         //   desvio = Math.sqrt((somaQuadClassification - (Math.pow(somaClassification,2)/cont))/(cont-1));
        //    System.out.println(show.format(somaClassification/cont) + "   " + show.format(desvio));


        // System.out.println(mediaClass + "  " + desvio);// + "  " + mediaCenter + "  " + desvioCenter + "  " +  newMediaClass + "  " + newDesvio);


    }

     public void RefiningStratifiedCrossValidationFull(double[][] matriz, String fileName, int itCv, double prob){
         // metodo usado para refinar conjunto de treinamento. Realiza cross-validation em matriz e cria dois conjuntos
         // um com as instancias que o classificador acerta e um com as instancias que o classificador erra

           int line = matriz.length;
           int coll = matriz[0].length;
           double[][] matrizTreino;
           double[][] matrizTeste;
           int fold = 10;
           int contTreino = 0, contTeste = 0, it = 1;
           int compAux = 0;
           double[] mainClassification1;

           double[] somaQuadClassification1 = new double[12];
           double[] somaClassification1 = new double[12];
           double[] desvio1 = new double[12];
           double[] mediaClass1 = new double[12];
           double[][] oneClassTrain;
          // networks = new Networks[nroClasses];
           double[][] corrects = new double[line][coll];
           double[][] wrongs = new double[line][coll];
           int contCorretos = 0, contErrados = 0;

          DecimalFormat show = new DecimalFormat("0.00");


           it = 1;
           matriz = shuffle(matriz);
           int[] stratification = indicesEstratificados(matriz,fold);


          while(it <= fold){

              //networksFull = new NetworkFull[nroClasses];
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
            it++;

            particionaAtributo(matrizTreino);

            int[] auxAtr = new int[vetAtrHandler.length];
            int[] ordem;
            for(int o = 0; o < auxAtr.length; o++)
               auxAtr[o] = 0;
            int contAtrZero = 0;


              /* #### retirar this.intervals = thrs de AttributeHandler  #### */
             for(int a = 0; a < coll - 1; a++){
                 if(attributeType[a] == 'n')         // atributo real - numerico
                      vetAtrHandler[a].MDLP(); // vetAtrHandler[a].histogramPart(4);  // vetAtrHandler[a].EDADB(nroClasses);//  // //          // usa metodo EDA-DB para obter intervalos
                    else
                      vetAtrHandler[a].Categorical();

                                                            //  vetAtrHandler[a].optmizeIntervals();
                //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
                if(vetAtrHandler[a].getIntervalGain() == 0){
                    auxAtr[a] = 1;
                    contAtrZero++;
                }

           //    System.out.println(a + "  " + vetAtrHandler[a].getIntervalGain());
                  vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
             //  vetAtrHandler[a].intervalWeightsEntropy();
            }


     // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   FULL   %%%%%%%%%%%%%%%%
   /*
          if(contAtrZero != 0 && contAtrZero != (coll-1)){
                   matrizTreino = retiraAtributos(auxAtr, contAtrZero, matrizTreino);
                   matrizTeste = retiraAtributosTeste(auxAtr, contAtrZero, matrizTeste);
                   coll = matrizTreino[0].length;
               }
   */
              //#############################################



       networksFull = new NetworkFull(matrizTreino, nroClasses, vetAtrHandler);
       networksFull.learnFullConection();

        int numAttr = coll - 1;
        int numEdges = numAttr + (numAttr*(numAttr-3))/2;

      //  printNetwork(coll-1);

        networksFull.normalizeCorrelationsFullVersion();

        normalizeIntervalGain(coll-1);



           // mainClassification = classificationAccuracyTest(matrizTeste);
       mainClassification1 = DnoClassifierRetClassesFull(matrizTeste);


           for(int i = 0; i < mainClassification1.length; i++){
              if(mainClassification1[i] == matrizTeste[i][coll-1]){    // acerta
                for(int j = 0; j < coll; j++)
                   corrects[contCorretos][j] = matrizTeste[i][j];
                contCorretos++;
              }
               else{
                 for(int j = 0; j < coll; j++)
                    wrongs[contErrados][j] = matrizTeste[i][j];
                  contErrados++;
             }
           }



           networksFull = null;
           vetAtrHandler = null;


        } // fim do while it < 10        -- cross validation --


         corrects = compactaConjunto(corrects, contCorretos);
         wrongs = compactaConjunto(wrongs, contErrados);

      //   criaDominioRefinadoCppAbDG(matriz, corrects, wrongs, fileName, itCv, prob);


   }



    // REFINING FOR TEST
  /*  public double[][] refiningForTestAssumingAClassFull(double[][] testSet, double probChange){ // matriz teste e para refinar intervalos
        // atribui valor aleatorio do intervalo encontrado
        //   usar probs obtidas dos erros no treino
        int len = testSet.length;
        int col = testSet[0].length;
        double[][] refInstance = new double[nroClasses][col];
        double[][] refMatriz = new double[len][col];
        double[] intervalo;
        double prob = 0;
        double soma = 0, dist, maior;
        double pontoMedio, meioIntervalo;
        int indMaior = 0;
        boolean flag = false, flag2 = false;
        Random rand = new Random();
        double[] estimates = new double[nroClasses];


        for(int k = 0; k < len; k++){

            for(int p = 0; p < nroClasses; p++)
                for(int q = 0; q < col-1; q++)
                    refInstance[p][q] = testSet[k][q];

         //   for(int it = 0; it < 1; it++){

                for(int q = 0; q < col - 1; q++){
                    prob = Math.random();
                    if(prob < probChange){
                        for(int p = 0; p < nroClasses; p++)
                            refInstance[p][q] = emptyValue;
                    }
                }


                for(int j = 0; j < col; j++){                            // atributo faltante � j
                    flag = false;
                    flag2 = false;
                    for(int c = 0; c < nroClasses; c++) {          // encontra rede da classe da instacia que tem o atributo faltante
                        if(refInstance[c][j] == emptyValue){
                            intervalo = networksFull.retrieveIntervalExperimenting(refInstance[c], j, vetAtrHandler[j].getNumInterval());
                            if(testSet[k][j] > intervalo[0] && testSet[k][j] < intervalo[1]){ // valor original esta dentro do intervalo inferido
                                refInstance[c][j] = testSet[k][j];
                                flag2 = true;
                            }
                            else{
                                meioIntervalo = (intervalo[1] - intervalo[0])/2;
                                pontoMedio = intervalo[0] + meioIntervalo;
                                refInstance[c][j] = (rand.nextGaussian()*meioIntervalo + pontoMedio);     // valor aleat�rio dentro do intervalo
                                flag = true;
                            }

                            estimates[c] = networksFull[c].getSomaPesos();  // retorma valor do estimate, s, correspondente ao intervalo

                        }
                    }
                if(flag)
                   contAltAtrTE++;

                if(flag2)
                   contMissAtrTE++;
                }


       //     }

            // encontra distancias entre instancias inferidas e original / seleciona a de menor distancia
            maior = estimates[0];
            for(int a = 1; a < nroClasses; a++){
                if(estimates[a] > maior)
                    indMaior = a;
            }

            for(int r = 0; r < col-1; r++)
                refMatriz[k][r] = refInstance[indMaior][r];
            refMatriz[k][col-1] = testSet[k][col-1];

        }


        atrAltRateTE += (contAltAtrTE/(double)(contAltAtrTE + contMissAtrTE));        
        return refMatriz;

    }


    public double[][] refiningForTestAssumingAClassFull(double[][] testSet, int[][] mask, int it){ // matriz teste para refinar intervalos
        // atribui valor aleatorio do intervalo encontrado
        //   usar probs obtidas dos erros no treino
        int len = testSet.length;
        int col = testSet[0].length;
        double[][] refInstance = new double[nroClasses][col];
        double[][] refMatriz = new double[len][col];
        double[] intervalo;
        double prob = 0;
        double soma = 0, dist, maior;
        double pontoMedio, meioIntervalo;
        int indMaior = 0;
        boolean flag = false, flag2 = false;
        Random rand = new Random();
        double[] estimates = new double[nroClasses];


        for(int k = 0; k < len; k++){

            for(int p = 0; p < nroClasses; p++)
                for(int q = 0; q < col-1; q++)
                    refInstance[p][q] = testSet[k][q];

            //   for(int it = 0; it < 1; it++){

            for(int q = 0; q < col - 1; q++){
               // prob = Math.random();
                if(mask[k][q] == it){
                    for(int p = 0; p < nroClasses; p++)
                        refInstance[p][q] = emptyValue;
                }
            }


            for(int j = 0; j < col; j++){                            // atributo faltante � j
                flag = false;
                flag2 = false;
                for(int c = 0; c < nroClasses; c++) {          // encontra rede da classe da instacia que tem o atributo faltante
                    if(refInstance[c][j] == emptyValue){
                        intervalo = networksFull[c].retrieveIntervalExperimenting(refInstance[c], j, vetAtrHandler[j].getNumInterval());
                        if(testSet[k][j] > intervalo[0] && testSet[k][j] < intervalo[1]){ // valor original esta dentro do intervalo inferido
                            refInstance[c][j] = testSet[k][j];
                            flag2 = true;
                        }
                        else{
                            meioIntervalo = (intervalo[1] - intervalo[0])/2;
                            pontoMedio = intervalo[0] + meioIntervalo;
                            refInstance[c][j] = (rand.nextGaussian()*meioIntervalo + pontoMedio);     // valor aleat�rio dentro do intervalo
                            flag = true;
                        }

                        estimates[c] = networksFull[c].getSomaPesos();  // retorma valor do estimate, s, correspondente ao intervalo

                    }
                }
                if(flag)
                    contAltAtrTE++;

                if(flag2)
                    contMissAtrTE++;

                if(flag || flag2) {
                    maior = estimates[0];
                    for (int a = 1; a < nroClasses; a++) {
                        if (estimates[a] > maior)
                            indMaior = a;
                    }
                }

            }


            //     }

            // encontra distancias entre instancias inferidas e original / seleciona a de menor distancia


            for(int r = 0; r < col-1; r++)
                refMatriz[k][r] = refInstance[indMaior][r];
            refMatriz[k][col-1] = testSet[k][col-1];

        }


        atrAltRateTE += (contAltAtrTE/(double)(contAltAtrTE + contMissAtrTE));
        return refMatriz;

    }

    */

  public void exemploAbDG(double[][] matriz){

      int line = matriz.length;
      int coll = matriz[0].length;
      double[][] matrizTreino;
      double[][] matrizTeste;
      int fold = 10;
      int contTreino = 0, contTeste = 0, it = 1;
      int compAux = 0;
      double[] mainClassification, mainClassification1;
      double[] somaQuadClassification = new double[3];
      double[] somaClassification = new double[3];
      double[] desvio = new double[3];
      double[] mediaClass = new double[3];

      double[] somaQuadClassification1 = new double[12];
      double[] somaClassification1 = new double[12];
      double[] desvio1 = new double[12];
      double[] mediaClass1 = new double[12];
      double newDesvio = 0, newMediaClass = 0;
      double[] classDesvio = new double[2];
      double[][] oneClassTrain;
      // networks = new Networks[nroClasses];

      DecimalFormat show = new DecimalFormat("0.00");



              coll = matriz[0].length;

              particionaAtributo(matriz);

              int[] auxAtr = new int[vetAtrHandler.length];
              for(int o = 0; o < auxAtr.length; o++)
                  auxAtr[o] = 0;

              /* #### AbDG no exemplo do paper RuleAbDG - para o conjunto Iris #### */
        // atr 1
      vetAtrHandler[0].histogramPart(3);
     // vetAtrHandler[0].calculateIntervalGain();
      vetAtrHandler[0].intervalWeights();

        // atr2
      vetAtrHandler[1].histogramPart(2);
      //vetAtrHandler[1].calculateIntervalGain();
      vetAtrHandler[1].intervalWeights();

      vetAtrHandler[2].histogramPart(2);
      //vetAtrHandler[2].calculateIntervalGain();
      vetAtrHandler[2].intervalWeights();

      vetAtrHandler[3].histogramPart(4);
      //vetAtrHandler[3].calculateIntervalGain();
      vetAtrHandler[3].intervalWeights();



     networksFull = new NetworkFull(matriz, nroClasses, vetAtrHandler);
     networksFull.learnFullConection();
     int numAttr = coll - 1;
     int numEdges = numAttr + (numAttr*(numAttr-3))/2;

     networksFull.normalizeCorrelationsFullVersion();  // normaliza correlações de arestas

     normalizeIntervalGain(coll-1);




  }

    public double[][] compactaConjunto(double[][] A, int numEl){

        int coll = A[0].length;
        double[][] newA = new double[numEl][coll];

        for(int i = 0; i < numEl; i++)
          for(int j = 0; j < coll; j++)
             newA[i][j] = A[i][j];

        return newA;
    }



    public double[][] criaTreinamento(double[][] matriz, double classe){
        int cont = 0;
        int line = matriz.length;
        int coll = matriz[0].length;
        for(int i = 0; i < line; i++)
            if(matriz[i][coll-1] == classe)
                cont++;

        double[][] newE = new double[cont][coll];
        cont = 0;

        for(int i = 0; i < line; i++)
            if(matriz[i][coll-1] == classe){
                for(int j = 0; j < coll; j++)
                    newE[cont][j] = matriz[i][j];
                cont++;
            }

        return newE;
    }

    public void fileProbMissingTreinoeTeste(double[][] matriz, String FileName){      // CPP-AbDG

           int line = matriz.length;
           int coll = matriz[0].length;
       //    int noTXT = filename.length() - 4;
       //    String FileName = filename.substring(0, noTXT);
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
               // 'iris', 'teac', 'wine', 'vehi', 'wdpc', 'wdbc', 'habe', 'pima', 'wave', 'sona', 'hspe', 'bala',	'hear', 'live'    


               //     System.out.println("C:\\Users\\Jo�o\\Documents\\MATLAB\\simpleImputation\\CPPA\\EDAmedia\\" + FileName + "\\" + ((int)(probMiss[d]*100)+100) + "\\" +  cont +  "TE.txt");
                PrintWriter out1;
                FileOutputStream outputStream1 = null;
                try{
                    outputStream1 = new FileOutputStream("C:\\Users\\Jo�o\\Documents\\MATLAB\\simpleImputation\\CPPA\\FUSI5M\\" + FileName + "\\" + ((int)(probMiss[d]*100)+100) + "\\" +  cont +  "TE.txt");
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

                                ///   CPP-AbDG
                    matrizTreino = ImputacaoCppAbDG(matrizTreino);

                     PrintWriter out;
                  FileOutputStream outputStream = null;
                  try {
                      outputStream = new FileOutputStream("C:\\Users\\Jo�o\\Documents\\MATLAB\\simpleImputation\\CPPA\\FUSI5M\\" + FileName + "\\" + ((int)(probMiss[d]*100)+100) + "\\"+ cont + ".txt");
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

    public void fileProbMissingTreinoeTesteAbDG(double[][] matriz, String filename){   // AbDG

             int line = matriz.length;
             int coll = matriz[0].length;
          //   int noTXT = filename.length() - 4;
          //   String FileName = filename.substring(0, noTXT);
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
                       outputStream1 = new FileOutputStream("C:\\Users\\Jo�o\\Documents\\MATLAB\\simpleImputation\\ABDG\\AbMDLP5H\\" + filename + "\\" + ((int)(probMiss[d]*100)+100) + "\\"+ cont + "TE.txt");
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

                   // AbDG
                       matrizTreino = ImputacaoAbDG(matrizTreino);

                     PrintWriter out;
                     FileOutputStream outputStream = null;
                     try {
                         outputStream = new FileOutputStream("C:\\Users\\Jo�o\\Documents\\MATLAB\\simpleImputation\\ABDG\\AbMDLP5H\\" + filename + "\\" + ((int)(probMiss[d]*100)+100) + "\\"+ cont + ".txt");
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
                                if (Math.random() < 0.95)
                                   if(Math.random() < 0.5)
                                        matriz[i][0] = emptyValue;
                                   else
                                        matriz[i][1] = emptyValue;

                             }




       
                   /*       for (int i = 0; i < matriz.length; i++) {
                                   if (Math.random() < 0.8)
                                     for(int j = 0; j < coll- 1; j++)
                                         if(Math.random() < 0.3)
                                              matriz[i][j] = emptyValue;
                          }

                    */

                    //     matriz = normalizacaoMaiorMenorMissing(matriz);

                     // AbDG
                         matriz = ImputacaoAbDG(matriz);

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


    public double[][] ImputacaoCppAbDG(double[][] matriz){
         // m�todo em duas vias para imputa��o  - metodo cria matriz completa
          // matriz j� deve ter atributos faltantes

           double[][] oneClassTrain;
           int line = matriz.length;
           int coll = matriz[0].length;
          // int noTXT = fileName.length() - 4;
         //  String auxFileName = fileName.substring(0, noTXT);

           double[][] matrizImputed;
           double[][] matrizMean = new double[line][coll];

           int part10 = line / 10;
           int i1 = 0, i2 = 0;
           int[] validation = new int[10];
         //  networksFull = new NetworkFull[nroClasses];                     // ###############

           int atrOff, it = 5;
           double rand, prob;

      

           particionaAtributo(matriz);        //Imputa��o ########### matrizToInput  ##################

           int[] auxAtr = new int[vetAtrHandler.length];
           int contAtrZero = 0;

           for(int a = 0; a < coll - 1; a++){
               vetAtrHandler[a].MDLP();                  // FUSINTER(); //EDADB(nroClasses);//vetAtrHandler[a].MDLP();
               //   vetAtrHandler[a].optmizeIntervals();
               //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
               vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
               if(vetAtrHandler[a].getIntervalGain() == 0){
                   auxAtr[a] = 1;
                   contAtrZero++;
               }

               //  vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
               vetAtrHandler[a].intervalWeightsEntropy();
           }

           // atualiza matriz - retira atributos com ganho 0    -   n�o se aplica � imputa��o
      /*     if(contAtrZero != 0){
               matriz = retiraAtributos(auxAtr, contAtrZero, matriz);
               coll = matriz[0].length;
           }
       */
        //   for(int i = 0; i < nroClasses; i++) {   // anda em classes
             //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
          //     oneClassTrain = criaTreinamento(matriz,Classes[i]);                 // #####  matrizToInput ##############
               networksFull = new NetworkFull(matriz, nroClasses, vetAtrHandler);
               //networksFull[i].learnFullConection(vetAtrHandler);
               networksFull.learnFullConectionImputationPAPER();  // #####  matrizToInput ##############


           int numAttr = coll - 1;
           int numEdges = numAttr + numAttr*(numAttr-3)/2;

         //  printNetwork(coll-1);

        //   refineNetworkForImputation(matrizToImput);

           networksFull.normalizeCorrelationsFullVersion();

           normalizeIntervalGain(coll-1);


        //  matrizImputed = imputationFromIntervalForTrainingFull(matriz);
          matrizImputed = imputationFromIntervalForTrainingFullRandomOrder(matriz);                //imputationFromIntervalSameClass       imputationFromInteral


     for(int r = 0; r < it; r++){

            double[][] auxAtrClass;
            for(int a = 0; a < coll - 1; a++){
                auxAtrClass = criaNovoVetorClasseAtributo(matrizImputed,a);
                vetAtrHandler[a].intervalReWeightsImputation(auxAtrClass); // redefine pesos de vertices com base na matriz imputada
             }

          // ########## MOSTRA VALORES REAIS E IMPUTADOS ##############
      /*     double[] bounds;
           for(int i = 0; i < line; i++)
              for(int j = 0; j < coll; j++)
                if(matrizToImput[i][j] == emptyValue){
                   bounds = vetAtrHandler[j].getIntervalFromValue(matriz[i][j]);
                   System.out.println("valor real " + matriz[i][j] + " valor imputado " + matrizImputed[i][j]);
                 //  matrizImputed[i][coll - 1] = 10; // muda classe para vizualiza��o
                }
       */   //############################



      //    for(int i = 0; i < nroClasses; i++) {   // anda em classes
             //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
          //     oneClassTrain = criaTreinamento(matrizImputed,Classes[i]);                 // #####  matrizToInput ##############
               networksFull = new NetworkFull(matrizImputed, nroClasses, vetAtrHandler);
               networksFull.learnFullConection();
             //  networksFull[i].learnFullConectionImputation(vetAtrHandler, matrizImputed);  // #####  matrizToInput ##############
        //   }

             networksFull.normalizeCorrelationsFullVersion();

             normalizeIntervalGain(coll-1);



           //  matrizImputed = imputationFromIntervalForTrainingFull(matriz);  // pareceu melhor
            matrizImputed = imputationFromIntervalForTrainingFullRandomOrder(matriz);
         //    matrizImputed = imputationFromIntervalSameClass(matrizToImput);

          //          FIM TESTE

      /*    for(int i = 0; i < line; i++)
             for(int j = 0; j < coll-1; j++)
                matrizMean[i][j] += matrizImputed[i][j];
        */

    }

     /*      for(int i = 0; i < line; i++){
             for(int j = 0; j < coll-1; j++)
                matrizMean[i][j] /= (double)it;

                matrizMean[i][coll-1] = matriz[i][coll-1];
           }
     */
         return matrizImputed;

       }



    public void criaDominioImputadoCppAbDG(double[][] matriz, String fileName){        // metodo em duas vias para imputacao  - metodo cria matriz completa

        double[][] oneClassTrain;
        int line = matriz.length;
        int coll = matriz[0].length;
        int noTXT = fileName.length() - 4;
	    String auxFileName = fileName.substring(0, noTXT);

        double trainClassification = 0, testClassification = 0;
        double[][] matrizToImput;
        double[][] matrizMask;
        double[][] matrizImputed;
        int part10 = line / 10;
        int i1 = 0, i2 = 0;
        int contTreino = 0, contTeste = 0, it = 0, aux = 0;
        int[] validation = new int[10];
        //networksFull = new NetworkFull[nroClasses];                     // ###############

        int atrOff, cont = 0;
        double rand, prob;
        matrizToImput = new double[line][coll];      // cria copia
        matrizMask = new double[line][coll];        //  matriz usada para indicar onde foram feitas as imputa��es

       for(int f = 1; f <= 12; f++){
        prob = 0.1;

        while(prob < 0.6){


        for(int i = 0; i < line; i++){
            for(int j = 0; j < coll-1; j++){
                rand = Math.random();
                if(rand < prob)                                 // atributo escolhido para ter valor apagado
                    matrizToImput[i][j] = emptyValue;
                else
                    matrizToImput[i][j] = matriz[i][j];
            }
              matrizToImput[i][coll-1] = matriz[i][coll-1];
        }


        particionaAtributo(matrizToImput);        //Imputa��o ########### matrizToInput  ##################

        int[] auxAtr = new int[vetAtrHandler.length];
        int contAtrZero = 0;

        for(int a = 0; a < coll - 1; a++){
            vetAtrHandler[a].EDADB(nroClasses);         // vetAtrHandler[a].MDLP();
            //   vetAtrHandler[a].optmizeIntervals();
            //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
            vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
            if(vetAtrHandler[a].getIntervalGain() == 0){
                auxAtr[a] = 1;
                contAtrZero++;
            }

            //  vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
            vetAtrHandler[a].intervalWeightsEntropy();
        }

        // atualiza matriz - retira atributos com ganho 0    -   n�o se aplica � imputa��o      
   /*     if(contAtrZero != 0){
            matriz = retiraAtributos(auxAtr, contAtrZero, matriz);
            coll = matriz[0].length;
        }
    */
    //    for(int i = 0; i < nroClasses; i++) {   // anda em classes
          //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
    //        oneClassTrain = criaTreinamento(matrizToImput,Classes[i]);                 // #####  matrizToInput ##############
            networksFull = new NetworkFull(matrizToImput, nroClasses, vetAtrHandler);
            //networksFull[i].learnFullConection(vetAtrHandler);
            networksFull.learnFullConectionImputationPAPER();  // #####  matrizToInput ##############
        }


        int numAttr = coll - 1;
        int numEdges = numAttr + numAttr*(numAttr-3)/2;

      //  printNetwork(coll-1);

     //   refineNetworkForImputation(matrizToImput);    

        networksFull.normalizeCorrelationsFullVersion();

        normalizeIntervalGain(coll-1);

        //   trainClassification += classificationAccuracy(matrizTreino);
        //      testClassification += classificationAccuracy(matrizTeste);

       matrizImputed = imputationFromIntervalForTrainingFull(matrizToImput);
     //  matrizImputed = imputationFromIntervalSameClass(matrizToImput);                //imputationFromIntervalSameClass       imputationFromInteral


         double[][] auxAtrClass;
         for(int a = 0; a < coll - 1; a++){
             auxAtrClass = criaNovoVetorClasseAtributo(matrizImputed,a);
             vetAtrHandler[a].intervalReWeightsImputation(auxAtrClass); // redefine pesos de vertices com base na matriz imputada
          }

       // ########## MOSTRA VALORES REAIS E IMPUTADOS ##############
   /*     double[] bounds;
        for(int i = 0; i < line; i++)
           for(int j = 0; j < coll; j++)
             if(matrizToImput[i][j] == emptyValue){
                bounds = vetAtrHandler[j].getIntervalFromValue(matriz[i][j]);
                System.out.println("valor real " + matriz[i][j] + " valor imputado " + matrizImputed[i][j]);
              //  matrizImputed[i][coll - 1] = 10; // muda classe para vizualiza��o
             }
    */   //############################



    //   for(int i = 0; i < nroClasses; i++) {   // anda em classes
          //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
      //      oneClassTrain = criaTreinamento(matrizImputed,Classes[i]);                 // #####  matrizToInput ##############
            networksFull = new NetworkFull(matrizImputed, nroClasses, vetAtrHandler);
            networksFull.learnFullConection();
          //  networksFull[i].learnFullConectionImputation(vetAtrHandler, matrizImputed);  // #####  matrizToInput ##############
     //   }

          networksFull.normalizeCorrelationsFullVersion();

          normalizeIntervalGain(coll-1);

        //   trainClassification += classificationAccuracy(matrizTreino);
        //      testClassification += classificationAccuracy(matrizTeste);

          matrizImputed = imputationFromIntervalForTrainingFull(matrizToImput);  // pareceu melhor
      //    matrizImputed = imputationFromIntervalSameClass(matrizToImput);

       //          FIM TESTE


       PrintWriter out;
       FileOutputStream outputStream = null;
       try {
          outputStream = new FileOutputStream ("C:\\Users\\Jo�o\\Documents\\MATLAB\\imputation\\CAbDG\\" + auxFileName + "\\" + (int)(prob*100) + "\\" + f +  ".txt");
           } catch ( java.io.IOException e) {
          System.out.println("Could not create result.txt");
      }

       out = new PrintWriter(outputStream);

         for(int i = 0; i < line; i++){
           for(int j = 0; j < coll; j++)
                 out.print(matrizImputed[i][j] + " ");

          out.println();
         }
       out.close();

        prob += 0.1;
    }

    }





    public void twoWayImputationAbDGClassifierFull(double[][] matriz){  // metodo interno para lidar com imputacao (CpP-AbDG) - tnnls

        double[][] oneClassTrain;
        int line = matriz.length;
        int coll = matriz[0].length;


        double[][] matrizTreino;
        double[][] matrizTeste;
        int fold = 10;

        double[] mainClassification;
        double[] somaQuadClassification = new double[11];
        double[] somaClassification = new double[11];
        double[] desvio = new double[11];
        double[] mediaClass = new double[11];

        double[][] matrizToImput;
        double[][] matrizImputed;
        double[][] matrizTesteImp;
        int part10 = line / 10;
        int i1 = 0, i2 = 0;
        int contTreino = 0, contTeste = 0, it = 0, aux = 0;
        int[] validation = new int[10];
       // networksFull = new NetworkFull[nroClasses];                     // ###############

        int atrOff, cont = 0;
        matrizToImput = new double[line][coll];      // cria copia
        DecimalFormat show = new DecimalFormat("0.00");
        double rand, prob = 0.0;
        int nItCV = 5;


        for(int ncv = 0; ncv < nItCV; ncv++) {
        // para experimentos com dominios sem atributos faltantes
        for(int i = 0; i < line; i++){
            for(int j = 0; j < coll-1; j++){
                rand = Math.random();
                if(rand < prob)                                 // atributo escolhido para ter valor apagado
                    matrizToImput[i][j] = emptyValue;
                else
                    matrizToImput[i][j] = matriz[i][j];
            }
              matrizToImput[i][coll-1] = matriz[i][coll-1];
        }


        // inicia cross-validation

      //  for(int ncv = 0; ncv < nItCV; ncv++) {

            it = 1;
            matrizToImput = shuffle(matrizToImput);  //##############################################################################
            int[] stratification = indicesEstratificados(matrizToImput,fold);


            while(it <= fold){

           //     networksFull = new NetworkFull[nroClasses];
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
                        for(int y = 0; y < coll; y++ ){
                            matrizTreino[contTreino][y] = matrizToImput[h][y];
                         //   matrizTRTRTR[contTreino][y] = matriz[h][y];
                         //   matrizNANANA[contTreino][y] = matrizToImput[h][y];
                        }
                        contTreino++;
                    }
                    else{
                        for(int y = 0; y < coll; y++ ){
                            matrizTeste[contTeste][y] = matrizToImput[h][y];
                         //   matrizTETETE[contTeste][y] = matriz[h][y];
                         //   matrizNENENE[contTeste][y] = matrizToImput[h][y];
                        }
                        contTeste++;
                    }
                it++;



                particionaAtributo(matrizTreino);        //Imputa��o ########### matrizToInput  ##################

                int[] auxAtr = new int[vetAtrHandler.length];
                int contAtrZero = 0;

                for(int a = 0; a < coll - 1; a++){
                    if(attributeType[a] == 'n')         // atributo real - numerico
                       vetAtrHandler[a].histogramPart(3);// vetAtrHandler[a].MDLP();// vetAtrHandler[a].EDADB(nroClasses); // vetAtrHandler[a].EDADB(nroClasses);  // //   // vetAtrHandler[a].EDADB(nroClasses);    //        // usa metodo EDA-DB para obter intervalos
                    else
                       vetAtrHandler[a].Categorical();

                    //vetAtrHandler[a].MDLP();        //.EDADB(nroClasses);
                    //   vetAtrHandler[a].optmizeIntervals();
                    //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                    vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
                    if(vetAtrHandler[a].getIntervalGain() == 0){
                        auxAtr[a] = 1;
                        contAtrZero++;
                    }

                     vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos - vertices
                  //  vetAtrHandler[a].intervalWeightsEntropy();
                }



                // N�o tinha nesta vers�o %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   FULL   %%%%%%%%%%%%%%%%

              if(contAtrZero != 0 && contAtrZero != (coll-1)){
                   matrizTreino = retiraAtributos(auxAtr, contAtrZero, matrizTreino);
                   matrizTeste = retiraAtributosTeste(auxAtr, contAtrZero, matrizTeste);
                   coll = matrizTreino[0].length;
               }

              //#############################################


           //     for(int i = 0; i < nroClasses; i++) {   // anda em classes
                    //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
           //         oneClassTrain = criaTreinamento(matrizTreino,Classes[i]);                 // #####  matrizToInput ##############
                    networksFull = new NetworkFull(matrizTreino, nroClasses, vetAtrHandler);
                    //networksFull[i].learnFullConection(vetAtrHandler);
                    networksFull.learnFullConectionImputationPAPER();  // #####  matrizToInput ##############
            //    }


                int numAttr = coll - 1;
                int numEdges = numAttr + numAttr*(numAttr-3)/2;

                //  printNetwork(coll-1);
                //   refineNetworkForImputation(matrizToImput);

                networksFull.normalizeCorrelationsFullVersion();

                normalizeIntervalGain(coll-1);
                //   trainClassification += classificationAccuracy(matrizTreino);
                //    testClassification += classificationAccuracy(matrizTeste);

                matrizImputed = imputationFromIntervalForTrainingFull(matrizTreino);      // realiza imputa��o - no caso basta definir um intervalo
                //  matrizImputed = imputationFromIntervalSameClass(matrizToImput);                //imputationFromIntervalSameClass       imputationFromInteral


                double[][] auxAtrClass;
                for(int a = 0; a < coll - 1; a++){

                    auxAtrClass = criaNovoVetorClasseAtributo(matrizImputed,a);
                    vetAtrHandler[a].intervalReWeightsImputation(auxAtrClass); // redefine pesos de vertices com base na matriz imputada

                }


    //#########3########## MOSTRA VALORES REAIS E IMPUTADOS ##############
   /*
                double[] bounds;
                int contaCorreto = 0, contaNA = 0;
                for(int i = 0; i < matrizTreino.length; i++)
                    for(int j = 0; j < coll; j++)
                        if(matrizNANANA[i][j] == emptyValue){
                            bounds = vetAtrHandler[j].getIntervalFromValue(matrizTRTRTR[i][j]);
                            System.out.println("Intervalo " + bounds[0] + " " + bounds[1] + " valor real " + matrizTRTRTR[i][j] + " valor imputado " + matrizImputed[i][j]);
                            if(matrizImputed[i][j] > bounds[0] && matrizImputed[i][j] < bounds[1] )
                                  contaCorreto++;
                            contaNA++;
                        }
                      System.out.println(" acertou " + contaCorreto + " de " + contaNA + " " + (contaCorreto/(double)contaNA)*100 + "%" );
       // ###3###########################################
     */




       //         for(int i = 0; i < nroClasses; i++) {   // anda em classes
                    //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
         //           oneClassTrain = criaTreinamento(matrizImputed,Classes[i]);                 // #####  matrizToInput ##############
                    networksFull = new NetworkFull(matrizImputed, nroClasses, vetAtrHandler);
                    networksFull.learnFullConection();
                    //  networksFull[i].learnFullConectionImputation(vetAtrHandler, matrizImputed);  // #####  matrizToInput ##############
           //     }

                networksFull.normalizeCorrelationsFullVersion();

                normalizeIntervalGain(coll-1);




                 //#########3########## MOSTRA VALORES REAIS E IMPUTADOS ##############
     /*
                double[] bounds1;
                int contaCorreto1 = 0, contaNA1 = 0;
                for(int i = 0; i < matrizTeste.length; i++)
                    for(int j = 0; j < coll; j++)
                        if(matrizNENENE[i][j] == emptyValue){
                            bounds1 = vetAtrHandler[j].getIntervalFromValue(matrizTETETE[i][j]);
                            System.out.println("Intervalo " + bounds1[0] + " " + bounds1[1] + " valor real " + matrizTETETE[i][j] + " valor imputado " + matrizTesteImp[i][j]);
                            if(matrizTesteImp[i][j] > bounds1[0] && matrizTesteImp[i][j] < bounds1[1] )
                                  contaCorreto1++;
                            contaNA1++;
                        }
                      System.out.println(" acertou " + contaCorreto1 + " de " + contaNA1 + " " + (contaCorreto1/(double)contaNA1)*100 + "%" );
       // ###3###########################################
      */


                // mainClassification = classificationAccuracyTest(matrizTeste);



                mainClassification = DnoClassifierFullforMissingAttrValues(matrizTeste);
              //  mainClassification = DnoClassifierFull(matrizTeste);         // varios classificadores para teste

                for(int i = 0; i < mainClassification.length; i++){
                    //   somaQuadClassification[i] += Math.pow(mainClassification[i],2);
                    //   somaClassification[i] += mainClassification[i];

                    somaQuadClassification[i] += Math.pow(mainClassification[i],2);
                    somaClassification[i] += mainClassification[i];

                }

                networksFull = null;
                vetAtrHandler = null;


            } // fim do while it < 10        -- cross validation --

        }

        //  System.out.println();
        /*     for(int i = 0; i < desvio.length; i++){
                desvio[i] = Math.sqrt((somaQuadClassification[i] - (Math.pow(somaClassification[i],2)/100))/99);
                mediaClass[i] = somaClassification[i]/100;
                System.out.println(mediaClass[i] + "   " + desvio[i]);
            }
        */

       double den = 10*nItCV;
       double den1 = 10*nItCV-1;

        for(int i = 0; i < desvio.length; i++){
            desvio[i] = Math.sqrt((somaQuadClassification[i] - (Math.pow(somaClassification[i],2)/den))/den1);
            mediaClass[i] = somaClassification[i]/(10*nItCV);
            System.out.println(show.format(i*0.1) + "  " + show.format(mediaClass[i]) + "   " + show.format(desvio[i]));
        }


    }

    public double[][] ImputacaoAbDG(double[][] matriz){        // m�todo em duas vias para imputa��o  - metodo cria matriz completa

           double[][] oneClassTrain;
           int line = matriz.length;
           int coll = matriz[0].length;
          
           double trainClassification = 0, testClassification = 0;
           double[][] matrizImputed;
           networks = new Networks[nroClasses];                     // ###############
           int it = 5;
      
           particionaAtributo(matriz);        //Imputa��o ########### matrizToInput  ##################

           int[] auxAtr = new int[vetAtrHandler.length];
           int contAtrZero = 0;

           for(int a = 0; a < coll - 1; a++){
               vetAtrHandler[a].FUSINTER(); //  MDLP();           // vetAtrHandler[a].MDLP();
               //   vetAtrHandler[a].optmizeIntervals();
               //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
               vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
               if(vetAtrHandler[a].getIntervalGain() == 0){
                   auxAtr[a] = 1;
                   contAtrZero++;
               }

               //  vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
               vetAtrHandler[a].intervalWeightsEntropy();
           }

           // atualiza matriz - retira atributos com ganho 0    -   n�o se aplica � imputa��o
      /*     if(contAtrZero != 0){
               matriz = retiraAtributos(auxAtr, contAtrZero, matriz);
               coll = matriz[0].length;
           }
       */
           for(int i = 0; i < nroClasses; i++) {   // anda em classes
             //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
               oneClassTrain = criaTreinamento(matriz,Classes[i]);                 // #####  matrizToInput ##############
               networks[i] = new Networks(oneClassTrain, Classes[i], matriz.length);
               //networksFull[i].learnFullConection(vetAtrHandler);
               networks[i].learnConectionImputationPAPER(vetAtrHandler, matriz);  // #####  matrizToInput ##############
           }


           normalizeCorrelations(coll-1);
           normalizeIntervalGain(coll-1);

           matrizImputed = imputationFromIntervalForTraining(matriz);


        for(int r = 0; r < it; r++){

            double[][] auxAtrClass;
            for(int a = 0; a < coll - 1; a++){
                auxAtrClass = criaNovoVetorClasseAtributo(matrizImputed,a);
                vetAtrHandler[a].intervalReWeightsImputation(auxAtrClass); // redefine pesos de vertices com base na matriz imputada
             }


          for(int i = 0; i < nroClasses; i++) {   // anda em classes
             //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
               oneClassTrain = criaTreinamento(matrizImputed,Classes[i]);                 // #####  matrizToInput ##############
               networks[i] = new Networks(oneClassTrain, Classes[i], matriz.length);
               networks[i].learnPAPER(vetAtrHandler);
             //  networksFull[i].learnFullConectionImputation(vetAtrHandler, matrizImputed);  // #####  matrizToInput ##############
           }

            normalizeCorrelations(coll-1);
            normalizeIntervalGain(coll-1);

            matrizImputed = imputationFromIntervalForTraining(matriz);  // pareceu melhor
        }

         return matrizImputed;

       }



    public void criaDominioImputadoAbDG(double[][] matriz, String fileName){        // m�todo em duas vias para imputa��o  - metodo cria matriz completa

        double[][] oneClassTrain;
        int line = matriz.length;
        int coll = matriz[0].length;
        int noTXT = fileName.length() - 4;
	    String auxFileName = fileName.substring(0, noTXT);

        double trainClassification = 0, testClassification = 0;
        double[][] matrizToImput;
        double[][] matrizMask;
        double[][] matrizImputed;
        int part10 = line / 10;
        int i1 = 0, i2 = 0;
        int contTreino = 0, contTeste = 0, it = 0, aux = 0;
        int[] validation = new int[10];
        networks = new Networks[nroClasses];                     // ###############

        int atrOff, cont = 0;
        double rand, prob;
        matrizToImput = new double[line][coll];      // cria copia
        matrizMask = new double[line][coll];        //  matriz usada para indicar onde foram feitas as imputa��es

       for(int f = 1; f <= 12; f++){
        prob = 0.1;

        while(prob < 0.6){


       for(int i = 0; i < line; i++){
           for(int j = 0; j < coll-1; j++){
                rand = Math.random();
                if(rand < prob)                                 // atributo escolhido para ter valor apagado
                    matrizToImput[i][j] = emptyValue;
                else
                    matrizToImput[i][j] = matriz[i][j];
            }
              matrizToImput[i][coll-1] = matriz[i][coll-1];
        }

        particionaAtributo(matrizToImput);        //Imputa��o ########### matrizToInput  ##################

        int[] auxAtr = new int[vetAtrHandler.length];
        int contAtrZero = 0;

        for(int a = 0; a < coll - 1; a++){
            vetAtrHandler[a].EDADB(nroClasses);         // vetAtrHandler[a].MDLP();
            //   vetAtrHandler[a].optmizeIntervals();
            //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
            vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
            if(vetAtrHandler[a].getIntervalGain() == 0){
                auxAtr[a] = 1;
                contAtrZero++;
            }

            //  vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
            vetAtrHandler[a].intervalWeightsEntropy();
        }

        // atualiza matriz - retira atributos com ganho 0    -   n�o se aplica � imputa��o
   /*     if(contAtrZero != 0){
            matriz = retiraAtributos(auxAtr, contAtrZero, matriz);
            coll = matriz[0].length;
        }
    */
        for(int i = 0; i < nroClasses; i++) {   // anda em classes
          //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
            oneClassTrain = criaTreinamento(matrizToImput,Classes[i]);                 // #####  matrizToInput ##############
            networks[i] = new Networks(oneClassTrain, Classes[i], matriz.length);
            //networksFull[i].learnFullConection(vetAtrHandler);
            networks[i].learnConectionImputationPAPER(vetAtrHandler, matrizToImput);  // #####  matrizToInput ##############
        }


        normalizeCorrelations(coll-1);
        normalizeIntervalGain(coll-1);

        matrizImputed = imputationFromIntervalForTraining(matrizToImput);


         double[][] auxAtrClass;
         for(int a = 0; a < coll - 1; a++){
             auxAtrClass = criaNovoVetorClasseAtributo(matrizImputed,a);
             vetAtrHandler[a].intervalReWeightsImputation(auxAtrClass); // redefine pesos de vertices com base na matriz imputada
          }


       for(int i = 0; i < nroClasses; i++) {   // anda em classes
          //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
            oneClassTrain = criaTreinamento(matrizImputed,Classes[i]);                 // #####  matrizToInput ##############
            networks[i] = new Networks(oneClassTrain, Classes[i], matriz.length);
            networks[i].learnPAPER(vetAtrHandler);
          //  networksFull[i].learnFullConectionImputation(vetAtrHandler, matrizImputed);  // #####  matrizToInput ##############
        }

         normalizeCorrelations(coll-1);
         normalizeIntervalGain(coll-1);

         matrizImputed = imputationFromIntervalForTraining(matrizToImput);  // pareceu melhor


       PrintWriter out;
       FileOutputStream outputStream = null;
       try {
          outputStream = new FileOutputStream ("C:\\Users\\Jo�o\\Documents\\MATLAB\\imputation\\pAbDG\\" + auxFileName + "\\" + (int)(prob*100) + "\\" + f +  ".txt");
           } catch ( java.io.IOException e) {
          System.out.println("Could not create result.txt");
      }

       out = new PrintWriter(outputStream);

         for(int i = 0; i < line; i++){
           for(int j = 0; j < coll; j++)
                 out.print(matrizImputed[i][j] + " ");

          out.println();
         }
       out.close();

        prob += 0.1;
    }

    }

    }


    public void twoWayImputationAbDGClassifier(double[][] matriz){  // metodo interno para lidar com imputa��o (AbDG) - tnnls

        double[][] oneClassTrain;
        int line = matriz.length;
        int coll = matriz[0].length;

        double[][] matrizTreino;
        double[][] matrizTeste;
        int fold = 10;

        double[] mainClassification;
        double[] somaQuadClassification = new double[11];
        double[] somaClassification = new double[11];
        double[] desvio = new double[11];
        double[] mediaClass = new double[11];

        double[][] matrizToImput;
        double[][] matrizImputed;
        double[][] matrizTesteImp;
        int part10 = line / 10;
        int i1 = 0, i2 = 0;
        int contTreino = 0, contTeste = 0, it = 0, aux = 0;
        int[] validation = new int[10];
        networks = new Networks[nroClasses];
        int atrOff, cont = 0;
        matrizToImput = new double[line][coll];      // cria copia
        DecimalFormat show = new DecimalFormat("0.00");
        double rand, prob = 0.0;                                   // ###############
        int nItCV = 10;


       for(int ncv = 0; ncv < nItCV; ncv++) {
           
        // para experimentos com dominios sem atributos faltantes
        for(int i = 0; i < line; i++){
            for(int j = 0; j < coll-1; j++){
                rand = Math.random();
                if(rand < prob)                                 // atributo escolhido para ter valor apagado
                    matrizToImput[i][j] = emptyValue;
                else
                    matrizToImput[i][j] = matriz[i][j];
            }
              matrizToImput[i][coll-1] = matriz[i][coll-1];
        }


        // inicia cross-validation
   //    for(int ncv = 0; ncv < nItCV; ncv++) {


            it = 1;
            matrizToImput = shuffle(matrizToImput);
            int[] stratification = indicesEstratificados(matrizToImput,fold);


            while(it <= fold){

                networks = new Networks[nroClasses];
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
                            matrizTreino[contTreino][y] = matrizToImput[h][y];  //  matrizTreino[contTreino][y] = matriz[h][y];
                        contTreino++;
                    }
                    else{
                        for(int y = 0; y < coll; y++ )
                            matrizTeste[contTeste][y] = matrizToImput[h][y];
                        contTeste++;
                    }
                it++;


                particionaAtributo(matrizTreino);        //Imputa��o ########### matrizToInput  ##################

                int[] auxAtr = new int[vetAtrHandler.length];
                int[] ordem;
                int contAtrZero = 0;

                for(int a = 0; a < coll - 1; a++){
                   if(attributeType[a] == 'n')         // atributo real - numerico
                       vetAtrHandler[a].MDLP();  // .histogramPart(11); // //.EDADB(nroClasses);   //  //   ;usa metodo EDA-DB para obter intervalos
                   else
                       vetAtrHandler[a].Categorical();

                    vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
                    if(vetAtrHandler[a].getIntervalGain() == 0){
                        auxAtr[a] = 1;
                        contAtrZero++;
                    }

                    vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos - vertices

                }


                if(contAtrZero != 0 && contAtrZero != (coll-1)){
                                 matrizTreino = retiraAtributos(auxAtr, contAtrZero, matrizTreino);
                                 matrizTeste = retiraAtributosTeste(auxAtr, contAtrZero, matrizTeste);
                                 coll = matrizTreino[0].length;
                 }


                 ordem = ordenaVetorDeAtributos(coll);                // ordena atributos por ganho de informa��o
                 matrizTreino = ordenaAtributosPorGanho(matrizTreino,ordem);
                 matrizTeste = ordenaAtributosPorGanho(matrizTeste,ordem);


                for(int i = 0; i < nroClasses; i++) {   // anda em classes

                    oneClassTrain = criaTreinamento(matrizTreino,Classes[i]);                 // #####  matrizToInput ##############
                    networks[i] = new Networks(oneClassTrain, Classes[i], matrizTreino.length);
                    networks[i].learnConectionImputationPAPER(vetAtrHandler, matrizTreino);  // #####  matrizToInput ##############
                }


               normalizeCorrelations(coll-1);
               normalizeIntervalGain(coll-1);

               matrizTreino = removeFullyEmptyInstances(matrizTreino);
               matrizImputed = imputationFromIntervalForTraining(matrizTreino);      // realiza imputa��o - no caso basta definir um intervalo


                double[][] auxAtrClass;
                for(int a = 0; a < coll - 1; a++){

                    auxAtrClass = criaNovoVetorClasseAtributo(matrizImputed,a);
                    vetAtrHandler[a].intervalReWeightsImputation(auxAtrClass); // redefine pesos de vertices com base na matriz imputada

                }


                for(int i = 0; i < nroClasses; i++) {   // anda em classes
                    //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
                    oneClassTrain = criaTreinamento(matrizImputed,Classes[i]);                 // #####  matrizToInput ##############
                    networks[i] = new Networks(oneClassTrain, Classes[i], matrizImputed.length);
                    networks[i].learnPAPER(vetAtrHandler);
                    //  networksFull[i].learnFullConectionImputation(vetAtrHandler, matrizImputed);  // #####  matrizToInput ##############
                }


               normalizeCorrelations(coll-1);
               normalizeIntervalGain(coll-1);

               matrizTeste = removeFullyEmptyInstances(matrizTeste);

               mainClassification = DnoClassifierforMissingAttrValues(matrizTeste);


            //    matrizTesteImp = imputationFromIntervalForTesting(matrizTeste);

             //   mainClassification = DnoClassifier(matrizTesteImp);         // varios classificadores para teste


                for(int i = 0; i < mainClassification.length; i++){
                    //   somaQuadClassification[i] += Math.pow(mainClassification[i],2);
                    //   somaClassification[i] += mainClassification[i];

                    somaQuadClassification[i] += Math.pow(mainClassification[i],2);
                    somaClassification[i] += mainClassification[i];

                }

                networksFull = null;
                vetAtrHandler = null;


            } // fim do while it < 10        -- cross validation --

        }

        //  System.out.println();
        /*     for(int i = 0; i < desvio.length; i++){
                desvio[i] = Math.sqrt((somaQuadClassification[i] - (Math.pow(somaClassification[i],2)/100))/99);
                mediaClass[i] = somaClassification[i]/100;
                System.out.println(mediaClass[i] + "   " + desvio[i]);
            }
        */



       double den = 10*nItCV;
       double den1 = 10*nItCV-1;

        for(int i = 0; i < desvio.length; i++){
            desvio[i] = Math.sqrt((somaQuadClassification[i] - (Math.pow(somaClassification[i],2)/den))/den1);
            mediaClass[i] = somaClassification[i]/(10*nItCV);
            System.out.println(show.format(i*0.1) + "  " + show.format(mediaClass[i]) + "   " + show.format(desvio[i]));
        }


    }

      public void criaDominioRefinadoCppAbDG(double[][] matriz, String fileName, int itCv, int noise) {
          // valores de atributos das instancias classificadas erradas s�o consideradas para altera��o
          // se classifica��o continua errada, uma nova classe � atribuida

          double[][] oneClassTrain;
          int line = matriz.length;
          int coll = matriz[0].length;
          double[][] matrizRefined = new double[line][coll];//   int lineW = wrongs.length;


          int noTXT = fileName.length() - 4;
          String auxFileName = fileName.substring(0, noTXT);

          double[] wrongsClassification;
          double[][] matrizToImput;
          double[][] matrizMask;
          double[][] matrizImputed = new double[line][coll];
          int[][] refMaskMat;
          int contCorretos = 0;
         // networksFull = new NetworkFull[nroClasses];                     // ###############

          int atrOff, cont = 0;
          double rand;

          //  matrizToImput = new double[line][coll];      // cria copia
          matrizMask = new double[line][coll];        //  matriz usada para indicar onde foram feitas as imputa��es
          int numAttr = 0;
          int numEdges = 0;
          //   while(prob < 0.6){

          //     for(int t = 0; t < 1; t++){   // numero de repeti�oes


   /*       for (int i = 0; i < line; i++) {
              for (int j = 0; j < coll - 1; j++) {
                  // atrOff = (int) (Math.random()*(line));         //      ############################################
                  rand = Math.random();
                  if (rand < prob) {                                 // atributo escolhido para ter valor apagado
                      // atrOff = (int) (Math.random()*(coll-1));
                      matrizMask[i][j] = matriz[i][j];
                      matriz[i][j] = emptyValue;
                      cont++;
                  } else
                      matrizMask[i][j] = -100; // valor presente
              }
          }
*/
          //   if(t == 0){

          int rep = 10;
          double cutW = 0.2;

          refMaskMat = criaMatrizRefineAll(rep, line, coll-1);


          for (int r = 0; r < rep; r++){

            matrizToImput = retiraValoresMask(matriz,refMaskMat,r+1);


          particionaAtributo(matrizToImput);        //Imputação ########### matrizToInput  ##################

          int[] auxAtr = new int[vetAtrHandler.length];
          int contAtrZero = 0;

          for (int a = 0; a < coll - 1; a++) {
              vetAtrHandler[a].MDLP();// vetAtrHandler[a].histogramPart(5);//          //vetAtrHandler[a].EDADB(nroClasses);
              //   vetAtrHandler[a].optmizeIntervals();
              //  vetAtrHandler[a].histogramPart(5);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
              vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
              if (vetAtrHandler[a].getIntervalGain() == 0) {
                  auxAtr[a] = 1;
                  contAtrZero++;
              }

              vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
              vetAtrHandler[a].intervalWeightsRefining(cutW);         // calcula pesos dos intervalos
              // vetAtrHandler[a].intervalWeightsEntropy();
          }


       //   for (int i = 0; i < nroClasses; i++) {   // anda em classes
              //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
           //   oneClassTrain = criaTreinamento(matrizToImput, Classes[i]);                 // #####  matrizToInput ##############
              networksFull = new NetworkFull(matrizToImput, nroClasses, vetAtrHandler);
              //networksFull[i].learnFullConection(vetAtrHandler);
              networksFull.learnFullConection();  // #####  matrizToInput ##############
         // }


          numAttr = coll - 1;
          numEdges = numAttr + numAttr * (numAttr - 3) / 2;

          //  printNetwork(coll-1);

          //   refineNetworkForImputation(matrizToImput);

          networksFull.normalizeCorrelationsFullVersion();
          zeraCorrelacoesAbaixoThs(numEdges, cutW);
          networksFull.normalizeCorrelationsFullVersion();


          matrizImputed = cleansingFromIntervalForTrainingFullGaussian(matriz, refMaskMat ,r+1);

              // copia em matrizRefined somente os valores de atributos que foram analisados
              for(int j = 0; j < line; j++)
                  for(int k = 0; k < coll - 1; k++)
                      if(refMaskMat[j][k] == r + 1)
                           matrizRefined[j][k] = matrizImputed[j][k];

      }

          for(int j = 0; j < line; j++)
            //  for(int k = 0; k < coll; k++)
                  matrizRefined[j][coll - 1] = matrizImputed[j][coll - 1]; //(double)rep;




       PrintWriter out;
       FileOutputStream outputStream = null;
       try {
          outputStream = new FileOutputStream ("C:\\Users\\João\\Documents\\MATLAB\\CleanseOne\\"  + auxFileName + "\\" +  noise + "\\ML\\" + auxFileName + itCv + ".txt");
           } catch ( java.io.IOException e) {
          System.out.println("Could not create result.txt");
      }

       out = new PrintWriter(outputStream);



         for(int i = 0; i < line; i++){
           for(int j = 0; j < coll; j++)
                 out.print(matrizRefined[i][j] + " ");   //
             out.println();
         }


     
       out.close();

       

     //   prob += 0.1;
 //  }



     //   System.out.println();

        // System.out.println("treno " + trainClassification/10);
    //    System.out.println("teste " + testClassification);


    }

    public void criaDominioRefinadoCppAbDGMultiGenMode(double[][] matriz, String fileName, int itCv, int noise, int ncv) {
        // valores de atributos das instancias classificadas erradas s�o consideradas para altera��o
        // se classifica��o continua errada, uma nova classe � atribuida

        double[][] oneClassTrain;
        int line = matriz.length;
        int coll = matriz[0].length;
        double[][] matrizRefined = new double[line][coll];//   int lineW = wrongs.length;


        int noTXT = fileName.length() - 4;
        String auxFileName = fileName.substring(0, noTXT);

        double[] wrongsClassification;
        double[][] matrizToImput;
        double[][] matrizMask;
        double[][] matrizImputed = new double[line][coll];
        int[][] refMaskMat;
        int contCorretos = 0;
       // networksFull = new NetworkFull[nroClasses];                     // ###############

        int atrOff, cont = 0;
        double rand;

        //  matrizToImput = new double[line][coll];      // cria copia
        matrizMask = new double[line][coll];        //  matriz usada para indicar onde foram feitas as imputa��es
        int numAttr = 0;
        int numEdges = 0;


        int rep = 10;
        int repMode = 10;
        double[][][] matrizMode = new double[line][coll][repMode];
        double cutW = 0.0;

        for(int rm = 0; rm < repMode; rm++){

        refMaskMat = criaMatrizRefineAll(rep, line, coll - 1);


        for (int r = 0; r < rep; r++) {

            matrizToImput = retiraValoresMask(matriz, refMaskMat, r + 1);


            particionaAtributo(matrizToImput);        //Imputação ########### matrizToInput  ##################

            int[] auxAtr = new int[vetAtrHandler.length];
            int contAtrZero = 0;

            for (int a = 0; a < coll - 1; a++) {
                vetAtrHandler[a].MDLP();// vetAtrHandler[a].histogramPart(5);//          //vetAtrHandler[a].EDADB(nroClasses);
                //   vetAtrHandler[a].optmizeIntervals();
                //  vetAtrHandler[a].histogramPart(5);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
                if (vetAtrHandler[a].getIntervalGain() == 0) {
                    auxAtr[a] = 1;
                    contAtrZero++;
                }

                vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
                vetAtrHandler[a].intervalWeightsRefining(cutW);         // calcula pesos dos intervalos
                // vetAtrHandler[a].intervalWeightsEntropy();
            }


         //   for (int i = 0; i < nroClasses; i++) {   // anda em classes
                //  oneClassTrain = criaTreinamento(matriz,Classes[i]);
          //      oneClassTrain = criaTreinamento(matrizToImput, Classes[i]);                 // #####  matrizToInput ##############
                networksFull = new NetworkFull(matrizToImput, nroClasses, vetAtrHandler);
                //networksFull[i].learnFullConection(vetAtrHandler);
                networksFull.learnFullConection();  // #####  matrizToInput ##############
         //   }


            numAttr = coll - 1;
            numEdges = numAttr + numAttr * (numAttr - 3) / 2;

            //  printNetwork(coll-1);

            //   refineNetworkForImputation(matrizToImput);

            networksFull.normalizeCorrelationsFullVersion();
            zeraCorrelacoesAbaixoThs(numEdges, cutW);
            networksFull.normalizeCorrelationsFullVersion();


            matrizImputed = cleansingFromIntervalForTrainingFullGaussian(matriz, refMaskMat, r + 1);

            // copia em matrizRefined somente os valores de atributos que foram analisados
            for (int j = 0; j < line; j++) {
                for (int k = 0; k < coll - 1; k++) {
                    if (refMaskMat[j][k] == (r + 1))            // copia os valores que foram considerados na iteração
                        matrizMode[j][k][rm] = matrizImputed[j][k];   // cada iteração produz um conjunto de dados, colocado na dimensão rm
                }
            matrizMode[j][coll-1][rm] = matrizImputed[j][coll-1];
            }
        }



       // for (int j = 0; j < line; j++)
            //  for(int k = 0; k < coll; k++)
       //     matrizRefined[j][coll - 1] = matrizImputed[j][coll - 1]; //(double)rep;


    }

        matrizRefined = condenceByMode(matrizMode,line,coll,repMode);

        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream ("C:\\Users\\João\\Documents\\MATLAB\\CleanseMulti\\"  + auxFileName + "\\" +  noise + "\\ML\\" + auxFileName + itCv + ncv+ ".txt");
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");
        }

        out = new PrintWriter(outputStream);



        for(int i = 0; i < line; i++){
            for(int j = 0; j < coll; j++)
                out.print(matrizRefined[i][j] + " ");   //
            out.println();
        }



        out.close();



    }


    public double[][] condenceByMode(double[][][] M, int len, int col, int dimZ){


        double classe = 0;
        double[] intervalo;
        int[] intCount; // conta quantos elementos em cada intervalo
        int maior = 0;
        int indMaior = 0;
        double[][] condencedMatriz = new double[len][col];

        for(int i = 0; i < len; i++){
            for(int j = 0; j < col-1; j++) {                           // atributo faltante � j
                                      // procura por atributo faltante
                    classe = M[i][col - 1][0];
                    //contMissAtr++;
                //    for (int c = 0; c < nroClasses; c++) {          // encontra intervalo modal
                      //  if (classe == networksFull[c].getClasse()) {
                            intervalo = vetAtrHandler[j].getVetAtr(); //networksFull[c].getIntervalTest(j);//networksFull[c].retrieveIntervalExperimenting(M[i][j], j, vetAtrHandler[j].getNumInterval());
                            intCount = new int[intervalo.length];
                            for(int k = 0; k < dimZ; k++) {
                                for (int l = 0; l < intervalo.length - 1; l++)
                                    if (l == intervalo.length - 2) {
                                        if (M[i][j][k] >= intervalo[l] && M[i][j][k] <= intervalo[l + 1])
                                            intCount[l]++;
                                    }
                                    else{
                                        if (M[i][j][k] >= intervalo[l] && M[i][j][k] < intervalo[l + 1])
                                            intCount[l]++;
                                    }

                            }
                                maior = 0;
                                for(int m = 0; m < intervalo.length; m++)
                                    if(intCount[m] > maior){
                                        maior = intCount[m];
                                        indMaior = m;
                                    }

                                 for(int h = 0; h < dimZ; h++)
                                     if(M[i][j][h] >= intervalo[indMaior] && M[i][j][h] < intervalo[indMaior+1]) {
                                         condencedMatriz[i][j] = M[i][j][h];// intervalo[indMaior] + Math.random()*(intervalo[indMaior+1] - intervalo[indMaior]);
                                         break;
                                     }
                                if(condencedMatriz[i][j] < 0)
                                    System.out.println();
                 //  }

              //  }

            }
            condencedMatriz[i][col-1] = classe;
        }

        return condencedMatriz;

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public int[][] criaMatrizRefineAll(int gran, int line, int coll){ // cria matriz que será usada como mascara no processo de refinamento
                                                    // atribui valores de 1 a gran que serão refinados na iteração correspondente

        int[][] mask = new int[line][coll];

        for(int i = 0; i < line; i++)
            for(int j = 0; j < coll; j++)
                mask[i][j] = (int)(1 + Math.random()*gran);


        return mask;
    }

    public double[][] retiraValoresMask(double[][] matrizOr, int[][] mask, int ept){

        int line = matrizOr.length;
        int coll = matrizOr[0].length;
        double[][] newMat = new double[line][coll];

        for(int i = 0; i < line; i++) {
            for (int j = 0; j < coll - 1; j++) {
                if (mask[i][j] == ept)
                    newMat[i][j] = emptyValue;
                else
                    newMat[i][j] = matrizOr[i][j];
            }
        newMat[i][coll-1] = matrizOr[i][coll-1];
        }

        return newMat;
    }

   public double[][] criaNovoVetorClasseAtributo(double[][] Mimp, int atr ){

       int line = Mimp.length;
       int coll = Mimp[0].length;
       double[][] newAtrClass = new double[line][2];

       for(int i = 0; i < line; i++){
          newAtrClass[i][0] = Mimp[i][atr]; // atributo
          newAtrClass[i][1] = Mimp[i][coll-1]; // classe  
       }

       return newAtrClass;

    }

           
      public void trainDNO(double[][] matriz){

               int line = matriz.length;
               int coll = matriz[0].length;
               double[][] oneClassTrain;
            //   networksFull = new NetworkFull[nroClasses];

               particionaAtributo(matriz);

               int[] auxAtr = new int[vetAtrHandler.length];
               int contAtrZero = 0;

               for(int a = 0; a < coll - 1; a++){
                   vetAtrHandler[a].MDLP();
                   //   vetAtrHandler[a].optmizeIntervals();
                   //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                   vetAtrHandler[a].calculateIntervalGain();      // calcula ganho de informa��o de intervalo atual
                   if(vetAtrHandler[a].getIntervalGain() == 0){
                       auxAtr[a] = 1;
                       contAtrZero++;
                   }

                   //  vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
                   vetAtrHandler[a].intervalWeightsEntropy();
               }

               // atualiza matriz - retira atributos com ganho 0    -   n�o se aplica � imputa��o
  /*            if(contAtrZero != 0){
                   matriz = retiraAtributos(auxAtr, contAtrZero, matriz);
                   coll = matriz[0].length;
               }
   */
               for(int i = 0; i < nroClasses; i++) {
                   oneClassTrain = criaTreinamento(matriz,Classes[i]);
                  if(oneClassTrain.length > 0){
                   networks[i] = new Networks(oneClassTrain, Classes[i], matriz.length);
                   networks[i].learn(vetAtrHandler);
                  }
                   else
                    System.out.println("uma classe");
               }


                  normalizeCorrelations(coll-1);
                  normalizeIntervalGain(coll-1);


    }

      public void trainFullDNO(double[][] matriz){

               int line = matriz.length;
               int coll = matriz[0].length;
               double[][] oneClassTrain;
             //  networksFull = new NetworkFull[nroClasses];

               particionaAtributo(matriz);

               int[] auxAtr = new int[vetAtrHandler.length];
               int contAtrZero = 0;

               for(int a = 0; a < coll - 1; a++){
                   vetAtrHandler[a].MDLP();
                   //   vetAtrHandler[a].optmizeIntervals();
                   //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                   vetAtrHandler[a].calculateIntervalGain();      // calcula ganho de informa��o de intervalo atual
                   if(vetAtrHandler[a].getIntervalGain() == 0){
                       auxAtr[a] = 1;
                       contAtrZero++;
                   }

                   //  vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
                   vetAtrHandler[a].intervalWeightsEntropy();
               }

               // atualiza matriz - retira atributos com ganho 0    -   n�o se aplica � imputa��o
  /*            if(contAtrZero != 0){
                   matriz = retiraAtributos(auxAtr, contAtrZero, matriz);
                   coll = matriz[0].length;
               }
   */
            //   for(int i = 0; i < nroClasses; i++) {
              //     oneClassTrain = criaTreinamento(matriz,Classes[i]);
              //    if(oneClassTrain.length > 0){
                   networksFull = new NetworkFull(matriz, nroClasses, vetAtrHandler);
                   networksFull.learnFullConection();
               //   }
               //    else
               //     System.out.println("uma classe");
              // }

               int numAttr = coll - 2;
               int numEdges = numAttr + numAttr*(numAttr-3)/2;

             //  printNetwork(coll-1);

               networksFull.normalizeCorrelationsFullVersion();

    }

     public double[][] imputationFromIntervalSameClass(double[][] MFVA){ // matriz com valores de atributos faltantes
                                          // utiliza informaçao de classe para encontra intervalo
                                          // atribui a media dos valores pertencentes ao intervalo, no treino, como valor imputado 
        int len = MFVA.length;
        int col = MFVA[0].length;
        double[][] impMatriz = new double[len][col];
        double classe = 0, soma = 0, cont = 0, somaTodos = 0, contTodos = 0;
        double[] intervalo;


         for(int i = 0; i < len; i++)
             for(int j = 0; j < col; j++)                            // atributo faltante � j
                 if(MFVA[i][j] == emptyValue){                       // procura por atributo faltante
                     classe = MFVA[i][col-1];
                     intervalo = networksFull.retrieveInterval(MFVA[i], j, vetAtrHandler[j].getNumInterval(),(int)classe);
                     soma = 0;
                     cont = 0;
                     somaTodos = 0;
                     contTodos = 0;
                     for(int h = 0; h < len; h++){
                         if(MFVA[h][j] >= intervalo[0] && MFVA[h][j] < intervalo[1]){
                             if(MFVA[h][col -1] == classe){          //  de mesma classe
                                 soma += MFVA[h][j];
                                 cont++;
                             }
                             else{                                   // de qualquer classe
                                 somaTodos += MFVA[h][j];
                                 contTodos++;
                             }
                         }
                     }
                     System.out.println(somaTodos / contTodos + " " + soma / cont);
                     if(cont == 0)
                         impMatriz[i][j] = somaTodos / contTodos;
                     else
                         impMatriz[i][j] = soma / cont;

                 }
                 else{
                     impMatriz[i][j] = MFVA[i][j];
                 }

      return impMatriz;

    }

     public double[][] classifierImputationFromIntervalForTrainingFull(double[][] MFVA, double classe){ // matriz com valores de atributos faltantes e classe para estimar intervalos
                                           // encontra o intervalo mais provavel usando informa��o de possivel classe
                                           // atribui ponto medio do intervalo como valor imputado
        int len = MFVA.length;
        int col = MFVA[0].length;
        double sizeInt = 0;
        double[][] impMatriz = new double[len][col];
        double[][] MFVAcopy = new double[len][col];
        double[] intervalo;


         // copia MFVA em MFVAcopy para permitir o uso de valores imputados na imputa��o de outros
         for(int k = 0; k < len; k++)
            for(int q = 0; q < col; q++)
               MFVAcopy[k][q] = MFVA[k][q];


        for(int i = 0; i < len; i++)
           for(int j = 0; j < col; j++)                            // atributo faltante � j
              if(MFVAcopy[i][j] == emptyValue){                       // procura por atributo faltante
                 sizeInt = 0;
                         // encontra rede da classe da instacia que tem o atributo faltante
                     intervalo = networksFull.retrieveIntervalExperimenting(MFVAcopy[i], j, vetAtrHandler[j].getNumInterval(),(int)classe);
                     sizeInt = intervalo[1] - intervalo[0];
                     impMatriz[i][j] = intervalo[0] + sizeInt/2;
                     MFVAcopy[i][j] = impMatriz[i][j];  // para usar valor imputado no caso onde ocorre mais que um '?' em uma instancia
                          // coloca no meio do intervalo - intevalo[0] menor; intervalo[1] maior.


              }
        else{
              impMatriz[i][j] = MFVAcopy[i][j];
              }

      return impMatriz;

    }

      public double[][] imputationFromIntervalForTrainingFull(double[][] MFVA){ // matriz com valores de atributos faltantes
                                           // encontra o intervalo mais provavel usando informa��o de classe
                                           // atribui ponto medio do intervalo como valor imputado
        int len = MFVA.length;
        int col = MFVA[0].length;
        double sizeInt = 0;
        double[][] impMatriz = new double[len][col];
        double classe = 0;
        double[] intervalo;
        double rand, soma, cont;


        for(int i = 0; i < len; i++)
           for(int j = 0; j < col; j++)                            // atributo faltante � j
              if(MFVA[i][j] == emptyValue){                       // procura por atributo faltante
                 classe = MFVA[i][col-1];
                 sizeInt = 0; 
                         intervalo = networksFull.retrieveIntervalExperimenting(MFVA[i], j, vetAtrHandler[j].getNumInterval(),(int)classe);

                           // atribui��o randomica
                          //rand = Math.random();
                          //impMatriz[i][j] = (rand*(intervalo[1] - intervalo[0]) + intervalo[0]);   // atribui valor aleat�rio no intervalo

                         // atribui��o de ponto medio
                         // sizeInt = intervalo[1] - intervalo[0];
                         // impMatriz[i][j] = intervalo[0] + sizeInt/2;              // atribui ponto m�dio do intervalo

                         // atribui��o da m�dia do intervalo
                         soma = cont = 0;
                         for(int k = 0; k < len; k++)
                             if(MFVA[k][j] != emptyValue && MFVA[k][j] >= intervalo[0] && MFVA[k][j] <= intervalo[1]){
                                 soma += MFVA[k][j];
                                 cont++;
                             }
                         if(cont == 0)
                             System.out.println("Nao ha valores no intervalo");
                         impMatriz[i][j] = soma/cont;


                         MFVA[i][j] = impMatriz[i][j];  // para usar valor imputado no caso onde ocorre mais que um '?' em uma instancia
                          // coloca no meio do intervalo - intevalo[0] menor; intervalo[1] maior.


              }
        else{
              impMatriz[i][j] = MFVA[i][j];
              }

      return impMatriz;

    }

      public double[][] imputationFromIntervalForTrainingFullRandomOrder(double[][] MFVA){ // matriz com valores de atributos faltantes
                                           // encontra o intervalo mais provavel usando informa��o de classe
                                           // atribui ponto medio do intervalo como valor imputado
        int len = MFVA.length;
        int col = MFVA[0].length;
        double sizeInt = 0;
        double[][] impMatriz = new double[len][col];
        double classe = 0;
        double[] intervalo;
        double rand, soma, cont, contAll;
        int[] vetInd = new int[col];

        int somaCont = 0, somaContAll = 0, somaIt = 0;

       vetInd = shuffle(vetInd);


        for(int i = 0; i < len; i++)
           for(int j = 0; j < col; j++)                            // atributo faltante � j; anda de acordo com ordem aleatoria
              if(MFVA[i][vetInd[j]] == emptyValue){                       // procura por atributo faltante
                 classe = MFVA[i][col-1];
                 sizeInt = 0;
                           // encontra rede da classe da instacia que tem o atributo faltante

                          intervalo = networksFull.retrieveIntervalExperimenting(MFVA[i], vetInd[j], vetAtrHandler[vetInd[j]].getNumInterval(),(int)classe);
                     // atribuiçao da media do intervalo

                         soma = cont = contAll = 0;
                         for(int k = 0; k < len; k++)         // media dos valores dentro do intervalo pertencentes a mesma classe
                             if(MFVA[k][vetInd[j]] != emptyValue && MFVA[k][vetInd[j]] >= intervalo[0] && MFVA[k][vetInd[j]] <= intervalo[1] && MFVA[k][col-1] == classe){
                                 soma += MFVA[k][vetInd[j]];
                                 cont++;
                             }
                         if(cont == 0){
                             for(int k = 0; k < len; k++)       // media dos valores do intervalo - caso nenhuma instancia seja da mesma classe
                             if(MFVA[k][vetInd[j]] != emptyValue && MFVA[k][vetInd[j]] >= intervalo[0] && MFVA[k][vetInd[j]] <= intervalo[1]){
                                 soma += MFVA[k][vetInd[j]];
                                 cont++;
                                 contAll++;
                             }
                         }
                           else{
                             for(int k = 0; k < len; k++)       // media dos valores do intervalo - caso nenhuma instancia seja da mesma classe
                             if(MFVA[k][vetInd[j]] != emptyValue && MFVA[k][vetInd[j]] >= intervalo[0] && MFVA[k][vetInd[j]] <= intervalo[1]){//para depura��o
                             contAll++;
                             }
                         }

                         somaCont += cont;
                         somaContAll += contAll;
                         somaIt++;

                         System.out.println("Todos " + contAll + " Mesma classe " + cont);

                         impMatriz[i][vetInd[j]] = soma/cont;

                         MFVA[i][vetInd[j]] = impMatriz[i][vetInd[j]];  // para usar valor imputado no caso onde ocorre mais que um '?' em uma instancia
                          // coloca no meio do intervalo - intevalo[0] menor; intervalo[1] maior.


              }
        else{
              impMatriz[i][vetInd[j]] = MFVA[i][vetInd[j]];
              }

          System.out.println(" Media todos " + somaContAll/(double)somaIt + "Media mesma classe " + somaCont/(double)somaIt + " " + somaCont / (double)somaContAll);

      return impMatriz;

    }

       public double[][] imputationFromIntervalForTestingFull(double[][] MFVA){ // matriz com valores de atributos faltantes
                              // encontra o intervalo mais provavel considerando todas as classes
                             // selecionando o intervalo com maior soma dos pesos
                                // atribui ponto medio do intervalo como valor imputado

        int len = MFVA.length;
        int col = MFVA[0].length;
        double sizeInt = 0;
        double[][] impMatriz = new double[len][col];
        double somaPesos = 0, maiorSoma = 0;
        double[] intervalo;
        double aux, maior = 0;
        int indAux = 0;
        int maxSumClass = 0;
        double[] vetTeste = new double[nroClasses+1];

        for(int i = 0; i < len; i++)
           for(int j = 0; j < col; j++){                            // atributo faltante � j
              if(MFVA[i][j] == emptyValue){                       // procura por atributo faltante
                 sizeInt = 0;
                 for(int c = 1; c < nroClasses + 1; c++) {          // encontra rede da classe da instacia que tem o atributo faltante
                     somaPesos = networksFull.retrieveSumExperimenting(MFVA[i], j, vetAtrHandler[j].getNumInterval(),c); // retorna indice do intervalo
                     vetTeste[c] = somaPesos;
                     if(somaPesos > maiorSoma){
                         maiorSoma = somaPesos;
                         maxSumClass = c;
                     }
                }
                  int cont = 0;
                  for(int k = 0; k < nroClasses; k++)
                     if(vetTeste[k] == maiorSoma)
                        cont++;
                     if(cont > 1){
                         for(int k = 0; k < nroClasses; k++)
                                  if(vetTeste[k] == maiorSoma){
                                       aux = vetAtrHandler[j].getHighestWeightofClass(k);
                                     if(aux > maior){
                                        maior = aux;
                                        indAux = k;
                                     }
                                  }
                         intervalo = vetAtrHandler[j].getInterval(vetAtrHandler[j].getIntFromHighestWeightofClass(indAux));
                     }
                     else
                         intervalo = networksFull.getIntervalTest(j,maxSumClass);

                  sizeInt = intervalo[1] - intervalo[0];
                impMatriz[i][j] = intervalo[0] + sizeInt/2;   // coloca no meio do intervalo - intevalo[0] menor; intervalo[1] maior.
                MFVA[i][j] = impMatriz[i][j];
              }
        else{
              impMatriz[i][j] = MFVA[i][j];
              }

      }

      return impMatriz;

    }


    public double[][] imputationFromIntervalForTraining(double[][] MFVA){ // matriz com valores de atributos faltantes
        // encontra o intervalo mais provavel usando informa��o de classe
        // atribui ponto medio do intervalo como valor imputado
        int len = MFVA.length;
        int col = MFVA[0].length;
        double sizeInt = 0;
        double[][] impMatriz = new double[len][col];
        double classe = 0;
        double[] intervalo;
        boolean hasNeighbor = true, stillEmpty = false;
        int i = 0, j = 0;
        double soma, cont, contAll, somaAll, somaAllQuad, somaQuad;
        double media, quadSoma, quadSomaAll, desvio;
        Random rand = new Random();

        while(i < len){      // anda nas linhas
            j = 0;
            stillEmpty = false;
       //     System.out.println();
            while(j < col){
             //   System.out.println(MFVA[i][j] + " ");
                                                    // atributo faltante � j
                if(MFVA[i][j] == emptyValue){                       // procura por atributo faltante
                    if(j == 0){
                        if(MFVA[i][j+1] == emptyValue)
                            hasNeighbor = false;
                    }
                    else if (j == col-1){
                            if(MFVA[i][j-1] == emptyValue)
                                hasNeighbor = false;
                    }
                    else
                    if(MFVA[i][j-1] == emptyValue && MFVA[i][j+1] == emptyValue)
                        hasNeighbor = false;

                    if(hasNeighbor){
                        classe = MFVA[i][col-1];
                        sizeInt = 0;
                        for(int c = 0; c < nroClasses; c++) {          // encontra rede da classe da instacia que tem o atributo faltante
                            if(classe == networks[c].getClasse()){
                                intervalo = networks[c].retrieveIntervalExperimenting(MFVA[i], j, vetAtrHandler[j].getNumInterval());

                                // atribui��o aleat�ria
                             //    rand = Math.random();
                             //   impMatriz[i][j] = (rand*(intervalo[1] - intervalo[0]) + intervalo[0]);

                                // atribui��o do ponto m�dio
                                //sizeInt = intervalo[1] - intervalo[0];
                                //impMatriz[i][j] = intervalo[0] + sizeInt/2;

                                // atribui��o da m�dia dos valores do intervalo



                 /*
                         soma = cont = contAll = 0;
                         for(int k = 0; k < len; k++)         // media dos valores dentro do intervalo pertencentes a mesma classe
                             if(MFVA[k][j] != emptyValue && MFVA[k][j] >= intervalo[0] && MFVA[k][j] <= intervalo[1] && MFVA[k][col-1] == classe){
                                 soma += MFVA[k][j];
                                 cont++;
                             }
                         if(cont == 0){
                             for(int k = 0; k < len; k++)       // media dos valores do intervalo - caso nenhuma instancia seja da mesma classe
                             if(MFVA[k][j] != emptyValue && MFVA[k][j] >= intervalo[0] && MFVA[k][j] <= intervalo[1]){
                                 soma += MFVA[k][j];
                                 cont++;

                             }
                         }
                   */


                                //TENTAR A MEDIA DOS K VIZINHOS QUE COMPARTILHAM MESMO INTERVALO




               /*             // heuristica  usada no paper
                     soma = cont = somaAll = contAll = 0;
                         for(int k = 0; k < len; k++){         // media dos valores dentro do intervalo pertencentes a mesma classe
                             if(MFVA[k][j] != emptyValue && MFVA[k][j] >= intervalo[0] && MFVA[k][j] <= intervalo[1] && MFVA[k][col-1] == classe){
                                 soma += MFVA[k][j];
                                 cont++;
                                 contAll++;
                                 somaAll += MFVA[k][j];
                             }
                             else if(MFVA[k][j] != emptyValue && MFVA[k][j] >= intervalo[0] && MFVA[k][j] <= intervalo[1]){
                                 somaAll += MFVA[k][j];
                                 contAll++;
                             }
                         }

                         if((cont/contAll) > 0.6){
                             impMatriz[i][j] = soma/cont;    // usa media de el. da mesma classe
                         }
                         else
                             impMatriz[i][j] = somaAll/contAll;    // usa media dos el. do intervalo



                 */
                                //ALIADO A HEURISTICA, USAR A MEDIA E O DESVIO PARA DEFINIR UMA GAUSSIANA USADA PARA INFERIR VALORES NO ITERVALO

                              quadSoma = media = desvio = somaAllQuad = somaQuad = soma = cont = somaAll = contAll = 0;
                                for(int k = 0; k < len; k++){         // media dos valores dentro do intervalo pertencentes a mesma classe
                                    if(MFVA[k][j] != emptyValue && MFVA[k][j] >= intervalo[0] && MFVA[k][j] <= intervalo[1] && MFVA[k][col-1] == classe){
                                        soma += MFVA[k][j];
                                        somaQuad += Math.pow(MFVA[k][j],2);   // soma dos quadrados
                                        cont++;
                                        contAll++;
                                        somaAll += MFVA[k][j];
                                        somaAllQuad += Math.pow(MFVA[k][j],2);
                                    }
                                    else if(MFVA[k][j] != emptyValue && MFVA[k][j] >= intervalo[0] && MFVA[k][j] <= intervalo[1]){
                                        somaAll += MFVA[k][j];
                                        somaAllQuad += Math.pow(MFVA[k][j],2);
                                        contAll++;
                                    }
                                }

                                if((cont/contAll) > 0.6){
                                    media  = soma/cont;
                                    quadSoma = Math.pow(soma,2);
                                    desvio = Math.sqrt((somaQuad - (1/cont)*quadSoma)/(cont - 1));

                                    impMatriz[i][j] =  rand.nextGaussian()*(desvio/2) + media;    // usa media de el. da mesma classe
                                }
                                else{
                                    media  = somaAll/contAll;
                                    quadSoma = Math.pow(somaAll,2);
                                    desvio = Math.sqrt((somaAllQuad - (1/contAll)*quadSoma) /(contAll - 1));

                                    impMatriz[i][j] =  rand.nextGaussian()*(desvio/2) + media;    // usa media de el. da mesma classe

                                }

                  
                                MFVA[i][j] = impMatriz[i][j];

                            }

                        }

                    }


                    if(!hasNeighbor){
                        hasNeighbor = true;
                        stillEmpty = true;
                    }
                }

                else{
                    impMatriz[i][j] = MFVA[i][j];
                }

               j++;
            }
            if(!stillEmpty)
                i++;

    //        System.out.println(" " + i);
    //        for(int c = 0; c < col; c++)
    //           System.out.print(MFVA[i-1][c] + " ");
       }

        return impMatriz;

    }

     public double[][] classifierImputationFromIntervalForTraining(double[][] MFVA, double classe){ // matriz com valores de atributos faltantes
        // encontra o intervalo mais provavel usando informa��o de classe
        // atribui ponto medio do intervalo como valor imputado
        int len = MFVA.length;
        int col = MFVA[0].length;
        double sizeInt = 0;
        double[][] impMatriz = new double[len][col];
        double[][] MFVAcopy = new double[len][col];
        double[] intervalo;
        boolean hasNeighbor = true, stillEmpty = false;
        int i = 0, j = 0;


          for(int k = 0; k < len; k++)
            for(int q = 0; q < col; q++)
               MFVAcopy[k][q] = MFVA[k][q];



        while(i < len){      // anda nas linhas
            j = 0;
            stillEmpty = false;
       //     System.out.println();
            while(j < col){
             //   System.out.println(MFVA[i][j] + " ");
                                                    // atributo faltante � j
                if(MFVAcopy[i][j] == emptyValue){                       // procura por atributo faltante
                    if(j == 0){
                        if(MFVAcopy[i][j+1] == emptyValue)
                            hasNeighbor = false;
                    }
                    else if (j == col-1){
                            if(MFVAcopy[i][j-1] == emptyValue)
                                hasNeighbor = false;
                    }
                    else
                    if(MFVAcopy[i][j-1] == emptyValue && MFVAcopy[i][j+1] == emptyValue)
                        hasNeighbor = false;

                    if(hasNeighbor){
                       sizeInt = 0;
                        for(int c = 0; c < nroClasses; c++) {          // encontra rede da classe da instacia que tem o atributo faltante
                            if(classe == networks[c].getClasse()){
                                intervalo = networks[c].retrieveIntervalExperimenting(MFVAcopy[i], j, vetAtrHandler[j].getNumInterval());
                                sizeInt = intervalo[1] - intervalo[0];
                                impMatriz[i][j] = intervalo[0] + sizeInt/2;
                                MFVAcopy[i][j] = impMatriz[i][j];  // para usar valor imputado no caso onde ocorre mais que um '?' em uma instancia
                                // coloca no meio do intervalo - intevalo[0] menor; intervalo[1] maior.
                            }
                        }
                    }


                    if(!hasNeighbor){
                        hasNeighbor = true;
                        stillEmpty = true;
                    }
                }

                else{
                    impMatriz[i][j] = MFVAcopy[i][j];
                }

               j++;
            }
            if(!stillEmpty)
                i++;
        }

        return impMatriz;

    }

    public double[][] imputationFromIntervalForTesting(double[][] MFVA){ // matriz com valores de atributos faltantes
        // encontra o intervalo mais provavel considerando todas as classes
        // selecionando o intervalo com maior soma dos pesos
        // atribui ponto medio do intervalo como valor imputado

        int len = MFVA.length;
        int col = MFVA[0].length;
        double sizeInt = 0;
        double[][] impMatriz = new double[len][col];
        double somaPesos = 0, maiorSoma = 0;
        double[] intervalo;
        double aux, maior = 0;
        int indAux = 0;
        int maxSumClass = 0;
        double[] vetTeste = new double[nroClasses];
        boolean hasNeighbor = true, stillEmpty = false;
        int i = 0, j = 0;

        while(i < len){      // anda nas linhas
            j = 0;
            stillEmpty = false;
            while(j < col){                            // atributo faltante � j

                if(MFVA[i][j] == emptyValue){                       // procura por atributo faltante
                    if(j == 0){
                        if(MFVA[i][j+1] == emptyValue)
                            hasNeighbor = false;
                    }
                    else if (j == col-1){
                            if(MFVA[i][j-1] == emptyValue)
                                hasNeighbor = false;
                    }
                    else
                    if(MFVA[i][j-1] == emptyValue && MFVA[i][j+1] == emptyValue)
                        hasNeighbor = false;



                    if(hasNeighbor){
                        if(MFVA[i][j] == emptyValue){                       // procura por atributo faltante
                            sizeInt = 0;
                            for(int c = 0; c < nroClasses; c++) {          // encontra rede da classe da instacia que tem o atributo faltante
                                somaPesos = networks[c].retrieveSum(MFVA[i], j, vetAtrHandler[j].getNumInterval()); // retorna indice do intervalo
                                vetTeste[c] = somaPesos;
                                if(somaPesos > maiorSoma){
                                    maiorSoma = somaPesos;
                                    maxSumClass = c;
                                }
                            }
                            int cont = 0;
                            for(int k = 0; k < nroClasses; k++)
                                if(vetTeste[k] == maiorSoma)
                                    cont++;

                            if(cont > 1){    // trata empate
                               for(int k = 0; k < nroClasses; k++)
                                  if(vetTeste[k] == maiorSoma){
                                       aux = vetAtrHandler[j].getHighestWeightofClass(k);
                                     if(aux > maior){
                                        maior = aux;
                                        indAux = k;
                                     }
                                  }
                                  intervalo = vetAtrHandler[j].getInterval(vetAtrHandler[j].getIntFromHighestWeightofClass(indAux));
                            }
                            else
                               intervalo = networks[maxSumClass].getIntervalTest(j);

                            sizeInt = intervalo[1] - intervalo[0];
                            impMatriz[i][j] = intervalo[0] + sizeInt/2;   // coloca no meio do intervalo - intevalo[0] menor; intervalo[1] maior.
                            MFVA[i][j] = impMatriz[i][j];
                        }
                    }
                    if(!hasNeighbor){
                        hasNeighbor = true;
                        stillEmpty = true;
                    }
                }

                else{
                    impMatriz[i][j] = MFVA[i][j];
                }

                j++;
            }
            if(!stillEmpty)
                i++;
        }

        return impMatriz;

    }

    public double[][] removeFullyEmptyInstances(double[][] A){

        int line = A.length;
        int coll = A[0].length;
        int contEmpty, contNonEmpty = line;
        int[] markEmpty = new int[line]; 
        int cont = 0;


        // conta linhas vazias
        for(int i = 0; i < line; i++){
           contEmpty = 0;
           for(int j = 0; j < coll-1; j++)
              if(A[i][j] == emptyValue)
                contEmpty++;
              else
                 break;
           if(contEmpty == coll-1){
              markEmpty[i] = -1;
              contNonEmpty--;
           }
        }

        double[][] newA = new double[contNonEmpty][coll];
        for(int i = 0; i < line; i++)
           if(markEmpty[i] != -1){
              for(int j = 0; j < coll; j++)
                newA[cont][j] = A[i][j];
            cont++;
           }

        return newA;

     }


     public double[][] cleansingFromIntervalForTrainingFull(double[][] MFVA, double[][] mask){ // matriz com valores de atributos faltantes
        // encontra o intervalo mais provavel usando informações de classe e demais atributos
        // se novo valor pertence ao mesmo intervalo, valor antigo é mantido senão atribui-se um ponto aleatório do intervalo como novo valor

        int len = MFVA.length;
        int col = MFVA[0].length;
        double sizeInt = 0;
        double[][] impMatriz = new double[len][col];
        double classe = 0;
        double[] intervalo;
        double rand;


        for(int i = 0; i < len; i++)
           for(int j = 0; j < col; j++)                            // atributo faltante � j
              if(MFVA[i][j] == emptyValue){                       // procura por atributo faltante
                 classe = MFVA[i][col-1];
                 contMissAtr++;
                 sizeInt = 0;
                 intervalo = networksFull.retrieveIntervalExperimenting(MFVA[i], j, vetAtrHandler[j].getNumInterval(),(int)classe);
                  if(mask[i][j] != -100 && mask[i][j] > intervalo[0] && mask[i][j] < intervalo[1])
                      impMatriz[i][j] = mask[i][j];
                  else{
                      rand = Math.random();
                      impMatriz[i][j] = (rand*(intervalo[1] - intervalo[0]) + intervalo[0]);     // valor aleat�rio dentro do intervalo
                      contAltAtrTR++;
                  }
                  MFVA[i][j] = impMatriz[i][j];  // para usar valor imputado no caso onde ocorre mais que um '?' em uma instancia
                  // coloca no meio do intervalo - intevalo[0] menor; intervalo[1] maior.


              }
        else{
              impMatriz[i][j] = MFVA[i][j];
              }

      atrAltRate += contAltAtrTR/(double)contMissAtr;

      return impMatriz;

    }

    public double[][] cleansingFromIntervalForTrainingFullGaussian(double[][] MFVA, double[][] mask){ // matriz com valores de atributos faltantes
        // encontra o intervalo mais provavel usando informações de classe e demais atributos
        // se novo valor pertence ao mesmo intervalo, valor antigo é mantido senão atribui-se um ponto aleatório do intervalo como novo valor

        int len = MFVA.length;
        int col = MFVA[0].length;
        double sizeInt = 0;
        double[][] impMatriz = new double[len][col];
        double classe = 0;
        double[] intervalo;
        double quadSoma, media, desvio, somaAllQuad, somaQuad, soma, cont, somaAll, contAll;
        double meioIntervalo, pontoMedio;
        Random rand = new Random();


        for(int i = 0; i < len; i++){
            for(int j = 0; j < col; j++) {                           // atributo faltante � j
                if (MFVA[i][j] == emptyValue) {                       // procura por atributo faltante
                    classe = MFVA[i][col - 1];
                    contMissAtr++;
                    sizeInt = 0;

                    intervalo = networksFull.retrieveIntervalExperimenting(MFVA[i], j, vetAtrHandler[j].getNumInterval(),(int)classe);
                    if (mask[i][j] != -100 && mask[i][j] >= intervalo[0] && mask[i][j] < intervalo[1])
                        impMatriz[i][j] = mask[i][j];
                    else {

                        meioIntervalo = (intervalo[1] - intervalo[0]) / 2;
                        pontoMedio = intervalo[0] + meioIntervalo;
                        impMatriz[i][j] = (rand.nextGaussian() * meioIntervalo + pontoMedio);     // valor aleat�rio dentro do intervalo

                        MFVA[i][j] = impMatriz[i][j];


                        contAltAtrTR++;
                    }
                    //   MFVA[i][j] = impMatriz[i][j];  // para usar valor imputado no caso onde ocorre mais que um '?' em uma instancia
                    // coloca no meio do intervalo - intevalo[0] menor; intervalo[1] maior.


                } else {
                    impMatriz[i][j] = MFVA[i][j];
                }

            }
            impMatriz[i][col-1] = MFVA[i][col-1];
        }
        atrAltRate += contAltAtrTR/(double)contMissAtr;

                System.out.println(atrAltRate);
        return impMatriz;

    }

    public double[][] cleansingFromIntervalForTrainingFullGaussian(double[][] MFVA, int[][] mask, int it){
        // mask tem os indices cujos valores deverao ser inferidos

        int len = MFVA.length;
        int col = MFVA[0].length;
        double sizeInt = 0;
        double[][] impMatriz = new double[len][col];
        double classe = 0;
        double[] intervalo;
        double quadSoma, media, desvio, somaAllQuad, somaQuad, soma, cont, somaAll, contAll;
        double meioIntervalo, pontoMedio;
        Random rand = new Random();



        for(int i = 0; i < len; i++){
            for(int j = 0; j < col-1; j++) {                           // atributo faltante � j
                if (mask[i][j] == it) {                       // procura por atributo faltante
                    classe = MFVA[i][col - 1];
                    contMissAtr++;
                    sizeInt = 0;

                    intervalo = networksFull.retrieveIntervalExperimenting(MFVA[i], j, vetAtrHandler[j].getNumInterval(),(int)classe);
                    if (MFVA[i][j] >= intervalo[0] && MFVA[i][j] < intervalo[1])
                        impMatriz[i][j] = MFVA[i][j];
                    else {

                        meioIntervalo = (intervalo[1] - intervalo[0]) / 2;
                        pontoMedio = intervalo[0] + meioIntervalo;
                        impMatriz[i][j] = intervalo[0] + Math.random()*(intervalo[1] - intervalo[0]); // pontoMedio;  (rand.nextGaussian() * meioIntervalo + pontoMedio);     // valor aleat�rio dentro do intervalo

                        //  MFVA[i][j] = impMatriz[i][j];


                        contAltAtrTR++;
                    }
                    //   MFVA[i][j] = impMatriz[i][j];  // para usar valor imputado no caso onde ocorre mais que um '?' em uma instancia
                    // coloca no meio do intervalo - intevalo[0] menor; intervalo[1] maior.


                } else {
                    impMatriz[i][j] = MFVA[i][j];
                }

            }
            impMatriz[i][col-1] = MFVA[i][col-1];
        }
        atrAltRate += contAltAtrTR/(double)contMissAtr;

        System.out.println(atrAltRate);
        return impMatriz;

    }


    public void crossValidation(double[][] matriz){

        double[][] oneClassTrain;
        int line = matriz.length;
        int coll = matriz[0].length;
        double trainClassification = 0, testClassification = 0;
        double[][] matrizTreino;
        double[][] matrizTeste;
        int part10 = line / 10;
        int i1 = 0, i2 = 0;
        int contTreino = 0, contTeste = 0, it = 0, aux = 0;
        int[] validation = new int[10];
        networks = new Networks[nroClasses];


        for(int v = 0; v < 10; v++)
            validation[v] = part10;

        int restante = line - part10 * 10;

        while(restante > 0){
            validation[aux]++;
            restante--;
            aux++;
        }

        matriz = shuffle(matriz);

        while(it < 10){

            networks = new Networks[nroClasses];
            coll = matriz[0].length;
            contTreino = 0;
            contTeste = 0;
            i1 = i2;
            i2 = i2 + validation[it];
//            System.out.println("i1 " + i1 + "i2 " + i2 );

            if(it == 9)
                i2 = line;

            matrizTreino = new double[line - (i2 - i1)][coll];
            matrizTeste = new double[i2 - i1][coll];

            //  ########## matriz --  matrizBias #########
            for(int h = 0; h < line; h++)
                if( h < i1 || h >= i2 ){
                    for(int y = 0; y < coll; y++ )
                        matrizTreino[contTreino][y] = matriz[h][y];
                    contTreino++;
                }
                else{
                    for(int y = 0; y < coll; y++ )
                        matrizTeste[contTeste][y] = matriz[h][y];
                    contTeste++;
                }
            it++;

            particionaAtributo(matrizTreino);

            int[] auxAtr = new int[vetAtrHandler.length];
            for(int o = 0; o < auxAtr.length; o++)
               auxAtr[o] = 0;
            int contAtrZero = 0;

            for(int a = 0; a < coll - 1; a++){
                vetAtrHandler[a].MDLP();
                //   vetAtrHandler[a].optmizeIntervals();
                //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
                if(vetAtrHandler[a].getIntervalGain() == 0){
                    auxAtr[a] = 1;
                    contAtrZero++;
                }

                //  vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
                vetAtrHandler[a].intervalWeightsEntropy();
            }

            // atualiza matriz - retira atributos com ganho 0
            if(contAtrZero != 0){
                matrizTreino = retiraAtributos(auxAtr, contAtrZero, matrizTreino);
                coll = matrizTreino[0].length;
            }

         //   int[] ordem;

            for(int i = 0; i < nroClasses; i++) {   // para experimento com 3 classe  - anda em classes
              //  ordem = ordenaAtributos(Classes[i]);
                oneClassTrain = criaTreinamento(matrizTreino,Classes[i]);
                networks[i] = new Networks(oneClassTrain, Classes[i], matrizTreino.length);
                networks[i].learn(vetAtrHandler);

            }

            normalizeCorrelations(coll-1);

            normalizeIntervalGain(coll-1);

            matrizTeste = retiraAtributosTeste(auxAtr, contAtrZero, matrizTeste);

            trainClassification = classificationAccuracy(matrizTeste);
            testClassification += trainClassification; //classificationAccuracy(matrizTeste);

     //       printNetwork(coll-1);

            //    System.out.println("#############" + trainClassification);

            networks = null;
            vetAtrHandler = null;

        } // fim do while it < 10        -- cross validation --


        System.out.println("treno " + trainClassification/10);
        System.out.println("teste " + testClassification/10);


    }




    public void zeraCorrelacoesAbaixoThs(int interAtrs, double cutW){

        int atrs1 = 0, atrs2 = 0;
        double soma = 0;

        for(int j = 0; j < interAtrs; j++){             // pq todas as redes usao mesmos intervalos de atributos
            atrs1 = networksFull.getVetCorrelation()[j][1].getCMLen();     // numero de atributos 1
            atrs2 = networksFull.getVetCorrelation()[j][1].getCMCol();     // numero de atributos 2
            for(int k = 0; k < atrs1; k++){
                for(int m = 0; m < atrs2; m++){
                   for(int z = 2; z < nroClasses+1; z++ )
                       if(networksFull.getVetCorrelation()[j][z].getCMEx(k,m) < cutW)
                           networksFull.getVetCorrelation()[j][z].setCMEx(k,m,0);
                }
            }

        }
    }

    public double[][] retiraAtributos(int[] auxAtr, int contAtrZero , double[][] matriz){
                           // 
        int line = matriz.length;
        int coll = matriz[0].length;

        AttributeHandler[] auxVetHandler = new AttributeHandler[vetAtrHandler.length];

        for(int b = 0; b < coll - 1; b++)
            auxVetHandler[b] = vetAtrHandler[b];       // copia vetAtrHandler

        vetAtrHandler = null;
        vetAtrHandler = new AttributeHandler[coll - (contAtrZero + 1)];   // redeclara
        int contAtr = 0;

        for(int a = 0; a < coll - 1; a++)
            if(auxAtr[a] != 1){
                vetAtrHandler[contAtr] = auxVetHandler[a];
                contAtr++;
            }

        // retirar atributos com ganho 0 do conjunto de treinamento
        double[][] newMatriz = new double[line][coll-contAtrZero];

        contAtr = 0;
        for(int n = 0; n < coll; n++)
            if(n == coll-1 || auxAtr[n] == 0){
                for(int m = 0; m < line; m++)
                    newMatriz[m][contAtr] = matriz[m][n];
                contAtr++;
            }

        return newMatriz;
    }

    public int[] ordenaAtributos(double classe){            // ordena considerando peso das classes. # verificar

        int len = vetAtrHandler.length;
        double maior = -1;
        int indMaior = 0;
        double [] maiores = new double[len];
        int [] indMaiores = new int[len];
        int [] indOrdenados = new int[len];

        for(int i = 0; i < len; i++){
            maior = -1;
            indMaior = 0;
           for(int j = 0; j < vetAtrHandler[i].getVetAtr().length -1; j++)
              if(vetAtrHandler[i].getIntClass(j,(int)classe) > maior){
                 maior = vetAtrHandler[i].getIntClass(j,(int)classe);
                 indMaior = j;
              }                                                         // seleciona o maior dos intervalos (prob) de cada atributo
          maiores[i] = maior;
          indMaiores[i] = indMaior;
        }

        for(int m = 0; m < len; m++){
          maior = maiores[0];
          indMaior = 0;                                                // ordena para por atributos
          for(int k = 1; k < len; k++)
             if(maiores[k] > maior && maiores[k] != -1){
               maior = maiores[k];
               indMaior = k;
           }
         indOrdenados[m] = indMaior;
         maiores[indMaior] = -1;   
        }

        return indOrdenados;
    }

     public double[][] ordenaAtributosPorGanho(double[][] matriz, int[] ordem){
                                 // recebe matriz e ordem de troca de colunas
        int line = matriz.length;
        int coll = matriz[0].length;
        double auxTroca;


     //   AttributeHandler auxVetHandler;     // para ordenar vetor

        for(int b = 0; b < coll - 1; b++){
            if(ordem[b] != b)
               for(int i = 0; i < line; i++){
                  auxTroca = matriz[i][b];
                  matriz[i][b] = matriz[i][ordem[b]];
                  matriz[i][ordem[b]] = auxTroca;
               }
        }

          return matriz; 
     }

   public int[] ordenaVetorDeAtributos(int coll){

        double maior, auxTroca;
        int indMaior = 0, aux;
        int[] ordem = new int[coll-1];
        double[] intGains = new double[coll-1];

        for(int i = 0; i < coll-1; i++){
           ordem[i] = i;
           intGains[i] = vetAtrHandler[i].getIntervalGain();
        }

        for(int b = 0; b < coll - 2; b++){
           maior = intGains[b];
           indMaior = b; 
           for(int a = b+1; a < coll - 1; a++)
              if(intGains[a] > maior){
                  maior = intGains[a];
                  indMaior = a;
              }

          auxTroca = intGains[b];
          intGains[b] = maior;
          intGains[indMaior] = auxTroca;

          aux = ordem[b];
          ordem[b] = ordem[indMaior];
          ordem[indMaior] = aux;


       }

          return ordem;
   }

    public double[][] retiraAtributosTeste(int[] auxAtr, int contAtrZero , double[][] matriz){

        int line = matriz.length;
        int coll = matriz[0].length;

        // retirar atributos com ganho 0 do conjunto de treinamento
        double[][] newMatriz = new double[line][coll-contAtrZero];

        int contAtr = 0;
        for(int n = 0; n < coll; n++)
            if(n == coll-1 || auxAtr[n] == 0){
                for(int m = 0; m < line; m++)
                    newMatriz[m][contAtr] = matriz[m][n];
                contAtr++;
            }

        return newMatriz;
    }

    public double classificationAccuracy(double[][] matriz){

        int line = matriz.length;
        int coll = matriz[0].length;
        double somaCorretos = 0, maior, classe = 0;
        int indMaior;

        for(int b = 0; b < nroClasses; b++)
            networks[b].criaProbClass(matriz);

        for(int a = 0; a < line; a++){                        // n�o trata empate
            maior = networks[0].getProb(a);
            indMaior = 0;
            for(int b = 1; b < nroClasses; b++)
                if(networks[b].getProb(a) > maior){
                    maior = networks[b].getProb(a);
                    indMaior = b;
                }

            classe = networks[indMaior].getClasse();

            for(int b = 0; b < coll; b++)
               System.out.print(matriz[a][b] + " "); 
            System.out.println("instance " + a + " " + classe + " "+ matriz[a][coll - 1]);

            if(networks[indMaior].getClasse() == matriz[a][coll - 1])
                somaCorretos++;


            //         else
            //         System.out.println();
        }

        // teste
        /*       for(int a = 0; a < line; a++){
                 for(int i = 0; i < coll-1; i++)
                    System.out.print(matriz[a][i] + "  ");
                 for(int b = 0; b < nroClasses; b++)
                    System.out.print(" " + networks[b].getProb(a));
              System.out.println("classe " + matriz[a][coll-1]);
              }
        */


        return (somaCorretos/line);
    }

    public double[] classificationAccuracyTest(double[][] matriz){

        int line = matriz.length;
        int coll = matriz[0].length;
        double somaCorretos = 0, maior, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0;
        double somaProb = 0, somaSum = 0;
        int indMaior;
        double classe1, classe2, aux = 0;
        double[] classifications = new double[3];

        for(int b = 0; b < nroClasses; b++)
            networks[b].criaProbClass(matriz);

        for(int a = 0; a < line; a++){                        // n�o trata empate
            somaProb = 0;
            maior = networks[0].getProb(a);
            somaProb += maior;
            indMaior = 0;
            for(int b = 1; b < nroClasses; b++){
                somaProb += networks[b].getProb(a);
                if(networks[b].getProb(a) > maior){
                    maior = networks[b].getProb(a);
                    indMaior = b;
                }
            }

            classe = networks[indMaior].getClasse();

            if(networks[indMaior].getClasse() == matriz[a][coll - 1])
                somaCorretos++;


            somaSum = 0;
            maior = networks[0].getSum(a);
            somaSum += maior;
            indMaior = 0;
            for(int b = 1; b < nroClasses; b++){
                somaSum += networks[b].getSum(a);
                if(networks[b].getSum(a) > maior){
                    maior = networks[b].getSum(a);
                    indMaior = b;
                }
            }

            classe1 = networks[indMaior].getClasse();

            if(networks[indMaior].getClasse() == matriz[a][coll - 1])
                somaCorretos1++;


            maior = (networks[0].getProb(a) + networks[0].getSum(a)); //((networks[0].getSum(a)/somaSum) + (networks[0].getProb(a)/somaProb))/2;
            for(int c = 1; c < nroClasses; c++){
                aux = (networks[c].getProb(a) + networks[c].getSum(a));//((networks[c].getSum(a)/somaSum) + (networks[c].getProb(a)/somaProb))/2;
                if(aux > maior){
                    maior = aux;
                    indMaior = c;
                }
            }

            classe2 = networks[indMaior].getClasse();

            if(networks[indMaior].getClasse() == matriz[a][coll - 1])
                somaCorretos2++;

            //         else
            //         System.out.println();
        }

    //    System.out.println();
    //    System.out.println(somaCorretos/line  + "  "  + somaCorretos1/line  + " " + somaCorretos2/line);

        classifications[0] = (somaCorretos/line)*100;
        classifications[1] = (somaCorretos1/line)*100;
        classifications[2] = (somaCorretos2/line)*100;


        return classifications; //(somaCorretos1/line)*100;
    }


    public double[] DnoClassifier(double[][] matriz){   // classificadores para testes

            int line = matriz.length;
            int coll = matriz[0].length;
            double somaCorretos = 0, maior, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0, somaCorretos3 = 0;
            double somaProb = 0, somaSum = 0;
            int indMaior;
            double classe1 = 0, classe2 = 0,  aux = 0;
            double[] classifications = new double[11];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
            double[] attrGain = new double[coll-1];                            // pesos de atributos
            double soma = 0;
            double n = 0.0;

                   for(int i = 0; i < coll-1; i++) {
                     attrGain[i] = vetAtrHandler[i].getIntervalGain();
                     soma += attrGain[i];
                   }

                  for(int i = 0; i < coll-1; i++){
                  //
                     if(soma != 0)
                      attrGain[i] /= soma;
                     // attrGain[i]++;
                                // teste
                //     System.out.println("ganho " + attrGain[i]);
                  }

              //    AttributeCorrelation[] vetAux

                  for(int b = 0; b < nroClasses; b++){
                      networks[b].criaProbClass(matriz);       // Weighted(matriz,attrGain);                // ############### classificador 3
                  //    vetAux = networks[b].getVetCorrelation();
                   }


            for(int a = 0; a < line; a++){


                somaProb = 0;
                somaSum = 0;
                for(int b = 0; b < nroClasses; b++){
                    somaProb += networks[b].getProb(a);
                    somaSum += networks[b].getSum(a);
                }

              n = 0.0;
             for(int cl = 0; cl < 11; cl++){


                if(somaSum == 0 && somaProb == 0)
                    maior = 0;
                else if(somaSum == 0)
                    maior = (networks[0].getProb(a)/somaProb);
                else if(somaProb == 0)
                    maior = (networks[0].getSum(a)/somaSum);
                else
                    maior = (n*(networks[0].getSum(a)/somaSum) + (1-n)*(networks[0].getProb(a)/somaProb));

                indMaior = 0;
                for(int c = 1; c < nroClasses; c++){

                    if(somaSum != 0 && somaProb != 0)
                       aux = (n*(networks[c].getSum(a)/somaSum) + (1-n)*(networks[c].getProb(a)/somaProb));
                    else if(somaSum == 0 && somaProb == 0)
                        aux = 0;
                    else if(somaSum == 0)
                        aux = (networks[c].getProb(a)/somaProb);
                    else if(somaProb == 0)
                        aux = (networks[c].getSum(a)/somaSum);


                    if(aux > maior){
                        maior = aux;
                        indMaior = c;
                    }
                }

                 classe2 = networks[indMaior].getClasse();

               
                  n = n + 0.1;
                 
                if(classe2 == matriz[a][coll-1])
                    classifications[cl]++;
           /*     else{
                    System.out.println("Classe correta " + matriz[a][coll-1] + " classe estimada " + classe2);
                    for(int f = 0; f < networks.length; f++)
                       System.out.println("classe "  +  networks[f].getClasse() + "prod " + networks[f].getProb(a) + " sum  " + networks[f].getSum(a) + " prodsum "+ ((networks[f].getSum(a)/somaSum) + (networks[f].getProb(a)/somaProb))/2  );

                }
              */


               }  // itera�ao de n += 0.1





            } // for-line

         for(int h = 0; h < 11; h++)
           classifications[h] = (classifications[h]/line)*100;

         return classifications;


        /*
         somaProb = 0;
                maior = networks[0].getProb(a);
                somaProb += maior;
                indMaior = 0;
                for(int b = 1; b < nroClasses; b++){
                    somaProb += networks[b].getProb(a);
                    if(networks[b].getProb(a) > maior){
                        maior = networks[b].getProb(a);
                        indMaior = b;
                    }
                }


                classe = networks[indMaior].getClasse();
                if(classe == matriz[a][coll-1])
                    somaCorretos++;

                                                             // ##### classificador 2
                somaSum = 0;
                maior = networks[0].getSumOfProds(a);                    // getProdSum
                somaSum += maior;
                indMaior = 0;
                for(int b = 1; b < nroClasses; b++){
                    somaSum += networks[b].getSum(a);
                    if(networks[b].getSumOfProds(a) > maior){
                        maior = networks[b].getSumOfProds(a);
                        indMaior = b;
                    }
                }

                classe1 = networks[indMaior].getClasse();
                if(classe1 == matriz[a][coll-1]){
                    somaCorretos1++;
                }

         */

        }

      public double DnoClassifier(double[][] matriz, AttributeCorrelation[] vetAttrCorr, double n){   // classificadores para testes

            int line = matriz.length;
            int coll = matriz[0].length;
            double somaCorretos = 0, maior, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0, somaCorretos3 = 0;
            double somaProb = 0, somaSum = 0;
            int indMaior;
            double classe1 = 0, classe2 = 0,  aux = 0;
            double[] classifications = new double[11];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
            double[] attrGain = new double[coll-1];                            // pesos de atributos
            double soma = 0;


                   for(int i = 0; i < coll-1; i++) {
                     attrGain[i] = vetAtrHandler[i].getIntervalGain();
                     soma += attrGain[i];
                   }

                  for(int i = 0; i < coll-1; i++){
                  //
                     if(soma != 0)
                      attrGain[i] /= soma;
                     // attrGain[i]++;
                                // teste
                //     System.out.println("ganho " + attrGain[i]);
                  }

              //    AttributeCorrelation[] vetAux

                  for(int b = 0; b < nroClasses; b++){
                      networks[b].criaProbClassWeighted(matriz,attrGain, vetAttrCorr);       // Weighted(matriz,attrGain);                // ############### classificador 3
                  //    vetAux = networks[b].getVetCorrelation();
                   }


            for(int a = 0; a < line; a++){


                somaProb = 0;
                somaSum = 0;
                for(int b = 0; b < nroClasses; b++){
                    somaProb += networks[b].getProb(a);
                    somaSum += networks[b].getSum(a);
                }



                if(somaSum == 0 && somaProb == 0)
                    maior = 0;
                else if(somaSum == 0)
                    maior = (networks[0].getProb(a)/somaProb);
                else if(somaProb == 0)
                    maior = (networks[0].getSum(a)/somaSum);
                else
                    maior = (n*(networks[0].getSum(a)/somaSum) + (1-n)*(networks[0].getProb(a)/somaProb));

                indMaior = 0;
                for(int c = 1; c < nroClasses; c++){

                    if(somaSum != 0 && somaProb != 0)
                       aux = (n*(networks[c].getSum(a)/somaSum) + (1-n)*(networks[c].getProb(a)/somaProb));
                    else if(somaSum == 0 && somaProb == 0)
                        aux = 0;
                    else if(somaSum == 0)
                        aux = (networks[c].getProb(a)/somaProb);
                    else if(somaProb == 0)
                        aux = (networks[c].getSum(a)/somaSum);


                    if(aux > maior){
                        maior = aux;
                        indMaior = c;
                    }
                }

                 classe2 = networks[indMaior].getClasse();

                 if(classe2 == matriz[a][coll-1])
                    somaCorretos++;


                // mostra classifica��o

                 for(int j = 0; j < coll-1; j++)
                      System.out.print(matriz[a][j] + " ");
                   System.out.println(classe2);


            } // for-line


         return line - somaCorretos;


        }

    public double[] DnoClassifierFull(double[][] matriz){   // classificadores para testes

               int line = matriz.length;
               int coll = matriz[0].length;
               double somaCorretos = 0, maior, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0, somaCorretos3 = 0;
               double somaProb = 0, somaSum = 0;
               int indMaior;
               double classe1 = 0, classe2 = 0,  aux = 0;
               double[] classifications = new double[11];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
               double[] attrGain = new double[coll-1];                            // pesos de atributos
               double soma = 0;
               double n = 0.0;

                      for(int i = 0; i < coll-1; i++) {
                        attrGain[i] = vetAtrHandler[i].getIntervalGain();
                        soma += attrGain[i];
                      }

                     for(int i = 0; i < coll-1; i++){
                     //
                        if(soma != 0)
                         attrGain[i] /= soma;
                        // attrGain[i]++;
                                   // teste
                   //     System.out.println("ganho " + attrGain[i]);
                     }

                 //    AttributeCorrelation[] vetAux

                                                      // vetAttrCorr
                         networksFull.criaProbClassWeighted(matriz,attrGain);       // Weighted(matriz,attrGain);                // ############### classificador 3
                     //    vetAux = networks[b].getVetCorrelation();



               for(int a = 0; a < line; a++){


                   somaProb = 0;
                   somaSum = 0;
                   for(int b = 1; b < nroClasses+1; b++){
                       somaProb += networksFull.getProb(a,b);
                       somaSum += networksFull.getSum(a,b);
                   }

                 n = 0.0;
                for(int cl = 0; cl < 11; cl++){


                   if(somaSum == 0 && somaProb == 0)
                       maior = 0;
                   else if(somaSum == 0)
                       maior = (networksFull.getProb(a,1)/somaProb);
                   else if(somaProb == 0)
                       maior = (networksFull.getSum(a,1)/somaSum);
                   else
                       maior = (n*(networksFull.getSum(a,1)/somaSum) + (1-n)*(networksFull.getProb(a,1)/somaProb));

                   indMaior = 1;
                   for(int c = 2; c < nroClasses+1; c++){

                       if(somaSum != 0 && somaProb != 0)
                          aux = (n*(networksFull.getSum(a,c)/somaSum) + (1-n)*(networksFull.getProb(a,c)/somaProb));
                       else if(somaSum == 0 && somaProb == 0)
                           aux = 0;
                       else if(somaSum == 0)
                           aux = (networksFull.getProb(a,c)/somaProb);
                       else if(somaProb == 0)
                           aux = (networksFull.getSum(a,c)/somaSum);


                       if(aux > maior){
                           maior = aux;
                           indMaior = c;
                       }
                   }

                    classe2 = indMaior;

              //      if(n == 0.5)
               //        System.out.println("C " + classe2);

                     n = n + 0.1;

                   if(classe2 == matriz[a][coll-1])
                       classifications[cl]++;
              /*     else{
                       System.out.println("Classe correta " + matriz[a][coll-1] + " classe estimada " + classe2);
                       for(int f = 0; f < networks.length; f++)
                          System.out.println("classe "  +  networks[f].getClasse() + "prod " + networks[f].getProb(a) + " sum  " + networks[f].getSum(a) + " prodsum "+ ((networks[f].getSum(a)/somaSum) + (networks[f].getProb(a)/somaProb))/2  );

                   }
                 */


                  }  // itera�ao de n += 0.1


               } // for-line

            for(int h = 0; h < 11; h++)
              classifications[h] = (classifications[h]/line)*100;

            return classifications;

           }

    public double DnoClassifierFull(double[][] matriz, double neta){   // classificadores para testes
                              // classificador que recebe parametro ja escolhino na fase de sele��o de modelos

               int line = matriz.length;
               int coll = matriz[0].length;
               double somaCorretos = 0, maior, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0, somaCorretos3 = 0;
               double somaProb = 0, somaSum = 0;
               int indMaior;
               double classe1 = 0, classe2 = 0,  aux = 0;
               double classifications = 0;            // rotulos atribuidos     //   double[] classifications2 = new double[line];
               double[] attrGain = new double[coll-1];                            // pesos de atributos
               double soma = 0;


                      for(int i = 0; i < coll-1; i++) {
                        attrGain[i] = vetAtrHandler[i].getIntervalGain();
                        soma += attrGain[i];
                      }

                     for(int i = 0; i < coll-1; i++){
                     //
                        if(soma != 0)
                         attrGain[i] /= soma;
                        // attrGain[i]++;
                                   // teste
                   //     System.out.println("ganho " + attrGain[i]);
                     }

                 //    AttributeCorrelation[] vetAux

                                                      // vetAttrCorr
                         networksFull.criaProbClassWeighted(matriz,attrGain);       // Weighted(matriz,attrGain);                // ############### classificador 3



               for(int a = 0; a < line; a++){


                   somaProb = 0;
                   somaSum = 0;
                   for(int b = 0; b < nroClasses; b++){
                       somaProb += networksFull.getProb(a,b);
                       somaSum += networksFull.getSum(a,b);
                   }



                   if(somaSum == 0 && somaProb == 0)
                       maior = 0;
                   else if(somaSum == 0)
                       maior = (networksFull.getProb(a,1)/somaProb);
                   else if(somaProb == 0)
                       maior = (networksFull.getSum(a,1)/somaSum);
                   else
                       maior = (neta*(networksFull.getSum(a,1)/somaSum) + (1-neta)*(networksFull.getProb(a,1)/somaProb));

                   indMaior = 0;
                   for(int c = 2; c < nroClasses+1; c++){

                       if(somaSum != 0 && somaProb != 0)
                           aux = (neta*(networksFull.getSum(a,c)/somaSum) + (1-neta)*(networksFull.getProb(a,c)/somaProb));
                       else if(somaSum == 0 && somaProb == 0)
                           aux = 0;
                       else if(somaSum == 0)
                           aux = (networksFull.getProb(a,c)/somaProb);
                       else if(somaProb == 0)
                           aux = (networksFull.getSum(a,c)/somaSum);


                       if(aux > maior){
                           maior = aux;
                           indMaior = c;
                       }
                   }

                   classe2 = indMaior;



                   if(classe2 == matriz[a][coll-1])
                       classifications++;
       

               } // for-line



            return (classifications/line)*100;

           }

    public double[] DnoClassifierRetClassesFull(double[][] matriz){


               int line = matriz.length;
               int coll = matriz[0].length;
               double somaCorretos = 0, maior = 0, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0, somaCorretos3 = 0;
               double somaProb = 0, somaSum = 0;
               int indMaior;
               double classe1 = 0, classe2 = 0,  aux = 0;
               double[] classifications = new double[line];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
               double[] attrGain = new double[coll-1];                            // pesos de atributos
               double soma = 0;
               double n = 0.2;


               for(int i = 0; i < coll-1; i++) {
                        attrGain[i] = vetAtrHandler[i].getIntervalGain();
                        soma += attrGain[i];
                      }

                     for(int i = 0; i < coll-1; i++){
                     //
                        if(soma != 0)
                         attrGain[i] /= soma;
                        // attrGain[i]++;
                                   // teste
                   //     System.out.println("ganho " + attrGain[i]);
                     }

                 //    AttributeCorrelation[] vetAux

                         networksFull.criaProbClassWeighted(matriz,attrGain);       // Weighted(matriz,attrGain);                // ############### classificador 3


               for(int a = 0; a < line; a++){


                   somaProb = 0;
                   somaSum = 0;
                   for(int b = 0; b < nroClasses; b++){
                       somaProb += networksFull.getProb(a,b);
                       somaSum += networksFull.getSum(a,b);
                   }


                   if(somaSum == 0 && somaProb == 0)
                       if(somaSum == 0 && somaProb == 0)
                           maior = 0;
                       else if(somaSum == 0)
                           maior = (networksFull.getProb(a,1)/somaProb);
                       else if(somaProb == 0)
                           maior = (networksFull.getSum(a,1)/somaSum);
                       else
                           maior = (n*(networksFull.getSum(a,1)/somaSum) + (1-n)*(networksFull.getProb(a,1)/somaProb));

                   indMaior = 0;
                   for(int c = 2; c < nroClasses+1; c++){

                       if(somaSum != 0 && somaProb != 0)
                           aux = (n*(networksFull.getSum(a,c)/somaSum) + (1-n)*(networksFull.getProb(a,c)/somaProb));
                       else if(somaSum == 0 && somaProb == 0)
                           aux = 0;
                       else if(somaSum == 0)
                           aux = (networksFull.getProb(a,c)/somaProb);
                       else if(somaProb == 0)
                           aux = (networksFull.getSum(a,c)/somaSum);


                       if(aux > maior){
                           maior = aux;
                           indMaior = c;
                       }
                   }

                   classe2 = indMaior;



                    classifications[a] = classe2;





               } // for-line


            return classifications;

}


    public double[] DnoClassifierRetClasses(double[][] matriz){
        // classificador usual, retorna as classes

            int line = matriz.length;
            int coll = matriz[0].length;
            double maior;
            double somaProb = 0, somaSum = 0;
            int indMaior;
            double aux = 0;
            double[] classifications = new double[line];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
            double[] attrGain = new double[coll-1];                            // pesos de atributos
            double soma = 0;
            double n = 0.6;

                   for(int i = 0; i < coll-1; i++) {
                     attrGain[i] = vetAtrHandler[i].getIntervalGain();
                     soma += attrGain[i];
                   }

                  for(int i = 0; i < coll-1; i++){
                  //
                     if(soma != 0)
                      attrGain[i] /= soma;
                     // attrGain[i]++;
                                // teste
                //     System.out.println("ganho " + attrGain[i]);
                  }

              //    AttributeCorrelation[] vetAux

                  for(int b = 0; b < nroClasses; b++){
                      networks[b].criaProbClass(matriz); // criaProbClassWeighted(matriz,attrGain, vetAttrCorr);       // Weighted(matriz,attrGain);                // ############### classificador 3
                  //    vetAux = networks[b].getVetCorrelation();
                   }


            for(int a = 0; a < line; a++){


                somaProb = 0;
                somaSum = 0;
                for(int b = 0; b < nroClasses; b++){
                    somaProb += networks[b].getProb(a);
                    somaSum += networks[b].getSum(a);
                }


        //     for(int cl = 0; cl < 11; cl++){


                if(somaSum == 0 && somaProb == 0)
                    maior = 0;
                else if(somaSum == 0)
                    maior = (networks[0].getProb(a)/somaProb);
                else if(somaProb == 0)
                    maior = (networks[0].getSum(a)/somaSum);
                else
                    maior = (n*(networks[0].getSum(a)/somaSum) + (1-n)*(networks[0].getProb(a)/somaProb));

                indMaior = 0;
                for(int c = 1; c < nroClasses; c++){

                    if(somaSum != 0 && somaProb != 0)
                       aux = (n*(networks[c].getSum(a)/somaSum) + (1-n)*(networks[c].getProb(a)/somaProb));
                    else if(somaSum == 0 && somaProb == 0)
                        aux = 0;
                    else if(somaSum == 0)
                        aux = (networks[c].getProb(a)/somaProb);
                    else if(somaProb == 0)
                        aux = (networks[c].getSum(a)/somaSum);


                    if(aux > maior){
                        maior = aux;
                        indMaior = c;
                    }
                }

                 classifications[a] = networks[indMaior].getClasse();



            } // for-line


         return classifications;


        }


      public double[] DnoClassifierforMissingAttrValues(double[][] matriz){   // classificadores para testes

            int line = matriz.length;
            int coll = matriz[0].length;
            double[][] imputedMatriz;
            double somaCorretos = 0, maior, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0, somaCorretos3 = 0;
            double somaProb = 0, somaSum = 0;
            int indMaior;
            double classe1 = 0, classe2 = 0,  aux = 0;
            double[] classifications = new double[11];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
            double[] attrGain = new double[coll-1];                            // pesos de atributos
            double soma = 0;
            double n = 0.0;

                   for(int i = 0; i < coll-1; i++) {
                     attrGain[i] = vetAtrHandler[i].getIntervalGain();
                     soma += attrGain[i];
                   }

                  for(int i = 0; i < coll-1; i++){
                  //
                     if(soma != 0)
                      attrGain[i] /= soma;
                     // attrGain[i]++;
                                // teste
                //     System.out.println("ganho " + attrGain[i]);
                  }

              //    AttributeCorrelation[] vetAux

                  for(int b = 0; b < nroClasses; b++){    // trata missing values

                     imputedMatriz = null;
                     imputedMatriz = classifierImputationFromIntervalForTraining(matriz, networks[b].getClasse());
                     networks[b].criaProbClass(imputedMatriz);       // Weighted(matriz,attrGain);                // ############### classificador 3
                  //    vetAux = networks[b].getVetCorrelation();
                   }


            for(int a = 0; a < line; a++){


                somaProb = 0;
                somaSum = 0;
                for(int b = 0; b < nroClasses; b++){
                    somaProb += networks[b].getProb(a);
                    somaSum += networks[b].getSum(a);
                }

              n = 0.0;
             for(int cl = 0; cl < 11; cl++){


                if(somaSum == 0 && somaProb == 0)
                    maior = 0;
                else if(somaSum == 0)
                    maior = (networks[0].getProb(a)/somaProb);
                else if(somaProb == 0)
                    maior = (networks[0].getSum(a)/somaSum);
                else
                    maior = (n*(networks[0].getSum(a)/somaSum) + (1-n)*(networks[0].getProb(a)/somaProb));

                indMaior = 0;
                for(int c = 1; c < nroClasses; c++){

                    if(somaSum != 0 && somaProb != 0)
                       aux = (n*(networks[c].getSum(a)/somaSum) + (1-n)*(networks[c].getProb(a)/somaProb));
                    else if(somaSum == 0 && somaProb == 0)
                        aux = 0;
                    else if(somaSum == 0)
                        aux = (networks[c].getProb(a)/somaProb);
                    else if(somaProb == 0)
                        aux = (networks[c].getSum(a)/somaSum);


                    if(aux > maior){
                        maior = aux;
                        indMaior = c;
                    }
                }

                 classe2 = networks[indMaior].getClasse();


                  n = n + 0.1;

                if(classe2 == matriz[a][coll-1])
                    classifications[cl]++;
           /*     else{
                    System.out.println("Classe correta " + matriz[a][coll-1] + " classe estimada " + classe2);
                    for(int f = 0; f < networks.length; f++)
                       System.out.println("classe "  +  networks[f].getClasse() + "prod " + networks[f].getProb(a) + " sum  " + networks[f].getSum(a) + " prodsum "+ ((networks[f].getSum(a)/somaSum) + (networks[f].getProb(a)/somaProb))/2  );

                }
              */


               }  // itera�ao de n += 0.1





            } // for-line

         for(int h = 0; h < 11; h++)
           classifications[h] = (classifications[h]/line)*100;

         return classifications;




        }


      public double[] DnoClassifierFullforMissingAttrValues(double[][] matriz ){ // classificador estima a probabilidade de instancia pertencer a uma dada classe
                                                                         // usado como classificador na imputa��o.
              // retorna uma matriz- linha corresponde a neta e coluna � probabilidade de classifica��o para a classe informada
          
               int line = matriz.length;
               int coll = matriz[0].length;
               double somaCorretos = 0, maior, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0, somaCorretos3 = 0;
               double somaProb = 0, somaSum = 0;
               int indMaior;
               double classe1 = 0, classe2 = 0,  aux = 0;
               double[] classifications = new double[11];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
               double[][] imputedMatriz;
               double[] attrGain = new double[coll-1];                            // pesos de atributos
               double soma = 0;
               double n = 0.0;

                      for(int i = 0; i < coll-1; i++) {
                        attrGain[i] = vetAtrHandler[i].getIntervalGain();
                        soma += attrGain[i];
                      }

                     for(int i = 0; i < coll-1; i++){
                     //
                        if(soma != 0)
                         attrGain[i] /= soma;
                        // attrGain[i]++;
                                   // teste
                   //     System.out.println("ganho " + attrGain[i]);
                     }

                 //    AttributeCorrelation[] vetAux

                     for(int b = 1; b < nroClasses+1; b++){         // pulo do gato

                        imputedMatriz = null;
                        imputedMatriz = classifierImputationFromIntervalForTrainingFull(matriz, b);
                        // cria probs com matriz imputada para cada classe
                         networksFull.criaProbClassWeighted(imputedMatriz,attrGain);       // Weighted(matriz,attrGain);                // ############### classificador 3
                     //    vetAux = networks[b].getVetCorrelation();
                      }


               for(int a = 0; a < line; a++){


                   somaProb = 0;
                   somaSum = 0;
                   for(int b = 0; b < nroClasses; b++){
                       somaProb += networksFull.getProb(a,b);
                       somaSum += networksFull.getSum(a,b);
                   }

                 n = 0.0;
                for(int cl = 0; cl < 11; cl++){


                   if(somaSum == 0 && somaProb == 0)
                       maior = 0;
                   else if(somaSum == 0)
                       maior = (networksFull.getProb(a,1)/somaProb);
                   else if(somaProb == 0)
                       maior = (networksFull.getSum(a,1)/somaSum);
                   else
                       maior = (n*(networksFull.getSum(a,1)/somaSum) + (1-n)*(networksFull.getProb(a,1)/somaProb));

                   indMaior = 0;
                   for(int c = 1; c < nroClasses+1; c++){

                       if(somaSum != 0 && somaProb != 0)
                          aux = (n*(networksFull.getSum(a,c)/somaSum) + (1-n)*(networksFull.getProb(a,c)/somaProb));
                       else if(somaSum == 0 && somaProb == 0)
                           aux = 0;
                       else if(somaSum == 0)
                           aux = (networksFull.getProb(a,c)/somaProb);
                       else if(somaProb == 0)
                           aux = (networksFull.getSum(a,c)/somaSum);


                       if(aux > maior){
                           maior = aux;
                           indMaior = c;
                       }
                   }

                    classe2 = indMaior;

                     n = n + 0.1;

                   if(classe2 == matriz[a][coll-1])
                       classifications[cl]++;
              /*     else{
                       System.out.println("Classe correta " + matriz[a][coll-1] + " classe estimada " + classe2);
                       for(int f = 0; f < networks.length; f++)
                          System.out.println("classe "  +  networks[f].getClasse() + "prod " + networks[f].getProb(a) + " sum  " + networks[f].getSum(a) + " prodsum "+ ((networks[f].getSum(a)/somaSum) + (networks[f].getProb(a)/somaProb))/2  );

                   }
                 */


                  }  // itera�ao de n += 0.1


               } // for-line

            for(int h = 0; h < 11; h++)
              classifications[h] = (classifications[h]/line)*100;

            return classifications;

           }


     public double[] DnoClassifierFuzzy(double[][] matriz){   // classificadores com fun�a� de pertin�ncia

            int line = matriz.length;
            int coll = matriz[0].length;
            double somaCorretos = 0, maior, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0, somaCorretos3 = 0;
            double somaProb = 0, somaSum = 0;
            int indMaior;
            double classe1 = 0, classe2 = 0,  aux = 0;
            double[] classifications = new double[11];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
            double[] attrGain = new double[coll-1];                            // pesos de atributos
            double soma = 0;
            double n = 0.0;

                   for(int i = 0; i < coll-1; i++) {
                     attrGain[i] = vetAtrHandler[i].getIntervalGain();
                     soma += attrGain[i];
                   }

                  for(int i = 0; i < coll-1; i++){
                  //
                     if(soma != 0)
                      attrGain[i] /= soma;
                     // attrGain[i]++;
                                // teste
                //     System.out.println("ganho " + attrGain[i]);
                  }

              //    AttributeCorrelation[] vetAux

                  for(int b = 0; b < nroClasses; b++){
                      networks[b].criaProbClassFuzzy(matriz);       // Weighted(matriz,attrGain);                // ############### classificador 3
                  //    vetAux = networks[b].getVetCorrelation();
                   }


            for(int a = 0; a < line; a++){

                somaProb = 0;
                somaSum = 0;
                for(int b = 0; b < nroClasses; b++){
                    somaProb += networks[b].getProb(a);
                    somaSum += networks[b].getSum(a);
                }

              n = 0.0;
             for(int cl = 0; cl < 11; cl++){


                if(somaSum == 0 && somaProb == 0)
                    maior = 0;
                else if(somaSum == 0)
                    maior = (networks[0].getProb(a)/somaProb);
                else if(somaProb == 0)
                    maior = (networks[0].getSum(a)/somaSum);
                else
                    maior = (n*(networks[0].getSum(a)/somaSum) + (1-n)*(networks[0].getProb(a)/somaProb));

                indMaior = 0;
                for(int c = 1; c < nroClasses; c++){

                    if(somaSum != 0 && somaProb != 0)
                       aux = (n*(networks[c].getSum(a)/somaSum) + (1-n)*(networks[c].getProb(a)/somaProb));
                    else if(somaSum == 0 && somaProb == 0)
                        aux = 0;
                    else if(somaSum == 0)
                        aux = (networks[c].getProb(a)/somaProb);
                    else if(somaProb == 0)
                        aux = (networks[c].getSum(a)/somaSum);


                    if(aux > maior){
                        maior = aux;
                        indMaior = c;
                    }
                }

                 classe2 = networks[indMaior].getClasse();


                  n = n + 0.1;

                if(classe2 == matriz[a][coll-1])
                    classifications[cl]++;
           /*     else{
                    System.out.println("Classe correta " + matriz[a][coll-1] + " classe estimada " + classe2);
                    for(int f = 0; f < networks.length; f++)
                       System.out.println("classe "  +  networks[f].getClasse() + "prod " + networks[f].getProb(a) + " sum  " + networks[f].getSum(a) + " prodsum "+ ((networks[f].getSum(a)/somaSum) + (networks[f].getProb(a)/somaProb))/2  );

                }
              */


               }  // itera�ao de n += 0.1





            } // for-line

         for(int h = 0; h < 11; h++)
           classifications[h] = (classifications[h]/line)*100;

         return classifications;


        /*
         somaProb = 0;
                maior = networks[0].getProb(a);
                somaProb += maior;
                indMaior = 0;
                for(int b = 1; b < nroClasses; b++){
                    somaProb += networks[b].getProb(a);
                    if(networks[b].getProb(a) > maior){
                        maior = networks[b].getProb(a);
                        indMaior = b;
                    }
                }


                classe = networks[indMaior].getClasse();
                if(classe == matriz[a][coll-1])
                    somaCorretos++;

                                                             // ##### classificador 2
                somaSum = 0;
                maior = networks[0].getSumOfProds(a);                    // getProdSum
                somaSum += maior;
                indMaior = 0;
                for(int b = 1; b < nroClasses; b++){
                    somaSum += networks[b].getSum(a);
                    if(networks[b].getSumOfProds(a) > maior){
                        maior = networks[b].getSumOfProds(a);
                        indMaior = b;
                    }
                }

                classe1 = networks[indMaior].getClasse();
                if(classe1 == matriz[a][coll-1]){
                    somaCorretos1++;
                }

         */

        }

    public double[] DnoClassifierFullGeneticAlgorithm(double[][] matriz){   // EM TESTE

        int line = matriz.length;
        int coll = matriz[0].length;
        double somaCorretos = 0, maior, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0, somaCorretos3 = 0;
        double somaProb = 0, somaSum = 0;
        int indMaior;
        double classe1 = 0, classe2 = 0,  aux = 0;
        double[] classifications = new double[11];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
        double[] attrGain = new double[coll-1];                            // pesos de atributos
        double soma = 0;
        double n = 0.0;
        int numAttr = coll - 1;


        for(int i = 0; i < numAttr; i++) {
            attrGain[i] = vetAtrHandler[i].getIntervalGain();
            soma += attrGain[i];
        }

     //  Evolution ea = new Evolution(networksFull, vetAtrHandler,matriz,30);

        // criar objeto para armazenar matrizes para manipulação de GA
        // talvez dois objs.. um para vertices outro para arestas
        // a partir de networksFull




        for(int i = 0; i < coll-1; i++){
            //
            if(soma != 0)
                attrGain[i] /= soma;
            // attrGain[i]++;
            // teste
            //     System.out.println("ganho " + attrGain[i]);
        }

        //    AttributeCorrelation[] vetAux

            networksFull.criaProbClassWeighted(matriz,attrGain);       // Weighted(matriz,attrGain);                // ############### classificador 3
            //    vetAux = networks[b].getVetCorrelation();



        for(int a = 0; a < line; a++){


            somaProb = 0;
            somaSum = 0;
            for(int b = 0; b < nroClasses; b++){
                somaProb += networksFull.getProb(a,b);
                somaSum += networksFull.getSum(a,b);
            }

            n = 0.0;
            for(int cl = 0; cl < 11; cl++){


                if(somaSum == 0 && somaProb == 0)
                    maior = 0;
                else if(somaSum == 0)
                    maior = (networksFull.getProb(a,1)/somaProb);
                else if(somaProb == 0)
                    maior = (networksFull.getSum(a,1)/somaSum);
                else
                    maior = (n*(networksFull.getSum(a,1)/somaSum) + (1-n)*(networksFull.getProb(a,1)/somaProb));

                indMaior = 0;
                for(int c = 1; c < nroClasses+1; c++){

                    if(somaSum != 0 && somaProb != 0)
                        aux = (n*(networksFull.getSum(a,c)/somaSum) + (1-n)*(networksFull.getProb(a,c)/somaProb));
                    else if(somaSum == 0 && somaProb == 0)
                        aux = 0;
                    else if(somaSum == 0)
                        aux = (networksFull.getProb(a,c)/somaProb);
                    else if(somaProb == 0)
                        aux = (networksFull.getSum(a,c)/somaSum);


                    if(aux > maior){
                        maior = aux;
                        indMaior = c;
                    }
                }

                classe2 = indMaior;

                //      if(n == 0.5)
                //        System.out.println("C " + classe2);

                n = n + 0.1;

                if(classe2 == matriz[a][coll-1])
                    classifications[cl]++;
              /*     else{
                       System.out.println("Classe correta " + matriz[a][coll-1] + " classe estimada " + classe2);
                       for(int f = 0; f < networks.length; f++)
                          System.out.println("classe "  +  networks[f].getClasse() + "prod " + networks[f].getProb(a) + " sum  " + networks[f].getSum(a) + " prodsum "+ ((networks[f].getSum(a)/somaSum) + (networks[f].getProb(a)/somaProb))/2  );

                   }
                 */


            }  // itera�ao de n += 0.1


        } // for-line

        for(int h = 0; h < 11; h++)
            classifications[h] = (classifications[h]/line)*100;

        return classifications;

    }

    public void printNetwork(int interAtrs){

        /*    for(int i = 0; i < nroClasses; i++){
             for(int j = 0; j < networks[i].getVetAtr().length; j++){
                numPart = networks[i].getVetAtr()[j].getVetAtr().length;
                for(int k = 0; k < numPart-1; k++)
                    System.out.println(networks[i].getVetAtr()[j].getIntClass(k,i+1));

          }
          }
        */
        int atrs1 = 0, atrs2 = 0;

        for(int h = 0; h < interAtrs; h++){
            System.out.println(" attribute " + (h + 1));
            vetAtrHandler[h].printWeightAtr();
        }


        for(int j = 0; j < interAtrs-1; j++){
            atrs1 = networks[0].getVetCorrelation()[j].getCMLen();     // numero de atributos
            atrs2 = networks[0].getVetCorrelation()[j].getCMCol();     // numero de atributos
            for(int k = 0; k < atrs1; k++){
                System.out.println(" intervalo " + k + " atributos " + j + " " + (j+1));
                for(int m = 0; m < atrs2; m++){
                    for(int i = 1; i < nroClasses + 1; i++){
                        for(int z = 0; z < nroClasses; z++ )
                            if(networks[z].getClasse() == i)
                                networks[z].getVetCorrelation()[j].showCMEx(k,m);
                    }
                    System.out.println();
                }
            }

        }

        for(int h = 0; h < interAtrs; h++)
            System.out.println("interval gain " + vetAtrHandler[h].getIntervalGain());


    }

    public void normalizeCorrelations(int interAtrs){

        int atrs1 = 0, atrs2 = 0;
        double soma = 0;

        for(int j = 0; j < interAtrs-1; j++){             // pq todas as redes usao mesmos intervalos de atributos
            atrs1 = networks[0].getVetCorrelation()[j].getCMLen();     // numero de atributos 1
            atrs2 = networks[0].getVetCorrelation()[j].getCMCol();     // numero de atributos 2
            for(int k = 0; k < atrs1; k++){
                for(int m = 0; m < atrs2; m++){
                    soma = 0;
                    for(int z = 0; z < nroClasses; z++ )
                        soma += networks[z].getVetCorrelation()[j].getCMEx(k,m);

                    for(int x = 0; x < nroClasses; x++ )
                        networks[x].getVetCorrelation()[j].setCMEx(k,m,soma);
                }
            }

        }
    }

    public void normalizeIntervalGain(int interAtrs){

        double soma = 0;

        for(int a = 0; a < interAtrs; a++)
            soma += vetAtrHandler[a].getIntervalGain();

        for(int a = 0; a < interAtrs; a++)
            vetAtrHandler[a].normIntervalGain(soma);


    }

    public void particionaAtributo(double[][] matriz){

        int line = matriz.length;
        int coll = matriz[0].length;
        double[][] MO;
        thresholds = new double[line];
        vetAtrHandler = new AttributeHandler[coll-1];

        for(int a = 0; a < coll - 1; a++){

            MO = quicksort(selecionaAtributo(matriz,a),0,line-1);     // atributo 1

            MO = retiraEmptyValue(MO);   // usado em imputa�ao para retirar valores vazios

   /*   for(int i = 0; i < line; i++) {
         for(int j = 0; j < 2; j++)
            System.out.print(MO[i][j] + " ");
      System.out.println();
      }
   */
            contThreshold = 0;
            for(int z = 0; z < line; z++)         // zera vetor de threshold  - valor nulo deve ser nada usual
                thresholds[z] = 10000000;

            //  particionaEmDois(MO);     //   #######   cria thresholds usando arvore binaria   ######


            vetAtrHandler[a] = new AttributeHandler(retiraRepeticao(MO), MO, Classes);
            // vetAtrHandler[a].optmizeIntervals();       // ##### encontra intervalo otimo para particionamento que usa info gain #####


        }
       // System.out.println();
    }

    public double[][] retiraEmptyValue(double[][] M){

        int line = M.length, cont = 0, aux = 0;

        for(int i = 0; i < line; i++)
          if(M[i][0]  == emptyValue)
            cont++;
          else
            break;       // pois valores est�o ordenados e emptyValue = -100000

        if(cont == 0)
           return M;

        double[][] newMO = new double[line - cont][2];

        for(int i = cont; i < line; i++){
          for(int j = 0; j < 2; j++)
            newMO[aux][j] = M[i][j];
          aux++;
        }

        return newMO;
    }

    public double[] retiraRepeticao(double[][] atrClass){    // retorna vetor de atributos sem repeti��es

        int len = atrClass.length;
        int cont = 0, contValid = 0;
        double menor = atrClass[0][0];                      // menor elemento     - recebe vetor ordenado
        double maior = atrClass[atrClass.length - 1][0];      // maior elemento

        for(int i = 0; i < len; i++)
            if(i == 0)
                contValid++;
            else if(atrClass[i][0] != atrClass[i-1][0])
                contValid++;

        double[] intervals = new double[contValid];

        contValid = 0;
        for(int i = 0; i < len; i++)
            if(i == 0){
                intervals[contValid] = atrClass[i][0];
                contValid++;
            }
            else if(atrClass[i][0] != atrClass[i-1][0]){
                intervals[contValid] = atrClass[i][0];
                contValid++;
            }

        return intervals;
    }

    public double[] ordenaSemRepeticao(double[][] atrClass){

        int cont = 0, contValid = 0;
        double menor = atrClass[0][0];    // menor elemento
        double maior = atrClass[atrClass.length - 1][0];      // maior elemento

        while(thresholds[cont] != 10000000){
            if(thresholds[cont] > menor && thresholds[cont] < maior)
                contValid++;
            cont++;
        }

        double[] newThreshold = new double[contValid+2];

        newThreshold[0] = menor;
        contValid = 1;
        for(int i = 0; i < cont; i++)
            if(thresholds[i] > menor && thresholds[i] < maior){
                newThreshold[contValid] = thresholds[i];
                contValid++;
            }
        newThreshold[contValid] = maior;

        newThreshold = quicksort(newThreshold, 0, contValid-1);

        cont = 0;
        for(int i = 1; i < newThreshold.length; i++)
            if(newThreshold[i-1] == newThreshold[i])
                cont++;



        double[] intervals = new double[newThreshold.length - cont];

        cont = 0;
        intervals[0] = newThreshold[0];
        intervals[intervals.length - 1] = newThreshold[newThreshold.length - 1];
        for(int i = 1; i < newThreshold.length; i++)
            if(i < newThreshold.length && newThreshold[i-1] == newThreshold[i]){
                intervals[cont] = newThreshold[i];
                cont++;
                i++;
            }
            else{
                intervals[cont] = newThreshold[i-1];
                cont++;
            }
        intervals[cont] = newThreshold[newThreshold.length - 1];

        return intervals;
    }

    public void particionaEmDois(double[][] atributoOrdenado){    // metodo recursivo que particiona em 2 ou mantem particao antiga
        //  atributoOrdenado[][0] - atributo atributoOrdenado[][1] - classe
        // recebe atributo ordenado
        boolean newClass = false;
        int line = atributoOrdenado.length;

        double[] tempClass = new double[line];
        int cont = 0, findThr = 0;
        double particao = 0, gain = 0;
        double currentGain = 0, threshold = 0;
        double T = 0, T1 = 0;
        double[][] part1, part2;


        if(atributoOrdenado[0][0] != atributoOrdenado[line-1][0])
            for(int i = 1; i < line-1; i++){
                particao = (atributoOrdenado[i-1][0] + atributoOrdenado[i][0])/2;
                T = i;
                T1 = line - i;
                gain = info(atributoOrdenado,0,0) - ((T/line)*info(atributoOrdenado,particao,1) + (T1/line)*info(atributoOrdenado,particao,2));  // rever

                if(gain >= currentGain){
                    currentGain = gain;
                    threshold = particao;
                    /*  findThr = 0;
                       while(particao > atributoOrdenado[findThr][0])
                          findThr++;
                       if(findThr == 0)
                          threshold = atributoOrdenado[0][0];
                       else
                           threshold = atributoOrdenado[findThr-1][0];
                    */
                }
            }

        //   System.out.println(" " + threshold);
        thresholds[contThreshold] = threshold;
        contThreshold++;
        part1 = criaParticao(atributoOrdenado,threshold,1);
        if(part1.length != line && part1.length > 0)
            particionaEmDois(part1);   // menores que threshold

        part2 = criaParticao(atributoOrdenado,threshold,2);
        if(part2.length != line && part2.length > 0)
            particionaEmDois(part2);   // menores que threshold

    }

    public double[][] criaParticao(double[][] A, double threshold, int side){      // recebe unico atributo juntamente com classe - ordenado por atributo

        int line = A.length;
        int coll = A[0].length;
        double atr = A[0][0];    // atributo ja esta ordenado
        int cont = 0;
        double[][] newParticion;

        while(atr <= threshold && cont < line) {
            cont++;
            if(cont < line)
                atr = A[cont][0];
        }

        if(side == 1){                               // elementos menores (ou=) que o threshold
            newParticion = new double[cont][2];

            for(int i = 0; i < cont; i++)
                for(int j = 0; j < 2; j++)
                    newParticion[i][j] = A[i][j];
        }
        else{ //(side == 2){                   // elementos maiores que threshold
            newParticion = new double[line - cont][2];
            int contIni = 0;

            for(int i = cont; i < line; i++){
                for(int j = 0; j < 2; j++)
                    newParticion[contIni][j] = A[i][j];
                contIni++;
            }
        }

        return newParticion;
    }

    public double[][] selecionaAtributo(double[][] A, int a){

        int line = A.length;
        int coll = A[0].length;

        double[][] atributo = new double[line][2];

        for(int i = 0; i < line; i++){
            atributo[i][0] = A[i][a];
            atributo[i][1] = A[i][coll-1];
        }

        return atributo;
    }

    public double[][] quicksort(double[][] a, int p, int r) {      // vers�o matriz 1-atributo-classe
        int i;
        if (p < r) {
            i = partition(a, p, r);
            quicksort(a, p, i-1);
            quicksort(a, i+1, r);
        }
        return a;
    }

    public int partition(double[][] a, int p, int r)          // ordena matriz, segundo posicao do atributo [0], posiscao [1] refere-se � classe
    {
        int i = p-1, j = r;
        double v = a[r][0], aux = 0, aux1 = 0;

        for (;;) {
            while ((a[++i][0] < v));
            while ((v < a[--j][0]))
                if (/*X*/ j == p) break;
            if (i >= j) break;
            aux = a[i][0];
            aux1 = a[i][1];
            a[i][0] = a[j][0];
            a[i][1] = a[j][1];
            a[j][0] = aux;
            a[j][1] = aux1;
        }
        aux = a[i][0];
        aux1 = a[i][1];
        a[i][0] = a[r][0];
        a[i][1] = a[r][1];
        a[r][0] = aux;
        a[r][1] = aux1;

        return i;
    }

    public double[] quicksort(double[] a, int p, int r) {    // vers�o normal
        int i;
        if (p < r) {
            i = partition(a, p, r);
            quicksort(a, p, i-1);
            quicksort(a, i+1, r);
        }
        return a;
    }

    public int partition(double[] a, int p, int r)          // ordena matriz, segundo posicao do atributo [0], posiscao [1] refere-se � classe
    {
        int i = p-1, j = r;
        double v = a[r], aux = 0;

        for (;;) {
            while ((a[++i] < v));
            while ((v < a[--j]))
                if (/*X*/ j == p) break;
            if (i >= j) break;
            aux = a[i];
            a[i] = a[j];
            a[j] = aux;

        }
        aux = a[i];
        a[i] = a[r];
        a[r] = aux;

        return i;
    }


    public double info(double[][] A, double particao, int particionSide){
        // A[][0] -  atributo
        double S = 0;                                 // A[][1] - classe
        double info = 0;
        int line = A.length, coll = A[0].length;
        double[] classes = new double[nroClasses+1];

        for(int k = 0; k < nroClasses; k++)
            classes[k] = 0;

        // particionSide = 0 - conjunto completo | 1 < particao | 2 > particao
        switch(particionSide){
            case  0:

                S = A.length;
                for(int i = 0; i < line; i++)
                    classes[(int)A[i][1]]++;

                for(int k = 1; k < nroClasses+1; k++)
                    if(classes[k] > 0)
                        info += -1 * (classes[k]/S) * (Math.log10((classes[k]/S))/Math.log10(2));

                break;

            case 1:

                for(int c = 0; c < line; c++)
                    if(A[c][0] <= particao){
                        S++;
                        classes[(int)A[c][coll-1]]++;
                    }

                for(int k = 1; k < nroClasses+1; k++)
                    if(classes[k] > 0)
                        info += -1 * (classes[k]/S) * (Math.log10(classes[k]/S)/Math.log10(2));

                break;

            case 2:

                for(int c = 0; c < line; c++)
                    if(A[c][0] > particao){
                        S++;
                        classes[(int)A[c][coll-1]]++;
                    }

                for(int k = 1; k < nroClasses+1; k++)
                    if(classes[k] > 0)
                        info += -1 * (classes[k]/S) * (Math.log10(classes[k]/S)/Math.log10(2));

                break;

            default:
                System.out.println(" Erro, conferir particionSide");
                info = -1;
        }

        return info;

    }


     public void complexityExp(){

       int len = 1000;
       int it = 20;
       int coll = 1;

          PrintWriter out;
        FileOutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream("C:\\Java\\Datasets\\resultsTimeP1.txt");
        } catch (java.io.IOException e){
            System.out.println("Could not create result.txt");
        }

         out = new PrintWriter(outputStream);

       for(int h = 1; h <= 10; h++){
            coll = (int)(h*1000);

       double[][] matriz = new double[len][coll];
       double somaTempo = 0, somaQuadTempo = 0, desvio = 0, menor = 100000000, maior = 0, tempo = 0;
       Classes = new double[2];
       double[][] oneClassTrain;
       nroClasses = 2;
       // %%%%%% FULL
      // networksFull = new NetworkFull[nroClasses];
      networks = new Networks[nroClasses];

       for(int j = 0; j < it; j++){

       for(int i = 0; i < len; i++){
           for(int k = 0; k < coll; k++)
              matriz[i][k] = 10*Math.random();
             // matriz[i][1] = 2*Math.random();
              matriz[i][coll-1] = Math.floor(1 + 2*Math.random());
       }

         Classes[0] = 1;
         Classes[1] = 2;

          particionaAtributo(matriz);
        // Calendar dateTime = Calendar.getInstance();




            int[] auxAtr = new int[vetAtrHandler.length];
            int[] ordem;
            for(int o = 0; o < auxAtr.length; o++)
               auxAtr[o] = 0;
            int contAtrZero = 0;

           long a =  System.currentTimeMillis();
              /* #### retirar this.intervals = thrs de AttributeHandler  #### */
             for(int i = 0; i < coll - 1; i++){
                vetAtrHandler[i].histogramPart(3); // vetAtrHandler[i].MDLP(); // EDADB(nroClasses);//  // vetAtrHandler[a].MDLP();//          // usa metodo EDA-DB para obter intervalos


                                                            //  vetAtrHandler[a].optmizeIntervals();
                //   vetAtrHandler[a].histogramPart(3);        // numero de intervalos n�o de thresholds.   retirar para o caso de info gain
                vetAtrHandler[i].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual


           //    System.out.println(a + "  " + vetAtrHandler[a].getIntervalGain());
               //   vetAtrHandler[a].intervalWeights();         // calcula pesos dos intervalos
                vetAtrHandler[i].intervalWeightsEntropy();
            }


                 //#############################################



         for(int i = 0; i < nroClasses; i++) {   // para experimento com 3 classe  - anda em classes
            oneClassTrain = criaTreinamento(matriz,Classes[i]);
             networks[i] = new Networks(oneClassTrain, Classes[i], matriz.length);
             networks[i].learn(vetAtrHandler);
         //   networksFull[i] = new NetworkFull(oneClassTrain, Classes[i], matriz.length);
         //   networksFull[i].learnFullConection(vetAtrHandler);
        }

        // %%%%% FULL
      //  int numAttr = coll - 2;
      //  int numEdges = numAttr + (numAttr*(numAttr-3))/2;

       ordem = ordenaVetorDeAtributos(coll);                // ordena atributos por ganho de informa��o
       matriz = ordenaAtributosPorGanho(matriz,ordem);

        normalizeCorrelations(coll-1);

        //%%%%%%% FULL
     ///   normalizeCorrelationsFullVersion(numEdges);

        normalizeIntervalGain(coll-1);

          long b =  System.currentTimeMillis();

         tempo = (b-a);
         somaTempo += tempo;
         somaQuadTempo += Math.pow(tempo,2);

         if(tempo == 0)
           System.out.println(" " + it);

         if(it == 1){
             maior = tempo;
             menor = tempo;
         }


         if(tempo < menor)
            menor = tempo;

         if(tempo > maior)
           maior = tempo;

       }



        desvio = Math.sqrt((somaQuadTempo - (Math.pow(somaTempo,2)/(double)it))/(double)(it-1));
        out.println(coll + " " + len + "  " + (somaTempo/(double)it) + " " + desvio  +  "  " + maior + "  " + menor);





     }

           out.close();
    }


    public double[][] artificialData(){


        int line = 300;
        int nc = 3;
        int coll = 4;
        int onePart = line/nc;
        double[][] matriz = new double[line][coll];
        Random random = new Random();

        for(int i = 0; i < onePart; i++){
            matriz[i][0] = random.nextGaussian();
            matriz[i][1] = random.nextGaussian();
            matriz[i][2] = random.nextGaussian();
            matriz[i][3] = 1;
        }

        for(int i = onePart; i < 2*onePart; i++){
            matriz[i][0] = random.nextGaussian();
            matriz[i][1] = 4 + 2*random.nextGaussian();
            matriz[i][2] = 4 + 2*random.nextGaussian();
            matriz[i][3] = 2;
        }


        for(int i = 2*onePart; i < 3*onePart; i++){
            matriz[i][0] = 6 + random.nextGaussian();
            matriz[i][1] = 1.5 + random.nextGaussian();
            matriz[i][2] = random.nextGaussian();
            matriz[i][3] = 3;
        }
/*
        for(int i = 3*onePart; i < 4*onePart; i++){
           matriz[i][0] = random.nextGaussian();
           matriz[i][1] = 7.5 + random.nextGaussian();
           matriz[i][2] = 4;
        }

        // quatro

         for(int i = 4*onePart; i < 5*onePart; i++){
           matriz[i][0] = 2.5 + random.nextGaussian();
           matriz[i][1] = random.nextGaussian();
           matriz[i][2] = 5;
        }

        for(int i = 5*onePart; i < 6*onePart; i++){
           matriz[i][0] = 2.5 + random.nextGaussian();
           matriz[i][1] = 2.5 + random.nextGaussian();
           matriz[i][2] = 6;
       }


        for(int i = 6*onePart; i < 7*onePart; i++){
           matriz[i][0] = 2.5 + random.nextGaussian();
           matriz[i][1] = 4 + random.nextGaussian();
           matriz[i][2] = 7;
        }

        for(int i = 7*onePart; i < 8*onePart; i++){
           matriz[i][0] = 2.5 + random.nextGaussian();
           matriz[i][1] = 7.5 + random.nextGaussian();
           matriz[i][2] = 8;
        }

        // oito

         for(int i = 8*onePart; i < 9*onePart; i++){
           matriz[i][0] = 5 + random.nextGaussian();
           matriz[i][1] = random.nextGaussian();
           matriz[i][2] = 9;
        }

        for(int i = 9*onePart; i < 10*onePart; i++){
           matriz[i][0] = 5 + random.nextGaussian();
           matriz[i][1] = 2.5 + random.nextGaussian();
           matriz[i][2] = 10;
        }


        for(int i = 10*onePart; i < 11*onePart; i++){
           matriz[i][0] = 5 + random.nextGaussian();
           matriz[i][1] = 5 + random.nextGaussian();
           matriz[i][2] = 11;
        }

        for(int i = 11*onePart; i < 12*onePart; i++){
           matriz[i][0] = 5 + random.nextGaussian();
           matriz[i][1] = 7.5 + random.nextGaussian();
           matriz[i][2] = 12;
        }

        // doze

         for(int i = 12*onePart; i < 13*onePart; i++){
           matriz[i][0] = 7.5 + random.nextGaussian();
           matriz[i][1] = random.nextGaussian();
           matriz[i][2] = 13;
        }

        for(int i = 13*onePart; i < 14*onePart; i++){
           matriz[i][0] = 7.5 + random.nextGaussian();
           matriz[i][1] = 2.5 + random.nextGaussian();
           matriz[i][2] = 14;
        }


        for(int i = 14*onePart; i < 15*onePart; i++){
           matriz[i][0] = 7.5 + random.nextGaussian();
           matriz[i][1] = 5 + random.nextGaussian();
           matriz[i][2] = 15;
        }

        for(int i = 15*onePart; i < 16*onePart; i++){
           matriz[i][0] = 7.5 + random.nextGaussian();
           matriz[i][1] = 7.5 + random.nextGaussian();
           matriz[i][2] = 16;
        }

         // desesseis

        for(int i = 16*onePart; i < 17*onePart; i++){
           matriz[i][0] = 10 + random.nextGaussian();
           matriz[i][1] = random.nextGaussian();
           matriz[i][2] = 17;
        }

        for(int i = 17*onePart; i < 18*onePart; i++){
           matriz[i][0] = 10 + random.nextGaussian();
           matriz[i][1] = 2.5 + random.nextGaussian();
           matriz[i][2] = 18;
        }


        for(int i = 18*onePart; i < 19*onePart; i++){
           matriz[i][0] = 10 + random.nextGaussian();
           matriz[i][1] = 5 + random.nextGaussian();
           matriz[i][2] = 19;
        }

        for(int i = 19*onePart; i < 20*onePart; i++){
           matriz[i][0] = 10 + random.nextGaussian();
           matriz[i][1] = 7.5 + random.nextGaussian();
           matriz[i][2] = 20;
        }

        // vinte

         for(int i = 20*onePart; i < 21*onePart; i++){
           matriz[i][0] = 12.5 + random.nextGaussian();
           matriz[i][1] = random.nextGaussian();
           matriz[i][2] = 21;
        }

        for(int i = 21*onePart; i < 22*onePart; i++){
           matriz[i][0] = 12.5 + random.nextGaussian();
           matriz[i][1] = 2.5 + random.nextGaussian();
           matriz[i][2] = 22;
        }


        for(int i = 22*onePart; i < 23*onePart; i++){
           matriz[i][0] = 12.5 + random.nextGaussian();
           matriz[i][1] = 5 + random.nextGaussian();
           matriz[i][2] = 23;
        }

        for(int i = 23*onePart; i < 24*onePart; i++){
           matriz[i][0] = 12.5 + random.nextGaussian();
           matriz[i][1] = 7.5 + random.nextGaussian();
           matriz[i][2] = 24;
        }

        // vinte e quatro

         for(int i = 24*onePart; i < 25*onePart; i++){
           matriz[i][0] = 15 + random.nextGaussian();
           matriz[i][1] = random.nextGaussian();
           matriz[i][2] = 25;
        }

        for(int i = 25*onePart; i < 26*onePart; i++){
           matriz[i][0] = 15 + random.nextGaussian();
           matriz[i][1] = 2.5 + random.nextGaussian();
           matriz[i][2] = 26;
        }


        for(int i = 26*onePart; i < 27*onePart; i++){
           matriz[i][0] = 15 + random.nextGaussian();
           matriz[i][1] = 5 + random.nextGaussian();
           matriz[i][2] = 27;
        }

        for(int i = 27*onePart; i < 28*onePart; i++){
           matriz[i][0] = 15 + random.nextGaussian();
           matriz[i][1] = 7.5 + random.nextGaussian();
           matriz[i][2] = 28;
        }

        // vinte e oito

  /*       for(int i = 12*onePart; i < 13*onePart; i++){
           matriz[i][0] = 7.5 + random.nextGaussian();
           matriz[i][1] = random.nextGaussian();
           matriz[i][2] = 13;
        }

        for(int i = 13*onePart; i < 14*onePart; i++){
           matriz[i][0] = 7.5 + random.nextGaussian();
           matriz[i][1] = 2.5 + random.nextGaussian();
           matriz[i][2] = 14;
        }


        for(int i = 14*onePart; i < 15*onePart; i++){
           matriz[i][0] = 7.5 + random.nextGaussian();
           matriz[i][1] = 5 + random.nextGaussian();
           matriz[i][2] = 15;
        }

        for(int i = 15*onePart; i < 16*onePart; i++){
           matriz[i][0] = 7.5 + random.nextGaussian();
           matriz[i][1] = 7.5 + random.nextGaussian();
           matriz[i][2] = 16;
        }

        // trinta e dois
        */

        PrintWriter out;
        FileOutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream("C:\\Java\\Datasets\\GaussianData.txt");
        } catch (java.io.IOException e){
            System.out.println("Could not create result.txt");
        }

        out = new PrintWriter(outputStream);

        for(int i = 0; i < line; i++){
            for(int j = 0; j < coll; j++)
                out.print(matriz[i][j] + " ");
            out.println();
        }

        out.close();

        return matriz;

    }
    
    public double[][] artificialDataHighDimension(){

          DecimalFormat show = new DecimalFormat("0.00");

          int dimension = 100;
          int categorico = 10;
          int line = 500;
          int nc = 5;
          int coll = dimension + 1;
          int onePart = line/nc;
          double[][] matriz = new double[line][coll];
          Random random = new Random();


   /*     for(int j = 0; j < categorico; j++)
           for(int i = 0; i < line; i++) {


          }    */




          for(int i = 0; i < onePart; i++){
              for(int j = categorico; j < dimension; j++)
                 matriz[i][j] = random.nextGaussian();

              matriz[i][dimension] = 1;
          }

          for(int i = onePart; i < 2*onePart; i++){
              for(int j = categorico; j < dimension; j++)
                 matriz[i][j] = 6 + 2*random.nextGaussian();

              matriz[i][dimension] = 2;
          }


          for(int i = 2*onePart; i < 3*onePart; i++){
              for(int j = categorico; j < dimension; j++)
                 matriz[i][j] = 12 + random.nextGaussian();

              matriz[i][dimension] = 3;
          }

        for(int i = 3*onePart; i < 4*onePart; i++){
            for(int j = categorico; j < dimension; j++)
               matriz[i][j] = 40 + 5*random.nextGaussian();

              matriz[i][dimension] = 4;
        }

        // quatro

         for(int i = 4*onePart; i < 5*onePart; i++){
             for(int j = categorico; j < dimension; j++)
               matriz[i][j] = 2*j + random.nextGaussian();

              matriz[i][dimension] = 5;
        }



        /*
        for(int i = 5*onePart; i < 6*onePart; i++){
            for(int j = 0; j < dimension;)
          matriz[i][j] = random.nextGaussian();

              matriz[i][dimension] = 1;
       }


        for(int i = 6*onePart; i < 7*onePart; i++){
           matriz[i][0] = 2.5 + random.nextGaussian();
           matriz[i][1] = 4 + random.nextGaussian();
           matriz[i][2] = 7;
        }

        for(int i = 7*onePart; i < 8*onePart; i++){
           matriz[i][0] = 2.5 + random.nextGaussian();
           matriz[i][1] = 7.5 + random.nextGaussian();
           matriz[i][2] = 8;
        }

        // oito

         for(int i = 8*onePart; i < 9*onePart; i++){
           matriz[i][0] = 5 + random.nextGaussian();
           matriz[i][1] = random.nextGaussian();
           matriz[i][2] = 9;
        }

       */

          PrintWriter out;
          FileOutputStream outputStream = null;
          try{
              outputStream = new FileOutputStream("C:\\Java\\Datasets\\highDimension\\GaussianData" + dimension + ".txt");
          } catch (java.io.IOException e){
              System.out.println("Could not create result.txt");
          }

          out = new PrintWriter(outputStream);

           for(int j = 0; j < dimension; j++)
             out.print("n" + " ");
           out.println("y");
          // out.println();

          for(int i = 0; i < line; i++){
              for(int j = 0; j < coll; j++)
                  out.print(show.format(matriz[i][j]) + " ");
              out.println();
          }

          out.close();

          return matriz;

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

    public int[] shuffle(int[] E){

            int line = E.length;
            int aux = 0, cont = 0, aux1 = 0;
            Random randomNumbers = new Random();

            for(int h = 0; h < line; h++)
                E[h] = h;

            for(int i = 0; i < line; i++){
               aux = randomNumbers.nextInt(line-1);
               aux1 = E[i];
               E[i] = E[aux];
               E[aux] = aux1;

            }


            return E;
        }


    public double[][] normalizaMediaEDesvio(double[][] matriz){
        //  somente para atributos numericos
        int line = matriz.length;
        int coll = matriz[0].length;
        double soma = 0, media = 0, somaDesvio = 0, desvio = 0;
        double[][] normMatriz = new double[line][coll];

        for(int j = 0; j < coll-1; j++){
           soma = 0;
           media = 0;
           somaDesvio = 0;
           desvio = 0;
           for(int i = 0; i < line; i++)
              soma += matriz[i][j];

        media = soma/line;

        for(int i = 0; i < line; i++)
          somaDesvio += matriz[i][j] - media;

         desvio = Math.sqrt((1/(line-1)) * somaDesvio);

         for(int i = 0; i < line; i++)
           if(desvio != 0)
              normMatriz[i][j] = (matriz[i][j] - media)/desvio;
           else
              normMatriz[i][j] = (matriz[i][j] - media);
        }
       for(int i = 0; i < line; i++)
           normMatriz[i][coll-1] = matriz[i][coll-1];   // coloca classe

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


    public double[][] normalizacaoReescala(double[][] A, double novoMenor, double novoMaior){  // para de intervalo [a,b] para [x,y]

        double normMatriz[][];
        double maior, menor;
        int line = A.length;
        int coll = A[0].length;
        normMatriz = new double[line][coll];


        for(int q = 0; q < coll - 1; q++) {      // encontra maior e menor
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
                   normMatriz[r][q] = (A[r][q] - menor)*(novoMaior - novoMenor)/(maior - menor) + novoMenor;

               }
        }
        for(int c = 0; c < line; c++)
           normMatriz[c][coll - 1] = A[c][coll -1];   // coloca a classe

        return normMatriz;
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
    //  CCCCCCCCCCCCCC for comparative methods CCCCCCCCCCCCCCCCCC

    public double[][] simulateMissingAttributeZeros(double[][] matriz, double prob){
        int atrOff, cont = 0;
        int coll = matriz[0].length;               //  coloca 0 onde n�o h� dado
        int line = matriz.length;
        double rand;
        double[][] matrizToImput = new double[line][coll];

          for(int i = 0; i < line; i++)
             for(int j = 0; j < coll; j++)                       // cria copia
                matrizToImput[i][j] = matriz[i][j];

          for(int i = 0; i < line; i++){                        // cria matriz mascara com 0 onde existe valor e 1 onde nao exite
               rand = Math.random();
               if(rand < prob){                                 // atributo escolhido para ter valor apagado
                  atrOff = (int) (Math.random()*(coll-1));
                  matrizToImput[i][atrOff] = 0;
                  cont++;
               }
           }
     //     System.out.println(cont);
          return matrizToImput;
      }

    public double[][] simulateMissingAttributeMeans(double[][] matriz, double prob, double[] means){
        int atrOff, cont = 0;
        int coll = matriz[0].length;               //  coloca m�dia onde n�o h� dado
        int line = matriz.length;
        double rand;
        double[][] matrizToImput = new double[line][coll];

          for(int i = 0; i < line; i++)
             for(int j = 0; j < coll; j++)                       // cria copia
                matrizToImput[i][j] = matriz[i][j];

          for(int i = 0; i < line; i++){                        // cria matriz mascara com 0 onde existe valor e 1 onde nao exite
               rand = Math.random();
               if(rand < prob){                                 // atributo escolhido para ter valor apagado
                  atrOff = (int) (Math.random()*(coll-1));
                  matrizToImput[i][atrOff] = means[atrOff];         // media dos valores de attributo
                  cont++;
               }
           }
          return matrizToImput;
      }


    public double[][] simulateMissingAttributeMeans(double[][] matriz){
        int atrOff, cont = 0;
        int coll = matriz[0].length;               //  coloca media onde nao ha dado
        int line = matriz.length;
        double soma = 0;
        double[] means = new double[coll-1];
        double[][] matrizToImput = new double[line][coll];



            for(int j = 0; j < coll-1; j++) {                       // cria copia
                soma = 0;
                cont = 0;
                for (int i = 0; i < line; i++)
                    if (matriz[i][j] != emptyValue) {
                        matrizToImput[i][j] = matriz[i][j];
                        soma += matriz[i][j];
                        cont++;
                    }
            means[j] = soma/cont;
            }


        for(int i = 0; i < line; i++){                        // cria matriz mascara com 0 onde existe valor e 1 onde nao exite
            for(int j = 0; j < coll; j++)
                if(matriz[i][j] == emptyValue){                                 // atributo escolhido para ter valor apagado
                    matrizToImput[i][j] = means[j];
            }
            matrizToImput[i][coll -1] = matriz[i][coll-1];
        }

       return matrizToImput;
    }

/*
    public void criaTreinoeTesteRefining(double[][] matriz, String fileName) {

        int noTXT = fileName.length() - 4;
        String auxFileName = fileName.substring(0, noTXT);
        int line = matriz.length;
        int coll = matriz[0].length;
        double[][] matrizTreino;
        double[][] matrizTeste, matrizAuxTeste, matrizTE, matrizNoise;
        int[][] refMaskMat;
        int fold = 8;
        int contTreino = 0, contTeste = 0, it = 1;
        // double prob = 0.5; // ########################################################

        double[] mainClassification = new double[12];
        double[] somaClassification = new double[12];
        double[] desvio = new double[12];
        double[] mediaClass = new double[12];

        DecimalFormat show = new DecimalFormat("0.00");

        matriz = simulateMissingAttributeMeans(matriz);  // coloca a media onde ha atributos faltantes

        for (double prob = 0.0; prob <= 0.0; prob += 0.1){    // 0.1 a 0.5

            contMissAtr = 0;
            contAltAtrTR = 0;
            atrAltRate = 0;


           // matrizNoise = addNoiseAtrLimits(matriz,prob);

            for (int ncv = 1; ncv < 5; ncv++) {         // numero de repeti��es valida��es

                it = 1;
                matriz = shuffle(matriz);
                int[] stratification = indicesEstratificados(matriz, fold);


                while (it <= fold) {

                    //networksFull = new NetworkFull[nroClasses];
                    coll = matriz[0].length;
                    contTreino = 0;
                    contTeste = 0;
                    for (int i = 0; i < line; i++)
                        if (stratification[i] == it)
                            contTeste++;

                    matrizTreino = new double[line - contTeste][coll];
                    matrizTeste = new double[contTeste][coll];
                    matrizTE = new double[contTeste][coll];

                    contTeste = 0;
                    contTreino = 0;
                    for (int h = 0; h < line; h++)
                        if (stratification[h] != it) {
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = matriz[h][y];  //  matrizTreino[contTreino][y] = matriz[h][y];
                            contTreino++;
                        } else {
                            for (int y = 0; y < coll; y++)
                                matrizTeste[contTeste][y] = matriz[h][y];
                            contTeste++;
                        }

                  matrizTreino = addNoiseAtrLimits(matrizTreino,prob);


                    // imprime os dados com alteração aleatoria - Treino

                    PrintWriter out2;
                    FileOutputStream outputStream2 = null;
                    try {
//                        outputStream2 = new FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\CleanseMulti\\" + auxFileName + "\\" +  (int)(prob*100) + "\\AL\\" + auxFileName + it + ncv + ".txt");  //

                        outputStream2 = new FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\CleanseMulti\\" + auxFileName + "\\OR\\" + auxFileName + it + ncv + ".txt");  //

                    } catch (java.io.IOException e) {
                        System.out.println("Could not create result.txt");
                    }

                    out2 = new PrintWriter(outputStream2);


                    for (int i = 0; i < contTreino; i++) {
                        for (int j = 0; j < coll; j++)
                            out2.print(matrizTreino[i][j] + " ");   //  round(matriz,2) imprimindo matriz original
                        out2.println();
                    }

                    out2.close();


                    // imprime os dados  com alteração aleatoria Teste
                    PrintWriter out1;
                    FileOutputStream outputStream1 = null;
                    try {
  //                      outputStream1 = new FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\CleanseMulti\\" + auxFileName + "\\" + (int)(prob*100) + "\\AL\\" + auxFileName + it +  ncv+ "TE.txt");  //

                        outputStream1 = new FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\CleanseMulti\\" + auxFileName + "\\OR\\" + auxFileName + it +  ncv+ "TE.txt");  //

                    } catch (java.io.IOException e) {
                        System.out.println("Could not create result.txt");
                    }

                    out1 = new PrintWriter(outputStream1);


                    for (int i = 0; i < contTeste; i++) {
                        for (int j = 0; j < coll; j++)
                            out1.print(matrizTeste[i][j] + " ");   // imprimindo matriz original
                        out1.println();
                    }

                    out1.close();







                    //     RefiningStratifiedCrossValidationFull(matriz, fileName, it, prob);     // cria os conjuntos de treinamento

                  //  criaDominioRefinadoCppAbDG(matrizTreino, fileName, it, (int)(prob*100));    // CleanseOne     // matriz
                    criaDominioRefinadoCppAbDGMultiGenMode(matrizTreino, fileName, it, (int)(prob*100),  ncv);  // CleanseMulti

                    // ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,matrizes devem estar normalizadas!!!!!!!!!!!



                    int rep = 20;
                    refMaskMat = criaMatrizRefineAll(rep, contTeste, coll - 1);

                    for (int i = 0; i < rep; i++) {
//#############################################################################################
                  //      matrizAuxTeste = refiningForTestAssumingAClassFull(matrizTeste, refMaskMat, i + 1);//refiningForTestAssumingAClassFull(matrizTeste, prob); // altera conjunto de teste de maneira similar a do treino
//#############################################################################################
                        for (int j = 0; j < contTeste; j++)
                            for(int k = 0; k < coll - 1; k++)
                               if(refMaskMat[j][k] == i + 1)
                                   matrizTE[j][k] = matrizAuxTeste[j][k];

                    }

                    for (int j = 0; j < contTeste; j++)
                       // for (int k = 0; k < coll; k++)
                            matrizTE[j][coll - 1] = matrizTeste[j][coll-1];


                    PrintWriter out;
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\CleanseMulti\\" + auxFileName + "\\" + (int)(prob*100) + "\\ML\\" + auxFileName + it + ncv + "TE.txt");  //

                    } catch (java.io.IOException e) {
                        System.out.println("Could not create result.txt");
                    }

                    out = new PrintWriter(outputStream);


                    for (int i = 0; i < matrizTeste.length; i++) {
                        for (int j = 0; j < coll; j++)
                            out.print(matrizTeste[i][j] + " ");   // imprimindo matriz original
                        out.println();
                    }

                    out.close();




                    it++;

                }
            }
      //         for(int i = 0; i < mainClassification.length; i++){
         //   desvio1[i] = Math.sqrt((somaQuadClassification1[i] - (Math.pow(somaClassification1[i],2)/100))/99);
         //   mediaClass[i] = somaClassification[i]/8;
         //        System.out.println(show.format(i) + "  " + show.format(mediaClass[i]));



            PrintWriter out3;
            FileOutputStream outputStream3 = null;
            try {
                outputStream3 = new FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\CleanseOne\\" + auxFileName + "\\taxaDeAlteracao" + (int)(prob*100) + ".txt");  //

            } catch (java.io.IOException e) {
                System.out.println("Could not create result.txt");
            }

            out3 = new PrintWriter(outputStream3);

        out3.print("Taxa de alteraçoes TR " + atrAltRate/(double)fold);
     //   System.out.println("Taxa de alteraçoes TE " + atrAltRateTE/(double)fold);
     //   System.out.println("Taxa alteraçao de classe " + contAltClasses/(double)fold);

            out3.close();



            }

 //   }
        

    }

    */

    public void criaTreinoeTesteRefiningExtension(double[][] matriz, String fileName) {
    // cria treino e teste, ambos com ruido. Para data quality enhancement - ainda não considera attr faltantes e versão para AbDG

        int noTXT = fileName.length() - 4;
        String auxFileName = fileName.substring(0, noTXT);
        int line = matriz.length;
        int coll = matriz[0].length;
        double[][] matrizTreino;
        double[][] matrizTeste, matrizAuxTeste, matrizTE, matrizNoise;
        int[][] refMaskMat;
        int fold = 8;
        int contTreino = 0, contTeste = 0, it = 1;
        // double prob = 0.5; // ########################################################

        double[] mainClassification = new double[12];
        double[] somaClassification = new double[12];
        double[] desvio = new double[12];
        double[] mediaClass = new double[12];

        DecimalFormat show = new DecimalFormat("0.00");

      //  matriz = simulateMissingAttributeMeans(matriz);  // coloca a media onde ha atributos faltantes

        for (double prob = 0.0; prob <= 0.5; prob += 0.1){    // 0.1 a 0.5

            contMissAtr = 0;
            contAltAtrTR = 0;
            atrAltRate = 0;


            // matrizNoise = addNoiseAtrLimits(matriz,prob);

            for (int ncv = 1; ncv < 11; ncv++) {         // numero de repeti��es valida��es

                it = 1;
                matriz = shuffle(matriz);
                int[] stratification = indicesEstratificados(matriz, fold);


                while (it <= fold) {

               //     networksFull = new NetworkFull[nroClasses];
                    coll = matriz[0].length;
                    contTreino = 0;
                    contTeste = 0;
                    for (int i = 0; i < line; i++)
                        if (stratification[i] == it)
                            contTeste++;

                    matrizTreino = new double[line - contTeste][coll];
                    matrizTeste = new double[contTeste][coll];
                    matrizTE = new double[contTeste][coll];

                    contTeste = 0;
                    contTreino = 0;
                    for (int h = 0; h < line; h++)
                        if (stratification[h] != it) {
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = matriz[h][y];  //  matrizTreino[contTreino][y] = matriz[h][y];
                            contTreino++;
                        } else {
                            for (int y = 0; y < coll; y++)
                                matrizTeste[contTeste][y] = matriz[h][y];
                            contTeste++;
                        }

                    matrizTreino = addNoiseAtrLimits(matrizTreino,prob);
                    matrizTeste = addNoiseAtrLimits(matrizTeste,prob);


                    // imprime os dados com alteração aleatoria - Treino

                    PrintWriter out2;
                    FileOutputStream outputStream2 = null;
                    try {

                        outputStream2 = new FileOutputStream("C:\\Users\\João\\Documents\\EXPS\\dataset-noise\\" + auxFileName + "\\" +  (int)(prob*100) + "\\" + auxFileName + ncv + "_" + it + "TR.txt");  //

                      //  outputStream2 = new FileOutputStream("C:\\Users\\João\\Documents\\EXPS\\" + auxFileName + "\\OR\\" + auxFileName + it + ncv + ".txt");  //

                    } catch (java.io.IOException e) {
                        System.out.println("Could not create result.txt");
                    }

                    out2 = new PrintWriter(outputStream2);


                    for (int i = 0; i < contTreino; i++) {
                        for (int j = 0; j < coll; j++)
                            out2.print(show.format(matrizTreino[i][j]).replace(",",".") + " ");   //  round(matriz,2) imprimindo matriz original
                        out2.println();
                    }

                    out2.close();


                    // imprime os dados  com alteração aleatoria Teste
                    PrintWriter out1;
                    FileOutputStream outputStream1 = null;
                    try {
                         outputStream1 = new FileOutputStream("C:\\Users\\João\\Documents\\EXPS\\dataset-noise\\" + auxFileName + "\\" +  (int)(prob*100) + "\\" + auxFileName + ncv + "_" + it + "TE.txt");

                       // outputStream1 = new FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\CleanseMulti\\" + auxFileName + "\\OR\\" + auxFileName + it +  ncv+ "TE.txt");  //

                    } catch (java.io.IOException e) {
                        System.out.println("Could not create result.txt");
                    }

                    out1 = new PrintWriter(outputStream1);


                    for (int i = 0; i < contTeste; i++) {
                        for (int j = 0; j < coll; j++)
                            out1.print(show.format(matrizTeste[i][j]).replace(",",".") + " " + " ");   // imprimindo matriz original
                        out1.println();
                    }

                    out1.close();

/*
                    //FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\CleanseMulti\\" + auxFileName + "\\" + (int)(prob*100) + "\\ML\\" + auxFileName + it + ncv + "TE.txt");  //


                    //     RefiningStratifiedCrossValidationFull(matriz, fileName, it, prob);     // cria os conjuntos de treinamento

                    //  criaDominioRefinadoCppAbDG(matrizTreino, fileName, it, (int)(prob*100));    // CleanseOne     // matriz

                    criaDominioRefinadoCppAbDGMultiGenMode(matrizTreino, fileName, it, (int)(prob*100),  ncv);  // CleanseMulti

                    // ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,matrizes devem estar normalizadas!!!!!!!!!!!



                    int rep = 20;
                    refMaskMat = criaMatrizRefineAll(rep, contTeste, coll - 1);

                    for (int i = 0; i < rep; i++) {

                        matrizAuxTeste = refiningForTestAssumingAClassFull(matrizTeste, refMaskMat, i + 1);//refiningForTestAssumingAClassFull(matrizTeste, prob); // altera conjunto de teste de maneira similar a do treino

                        for (int j = 0; j < contTeste; j++)
                            for(int k = 0; k < coll - 1; k++)
                                if(refMaskMat[j][k] == i + 1)
                                    matrizTE[j][k] = matrizAuxTeste[j][k];

                    }

                    for (int j = 0; j < contTeste; j++)
                        // for (int k = 0; k < coll; k++)
                        matrizTE[j][coll - 1] = matrizTeste[j][coll-1];


                    PrintWriter out;
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\CleanseMulti\\" + auxFileName + "\\" + (int)(prob*100) + "\\ML\\" + auxFileName + it + ncv + "TE.txt");  //

                    } catch (java.io.IOException e) {
                        System.out.println("Could not create result.txt");
                    }

                    out = new PrintWriter(outputStream);


                    for (int i = 0; i < matrizTeste.length; i++) {
                        for (int j = 0; j < coll; j++)
                            out.print(matrizTeste[i][j] + " ");   // imprimindo matriz original
                        out.println();
                    }

                    out.close();

        */


                    it++;

                }
            }
            //         for(int i = 0; i < mainClassification.length; i++){
            //   desvio1[i] = Math.sqrt((somaQuadClassification1[i] - (Math.pow(somaClassification1[i],2)/100))/99);
            //   mediaClass[i] = somaClassification[i]/8;
            //        System.out.println(show.format(i) + "  " + show.format(mediaClass[i]));


        /*
            PrintWriter out3;
            FileOutputStream outputStream3 = null;
            try {
                outputStream3 = new FileOutputStream("C:\\Users\\João\\Documents\\MATLAB\\CleanseOne\\" + auxFileName + "\\taxaDeAlteracao" + (int)(prob*100) + ".txt");  //

            } catch (java.io.IOException e) {
                System.out.println("Could not create result.txt");
            }

            out3 = new PrintWriter(outputStream3);

            out3.print("Taxa de alteraçoes TR " + atrAltRate/(double)fold);
            //   System.out.println("Taxa de alteraçoes TE " + atrAltRateTE/(double)fold);
            //   System.out.println("Taxa alteraçao de classe " + contAltClasses/(double)fold);

            out3.close();

        */

        }

        //   }


    }




    public void simulateRefiningAttributeRandom(double[][] A, String fileName, int itCv){
                                                  // troca alguns valores por valores aleat�rios de atributos
      //  int noTXT = fileName.length() - 4;
	    String auxFileName = fileName; // fileName.substring(0, noTXT);
        double maior, menor;
        int line = A.length;
        int coll = A[0].length;
        double[][] newMatriz = new double[line][coll];
        double[][] menorMaiorAtrs = new double[coll-1][2];    // armazena menor e maior para cada atributo
        double rand = 0, rand1;
        double prob = 0.1;

        for(int q = 0; q < coll - 1; q++) {      // encontra maior e menor para cada atributo
            maior = A[0][q];
            menor = A[0][q];
            for(int p = 0; p < line; p++){
               if(A[p][q] > maior)
                  maior = A[p][q];
               if(A[p][q] < menor)
                  menor = A[p][q];
            }

                menorMaiorAtrs[q][0] = menor;
                menorMaiorAtrs[q][1] = maior;

        }


       for(int t = 1; t <= 4; t++){

       prob = 0.1;
       while(prob < 0.6){

         for(int i = 0; i < line; i++)
           for(int j = 0; j < coll; j++)
              newMatriz[i][j] = A[i][j];


            for(int i = 0; i < line; i++){
                for(int j = 0; j < coll-1; j++){
                   rand = Math.random();
                   if(rand < prob){                                 // atributo escolhido para ter valor apagado
                      rand1 = Math.random();
                      newMatriz[i][j] = (rand1*(menorMaiorAtrs[j][1] - menorMaiorAtrs[j][0]) + menorMaiorAtrs[j][0]);
                  }
                }
           }


         PrintWriter out;
          FileOutputStream outputStream = null;
          try{
              outputStream = new FileOutputStream("C:\\Users\\Jo�o\\Documents\\MATLAB\\random\\" + auxFileName + "\\"+ (int)(prob*100) + "\\" + auxFileName +  t + "-" + itCv + ".txt");
          } catch (java.io.IOException e){
              System.out.println("Could not create result.txt");
          }

          out = new PrintWriter(outputStream);


          for(int i = 0; i < line; i++){
              for(int j = 0; j < coll; j++)
                  out.print(newMatriz[i][j] + " ");
              out.println();
          }

          out.close();

         prob += 0.1;
     }
       }

      }

    double[] meanOfAttributes(double[][] A, double[] currentMean){

          int line = A.length;
          int coll = currentMean.length;
          double soma;

          for(int i = 0; i < coll; i++){
             soma = 0;
             for(int j = 0; j < line; j++)
                soma += A[j][i];

          currentMean[i] += soma;
          currentMean[i] /= (line + 1);
          }

         return currentMean;

      }

    void criaMatrizMissingAttrubute(double[][] matriz, String fileName){

       //  DecimalFormat show = new DecimalFormat("0,00");
          int line = matriz.length;
          int coll = matriz[0].length;
          int noTXT = fileName.length() - 4;
	      String auxFileName = fileName.substring(0, noTXT);
          double prob = 0.5;

          matriz = simulateMissingAttributeZeros(matriz, prob);
          matriz = normalizaMediaEDesvio(matriz);
        
          PrintWriter out;
          FileOutputStream outputStream = null;
          try{
              outputStream = new FileOutputStream("C:\\Java\\Datasets\\imputation\\" + auxFileName + (int)(prob*10) + ".txt");
          } catch (java.io.IOException e){
              System.out.println("Could not create result.txt");
          }

          out = new PrintWriter(outputStream);


          for(int i = 0; i < line; i++){
              for(int j = 0; j < coll; j++)
                  out.print(matriz[i][j] + " ");
              out.println();
          }

          out.close();
    }


      void criaMatrizMissingAttrubuteNA(double[][] matriz, String fileName){  //substitui 0 por NA para usar no R
                                                                            // 0 para n�o atrapalhar normaliza��o
       //  DecimalFormat show = new DecimalFormat("0,00");
          int line = matriz.length;
          int coll = matriz[0].length;
          int noTXT = fileName.length() - 4;
	      String auxFileName = fileName.substring(0, noTXT);
          double prob = 0.0;
          double rand = 0;
          int atrOff = 0, cont = 0, s = 0;

       //   matriz = shuffle(matriz);

       /*      for(int i = 0; i < line; i++){
               s = (int) (Math.random()*(line));
               rand = Math.random();
               if(rand < prob){                                 // atributo escolhido para ter valor apagado
                  atrOff = (int) (Math.random()*(coll-1));
                  matriz[s][atrOff] = emptyValue;
                  cont++;
               }
           }
          System.out.println(cont);
       */

          PrintWriter out;
          FileOutputStream outputStream = null;
          try{
              outputStream = new FileOutputStream("C:\\Users\\Public\\Documents\\Raw\\" + auxFileName + ".txt");
          } catch (java.io.IOException e){
              System.out.println("Could not create result.txt");
          }

          out = new PrintWriter(outputStream);


          for(int i = 0; i < line; i++){
              for(int j = 0; j < coll-1; j++)
                 if(matriz[i][j] != emptyValue)
                   out.print(matriz[i][j] + " ");
                 else{
                    out.print("NA ");          // ***********************(((((((((("NA,"
                    cont++;
                 }
               if(i == (line-1))
                   out.print(matriz[i][coll-1]);
                else
                   out.println(matriz[i][coll-1]);
          }

          out.close();
            System.out.println(cont);
    }


     void criaMatrizMissingAttrubuteNALinXCol(double[][] matrizOrig, String fileName){  //substitui 0 por NA para usar no R
                                                                            // 0 para n�o atrapalhar normaliza��o
       //  DecimalFormat show = new DecimalFormat("0,00");
          int line = matrizOrig.length;
          int coll = matrizOrig[0].length;
          int noTXT = fileName.length() - 4;
	      String auxFileName = fileName.substring(0, noTXT);
          double prob = 0.1;
          double rand = 0;
          int atrOff = 0, cont = 0, s = 0;
          double[][] matriz;

         while(prob < 0.6){
          matriz = matrizOrig;
          matriz = shuffle(matriz);

             for(int i = 0; i < line; i++){
                for(int j = 0; j < coll-1; j++){
                   s = (int) (Math.random()*(line));
                   rand = Math.random();
                   if(rand < prob){                                 // atributo escolhido para ter valor apagado
                     // atrOff = (int) (Math.random()*(coll-1));
                      matriz[s][j] = emptyValue;
                      cont++;
                  }
             }
             }
      //    System.out.println(cont);

          PrintWriter out;
          FileOutputStream outputStream = null;
          try{
              outputStream = new FileOutputStream("C:\\Java\\Datasets\\imputation\\toR\\" + auxFileName + (int)(prob*10) + "f.txt");
          } catch (java.io.IOException e){
              System.out.println("Could not create result.txt");
          }

          out = new PrintWriter(outputStream);


          for(int i = 0; i < line; i++){
              for(int j = 0; j < coll-1; j++)
                 if(matriz[i][j] != emptyValue)
                   out.print(matriz[i][j] + ",");
                 else
                   out.print("NA,");        // out.print(0 + ",");
              out.println(matriz[i][coll-1]);
          }

          out.close();

             prob += 0.1;
         }
    }

    void criaMatrizMissingAttrubuteNALinXColForAmelia(double[][] matrizOrig, String fileName){  //substitui 0 por NA para usar no R
                                                                              // 0 para n�o atrapalhar normaliza��o
         //  DecimalFormat show = new DecimalFormat("0,00");
            int line = matrizOrig.length;
            int coll = matrizOrig[0].length;
            int noTXT = fileName.length() - 4;
            String auxFileName = fileName.substring(0, noTXT);
            double prob = 0.1;
            double rand = 0;
            int atrOff = 0, cont = 0, s = 0;
            double[][] matriz;

           while(prob < 0.6){
            matriz = matrizOrig;
            matriz = shuffle(matriz);

               for(int i = 0; i < line; i++){
                  for(int j = 0; j < coll-1; j++){
                     s = (int) (Math.random()*(line));
                     rand = Math.random();
                     if(rand < prob){                                 // atributo escolhido para ter valor apagado
                       // atrOff = (int) (Math.random()*(coll-1));
                        matriz[s][j] = emptyValue;
                        cont++;
                    }
               }
               }
        //    System.out.println(cont);

            PrintWriter out;
            FileOutputStream outputStream = null;
            try{
                outputStream = new FileOutputStream("C:\\Users\\Public\\Documents\\" + auxFileName + (int)(prob*10) + "a.txt");
            } catch (java.io.IOException e){
                System.out.println("Could not create result.txt");
            }

            out = new PrintWriter(outputStream);

            for(int k = 0; k < coll - 1; k++)
               out.print("\"X"+(k+1)+ "\" ");
           out.println("\"C\"");

            for(int i = 0; i < line; i++){
                for(int j = 0; j < coll-1; j++)
                   if(matriz[i][j] != emptyValue)
                     out.print(matriz[i][j] + " ");
                   else
                     out.print("NA ");        // out.print(0 + ",");
                if(i == (line-1))
                   out.print(matriz[i][coll-1]);
                else
                   out.println(matriz[i][coll-1]);
            }

            out.close();

               prob += 0.1;
           }
      }


    public void criaConjuntosTreinoeTeste(double[][] matriz){   // cria conjunto de treino (com atr faltantes) e de teste (sem atr faltantes)

        int line = matriz.length;
        int coll = matriz[0].length;
        double[][] matrizTreino;
        double[][] matrizTeste;
        double[][] matrizValidacao;
        double[][] matrizTrVal;           //  matriz usada para treinar classificador qdo descoberto melhor parametro
        int fold = 5;
        int contTreino = 0, contTeste = 0, contValidacao = 0, it = 1, te = 1;




        for(int ncv = 1; ncv <= 10; ncv++) {    // repeti��o para o numero de rdcv ser�o realizados

            te = 1;
            matriz = shuffle(matriz);
            int[] stratification = indicesEstratificados(matriz,fold);


            while(te <= fold){               // Separa teste do conjunto cv
                // separa um conjunto para teste
                contTeste = 0;
                contTreino = 0;
                for(int i = 0; i < line; i++)
                    if(stratification[i] == te)
                        contTeste++;

                matrizTeste = new double[contTeste][coll];
                matrizTrVal = new double[line - contTeste][coll];
                contTeste = 0;

                for(int h = 0; h < line; h++)
                    if(stratification[h] == te){
                        for(int y = 0; y < coll; y++)
                            matrizTeste[contTeste][y] = matriz[h][y];
                        contTeste++;
                    }
                    else{
                        for(int y = 0; y < coll; y++)
                            matrizTrVal[contTreino][y] = matriz[h][y];
                        contTreino++;
                    }



                criaMatrizMissingAttrubuteNALinXCol(matrizTrVal, te, ncv);  // cria treinos com atributos faltantes


                PrintWriter out;
                FileOutputStream outputStream = null;
                try{
                    outputStream = new FileOutputStream("C:\\Java\\Datasets\\imputation\\toR\\R" + ncv + "teste" + te + ".txt");
                } catch (java.io.IOException e){
                    System.out.println("Could not create result.txt");
                }

                out = new PrintWriter(outputStream);


                for(int i = 0; i < contTeste; i++){
                    for(int j = 0; j < coll-1; j++)
                        if(matriz[i][j] != emptyValue)
                            out.print(matrizTeste[i][j] + ",");
                        else
                            out.print("NA,");        // out.print(0 + ",");
                    if(i == contTeste-1)
                       out.print(matrizTeste[i][coll-1]);
                    else
                       out.println(matrizTeste[i][coll-1]);
                }

                out.close();

                te++;

            }

        }

    }


    void criaMatrizMissingAttrubuteNALinXCol(double[][] matrizOrig, int it, int run){  //substitui 0 por NA para usar no R
                                                                            // 0 para n�o atrapalhar normaliza��o
       //  DecimalFormat show = new DecimalFormat("0,00");
          int line = matrizOrig.length;
          int coll = matrizOrig[0].length;
          double prob = 0.1;
          double rand = 0;
          int atrOff = 0, cont = 0, s = 0;
          double[][] matriz;

         while(prob < 0.6){
          matriz = matrizOrig;
          matriz = shuffle(matriz);

             for(int i = 0; i < line; i++){
                for(int j = 0; j < coll-1; j++){
                   s = (int) (Math.random()*(line));
                   rand = Math.random();
                   if(rand < prob){                                 // atributo escolhido para ter valor apagado
                     // atrOff = (int) (Math.random()*(coll-1));
                      matriz[s][j] = emptyValue;
                      cont++;
                  }
             }
             }
      //    System.out.println(cont);

          PrintWriter out;
          FileOutputStream outputStream = null;
          try{
              outputStream = new FileOutputStream("C:\\Java\\Datasets\\imputation\\toR\\P" + (int)(prob*10) + "\\R" + run  + "treino" + it + ".txt");
          } catch (java.io.IOException e){
              System.out.println("Could not create result.txt");
          }

          out = new PrintWriter(outputStream);


          for(int i = 0; i < line; i++){
              for(int j = 0; j < coll-1; j++)
                 if(matriz[i][j] != emptyValue)
                   out.print(matriz[i][j] + ",");
                 else
                   out.print("NA,");        // out.print(0 + ",");
           if(i == line - 1) 
              out.print(matriz[i][coll-1]);
           else
              out.println(matriz[i][coll-1]);

          }

          out.close();

             prob += 0.1;
         }
    }

    public double[][] addNoiseAtrLimits(double[][] A, double prob){


        int line = A.length;
        int coll = A[0].length;
        double[][] newE = new double[line][coll];
        int cont = 0;
        double[][] atrLimits = new double[coll-1][2];  //  [0] - menor [1] - maior
        double menor = 0, maior = 0;

        for(int i = 0; i < coll - 1; i++) {
            maior = menor = A[0][i];
            for (int j = 0; j < line; j++) {
                if (menor > A[j][i])
                    menor = A[j][i];
                if (maior < A[j][i])
                    maior = A[j][i];
            }

            atrLimits[i][0] = menor;
            atrLimits[i][1] = maior;
        }


       for(int j = 0; j < line; j++){
         for(int i = 0; i < coll-1; i++)
           if(Math.random() < prob){
             newE[j][i] = atrLimits[i][0] + Math.random()*(atrLimits[i][1] - atrLimits[i][0]);
             cont++;
           }
           else
             newE[j][i] = A[j][i];

         newE[j][coll-1] = A[j][coll-1];
       }


       return newE;

    }

    public double[][] addNoise(double[][] A, double prob){

        int line = A.length;
        int coll = A[0].length;
        double[][] newE = new double[line][coll];
        int cont = 0;

        for(int j = 0; j < line; j++){
            for(int i = 0; i < coll-1; i++)
                if(prob > Math.random()){
                    newE[j][i] = Math.random()*10;
                    cont++;
                }
                else
                    newE[j][i] = A[j][i];

            newE[j][coll-1] = A[j][coll-1];
        }


        System.out.println(cont);

        return newE;

    }

    double[] Classes;
    Networks[] networks;
    NetworkFull networksFull;
    private AttributeHandler[] vetAtrHandler;
    private static double[] thresholds;
    private static int contThreshold;
    private int numAtr, nroClasses;
    private char[] attributeType;
    // usado em imputa��o
    private double emptyValue = -10000;
    // usado em refinamento
    private int contAltAtrTR = 0, contAltAtrTE = 0, contAltClasses = 0, contMissAtr = 0, contMissAtrTE = 0;
    private double atrAltRate = 0, atrAltRateTE = 0;

}