import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Joao
 * Date: 23/09/2008
 * Time: 09:03:49
 * To change this template use File | Settings | File Templates.
 */
public class DecisionTree {


    public DecisionTree(char[] inputFile){


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


    // correcao necessaria para execucao de C45  -  trocar as classes -1 por 2
    for(int c = 0; c < line; c++)
       if(matriz[c][coll-1] == -1)
          matriz[c][coll-1] = 2;

            
 // matriz = normalizacao(matriz);
 // matriz = addNoise(matriz,0.05);
 // C45(matriz);

  /*
  double[] somaClassDesvio = new double[2];
  double[] auxSoma = new double[2];



  for(int r = 0; r < 10; r++){
     auxSoma = stratifiedCrossValidation(matriz);
     somaClassDesvio[0] += auxSoma[0];
     somaClassDesvio[1] += auxSoma[1];
  }

      System.out.println(" Classification " + somaClassDesvio[0]/10  +  "  " + " desvio " + somaClassDesvio[1]/10);

   */
    //  tree = new  Tree(C45(matriz));

   //   tree.preorderTraversal();

      crossValidation(matriz);
    }


    public DecisionTree(){

       for(int l = 5000; l < 10001; l = l+1000){

        int line = l;
        int nc = 4;
        nroClasses = nc;
        int coll = 3;
        int it = 20;
        int onePart = line/nc;
        double[][] matriz = new double[line][coll];
        double somaTempo = 0, somaQuadTempo = 0, desvio = 0, menor = 100000000, maior = 0, tempo = 0;
        Random random = new Random();


        for(int j = 0; j < it; j++){

        for(int i = 0; i < line; i++){
           matriz[i][0] = Math.random();
           matriz[i][1] = Math.random();
           matriz[i][2] = Math.ceil(nc * Math.random());
        }

     //   System.out.println();

        long a =  System.currentTimeMillis();

        tree = new  Tree(C45(matriz));

        long b =  System.currentTimeMillis();

         tempo = (b-a);
         somaTempo += tempo;
         somaQuadTempo += Math.pow(tempo,2);

   //      if(tempo == 0)
   //        System.out.println(" " + it);

         if(it == 1){
             maior = tempo;
             menor = tempo;
         }


         if(tempo < menor)
            menor = tempo;

         if(tempo > maior)
           maior = tempo;


        }
       // desvio = Math.sqrt((somaQuadTempo - (Math.pow(somaTempo,2)/(double)it))/(double)(it-1));
        System.out.println( somaTempo/it  +  "  " + maior + "  " + menor);

       }
    }


      public DecisionTree(char[] inputFile1, char[] inputFile2){

        double[][] matriz = adjustFile(inputFile1);
        double[][] test = adjustFile(inputFile2);
        double[] classes;

        int line = matriz.length;     // numero de elementos
        int coll = matriz[0].length;  // numero de atrbutos
        double[] tempClass = new double[line];

        // descobre quantas classes tem o problema
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


          if(nroClasses == 2){
            for(int n = 0; n < line; n++)
                if(matriz[n][coll-1] == -1 || matriz[n][coll-1] == 0)
                    matriz[n][coll-1] = 2;

            if(Classes[0] == -1 || Classes[0] == 0)
                Classes[0] = 2;

            if(Classes[1] == -1 || Classes[1] == 0)
                Classes[1] = 2;
        }



        // ------------------------------    inicia constru��o da arvore

       tree = new  Tree(C45(matriz));

       classes = classificationRetClasses(test);

       for(int i = 0; i < classes.length ; i++)
          System.out.println(classes[i]);


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

       public double[][] addNoise(double[][] A, double noise){     // change some classes

       int line = A.length;
       int coll = A[0].length;
       int numChange = (int)(line * noise);
       int cont = 0;
       int r1 = 0, r2 = 0;
       double aux = 0;
       Random randonNumbers = new Random();

       while(cont < numChange){

          r1 = randonNumbers.nextInt(line-1);
          r2 = randonNumbers.nextInt(line-1);

          if(A[r1][coll-1] != A[r2][coll-1]){
              cont++;
              aux = A[r1][coll-1];
              A[r1][coll-1] = A[r2][coll-1];
              A[r2][coll -1] = aux;
          }
       }

     return A;

    }



    public void crossValidation(double[][] matriz){


                matriz = shuffle(matriz);

                int line = matriz.length;
                int coll = matriz[0].length;
                double[] classDesvio = new double[2];
                double[][] matrizTreino;
                double[][] matrizTeste;
                int part10 = line / 10;
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


               int nroEl = 0, somaEl = 0;
               int i1= 0, i2 = 0;

               double[] somaClasse = new double[nroClasses+1];
               double[] porcentClass = new double[nroClasses+1];
               double[] restantes = new double[nroClasses+1];


               for(int i = 0; i < line; i++)                      // numero de exemplos por classe
                  somaClasse[(int)matriz[i][coll-1]]++;

               for(int i = 0; i < nroClasses + 1; i++){              // porcentagem de exemplos em cada classe
                  porcentClass[i] = (somaClasse[i]/line);
                  restantes[i] = somaClasse[i];
               }
   

           //    ########### preenche matrizTeste e matrizTreino #####################


               double treeSomaQuadClassification = 0, treeSomaClassification = 0, treeDesvio = 0, treeMedia = 0, treeClassification = 0;
               int elClass = 0;
               int[] usedPatterns = new int[line];

               for(int i = 0; i < line; i++)
                  usedPatterns[i] = 0;       // nenhum padrao foi usado em nenhum fold  | 1 - padrao ja usado

       while(it < 10){

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

              //  ########## Classifica��o -  Fold it -  #########


               tree = new  Tree(C45(matrizTreino));
               treeClassification = classification(matrizTeste);
               treeSomaClassification += treeClassification;
               treeSomaQuadClassification += Math.pow(treeClassification,2);
               // escreve arvore
         //      tree.preorderTraversal();

               tree = null;


                System.gc();


         } // fim do while it < 10        -- cross validation --

               System.out.println();

              // new  nonParametric
              treeDesvio = Math.sqrt((treeSomaQuadClassification - (Math.pow(treeSomaClassification,2)/10))/9);
              treeMedia = treeSomaClassification/10;

              System.out.println(treeMedia + "  " + treeDesvio);


           }



    public void stratifiedCrossValidation(double[][] matriz){


            matriz = shuffle(matriz);

            int line = matriz.length;
            int coll = matriz[0].length;
            double[] classDesvio = new double[2];
            double[][] matrizTreino;
            double[][] matrizTeste;
            int part10 = line / 10;
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


           int nroEl = 0, somaEl = 0;


           double[] somaClasse = new double[nroClasses+1];
           double[] porcentClass = new double[nroClasses+1];
           double[] restantes = new double[nroClasses+1];
           int crossClass[][] = new int[10][nroClasses+1];  // matriz que armazena propor��o de elementos por classe
           int ind = 0;

           for(int i = 0; i < line; i++)                      // numero de exemplos por classe
              somaClasse[(int)matriz[i][coll-1]]++;

           for(int i = 0; i < nroClasses + 1; i++){              // porcentagem de exemplos em cada classe
              porcentClass[i] = (somaClasse[i]/line);
              restantes[i] = somaClasse[i];
           }

          // #############  calcula estratifica��o ##############

          for(int j = 0; j < 10; j++){

             somaEl = 0;
             for(int v = 1; v < nroClasses + 1; v++){
                nroEl = (int)Math.floor(porcentClass[v] * validation[j]);  // aproxima para o menor
                if(restantes[v] >= nroEl){         // caso ideal - restam mais elementos que o necessario
                   crossClass[j][v] = nroEl;
                   restantes[v] -= nroEl;
                   somaEl += nroEl;
                }
                 else if(restantes[v] > 0){
                    crossClass[j][v] = (int)restantes[v];
                    somaEl += restantes[v];
                    restantes[v] = 0;
                }
             }
              if(somaEl < validation[j]){              // manter porcentagem no conjunto restante
                  while(somaEl < validation[j]){
                      ind = (int) (1 + Math.random()*nroClasses);
                      while(restantes[ind] == 0)
                          ind = (int) (1 + Math.random()*nroClasses);
                      if(restantes[ind] > 0){
                          crossClass[j][ind]++;
                          somaEl++;
                          restantes[ind]--;
                      }
                  }
              }
              else if(somaEl > validation[j])
                  System.out.println("Inesperado");

          }


       //    ########### preenche matrizTeste e matrizTreino #####################


           double treeSomaQuadClassification = 0, treeSomaClassification = 0, treeDesvio = 0, treeMedia = 0, treeClassification = 0;
           int elClass = 0;
           int[] usedPatterns = new int[line];

           for(int i = 0; i < line; i++)
              usedPatterns[i] = 0;       // nenhum padrao foi usado em nenhum fold  | 1 - padrao ja usado

            while(it < 10){

               contTreino = 0;
               contTeste = 0;
               matrizTreino = new double[line - validation[it]][coll];
               matrizTeste = new double[validation[it]][coll];

               for(int j = 1; j < nroClasses + 1; j++){
                  elClass = 0;
                  for(int k = 0; k < line; k++)
                      if(matriz[k][coll-1] == j){
                         if(usedPatterns[k] == 0 && elClass < crossClass[it][j]){
                              for(int y = 0; y < coll; y++ )
                                 matrizTeste[contTeste][y] = matriz[k][y];
                              contTeste++;
                              usedPatterns[k] = 1;
                              elClass++;
                           }
                          else{
                             for(int y = 0; y < coll; y++ )
                                matrizTreino[contTreino][y] = matriz[k][y];
                             contTreino++;
                         }
                       }
               }


           it++;


          //  ########## Classifica��o -  Fold it -  #########

          
           tree = new  Tree(C45(matrizTreino));
           treeClassification = classification(matrizTeste);
           treeSomaClassification += treeClassification;
           treeSomaQuadClassification += Math.pow(treeClassification,2);
           // escreve arvore
     //      tree.preorderTraversal();

           tree = null;


            System.gc();


     } // fim do while it < 10        -- cross validation --

           System.out.println();

          // new  nonParametric
          treeDesvio = Math.sqrt((treeSomaQuadClassification - (Math.pow(treeSomaClassification,2)/10))/9);
          treeMedia = treeSomaClassification/10;

          System.out.println(treeMedia + "  " + treeDesvio);


       }




       // metodos relacionados ao C4.5
     public TreeNode C45(double[][] A){

           double[][] newA, newB;
           TreeNode treeNode;
           double classe = hasOneClass(A);     // retorma -1 se tem mais que uma classe; sen�o retorna a unica classe

           if(classe == -1){
               treeNode = criaNo(A);
               // false <= thr

               newA = criaTreinamento(A, treeNode.getAttribute(), treeNode.getThreshold(),false);
               newB = criaTreinamento(A, treeNode.getAttribute(), treeNode.getThreshold(),true);

               if(newA.length != 0 && newB.length != 0){

                   treeNode.insertAtLeft(C45(newA));
                   // true > que thr
                   treeNode.insertAtRight(C45(newB));
               }
               else{
                   classe = mostCommonClass(A);
                   treeNode.setClasse(classe);
               }

           }
           else{
               treeNode = new TreeNode(classe);
           }

           return treeNode;
       }

    public TreeNode criaNo(double[][] A){

        boolean newClass = false;
        int line = A.length;
        int coll = A[0].length;
        double[] tempClass = new double[line];
        int cont = 0, findThr = 0;
        double Classes[];
        double particao = 0, gain = 0;
        double mainGain = 0, threshold = 0;
        int feature = 0;


     double T = 0, T1 = 0, teste = 0, teste1 = 0, teste2 = 0;

     double[] atributoOrdenado;
     for(int a = 0; a < coll-1; a++){
        atributoOrdenado = quicksort(selecionaAtributo(A,a),0,line-1);
        if(atributoOrdenado[0] != atributoOrdenado[line-1])
         for(int i = 1; i < line-1; i++){
           particao = (atributoOrdenado[i-1] + atributoOrdenado[i])/2;
           T = i;
           T1 = line - i;
           gain = info(A,0,0,0) - ((T/line)*info(A,a,particao,1) + (T1/line)*info(A,a,particao,2));

            if(gain >= mainGain){
               mainGain = gain;
               feature = a;
               findThr = 0;
               while(particao > atributoOrdenado[findThr])
                  findThr++;
               if(findThr == 0)
                  threshold = atributoOrdenado[0];
               else
                   threshold = atributoOrdenado[findThr-1];
            }
        }
     }


   //  System.out.println("gain " + mainGain + " part " + threshold + " particao " + particao + " atributo " + feature);

      return new TreeNode(feature, threshold);
    }

   public double info(double[][] A, int atributo, double particao, int particionSide){

       double S = 0;
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
                  classes[(int)A[i][coll-1]]++;

               for(int k = 1; k < nroClasses+1; k++)
                  if(classes[k] > 0)
                     info += -1 * (classes[k]/S) * (Math.log10((classes[k]/S))/Math.log10(2));
            
               break;

           case 1:

                 for(int c = 0; c < line; c++)
                    if(A[c][atributo] <= particao){
                       S++;
                       classes[(int)A[c][coll-1]]++;
                    }

                  for(int k = 1; k < nroClasses+1; k++)
                      if(classes[k] > 0)
                         info += -1 * (classes[k]/S) * (Math.log10(classes[k]/S)/Math.log10(2));
        
               break;

           case 2:

                for(int c = 0; c < line; c++)
                    if(A[c][atributo] > particao){
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

    public double[][] criaTreinamento(double[][] A, int atributo, double threshold, boolean isGreaterThanThr){

        int line = A.length;
        int coll = A[0].length;
        int cont = 0;
        double[][] newA;

        if(!isGreaterThanThr){
            for(int i = 0; i < line; i++)
                if(A[i][atributo] <= threshold)
                    cont++;

            newA = new double[cont][coll];
            cont = 0;
            for(int i = 0; i < line; i++)
                if(A[i][atributo] <= threshold){
                    for(int j = 0; j < coll; j++)
                        newA[cont][j] = A[i][j];
                    cont++;
                }
        }
        else{
            for(int i = 0; i < line; i++)
                if(A[i][atributo] > threshold)
                    cont++;

            newA = new double[cont][coll];
            cont = 0;
            for(int i = 0; i < line; i++)
                if(A[i][atributo] > threshold){
                    for(int j = 0; j < coll; j++)
                        newA[cont][j] = A[i][j];
                    cont++;
                }
        }

        return newA;
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
        double maior = 0, indMaior = 0;

        for(int i = 0; i < line; i++)
          classes[(int)A[i][coll-1]]++;

        maior = classes[1];
        indMaior = 1;
        for(int j = 2; j < nroClasses+1; j++)
           if(classes[j] > maior){
              maior = classes[j];
              indMaior = j;
           }

      return indMaior;
    }

   public double[] quicksort(double a[], int p, int r) {
        int i;
        if (p < r) {
           i = partition(a, p, r);
           quicksort(a, p, i-1);
           quicksort(a, i+1, r);
        }
       return a;
    }

  public int partition(double a[], int p, int r)
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


    public double classification(double[][] testSet){

        int a = 0, line = testSet.length, coll = testSet[0].length;
        double acertos = 0;
        TreeNode treeNode;
        double classe = 0;
        for(int i = 0; i < line; i++) {
            treeNode = tree.getRoot();
            while(treeNode.getClasse() == -1){
                a = treeNode.getAttribute();
                if(testSet[i][a] > treeNode.getThreshold())
                    treeNode = treeNode.rightNode;
                else if(testSet[i][a] <= treeNode.getThreshold())
                treeNode = treeNode.leftNode;
           }
           classe = treeNode.getClasse();
           if(testSet[i][coll-1] == classe)
              acertos++;
        }

        return (acertos/line)*100;
    }


     public double[] classificationRetClasses(double[][] testSet){

        int a = 0, line = testSet.length, coll = testSet[0].length;
        double[] classe = new double[line];
        TreeNode treeNode;

        for(int i = 0; i < line; i++) {
            treeNode = tree.getRoot();
            while(treeNode.getClasse() == -1){
                a = treeNode.getAttribute();
                if(testSet[i][a] > treeNode.getThreshold())
                    treeNode = treeNode.rightNode;
                else if(testSet[i][a] <= treeNode.getThreshold())
                treeNode = treeNode.leftNode;
           }
           classe[i] = treeNode.getClasse();

        }

         return classe;
    }

    public double[] selecionaAtributo(double[][] A, int a){

     int line = A.length;

     double[] atributo = new double[line];

     for(int i = 0; i < line; i++)
        atributo[i] = A[i][a];

     return atributo;
 }




    public double[][] normalizacao(double[][] A){
       double normMatriz[][];
       normMatriz = new double[A.length][A[0].length];
       double norm = 0;
      // Normas = new double[A.length];

           for(int p = 0; p < A.length; p++ ){
               norm = 0;
               for(int q = 0; q < A[0].length; q++)
                   norm += Math.pow(A[p][q],2);

               norm = Math.sqrt(norm);
               for(int r = 0; r < A[0].length; r++)
                   normMatriz[p][r] = A[p][r] / norm;

         //      Normas[p] = norm;
               normMatriz[p][A[0].length -1] = A[p][A[0].length -1];
           }

       return normMatriz;
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

       /*
           // mostra newE
           for(int j = 0; j < line; j++){
               System.out.print(mask[j] + "   ");
              for(int k = 0; k < coll; k++)
                  System.out.print(newE[j][k] + " ");
               System.out.println();
            }
       */

           return newE;
       }



    Tree tree;
    int nGrau, width, height;
    private int nroClasses;
    int[][] knns;
    int[][] trteProximity;
    int[][] trtrProximity;
    int[] componentes;
    double[] componenteClasse;
    double[] purezasComponentes;
    double[][] distances;
    private double[] Classes;
    int[] image;
    int[] grau;
    int[] distComp;
    double[] vetPk, maisVotos, maisVotosCorretos;


}
