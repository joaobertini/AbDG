public class GAIndividual {
        // classe que define individuo, formado por um vetor de vertices e um de arestas

    public GAIndividual(int nc, int numAtr, int numEdges) {
        nroClasses = nc;
        fit = 0;
        neta = 0;
        Vertices = new GAvertices[numAtr];
        Edges = new GAedges[numEdges];
        AttrParents = new int[numAtr];

    }


    public double fitness(double[][] treino, double[][] teste){  // evaluates the archtedure defined by GAvertices and GAedges
        // also search for the best neta

        double somaProb = 0, somaSum = 0, aux = 0;
        double maior = 0, classe, acertos = 0, bestNeta = 0, bestAcc = 0;
        int line = teste.length, indMaior;
        int coll = treino[0].length;

        // criar vetAtrHandler usando GAvertice
        checkArchitecture();

        networksFull = new NetworkFull(treino, nroClasses);
        networksFull.learnFullConectionGAextended(Vertices);

        int numAttr = coll - 1;
        int numEdges = numAttr + (numAttr*(numAttr-3))/2;

        //  printNetwork(coll-1);

        networksFull.normalizeCorrelationsFullVersion();

        //normalizeIntervalGain(coll-1);


        for(int b = 0; b < nroClasses; b++){                                  // vetAttrCorr
            networksFull.criaProbClassWeightedGeneticAlgorithm(teste, Edges);       // Weighted(matriz,attrGain);                // ############### classificador 3
            //    vetAux = networks[b].getVetCorrelation();
        }


        for(double neta = 0.1; neta < 1.05; neta += 0.1 ) {
            acertos = 0;
            for (int a = 0; a < line; a++) {

                somaProb = 0;
                somaSum = 0;
                for (int b = 1; b < nroClasses+1; b++) {
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

                indMaior = 1;
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

                if (classe == teste[a][coll - 1])
                    acertos++;


            } // for-line

            if(acertos/line > bestAcc) {
                bestAcc = acertos / line;
                this.neta = neta;
            }

        }
        fit = bestAcc;

        return bestAcc;
    }


    public void checkArchitecture() {

        int numAttr = Vertices.length;
        int aux1, aux2, cont;
        int[][] mat;
        boolean flag = true;

        cont = 0;
        for (int i = 0; i < numAttr; i++) {
            for (int j = i + 1; j < numAttr; j++) {
                aux1 = Vertices[i].getVetAtrLen();
                aux2 = Vertices[j].getVetAtrLen();
                mat = Edges[cont].getMatConexao();
                if((aux1 -1) != mat.length || (aux2-1) != mat[1].length)
                    flag = false;
                cont++;
            }
        }
    }

    public boolean checkArchitecture(int v1, int v2, int cont) {

        int numAttr = Vertices.length;
        int aux1, aux2;
        int[][] mat;
        boolean flag = true;

        aux1 = Vertices[v1].getVetAtrLen();
        aux2 = Vertices[v2].getVetAtrLen();
        mat = Edges[cont].getMatConexao();
        if((aux1 -1) != mat.length || (aux2-1) != mat[1].length)
            flag = false;

        return flag;

    }

    public double predict(double[][] treino, double[][] teste){  // evaluates the archtedure defined by GAvertices and GAedges

        double somaProb = 0, somaSum = 0, aux = 0;
        double maior = 0, classe, acertos = 0, bestAcc = 0;
        int line = teste.length, indMaior;
        int coll = teste[0].length;

        // criar vetAtrHandler usando GAvertice

        networksFull = new NetworkFull(treino, nroClasses);
        networksFull.learnFullConectionGAextended(Vertices);

        int numAttr = coll - 1;
        int numEdges = numAttr + (numAttr*(numAttr-3))/2;

        //  printNetwork(coll-1);

        networksFull.normalizeCorrelationsFullVersion();


        for(int b = 0; b < nroClasses; b++){                                  // vetAttrCorr
            networksFull.criaProbClassWeightedGeneticAlgorithm(teste,Edges);       // Weighted(matriz,attrGain);                // ############### classificador 3
            //    vetAux = networks[b].getVetCorrelation();
        }


        for (int a = 0; a < line; a++) {

            somaProb = 0;
            somaSum = 0;
            for (int b = 1; b < nroClasses+1; b++) {
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

            indMaior = 1;
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

            if (classe == teste[a][coll - 1])
                acertos++;


        } // for-line


        return acertos/line;
    }



    public int[] projectConnection(int ind, int atr){
        // chama a projeção que deve ser feita na posição ind, que corresponde a uma matriz entre os atributos k atr e atr+1 ou atr-1 e atr
       return Edges[ind].getProjConnection(atr);
    }


    public void setVertexSet(GAvertices[] ve){  // recebe vetor de vertices - conjunto inteiro dos vertices
        Vertices = ve;
    }

    public GAvertices[] getVertexSet(){  // retorna o conjunto de vértices
        return Vertices;
    }

    public void setVerticesAttr(GAvertices ve, int atr, int parent){  // recebe vetor vertices de um atributo
        Vertices[atr] = ve;
        AttrParents[atr] = parent;
    }

    public GAvertices getVerticesAttr(int atr){   // retorna vetor de vértices de um atributo
        return Vertices[atr];
    }

    public void setEdges(GAedges[] ed){     // recebe conjunto completo de arestas
        Edges = ed;
    }

    public void setEdgesAttr(GAedges ge, int ind){     // recebe conjunto completo de arestas
        Edges[ind] = ge;
    }

    public GAedges getEdgesAttr(int ind){
        return Edges[ind];
    }

    public int getAttrParent(int ind){
        return AttrParents[ind];  // retorna pai do atributo ind. Setado em setVerticesAttr
    }

    public double getFitness(){
        return fit;
    }

    public double getNeta(){
        return neta;
    }

    private GAedges[] Edges;
    private GAvertices[] Vertices;
    private double fit, neta;
    private int nroClasses;
    private int[] AttrParents;   // armazena indice do pai que passou o atribuo
    private NetworkFull networksFull;

}
