/**
 * Created by IntelliJ IDEA.
 * User: João
 * Date: 16/02/2012
 * Time: 11:42:11
 * To change this template use File | Settings | File Templates.
 */
public class NetworkFull {

    public NetworkFull(double[][] matriz){
       E = matriz;
       line = E.length;        // tamanho da classe
       coll = E[0].length;
    }

    public NetworkFull(double[][] matriz, int nrClasses, AttributeHandler[] atr){
       E = matriz;
       line = E.length;                           // conjuto com classe 'classe'  usado no treinamento desta rede
       coll = E[0].length;
       vetAtr = atr;

       nroClasses = nrClasses;//       // tamanho do conjunto de treinamento completo

       numAttr = coll-1;
       numEdgeFull = numAttr + (numAttr*(numAttr-3))/2;       // numero de 'matrizes' de conexoes entre atributos
       fullVetCorrelations = new AttributeCorrelation[numEdgeFull][nroClasses+1];  // matriz que representa as arestas e seus pesos

     // learnFullConection();

    }


    public void learnFullConection(){   // conecta cada atributo com todos os outros - resulta em
        // como estava em Networks
     double[][] A;
     int lineA = 0;

      int cont = 0;
      for(int i = 0; i < numAttr; i++){
        for(int j = i  + 1; j < numAttr; j++){
            for(int c = 1; c < nroClasses + 1; c++)
                fullVetCorrelations[cont][c] = new AttributeCorrelation(i, j, vetAtr[i].getVetAtr(), vetAtr[j].getVetAtr()); // cria matriz
            cont++;
         }

       }


        for(int c = 1; c < nroClasses+1; c++) {
            A = criaTreinamento(E,c);  // separa elementos da classe c
            lineA = A.length;
            cont = 0;
            for (int i = 0; i < numAttr; i++) {
                for (int j = i + 1; j < numAttr; j++) {                        // separar por classe
                    for (int k = 0; k < lineA; k++)
                        fullVetCorrelations[cont][c].buildCorrelation(A[k][i], A[k][j]);
                    //    vetCorrelations[j].showCM();
                    if(lineA != 0)
                        fullVetCorrelations[cont][c].createWeightsPAPER(lineA,((double)lineA/line));
                    else
                        System.out.println("Erro - classe inexistente");
                    cont++;
                    //     vetCorrelations[j].showCM();
                }
            }
        }
   //     System.out.println();
    }

    public void learnFullConection(int[] atrMask){   // conecta cada atributo com todos os outros - resulta em
        double[][] A;
        int lineA = 0;

        int cont = 0;
        for(int i = 0; i < numAttr; i++){
            for(int j = i  + 1; j < numAttr; j++){
                for(int c = 1; c < nroClasses+1; c++)
                    if(atrMask[i] == 1 && atrMask[j] == 1)
                        fullVetCorrelations[cont][c] = new AttributeCorrelation(i, j, vetAtr[i].getVetAtr(), vetAtr[j].getVetAtr());
                cont++;
            }

        }

        for(int c = 1; c < nroClasses + 1; c++) {
            A = criaTreinamento(E,c);  // separa elementos da classe c
            lineA = A.length;
            cont = 0;

            for (int i = 0; i < numAttr; i++) {
                for (int j = i + 1; j < numAttr; j++) {
                    if (atrMask[i] == 1 && atrMask[j] == 1) {// separar por classe
                        for (int k = 0; k < lineA; k++)
                            fullVetCorrelations[cont][c].buildCorrelation(A[k][i], A[k][j]);
                        //    vetCorrelations[j].showCM();
                        //fullVetCorrelations[cont].createWeights(line);
                        fullVetCorrelations[cont][c].createWeightsPAPER(lineA,((double)lineA/line));
                    }
                    cont++;
                    //     vetCorrelations[j].showCM();
                }
            }

            //     System.out.println();
        }
    }


    public void updateFullConection(double[][] E, double alpha, double[] lastClassifications){   // conecta cada atributo com todos os outros - resulta em
      int cont = 0;
      int line = E.length;
      double[][] A;
      int lineA;

        for(int c = 1; c < nroClasses + 1; c++) {
            A = criaTreinamento(E,c);  // separa elementos da classe c
            lineA = A.length;
            cont = 0;

        for(int i = 0; i < numAttr; i++){
            for(int j = i + 1; j < numAttr; j++){
                fullVetCorrelations[cont][c].zeraCorrelacoes();
                    for (int k = 0; k < lineA; k++)
                        fullVetCorrelations[cont][c].updateCorrelation(A[k][i], A[k][j], (int)(lastClassifications[k]) == c);
                    //    vetCorrelations[j].showCM();
                    fullVetCorrelations[cont][c].updateWeights(lineA, ((double)lineA/line), alpha);
                }
                cont++;
                //     vetCorrelations[j].showCM();
            }
        }

        //     System.out.println();
    }

    public void updateFullConection(double[][] E, int[] atrMask, double alpha, double[] lastClassifications) {   // conecta cada atributo com todos os outros - resulta em
        int cont = 0;
        int line = E.length;
        double[][] A;
        int lineA;


        for (int c = 1; c < nroClasses + 1; c++) {
            A = criaTreinamento(E, c);  // separa elementos da classe c
            lineA = A.length;
            cont = 0;

            for (int i = 0; i < numAttr; i++) {
                for (int j = i + 1; j < numAttr; j++) {                        // separar por classe
                    if (atrMask[i] == 1 && atrMask[j] == 1) {
                        for (int k = 0; k < lineA; k++)
                            fullVetCorrelations[cont][c].updateCorrelation(A[k][i], A[k][j],(int)(lastClassifications[k]) == c);
                        //    vetCorrelations[j].showCM();
                        fullVetCorrelations[cont][c].updateWeights(lineA, ((double)lineA/line), alpha);
                    }
                    cont++;
                    //     vetCorrelations[j].showCM();
                }
            }

            //     System.out.println();
        }
    }



    public void learnFullConectionImputationPAPER(){   // conecta cada atributo com todos os outros - resulta em
        double[][] A;
        int lineA;
        int cont = 0;
        int pairCount = 0;  // conta quantas instancias possuem ambos atributos Ai e Aj

         for(int i = 0; i < numAttr; i++){
           for(int j = i  + 1; j < numAttr; j++){
               for(int c = 1; c < nroClasses + 1; c++)
                   fullVetCorrelations[cont][c] = new AttributeCorrelation(i, j, vetAtr[i].getVetAtr(), vetAtr[j].getVetAtr());
               cont++;
            }

          }


        for(int c = 1; c < nroClasses+1; c++) {
            A = criaTreinamento(E,c);  // separa elementos da classe c
            lineA = A.length;
            cont = 0;
            for (int i = 0; i < numAttr; i++) {
                for (int j = i + 1; j < numAttr; j++) {                        // separar por classe
                    pairCount =0;
                    for (int k = 0; k < lineA; k++)
                        if(A[k][i] != emptyValue && A[k][j] != emptyValue) {
                            fullVetCorrelations[cont][c].buildCorrelation(A[k][i], A[k][j]);
                            pairCount++;
                        }
                    fullVetCorrelations[cont][c].createWeightsPAPERimputation(lineA,pairCount,((double)lineA/line));
                    cont++;

                }
            }
        }
              //   System.out.println();

    }


    public void normalizeCorrelationsFullVersion() {

        int atr1Len = 0, atr2Len = 0, cont = 0;
        double soma = 0;

        for (int i = 0; i < numAttr; i++) {
            for (int j = i + 1; j < numAttr; j++) {
                atr1Len = vetAtr[i].getVetAtr().length;
                atr2Len = vetAtr[j].getVetAtr().length;
                for (int k = 0; k < atr1Len - 1; k++) {
                    for (int m = 0; m < atr2Len - 1; m++) {
                        soma = 0;
                        for (int z = 1; z < nroClasses + 1; z++)
                            soma += fullVetCorrelations[cont][z].getCMEx(k, m);

                        if (soma != 0)
                            for (int x = 1; x < nroClasses + 1; x++) {
                              //  System.out.println(cont + " " + k + " " + m + " " + soma);
                                fullVetCorrelations[cont][x].setCMEx(k, m, soma);
                            }
                    }
                }
                cont++;
            }

        }
   //     System.out.println();
    }


    public void normalizeCorrelationsFullVersionInc() {
        // normaliza pesos e calcula ent, coverage e define acc = 1.
        // como medidas não são diferentes para as classes, são armazenadas apenas na class 1

        int atr1Len = 0, atr2Len = 0, cont = 0;
        double soma = 0;

        for (int i = 0; i < numAttr; i++) {
            for (int j = i + 1; j < numAttr; j++) {
                atr1Len = vetAtr[i].getVetAtr().length;
                atr2Len = vetAtr[j].getVetAtr().length;
                for (int k = 0; k < atr1Len - 1; k++) {
                    for (int m = 0; m < atr2Len - 1; m++) {
                        soma = 0;
                        for (int z = 1; z < nroClasses + 1; z++)
                            soma += fullVetCorrelations[cont][z].getCMEx(k, m);

                        if (soma != 0)
                            for (int x = 1; x < nroClasses + 1; x++)
                                fullVetCorrelations[cont][x].setCMEx(k, m, soma);

                        // calculo de coverage
                      //  if(soma != 0)
                      //      fullVetCorrelations[cont][1].setCoverage(k,m,);
                     //   else
                        fullVetCorrelations[cont][1].setCoverage(k,m,soma);

                        double ent = 0;
                        for (int z = 1; z < nroClasses + 1; z++)
                            if(fullVetCorrelations[cont][z].getFullCorrelation()[k][m] != 0)
                                ent += fullVetCorrelations[cont][z].getFullCorrelation()[k][m] * (Math.log(fullVetCorrelations[cont][z].getFullCorrelation()[k][m])/Math.log(2));


                        fullVetCorrelations[cont][1].setEntropy(k,m,ent);
                      //  fullVetCorrelations[cont][1].setAccInterval(k,m,1);
                    }
                }
                cont++;
            }

        }
    }

    public void updateNormalizeCorrelationsFullVersionInc() {
        // normaliza pesos e calcula ent, coverage e define acc = 1.
        // como medidas não são diferentes para as classes, são armazenadas apenas na class 1

        int atr1Len = 0, atr2Len = 0, cont = 0;
        double soma = 0;

        for (int i = 0; i < numAttr; i++) {
            for (int j = i + 1; j < numAttr; j++) {
                atr1Len = vetAtr[i].getVetAtr().length;
                atr2Len = vetAtr[j].getVetAtr().length;
                for (int k = 0; k < atr1Len - 1; k++) {
                    for (int m = 0; m < atr2Len - 1; m++) {
                        soma = 0;
                        for (int z = 1; z < nroClasses + 1; z++)
                            soma += fullVetCorrelations[cont][z].getCMEx(k, m);

                        if (soma != 0)
                            for (int x = 1; x < nroClasses + 1; x++)
                                fullVetCorrelations[cont][x].setCMEx(k, m, soma);

                        // calculo de coverage
                        // coverage e entropia são recalculados a cada batch
                        fullVetCorrelations[cont][1].setCoverage(k,m,soma);

                        double ent = 0;
                        for (int z = 1; z < nroClasses + 1; z++)
                            if(fullVetCorrelations[cont][z].getFullCorrelation()[k][m] != 0)
                                ent += fullVetCorrelations[cont][z].getFullCorrelation()[k][m] * (Math.log(fullVetCorrelations[cont][z].getFullCorrelation()[k][m])/Math.log(2));


                        fullVetCorrelations[cont][1].setEntropy(k,m,ent);
                        //  fullVetCorrelations[cont][1].setAccInterval(k,m,1);
                    }
                }

                // acuracia está dividida por classe - soma acuraria das demais classes na classe 1, onde é acessada sem dependencia de classe
                for(int h = 2; h < nroClasses + 1; h++)
                    fullVetCorrelations[cont][1].sumAccOverClasses(fullVetCorrelations[cont][h].getAccEdge(),fullVetCorrelations[cont][h].getDeNumEdge());

                fullVetCorrelations[cont][1].normalizaAccEdge();

                    cont++;
            }

        }

    }


    public void updateNormalizeCorrelationsFullVersionInc(int[] atrMask) {
        // normaliza pesos e calcula ent, coverage e define acc = 1.
        // como medidas não são diferentes para as classes, são armazenadas apenas na class 1

        int atr1Len = 0, atr2Len = 0, cont = 0;
        double soma = 0;

        for (int i = 0; i < numAttr; i++) {
            for (int j = i + 1; j < numAttr; j++) {
                if (atrMask[i] == 1 && atrMask[j] == 1){
                atr1Len = vetAtr[i].getVetAtr().length;
                atr2Len = vetAtr[j].getVetAtr().length;
                for (int k = 0; k < atr1Len - 1; k++) {
                    for (int m = 0; m < atr2Len - 1; m++) {
                        soma = 0;
                        for (int z = 1; z < nroClasses + 1; z++)
                            soma += fullVetCorrelations[cont][z].getCMEx(k, m);

                        if (soma != 0)
                            for (int x = 1; x < nroClasses + 1; x++)
                                fullVetCorrelations[cont][x].setCMEx(k, m, soma);

                        // calculo de coverage
                        // coverage e entropia são recalculados a cada batch
                        fullVetCorrelations[cont][1].setCoverage(k, m, soma);

                        double ent = 0;
                        for (int z = 1; z < nroClasses + 1; z++)
                            if (fullVetCorrelations[cont][z].getFullCorrelation()[k][m] != 0)
                                ent += fullVetCorrelations[cont][z].getFullCorrelation()[k][m] * (Math.log(fullVetCorrelations[cont][z].getFullCorrelation()[k][m]) / Math.log(2));


                        fullVetCorrelations[cont][1].setEntropy(k, m, ent);
                        //  fullVetCorrelations[cont][1].setAccInterval(k,m,1);
                    }
                }

                for (int h = 2; h < nroClasses + 1; h++)
                    fullVetCorrelations[cont][1].sumAccOverClasses(fullVetCorrelations[cont][h].getAccEdge(), fullVetCorrelations[cont][h].getDeNumEdge());

                fullVetCorrelations[cont][1].normalizaAccEdge();
            }
                cont++;
            }

        }

    }
    public void normalizeCorrelationsFullVersionInc(int[] atrMask) {
        // normaliza pesos e calcula ent, coverage e define acc = 1.
        // como medidas não são diferentes para as classes, são armazenadas apenas na class 1

        int atr1Len = 0, atr2Len = 0, cont = 0;
        double soma = 0;

        for (int i = 0; i < numAttr; i++)
            for (int j = i + 1; j < numAttr; j++){
                if (atrMask[i] == 1 && atrMask[j] == 1){
                    atr1Len = vetAtr[i].getVetAtr().length;
                    atr2Len = vetAtr[j].getVetAtr().length;
                    for (int k = 0; k < atr1Len - 1; k++) {
                        for (int m = 0; m < atr2Len - 1; m++) {
                            soma = 0;
                            for (int z = 1; z < nroClasses + 1; z++)
                                soma += fullVetCorrelations[cont][z].getCMEx(k, m);

                            if (soma != 0)
                                for (int x = 1; x < nroClasses + 1; x++)
                                    fullVetCorrelations[cont][x].setCMEx(k, m, soma);

                            // calculo de coverage
                            //  if(soma != 0)
                            //      fullVetCorrelations[cont][1].setCoverage(k,m,);
                            //   else
                            fullVetCorrelations[cont][1].setCoverage(k,m,soma);

                            double ent = 0;
                            for (int z = 1; z < nroClasses + 1; z++)
                                if(fullVetCorrelations[cont][z].getFullCorrelation()[k][m] != 0)
                                    ent += fullVetCorrelations[cont][z].getFullCorrelation()[k][m] * (Math.log(fullVetCorrelations[cont][z].getFullCorrelation()[k][m])/Math.log(2));


                            fullVetCorrelations[cont][1].setEntropy(k,m,ent);
                           // fullVetCorrelations[cont][1].setAccInterval(k,m,1);
                        }
                    }

                }
                cont++;
        }
    }

/*
    // calcula coverage para cada intervalo
    if(normTerm != 1)
    coverageInterval[i] = normTerm;
    else
    coverageInterval[i] = -2;


}

// calcula entropia
for(int e = 0; e < nroIntervals-1; e++) {
        for (int c = 1; c < nroClasses + 1; c++)
        if(classesWeights[e][c] != 0)
        entropyInterval[e] += classesWeights[e][c] * (Math.log(classesWeights[e][c]) / Math.log(2));
        else
        entropyInterval[e] = 1;

        entropyInterval[e] *= -1;
        }

        for(int a = 0; a < nroIntervals - 1; a++) {
        // probAccInterval[a] = 1;
        for (int b = 1; b < nroClasses + 1; b++)
        probAccIntervalMat[a][b] = 1;
        }

*/

// System.out.println();

/*    public double[] probClass(double[][] A){           // retorna vetor de probabilidades da rede corrente

       int lineTe = A.length;
       int coll = A[0].length;
       double[] probVector = new double[lineTe];

       for(int z = 0; z < lineTe; z++)
          probVector[z] = 0;                // zera vetor

       for(int i = 0; i < lineTe; i++){
          for(int j = 0; j < coll - 2; j++)
             probVector[i] += fullVetCorrelations[j].findCorrelation(A[i][j], A[i][j+1]);
       }

       return probVector;
    }
*/


 /*   public void criaProbClassMaxMin(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

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
             corr = fullVetCorrelations[j].findCorrelation(A[i][j], A[i][j+1]);
             if(corr != 0)
                somaGain += Math.max(aux, corr);
             else
                somaGain += aux;
           }
            privateProbVector[i] = somaGain;
       }

    }
    */


    public void criaProbClassWeighted(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe][nroClasses+1];
        privateSumVector = new double[lineTe][nroClasses+1];
        privateProdSumVector = new double[lineTe][nroClasses+1];

        for(int z = 0; z < lineTe; z++)
            for(int c = 0; c < nroClasses +1; c++){
                privateProbVector[z][c] = 1;                // zera vetor
                privateSumVector[z][c] = 0;
                privateProdSumVector[z][c] = 1;
        }


        for(int i = 0; i < lineTe; i++){
            soma = 0;
            aux = 1;                                   //   peso de atributo - atrW[j]
            for(int c = 0; c < nroClasses+1; c++)
                for(int j = 0; j < coll - 1; j++){
                      privateProbVector[i][c] *= vetAtr[j].getWeight(A[i][j],c); // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
        }

        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    for(int c = 0; c < nroClasses +1; c++)
                        privateSumVector[i][c] += fullVetCorrelations[cont][c].findCorrelation(A[i][a], A[i][b]);
                }
                cont++;
            }

        }


        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    for(int c = 0; c < nroClasses +1; c++)
                        privateProdSumVector[i][c] += fullVetCorrelations[cont][c].findCorrelation(A[i][a], A[i][b]) * vetAtr[a].getWeight(A[i][a],(int)classe)  * vetAtr[b].getWeight(A[i][b],(int)classe);
                }
                cont++;
            }

        }


    }

    public void criaProbClassWeighted(double[][] A, int[] atrMask){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe][nroClasses+1];
        privateSumVector = new double[lineTe][nroClasses+1];
        privateProdSumVector = new double[lineTe][nroClasses+1];

      /*  for(int z = 0; z < lineTe; z++){
            privateProbVector[z][] = 0;                // zera vetor
            privateSumVector[z][] = 0;
            privateProdSumVector[z][] = 1;
        }
    */

        for(int i = 0; i < lineTe; i++){
            soma = 0;
            aux = 1;                                   //   peso de atributo - atrW[j]
            for(int j = 0; j < coll - 1; j++){
                if(atrMask[j] == 1)
                    for(int c = 0; c < nroClasses +1; c++)
                        privateProbVector[i][c] += Math.log(vetAtr[j].getWeightedInterval(A[i][j],c)); // faster // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
            for(int c = 0; c < nroClasses +1; c++)
                privateProbVector[i][c] = Math.exp(privateProbVector[i][c]);
        }


        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                if(atrMask[a] == 1 && atrMask[b] == 1 )
                    for(int i = 0; i < lineTe; i++){
                        for(int c = 0; c < nroClasses +1; c++)
                            privateSumVector[i][c] += Math.log(fullVetCorrelations[cont][c].findCorrelation(A[i][a], A[i][b]));
                    }
                cont++;
            }

        }

        for(int i = 0; i < lineTe; i++)
            for(int c = 0; c < nroClasses +1; c++)
                privateSumVector[i][c] = Math.exp(privateSumVector[i][c]);




    }

    public void criaProbClassWeighted(double[][] A, double[] atrGain){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe][nroClasses+1];
        privateSumVector = new double[lineTe][nroClasses+1];
        privateProdSumVector = new double[lineTe][nroClasses+1];

      /*  for(int z = 0; z < lineTe; z++){
            privateProbVector[z][] = 0;                // zera vetor
            privateSumVector[z][] = 0;
            privateProdSumVector[z][] = 1;
        }
    */

        for(int i = 0; i < lineTe; i++) {
            soma = 0;
            aux = 1;                                   //   peso de atributo - atrW[j]
            for (int j = 0; j < coll - 1; j++) {

                for (int c = 1; c < nroClasses + 1; c++)
                    privateProbVector[i][c] += Math.log(vetAtr[j].getWeightedInterval(A[i][j], c)); // faster // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
        }
        for(int i = 0; i < lineTe; i++)
            for(int c = 1; c < nroClasses +1; c++)
                privateProbVector[i][c] = Math.exp(privateProbVector[i][c]);


        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++) {
                    for (int c = 1; c < nroClasses + 1; c++) {
                        if(fullVetCorrelations[cont][c].findCorrelation(A[i][a], A[i][b]) > 0)
                            privateSumVector[i][c] += Math.log(fullVetCorrelations[cont][c].findCorrelation(A[i][a], A[i][b]));
                    }
                }
                cont++;
            }

        }

        for(int i = 0; i < lineTe; i++)
            for(int c = 1; c < nroClasses + 1; c++) {
                if(privateSumVector[i][c] < 0)
                    privateSumVector[i][c] = Math.exp(privateSumVector[i][c]);
            }


        System.out.println();

    }

    public void criaProbClassWeightedSA(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe][nroClasses+1];
        privateSumVector = new double[lineTe][nroClasses+1];
        privateProdSumVector = new double[lineTe][nroClasses+1];


        for(int i = 0; i < lineTe; i++){
            soma = 0;
            aux = 1;                                   //   peso de atributo - atrW[j]
            for(int j = 0; j < coll - 1; j++){
                for(int c = 0; c < nroClasses +1; c++)
                    privateProbVector[i][c] += Math.log(vetAtr[j].getWeightedInterval(A[i][j],c)); // faster // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
            for(int c = 0; c < nroClasses +1; c++)
                privateProbVector[i][c] = Math.exp(privateProbVector[i][c]);
        }



        // trata peso de correlacao entre atributos como a soma dos logs.
        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    for(int c = 0; c < nroClasses +1; c++)
                        if(fullVetCorrelations[cont][c].findCorrelation(A[i][a], A[i][b]) > 0)
                            privateSumVector[i][c] += Math.log(fullVetCorrelations[cont][c].findCorrelation(A[i][a], A[i][b]));
                }
                cont++;
            }

        }

        for(int i = 0; i < lineTe; i++)
            for(int c = 0; c < nroClasses +1; c++)
                privateSumVector[i][c] = Math.exp(privateSumVector[i][c]);


    }


    public void criaProbClassWeightedGeneticAlgorithm(double[][] A, GAedges[] vetEdge){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe][nroClasses+1];
        privateSumVector = new double[lineTe][nroClasses+1];
        privateProdSumVector = new double[lineTe][nroClasses+1];


        for(int i = 0; i < lineTe; i++){
            soma = 0;
            aux = 1;
            for(int c = 1; c < nroClasses+1; c++)
                for(int j = 0; j < coll - 1; j++){
                    if(vetAtr[j].getWeight(A[i][j],c) != 0)
                        privateProbVector[i][c] += Math.log(vetAtr[j].getWeight(A[i][j],c)); // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
        }

        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    for(int c = 1; c < nroClasses+1; c++)
                            if(vetEdge[cont].getConexao(fullVetCorrelations[cont][c].getIntervalNumATR1(A[i][a]),fullVetCorrelations[cont][c].getIntervalNumATR2(A[i][b])) == 1)
                                if(fullVetCorrelations[cont][c].findCorrelation(A[i][a], A[i][b]) != 0)
                                    privateSumVector[i][c] += Math.log(fullVetCorrelations[cont][c].findCorrelation(A[i][a], A[i][b]));
                }
                cont++;
            }

        }
    }


    public void criaProbClassWeightedGeneticAlgorithmF1(double[][] A, GAedges[] vetEdge){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe][nroClasses+1];
        privateSumVector = new double[lineTe][nroClasses+1];
        privateProdSumVector = new double[lineTe][nroClasses+1];

   /*     for(int z = 0; z < lineTe; z++){
            privateProbVector[z] = 0;                // zera vetor
            privateSumVector[z] = 0;
            privateProdSumVector[z] = 1;
        }
*/

        for(int i = 0; i < lineTe; i++){
            soma = 0;
            aux = 1;                                   //   peso de atributo - atrW[j]
            for(int j = 0; j < coll - 1; j++){
                if(vetAtr[j].getWeight(A[i][j],(int)classe) != 0)
                    privateProbVector[i][(int)classe] *= vetAtr[j].getWeight(A[i][j],(int)classe); // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
        }

        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    for(int c = 0; c < nroClasses + 1; c++)
                        if(vetEdge[cont].getConexao(fullVetCorrelations[cont][c].getIntervalNumATR1(A[i][a]),fullVetCorrelations[cont][c].getIntervalNumATR2(A[i][b])) == 1)
                            if(fullVetCorrelations[cont][c].findCorrelation(A[i][a], A[i][b]) != 0)
                                privateSumVector[i][c] += fullVetCorrelations[cont][c].findCorrelation(A[i][a], A[i][b]);
                }
                cont++;
            }

        }


    }



    public double retrieveSum(double[] inst ,int atr, int numInter, int classe){   //      imputa��o teste

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

       for(int i = 0; i < numAttr; i++){
          for(int j = i + 1; j < numAttr; j++){
             if(i == atr){
                atrCorr = fullVetCorrelations[cont][classe].findPossibleCorrelations(i, j, inst[j]); // falta atributo i
                auxInd = cont;

               for(int p = 0; p < len; p++)
                  somaAtr[p] += atrCorr[p];
             }
            else if (j == atr){
                atrCorr = fullVetCorrelations[cont][classe].findPossibleCorrelations(j, i, inst[i]);
                auxInd = cont;

                for(int p = 0; p < len; p++)
                 somaAtr[p] += atrCorr[p];
             }

             cont++;
             // retorna vetor com possiveis intervalos - pertencentes ao atributo faltante

             // atrCorr armazena os possiveis intervalos do atributo atr faltante  -  todos t�m a mesma dimens�o

          }
        }


         indiceIntervalo = maiorInd(somaAtr);
         indiceVetCorr = auxInd;

         return somaAtr[indiceIntervalo];  // maior soma



     }

      public double retrieveSumExperimenting(double[] inst ,int atr, int numInter, int classe){   //      imputa��o teste

         // verificar (atr-1) x atr e atr x (atr + 1)
         int coll = inst.length;
         int intervalo = 0, inta, intb;
         int cont = 0;
         int len = numInter;            // numero de intervalos -1
         double[] somaAtr = new double[len];
         double[] atrCorr = new double[len];
         int auxInd = 0; // armazena algum indice de vetCorrelation onde um dos attributos seja o faltante
         double weightVert = 0;
         indiceIntervalo = 0;   // imputa��o de testes
         indiceVetCorr = 0;

       for(int i = 0; i < numAttr; i++){
          for(int j = i + 1; j < numAttr; j++){
             if(i == atr){
                atrCorr = fullVetCorrelations[cont][classe].findPossibleCorrelations(i, j, inst[j]); // falta atributo i
                auxInd = cont;
                weightVert = vetAtr[j].getIntervalGain() * vetAtr[j].getWeight(inst[j],(int)classe);

               for(int p = 0; p < len; p++)
                  somaAtr[p] += weightVert * atrCorr[p];
             }
            else if (j == atr){
                atrCorr = fullVetCorrelations[cont][classe].findPossibleCorrelations(j, i, inst[i]);
                auxInd = cont;
                weightVert = vetAtr[i].getIntervalGain() * vetAtr[i].getWeight(inst[j],(int)classe);

                for(int p = 0; p < len; p++)
                 somaAtr[p] += weightVert * atrCorr[p];
             }

             cont++;
             // retorna vetor com possiveis intervalos - pertencentes ao atributo faltante

             // atrCorr armazena os possiveis intervalos do atributo atr faltante  -  todos t�m a mesma dimens�o

          }
        }

          for(int p = 0; p < len; p++)
             somaAtr[p] *= vetAtr[atr].getIntClass(p,(int)classe);// * vetAtr[atr].getIntervalGain(); // peso dos vertices, no atributo desconhecido


         indiceIntervalo = maiorInd(somaAtr);
         indiceVetCorr = auxInd;

         return somaAtr[indiceIntervalo];  // maior soma



     }

     public double getMaxCorrelation(int indVec, double v1, double v2){

        double maior = 0, aux;
        for(int classe = 1; classe < nroClasses + 1; classe++) {
            aux = fullVetCorrelations[indVec][classe].findCorrelation(v1, v2);
            if (aux > maior)
                maior = aux;
        }

        return maior;
     }

    public double[] getIntervalTest(int atr, int classe){

        return fullVetCorrelations[indiceVetCorr][classe].getInterval(indiceIntervalo,atr);

    }

      public double[] retrieveInterval(double[] inst ,int atr, int numInter, int classe){   //      imputa��o treino

         // verificar (atr-1) x atr e atr x (atr + 1)
         int coll = inst.length;
         int intervalo = 0, inta, intb;
         int cont = 0;
         int len = numInter;            // numero de intervalos -1
         double[] somaAtr = new double[len];
         double[] atrCorr = new double[len];
         int auxInd = 0; // armazena algum indice de vetCorrelation onde um dos attributos seja o faltante

       for(int i = 0; i < numAttr; i++){
          for(int j = i + 1; j < numAttr; j++){
             if(i == atr){
                atrCorr = fullVetCorrelations[cont][classe].findPossibleCorrelations(i, j, inst[j]); // falta atributo i
                auxInd = cont;

               for(int p = 0; p < len; p++)
                  somaAtr[p] += atrCorr[p];
             }
            else if (j == atr){
                atrCorr = fullVetCorrelations[cont][classe].findPossibleCorrelations(j, i, inst[i]);
                auxInd = cont;

                for(int p = 0; p < len; p++)
                 somaAtr[p] += atrCorr[p];
             }
                         
             cont++;
             // retorna vetor com possiveis intervalos - pertencentes ao atributo faltante

             // atrCorr armazena os possiveis intervalos do atributo atr faltante  -  todos t�m a mesma dimens�o

          }
        }


         intervalo = maiorInd(somaAtr,atr,inst[coll-1]);   // resolve impate


         return fullVetCorrelations[auxInd][classe].getInterval(intervalo,atr);
         //   encontrar intervalo correspondente a correlaçao em attributeCorrelation

     }

     public double[] retrieveIntervalExperimenting(double[] inst ,int atr, int numInter, int classe){   //      imputaçao treino
                           // atr � atributo que falta
         // verificar (atr-1) x atr e atr x (atr + 1)
         int coll = inst.length;
         int intervalo = 0, inta, intb;
         int cont = 0;
         int len = numInter;            // numero de intervalos -1
         double[] somaAtr = new double[len];
         double[] atrCorr = new double[len];
         int auxInd = 0; // armazena algum indice de vetCorrelation onde um dos attributos seja o faltante
         double weightVert = 0;

       for(int i = 0; i < numAttr; i++){
          for(int j = i + 1; j < numAttr; j++){
             if(i == atr){
                atrCorr = fullVetCorrelations[cont][classe].findPossibleCorrelations(i, j, inst[j]); // falta atributo i
                auxInd = cont;
                weightVert = vetAtr[j].getWeight(inst[j],(int)classe) * vetAtr[j].getIntervalGain();

               for(int p = 0; p < len; p++)
                  somaAtr[p] += weightVert * atrCorr[p];   // peso do vertice em que existe valor de atributo
             }
            else if (j == atr){
                atrCorr = fullVetCorrelations[cont][classe].findPossibleCorrelations(j, i, inst[i]);
                auxInd = cont;
                weightVert = vetAtr[i].getWeight(inst[j],(int)classe) * vetAtr[i].getIntervalGain();

                for(int p = 0; p < len; p++)
                 somaAtr[p] += weightVert * atrCorr[p];
             }

             cont++;
             // retorna vetor com possiveis intervalos - pertencentes ao atributo faltante

             // atrCorr armazena os possiveis intervalos do atributo atr faltante  -  todos t�m a mesma dimens�o

          }
        }


          for(int p = 0; p < len; p++)
             somaAtr[p] *= vetAtr[atr].getIntClass(p,(int)classe); // peso dos vertices, no atributo desconhecido

         intervalo = maiorInd(somaAtr,atr,inst[coll-1]);   // resolve impate

         return fullVetCorrelations[auxInd][classe].getInterval(intervalo,atr);
         //   encontrar intervalo correspondente � correla��o em attributeCorrelation

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

      public double getProdSum(int i, int c){
         return privateProdSumVector[i][c];
     }



     public int maiorInd(double[] A){
        int ind = 0;
        double maior = A[0];
        int cont = 0;
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

        somaPesos = maior;  // para refinig data sets
           // System.out.println("Empate!");

       return ind;
    }

    public double getProb(int i, int c){
        return privateProbVector[i][c];
    }

    public double getSum(int i, int c){
        return privateSumVector[i][c];
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

    public AttributeCorrelation[][] getVetCorrelation(){
        return fullVetCorrelations;
    }



    public  AttributeHandler[] getVetAtr(){
        return vetAtr;
    }


    public double getSomaPesos(){
        return somaPesos;
    }


    private double[][] E;
    private double[][] privateProbVector;
    private double[][] privateProdSumVector;
    private double[][] privateSumVector;
    private double classe;
    private double somaPesos;  // usada em refinig data sets
  //  private int[] ordem;
    private int line, coll, nroClasses;
    private AttributeCorrelation[][] fullVetCorrelations;
    private AttributeHandler[] vetAtr;
    private double emptyValue = -10000;
    private int numAttr, numEdgeFull;

    //  usado na imputa��o de instancias de teste
    private int indiceIntervalo, indiceVetCorr;

}
