/**
 * Created by João on 19/01/2016.
 */
public class Evolution {

    public Evolution(NetworkFull[] networksFull, AttributeHandler[] vetAtrHandler, double[][] m, int poolsize, double n){

        nroClasses = networksFull.length;
        matriz = m;
        neta = n;
        popSize = poolsize;
        abdg = networksFull;
        numAttr = vetAtrHandler.length;
        numEdgeFull = numAttr + (numAttr*(numAttr-3))/2;
        int cont;

        vetGAedges = new GAedges[poolsize][numEdgeFull];  // vetor que representa conexoes entre atributos - e sera usado para manipulacao do GA

        for(int a = 0; a < poolsize; a++){
           cont = 0;
           for(int i = 0; i < numAttr; i++) {
               for (int j = i + 1; j < numAttr; j++) {
                   vetGAedges[a][cont] = new GAedges(i, j, vetAtrHandler[i].getVetAtr().length, vetAtrHandler[j].getVetAtr().length);
                   cont++;
               }
           }
        }

        evolve();
    }

    public void evolve(){

        int itMax = 100;
        int numSelecionados;
        double[] populacao = new double[popSize];  // vetor que armazena o fitness de cada individuo
        for(int i = 0; i < itMax; i++) {

            for (int j = 0; j < popSize; j++)
                populacao[j] = fitness1(vetGAedges[j]);


        //    for(int p = 0; p < popSize; p++)
         //       System.out.print(populacao[p] + " ");
        //    System.out.println();

            numSelecionados = selection(populacao);
            reproduction(numSelecionados);        // gera numSelecionados individuos
            crossover(numSelecionados);
            mutation(numSelecionados);




        }



    }

    public int selection(double[] populacao){ // seleciona os individuos com maior acuracia

        int selSize = (int)(0.3 * popSize); // porcentagem da populacao que será mantida
        bestFitness = new int[popSize];
        double maior;
        int indMaior;

        for(int k = 1; k <= selSize; k++) {
            maior = 0;
            indMaior = 0;
            for (int i = 0; i < popSize; i++)
                if (populacao[i] > maior && bestFitness[i] == 0) {
                    maior = populacao[i];
                    indMaior = i;
                }
            bestFitness[indMaior] = k;
            if( k == 1)
                bestAcc = maior;
        }
          return selSize;
    }

    public double fitness(GAedges[] va){  // retorna acuracia da arquitetura passada como um vetor de GAedges

        double somaProb = 0, somaSum = 0;
        double maior = 0, classe, acertos = 0;
        int line = matriz.length, indMaior;
        int coll = matriz[0].length;

        for(int b = 0; b < nroClasses; b++){                                  // vetAttrCorr
            abdg[b].criaProbClassWeightedGeneticAlgorithm(matriz,va);       // Weighted(matriz,attrGain);                // ############### classificador 3
            //    vetAux = networks[b].getVetCorrelation();
        }


        for(int a = 0; a < line; a++){

           somaProb = abdg[0].getProb(a);
           somaSum = abdg[0].getSum(a);

           somaSum += somaProb;
           maior = somaSum;
           indMaior = 0;

           for(int c = 1; c < nroClasses; c++){

               somaProb = abdg[c].getProb(a);
               somaSum = abdg[c].getSum(a);

               somaSum += somaProb;
               somaSum = somaSum;

               if(somaSum > maior){
                   maior = somaSum;
                   indMaior = c;
                 }
              }

               classe = abdg[indMaior].getClasse();


               if(classe == matriz[a][coll-1])
                   acertos++;


        } // for-line

        return (acertos/line);
    }



    public double fitness1(GAedges[] va){  // retorna acuracia da arquitetura passada como um vetor de GAedges

        double somaProb = 0, somaSum = 0, maior = 0, aux = 0, classe, acertos = 0;
        int line = matriz.length, indMaior = 0;
        int coll = matriz[0].length;

        for(int b = 0; b < nroClasses; b++){                                  // vetAttrCorr
            abdg[b].criaProbClassWeightedGeneticAlgorithmF1(matriz,va);       // Weighted(matriz,attrGain);                // ############### classificador 3
            //    vetAux = networks[b].getVetCorrelation();
        }


        for(int a = 0; a < line; a++){

            somaProb = 0;
            somaSum = 0;
            for(int b = 0; b < nroClasses; b++){
                somaProb += abdg[b].getProb(a);
                somaSum += abdg[b].getSum(a);
            }



                if(somaSum == 0 && somaProb == 0)
                    maior = 0;
                else if(somaSum == 0)
                    maior = (abdg[0].getProb(a)/somaProb);
                else if(somaProb == 0)
                    maior = (abdg[0].getSum(a)/somaSum);
                else
                    maior = (neta*(abdg[0].getSum(a)/somaSum) + (1-neta)*(abdg[0].getProb(a)/somaProb));

                indMaior = 0;
                for(int c = 1; c < nroClasses; c++){

                    if(somaSum != 0 && somaProb != 0)
                        aux = (neta*(abdg[c].getSum(a)/somaSum) + (1-neta)*(abdg[c].getProb(a)/somaProb));
                    else if(somaSum == 0 && somaProb == 0)
                        aux = 0;
                    else if(somaSum == 0)
                        aux = (abdg[c].getProb(a)/somaProb);
                    else if(somaProb == 0)
                        aux = (abdg[c].getSum(a)/somaSum);


                    if(aux > maior){
                        maior = aux;
                        indMaior = c;
                    }
                }

                classe = abdg[indMaior].getClasse();

            if(classe == matriz[a][coll-1])
                acertos++;


        } // for-line

        return (acertos/line);



    }




    public void reproduction(int numSel){ // reproduz dois melhores / numSel deve ser par
        int pai1 = 0, pai2 = 0;
        int cont = 0;


        for(int i = 1; i <= numSel; i++) {

            for (int j = 0; j < popSize; j++)
                if (bestFitness[j] == i)
                    if (i % 2 != 0) // i é impar
                        pai1 = j;
                    else
                        pai2 = j; // i é par


            if (i % 2 == 0)  // a cada par
              for(int q = 0; q < 2; q++)  // reproducao gera dois filhos
                for(int k = 0; k < popSize; k++) {
                    if (bestFitness[k] == 0) {
                        cont = 0;
                        for (int a = 0; a < numAttr; a++) {
                            for (int b = a + 1; b < numAttr; b++) {
                                vetGAedges[k][cont].reproduction(vetGAedges[pai1][cont].getMatConexao(), vetGAedges[pai2][cont].getMatConexao(), q);                  // GAedges(i, j, vetAtrHandler[i].getVetAtr().length, vetAtrHandler[j].getVetAtr().length);
                                cont++;
                            }
                        }

                    }

                    bestFitness[k] = -1; // para não ser selecionado novamente
                }

        }


    }

    public void crossover(int numSel){ // reproduz dois melhores / numSel deve ser par
        int pai1 = 0, pai2 = 0;
        int cont = 0;
        int crossPoint = (int)Math.random() * numEdgeFull;


        for(int i = 1; i <= numSel; i++) {

            for (int j = 0; j < popSize; j++)
                if (bestFitness[j] != 0 && bestFitness[j] != 1)  // primeiros pais possiveis encontrados
                    if (i % 2 != 0) // i é impar
                        pai1 = j;
                    else
                        pai2 = j; // i é par


            if (i % 2 == 0)  // a cada par
                for(int q = 0; q < 2; q++)  // reproducao gera dois filhos
                    for(int k = 0; k < popSize; k++)
                        if(bestFitness[k] == 0){
                            cont = 0;
                            for(int a = 0; a < numAttr; a++) {
                                for (int b = a + 1; b < numAttr; b++) {
                                    if(cont < crossPoint)
                                        vetGAedges[k][cont].cross(vetGAedges[pai1][cont].getMatConexao());                  // GAedges(i, j, vetAtrHandler[i].getVetAtr().length, vetAtrHandler[j].getVetAtr().length);
                                    else
                                        vetGAedges[k][cont].cross(vetGAedges[pai2][cont].getMatConexao());
                                    cont++;
                                }
                            }
                            bestFitness[k] = -1;
                        }

        }


    }

    public void mutation(int numSel){

        int mut = (int)(2 + Math.random()*(numSel-2));
        int mutateAt; //(int) Math.random() * numEdgeFull;


        for(int i = 2; i < popSize; i++){
            mutateAt = (int) Math.random() * numEdgeFull;
            vetGAedges[i][mutateAt].mutation();                  // GAedges(i, j, vetAtrHandler[i].getVetAtr().length, vetAtrHandler[j].getVetAtr().length);
        }

    }

    public double getAcc(){
        return bestAcc;
    }

    private GAedges[][] vetGAedges;
    private NetworkFull[] abdg;
    private int nroClasses, popSize, numEdgeFull;
    private double[][] matriz;
    private int[] bestFitness;
    private int numAttr;
    private double bestAcc, neta;
}
