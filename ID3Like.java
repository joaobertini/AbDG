import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Jo�o
 * Date: 11/05/2010
 * Time: 17:32:25
 * To change this template use File | Settings | File Templates.
 */
public class ID3Like {

     public ID3Like(char[] inputFile){


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


        int line = matriz.length;     // numero de elementos
        int coll = matriz[0].length;  // numero de atrbutos
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

          stratifiedCrossValidation(matriz);

    }


    
   public void crossValidationID3Like(double[][] matriz){

        int line = matriz.length;
        int coll = matriz[0].length;
        double trainClassification = 0, testClassification = 0;
        double[][] matrizTreino;
        double[][] matrizTeste;
        int part10 = line / 10;
        int i1 = 0, i2 = 0;
        int contTreino = 0, contTeste = 0, it = 0, aux = 0;
        int[] validation = new int[10];

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

            tree = new Tree(ID3MultiInterval(matrizTreino));

            trainClassification = classification(matrizTeste);
            testClassification += trainClassification; //classificationAccuracy(matrizTeste);


        } // fim do while it < 10        -- cross validation --


        System.out.println("treno " + trainClassification/10);
        System.out.println("teste " + testClassification/10);


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

       // for(int kk = 1; kk <= folds; kk++)
       //       System.out.println( kk + " " + contV[kk]);

    return indices;

    }



    public double[] stratifiedCrossValidation(double[][] matriz){

           int line = matriz.length;
           int coll = matriz[0].length;
           double[][] matrizTreino;
           double[][] matrizTeste;
           int fold = 10;
           int contTreino = 0, contTeste = 0, it = 1;
           int compAux = 0;
           double mainClassification = 0;
           double somaQuadClassification = 0, somaClassification = 0, desvio = 0, mediaClass = 0;
           double newDesvio = 0, newMediaClass = 0;
           double[] classDesvio = new double[2];



        for(int ncv = 0; ncv < 10; ncv++) {

           it = 1;
           matriz = shuffle(matriz);
           int[] stratification = indicesEstratificados(matriz,fold);


          while(it <= fold){

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


         tree = new Tree(ID3MultiInterval(matrizTreino));

         mainClassification = classification(matrizTeste);

         somaQuadClassification += Math.pow(mainClassification,2);
         somaClassification += mainClassification;


         tree = null;     
         System.gc();


        } // fim do while it < 10        -- cross validation --

    }

        System.out.println();

       desvio = Math.sqrt((somaQuadClassification - (Math.pow(somaClassification,2)/100))/99);
       mediaClass = somaClassification/100;


       System.out.println(mediaClass + "  " + desvio);// + "  " + mediaCenter + "  " + desvioCenter + "  " +  newMediaClass + "  " + newDesvio);


       classDesvio[0] = newMediaClass;
       classDesvio[1] = newDesvio;

       return classDesvio;


     }

      public double classification(double[][] testSet){

        int a = 0, line = testSet.length, coll = testSet[0].length;
        double acertos = 0;
        TreeNode treeNode;
        double classe = 0;
        for(int i = 0; i < line; i++) {
            treeNode = tree.getRoot();
            while(treeNode.getClasse() == -1){
                a = treeNode.getAttribute();
                treeNode = treeNode.findPath(testSet[i][a]);
            }
           classe = treeNode.getClasse();
           if(testSet[i][coll-1] == classe)
              acertos++;
        }

        return (acertos/line)*100;
    }

    public TreeNode ID3MultiInterval(double[][] A){

        int coll = A[0].length;
        double[][] newA;
        TreeNode treeNode = null;
        double classe = hasOneClass(A);     // retorma -1 se tem mais que uma classe; senao retorna a unica classe
        double maior = -1;
        int indMaior = -1;
        int aux =  0;

        if(classe == -1){   // possui mais de uma classe

            vetAtrHandler = null;
            System.gc();
            particionaAtributo(A);  // recria vetAtrHandler

            for(int a = 0; a < coll - 1; a++){
                vetAtrHandler[a].MDLP();
                vetAtrHandler[a].calculateIntervalGain();    // calcula ganho de informa��o de intervalo atual
                if(vetAtrHandler[a].getIntervalGain() > maior){
                   maior = vetAtrHandler[a].getIntervalGain();
                   indMaior = a;                               // encontra atributo de maior ganho
                 }
            }

//            System.out.println(maior + "  " + vetAtrHandler[indMaior].getVetAtr().length);
//            if(vetAtrHandler[indMaior].getVetAtr().length == 3)
//               System.out.println();

            if(vetAtrHandler[indMaior].getVetAtr().length > 2){  // condi�ao que assegura que novas parti�oes foram aceitas

                treeNode = new TreeNode(indMaior, vetAtrHandler[indMaior].getVetAtr());
               
                for(int i = 0; i < treeNode.getVetThreshold().length - 1; i++){
                    newA = criaTreinamento(A, indMaior, treeNode.getVetThreshold(), i);
                    if(newA.length != 0)
                       treeNode.insertAtPosition(ID3MultiInterval(newA),i);
                    else
                       System.out.println();
                }
             }
             else{
                 classe = mostCommonClass(A);
                 treeNode = new TreeNode(classe);
             }


        }
        else{
            treeNode = new TreeNode(classe);
        }

        return treeNode;
    }

    public double[][] criaTreinamento(double[][] A, int atr, double[] thresholds, int interval){
               // retorna intervalo relativo a 'intervalo' do conjunto A
        
        int line = A.length;
        int coll = A[0].length;
        int cont = 0;
        double[][] newA;
        double thr1 = thresholds[interval], thr2 = thresholds[interval + 1];


        for(int i = 0; i < line; i++)
           if(A[i][atr] >= thr1 && A[i][atr] <= thr2)
             cont++;

        if(cont == 0)
            System.out.println();

        newA = new double[cont][coll];
        cont = 0;
        for(int i = 0; i < line; i++)
           if(A[i][atr] >= thr1 && A[i][atr] <= thr2){
              for(int j = 0; j < coll; j++)
                  newA[cont][j] = A[i][j];
                  cont++;
              }

        return newA;

    }


    public void particionaAtributo(double[][] matriz){

        int line = matriz.length;
        int coll = matriz[0].length;
        double[][] MO;
        vetAtrHandler = new AttributeHandler[coll-1];

        for(int a = 0; a < coll - 1; a++){

            MO = quicksort(selecionaAtributo(matriz,a),0,line-1);     // atributo 1


            vetAtrHandler[a] = new AttributeHandler(MO, Classes);
            // vetAtrHandler[a].optmizeIntervals();       // ##### encontra intervalo otimo para particionamento que usa info gain #####


        }
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


    public double hasOneClass(double[][] A){

        int coll = A[0].length;
        double oneClass = A[0][coll - 1];

        for(int i = 1; i < A.length; i++){
           if(oneClass != A[i][coll - 1]){
              oneClass = -1;
              break;
           }
        }
       return oneClass;
    }

    public double mostCommonClass(double[][] A){

        int line = A.length;
        int coll = A[0].length;
        double[] classes = new double[nroClasses+1];
        double maior = 0;
        int indMaior = 0;

        for(int i = 0; i < line; i++)
          classes[(int)A[i][coll-1]]++;

        maior = classes[0];
        indMaior = 0;
        for(int j = 1; j < nroClasses+1; j++)
           if(classes[j] > maior){
              maior = classes[j];
              indMaior = j;
           }

      return (double)indMaior;
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
       

        return newE;
    }


  private AttributeHandler[] vetAtrHandler;
  private double[] Classes;
  private int nroClasses;
  private Tree tree;

}
