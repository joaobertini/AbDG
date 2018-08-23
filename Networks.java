/**
 * Created by IntelliJ IDEA.
 * User: Jo�o
 * Date: 03/06/2009
 * Time: 11:04:24
 * To change this template use File | Settings | File Templates.
 */
public class Networks {               // cria rede de uma determinada classe

    public Networks(double[][] matriz){
       E = matriz;
       line = E.length;
       coll = E[0].length;
       vetCorrelations = new AttributeCorrelation[coll-2];


    }

    public Networks(double[][] matriz, boolean bol){ // cria Networks para flow graphs, boolean só para realizar polimorfimos
        E = matriz;
        line = E.length;
        coll = E[0].length;
        vetCorrelations = new AttributeCorrelation[coll-1];  // valor da classe faz parte do grafo


    }

    public Networks(double[][] matriz, double classe, int trainLen){
       E = matriz;
       line = E.length;                           // conjuto com classe 'classe'  usado no treinamento desta rede
       coll = E[0].length;
       vetCorrelations = new AttributeCorrelation[coll-2];
       this.classe = classe;
       this.trSize = trainLen;      // tamanho do conjunto de treinamento completo
     //  this.ordem = ordem;
    }

    public void learn(AttributeHandler[] atr){
        vetAtr = atr;

     //  int NroInterval = 6;
     //  int intDescript = NroInterval + 1;
     //  intervalos = new double[coll-1][intDescript];               // matriz que define intervalos linha = atributo/  col = valor de corte
                                                // inicialmente histogramas tem mesmo valor 8;

      for(int i = 0; i < coll-2; i++){
         vetCorrelations[i] = new AttributeCorrelation(i, i+1, atr[i].getVetAtr(), atr[i+1].getVetAtr());
       }

/*       int i1, i2;
       correlation = new double[NroInterval][NroInterval];
       for(int a = 0; a < NroInterval; a++)
          for(int b = 0; b < NroInterval; b++)
             correlation[a][b] = 0;
 */

     // for(int i = 0; i < line; i++)



      for(int j = 0; j < coll - 2; j++) {                        // separar por classe
          for(int i = 0; i < line; i++)
             vetCorrelations[j].buildCorrelation(E[i][j],E[i][j+1]);
    //     vetCorrelations[j].createWeights(line);
     }

    }

    public void learnConectionImputation(AttributeHandler[] atr, double[][] MTI){
      vetAtr = atr;

      int pairCount = 0;
      for(int i = 0; i < coll-2; i++){
         vetCorrelations[i] = new AttributeCorrelation(i, i+1, atr[i].getVetAtr(), atr[i+1].getVetAtr());
       }



      for(int j = 0; j < coll - 2; j++) {                        // separar por classe
          pairCount = 0;
          for(int i = 0; i < line; i++)
             if(MTI[i][j] != emptyValue && MTI[i][j+1] != emptyValue){
                vetCorrelations[j].buildCorrelation(E[i][j],E[i][j+1]);
                pairCount++;
             }
//         vetCorrelations[j].createWeights(pairCount);
      }


    }

     public void learnPAPER(AttributeHandler[] atr){
        vetAtr = atr;
        double pw = line/(double)trSize;
     //  int NroInterval = 6;
     //  int intDescript = NroInterval + 1;
     //  intervalos = new double[coll-1][intDescript];               // matriz que define intervalos linha = atributo/  col = valor de corte
                                                // inicialmente histogramas tem mesmo valor 8;

      for(int i = 0; i < coll-2; i++){
         vetCorrelations[i] = new AttributeCorrelation(i, i+1, atr[i].getVetAtr(), atr[i+1].getVetAtr());
       }

/*       int i1, i2;
       correlation = new double[NroInterval][NroInterval];
       for(int a = 0; a < NroInterval; a++)
          for(int b = 0; b < NroInterval; b++)
             correlation[a][b] = 0;
 */

     // for(int i = 0; i < line; i++)



      for(int j = 0; j < coll - 2; j++) {                        // separar por classe
          for(int i = 0; i < line; i++)
             vetCorrelations[j].buildCorrelation(E[i][j],E[i][j+1]);
     //    vetCorrelations[j].showCM();
         vetCorrelations[j].createWeightsPAPER(line,pw);
     //     vetCorrelations[j].showCM();
      }

    }

     public void learnConectionImputationPAPER(AttributeHandler[] atr, double[][] MTI){
      vetAtr = atr;
      double pw = line/(double)trSize;       // probabilidade marginal da classe
      int pairCount = 0;

      for(int i = 0; i < coll-2; i++){
         vetCorrelations[i] = new AttributeCorrelation(i, i+1, atr[i].getVetAtr(), atr[i+1].getVetAtr());
       }



      for(int j = 0; j < coll - 2; j++) {                        // separar por classe
          pairCount = 0;
          for(int i = 0; i < line; i++)
             if(MTI[i][j] != emptyValue && MTI[i][j+1] != emptyValue){
                vetCorrelations[j].buildCorrelation(E[i][j],E[i][j+1]);
                pairCount++;
             }
     //    vetCorrelations[j].showCM();
         vetCorrelations[j].createWeightsPAPERimputation(line,pairCount,pw);
     //     vetCorrelations[j].showCM();
      }


    }


    public void learnFlowGraph(AttributeHandler[] atr)  {

        vetAtr = atr;

        for(int i = 0; i < coll-1; i++){  // grafo inclui classe
            vetCorrelations[i] = new AttributeCorrelation(i, i+1, atr[i].getVetAtr(), atr[i+1].getVetAtr());
        }


        for(int j = 0; j < coll - 1; j++) {
            for(int i = 0; i < line; i++)
                vetCorrelations[j].buildCorrelation(E[i][j],E[i][j+1]);
            //    vetCorrelations[j].showCM();
            vetCorrelations[j].createWeightsFlowGraph(trSize);   // normaliza pesos de aresta usando tamanho do conjunto
            //     vetCorrelations[j].showCM();
        }

    }

    public void learnSAbSDG(AttributeHandler[] atr)  {

        vetAtr = atr;

        for(int i = 0; i < coll-2; i++){  // grafo inclui classe
            vetCorrelations[i] = new AttributeCorrelation(i, i+1, atr[i].getVetAtr(), atr[i+1].getVetAtr());
        }


        for(int j = 0; j < coll - 2; j++) {
            for(int i = 0; i < line; i++)
                vetCorrelations[j].buildCorrelation(E[i][j],E[i][j+1]);
            //    vetCorrelations[j].showCM();
            vetCorrelations[j].createWeightsFlowGraph(trSize);   // normaliza pesos de aresta usando tamanho do conjunto
            //     vetCorrelations[j].showCM();
        }

    }

    public void updateSAbDG(double[][] E)  {


        for(int j = 0; j < coll - 2; j++) {
            for(int i = 0; i < line; i++)
                vetCorrelations[j].buildCorrelation(E[i][j],E[i][j+1]);
            //    vetCorrelations[j].showCM();
            vetCorrelations[j].createWeightsFlowGraph(line);   // normaliza pesos de aresta usando tamanho do conjunto
            //     vetCorrelations[j].showCM();
        }

    }

    public void updateFlowGraph(double[][] E)  {


        for(int j = 0; j < coll - 1; j++) {
            for(int i = 0; i < line; i++)
                vetCorrelations[j].buildCorrelation(E[i][j],E[i][j+1]);
            //    vetCorrelations[j].showCM();
            vetCorrelations[j].createWeightsFlowGraph(line);   // normaliza pesos de aresta usando tamanho do conjunto
            //     vetCorrelations[j].showCM();
        }

    }


    public void criaProbClassSAbDG(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        privateProbVector = new double[lineTe];

        for(int i = 0; i < lineTe; i++){

            privateProbVector[i] = 1;
            for(int j = 0; j < coll - 2; j++)
                if(vetCorrelations[j].findCorrelationFlowGraph(A[i][j], A[i][j+1]) == 0)
                    privateProbVector[i] *= 0.001;
                else
                    privateProbVector[i] *= vetCorrelations[j].findCorrelationFlowGraph(A[i][j], A[i][j+1]);  // produto dos pesos dos vertices

            for(int j = 0; j < coll - 1; j++)
                if(vetAtr[j].getWeightSAbDG(A[i][j],(int)classe) == 0)
                    privateProbVector[i] *= 0.001;
                else
                    privateProbVector[i] *= vetAtr[j].getWeightSAbDG(A[i][j],(int)classe); // vetAtr[j].getWeight(A[i][j],(int)classe);

        }

        System.out.println();

    }

    public void criaProbClassSAbDGlog(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        privateProbVector = new double[lineTe];

        for(int i = 0; i < lineTe; i++){

            privateProbVector[i] = 0;
            for(int j = 0; j < coll - 2; j++)
                if(vetCorrelations[j].findCorrelationFlowGraph(A[i][j], A[i][j+1]) == 1)
                    privateProbVector[i] += Math.log(1000);// produto dos pesos dos vertices
                else
                    privateProbVector[i] += Math.log(1/(1-vetCorrelations[j].findCorrelationFlowGraph(A[i][j], A[i][j+1])));

            for(int j = 0; j < coll - 1; j++)
                if(vetAtr[j].getWeightSAbDG(A[i][j],(int)classe) == 1)
                    privateProbVector[i] += Math.log(1000);
                else
                    privateProbVector[i] += Math.log(1/(1-vetAtr[j].getWeightSAbDG(A[i][j], (int)classe))); // vetAtr[j].getWeight(A[i][j],(int)classe);

        }


    }
    public double[] criaProbSemClasseFG(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        double[] prod = new double[lineTe];

        for(int i = 0; i < lineTe; i++){

            prod[i] = 1;
            for(int j = 0; j < coll - 2; j++)
                if(vetCorrelations[j].findCorrelationFlowGraph(A[i][j], A[i][j+1]) == 0)
                    prod[i] *= 0.001;
                else
                    prod[i] *= vetCorrelations[j].findCorrelationFlowGraph(A[i][j], A[i][j+1]);  // produto dos pesos dos vertices

            for(int j = 0; j < coll - 1; j++)
                if(vetAtr[j].getWeightFlowGraph(A[i][j]) == 0)
                    prod[i] *= 0.001;
                else
                    prod[i] *= vetAtr[j].getWeightFlowGraph(A[i][j]); // vetAtr[j].getWeight(A[i][j],(int)classe);

        }

        return prod;
    }

    public double criaProbClassFG(double[] A, double classe){           // armazena no objeto o vetor de probabilidades da rede corrente

        int coll = A.length;
        double prod = 1;

        if(vetCorrelations[coll-2].findCorrelationFlowGraph(A[coll-2], classe) == 0)
            prod *= 0.001;
        else
            prod *= vetCorrelations[coll-2].findCorrelationFlowGraph(A[coll-2], classe);  // pesos da aresta entre o ultimo vertice e a classe

      /*  if(vetAtr[coll-1].getWeightFlowGraph(classe) == 1)
            prod *= 0.001;
        else
            prod *= vetAtr[coll-1].getWeightFlowGraph(classe); // vetAtr[j].getWeight(A[i][j],(int)classe);
     */

        return prod;
    }


    public double[] criaProbSemClasseFGlog(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        double[] prod = new double[lineTe];

        for(int i = 0; i < lineTe; i++){

            prod[i] = 0;
            for(int j = 0; j < coll - 2; j++)
                if(vetCorrelations[j].findCorrelationFlowGraph(A[i][j], A[i][j+1]) == 1)
                    prod[i] += Math.log(1000);// produto dos pesos dos vertices
                else
                     prod[i] += Math.log(1/(1-vetCorrelations[j].findCorrelationFlowGraph(A[i][j], A[i][j+1])));

            for(int j = 0; j < coll - 1; j++)
                if(vetAtr[j].getWeightFlowGraph(A[i][j]) == 1)
                    prod[i] += Math.log(1000);
                else
                    prod[i] += Math.log(1/(1-vetAtr[j].getWeightFlowGraph(A[i][j]))); // vetAtr[j].getWeight(A[i][j],(int)classe);

        }

        return prod;
    }

    public double criaProbClassFGlog(double[] A, double classe){           // armazena no objeto o vetor de probabilidades da rede corrente

        int coll = A.length;
        double prod = 1;

        if(vetCorrelations[coll-2].findCorrelationFlowGraph(A[coll-2], classe) == 1)
            prod += Math.log(1000);
        else
            prod += Math.log(1/(1-vetCorrelations[coll-2].findCorrelationFlowGraph(A[coll-2], classe)));  // pesos da aresta entre o ultimo vertice e a classe


/*
        if(vetAtr[coll-1].getWeightFlowGraph(classe) == 1)
           prod += Math.log(1000);
        else
           prod += Math.log(1/(1-vetAtr[coll-1].getWeightFlowGraph(classe))); // vetAtr[j].getWeight(A[i][j],(int)classe);
*/

        return prod;
    }
    public double[] probClass(double[][] A){           // retorna vetor de probabilidades da rede corrente

       int lineTe = A.length;
       int coll = A[0].length;
       double[] probVector = new double[lineTe];

       for(int z = 0; z < lineTe; z++)
          probVector[z] = 0;                // zera vetor

       for(int i = 0; i < lineTe; i++){
          for(int j = 0; j < coll - 2; j++)
             probVector[i] += vetCorrelations[j].findCorrelation(A[i][j], A[i][j+1]); 
       }

       return probVector;
    }

     public void criaProbClass(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

       int lineTe = A.length;
       int coll = A[0].length;
       double aux = 1, soma = 0, maior = 0, somaGain = 0;
       privateProbVector = new double[lineTe];
       privateSumVector = new double[lineTe];  

       for(int z = 0; z < lineTe; z++){
          privateProbVector[z] = 1;                // zera vetor
          privateSumVector[z] = 0;
       }

       for(int i = 0; i < coll-1; i++)
           somaGain += vetAtr[i].getIntervalGain();
         //      System.out.println(vetAtr[i].getIntervalGain());
         //

       for(int i = 0; i < lineTe; i++){
      //     System.out.print("instancia " + i + "rede classe " + classe);
           soma = 0;
           aux = 1;
           for(int j = 0; j < coll - 2; j++)
               privateSumVector[i] += vetCorrelations[j].findCorrelation(A[i][j], A[i][j+1]);

           for(int j = 0; j < coll - 1; j++)
               privateProbVector[i] *= vetAtr[j].getWeight(A[i][j],(int)classe); // vetAtr[j].getWeight(A[i][j],(int)classe);



        //     maior += vetAtr[j].getWeight(A[i][ordem[j]],(int)classe);
        //     soma += vetCorrelations[j].findCorrelation(A[i][ordem[j]], A[i][ordem[j+1]]);



       //     privateProbVector[i] *= vetAtr[coll-2].getWeight(A[i][coll-2],(int)classe); // * (vetAtr[coll - 2].getIntervalGain()/somaGain);
         //   System.out.println( "result " + privateProbVector[i]);

       }

    }

    public void criaProbClassWeighted(double[][] A, double[] atrW, AttributeCorrelation[] vetAtrCorr){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe];
        privateSumVector = new double[lineTe];
        privateProdSumVector = new double[lineTe];

        for(int z = 0; z < lineTe; z++){
            privateProbVector[z] = 1;                // zera vetor
            privateSumVector[z] = 0;
            privateProdSumVector[z] = 0;
        }

       // coll = (int)(coll * 0.2);

        for(int i = 0; i < atrW.length; i++)
           somaGain += atrW[i];

        for(int i = 0; i < lineTe; i++){
            soma = 0;
            aux = 1;                                   //   peso de atributo - atrW[j]
            for(int j = 0; j < coll - 1; j++){
                //   System.out.print(" #### " +  vetAtr[j].getWeight(A[i][j],(int)classe) + " " + vetCorrelations[j].findCorrelation(A[i][j], A[i][j+1])  + " " +  vetAtr[j+1].getWeight(A[i][j+1],(int)classe));
                 privateProbVector[i] *= vetAtr[j].getWeight(A[i][j],(int)classe); // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
        }

        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < coll - 2; a++){
                for(int i = 0; i < lineTe; i++){
                     aux = vetCorrelations[cont].findCorrelation(A[i][a], A[i][a+1]);
                 //   if(aux != 0)
                       privateSumVector[i] += aux;
                 //  else
                 //      privateSumVector[i] = 0;
         }
                cont++;

        }


        cont = 0;                                                   

            for(int a = 0; a < coll-2; a++){
                for(int i = 0; i < lineTe; i++){
                    aux = vetCorrelations[cont].findCorrelation(A[i][a], A[i][a+1]);

                   if(aux!= 0) 
                      privateProdSumVector[i] += vetAtrCorr[cont].findCorrelation(A[i][a], A[i][a+1])/aux + ((vetAtr[a].getWeight(A[i][a],(int)classe)) * vetAtr[a+1].getWeight(A[i][a+1],(int)classe));
                   else
                      privateProdSumVector[i] += ((vetAtr[a].getWeight(A[i][a],(int)classe)) * vetAtr[a+1].getWeight(A[i][a+1],(int)classe));
                    }
                    cont++;
            }




    }

     public void criaProbClassFuzzy(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

       int lineTe = A.length;
       int coll = A[0].length;
       double aux = 1, soma = 0, maior = 0, somaGain = 0;
       privateProbVector = new double[lineTe];
       privateSumVector = new double[lineTe];
       double[] edgeWeights = new double[4];
       double max12, max23;

       double somaPert= 0, somaDiv = 0;

       for(int z = 0; z < lineTe; z++){
          privateProbVector[z] = 1;                           // zera vetor   ######## era 1
          privateSumVector[z] = 0;
       }

       for(int i = 0; i < coll-1; i++)
           somaGain += vetAtr[i].getIntervalGain();
         //      System.out.println(vetAtr[i].getIntervalGain());
         //

       for(int i = 0; i < lineTe; i++){
      //     System.out.print("instancia " + i + "rede classe " + classe);
           soma = 0;
           aux = 1;

           for(int j = 0; j < coll - 1; j++){
               //privateProbVector[i] = Math.max(privateProbVector[i], vetAtr[j].getWeightMembershipFunction(A[i][j],(int)classe));

          //     privateProbVector[i] += vetAtr[j].getWeightMembershipFunction(A[i][j],(int)classe);
               vetAtr[j].getWeightMembershipFunction(A[i][j],(int)classe);
               privateProbVector[i] *= vetAtr[j].getClassWeight();

          //
          //    somaPert += vetAtr[j].getMaxPert()*vetAtr[j].getClassWeight();
          //    somaDiv += vetAtr[j].getMaxPert() ;


           }
          // privateProbVector[i] = somaPert /somaDiv;

           int[][] overIntAttr1, overIntAttr2;
           double[][] pertValuesAttr1, pertValuesAttr2;

           for(int j = 0; j < coll - 2; j++) { // encontra o maior valor para p(vi)deltij p(vj) para as 4 possiveis combina��es 

               overIntAttr1 = vetAtr[j].getOverlapIntervals();  // retorna os intervalos para os quais a fun��o de pertinencia � diferente de 0 para value
               pertValuesAttr1 = vetAtr[j].getMembershipValues();
               overIntAttr2 = vetAtr[j+1].getOverlapIntervals();
               pertValuesAttr2 = vetAtr[j+1].getMembershipValues();

              edgeWeights[0] = vetCorrelations[j].getCorrelation(overIntAttr1[0][0], overIntAttr2[0][0]);       // peso da aresta   - original com original
              edgeWeights[1] = vetCorrelations[j].getCorrelation(overIntAttr1[0][0], overIntAttr2[0][1]);
              edgeWeights[2] = vetCorrelations[j].getCorrelation(overIntAttr1[0][1], overIntAttr2[0][0]);
              edgeWeights[3] = vetCorrelations[j].getCorrelation(overIntAttr1[0][1], overIntAttr2[0][1]);

              max12 = Math.max(edgeWeights[0]*pertValuesAttr1[0][0]*pertValuesAttr2[0][0], edgeWeights[1]*pertValuesAttr1[0][0]*pertValuesAttr2[0][1]);
              max23 = Math.max(edgeWeights[2]*pertValuesAttr1[0][1]*pertValuesAttr2[0][0], edgeWeights[3]*pertValuesAttr1[0][1]*pertValuesAttr2[0][1]);

              privateSumVector[i] += Math.max(max12, max23);


            }

       }

    }

    public void criaProbClassMaxMin(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

       int lineTe = A.length;
       int coll = A[0].length;
       double aux = 0, somaGain = 0, corr = 0;
       privateProbVector = new double[lineTe];

       for(int z = 0; z < lineTe; z++)
          privateProbVector[z] = 0;                // zera vetor

     //  for(int i = 0; i < coll-1; i++)
     //     System.out.println(vetAtr[i].getIntervalGain());
         // somaGain += vetAtr[i].getIntervalGain();

       for(int i = 0; i < lineTe; i++){
           somaGain = 0;
          for(int j = 0; j < coll - 2; j++) {
             aux = Math.min(vetAtr[j].getWeight(A[i][j],(int)classe) , vetAtr[j+1].getWeight(A[i][j+1],(int)classe));  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
             corr = vetCorrelations[j].findCorrelation(A[i][j], A[i][j+1]);
             if(corr != 0)
                somaGain += Math.max(aux, corr);
             else
                somaGain += aux;
           }
            privateProbVector[i] = somaGain;
       }

    }

      public double[] retrieveInterval(double[] inst ,int atr, int numInter){   //      imputa��o treino

         // verificar (atr-1) x atr e atr x (atr + 1)
         int coll = inst.length;
         int intervalo = 0, inta, intb;
         int cont = 0;
         int len = numInter;            // numero de intervalos -1
         double[] somaAtr = new double[len];
         double[] atrCorr = new double[len];
         int auxInd = 0; // armazena algum indice de vetCorrelation onde um dos attributos seja o faltante


          if(atr > 0 && inst[atr-1] != emptyValue){
                atrCorr = vetCorrelations[atr-1].findPossibleCorrelations(atr, atr-1 , inst[atr-1]); // falta atributo i
               // auxInd = cont;

               for(int p = 0; p < len; p++)
                  somaAtr[p] += atrCorr[p];
             }

           if (atr < (coll-2) && inst[atr+1] != emptyValue){
                atrCorr = vetCorrelations[atr].findPossibleCorrelations(atr, atr+1, inst[atr+1]);
              //  auxInd = cont;

                for(int p = 0; p < len; p++)
                 somaAtr[p] += atrCorr[p];
             }


          intervalo = maiorInd(somaAtr,atr,inst[coll-1]);   // resolve impate

         if(atr != 0)
            return vetCorrelations[atr-1].getInterval(intervalo,atr);
         else
            return vetCorrelations[atr].getInterval(intervalo,atr);
          //   encontrar intervalo correspondente � correla��o em attributeCorrelation

     }

      public double[] retrieveIntervalExperimenting(double[] inst ,int atr, int numInter){   //      imputa��o treino

         // verificar (atr-1) x atr e atr x (atr + 1)
         int coll = inst.length;
         int intervalo = 0, inta, intb;
         int cont = 0;
         int len = numInter;            // numero de intervalos -1
         double[] somaAtr = new double[len];
         double[] atrCorr = new double[len];
         int auxInd = 0; // armazena algum indice de vetCorrelation onde um dos attributos seja o faltante
         double weightVert = 0;

          if(atr > 0 && inst[atr-1] != emptyValue){
                atrCorr = vetCorrelations[atr-1].findPossibleCorrelations(atr, atr-1 , inst[atr-1]); // falta atributo i
                weightVert = vetAtr[atr-1].getWeight(inst[atr-1],(int)classe) * vetAtr[atr-1].getIntervalGain();

               for(int p = 0; p < len; p++)
                  somaAtr[p] += weightVert * atrCorr[p];
             }

           if (atr < (coll-2) && inst[atr+1] != emptyValue){
                atrCorr = vetCorrelations[atr].findPossibleCorrelations(atr, atr+1, inst[atr+1]);
                weightVert = vetAtr[atr+1].getWeight(inst[atr+1],(int)classe) * vetAtr[atr+1].getIntervalGain();

                for(int p = 0; p < len; p++)
                 somaAtr[p] += weightVert * atrCorr[p];
             }

          
           for(int p = 0; p < len; p++)
             somaAtr[p] *= vetAtr[atr].getIntClass(p,(int)classe); // peso dos vertices, no atributo desconhecido


          intervalo = maiorInd(somaAtr,atr,inst[coll-1]);   // resolve impate

         if(atr != 0)
            return vetCorrelations[atr-1].getInterval(intervalo,atr);
         else
            return vetCorrelations[atr].getInterval(intervalo,atr);
          //   encontrar intervalo correspondente � correla��o em attributeCorrelation

     }

     public double retrieveSum(double[] inst ,int atr, int numInter){   //      imputa��o teste

         // verificar (atr-1) x atr e atr x (atr + 1)
         int coll = inst.length;
         int intervalo = 0, inta, intb;
         int cont = 0;
         int len = numInter;            // numero de intervalos -1
         double[] somaAtr = new double[len];
         double[] atrCorr = new double[len];
         int auxInd = 0; // armazena algum indice de vetCorrelation onde um dos attributos seja o faltante
         indiceIntervalo = 0;   // imputa��o de testes
         indiceVetCorr = 0;

         if(atr > 0 && inst[atr-1] != emptyValue){
                atrCorr = vetCorrelations[atr-1].findPossibleCorrelations(atr, atr-1 , inst[atr-1]); // falta atributo i
             //   auxInd = cont;

               for(int p = 0; p < len; p++)
                  somaAtr[p] += atrCorr[p];
             }

           if (atr < (coll-2) && inst[atr+1] != emptyValue){
                atrCorr = vetCorrelations[atr].findPossibleCorrelations(atr, atr+1, inst[atr+1]);
            //    auxInd = cont;

                for(int p = 0; p < len; p++)
                 somaAtr[p] += atrCorr[p];
             }


         indiceIntervalo = maiorInd(somaAtr);
       //  indiceVetCorr = atr - 1;

         return somaAtr[indiceIntervalo];  // maior soma



     }


    public int maiorInd(double[] A){
        int ind = 0;
        double maior = A[0];
        for(int i = 1; i < A.length; i++)
           if(A[i] > maior){
               maior = A[i];
               ind = i;
            }

       return ind;
    }

    public int maiorInd(double[] A, int atr, double classe){
        int ind = 0;
        double maior = A[0];
        int cont = 0;
        for(int i = 1; i < A.length; i++)
           if(A[i] > maior){
               maior = A[i];
               ind = i;
            }

       for(int i = 0; i < A.length; i++)
          if(A[i] == maior)
             cont++;
          if(cont > 1){
            ind = vetAtr[atr].getIntFromHighestWeightofClass((int)classe);

          }
           // System.out.println("Empate!");

       return ind;
    }

    public double[] getIntervalTest(int atr){


        if(atr != 0)
            return vetCorrelations[atr-1].getInterval(indiceIntervalo,atr);
         else
            return vetCorrelations[atr].getInterval(indiceIntervalo,atr);

    
      }


    public double getProb(int i){
        return privateProbVector[i];
    }

    public double getSum(int i){
        return privateSumVector[i];
    }

    public double getProdSum(int i){
           return privateProdSumVector[i];
       }

    public double getSumOfProds(int i){
        return (privateProbVector[i] * privateSumVector[i]);
    }

    public double[] calculaIntervalos(int atr, int nroInt){  // recebe atributo ao qual sera econtrado menor valor

        double menor = E[0][atr];
        double maior = menor;
        double range, intLen;
        double[] intervals = new double[nroInt];
                  
        for(int i = 1; i < line; i++){
           if(E[i][atr] < menor)
              menor = E[i][atr];
           if(E[i][atr] > maior)
              maior = E[i][atr];
        }

        range = maior - menor;
        intLen = range/(nroInt-1);

        for(int j = 0; j < nroInt; j++)
           intervals[j] = menor + j * intLen;

        return intervals;

        }

    public double getClasse(){
        return classe;
    }

    public AttributeCorrelation[] getVetCorrelation(){
        return vetCorrelations;
    }

    public  AttributeHandler[] getVetAtr(){
        return vetAtr;
    }

 
  
    private double[][] E;
    private double[] privateProbVector;
    private double[] privateSumVector;
    private double[] privateProdSumVector;
    private double classe;
  //  private int[] ordem;
    private int line, coll, trSize;
    private AttributeCorrelation[] vetCorrelations, fullVetCorrelations;
    private AttributeHandler[] vetAtr;
    private double emptyValue = -10000;
    private int indiceIntervalo, indiceVetCorr;
  //  private int numAttr;
}


/*  public double calculateEmptyAtributeValue(double[] inst ,int atr){   //      imputa��o

        // verificar (atr-1) x atr e atr x (atr + 1)
        int coll = inst.length;
        int intervalo = 0, inta, intb;
        double[] atrAnt;
        double[] atrDep;
        int side = 1;

       if(atr > 0 && atr < coll - 2){
          atrAnt = vetCorrelations[atr-1].findPossibleCorrelations(inst[atr-1],1);  // retorna vetor com possiveis intervalos - pertencentes ao atributo faltante
          atrDep = vetCorrelations[atr].findPossibleCorrelations(inst[atr+1],2);
          inta = maiorInd(atrAnt);
          intb = maiorInd(atrDep);
          if(inta != intb){
            if(atrAnt[inta] > atrDep[intb]){
               intervalo = inta;
               atr--;
               side = 1;
            }
            else{
               intervalo = intb;
               side = 2;
            }
          }
          else{
             intervalo = intb;
             side = 2;
          }

           // encontrar maior entre atrAnt e atrDep
      }
       else if(atr == 0){
           atrDep = vetCorrelations[atr].findPossibleCorrelations(inst[atr+1],2);
           intervalo = maiorInd(atrDep);
           side = 1; // procura em vetAtr1
         }

      else if(atr == (coll-2)){
           atr--;
           atrAnt = vetCorrelations[atr].findPossibleCorrelations(inst[atr],1);
           intervalo = maiorInd(atrAnt);
           side = 2;  // procura em vetAtr2
         }

       return vetCorrelations[atr].getGuessFromInterval(intervalo,side);
        //   encontrar intervalo correspondente � correla��o em attributeCorrelation


    }   */

    /*   public double[] retrieveInterval(double[] inst ,int atr){   //      imputa��o

        // verificar (atr-1) x atr e atr x (atr + 1)
        int coll = inst.length;
        int intervalo = 0, inta, intb;
        double[] atrAnt;
        double[] atrDep;
        int side = 1;

       if(atr > 0 && atr < coll - 2){
          atrAnt = vetCorrelations[atr-1].findPossibleCorrelations(inst[atr-1],1);  // retorna vetor com possiveis intervalos - pertencentes ao atributo faltante
          atrDep = vetCorrelations[atr].findPossibleCorrelations(inst[atr+1],2);
          inta = maiorInd(atrAnt);
          intb = maiorInd(atrDep);
          if(inta != intb){
            if(atrAnt[inta] > atrDep[intb]){
               intervalo = inta;
               atr--;
               side = 1;
            }
            else{
               intervalo = intb;
               side = 2;
            }
          }
          else{
             intervalo = intb;
             side = 2;
          }

           // encontrar maior entre atrAnt e atrDep
      }
       else if(atr == 0){
           atrDep = vetCorrelations[atr].findPossibleCorrelations(inst[atr+1],2);
           intervalo = maiorInd(atrDep);
           side = 1; // procura em vetAtr1
         }

      else if(atr == (coll-2)){
           atr--;
           atrAnt = vetCorrelations[atr].findPossibleCorrelations(inst[atr],1);
           intervalo = maiorInd(atrAnt);
           side = 2;  // procura em vetAtr2
         }

       return vetCorrelations[atr].getInterval(intervalo,side);
        //   encontrar intervalo correspondente � correla��o em attributeCorrelation


    }    */