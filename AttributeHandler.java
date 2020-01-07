import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Jo�o
 * Date: 16/07/2009
 * Time: 16:25:31
 * To change this template use File | Settings | File Templates.
 */
public class AttributeHandler {    // classe com objetivo de lidar com os atributos um por vez

    public AttributeHandler(double[] thrs, double[][] atrClass, double[] Classes){
        this.intervals = thrs;        // %%%%%%%%%%%% liberar para usar histogramas
        this.atrClass = atrClass;
        this.classes = Classes;

        // exemplo
      //  for(int i = 0; i < atrClass.length; i++)
       //    System.out.println(atrClass[i][0] + " " + atrClass[i][1]);


    }

     public AttributeHandler(double[][] atrClass, double[] Classes){

        this.atrClass = atrClass;
        this.classes = Classes;

    }

    public AttributeHandler(double[] atrClass, boolean isClassLabel){  // para Flow graphs

        this.atrValues = atrClass;
        this.isClassLabel = isClassLabel;
    }


    public void histogramPartFlowGraph(int particoes){

        double maior = atrClass[0][0], menor = atrClass[0][0];
        intervals = new double[particoes];

        for(int i = 0; i < atrClass.length; i++) {
            if (atrClass[i][0] > maior)
                maior = atrClass[i][0];
            if (atrClass[i][0] < menor)
                menor = atrClass[i][0];
        }

        double part = (maior-menor)/(double)particoes;
        particoes++;
        vetAtr = new double[particoes];

        for(int i = 0; i < particoes; i++) {
            vetAtr[i] = menor + i * part;
        }

        intervals = vetAtr;
       }

    public void randomPart(){

        double[] maxVet = new double[20]; // numero maximo de intervalos
        double menor = 0, maior = 1;
        int cont = 0;
        double intMark = 0;
        maxVet[0] = menor;
        while(intMark <= maior){
            intMark = intMark + Math.random()/2;
            if(intMark <= maior) {
                cont++;
                maxVet[cont] = intMark;
            }
        }
        if(maxVet[cont] != 1){
            cont++;
            maxVet[cont] = 1;
        }

        intervals = new double[cont+1];
        vetAtr = new double[cont+1];

        for(int i = 0; i < cont+1; i++) {
            vetAtr[i] = maxVet[i];
        }

        intervals = vetAtr;
    }


    public void CategoricalFlowGraph(){

        int line = atrClass.length;
        double[] tempClass = new double[line];
        double menor;
        int indMenor = 0;

        int cont = 0;
        boolean newValue = true;

        // descobre quantas classes possui E
        for(int p = 0; p < line; p++){
            newValue = true;
            for(int q = 0; q < cont; q++)
                if(atrValues[p] == tempClass[q])
                    newValue = false;

            if(newValue){
                tempClass[cont] = atrValues[p];
                cont++;
            }
        }

        vetAtr = new double[cont+1];
        double[] auxValues = new double[cont];

        for(int j = 0; j < cont; j++) {
            menor = 10000;
            for (int i = 0; i < cont; i++)
                if (tempClass[i] < menor) {
                    menor = tempClass[i];
                    indMenor = i;
                }

            auxValues[j] = tempClass[indMenor];

            if(j == 0)
                vetAtr[j] = tempClass[indMenor];
            else
                vetAtr[j] = auxValues[j-1] + (tempClass[indMenor] - auxValues[j-1])/2;

            tempClass[indMenor] = 10000;

        }

        vetAtr[cont] = auxValues[cont-1];

        intervals = vetAtr;
    }

    public void histogramPart(int particoes){     // particiona como em um histograma de partes iguais dado pelo usuario

        //int line = atrClass.length;
        int lineValues = intervals.length;
        double inicio = intervals[0];
        double fim = intervals[lineValues-1];
        double part = (fim-inicio)/(double)particoes;
        particoes++;
        vetAtr = new double[particoes];

        for(int i = 0; i < particoes; i++)
           vetAtr[i] = inicio + i*part;

      //  System.out.println();

    }


    public void optmizeIntervals(){               //  heuristica para otimizar escolha de intervalos

        int nroInterval = intervals.length;
        double[] limits = new double[2];
        double[] bestLimits = new double[1];    // inicializacao    - verificar se sempre a divisao de intervalos resulta em maior ganho
        double[] bestSoFar = new double[2];
        int[] indices;
        double globalEntropy = datasetEntropy(intervals[0],intervals[nroInterval-1],true);    // ganho do conjunto sem parti��es
        double currentGain = 0, currentGain1 = 0, bestGain = 1-globalEntropy;  // ver
        bestSoFar[0] = intervals[0];
        bestSoFar[1] = intervals[nroInterval - 1];        // inicia com valores extremos
        boolean lim = true;

        for(int j = 1 ; j < nroInterval-1; j++){      // nao considera inicial e final
            if(lim){
            limits = new double[bestSoFar.length  + 1];
            for(int b = 0; b < bestSoFar.length; b++)
               limits[b] = bestSoFar[b];
               limits[bestSoFar.length - 1] = intervals[j];
               limits[bestSoFar.length] = bestSoFar[bestSoFar.length-1];
               // calcula para dois
            }


               if(j>1)
                 bestSoFar[bestSoFar.length - 2] = intervals[j];     // aproveita bestsofar

              System.out.print(" limits ");
             for(int l = 0; l < limits.length; l++)
                 System.out.print(" " + limits[l]);                      // teste
              System.out.println();

            System.out.print(" so far  ");
            for(int l = 0; l < bestSoFar.length; l++)
                System.out.print(" " + bestSoFar[l]);                   // teste
            System.out.println();


            // teste     #############################
            if(bestSoFar.length == 4)
               if(bestSoFar[0] == 1.1 && bestSoFar[1] == 3.0 && bestSoFar[2] == 4.9 && bestSoFar[3] == 6.7)
                     currentGain = gain(bestSoFar, globalEntropy);





               if(lim)
                  currentGain = gain(limits, globalEntropy);

               lim = true;

               if(j>1)
               currentGain1 = gain(bestSoFar, globalEntropy);
           //     System.out.println(currentGain + "  " + globalEntropy);
                if(currentGain > currentGain1 && currentGain > bestGain){ // ganho de intervalos divididos
                    bestGain = currentGain;
                    bestSoFar = new double[limits.length];
                    bestLimits = new double[limits.length];     // pois ambos variam
                    for(int k = 0; k < limits.length; k++){
                       bestSoFar[k] = limits[k];
                       bestLimits[k] = limits[k];
                    }

                   System.out.print(" BEST ");
                    for(int l = 0; l < bestLimits.length; l++)
                       System.out.print("  " + bestLimits[l]);                      // teste
                        System.out.println();


                }
              else if(currentGain1 > currentGain && currentGain1 > bestGain){     // bestSoFar melhor
                      bestGain = currentGain1;
                      bestLimits = new double[bestSoFar.length];     // pois ambos variam
                      for(int k = 0; k < bestSoFar.length; k++)
                         bestLimits[k] = bestSoFar[k];



                    System.out.print(" BEST ");
                    for(int l = 0; l < bestLimits.length; l++)
                       System.out.print(" " + bestLimits[l]);                      // teste
                        System.out.println();



                }
             else {
                    lim = false;
                    bestSoFar = new double[bestLimits.length + 1];
                    for(int k = 0; k < bestLimits.length - 1; k++)
                       bestSoFar[k] = bestLimits[k];
                       bestSoFar[bestLimits.length] = bestLimits[bestLimits.length - 1];
                }


        }

        for(int i = 0; i < bestLimits.length; i++)
           System.out.print("best " + bestLimits[i]);

        intervalGain = bestGain; 
        System.out.println("ganho " + bestGain);
        vetAtr = bestLimits;
    }


    public void optmizeIntervalsAllTests(){

        for(int s = 0; s < intervals.length; s++)
           System.out.println(intervals[s]);

        int nroInterval = intervals.length;
        double[] limits;
        double[] bestLimits = new double[1];    // inicializacao    - verificar se sempre a divisao de intervalos resulta em maior ganho
        int[] indices;
        double globalEntropy = datasetEntropy(intervals[0],intervals[nroInterval-1],true);    // ganho do conjunto sem parti��es
        double currentGain = 0, bestGain = 0;  // ver

        for(int j = 1 ; j < nroInterval - 2; j++){      // nao considera inicial e final
            limits = new double[2+j];
            limits[0] = intervals[0];  // cte

            CombinationGenerator x = new CombinationGenerator(nroInterval-2, j);

            while (x.hasMore ()) {
                // combination = new StringBuffer ();
                indices = x.getNext ();
                for (int i = 0; i < indices.length; i++) {
                    limits[i+1] = intervals[indices[i] + 1];
               //     System.out.print("int " + intervals[indices[i] +1]); // combination.append(elements[indices[i]]);
                }
            //    System.out.println();//(combination.toString ());
                limits[limits.length - 1] = intervals[nroInterval - 1];
                currentGain = gain(limits, globalEntropy);
           //     System.out.println(currentGain + "  " + globalEntropy);
                if(currentGain > bestGain){ // >= prioriza maior numero de intervalo
                    bestGain = currentGain;
                    bestLimits = new double[limits.length];
                    for(int k = 0; k < limits.length; k++)
                       bestLimits[k] = limits[k];
                }
            }
        //     System.out.println();
        }

        for(int i = 0; i < bestLimits.length; i++)
           System.out.print("best " + bestLimits[i]);

        System.out.println("ganho " + bestGain);
        vetAtr = bestLimits;
    }


     public void Categorical(){    // Metodo EDADB - para discretizar atributo; An e Cercone
          // atributos n�o numericos - como 'calor', 'frio'
          // cada possivel valor de atributo representa um intervalo

         int line = atrClass.length;
         int cutPoints = 0, cont = 0;
         double[] candidates;


        for(int i = 1; i < line; i++)
            if(atrClass[i-1][0] != atrClass[i][0])
               cutPoints++;

        candidates = new double[cutPoints];

        for(int i = 1; i < line; i++)
            if(atrClass[i-1][0] != atrClass[i][0])
              candidates[cont++] = (atrClass[i-1][0] + atrClass[i][0]) / 2;


        vetAtr = new double[cutPoints+2];               // cortes + boundaries

        vetAtr[0] = atrClass[0][0];
        for(int i = 1; i < cutPoints+1; i++)
           vetAtr[i] = candidates[i-1];

        vetAtr[cutPoints+1] = atrClass[line-1][0];


        intervals = vetAtr;
       }

    public void MDLP(){    // Metodo MDLP do Fayyad e Irani

        int line = atrClass.length;
        int cutPoints = 0;
        double[] candidates;

        for(int i = 1; i < line; i++)
           if( atrClass[i-1][0] != atrClass[i][0] &&   atrClass[i-1][1] != atrClass[i][1])
              cutPoints++;

        candidates = new double[cutPoints];
        cuts = new double[cutPoints];          // cuts encontrados e seu contador
        contCuts = 0;
        cutPoints = 0;

        for(int i = 1; i < line; i++)
           if( atrClass[i-1][0] != atrClass[i][0] && atrClass[i-1][1] != atrClass[i][1]){        // sempre que trocar mas com valor de atributo diferente
              candidates[cutPoints] = (atrClass[i-1][0] + atrClass[i][0])/2;
              cutPoints++;
           }


       splits(candidates,atrClass[0][0], atrClass[line-1][0]);    //  splitsTesting(candidates,atrClass[0][0], atrClass[line-1][0]);

        vetAtr = new double[contCuts+2];               // cortes + boundaries
        int cont = 0, indMenor = 0;
        double menor;

        for(int i = 0; i < contCuts; i++){
            menor = 100000;
            for(int j = 0; j < contCuts; j++)
               if(cuts[j] < menor && cuts[j] != -1000){
                  menor = cuts[j];
                  indMenor = j;
               }

           vetAtr[i+1] = menor;
           cuts[indMenor] = -1000;
        }
        // boundaries
        vetAtr[0] = atrClass[0][0];
        vetAtr[contCuts+1] = atrClass[line-1][0];
      //  System.out.println();

 //       for(int y = 0; y < vetAtr.length; y++)
 //          System.out.print( vetAtr[y] + " ");
 //       System.out.println();

        intervals = vetAtr;
    }


    public void splitsTesting(double[] actualCandidates, double begin, double end){
            // considera todos os candidatos com igual prioridade - pior desenpenho

        int thrLen = actualCandidates.length, indMaior = 0;
        double[] gain = new double[thrLen];
        double maior = -1000, aux = 0;
        double nextBegin = 0, nextEnd = 0;
        int cont = 0, validated = 0;
      //  double[] validatedCandidates = new double[thrLen];

        for(int i = 0; i < thrLen; i++){
            aux = MDLPC(actualCandidates[i], begin, end);
            if(aux != -1000 && aux > maior){
                cuts[i] = actualCandidates[i];
                validated++;
            }
            else
               cuts[i] = -1000;
        }
         contCuts = validated;

    }

    public void splits(double[] actualCandidates, double begin, double end){

        int thrLen = actualCandidates.length, indMaior = 0;
        double[] gain = new double[thrLen];
        double maior = -1000, aux = 0;
        double nextBegin = 0, nextEnd = 0;
        int cont = 0;

        for(int i = 0; i < thrLen; i++){
            aux = MDLPC(actualCandidates[i], begin, end);
            if(aux != -1000 && aux > maior){
                maior = aux;
                indMaior = i;
            }
        }

        if(maior != -1000){   //  MDLPC Criterion satisfied

            cuts[contCuts] = actualCandidates[indMaior];
            contCuts++;

            double[] rightCands = new double[thrLen - (indMaior +1)];
            double[] leftCands = new double[indMaior];

            nextBegin = actualCandidates[indMaior];
            nextEnd = nextBegin;

            for(int j = 0; j < thrLen; j++)
                if(j < indMaior)
                    leftCands[j] = actualCandidates[j];
                else if( j > indMaior){
                    rightCands[cont] = actualCandidates[j];
                    cont++;
                }

            splits(leftCands, begin, nextEnd);
            splits(rightCands, nextBegin, end);

        }

    }

    public double MDLPC(double thr, double begin, double end){

        int line = atrClass.length;
        double somaEl = 0, criterion = 0, gain = 0, Delta, k1 = 0, k2 = 0, k = 0;
        double entropy = 0, entropyS1 = 0, entropyS2 = 0, S1Len = 0, S2Len = 0;
        double numClass = classes.length, somaEntropia = 0;

           for(int j = 0; j < line; j++){
               if(atrClass[j][0] >= begin && atrClass[j][0] < thr){
                  S1Len++;
                  somaEl++;
               }
               else if(atrClass[j][0] > thr && atrClass[j][0] <= end){
                   S2Len++;
                   somaEl++;
               }

           }

            entropy =  datasetEntropy(begin, end, true);
            entropyS1 = datasetEntropy(begin, thr, true);
            entropyS2 = datasetEntropy(thr, end, true);

            gain = entropy - (S1Len/somaEl)*entropyS1 - (S2Len/somaEl)*entropyS2;

            k =  numberOfClass(begin,end);
            k1 = numberOfClass(begin,thr);
            k2 = numberOfClass(thr,end);


            // MDLPC Criterion calculations

            somaEntropia = k*entropy - k1*entropyS1 - k2*entropyS2;

            Delta = Math.log10(Math.pow(3,k) - 2)/Math.log10(2) - somaEntropia;

            criterion = (Math.log10(somaEl - 1)/Math.log10(2))/somaEl + Delta/somaEl;


            // MDLPC Criterion
            if(gain > criterion)
                return gain;
            else
               return -1000;   //  fake value

    }

    public void EDADB(double numClasses){    // Mtodo EDADB - para discretizar atributo; An e Cercone

           int line = atrClass.length;
           int cutPointsCand = 0;
           double[] candidates;
           double l = 1; // numero de valores distintos em A - partitiona N�O retira repeti��es

           for(int i = 1; i < line; i++){
              if(atrClass[i-1][0] != atrClass[i][0] && atrClass[i-1][1] != atrClass[i][1])
                 cutPointsCand++;
              if(atrClass[i-1][0] != atrClass[i][0])
                l++;
           }


           candidates = new double[cutPointsCand];
        //   cuts = new double[cutPoints];          // cut point candidates = boudary points
        //   contCuts = 0;
           cutPointsCand = 0;

           for(int i = 1; i < line; i++)
              if(atrClass[i-1][0] != atrClass[i][0] && atrClass[i-1][1] != atrClass[i][1]){        // sempre que trocar mas com valor de atributo diferente
                 candidates[cutPointsCand] = (atrClass[i-1][0] + atrClass[i][0])/2;
                 cutPointsCand++;
              }

          // parametros do eda-db
         int log2x = (int) Math.round(Math.log10(l)/Math.log10(2));
         int aux = (int)(numClasses * log2x);
         int m = Math.max(2,aux);
         int d = Math.max(1, log2x);

         double sizeIntd = (atrClass[line-1][0] - atrClass[0][0]) / (double)d;  // (max - min)/d   tamanho do intervalo
         int[] intervalsD = new int[d];   // vetor de intervalos do metodo eda-db - para distribuir os intervalos
         int[] numCutsInterval = new int[d];        // qi = pi * m  - numero de cuts por intervalo
         int contD = 1;
         int c = 0;
         int[] maskPoints = new int[cutPointsCand];        // VER SE PRECISA ZERAR
         int indMaior = 0, cutsFound = 0;
         double maior = 0;


        if(m < cutPointsCand){      // deve selecionar m cut points se o numero de candidatos for maior que m

         while(contD < d){              // conta cutPoints candidades por intervalo
             while(c < cutPointsCand && candidates[c] < atrClass[0][0] + sizeIntd*contD) {
               intervalsD[contD-1]++;
               c++;
             }
              contD++;
         }


       for(int i = 0; i < d; i++)
          numCutsInterval[i] = (int)Math.round((intervalsD[i]/(double)cutPointsCand) * m);
         
       double[] vetGains = new double[cutPointsCand];  // entropia � calculada para cada cutPoint
       vetGains = entropyForEveryCut(candidates);    // ganho  - selecionar pelo maior


       contD = 0;

       while(contD < d){       // seleciona m cut points dos possiveis

              c = 0;
              cutsFound = 0;
              while(c < cutPointsCand && candidates[c] < atrClass[0][0] + sizeIntd*contD)     // posiciona c no vetor
                 c++;
              while(cutsFound < numCutsInterval[contD]){
                   maior = -1000000;
                   for(int i = c; i < cutPointsCand && candidates[i] < atrClass[0][0] + sizeIntd*(contD+1); i++){
                      if(maskPoints[i] != 1 && vetGains[i] > maior){
                        indMaior = i;
                        maior = candidates[i];
                     }
                   }
                 maskPoints[indMaior] = 1;
                 cutsFound++;

                 }
             contD++;
         }

         }
          else{        // marca todos os m candidatos
            for(int i =0; i < cutPointsCand; i++)
               maskPoints[i] = 1;

            m = cutPointsCand;
            }

        vetAtr = new double[m+2];               // cortes + boundaries

        for(int i = 1; i < m+1; i++){
           for(int j = 0; j < cutPointsCand; j++)
               if(maskPoints[j] == 1){
                  vetAtr[i] = candidates[j];
                  maskPoints[j] = 0;
                  break; 
               }
        }
           // boundaries
        vetAtr[0] = atrClass[0][0];
        vetAtr[m+1] = atrClass[line-1][0];
         //  System.out.println();

    //       for(int y = 0; y < vetAtr.length; y++)
    //          System.out.print( vetAtr[y] + " ");
    //       System.out.println();

           intervals = vetAtr;
       }

    public void ChiMerge(){  // não esta pronto

        int line = atrClass.length;   // atributo já ordenado!? -considera repetição
        int cutPointsCand = 0;
        double[] candidates;
        double[] ChiCandidates;

        int cutPoint = 0;
        double maior = 0, val = 0;

        for(int i = 1; i < line; i++){
            if(atrClass[i-1][0] != atrClass[i][0])
                cutPointsCand++;

        }

        candidates = new double[cutPointsCand + 2];       // cut points      + inicio e fim
        ChiCandidates = new double[cutPointsCand + 1];

        cutPointsCand = 1;
        for(int i = 1; i < line; i++)
            if(atrClass[i-1][0] != atrClass[i][0]){        // sempre que trocar mas com valor de atributo diferente
                candidates[cutPointsCand] = (atrClass[i-1][0] +
                        atrClass[i][0])/2;
                cutPointsCand++;
            }
        candidates[0] = atrClass[0][0];
        candidates[cutPointsCand] = atrClass[line-1][0];

        do{
            maior = 0;
            for(int cut = 0; cut < candidates.length - 2; cut++){
                ChiCandidates[cut] = chiSquare(cut,cut+1, cut+2);
                if(val > maior){
                    maior = val;
                    cutPoint = cut;
                }
            }
            if(maior > 0)
                candidates = eliminaUmCutPoint(candidates, cutPoint);

        }while(maior > 0);


        vetAtr = new double[candidates.length];               // cortes         + boundaries
        for(int f = 0; f < candidates.length; f++)
            vetAtr[f] = candidates[f];

        intervals = vetAtr;

    }

    public double chiSquare(double b1, double b2, double b3){
       // boundaries comparando dois intervalos intervals >= b1 e < b3 // b1 I1 b2
        // I2 b3
        // mede ChiSquare para dois intervalos
        double numElIntClass = 0, numElInt = 0, ExpFreq = 0, chi2 = 0;
        int line = atrClass.length; // assumindo repetição de valores
        double[] elClass = new double[classes.length];

        for(int c = 0; c < classes.length; c++)
            for(int i = 0; i < line; i++)
                if(atrClass[i][1] == classes[c])
                    elClass[c]++;

        for(int j = 0; j < classes.length; j++) {
            numElIntClass = 0;
            numElInt = 0;
            for (int i = 0; i < line; i++)
                if (atrClass[i][0] >= b1 && atrClass[i][0] < b2) {
                    if (atrClass[i][1] == classes[j])
                        numElIntClass++;
                    numElInt++;
                }

            ExpFreq = (numElInt * elClass[j]) / line;

            chi2 += Math.pow((numElIntClass - ExpFreq),2)/ExpFreq;
        }


        for(int j = 0; j < classes.length; j++) {
            numElIntClass = 0;
            numElInt = 0;
            for (int i = 0; i < line; i++)
                if (atrClass[i][0] >= b2 && atrClass[i][0] < b3) {
                    if (atrClass[i][1] == classes[j])
                        numElIntClass++;
                    numElInt++;
                }

            ExpFreq = (numElInt * elClass[j]) / line;
            chi2 += Math.pow((numElIntClass - ExpFreq),2)/ExpFreq;
        }

        return chi2;

    }


    // Distance - Cerquides e Mantaras
/*
    public void Distance(){

        int line = atrClass.length;
        int cutPoints = 0;
        double[] candidates;
        double begin, end, somaij = 0;
        int nroClasses = classes.length;
        double[] vetClass = new double[nroClasses];
        double IPC = 0;


        for(int i = 1; i < line; i++)
            if(atrClass[i-1][0] != atrClass[i][0] &&  atrClass[i-1][1] != atrClass[i][1])
                cutPoints++;

        candidates = new double[cutPoints];
        cuts = new double[cutPoints];          // cuts encontrados e seu contador
        contCuts = 0;
        cutPoints = 0;

        for(int i = 1; i < line; i++)
            if(atrClass[i-1][0] != atrClass[i][0] && atrClass[i-1][1] != atrClass[i][1]){        // sempre que trocar mas com valor de atributo diferente
                candidates[cutPoints] = (atrClass[i-1][0] + atrClass[i][0])/2;
                cutPoints++;
            }

        // calcula IPC - entropia da partição definida por cutCandidates
        for(int i = 0; i < cutPoints - 1; i++) {
            begin = candidates[i];
            end = candidates[i + 1];
            vetClass = new double[nroClasses+1];
            somaij = 0;
            for (int k = 0; k < line; k++)
                if (atrClass[k][0] > begin && atrClass[k][0] <= end){   // intervalo em Pd
                    vetClass[(int) atrClass[k][1]]++; // numero de instancias que pertencem a ambos os intervalos  Pij = P(A_i inter B_j) por classe
                    somaij++;
                }
        }

        for(int h = 1; h < nroClasses; h++)
            IPC += vetClass[h]/somaij * Math.log(vetClass[h]/somaij) / Math.log(2);


        // Realiza discretização
        for(int i = 0; i < cutPoints; i++)
            dist = Dist(candidates, vetor cut sendo criado, novo ponto a tentar, numero de cortes);    //  splitsTesting(candidates,atrClass[0][0], atrClass[line-1][0]);


        vetAtr = new double[contCuts+2];               // cortes + boundaries
        int cont = 0, indMenor = 0;
        double menor;

        for(int i = 0; i < contCuts; i++){
            menor = 100000;
            for(int j = 0; j < contCuts; j++)
                if(cuts[j] < menor && cuts[j] != -1000){
                    menor = cuts[j];
                    indMenor = j;
                }

            vetAtr[i+1] = menor;
            cuts[indMenor] = -1000;
        }
        // boundaries
        vetAtr[0] = atrClass[0][0];
        vetAtr[contCuts+1] = atrClass[line-1][0];
        //  System.out.println();

        //       for(int y = 0; y < vetAtr.length; y++)
        //          System.out.print( vetAtr[y] + " ");
        //       System.out.println();

        intervals = vetAtr;
    }


    public void splitsDistance(double[] actualCandidates, double begin, double end){
        // usado em Distance
        int thrLen = actualCandidates.length, indMaior = 0; // candidates tem todos os pontos de corte
        int[] distDiv = new int[thrLen];  // vetor para armazenar os cortes encontrados por Distance - marca 0 ou 1 na posição do corte que; 1 se esta em uso.
        double maior = -1000, aux = 0;
        double distancia = 0;
        int numcuts = 0;

        // while para controlar stopping criteria
        maior = 0;
        indMaior = 0;
        for(int i = 0; i < thrLen; i++){
            if(distDiv[i] != 0)     // considera adicionar uma corte por vez
//                distancia = Dist(actualCandidates, distDiv, i, numcuts);   // calcula distancia entre partiçoes
            indMaior = i;

            if(distancia > maior){
                maior = distancia;
                indMaior = i;
            }
            distDiv[indMaior] = 1; // marca como cutpoint
            numcuts++;
        }


    }

    public double Dist(double[] cutCandidates, int[] cutDist, int posCut, int numcuts, double IPC){ // dist entre Pc (particao dada por cut candidate) e Pd (particao gerada)

        int line = atrClass.length;
        int nroClasses = classes.length;  // ver
        cutDist[posCut] = 1;
        numcuts++; // pois o ponto i foi adicionado
        int numCandidates = cutCandidates.length;
        double begin, end;
        double beginIn, endIn;  // inner loop
        double somaij = 0, somaJoint = 0, PCPD, IPD = 0;
        double[] vetClass = new double[nroClasses+1]; // vetor do tamanho de num de classes

        for(int i = 0; i < numCandidates - 1; i++)
            for(int j = 0; j < numcuts; j++) {
                somaij = 0;
                // caso geral
                begin = cutCandidates[i];
                end = cutCandidates[i + 1];
                beginIn = cutCandidates[cutDist[j]];
                endIn = cutCandidates[cutDist[j + 1]];

                // condições de fronteira
                if (i == numCandidates - 1)
                    end = atrClass[line - 1][0];
                if (cutDist[j] == 0) // primeiro cut de cutcandidate
                    beginIn = atrClass[0][0];
                if (cutDist[j] == numCandidates - 1)
                    endIn = atrClass[line - 1][0];

                for (int k = 0; k < line; k++)
                    if (atrClass[k][0] > begin && atrClass[k][0] <= end)           //    intervalo em Pc
                        if (atrClass[k][0] > beginIn && atrClass[k][0] <= endIn){   // intervalo em Pd
                            vetClass[(int) atrClass[k][1]]++; // numero de instancias que pertencem a ambos os intervalos  Pij = P(A_i inter B_j) por classe
                            somaij++;
                        }

                if(somaij != 0)
                    for(int h = 1; h < vetClass.length; h++)
                        somaJoint += vetClass[h]/somaij * Math.log(vetClass[h]/somaij) / Math.log(2);  // I(P_C inter P_D)
                // calcular P(A_i cup B_j) = Pij

            }

        PCPD = somaJoint * -1;


        // calculo de IPD
        for(int i = 0; i < numCandidates - 1; i++) {
            for (int j = 0; j < numcuts; j++) {
                begin = cutCandidates[cutDist[j]];
                end = cutCandidates[cutDist[j + 1]];
                vetClass = new double[nroClasses + 1];
                somaij = 0;

                for (int k = 0; k < line; k++)
                    if (atrClass[k][0] > begin && atrClass[k][0] <= end) {   // intervalo em Pd
                        vetClass[(int) atrClass[k][1]]++; // numero de instancias que pertencem a ambos os intervalos  Pij = P(A_i inter B_j) por classe
                        somaij++;
                    }
            }

            for (int h = 1; h < nroClasses; h++)
                IPD += vetClass[h] / somaij * Math.log(vetClass[h] / somaij) / Math.log(2);
        }

        double IPCPD = PCPD - IPD;
        double IPDPC = PCPD - IPC;


        // usado em Distance
        // I(Pc|Pd)
        // I(Pd|Pc)
        // I(Pc cup Pd)

        // I(Pa cup Pb)  Pij = Pi

        return ;

    }

   /* public double calculaIPA(double[] cutCandidates, double[] cuts, boolean isAll){
        // calcula I(PA)
        int numCandidates = cuts.length;
        int line = atrClass.length;
        double begin, end;
        double somaij = 0, somaJoint = 0;
        int nroClasses = classes.length;  // ver
        double vetClass[] = new double[nroClasses+1]; //

        // calculo de PA
        for(int i = 0; i < numCandidates - 1; i++) {
            begin = cuts[i];
            end = cuts[i + 1];
            vetClass = new double[nroClasses+1];
            somaij = 0;
            for (int k = 0; k < line; k++)
                if (atrClass[k][0] > begin && atrClass[k][0] <= end){   // intervalo em Pd
                    vetClass[(int) atrClass[k][1]]++; // numero de instancias que pertencem a ambos os intervalos  Pij = P(A_i inter B_j) por classe
                    somaij++;
                }
        }

        for(int h = 1; h < nroClasses; h++)
            somaJoint += vetClass[h]/somaij * Math.log(vetClass[h]/somaij) / Math.log(2);

    return somaJoint;
    }
*/


    public void FUSINTER(){    // Metodo FUSINTER - para discretizar atributo; Zighed et al.

              int line = atrClass.length;
              int cutPointsCand = 0;
              double[] candidates;
              double l = 1; // numero de valores distintos em A - partitiona N�O retira repeti��es
              int cutPoint = 0;  //
              double maior = 0, val = 0;

              for(int i = 1; i < line; i++){
                 if(atrClass[i-1][0] != atrClass[i][0] && atrClass[i-1][1] != atrClass[i][1])
                    cutPointsCand++;
                 if(atrClass[i-1][0] != atrClass[i][0])
                   l++;
              }


              candidates = new double[cutPointsCand + 2];       // cut points + inicio e fim

              cutPointsCand = 1;
              for(int i = 1; i < line; i++)
                 if(atrClass[i-1][0] != atrClass[i][0] && atrClass[i-1][1] != atrClass[i][1]){        // sempre que trocar mas com valor de atributo diferente
                    candidates[cutPointsCand] = (atrClass[i-1][0] + atrClass[i][0])/2;
                    cutPointsCand++;
                 }
               candidates[0] = atrClass[0][0];
               candidates[cutPointsCand] = atrClass[line-1][0];


             do {
              maior = 0;
              for(int cut = 1; cut < candidates.length - 1; cut++){
                 val = fusinterShanon(criaMatrizT(candidates),line) - fusinterShanon(criaMatrizT(eliminaUmCutPoint(candidates, cut)),line);
                 if(val > maior){
                     maior = val;
                     cutPoint = cut;
                 }
              }
              if(maior > 0)
                 candidates = eliminaUmCutPoint(candidates, cutPoint);

             }while(maior > 0);

        
           vetAtr = new double[candidates.length];               // cortes + boundaries
           for(int f = 0; f < candidates.length; f++)
              vetAtr[f] = candidates[f];

              intervals = vetAtr;
          }

    public int[][] criaMatrizT(double[] candidates){     // candidates corresponde aos cut points mais as bordas

        int[][] T;
        int nClasses = classes.length;
        int line = atrClass.length;
        int intervals = candidates.length, soma = 0;

         // cria matriz T sem juntar intervalos
           T = new int[nClasses][intervals-1];

          for(int i = 0; i < nClasses; i++)
              for(int j = 0; j < intervals-1; j++)
                  for(int l = 0; l < line; l++)
                     if(atrClass[l][0] >= candidates[j] && atrClass[l][0] <= candidates[j+1] && classes[i] == atrClass[l][1]){
                        T[i][j]++;
                        soma++;
                     }

      return T;  
    }


    double[] eliminaUmCutPoint(double[] intervals, int cut){

        int numInt = intervals.length;
        double[] newIntervals = new double[numInt-1];
        int cont = 0;

        for(int i = 0; i < numInt; i++){
            if(i != cut){
              newIntervals[cont] = intervals[i];
              cont++;
            }
        }
       return newIntervals;
    }


    double fusinterShanon(int[][] T, int N){

        int m = T.length;  // numero de clases
        int k = T[0].length;  // numero de intervalos
        double alfa = 0.975, lambda = 1;       // como em Garcia et al.2013
        double nj = 0, somaInterna = 0, aux = 0, somaExterna = 0;

       for(int j = 0; j < k; j++) {           // anda em intervalos
          // encontra Tj
          nj = 0;
          for(int q = 0; q < m; q++)
             nj += T[q][j];                 // numero de intancias no intervalo j

          somaInterna = 0; 
          for(int i = 0; i < m; i++){          // anda em classe
               aux = (T[i][j] + lambda)/(nj + m*lambda);
               somaInterna += aux*(Math.log10(aux)/Math.log10(2));
          }
           somaInterna *= -1;
         somaExterna += alfa * (nj/N) * somaInterna + (1 - alfa)* m*lambda/nj;
       }

       return somaExterna;
    }

    public double[] entropyForEveryCut(double[] candidates){

        int line = atrClass.length;
        int cdLines = candidates.length;
        double begin = atrClass[0][0];
        double end = atrClass[line-1][0];
        double entropy, entropyS1, entropyS2, S1Len, S2Len;
        double[] vetGains = new double[cdLines];

        for(int i = 0; i < cdLines; i++){
            entropy = datasetEntropy(begin, end, true);
            entropyS1 = datasetEntropy(begin, candidates[i], true);
            entropyS2 = datasetEntropy(candidates[i], end, true);

            S1Len = 0;
            S2Len = 0;
            for(int j = 0; j < candidates[i]; j++)
              S1Len++;
            S2Len = cdLines - S1Len - 1;

            vetGains[i] = entropy - (S1Len/(double)cdLines)*entropyS1 - (S2Len/(double)cdLines)*entropyS2;
         }

        return vetGains;

    }

    public double numberOfClass(double a, double b){

    double[] tempClass = new double[classes.length + 1];

    double cont = 0;

    for(int c = 0; c < classes.length; c++)
        tempClass[c] = 0;

   // descobre quantas classes possui E
   for(int p = 0; p < atrClass.length; p++)
      if(atrClass[p][0] >= a && atrClass[p][0] <= b)
         tempClass[(int)atrClass[p][1]]++;

   for(int c = 0; c < classes.length + 1; c++)
      if(tempClass[c] != 0)
         cont++;

    return cont;
    }

    public double gain(double[] limits, double globalEntropy){

        int nroIntervalos = limits.length;
        int line = atrClass.length;
        double somaEntropia = 0, Pk = 0, somaEl = 0, deltaI = 0, somaLog = 0, gain = 0, testEntropy = 0;

        for(int i = 1; i < nroIntervalos; i++){
            somaEl = 0;
            Pk = 0;
           for(int j = 0; j < line; j++){
               if(atrClass[j][0] >= limits[i-1] && atrClass[j][0] < limits[i])
                   somaEl++;
               else if(i == (nroIntervalos-1) && atrClass[j][0] == limits[i])      // condicao de borda
                      somaEl++;
           }

            Pk = (somaEl/(double)line);
            if(Pk != 0){
                if(i != 1){                         // (i != (nroIntervalos - 1))
                    testEntropy = datasetEntropy(limits[i-1], limits[i], false);
                    somaEntropia += Pk*testEntropy;
                }
                else{
                    testEntropy = datasetEntropy(limits[i-1], limits[i], true);
                    somaEntropia += Pk*testEntropy;  // verificar condi��o de fronteira final
                }

            }
            // teste
       //    deltaI += (somaEl/line)*(somaEntropia);

        }


        gain = globalEntropy - somaEntropia;

        //gain = deltaI/(-1*somaLog);

      //  gain = deltaI/(double)nroIntervalos;

        return gain;
    }

    public double datasetEntropy(double begin, double end, boolean isFirstInterval){    //isLastInterval
        int line = atrClass.length;
        int coll = atrClass[0].length;
        double contClass = 0, porcentClass = 0, entropy = 0;
        int nroClass = classes.length;
        double[] somaClass = new double[nroClass];

        for(int j = 0; j < line; j++) {
            if(isFirstInterval){
               if(atrClass[j][0] >= begin && atrClass[j][0] <= end){
                    contClass++;
                    for(int i = 0; i < nroClass; i++)
                        if(atrClass[j][coll - 1] == classes[i])
                            somaClass[i]++;
                }
            }
            else{
                if(atrClass[j][0] > begin && atrClass[j][0] <= end){
                    contClass++;
                    for(int i = 0; i < nroClass; i++)
                        if(atrClass[j][coll - 1] == classes[i])
                            somaClass[i]++;
                }
            }
        }
        
        for(int i = 0; i < nroClass; i++){
            porcentClass = somaClass[i]/contClass;
            if(porcentClass != 0)
                entropy += (porcentClass)*(Math.log10(porcentClass)/Math.log10(2));
        }

        return (-1*entropy); // (-1*gain);
    }

    public void intervalWeights(){               // calcula peso de intervalos para todas classes e para um atributo
                                                // cálculo como no papers NN e ESWA
        int line = atrClass.length;
        int nroIntervals = vetAtr.length;
        int nroClasses = classes.length;
        classesWeights = new double[nroIntervals-1][nroClasses+1];    //  intervalo / classe
        double[] somaClassInterval = new double[nroClasses +1];
        double[] jointP = new double[nroClasses +1];
        double[] somaClass = new double[classes.length +1];
        double[] PC = new double[classes.length +1];
        double somaElInterval = 0, normTerm = 0; // new double[nroIntervals];

        for(int j = 0; j < line; j++)             // P(Ci)      -   porcentagem de elementos da classe i no conjunto de treinamento
            somaClass[(int)atrClass[j][1]]++;

        for(int s = 0; s < nroClasses+1; s++)
            PC[s] = somaClass[s]/(double)line;


        for(int i = 0; i < nroIntervals-1; i++){
            somaElInterval = 0;
            normTerm = 0;
            //   px = 0;
            for(int s = 0; s < nroClasses+1; s++){
                somaClassInterval[s] = 0;
                jointP[s] = 0;
            }

            for(int j = 0; j < line; j++){
                if(i == 0){           //(nroIntervals-2)
                    if(atrClass[j][0] >= vetAtr[i] && atrClass[j][0] <= vetAtr[i+1]){
                        somaElInterval++;
            //            instancesPerInterval[i]++;
                        somaClassInterval[(int)atrClass[j][1]]++;        //   |S_x,y|
                    }
                }
                else{
                    if(atrClass[j][0] > vetAtr[i] && atrClass[j][0] <= vetAtr[i+1]){
                        somaElInterval++;            // P(D)            //   ||D||/||E||
//                        instancesPerInterval[i]++;
                        somaClassInterval[(int)atrClass[j][1]]++;        // P(D|Ci)       - elementos da classe i que estao em D
                    }
                }
            }


            for(int j = 1; j < nroClasses+1; j++)
                if(somaClass[j] != 0){
                    jointP[j] = (somaClassInterval[j]/somaClass[j])*PC[j]; // joint
                    normTerm += jointP[j];
                }
                else
                    jointP[j] = 0;

            for(int c = 1; c < nroClasses+1; c++) {
                if(normTerm == 0)
                   classesWeights[i][c] = 0;
                else
                   classesWeights[i][c] = (jointP[c]/normTerm);         //  Math.pow(normTerm,2);
            }

            System.out.println("int" + i + " " + normTerm);  // mostra termo normalizador para cada vertice - coverage
        }

           System.out.println();
    }

    public void intervalWeightsRefining(double cutW){               // calcula peso de intervalos para todas classes e para um atributo

        int line = atrClass.length;
        int nroIntervals = vetAtr.length;
        int nroClasses = classes.length;
        classesWeights = new double[nroIntervals-1][nroClasses+1];    //  intervalo / classe
        double[] somaClassInterval = new double[nroClasses +1];
        double[] jointP = new double[nroClasses +1];
        double[] somaClass = new double[classes.length +1];
        double[] PC = new double[classes.length +1];
        double somaElInterval = 0, normTerm = 0; // new double[nroIntervals];

        for(int j = 0; j < line; j++)             // P(Ci)      -   porcentagem de elementos da classe i no conjunto de treinamento
            somaClass[(int)atrClass[j][1]]++;

        for(int s = 0; s < nroClasses+1; s++)
            PC[s] = somaClass[s]/(double)line;


        for(int i = 0; i < nroIntervals-1; i++){
            somaElInterval = 0;
            normTerm = 0;
            //   px = 0;
            for(int s = 0; s < nroClasses+1; s++){
                somaClassInterval[s] = 0;
                jointP[s] = 0;
            }

            for(int j = 0; j < line; j++){
                if(i == 0){           //(nroIntervals-2)
                    if(atrClass[j][0] >= vetAtr[i] && atrClass[j][0] <= vetAtr[i+1]){
                        somaElInterval++;
                        //            instancesPerInterval[i]++;
                        somaClassInterval[(int)atrClass[j][1]]++;        //   |S_x,y|
                    }
                }
                else{
                    if(atrClass[j][0] > vetAtr[i] && atrClass[j][0] <= vetAtr[i+1]){
                        somaElInterval++;            // P(D)            //   ||D||/||E||
//                        instancesPerInterval[i]++;
                        somaClassInterval[(int)atrClass[j][1]]++;        // P(D|Ci)       - elementos da classe i que estao em D
                    }
                }
            }


            for(int j = 0; j < nroClasses+1; j++)
                if(somaClass[j] != 0){
                    jointP[j] = (somaClassInterval[j]/somaClass[j])*PC[j];
                    normTerm += jointP[j];
                }
                else
                    jointP[j] = 0;

            for(int c = 1; c < nroClasses+1; c++) {
                if(normTerm == 0)
                    classesWeights[i][c] = 0;
                else
                    classesWeights[i][c] = (jointP[c]/normTerm);         //  Math.pow(normTerm,2);
            }


            // zera pesos menores que cutW
            for(int c = 1; c < nroClasses+1; c++)
                if(classesWeights[i][c] < cutW)
                    classesWeights[i][c] = 0;

            // renormaliza
            normTerm = 0;
            for(int h = 0; h < nroClasses + 1; h++)
                normTerm += classesWeights[i][h];

            for(int h = 0; h < nroClasses + 1; h++)
                if(normTerm != 0)
                   classesWeights[i][h] /= normTerm;

        }

         System.out.println();
    }

    public void intervalReWeightsImputation(double[][] newAtrClass){               // calcula peso de intervalos para todas classes e para um atributo

          int line = newAtrClass.length;
          int nroIntervals = vetAtr.length;
          int nroClasses = classes.length;
          classesWeights = new double[nroIntervals-1][nroClasses+1];    //  intervalo / classe
          double[] somaClassInterval = new double[nroClasses +1];
          double[] jointP = new double[nroClasses +1];
          double[] somaClass = new double[classes.length +1];
          double[] PC = new double[classes.length +1];
          double somaElInterval = 0, normTerm = 0; // new double[nroIntervals];

          for(int j = 0; j < line; j++)             // P(Ci)      -   porcentagem de elementos da classe i no conjunto de treinamento
              somaClass[(int)newAtrClass[j][1]]++;

          for(int s = 0; s < nroClasses+1; s++)
              PC[s] = somaClass[s]/(double)line;


          for(int i = 0; i < nroIntervals-1; i++){
              somaElInterval = 0;
              normTerm = 0;
              //   px = 0;
              for(int s = 0; s < nroClasses+1; s++){
                  somaClassInterval[s] = 0;
                  jointP[s] = 0;
              }

              for(int j = 0; j < line; j++){
                  if(i == 0){           //(nroIntervals-2)
                      if(newAtrClass[j][0] >= vetAtr[i] && newAtrClass[j][0] <= vetAtr[i+1]){
                          somaElInterval++;
              //            instancesPerInterval[i]++;
                          somaClassInterval[(int)newAtrClass[j][1]]++;        //   |S_x,y|
                      }
                  }
                  else{
                      if(newAtrClass[j][0] > vetAtr[i] && newAtrClass[j][0] <= vetAtr[i+1]){
                          somaElInterval++;            // P(D)            //   ||D||/||E||
//                        instancesPerInterval[i]++;
                          somaClassInterval[(int)newAtrClass[j][1]]++;        // P(D|Ci)       - elementos da classe i que estao em D
                      }
                  }
              }


              for(int j = 0; j < nroClasses+1; j++)
                  if(somaClass[j] != 0){
                      jointP[j] = (somaClassInterval[j]/somaClass[j])*PC[j];
                      normTerm += jointP[j];
                  }
                  else
                      jointP[j] = 0;

              for(int c = 1; c < nroClasses+1; c++)
                  classesWeights[i][c] = (jointP[c]/normTerm);         //  Math.pow(normTerm,2);

          }

        //     System.out.println();
      }

    public void intervalWeightsEntropy(){               // calcula peso de intervalos para todas classes e para um atributo
                                                              //  considera intervalos no conjunto de treino inteiro
        int line = atrClass.length;
        int nroIntervals = vetAtr.length;
        int nroClasses = classes.length;
        instancesPerInterval = new double[nroIntervals - 1];
        classesWeights = new double[nroIntervals-1][nroClasses+1];    //  intervalo / classe
        double[] somaClassInterval = new double[nroClasses +1];
        double[] Pxy = new double[nroClasses +1];
        double[] somaClass = new double[classes.length +1];
        double[] P = new double[classes.length +1];
        double somaElInterval = 0; // new double[nroIntervals];
        double px = 0, entropy = 0;

         for(int j = 0; j < line; j++)             //  |S_y|      -  numero de elementos da classe y no conjunto de treinamento
           somaClass[(int)atrClass[j][1]]++;

        for(int s = 0; s < nroClasses+1; s++)            // P(Y)     priori da classe y
           P[s] = somaClass[s]/(double)line;


  //      for(int e = 0; e < nroClasses+1; e++)
  //        if(P[e] != 0)
  //            entropy += P[e]*(Math.log10(1/P[e])/Math.log10(2));


        for(int i = 0; i < nroIntervals-1; i++){
           somaElInterval = 0;
           px = 0;
           for(int s = 0; s < nroClasses+1; s++){
              somaClassInterval[s] = 0;
              Pxy[s] = 0;
           }

            
           for(int j = 0; j < line; j++){
              if(i == (nroIntervals-2)){
                 if(atrClass[j][0] >= vetAtr[i] && atrClass[j][0] <= vetAtr[i+1]){
                  somaElInterval++;
                  instancesPerInterval[i]++;
                  somaClassInterval[(int)atrClass[j][1]]++;        //   |S_x,y|
                  }
              }
             else{
                 if(atrClass[j][0] >= vetAtr[i] && atrClass[j][0] < vetAtr[i+1]){
                  somaElInterval++;            // P(D)            //   ||D||/||E||
                  instancesPerInterval[i]++;
                  somaClassInterval[(int)atrClass[j][1]]++;        // P(D|Ci)       - elementos da classe i que estao em D
                 }
              }
           }


         for(int e = 0; e < nroClasses+1; e++)
           if(somaClassInterval[e] != 0)
              entropy += (somaClassInterval[e]/somaElInterval)*(Math.log10(somaElInterval/somaClassInterval[e])/Math.log10(2));


            for(int j = 0; j < nroClasses+1; j++)
               if(somaClass[j] != 0){
                  Pxy[j] = somaClassInterval[j]/(double)line;  //P[j]*(somaClassInterval[j]/somaClass[j]);   antiga          // p(x,y)
                  px += Pxy[j];                                               // p(x)
               }
               else
                  Pxy[j] = 0;
            // normalizar quantidades
            somaElInterval /= (double)line;
                                    
            for(int c = 1; c < nroClasses+1; c++)
                if(Pxy[c] == 0)
                   classesWeights[i][c] = 0;
                else if(Pxy[c] == 1 || px == Pxy[c])
                      classesWeights[i][c] = 1;
                else
                   classesWeights[i][c] = Pxy[c];//px;  // entropy - Pxy[c] * (Math.log10(px/Pxy[c])/Math.log10(2));      // conditional entropy
                                          // conditional probability p(y|x) - prob de ser da classe y dado o intervalo x
        }

        //  System.out.println();
    }


    public void calculateIntervalGain(){

        int nroInterval = intervals.length; 
        double globalEntropy = datasetEntropy(intervals[0],intervals[nroInterval-1],true);

        intervalGain = gain(vetAtr,globalEntropy);

     }

    public void setVetAtr(double[] atr){
       this.vetAtr = atr;
    }

    public double[] getVetAtr(){
        return vetAtr;
    }

     public int getNumInterval(){
        return vetAtr.length - 1;
    }

    public double getWeight(double value, int classe){
     int atrLen = vetAtr.length;
     int intervalo = -1;

     for(int i = 0; i < atrLen-1; i++)
        if(value > vetAtr[i] && value <= vetAtr[i+1])
           intervalo = i;
        
        if(intervalo == -1)
           if(value <= vetAtr[0])
              intervalo = 0;
           else if (value > vetAtr[atrLen-1])
                   intervalo = atrLen-2;

        return classesWeights[intervalo][classe];
    }

    public double getWeightedInterval(double value, int classe){ // retorna peso multiplicado por confianca do intervalo
        int atrLen = vetAtr.length;
        int intervalo = -1;
        double weight;

        // for time
        double partSize = 1/(double)(atrLen-1);  // para valores normalizados entre [0,1]

        intervalo = (int)Math.floor(value/partSize);
        if(intervalo >= atrLen-1)
            intervalo--;

/*
        for(int i = 0; i < atrLen-1; i++)
            if(value > vetAtr[i] && value <= vetAtr[i+1])
                intervalo = i;

        if(intervalo == -1)
            if(value <= vetAtr[0])
                intervalo = 0;
            else if (value > vetAtr[atrLen-1])
                intervalo = atrLen-2;
*/
//        if(classesWeights[intervalo][classe] == 0)
//            System.out.println();

      //  double beta = 0.8;
       // weight = (1-beta)*classesWeights[intervalo][classe] + beta* probAccInterval[intervalo];
       // weight = classesWeights[intervalo][classe] * intervalGain; // + beta* probAccInterval[intervalo];

      //  if(probAccInterval[intervalo] > 0.5)
      //      System.out.println(intervalo + " " + classe);
            weight = classesWeights[intervalo][classe];// * probAccIntervalMat[intervalo][classe]; // * intervalGain;//* probAccInterval[intervalo];//
      //  else
          //  weight = 0;


        return weight;// ((weight > 0.2) ? weight: 0.0);
    }

    public double getWeightedIntervalRandSize(double value, int classe){ // retorna peso multiplicado por confianca do intervalo
        int atrLen = vetAtr.length;
        int intervalo = -1;
        double weight;


        for(int i = 0; i < atrLen-1; i++)
            if(value > vetAtr[i] && value <= vetAtr[i+1])
                intervalo = i;

        if(intervalo == -1)
            if(value <= vetAtr[0])
                intervalo = 0;
            else if (value > vetAtr[atrLen-1])
                intervalo = atrLen-2;

//        if(classesWeights[intervalo][classe] == 0)
//            System.out.println();

        //  double beta = 0.8;
        // weight = (1-beta)*classesWeights[intervalo][classe] + beta* probAccInterval[intervalo];
        // weight = classesWeights[intervalo][classe] * intervalGain; // + beta* probAccInterval[intervalo];

        //  if(probAccInterval[intervalo] > 0.5)
        //      System.out.println(intervalo + " " + classe);
        weight = classesWeights[intervalo][classe];// * probAccIntervalMat[intervalo][classe]; // * intervalGain;//* probAccInterval[intervalo];//
        //  else
        //  weight = 0;


        return weight;// ((weight > 0.2) ? weight: 0.0);
    }

    public double getMaxWeightInterval(double value){ // retorna o maior peso para o intervalo dentre as classes
        int atrLen = vetAtr.length;
        int intervalo = -1;
        double weight, maior;
        int numClass = classes.length;

        // for time
        double partSize = 1/(double)(atrLen-1);  // para valores normalizados entre [0,1]

        intervalo = (int)Math.floor(value/partSize);
        if(intervalo >= atrLen-1)
            intervalo--;

        maior = 0;
        for(int classe = 1; classe < numClass + 1; classe++) {
            weight = classesWeights[intervalo][classe];// * probAccIntervalMat[intervalo][classe]; // * intervalGain;//* probAccInterval[intervalo];//
            if (maior < weight)
                maior = weight;
        }
        //  weight = 0;


        return maior;// ((weight > 0.2) ? weight: 0.0);
    }

    public double getMaxWeightIntervalRandSize(double value){ // retorna o maior peso para o intervalo dentre as classes
        int atrLen = vetAtr.length;
        int intervalo = -1;
        double weight, maior;
        int numClass = classes.length;

        for(int i = 0; i < atrLen-1; i++)
            if(value > vetAtr[i] && value <= vetAtr[i+1])
                intervalo = i;

        if(intervalo == -1)
            if(value <= vetAtr[0])
                intervalo = 0;
            else if (value > vetAtr[atrLen-1])
                intervalo = atrLen-2;

        maior = 0;
        for(int classe = 1; classe < numClass + 1; classe++) {
            weight = classesWeights[intervalo][classe];// * probAccIntervalMat[intervalo][classe]; // * intervalGain;//* probAccInterval[intervalo];//
            if (maior < weight)
                maior = weight;
        }
        //  weight = 0;


        return maior;// ((weight > 0.2) ? weight: 0.0);
    }

    public double getCoverageInterval(double value){ // retorna a cobertura do intervalo
        int atrLen = vetAtr.length;
        int intervalo = -1;
        double cov;

        // for time
        double partSize = 1/(double)(atrLen-1);

        intervalo = (int)Math.floor(value/partSize);
        if(intervalo >= atrLen-1)
            intervalo--;

        cov = coverageInterval[intervalo];// * probAccIntervalMat[intervalo][classe]; // * intervalGain;//* probAccInterval[intervalo];//
        //  else
        //  weight = 0;


        return cov;// ((weight > 0.2) ? weight: 0.0);
    }


    public double getAccInterval(double value){ // retorna peso multiplicado por confianca do intervalo
        int atrLen = vetAtr.length;
        int intervalo = -1;
        double ent;

        // for time
        double partSize = 1/(double)(atrLen-1);

        intervalo = (int)Math.floor(value/partSize);
        if(intervalo >= atrLen-1)
            intervalo--;

        ent = probAccInterval[intervalo];// * probAccIntervalMat[intervalo][classe]; // * intervalGain;//* probAccInterval[intervalo];//
        //  else
        //  weight = 0;


        return ent;// ((weight > 0.2) ? weight: 0.0);
    }

    public double getEntropyInterval(double value){ // retorna entropia do intervalo
        int atrLen = vetAtr.length;
        int intervalo = -1;
        double ent;

        // for time
        double partSize = 1/(double)(atrLen-1);

        intervalo = (int)Math.floor(value/partSize);
        if(intervalo >= atrLen-1)
            intervalo--;

        ent = entropyInterval[intervalo];// * probAccIntervalMat[intervalo][classe]; // * intervalGain;//* probAccInterval[intervalo];//
        //  else
        //  weight = 0;


        return ent;// ((weight > 0.2) ? weight: 0.0);
    }

    public double getWeightMembershipFunction(double value, int classe){   // usando uma função de pertinencia para definir intervalo
        int atrLen = vetAtr.length;
        int intervalo = -1, intervalo2 = -1;
        double intSize;
        double pert1 = -1, pert2 = -1;
        double weight = 0;
        overlapIntervals = new int[1][2];   // [intervalo original][intervalo adjacente]
        membershipValues = new double[1][2];

        for(int i = 0; i < atrLen-1; i++)
            if(value > vetAtr[i] && value <= vetAtr[i+1]){
                intervalo = i;
                intSize = vetAtr[i+1] - vetAtr[i];

                if(value > (vetAtr[i] + 0.5*intSize)) {
                    if(i < atrLen-2) {             // valor maior que a metade do intervalo para o ultimo intervalo
                        pert1 =  triangularMembership(intervalo,value); // bellMembership(intervalo,value);   //trapezoidalMembership(intervalo,value);
                        pert2 =  triangularMembership(intervalo+1,value);   // bellMembership(intervalo+1,value);    //trapezoidalMembership(intervalo+1,value);
                        intervalo2 = intervalo+1;
                    }
                    }
                else{
                    if(i > 0){       // valor menor que a metade do intervalo para o primeiro intervalo
                        pert1 = triangularMembership(intervalo,value); // bellMembership(intervalo,value);  // trapezoidalMembership(intervalo,value);
                        pert2 = triangularMembership(intervalo-1,value); // bellMembership(intervalo-1,value);  // trapezoidalMembership(intervalo-1,value);
                        intervalo2 = intervalo-1;
                    }
                }
            }


        if(intervalo == -1)           // valor fora dos limites dos intervalos
            if(value <= vetAtr[0])
                intervalo = 0;
            else if (value > vetAtr[atrLen-1])
                intervalo = atrLen-2;




        if(pert1 != -1 && pert2 != -1){
           // weight = pert1*classesWeights[intervalo][classe] + pert2*classesWeights[intervalo2][classe];

            weight = Math.max(pert1*classesWeights[intervalo][classe], pert2*classesWeights[intervalo2][classe]);
            overlapIntervals[0][0] = intervalo;
            overlapIntervals[0][1] = intervalo2;
            membershipValues[0][0] = pert1;   // intervalo 1
            membershipValues[0][1] = pert2;

            if (pert1 > pert2)   {
               MaxPert = pert1;
               classWeight = classesWeights[intervalo][classe];
            }
            else {
               MaxPert = pert2;
               classWeight = classesWeights[intervalo2][classe];
            }

        }
        else{
           overlapIntervals[0][0] = intervalo;
           overlapIntervals[0][1] = intervalo;   // valor no final ou no come�o - pertence a um unico intervalo
           membershipValues[0][0] = pert1;
           membershipValues[0][1] = pert1;

           weight = classesWeights[intervalo][classe];
           MaxPert = pert1;
         //  if(classesWeights[intervalo][classe] != 0)
              classWeight = classesWeights[intervalo][classe];
        //   else
            //  classWeight = 0;


        }

        return weight;
    }


    public double getMaxPert(){
        return MaxPert;
    }

     public double getClassWeight(){
        return classWeight;
    }


    public double trapezoidalMembership(int intervalo, double value){    // fun��o de pertinencia trapezoidal  - trapmf MATLAB
       int atrLen = vetAtr.length;
       double intSize = vetAtr[intervalo+1] - vetAtr[intervalo];
       double overate = 0.5;     // taxa de sobreposi��o dos intervalos
       double cfrate = 0.25;  // taxa referente ao espa�o entre o limite do intervalo e a area onde a fun��o � 1
                               //  taxa == 0 corresponde � area onde a fun��o � 1 igual ao intervalo
       double a,b,c,d;

       double memberValue = 0;

       if(intervalo == 0){
           b = vetAtr[intervalo];
           a = b;
       }
       else{
          b = vetAtr[intervalo] + cfrate * intSize;
          a = vetAtr[intervalo] - overate * intSize;
       }

       if(intervalo == atrLen-2){
           c = vetAtr[intervalo+1];
           d = c;
       }
        else{
          c = Math.abs(vetAtr[intervalo+1] - cfrate * intSize);
          d = overate * intSize + vetAtr[intervalo+1];
        }
        // obtendo o valor de pertinencia


        if(value >= a && value < b)
          memberValue = (value - a)/(b - a);

        if(value >= b && value <= c)
           memberValue = 1;

        if(value > c && value <= d)
           memberValue = (d - value)/(d - c);


        return memberValue;

    }


     public double triangularMembership(int intervalo, double value){    // fun��o de pertinencia trapezoidal  - trapmf MATLAB
       int atrLen = vetAtr.length;
       double intSize = vetAtr[intervalo+1] - vetAtr[intervalo];
       double overate = 0.5;     // taxa de sobreposi��o dos intervalos
      
                               //  taxa == 0 corresponde � area onde a fun��o � 1 igual ao intervalo
       double a,b,c;

       double memberValue = 0;

      // obtendo o valor de pertinencia
        a = Math.abs(intSize*overate - vetAtr[intervalo]);
        b = (vetAtr[intervalo+1] + vetAtr[intervalo])/2;
        c = Math.abs(intSize*overate + vetAtr[intervalo+1]); 


        if(value >= a && value < b)
          memberValue = (value - a)/(b - a);

        if(value >= b && value <= c)
           memberValue = (c - value)/(c - b);


        return memberValue;

    }


    public double bellMembership(int intervalo, double value){    // fun��o de pertinencia trapezoidal  - trapmf MATLAB
          int atrLen = vetAtr.length;
          double intSize = vetAtr[intervalo+1] - vetAtr[intervalo];
          double overate = 0.5;     // tamanho de a
          double aux;
          double a,b,c;

          double memberValue = 0;

         a = intSize*overate;
         b = intSize;
         c = (vetAtr[intervalo+1] + vetAtr[intervalo])/2;  // centro do intervalo

         aux  = Math.pow(Math.abs((value - c)/a),2*b);
         memberValue = 1/ (1 + aux);

         return memberValue;

       }




     public void printWeightAtr(){

    for(int k = 0; k < vetAtr.length; k++)
       System.out.print(" " + vetAtr[k]);


    System.out.println();     
    for(int j = 0; j < classesWeights.length; j++) {
       for(int i = 1; i <= classes.length; i++)
          System.out.print(" " + classesWeights[j][i]);
     System.out.println();
    }

    }



    public double getIntClass(int i, int c){
        return classesWeights[i][c];  
    }

    public void normIntervalGain(double soma){
        intervalGain /= soma;
    }

    public double getIntervalGain(){
        return intervalGain;
    }

    public int[][] getOverlapIntervals(){
        return overlapIntervals;
    }

    public double[][] getMembershipValues(){
        return membershipValues;
    }


    public int getIntFromHighestWeightofClass(int classe){

        int line = classesWeights.length;  // numero de intervalos
        double maior = classesWeights[0][classe];
        int indMaior = 0;

        for(int i = 1; i < line; i++)
           if(classesWeights[i][classe] > maior){
              indMaior = i;
              maior = classesWeights[i][classe];
           }

        return indMaior;
    }

    public double getHighestWeightofClass(int classe){

        int line = classesWeights.length;  // numero de intervalos
        double maior = classesWeights[0][classe];


        for(int i = 1; i < line; i++)
           if(classesWeights[i][classe] > maior){

              maior = classesWeights[i][classe];
           }

        return maior;
    }

      public double[] getInterval(int intervalo){

        double[] edges = new double[2];

         edges[0] = vetAtr[intervalo];
         edges[1] = vetAtr[intervalo + 1];


        return edges;
   }

    public void printInterval(double value){

        DecimalFormat dec = new DecimalFormat("0.00");

       int atrLen = vetAtr.length;
       int intervalo = -1;
       double weight;

            // for time
       double partSize = 1/(double)(atrLen-1);  // para valores normalizados entre [0,1]

       intervalo = (int)Math.floor(value/partSize);
       if(intervalo >= atrLen-1)
           intervalo--;

       if(intervalo < vetAtr.length)
           System.out.print("[ " + dec.format(vetAtr[intervalo]) + ", " + dec.format(vetAtr[intervalo + 1]) + " ]");
       else
           System.out.print("[ " + dec.format(vetAtr[intervalo]) + ", "  + " )");

    }

     public double[] getIntervalFromValue(double value){
     int atrLen = vetAtr.length;
     int intervalo = -1;

     for(int i = 0; i < atrLen-1; i++)
        if(value > vetAtr[i] && value <= vetAtr[i+1])
           intervalo = i;

        if(intervalo == -1)
           if(value <= vetAtr[0])
              intervalo = 0;
           else if (value > vetAtr[atrLen-1])
                   intervalo = atrLen-2;

        return getInterval(intervalo);
    }


    // metodos para Simplified AbDG

    public void SAbDGVertexWeight(){

        int line = atrClass.length;
        total = line;
        int nroClasses = classes.length;
        int nroIntervals = vetAtr.length;
        SAbDGverticesWeights = new double[nroIntervals - 1][nroClasses + 1];    //  intervalo / classe
        SAbDGverticesValues = new double[nroIntervals - 1][nroClasses + 1];


        for(int j = 0; j < line; j++) {

            for (int i = 0; i < nroIntervals - 1; i++) {
                if (i == 0) {
                    if (atrClass[j][0] >= vetAtr[i] && atrClass[j][0] <= vetAtr[i + 1]) {
                        SAbDGverticesValues[i][(int)atrClass[j][1]]++;
                        break;
                    }
                } else {
                    if (atrClass[j][0] > vetAtr[i] && atrClass[j][0] <= vetAtr[i + 1]) {
                        SAbDGverticesValues[i][(int)atrClass[j][1]]++;            // P(D)            //   ||D||/||E||
                        break;
                    }
                }
            }

        }


        // normaliza pesos
        for(int c = 0; c < nroIntervals - 1; c++) {
            for(int d = 1; d < nroClasses + 1; d++)
            if(SAbDGverticesValues[c][d] != 0)
                SAbDGverticesWeights[c][d] = (SAbDGverticesValues[c][d] / line);
        }

        //  System.out.println();

    }


    public void updateVertexWeightSAbDG(double[][] attrCl){  // atualização de pesos do AbDG simplificado

        int line = attrCl.length;
        int nroClasses = classes.length;
        total += line;
        int nroIntervals = vetAtr.length;


        for(int j = 0; j < line; j++) {

            for (int i = 0; i < nroIntervals - 1; i++) {
                if (i == 0) {
                    if (attrCl[j][0] >= vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        SAbDGverticesValues[i][(int)atrClass[j][1]]++;
                        break;
                    }
                } else {
                    if (attrCl[j][0] > vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        SAbDGverticesValues[i][(int)atrClass[j][1]]++;
                        break;
                    }
                }
            }

        }


        // normaliza pesos
        for(int c = 0; c < nroIntervals - 1; c++) {
            for(int d = 1; d < nroClasses + 1; d++)
                if(SAbDGverticesValues[c][d] != 0)
                    SAbDGverticesWeights[c][d] = (SAbDGverticesValues[c][d] / line);
        }


        //  System.out.println();


    }

    public void intervalWeightsIncLearn(){               // calcula peso de intervalos e mantem variaveis globais para atualizar
        // cálculo como no papers NN e ESWA
        int line = atrClass.length;
        numTrain = line;
        int nroIntervals = vetAtr.length;
        int nroClasses = classes.length;
        classesWeights = new double[nroIntervals-1][nroClasses+1];    //  intervalo / classe
        somaIntervalClassIL = new double[nroIntervals-1][nroClasses +1];
        double[] jointP = new double[nroClasses +1];
        somaClassIL = new double[classes.length +1];
        double[] PC = new double[classes.length +1];
        double somaElInterval = 0, normTerm = 0; // new double[nroIntervals];
        probAccInterval = new double[nroIntervals-1];
        contaCftIntervalo = new double[nroIntervals-1];
        somaAcertoIntervalo = new double[nroIntervals-1];

        probAccIntervalMat = new double[nroIntervals-1][nroClasses+1];

        for(int j = 0; j < line; j++)             // P(Ci)      -   porcentagem de elementos da classe i no conjunto de treinamento
            somaClassIL[(int)atrClass[j][1]]++;

        for(int s = 0; s < nroClasses+1; s++)
            PC[s] = somaClassIL[s]/(double)line;

        for(int i = 0; i < nroIntervals-1; i++) {
            for (int s = 0; s < nroClasses + 1; s++) {
                somaIntervalClassIL[i][s] = 0;
               // jointIL[i][s] = 0;
            }
        }

        for(int i = 0; i < nroIntervals-1; i++){
            somaElInterval = 0;
            normTerm = 0;
            //   px = 0;


            for(int j = 0; j < line; j++){
                if(i == 0){           //(nroIntervals-2)
                    if(atrClass[j][0] >= vetAtr[i] && atrClass[j][0] <= vetAtr[i+1]){
                       // somaElInterval++;
                        //            instancesPerInterval[i]++;
                        somaIntervalClassIL[i][(int)atrClass[j][1]]++;        //   |S_x,y|
                    }
                }
                else{
                    if(atrClass[j][0] > vetAtr[i] && atrClass[j][0] <= vetAtr[i+1]){
                     //   somaElInterval++;            // P(D)            //   ||D||/||E||
//                        instancesPerInterval[i]++;
                        somaIntervalClassIL[i][(int)atrClass[j][1]]++;        // P(D|Ci)       - elementos da classe i que estao em D
                    }
                }
            }



            for(int j = 0; j < nroClasses+1; j++)
                if(somaClassIL[j] != 0){
                    jointP[j] = (somaIntervalClassIL[i][j]/somaClassIL[j])*PC[j]; // joint
                    normTerm += jointP[j];
                }
                else
                    jointP[j] = 0;

            for(int c = 1; c < nroClasses+1; c++) {
                if(normTerm == 0)
                    classesWeights[i][c] = 0;
                else
                    classesWeights[i][c] = (jointP[c]/normTerm);         //  Math.pow(normTerm,2);
            }


            probAccInterval[i] = 1; // inicia confiança de intervalo com 1


        }

        // System.out.println();
    }

    public void fastIntervalWeightsIncLearnPerClass(){               // calcula peso de intervalos e mantem variaveis globais para atualizar
        // cálculo como no papers NN e ESWA
        int line = atrClass.length;
        numTrain = line;
        int nroIntervals = vetAtr.length;
        int nroClasses = classes.length;
        classesWeights = new double[nroIntervals-1][nroClasses+1];    //  intervalo / classe
        somaIntervalClassIL = new double[nroIntervals-1][nroClasses +1];
        double[] jointP = new double[nroClasses +1];
        somaClassIL = new double[classes.length +1];
        double[] PC = new double[classes.length +1];
        double somaElInterval = 0, normTerm = 0; // new double[nroIntervals];
        probAccIntervalMat = new double[nroIntervals-1][nroClasses+1];
        contaCftIntervaloMat = new double[nroIntervals-1][nroClasses+1];
        somaAcertoIntervalo = new double[nroIntervals-1];

        probAccInterval = new double[nroIntervals-1];
        somaAcertoIntervaloMat = new double[nroIntervals-1][nroClasses+1];

        entropyInterval = new double[nroIntervals - 1];
        coverageInterval = new double[nroIntervals - 1];

        for(int j = 0; j < line; j++)             // P(Ci)      -   porcentagem de elementos da classe i no conjunto de treinamento
            somaClassIL[(int)atrClass[j][1]]++;

        for(int s = 0; s < nroClasses+1; s++)
            PC[s] = somaClassIL[s]/(double)line;

        for(int i = 0; i < nroIntervals-1; i++) {
            for (int s = 0; s < nroClasses + 1; s++) {
                somaIntervalClassIL[i][s] = 0;
                // jointIL[i][s] = 0;
            }
        }

        double partSize;
        int intervalo = 0;

    //    for(int i = 0; i < nroIntervals-1; i++){
           //somaElInterval = 0;
          //  normTerm = 0;

            for(int j = 0; j < line; j++){

                partSize = 1/(double)(vetAtr.length-1);
                intervalo = (int)Math.floor(atrClass[j][0]/partSize);
              //  System.out.println(partSize + " " + atrClass[j][0] + " " + intervalo);
                if(intervalo >= nroIntervals-1)
                    intervalo--;
                somaIntervalClassIL[intervalo][(int)atrClass[j][1]]++;

         }

        // calcula entropia com base no numero de instancias em cada intervalo
        for(int e = 0; e < nroIntervals-1; e++) {
            for (int c = 1; c < nroClasses + 1; c++)
                if(classesWeights[e][c] != 0)
                    entropyInterval[e] += (somaIntervalClassIL[e][c]/line) * (Math.log(somaIntervalClassIL[e][c]/line) / Math.log(2));
                else
                    entropyInterval[e] = 1;

            entropyInterval[e] *= -1;
        }


        for(int i = 0; i < nroIntervals-1; i++){
            normTerm = 0;

            for(int j = 0; j < nroClasses+1; j++)
                if(somaClassIL[j] != 0){
                    jointP[j] = (somaIntervalClassIL[i][j]/somaClassIL[j])*PC[j]; // joint
                    normTerm += jointP[j];
                }
                else
                    jointP[j] = 0;

            for(int c = 1; c < nroClasses+1; c++) {
                if(normTerm == 0)
                    classesWeights[i][c] = 0;
                else
                    classesWeights[i][c] = (jointP[c]/normTerm);         //  Math.pow(normTerm,2);
            }


            // calcula coverage para cada intervalo
               if(normTerm != 1)
                   coverageInterval[i] = normTerm;
               else
                   coverageInterval[i] = -2;


        }

        // calcula entropia
   /*     for(int e = 0; e < nroIntervals-1; e++) {
            for (int c = 1; c < nroClasses + 1; c++)
                if(classesWeights[e][c] != 0)
                    entropyInterval[e] += classesWeights[e][c] * (Math.log(classesWeights[e][c]) / Math.log(2));
                else
                    entropyInterval[e] = 1;

        entropyInterval[e] *= -1;
        }
*/
        for(int a = 0; a < nroIntervals - 1; a++) {
           // probAccInterval[a] = 1;
            for (int b = 1; b < nroClasses + 1; b++)
                probAccIntervalMat[a][b] = 1;
        }
        // System.out.println();

      //  normalizaClassesWeights();
    }

    public void intervalWeightsIncLearnPerClass(){               // calcula peso de intervalos e mantem variaveis globais para atualizar
        // cálculo como no papers NN e ESWA
        int line = atrClass.length;
        numTrain = line;
        int nroIntervals = vetAtr.length;
        int nroClasses = classes.length;
        classesWeights = new double[nroIntervals-1][nroClasses+1];    //  intervalo / classe
        somaIntervalClassIL = new double[nroIntervals-1][nroClasses +1];
        double[] jointP = new double[nroClasses +1];
        somaClassIL = new double[classes.length +1];
        double[] PC = new double[classes.length +1];
        double somaElInterval = 0, normTerm = 0; // new double[nroIntervals];
        probAccIntervalMat = new double[nroIntervals-1][nroClasses+1];
        contaCftIntervaloMat = new double[nroIntervals-1][nroClasses+1];
        somaAcertoIntervalo = new double[nroIntervals-1];

        probAccIntervalMat = new double[nroIntervals-1][nroClasses+1];
        somaAcertoIntervaloMat = new double[nroIntervals-1][nroClasses+1];

        for(int j = 0; j < line; j++)             // P(Ci)      -   porcentagem de elementos da classe i no conjunto de treinamento
            somaClassIL[(int)atrClass[j][1]]++;

        for(int s = 0; s < nroClasses+1; s++)
            PC[s] = somaClassIL[s]/(double)line;

        for(int i = 0; i < nroIntervals-1; i++) {
            for (int s = 0; s < nroClasses + 1; s++) {
                somaIntervalClassIL[i][s] = 0;
                // jointIL[i][s] = 0;
            }
        }

        for(int i = 0; i < nroIntervals-1; i++){
            somaElInterval = 0;
            normTerm = 0;
            //   px = 0;


            for(int j = 0; j < line; j++){
                if(i == 0){           //(nroIntervals-2)
                    if(atrClass[j][0] >= vetAtr[i] && atrClass[j][0] <= vetAtr[i+1]){
                        // somaElInterval++;
                        //            instancesPerInterval[i]++;
                        somaIntervalClassIL[i][(int)atrClass[j][1]]++;        //   |S_x,y|
                    }
                }
                else{
                    if(atrClass[j][0] > vetAtr[i] && atrClass[j][0] <= vetAtr[i+1]){
                        //   somaElInterval++;            // P(D)            //   ||D||/||E||
//                        instancesPerInterval[i]++;
                        somaIntervalClassIL[i][(int)atrClass[j][1]]++;        // P(D|Ci)       - elementos da classe i que estao em D
                    }
                }
            }



            for(int j = 0; j < nroClasses+1; j++)
                if(somaClassIL[j] != 0){
                    jointP[j] = (somaIntervalClassIL[i][j]/somaClassIL[j])*PC[j]; // joint
                    normTerm += jointP[j];
                }
                else
                    jointP[j] = 0;

            for(int c = 1; c < nroClasses+1; c++) {
                if(normTerm == 0)
                    classesWeights[i][c] = 0;
                else
                    classesWeights[i][c] = (jointP[c]/normTerm);         //  Math.pow(normTerm,2);
            }


            //  probAccInterval[i] = 1; // inicia confiança de intervalo com 1


        }
        for(int a = 0; a < nroIntervals - 1; a++)
            for(int b = 1; b < nroClasses + 1; b++)
                probAccIntervalMat[a][b] = 1;
        // System.out.println();

        normalizaClassesWeights();
    }


    public void normalizaClassesWeights(){

        int line = classesWeights.length;
        int coll = classesWeights[0].length;
        double soma;

            for(int j = 1; j < coll; j++) {  // anda em classes
                soma = 0;
                for (int i = 0; i < line; i++)
                    soma += classesWeights[i][j];
                for (int i = 0; i < line; i++)
                    if(soma != 0)
                        classesWeights[i][j] /= soma;
            }

    }

    public void updateIntervalWeightsIncLearn(double[][] attrCl){               //

        int line = attrCl.length;
        numTrain += line;  // atualiza numero de intancias processadas
        int nroClasses = classes.length;
        int nroIntervals = vetAtr.length;
        double[] PC = new double[classes.length +1];
        double[] jointP = new double[nroClasses +1];
        double normTerm;
        boolean allZero;

        for(int j = 0; j < line; j++)             // |Ci|      - numero de elementos da classe i no conjunto de treinamento
            somaClassIL[(int)attrCl[j][1]]++;

        for(int s = 0; s < nroClasses+1; s++)
            PC[s] = somaClassIL[s]/numTrain;   // P(Ci)  prob da classe i.

        for (int i = 0; i < nroIntervals - 1; i++) {
            normTerm = 0;

            for(int j = 0; j < line; j++) {
                if (i == 0) {
                    if (attrCl[j][0] >= vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        somaIntervalClassIL[i][(int) attrCl[j][1]]++;
                    //    break;
                    }
                } else {
                    if (attrCl[j][0] > vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        somaIntervalClassIL[i][(int) attrCl[j][1]]++;
                    //    break;
                    }
                }
            }


            for (int j = 0; j < nroClasses + 1; j++)
                if (somaClassIL[j] != 0) {
                    jointP[j] = (somaIntervalClassIL[i][j] / somaClassIL[j]) * PC[j]; // joint
                    normTerm += jointP[j];
                } else
                    jointP[j] = 0;

            for (int c = 1; c < nroClasses + 1; c++) {
                if (normTerm == 0)
                    classesWeights[i][c] = 0;
                else
                    classesWeights[i][c] = (jointP[c] / normTerm);         //  Math.pow(normTerm,2);
            }


            // pode ocorrer de um intervalo não conter nenhuma instancia de treinamento; neste caso coloca peso pequeno
            allZero = true;
            for (int c = 1; c < nroClasses + 1; c++)
               if(classesWeights[i][c] != 0) {
                 allZero = false;
                  break;
               }

               if(allZero){
                   for (int c = 1; c < nroClasses + 1; c++)
                       classesWeights[i][c] = 0.0001;
               }


        }

        // System.out.println();
    }

    public void updateIntervalWeightsIncLearnWeightedIntervals(double[][] attrCl, double[] lastClassifications){
        // never forget - batch
        // atualiza pesos de intervalos e considera confiabilidade de cada intervalo, usando os resultado de classificacao passados

        int line = attrCl.length;
        numTrain += line;  // atualiza numero de intancias processadas
        int nroClasses = classes.length;
        int nroIntervals = vetAtr.length;
        double[] PC = new double[classes.length +1];
        double[] jointP = new double[nroClasses +1];
        double normTerm;
        boolean allZero;

        for(int j = 0; j < line; j++)             // |Ci|      - numero de elementos da classe i no conjunto de treinamento
            somaClassIL[(int)attrCl[j][1]]++;

        for(int s = 0; s < nroClasses+1; s++)
            PC[s] = somaClassIL[s]/numTrain;   // P(Ci)  prob da classe i.

        for (int i = 0; i < nroIntervals - 1; i++) {
            normTerm = 0;

            for(int j = 0; j < line; j++) {
                if (i == 0) {
                    if (attrCl[j][0] >= vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        somaIntervalClassIL[i][(int) attrCl[j][1]]++;
                        if(attrCl[j][1] == lastClassifications[j])
                           somaAcertoIntervalo[i]++; // probAccInterval[i]
                        contaCftIntervalo[i]++;
                   //    break;
                    }
                } else {
                    if (attrCl[j][0] > vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        somaIntervalClassIL[i][(int) attrCl[j][1]]++;
                        if(attrCl[j][1] == lastClassifications[j])
                            somaAcertoIntervalo[i]++; // probAccInterval[i]
                        contaCftIntervalo[i]++;
                    //    break;
                    }
                }
            }


            for (int j = 0; j < nroClasses + 1; j++)
                if (somaClassIL[j] != 0) {
                    jointP[j] = (somaIntervalClassIL[i][j] / somaClassIL[j]) * PC[j]; // joint
                    normTerm += jointP[j];
                } else
                    jointP[j] = 0;

            for (int c = 1; c < nroClasses + 1; c++) {
                if (normTerm == 0)
                    classesWeights[i][c] = 0;
                else
                    classesWeights[i][c] = (jointP[c] / normTerm);         //  Math.pow(normTerm,2);
            }


            // pode ocorrer de um intervalo não conter nenhuma instancia de treinamento; neste caso coloca peso pequeno
            allZero = true;
            for (int c = 1; c < nroClasses + 1; c++)
                if(classesWeights[i][c] != 0) {
                    allZero = false;
                    break;
                }

            if(allZero){
                for (int c = 1; c < nroClasses + 1; c++)
                    classesWeights[i][c] = 0.0001;
            }


            // atualiza confiança de intervalo
            if(contaCftIntervalo[i] != 0)
               probAccInterval[i] = somaAcertoIntervalo[i]/contaCftIntervalo[i];

            if(somaAcertoIntervalo[i] == 0)
                probAccInterval[i] = 0.0001;  //gambiarra para não ficar 0

        }

      //   System.out.println();
    }


    public void updateIntervalWeightsIncLearnWeightedIntervalsLastBatch(double[][] attrCl, double[] lastClassifications) {
        // atualiza pesos de intervalos e considera confiabilidade de cada intervalo, usando os resultado de classificacao passados

        int line = attrCl.length;
        numTrain = line;  // atualiza numero de intancias processadas
        int nroClasses = classes.length;
        int nroIntervals = vetAtr.length;
        double[] PC = new double[classes.length + 1];
        double[] jointP = new double[nroClasses + 1];
        double normTerm;
        boolean allZero;
        double[] somaClassIL = new double[line];
        double[][] somaIntervalClassIL = new double[nroIntervals][line];


        for (int j = 0; j < line; j++) {             // |Ci|      - numero de elementos da classe i no conjunto de treinamento
            somaClassIL[(int) attrCl[j][1]]++;
        }

        for (int k = 0; k < somaAcertoIntervalo.length; k++){
            somaAcertoIntervalo[k] = 0;
           contaCftIntervalo[k] = 0;
    }

        for(int s = 0; s < nroClasses+1; s++)
            PC[s] = somaClassIL[s]/numTrain;   // P(Ci)  prob da classe i.

        for (int i = 0; i < nroIntervals - 1; i++) {
            normTerm = 0;

            for(int j = 0; j < line; j++) {
                if (i == 0) {
                    if (attrCl[j][0] >= vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        somaIntervalClassIL[i][(int) attrCl[j][1]]++;
                        if(attrCl[j][1] == lastClassifications[j])
                            somaAcertoIntervalo[i]++; // probAccInterval[i]
                        contaCftIntervalo[i]++;
                        //    break;
                    }
                } else {
                    if (attrCl[j][0] > vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        somaIntervalClassIL[i][(int) attrCl[j][1]]++;
                        if(attrCl[j][1] == lastClassifications[j])
                            somaAcertoIntervalo[i]++; // probAccInterval[i]
                        contaCftIntervalo[i]++;
                        //    break;
                    }
                }
            }


            for (int j = 0; j < nroClasses + 1; j++)
                if (somaClassIL[j] != 0) {
                    jointP[j] = (somaIntervalClassIL[i][j] / somaClassIL[j]) * PC[j]; // joint
                    normTerm += jointP[j];
                } else
                    jointP[j] = 0;

            for (int c = 1; c < nroClasses + 1; c++) {
                if (normTerm == 0)
                    classesWeights[i][c] = 0;
                else
                    classesWeights[i][c] = (jointP[c] / normTerm);         //  Math.pow(normTerm,2);
            }


            // pode ocorrer de um intervalo não conter nenhuma instancia de treinamento; neste caso coloca peso pequeno
            allZero = true;
            for (int c = 1; c < nroClasses + 1; c++)
                if(classesWeights[i][c] != 0) {
                    allZero = false;
                    break;
                }

            if(allZero){
                for (int c = 1; c < nroClasses + 1; c++)
                    classesWeights[i][c] = 0.0001;
            }


            // atualiza confiança de intervalo
            if(contaCftIntervalo[i] != 0)
                probAccInterval[i] = somaAcertoIntervalo[i]/contaCftIntervalo[i];

            if(somaAcertoIntervalo[i] == 0)
                probAccInterval[i] = 0.0001;  //gambiarra para não ficar 0

        }
        calculateIntervalGain();   // calcula ganho do intervalo para novo conjunto
        //   System.out.println();
    }


    public void updateIntervalWeightsIncLearnWeightedIntervalsLastBatchPerClass(double[][] attrCl, double[] lastClassifications) {
        // atualiza pesos de intervalos e considera confiabilidade de cada intervalo, usando os resultado de classificacao passados

        int line = attrCl.length;
        numTrain = line;  // atualiza numero de intancias processadas
        int nroClasses = classes.length;
        int nroIntervals = vetAtr.length;
        double[] PC = new double[classes.length + 1];
        double[] jointP = new double[nroClasses + 1];
        double normTerm;
        boolean allZero;
        double[] somaClassIL = new double[line];
        double[][] somaIntervalClassIL = new double[nroIntervals][line];


        for (int j = 0; j < line; j++) {             // |Ci|      - numero de elementos da classe i no conjunto de treinamento
            somaClassIL[(int) attrCl[j][1]]++;
        }

        for (int k = 0; k < nroIntervals - 1; k++)
            for(int l = 0; l < nroClasses + 1; l++){
                somaAcertoIntervaloMat[k][l] = 0;
                contaCftIntervaloMat[k][l] = 0;
        }

        for(int s = 0; s < nroClasses+1; s++)
            PC[s] = somaClassIL[s]/numTrain;   // P(Ci)  prob da classe i.

        for (int i = 0; i < nroIntervals - 1; i++) {
            normTerm = 0;

            for(int j = 0; j < line; j++) {
                if (i == 0) {
                    if (attrCl[j][0] >= vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        somaIntervalClassIL[i][(int) attrCl[j][1]]++;
                        if(attrCl[j][1] == lastClassifications[j])
                            somaAcertoIntervaloMat[i][(int) attrCl[j][1]]++; // probAccInterval[i]
                        contaCftIntervaloMat[i][(int) attrCl[j][1]]++;
                        //    break;
                    }
                } else {
                    if (attrCl[j][0] > vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        somaIntervalClassIL[i][(int) attrCl[j][1]]++;
                        if(attrCl[j][1] == lastClassifications[j])
                            somaAcertoIntervaloMat[i][(int) attrCl[j][1]]++; // probAccInterval[i]
                        contaCftIntervaloMat[i][(int) attrCl[j][1]]++;
                        //    break;
                    }
                }
            }


            for (int j = 0; j < nroClasses + 1; j++)
                if (somaClassIL[j] != 0) {
                    jointP[j] = (somaIntervalClassIL[i][j] / somaClassIL[j]) * PC[j]; // joint
                    normTerm += jointP[j];
                } else
                    jointP[j] = 0;

            for (int c = 1; c < nroClasses + 1; c++) {
                if (normTerm == 0)
                    classesWeights[i][c] = 0;
                else
                    classesWeights[i][c] = (jointP[c] / normTerm);         //  Math.pow(normTerm,2);
            }


            // pode ocorrer de um intervalo não conter nenhuma instancia de treinamento; neste caso coloca peso pequeno
            allZero = true;
            for (int c = 1; c < nroClasses + 1; c++)
                if(classesWeights[i][c] != 0) {
                    allZero = false;
                    break;
                }

            if(allZero){
                for (int c = 1; c < nroClasses + 1; c++)
                    classesWeights[i][c] = 0.0001;
            }


            // atualiza confiança de intervalo




         /*   if(contaCftIntervalo[i] != 0)
                probAccIntervalMat[i] = somaAcertoIntervalo[i]/contaCftIntervalo[i];

            if(somaAcertoIntervalo[i] == 0)
                probAccInterval[i] = 0.0001;  //gambiarra para não ficar 0
*/
        }

        for(int a = 0; a < nroIntervals - 1; a++)
            for(int b = 1; b < nroClasses + 1; b++)
                if(contaCftIntervaloMat[a][b] != 0)
                    probAccIntervalMat[a][b] = (somaAcertoIntervaloMat[a][b]/contaCftIntervaloMat[a][b]);
      //  calculateIntervalGain();   // calcula ganho do intervalo para novo conjunto
        //   System.out.println();

         normalizaClassesWeights();
    }

    public void fastUpdateIntervalWeightsIncLearnWeightedIntervalsFadingFactor(double[][] attrCl, double[] lastClassifications){
        // Fading factors
        // atualiza pesos de intervalos e considera confiabilidade de cada intervalo, usando os resultado de classificacao passados

        int line = attrCl.length;
        double alpha = 0.99; // fading rate
        //numTrain = line + alpha*numTrain; //
        numTrain += line;  // atualiza numero de intancias processadas
        int nroClasses = classes.length;
        int nroIntervals = vetAtr.length;
        double[] PC = new double[classes.length +1];
        double[] jointP = new double[nroClasses +1];
        double[] auxSomaClass = new double[somaClassIL.length];
        double[][] auxSomaInterClass = new double[nroIntervals-1][somaClassIL.length];
        double[] auxAcertoIntervalo = new double[somaAcertoIntervalo.length];
        double[] auxSomaIntervalo = new double[somaAcertoIntervalo.length];
        double normTerm;
        boolean allZero;

        contaCftIntervalo = new double[nroIntervals - 1];

        double partSize;
        int intervalo;

        for(int j = 0; j < line; j++)             // |Ci|      - numero de elementos da classe i no conjunto de treinamento
            auxSomaClass[(int)attrCl[j][1]]++;  // conta o numero de instancias por classe

        for(int s = 0; s < nroClasses+1; s++) {
            somaClassIL[s] = auxSomaClass[s] + alpha*somaClassIL[s];
            PC[s] = somaClassIL[s] / numTrain;   // P(Ci)  prob da classe i.
        }


            // zera vetores auxiliares
             for(int k = 0; k < auxAcertoIntervalo.length; k++) {
                auxAcertoIntervalo[k] = 0;
                auxSomaIntervalo[k] = 0;
            }


            for(int j = 0; j < line; j++) {

                partSize = 1/(double)(vetAtr.length-1);
                intervalo = (int)Math.floor(attrCl[j][0]/partSize);
                //  System.out.println(partSize + " " + atrClass[j][0] + " " + intervalo);

                if(intervalo >= nroIntervals-1)
                    intervalo--;

                auxSomaInterClass[intervalo][(int)attrCl[j][1]]++;
                if(attrCl[j][1] == lastClassifications[j])
                    auxAcertoIntervalo[intervalo]++;  // somaAcertoIntervalo[i]++; // probAccInterval[i]
                auxSomaIntervalo[intervalo]++;


            }



        for (int i = 0; i < nroIntervals - 1; i++) {
            normTerm = 0;

            for(int j = 0; j < auxSomaClass.length; j++)
                somaIntervalClassIL[i][j] = auxSomaInterClass[i][j] + alpha*somaIntervalClassIL[i][j];

            somaAcertoIntervalo[i] = auxAcertoIntervalo[i] + alpha*somaAcertoIntervalo[i];
            contaCftIntervalo[i] = auxSomaIntervalo[i] + alpha*contaCftIntervalo[i];


            for (int j = 0; j < nroClasses + 1; j++)
                if (somaClassIL[j] != 0) {
                    jointP[j] = (somaIntervalClassIL[i][j] / somaClassIL[j]) * PC[j]; // joint
                    normTerm += jointP[j];
                } else
                    jointP[j] = 0;

            for (int c = 1; c < nroClasses + 1; c++) {
                if (normTerm == 0)
                    classesWeights[i][c] = 0;
                else
                    classesWeights[i][c] = (jointP[c] / normTerm);         //  Math.pow(normTerm,2);
            }


            // pode ocorrer de um intervalo não conter nenhuma instancia de treinamento; neste caso coloca peso pequeno
            allZero = true;
            for (int c = 1; c < nroClasses + 1; c++)
                if(classesWeights[i][c] != 0) {
                    allZero = false;
                    break;
                }

            if(allZero){
                for (int c = 1; c < nroClasses + 1; c++)
                    classesWeights[i][c] = 0.0001;
            }


            // atualiza confiança de intervalo
      /*      if(contaCftIntervalo[i] != 0)
                probAccInterval[i] = somaAcertoIntervalo[i]/contaCftIntervalo[i];

            if(somaAcertoIntervalo[i] == 0)
                probAccInterval[i] = 0.0001;  //gambiarra para não ficar 0
*/
        }

        //   System.out.println();
    }

    public void fastUpdateIntervalWeightsIncLearnWeightedIntervalsFadingFactorOnWeights(double[][] attrCl, double[] lastClassifications, double alpha){
        // Fading factors
        // faz alteração diretamente nos pesos. classesWeights[i][c] = (jointP[c] / normTerm) + alpha*classesWeights[i][c];

        int line = attrCl.length;
      //  double alpha = 0.99; // fading rate
        //numTrain = line + alpha*numTrain; //
        numTrain = line + alpha*numTrain;  // atualiza numero de intancias processadas
        int nroClasses = classes.length;
        int nroIntervals = vetAtr.length;
        double[] PC = new double[classes.length +1];
        double[] jointP = new double[nroClasses +1];
        double[] auxSomaClass = new double[somaClassIL.length];
        double[][] auxSomaInterClass = new double[nroIntervals-1][somaClassIL.length];
        double[] auxAcertoIntervalo = new double[somaAcertoIntervalo.length];
        double[] auxSomaIntervalo = new double[somaAcertoIntervalo.length];
        double normTerm;
        boolean allZero;

        contaCftIntervalo = new double[nroIntervals - 1];

        entropyInterval = new double[nroIntervals - 1];
        coverageInterval = new double[nroIntervals - 1];

        double partSize;
        int intervalo;

        for(int j = 0; j < line; j++)             // |Ci|      - numero de elementos da classe i no conjunto de treinamento
            auxSomaClass[(int)attrCl[j][1]]++;  // conta o numero de instancias por classe

        for(int s = 0; s < nroClasses+1; s++) {
            somaClassIL[s] = auxSomaClass[s] + alpha*somaClassIL[s];
            PC[s] = somaClassIL[s] / numTrain;   // P(Ci)  prob da classe i.
        }


        // zera vetores auxiliares
        for(int k = 0; k < auxAcertoIntervalo.length; k++) {
            auxAcertoIntervalo[k] = 0;
            auxSomaIntervalo[k] = 0;
        }


        for(int j = 0; j < line; j++) {

            partSize = 1/(double)(vetAtr.length-1);
            intervalo = (int)Math.floor(attrCl[j][0]/partSize);
            //  System.out.println(partSize + " " + atrClass[j][0] + " " + intervalo);

            if(intervalo >= nroIntervals-1)
                intervalo--;

            auxSomaInterClass[intervalo][(int)attrCl[j][1]]++;
            if(attrCl[j][1] == lastClassifications[j])
                auxAcertoIntervalo[intervalo]++;  // somaAcertoIntervalo[i]++; // probAccInterval[i]
            auxSomaIntervalo[intervalo]++;


        }


        for (int i = 0; i < nroIntervals - 1; i++) {
            normTerm = 0;

            for(int j = 0; j < auxSomaClass.length; j++)
                somaIntervalClassIL[i][j] = auxSomaInterClass[i][j] + alpha*somaIntervalClassIL[i][j];


            // calcula entropia
            for(int e = 0; e < nroIntervals-1; e++) {
                for (int c = 1; c < nroClasses + 1; c++)
                    entropyInterval[e] += (somaIntervalClassIL[e][c]/numTrain) * (Math.log(somaIntervalClassIL[e][c]/numTrain) / Math.log(2));

                entropyInterval[e] *= -1;
            }

           // somaAcertoIntervalo[i] = auxAcertoIntervalo[i] + alpha*somaAcertoIntervalo[i];
           // contaCftIntervalo[i] = auxSomaIntervalo[i] + alpha*contaCftIntervalo[i];

            //teste  - acuracia é medida somente no ultimo batch
            if(auxSomaIntervalo[i] != 0)
                probAccInterval[i] = auxAcertoIntervalo[i]/auxSomaIntervalo[i]; //  + alpha*probAccInterval[i];


            for (int j = 0; j < nroClasses + 1; j++)
                if (auxSomaClass[j] != 0) {
                    jointP[j] = (somaIntervalClassIL[i][j] / somaClassIL[j]) * PC[j]; // joint  (auxSomaInterClass[i][j] / auxSomaClass[j]) * PC[j]; // joint
                    normTerm += jointP[j];
                } else
                    jointP[j] = 0;

            for (int c = 1; c < nroClasses + 1; c++) {
                if (normTerm != 0)
                    classesWeights[i][c] = (jointP[c] / normTerm);// + alpha*classesWeights[i][c];         //  Math.pow(normTerm,2);
            }


            // calcula coverage para cada intervalo
              if(normTerm != 1)
                coverageInterval[i] = normTerm;
              else
                coverageInterval[i] = 0;


        }

        // calcula entropia
   /*     for(int e = 0; e < nroIntervals-1; e++) {
            for (int c = 1; c < nroClasses + 1; c++)
                entropyInterval[e] += classesWeights[e][c] * (Math.log(classesWeights[e][c]) / Math.log(2));

            entropyInterval[e] *= -1;
        }
*/
      //  normalizaClassesWeights();
        //   System.out.println();
    }

    public void fastUpdateIntervalWeightsIncLearnWeightedIntervalsFadingFactorRandSize(double[][] attrCl, double[] lastClassifications, double alpha){
        // Fading factors
        // faz alteração diretamente nos pesos. classesWeights[i][c] = (jointP[c] / normTerm) + alpha*classesWeights[i][c];

        int line = attrCl.length;
        //  double alpha = 0.99; // fading rate
        //numTrain = line + alpha*numTrain; //
        numTrain = line + alpha*numTrain;  // atualiza numero de intancias processadas
        int nroClasses = classes.length;
        int nroIntervals = vetAtr.length;
        double[] PC = new double[classes.length +1];
        double[] jointP = new double[nroClasses +1];
        double[] auxSomaClass = new double[somaClassIL.length];
        double[][] auxSomaInterClass = new double[nroIntervals-1][somaClassIL.length];
        double[] auxAcertoIntervalo = new double[somaAcertoIntervalo.length];
        double[] auxSomaIntervalo = new double[somaAcertoIntervalo.length];
        double normTerm;
        boolean allZero;

        contaCftIntervalo = new double[nroIntervals - 1];

        entropyInterval = new double[nroIntervals - 1];
        coverageInterval = new double[nroIntervals - 1];

        double partSize;
        int intervalo;

        for(int j = 0; j < line; j++)             // |Ci|      - numero de elementos da classe i no conjunto de treinamento
            auxSomaClass[(int)attrCl[j][1]]++;  // conta o numero de instancias por classe

        for(int s = 0; s < nroClasses+1; s++) {
            somaClassIL[s] = auxSomaClass[s] + alpha*somaClassIL[s];
            PC[s] = somaClassIL[s] / numTrain;   // P(Ci)  prob da classe i.
        }


        // zera vetores auxiliares
        for(int k = 0; k < auxAcertoIntervalo.length; k++) {
            auxAcertoIntervalo[k] = 0;
            auxSomaIntervalo[k] = 0;
        }



/*
        for(int j = 0; j < line; j++) {

            partSize = 1/(double)(vetAtr.length-1);
            intervalo = (int)Math.floor(attrCl[j][0]/partSize);
            //  System.out.println(partSize + " " + atrClass[j][0] + " " + intervalo);

            if(intervalo >= nroIntervals-1)
                intervalo--;

            auxSomaInterClass[intervalo][(int)attrCl[j][1]]++;
            if(attrCl[j][1] == lastClassifications[j])
                auxAcertoIntervalo[intervalo]++;  // somaAcertoIntervalo[i]++; // probAccInterval[i]
            auxSomaIntervalo[intervalo]++;


        }
*/

        for (int i = 0; i < nroIntervals - 1; i++) {
            normTerm = 0;


            for(int j = 0; j < line; j++) {
                if (i == 0) {
                    if (attrCl[j][0] >= vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        auxSomaInterClass[i][(int) attrCl[j][1]]++;
                        if(attrCl[j][1] == lastClassifications[j])
                            auxAcertoIntervalo[i]++; // somaAcertoIntervaloMat[i][(int) attrCl[j][1]]++; // probAccInterval[i]
                        auxSomaIntervalo[i]++; // contaCftIntervaloMat[i][(int) attrCl[j][1]]++;
                        //    break;
                    }
                } else {
                    if (attrCl[j][0] > vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        auxSomaInterClass[i][(int) attrCl[j][1]]++;// somaIntervalClassIL[i][(int) attrCl[j][1]]++;
                        if(attrCl[j][1] == lastClassifications[j])
                            auxAcertoIntervalo[i]++;//somaAcertoIntervaloMat[i][(int) attrCl[j][1]]++; // probAccInterval[i]
                        auxSomaIntervalo[i]++; //contaCftIntervaloMat[i][(int) attrCl[j][1]]++;
                        //    break;
                    }
                }
            }



            for(int j = 0; j < auxSomaClass.length; j++)
                somaIntervalClassIL[i][j] = auxSomaInterClass[i][j] + alpha*somaIntervalClassIL[i][j];


            // calcula entropia
            for(int e = 0; e < nroIntervals-1; e++) {
                for (int c = 1; c < nroClasses + 1; c++)
                    entropyInterval[e] += (somaIntervalClassIL[e][c]/numTrain) * (Math.log(somaIntervalClassIL[e][c]/numTrain) / Math.log(2));

                entropyInterval[e] *= -1;
            }

            // somaAcertoIntervalo[i] = auxAcertoIntervalo[i] + alpha*somaAcertoIntervalo[i];
            // contaCftIntervalo[i] = auxSomaIntervalo[i] + alpha*contaCftIntervalo[i];

            //teste  - acuracia é medida somente no ultimo batch
            if(auxSomaIntervalo[i] != 0)
                probAccInterval[i] = auxAcertoIntervalo[i]/auxSomaIntervalo[i]; //  + alpha*probAccInterval[i];


            for (int j = 0; j < nroClasses + 1; j++)
                if (auxSomaClass[j] != 0) {
                    jointP[j] = (somaIntervalClassIL[i][j] / somaClassIL[j]) * PC[j]; // joint  (auxSomaInterClass[i][j] / auxSomaClass[j]) * PC[j]; // joint
                    normTerm += jointP[j];
                } else
                    jointP[j] = 0;

            for (int c = 1; c < nroClasses + 1; c++) {
                if (normTerm != 0)
                    classesWeights[i][c] = (jointP[c] / normTerm);// + alpha*classesWeights[i][c];         //  Math.pow(normTerm,2);
            }


            // calcula coverage para cada intervalo
            if(normTerm != 1)
                coverageInterval[i] = normTerm;
            else
                coverageInterval[i] = 0;


        }

        // calcula entropia
   /*     for(int e = 0; e < nroIntervals-1; e++) {
            for (int c = 1; c < nroClasses + 1; c++)
                entropyInterval[e] += classesWeights[e][c] * (Math.log(classesWeights[e][c]) / Math.log(2));

            entropyInterval[e] *= -1;
        }
*/
        //  normalizaClassesWeights();
        //   System.out.println();
    }
    public void fastUpdateIntervalWeightsKullbackLeibler(double[][] attrCl, double[] lastClassifications){
        // Testa média de cada intervalo para considerar atualizar
        // atualiza pesos de intervalos e considera confiabilidade de cada intervalo, usando os resultado de classificacao passados

        int line = attrCl.length;
        //  double alpha = 0.99; // fading rate
        //numTrain = line + alpha*numTrain; //
        // numTrain += line;  // atualiza numero de intancias processadas
        int nroClasses = classes.length;
        int nroIntervals = vetAtr.length;
        double[] PC = new double[classes.length +1];
        double[] jointP = new double[nroClasses +1];
        double[] auxSomaClass = new double[somaClassIL.length];
        double[][] auxSomaInterClass = new double[nroIntervals-1][somaClassIL.length];
        double[] auxAcertoIntervalo = new double[somaAcertoIntervalo.length];
        double[] auxSomaIntervalo = new double[somaAcertoIntervalo.length];
        double normTerm;
        boolean hasChanged = false;

        double partSize;
        int intervalo;

        for(int j = 0; j < line; j++)             // |Ci|      - numero de elementos da classe i no conjunto de treinamento
            auxSomaClass[(int)attrCl[j][1]]++;  // conta o numero de instancias por classe

        for(int s = 0; s < nroClasses+1; s++) {
            // somaClassIL[s] = auxSomaClass[s] + alpha*somaClassIL[s];
            PC[s] = auxSomaClass[s]/line; // somaClassIL[s] / numTrain;   // P(Ci)  prob da classe i.
        }

        // zera vetores auxiliares
        for(int k = 0; k < auxAcertoIntervalo.length; k++) {
            auxAcertoIntervalo[k] = 0;
            auxSomaIntervalo[k] = 0;
        }

        partSize = 1/(double)(vetAtr.length-1);

        for(int j = 0; j < line; j++) {
            intervalo = (int)Math.floor(attrCl[j][0]/partSize);
            //  System.out.println(partSize + " " + atrClass[j][0] + " " + intervalo);

            if(intervalo >= nroIntervals-1)
                intervalo--;

            auxSomaInterClass[intervalo][(int)attrCl[j][1]]++;    // numero de el. por intervalo e classe - dividir por line
            auxSomaIntervalo[intervalo]++;
        }

        // KL test
        double soma = 0;
        double thrs = 0.1;
        double aux1, aux2;

        for(int j = 1; j < nroClasses + 1; j++) {
            soma = 0;
            for(int i = 0; i < nroIntervals-1; i++){
                aux1 = auxSomaInterClass[i][j]/line;  // P(x)
                aux2 = somaIntervalClassIL[i][j]/line;  // Q(x)
                if(aux1 != 0 && aux2 != 0){
                    soma += aux1*Math.log(aux1/aux2);
                }
                else if(aux1 != 0 && aux2 == 0)
                    soma += aux1*Math.log(aux1/0.00000001);

            }

           System.out.println(soma);
            if(soma > thrs)
                hasChanged = true;
                break;
        }


        contaCftIntervalo = new double[nroIntervals - 1];

        entropyInterval = new double[nroIntervals - 1];
        coverageInterval = new double[nroIntervals - 1];
        double alpha = 0.998;

        if(hasChanged) {
            //System.out.println("Retrain");
            for (int i = 0; i < nroIntervals - 1; i++) {
                normTerm = 0;

                for (int j = 0; j < auxSomaClass.length; j++)
                    somaIntervalClassIL[i][j] = auxSomaInterClass[i][j];// +  somaIntervalClassIL[i][j];

                // somaAcertoIntervalo[i] = auxAcertoIntervalo[i] + alpha*somaAcertoIntervalo[i];
                // contaCftIntervalo[i] = auxSomaIntervalo[i] + alpha*contaCftIntervalo[i];

                //teste  - acuracia é medida somente no ultimo batch
                if (auxSomaIntervalo[i] != 0)
                    probAccInterval[i] = auxAcertoIntervalo[i] / auxSomaIntervalo[i]; //  + alpha*probAccInterval[i];


                for (int j = 0; j < nroClasses + 1; j++)
                    if (auxSomaClass[j] != 0) {
                        jointP[j] = (auxSomaInterClass[i][j] / auxSomaClass[j]) * PC[j]; // joint
                        normTerm += jointP[j];
                    } else
                        jointP[j] = 0;

                for (int c = 1; c < nroClasses + 1; c++) {
                    if (normTerm != 0)
                        classesWeights[i][c] = (jointP[c] / normTerm);// + alpha * classesWeights[i][c];         //  Math.pow(normTerm,2);
                }


                // calcula coverage para cada intervalo
                if (normTerm != 1)
                    coverageInterval[i] = normTerm;
                else
                    coverageInterval[i] = 0;


            }

            // calcula entropia
            for (int e = 0; e < nroIntervals - 1; e++) {
                for (int c = 1; c < nroClasses + 1; c++)
                    entropyInterval[e] += classesWeights[e][c] * (Math.log(classesWeights[e][c]) / Math.log(2));

                entropyInterval[e] *= -1;
            }
        }

       /* else{
            for (int i = 0; i < nroIntervals - 1; i++) {
                normTerm = 0;

                for(int j = 0; j < auxSomaClass.length; j++)
                    somaIntervalClassIL[i][j] = auxSomaInterClass[i][j] + alpha*somaIntervalClassIL[i][j];

                // somaAcertoIntervalo[i] = auxAcertoIntervalo[i] + alpha*somaAcertoIntervalo[i];
                // contaCftIntervalo[i] = auxSomaIntervalo[i] + alpha*contaCftIntervalo[i];

                //teste  - acuracia é medida somente no ultimo batch
                if(auxSomaIntervalo[i] != 0)
                    probAccInterval[i] = auxAcertoIntervalo[i]/auxSomaIntervalo[i]; //  + alpha*probAccInterval[i];


                for (int j = 0; j < nroClasses + 1; j++)
                    if (auxSomaClass[j] != 0) {
                        jointP[j] = (auxSomaInterClass[i][j] / auxSomaClass[j]) * PC[j]; // joint
                        normTerm += jointP[j];
                    } else
                        jointP[j] = 0;

                for (int c = 1; c < nroClasses + 1; c++) {
                    if (normTerm != 0)
                        classesWeights[i][c] = (jointP[c] / normTerm) + alpha*classesWeights[i][c];         //  Math.pow(normTerm,2);
                }


                // calcula coverage para cada intervalo
                if(normTerm != 1)
                    coverageInterval[i] = normTerm;
                else
                    coverageInterval[i] = 0;


            }

            // calcula entropia
            for(int e = 0; e < nroIntervals-1; e++) {
                for (int c = 1; c < nroClasses + 1; c++)
                    entropyInterval[e] += classesWeights[e][c] * (Math.log(classesWeights[e][c]) / Math.log(2));

                entropyInterval[e] *= -1;
            }

        }
*/
        //  normalizaClassesWeights();
        //   System.out.println();
    }


    public void updateIntervalWeightsIncLearnWeightedIntervalsFadingFactor(double[][] attrCl, double[] lastClassifications){
        // Fading factors
        // atualiza pesos de intervalos e considera confiabilidade de cada intervalo, usando os resultado de classificacao passados

        int line = attrCl.length;
        double alpha = 0.99; // fading rate
        //numTrain = line + alpha*numTrain; //
        numTrain += line;  // atualiza numero de intancias processadas
        int nroClasses = classes.length;
        int nroIntervals = vetAtr.length;
        double[] PC = new double[classes.length +1];
        double[] jointP = new double[nroClasses +1];
        double[] auxSomaClass = new double[somaClassIL.length];
        double[] auxAcertoIntervalo = new double[somaAcertoIntervalo.length];
        double[] auxSomaIntervalo = new double[somaAcertoIntervalo.length];
        double normTerm;
        boolean allZero;

        contaCftIntervalo = new double[nroIntervals - 1];


        for(int j = 0; j < line; j++)             // |Ci|      - numero de elementos da classe i no conjunto de treinamento
            auxSomaClass[(int)attrCl[j][1]]++;  // conta o numero de instancias por classe

        for(int s = 0; s < nroClasses+1; s++) {
            somaClassIL[s] = auxSomaClass[s] + alpha*somaClassIL[s];
            PC[s] = somaClassIL[s] / numTrain;   // P(Ci)  prob da classe i.
        }


        for (int i = 0; i < nroIntervals - 1; i++) {
            normTerm = 0;

            // zera vetores auxiliares
            for(int j = 0; j < auxSomaClass.length; j++)
                auxSomaClass[j] = 0;
            for(int k = 0; k < auxAcertoIntervalo.length; k++) {
                auxAcertoIntervalo[k] = 0;
                auxSomaIntervalo[k] = 0;
            }


            for(int j = 0; j < line; j++) {
                if (i == 0) {
                    if (attrCl[j][0] >= vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        auxSomaClass[(int)attrCl[j][1]]++; //somaIntervalClassIL[i][(int) attrCl[j][1]]++;
                        if(attrCl[j][1] == lastClassifications[j])
                            auxAcertoIntervalo[i]++;  // somaAcertoIntervalo[i]++; // probAccInterval[i]
                        auxSomaIntervalo[i]++;  // contaCftIntervalo[i]++;
                        //    break;
                    }
                } else {
                    if (attrCl[j][0] > vetAtr[i] && attrCl[j][0] <= vetAtr[i + 1]) {
                        auxSomaClass[(int)attrCl[j][1]]++; // somaIntervalClassIL[i][(int) attrCl[j][1]]++;
                        if(attrCl[j][1] == lastClassifications[j])
                            auxAcertoIntervalo[i]++;  // somaAcertoIntervalo[i]++; // probAccInterval[i]
                        auxSomaIntervalo[i]++;  // contaCftIntervalo[i]++;
                        //    break;
                    }
                }
            }


            for(int j = 0; j < auxSomaClass.length; j++)
                somaIntervalClassIL[i][j] = auxSomaClass[j] + alpha*somaIntervalClassIL[i][j];

            somaAcertoIntervalo[i] = auxAcertoIntervalo[i] + alpha*somaAcertoIntervalo[i];
            contaCftIntervalo[i] = auxSomaIntervalo[i] + alpha*contaCftIntervalo[i];


            for (int j = 0; j < nroClasses + 1; j++)
                if (somaClassIL[j] != 0) {
                    jointP[j] = (somaIntervalClassIL[i][j] / somaClassIL[j]) * PC[j]; // joint
                    normTerm += jointP[j];
                } else
                    jointP[j] = 0;

            for (int c = 1; c < nroClasses + 1; c++) {
                if (normTerm == 0)
                    classesWeights[i][c] = 0;
                else
                    classesWeights[i][c] = (jointP[c] / normTerm);         //  Math.pow(normTerm,2);
            }


            // pode ocorrer de um intervalo não conter nenhuma instancia de treinamento; neste caso coloca peso pequeno
            allZero = true;
            for (int c = 1; c < nroClasses + 1; c++)
                if(classesWeights[i][c] != 0) {
                    allZero = false;
                    break;
                }

            if(allZero){
                for (int c = 1; c < nroClasses + 1; c++)
                    classesWeights[i][c] = 0.0001;
            }


            // atualiza confiança de intervalo
      /*      if(contaCftIntervalo[i] != 0)
                probAccInterval[i] = somaAcertoIntervalo[i]/contaCftIntervalo[i];

            if(somaAcertoIntervalo[i] == 0)
                probAccInterval[i] = 0.0001;  //gambiarra para não ficar 0
*/
        }

        //   System.out.println();
    }

    public double getWeightSAbDG(double value, int classe){
        int atrLen = vetAtr.length;
        int intervalo = -1;

        for(int i = 0; i < atrLen-1; i++)
            if(value > vetAtr[i] && value <= vetAtr[i+1])
                intervalo = i;

        if(intervalo == -1)
            if(value <= vetAtr[0])
                intervalo = 0;
            else if (value > vetAtr[atrLen-1])
                intervalo = atrLen-2;

        return SAbDGverticesWeights[intervalo][classe];
    }



    // metodos para flow graph

    public void flowGraphVertexWeight(){

        int line = atrValues.length;
        total = line;
        int nroIntervals = vetAtr.length;
        flowGraphValues = new double[nroIntervals-1];
        flowGraphWeights = new double[nroIntervals-1];


        for(int j = 0; j < line; j++) {

            for (int i = 0; i < nroIntervals - 1; i++) {
                if (i == 0) {
                    if (atrValues[j] >= vetAtr[i] && atrValues[j] <= vetAtr[i + 1]) {
                        flowGraphValues[i]++;
                        break;
                    }
                } else {
                    if (atrValues[j] > vetAtr[i] && atrValues[j] <= vetAtr[i + 1]) {
                        flowGraphValues[i]++;            // P(D)            //   ||D||/||E||
                        break;
                    }
                }
            }

        }


           // normaliza pesos
            for(int c = 0; c < nroIntervals - 1; c++) {
                if(flowGraphValues[c] != 0)
                    flowGraphWeights[c] = (flowGraphValues[c] / line);
            }



      //  System.out.println();


    }


    public double getWeightFlowGraph(double value){
        int atrLen = vetAtr.length;
        int intervalo = -1;

        for(int i = 0; i < atrLen-1; i++)
            if(value > vetAtr[i] && value <= vetAtr[i+1])
                intervalo = i;

        if(intervalo == -1)
            if(value <= vetAtr[0])
                intervalo = 0;
            else if (value > vetAtr[atrLen-1])
                intervalo = atrLen-2;

        return flowGraphWeights[intervalo];
    }

    private double intervalGain;
    private double[] vetAtr;
    private double[] intervals;
    private double[] classes;
    private double[] instancesPerInterval;
    private double[][] classesWeights;
    private double[][] atrClass;
    private double[] atrValues;   // valores de atributos - usado em flow graph
    private double[] cuts;
    private int contCuts;
    private int[][] overlapIntervals; // fuzy classification
    private double[][] membershipValues;
    private double[] flowGraphWeights;
    private double[] flowGraphValues;
    private int total;  // numero de instancias apresentatdas até um determinado momento em um fluxo

    private double MaxPert;
    private double classWeight;
    private boolean isClassLabel;

    private double[][] SAbDGverticesWeights;
    private double[][] SAbDGverticesValues;

    // para aprendizado incremental
    private double numTrain;
    private double[] somaClassIL;
    private double [][] somaIntervalClassIL;
    private double[][] jointIL;
    private double[] somaAcertoIntervalo;
    private double[] contaCftIntervalo;
    private double[] probAccInterval;
    private double[][] probAccIntervalMat;
    private double[][] somaAcertoIntervaloMat;
    private double[][] contaCftIntervaloMat;

    private double[] entropyInterval, coverageInterval;
}
