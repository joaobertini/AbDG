/**
 * Created by João on 12/03/2018.
 */
public class FullEvolution {

    public FullEvolution( AttributeHandler[][] matAtrHandler, double[][] train, double[][] valid, int numClass, int poolsize, int numDiscretization){

        // nroClasses = networksFull.length;
        matriz = train;
        nroClasses = numClass;
        popSize = poolsize;
        numAttr = train[0].length-1;  //matAtrHandler[0].length;
        this.matAtrHandler = matAtrHandler;
        this.numDiscretization = numDiscretization;
        numEdgeFull = numAttr + (numAttr*(numAttr-3))/2;
        int cont, aux;

        Population = new GAIndividual[popSize];
        for(int i = 0; i < popSize; i++)
            Population[i] = new GAIndividual(nroClasses,numAttr,numEdgeFull);


        // vetGAedges = new GAedges[poolsize][numEdgeFull];  // vetor que representa conexoes entre atributos - e sera usado para manipulacao do GA
        // vetGAvertices = new GAvertices[poolsize][numAttr];  // vetor que representa possiveis intervalos dentro dos atributos - e sera usado para manipulacao do GA
        // vetGA edge e vertices representam a população; vetGAedges[a][] e vetGAvertices[a][] - representa o individuo a

        // cria população
        for(int a = 0; a < poolsize; a++){
            GAedges[] vetGAedges = new GAedges[numEdgeFull];
            GAvertices[] vetGAvertices = new GAvertices[numAttr];
            // cria vertices
            for(int i = 0; i < numAttr; i++) {
                aux = (int) (Math.random() * numDiscretization);     //sorteia dicretizador
                vetGAvertices[i] = new GAvertices(matAtrHandler[i][aux], aux);
            }
            // cria arestas
            cont = 0;
            for(int i = 0; i < numAttr; i++) {
                for (int j = i + 1; j < numAttr; j++) {
                    vetGAedges[cont] = new GAedges(i, j, vetGAvertices[i].getVetAtrLen(), vetGAvertices[j].getVetAtrLen());
                    cont++;
                }
            }
            Population[a].setVertexSet(vetGAvertices);
            Population[a].setEdges(vetGAedges);
        }

         evolve(valid);
    }

    public void evolve(double[][] valid){

        int itMax = 100;
        int[] numSelecionados;
       // double[] populacao = new double[popSize];  // vetor que armazena o fitness de cada individuo

        for(int i = 0; i < itMax; i++) {

            for (int j = 0; j < popSize; j++) {
              //  System.out.println(j);
                Population[j].fitness(matriz, valid);   //  fitness treino / não seria no teste?
            }

            Population = selection();
            crossover();
            mutation();
            //System.out.println();
        }


        // encontra o melhor individuo - não considera elitismo
        int indMaior = 0;
        double maior = 0, aux;
        for (int j = 1; j < popSize; j++) {
            Population[j].fitness(matriz,valid);
            aux = Population[j].getFitness();  // acuracia
            System.out.println(aux);
            if(maior < aux){
                maior = aux;
                indMaior = j;
            }
        }
        bestSolution = Population[indMaior];
        bestAcc = Population[indMaior].getFitness();
     }

    public GAIndividual[] selection(){ // seleciona popSize individuos torneio (tentar roleta, ...)

        int Nt = 3;  // numero de individuos no torneio
        double[] match = new double[3];
        double acc = 0, bestAcc = 0;
        int indMaior, ind;
        int[] newPopulationMask = new int[popSize];
        for(int j = 0; j < popSize; j++) {
            indMaior = -1;
            bestAcc = 0;
            for (int i = 0; i < Nt; i++) {              // torneio
                ind = (int) (Math.random() * popSize);
                acc = Population[ind].getFitness();
                if (bestAcc < acc) {
                    bestAcc = acc;
                    indMaior = ind;
                }
            }
            newPopulationMask[j] = indMaior;     // indice dos melhores individuos de população / Mask
        }


        GAIndividual[] newPopulation = new GAIndividual[popSize];

        for(int j = 0; j < popSize; j++) {
            newPopulation[j] = Population[newPopulationMask[j]];
        }

        return newPopulation;
    }

     public void crossover(){ // reproduz melhores individuos, não ordena por fitness
        // dois novos individuos são gerados escolhendo aleatoriamente os atributos de dois pais
        // conexões entre os novos atributos são estabelecidas somando-se na matriz em direção contrária à dimensão
        // de interesse para ambos os vetores; em seguida as novas arestas são determinadas por produto externo.

        int cont, p1, p2;
        double prob, probCross = 0.8;  // probabilidade de ocorrer crossover
        GAIndividual offspring1, offspring2;


        for(int i = 0; i < popSize - 2; i += 2) {  // população tem que ser par
            prob = Math.random();
            if(prob < probCross) {  // prob de ocorrer crossover
                // cópia de população
                offspring1 = new GAIndividual(nroClasses,numAttr,numEdgeFull); //Population[i];
                offspring2 = new GAIndividual(nroClasses,numAttr,numEdgeFull); // Population[i+1];

                for (int j = 0; j < numAttr; j++) {      // crossover dos vertices
                    if(Math.random() > 0.5) {
                        offspring1.setVerticesAttr(Population[i].getVerticesAttr(j),j,i);
                        offspring2.setVerticesAttr(Population[i+1].getVerticesAttr(j),j,i+1);
                    }
                    else{
                        offspring2.setVerticesAttr(Population[i].getVerticesAttr(j),j,i);
                        offspring1.setVerticesAttr(Population[i+1].getVerticesAttr(j),j,i+1);
                    }
                }

                cont = 0;
                for(int k = 0; k < numAttr; k++) {
                    for (int m = k + 1; m < numAttr; m++) {
                        // conexão k e m;
                        // verifica se atributos k e m são do mesmo pai
                        if(offspring1.getAttrParent(k) == offspring1.getAttrParent(m)) {   //   offspring1.getVerticesAttr(k).getDiscMethod() == offspring1.getVerticesAttr(m).getDiscMethod()
                            if(offspring1.getAttrParent(k) == i) {
                                offspring1.setEdgesAttr(Population[i].getEdgesAttr(cont), cont);  // cont corresponde à combinação k e m
                                // é igual para offspring2 também - resta saber qual é o atributo
                                offspring2.setEdgesAttr(Population[i + 1].getEdgesAttr(cont), cont);   // ver se são do mesmo pai?
                            }
                            else{
                                offspring1.setEdgesAttr(Population[i + 1].getEdgesAttr(cont), cont);  // cont corresponde à combinação k e m
                                offspring2.setEdgesAttr(Population[i].getEdgesAttr(cont), cont);   // ver se são do mesmo pai?
                            }
                        }
                        else{ // sets offspring1

                          //  do {
                                p1 = offspring1.getAttrParent(k);
                                p2 = offspring1.getAttrParent(m);
                                offspring1.setEdgesAttr(new GAedges(k, m, Population[p1].projectConnection(cont, k), Population[p2].projectConnection(cont, m)), cont);

                          //  }while(!offspring1.checkArchitecture(k,m,cont));
                                                        //todo verificar projeções e produto externo. Tentar alternativas no p.e. min, mult, double, etc..

                            // sets offspring2
                          //  do {
                                p1 = offspring2.getAttrParent(k);
                                p2 = offspring2.getAttrParent(m);
                                offspring2.setEdgesAttr(new GAedges(k, m, Population[p1].projectConnection(cont, k), Population[p2].projectConnection(cont, m)), cont);
                          //  }while(!offspring2.checkArchitecture(k,m,cont));
                        }
                        cont++;
                    }
                }

                Population[i] = null;
                Population[i+1] = null;
                Population[i] = offspring1;
                Population[i+1] = offspring2;

                }
            }

        }



    public void mutation(){
        // mutation may happen in vertex and edge with a given probability. A vertex mutation changes a vertex set to a
        // another one built with a different discretization. Edge mutation adds or removes a given edge.


        int cont;
        double prob, probMutationVertex = 0.1;    // proba of vertex mutaion
        double probMutationEdge = 0.1;           // probability of edge mutation
        int mutateAtAttr;   // atributo - conjunto de vértices - que sofrerá a mutação
        int currentDisc, mutDisc;
        GAIndividual offspring1, offspring2;

        for(int i = 0; i < popSize; i++) {  // população tem que ser par
            // vertex mutation
            prob = Math.random();
            if (prob < probMutationVertex) {  // prob de ocorrer crossover
                mutateAtAttr = (int) (Math.random() * numAttr);
                currentDisc = Population[i].getVerticesAttr(mutateAtAttr).getDiscMethod();

                do {
                    mutDisc = (int) (Math.random() * numDiscretization);
                } while (mutDisc == currentDisc);

                //  cria novo conjunto de vertice e insere no atributo mutateAtAttr / -1 para pai indica mutação
                Population[i].setVerticesAttr(new GAvertices(matAtrHandler[mutateAtAttr][mutDisc], mutDisc), mutateAtAttr, -1);
                // todo ver parent -1

                int intSize = matAtrHandler[mutateAtAttr][mutDisc].getNumInterval(); // retorna tamanho do intervalo adicionado
                int[] newConections = new int[intSize];
                for (int h = 0; h < intSize; h++)
                    newConections[h] = 1; // novo atributo inicia com vetor - projeção - com 1's / testar aleatório


                // adapta conexões do individuo ao novo conjunto de vertices.
                cont = 0;
                for (int k = 0; k < numAttr; k++) {
                    for (int m = k + 1; m < numAttr; m++) {
                        // conexão k e m;
                        // verifica se atributos k ou m foi alterado
                        if (k == mutateAtAttr) // se k foi alterado, projeta vetor de conexão em m e cria nova matriz entre a projeção e um vetor com 1's
                            Population[i].setEdgesAttr(new GAedges(k, m, newConections, Population[i].projectConnection(cont, m)), cont);

                        if (mutateAtAttr == m)
                            Population[i].setEdgesAttr(new GAedges(k, m, Population[i].projectConnection(cont, k), newConections), cont);

                        cont++;
                    }
                }

            }


            // edge mutation
            prob = Math.random();
            int mutateAt;
            if(probMutationEdge > prob) {
                mutateAt = (int) (Math.random() * numEdgeFull);
                Population[i].getEdgesAttr(mutateAt).mutation();  // Mutacao identica a realizada no paper do congresso
            }

        }

    }

    public double[][] bootstrap(double[][] A){    // retorna um conjunto boostrap
        // Bertini
        int line = A.length;
        int coll = A[0].length;
        int index, cont = 0;
        double[][] baggA = new double[line][coll];

        for(int i = 0; i < line; i++){
            index = (int)(Math.random()*line);

            for(int j = 0; j < coll; j++)
                baggA[cont][j] = A[index][j];

            cont++;
        }


        return baggA;
    }


    public double getAcc(){
        return bestAcc;
    }

    public double predict(double[][] Te){
        return bestSolution.predict(matriz,Te);
    }

    private GAIndividual[] Population;
    private GAIndividual bestSolution;
    //private NetworkFull[] networksFull;
    AttributeHandler[][] matAtrHandler;
    private int nroClasses, popSize, numEdgeFull;
    private double[][] matriz;
    private int[] bestFitness;
    private int numAttr, numDiscretization;
    private double bestAcc;

}

/*   public double fitness(int individuo, double[][] treino, double[][] teste){  // evaluates the archtedure defined by GAvertices and GAedges
                                           // also search for the best neta

        double somaProb = 0, somaSum = 0, aux = 0;
        double maior = 0, classe, acertos = 0, bestNeta = 0, bestAcc = 0;
        int line = matriz.length, indMaior;
        int coll = matriz[0].length;

        // criar vetAtrHandler usando GAvertice

        NetworkFull networksFull = new NetworkFull(treino, nroClasses);
        networksFull.learnFullConectionGAextended(vetGAvertices[individuo]);

        int numAttr = coll - 1;
        int numEdges = numAttr + (numAttr*(numAttr-3))/2;

        //  printNetwork(coll-1);

        networksFull.normalizeCorrelationsFullVersion();

        //normalizeIntervalGain(coll-1);


        for(int b = 0; b < nroClasses; b++){                                  // vetAttrCorr
            networksFull.criaProbClassWeightedGeneticAlgorithm(teste,vetGAedges[individuo]);       // Weighted(matriz,attrGain);                // ############### classificador 3
            //    vetAux = networks[b].getVetCorrelation();
        }


        for(double neta = 0.1; neta < 1.05; neta += 0.1 ) {

            for (int a = 0; a < line; a++) {

                somaProb = 0;
                somaSum = 0;
                for (int b = 0; b < nroClasses; b++) {
                    somaProb += networksFull.getProb(a, b);
                    somaSum += networksFull.getSum(a, b);
                }


                if (somaSum == 0 && somaProb == 0)
                    maior = 0;
                else if (somaSum == 0)
                    maior = (networksFull.getProb(a, 1) / somaProb);
                else if (somaProb == 0)
                    maior = (networksFull.getSum(a, 1) / somaSum);
                else
                    maior = (neta * (networksFull.getSum(a, 1) / somaSum) + (1 - neta) * (networksFull.getProb(a, 1) / somaProb));

                indMaior = 0;
                for (int c = 2; c < nroClasses + 1; c++) {

                    if (somaSum != 0 && somaProb != 0)
                        aux = (neta * (networksFull.getSum(a, c) / somaSum) + (1 - neta) * (networksFull.getProb(a, c) / somaProb));
                    else if (somaSum == 0 && somaProb == 0)
                        aux = 0;
                    else if (somaSum == 0)
                        aux = (networksFull.getProb(a, c) / somaProb);
                    else if (somaProb == 0)
                        aux = (networksFull.getSum(a, c) / somaSum);


                    if (aux > maior) {
                        maior = aux;
                        indMaior = c;
                    }
                }

                classe = indMaior;

                if (classe == matriz[a][coll - 1])
                    acertos++;


            } // for-line

            if(acertos/line > bestAcc) {
                bestAcc = acertos / line;
                bestNeta = neta;
            }

        }

        return (bestAcc);
    }
*/

/*

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
*/