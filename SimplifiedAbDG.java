/**
 * Created by João on 17/08/2016.
 */
import java.text.DecimalFormat;
import java.util.Random;


public class SimplifiedAbDG {


    public SimplifiedAbDG(double[][] matriz, char[] attrType, double[] classes){
        this.initialData = shuffle(matriz);
        this.attributeType = attrType;
        this.Classes = classes;
        this.nroClasses = classes.length;


        // buildFlowGraph(matriz);

    }



    public SimplifiedAbDG(double[][] matriz, double[] classes){   // usado em aprendizado incremental
        //  this.initialData = shuffle(matriz);
        this.Classes = classes;
        this.nroClasses = classes.length;



        buildSAbDG(matriz);

    }


    public SimplifiedAbDG(double[][] matriz, double[] classes, int granularidade){   // usado em aprendizado incremental
        //  this.initialData = shuffle(matriz);
        this.Classes = classes;
        this.nroClasses = classes.length;
        this.classifierAccAcumulado = 0;
        this.Weight = 0.0001;
        alpha = 0.3;// 999;
       // networksFull = new NetworkFull();


        if(granularidade != 0)
            buildVertexAbDG(matriz, granularidade);  //buildVertexMDLP(matriz);
        else {
       //     if(Math.random() > 0.0)
                buildVertexAleatorio(matriz);
       //     else
        //    buildVertexMDLP(matriz);
        }
    }



    public void buildSAbDG(double[][] matriz){  //  cria flw graph - considera atributos numéricos

        int coll = matriz[0].length;
        int grMax = 15;
        double[][] oneClassTrain;
        networks = new Networks[nroClasses];

        ordem = criaOrdemAleatoria(coll-1);                // define ordem aleatoriamente
        matriz = ordenaAtributosPorOrdem(matriz,ordem);  // ordena atributos de acordo com a ordem estabelecida

        // attributeType = ordenaAtributosPorOrdem(attributeType,ordem);

        // inicia construção do grafo
        particionaAtributoSAbDG(matriz);   // cria vetAtrHandler

        int granularidade;
        // if-else adicionado para diferenciar atributos numerico e categorico
        for(int a = 0; a < coll-1; a++){          //  #####  classe é atrHandles mas é particionada pelo numero de classe VERRR
            // atributos aleatórios e tamanho de partição aleatória
            granularidade = (int)(1 + (Math.random()*grMax));

          //  if(attributeType[a] == 'n')                                     // atributo real - numerico
                vetAtrHandler[a].histogramPartFlowGraph(granularidade);
          //  else
          //      vetAtrHandler[a].CategoricalFlowGraph();

            vetAtrHandler[a].SAbDGVertexWeight();   //  define pesos dos vértices


        }

        for(int i = 0; i < nroClasses; i++) {   // anda em classes

            oneClassTrain = criaTreinamento(matriz,Classes[i]);                 // #####  matrizToInput ##############
            networks[i] = new Networks(oneClassTrain, Classes[i], matriz.length);
            networks[i].learnSAbSDG(vetAtrHandler);   // estrutura do grafo sAbDG é semelhante a do flow graph
        }



    }


    public void buildVertexAbDG(double[][] matriz, int granularidade){  //  cria AbDG simplificado - só considera vertices

        int coll = matriz[0].length;
       // int grMax = 15;
        double[][] oneClassTrain;
       // networks = new Networks[nroClasses];

        // inicia construção do grafo
        particionaAtributoSAbDG(matriz);   // cria vetAtrHandler


        for(int a = 0; a < coll-1; a++){          //  #####  classe é atrHandles mas é particionada pelo numero de classe VERRR
            //  if(attributeType[a] == 'n')                                     // atributo real - numerico
            vetAtrHandler[a].histogramPartFlowGraph(granularidade);
            //  else
            //      vetAtrHandler[a].CategoricalFlowGraph();

            //vetAtrHandler[a].intervalWeightsIncLearn(); //SAbDGVertexWeight();   //  define pesos dos vértices
            vetAtrHandler[a].fastIntervalWeightsIncLearnPerClass();
           // vetAtrHandler[a].calculateIntervalGain();
        }

        // cria conexoes entre intervalos de atributos diferentes
         // para experimento com 3 classe  - anda em classes
            networksFull = new NetworkFull(matriz, nroClasses,vetAtrHandler);
            //networksFull.learnFullConection(vetAtrHandler);


    }
    public void buildVertexMDLP(double[][] matriz){  //  cria AbDG simplificado - só considera vertices

        int coll = matriz[0].length;
        double[][] oneClassTrain;
        isAttrRand = true;
        attrMask = new int[coll-1];
        int soma = 0;
        int granularidade;
        int grMax = 10;
        // networks = new Networks[nroClasses];
        // para dois atributos
        //  for(int i = 0; i < coll-1; i++)
        //     attrMask[(Math.random() > 0.5? 1:0)] = 1;

        for(int i = 0; i < coll-1; i++)
            attrMask[i] = (Math.random() > 0.5? 1:0);

        for(int i = 0; i < coll-1; i++)  // verificar se todos são iguais a 0
            soma += attrMask[i];

        if(soma == 0)           // garante que ao menos um atributo é usado
            for(int i = 0; i < coll-1; i++)
                attrMask[(Math.random() > 0.5? 1:0)] = 1;

        // inicia construção do grafo
        particionaAtributoSAbDG(matriz);   // cria vetAtrHandler


        for(int a = 0; a < coll-1; a++) {          //  #####  classe é atrHandles mas é particionada pelo numero de classe VERRR

            if (attrMask[a] == 1){
                granularidade = (int)(3 + (Math.random()*grMax));         //  if(attributeType[a] == 'n')                                     // atributo real - numerico

             //   if(granularidade > 6)
                vetAtrHandler[a].MDLP();
             //   else
               //     vetAtrHandler[a].EDADB(2);
                //vetAtrHandler[a].randomPart();   // intervalo aleatorio
                //  else
                //      vetAtrHandler[a].CategoricalFlowGraph();

                // vetAtrHandler[a].intervalWeightsIncLearn(); //SAbDGVertexWeight();   //  define pesos dos vértices

                vetAtrHandler[a].intervalWeightsIncLearnPerClass();
                //   vetAtrHandler[a].calculateIntervalGain();

            }
        }


    }

    public void buildVertexAleatorio(double[][] matriz){  //  cria AbDG simplificado - só considera vertices

        int coll = matriz[0].length;
        double[][] oneClassTrain;
        isAttrRand = true;
        attrMask = new int[coll-1];
        int soma = 0;
        int granularidade;
        int grMax = 5;
       // networks = new Networks[nroClasses];
       // para dois atributos
      //  for(int i = 0; i < coll-1; i++)
       //     attrMask[(Math.random() > 0.5? 1:0)] = 1;

        for(int i = 0; i < coll-1; i++)
            attrMask[i] = (Math.random() > 0.5? 1:0);

        for(int i = 0; i < coll-1; i++)  // verificar se todos são iguais a 0
            soma += attrMask[i];

        if(soma == 0)           // garante que ao menos um atributo é usado
            for(int i = 0; i < coll-1; i++)
              attrMask[(Math.random() > 0.5? 1:0)] = 1;

        // inicia construção do grafo
        particionaAtributoSAbDG(matriz);   // cria vetAtrHandler


        for(int a = 0; a < coll-1; a++) {          //  #####  classe é atrHandles mas é particionada pelo numero de classe VERRR

            if (attrMask[a] == 1){
                granularidade = (int)(3 + (Math.random()*grMax));         //  if(attributeType[a] == 'n')                                     // atributo real - numerico

                vetAtrHandler[a].histogramPartFlowGraph(granularidade);
                //vetAtrHandler[a].randomPart();   // intervalo aleatorio
            //  else
            //      vetAtrHandler[a].CategoricalFlowGraph();

               // vetAtrHandler[a].intervalWeightsIncLearn(); //SAbDGVertexWeight();   //  define pesos dos vértices

                vetAtrHandler[a].fastIntervalWeightsIncLearnPerClass();
               // vetAtrHandler[a].intervalWeightsIncLearnPerClass();


            }

        }

        networksFull = new NetworkFull(matriz, nroClasses, vetAtrHandler);

        // cria conexoes entre intervalos de atributos diferentes
    //    for(int i = 0; i < nroClasses; i++) {
      //      oneClassTrain = criaTreinamento(matriz,Classes[i]);
           // networksFull[i] = new NetworkFull(oneClassTrain, Classes[i], matriz.length);
         //   networksFull[i].learnFullConection(vetAtrHandler, attrMask, matriz.length);
           // networksFull[i].getCorrelation();

       // }

              //  geraRegras();
    }



    public void particionaAtributoSAbDG(double[][] matriz){

        int coll = matriz[0].length;
        double[][] MO;
        vetAtrHandler = new AttributeHandler[coll];  // classe tem o mesmo tratamento q atributo em Flow Graph

        for(int a = 0; a < coll-1; a++){

            vetAtrHandler[a] = new AttributeHandler(selecionaAtributoComClasse(matriz,a),Classes);

        }

       // vetAtrHandler[coll-1] = new AttributeHandler(selecionaAtributo(matriz,coll - 1), true);
        // System.out.println();
    }

    public double[] selecionaAtributo(double[][] A, int a){

        int line = A.length;
        int coll = A[0].length;

        double[] atributo = new double[line];

        for(int i = 0; i < line; i++){
            atributo[i] = A[i][a];

        }

        return atributo;

    }

    public double[][] selecionaAtributoComClasse(double[][] A, int a){

        int line = A.length;
        int coll = A[0].length;

        double[][] atributo = new double[line][2];

        for(int i = 0; i < line; i++){
            atributo[i][0] = A[i][a];
            atributo[i][1] = A[i][coll-1];

        }

        return atributo;

    }

    public void batchUpdate(double[][] matriz){  // não considera peso de acuracia
        int coll = matriz[0].length;

        if(!isAttrRand)
        for(int a = 0; a < coll - 1; a++)
           vetAtrHandler[a].updateIntervalWeightsIncLearn(selecionaAtributoComClasse(matriz,a));

        else{
            for(int a = 0; a < coll - 1; a++)
                if(attrMask[a] == 1)
                    vetAtrHandler[a].updateIntervalWeightsIncLearn(selecionaAtributoComClasse(matriz,a));
        }

    }


    public void batchUpdateIntConf(double[][] matriz){  // Short memory
        int coll = matriz[0].length;


        if(!isAttrRand)
            for(int a = 0; a < coll - 1; a++)
                vetAtrHandler[a].updateIntervalWeightsIncLearnWeightedIntervalsLastBatchPerClass(selecionaAtributoComClasse(matriz,a),predLabels);

        else{
            for(int a = 0; a < coll - 1; a++)
                if(attrMask[a] == 1)
                    vetAtrHandler[a].updateIntervalWeightsIncLearnWeightedIntervalsLastBatchPerClass(selecionaAtributoComClasse(matriz,a),predLabels);
        }


    }


    public void updateNeverForget(double[][] matriz){ // Never Forgetting
        int coll = matriz[0].length;

        for(int a = 0; a < coll - 1; a++)
            vetAtrHandler[a].updateIntervalWeightsIncLearnWeightedIntervals(selecionaAtributoComClasse(matriz,a),predLabels);

    }


    public void updateFadingFactor(double[][] matriz){
        int coll = matriz[0].length;
        int line = matriz.length;
        int[] somaClassIL = new int[nroClasses+1];
        double[][] oneClassTrain;
        alpha *= 1;//0.9; // taxa de decremento de alpha

        for(int j = 0; j < line; j++)             // P(Ci)      -   porcentagem de elementos da classe i no conjunto de treinamento
            somaClassIL[(int)matriz[j][coll-1]]++;

        if(!isAttrRand) {
            for (int a = 0; a < coll - 1; a++)
                vetAtrHandler[a].fastUpdateIntervalWeightsIncLearnWeightedIntervalsFadingFactorOnWeights(selecionaAtributoComClasse(matriz, a), predLabels, alpha);

                networksFull.updateFullConection(matriz, alpha);


        }
        else{                                           //fastUpdateIntervalWeightsIncLearnWeightedIntervalsFadingFactor
            for(int a = 0; a < coll - 1; a++)
                if(attrMask[a] == 1)
                    vetAtrHandler[a].fastUpdateIntervalWeightsIncLearnWeightedIntervalsFadingFactorOnWeights(selecionaAtributoComClasse(matriz,a),predLabels, alpha);

                 networksFull.updateFullConection(matriz,attrMask,alpha);


        }

  }



    public double[][] ordenaAtributosPorOrdem(double[][] matriz, int[] ordem){
        // recebe matriz e ordem de troca de colunas
        int line = matriz.length;
        int coll = matriz[0].length;
        double[][] newMatriz = new double[line][coll];

        //   AttributeHandler auxVetHandler;     // para ordenar vetor

        for(int b = 0; b < coll - 1; b++){
            if(ordem[b] != b)
                for(int i = 0; i < line; i++)
                    newMatriz[i][b] = matriz[i][ordem[b]];
        }

        for(int i = 0; i < line; i++)
            newMatriz[i][coll-1] = matriz[i][coll-1];


        return newMatriz;
    }

    public char[] ordenaAtributosPorOrdem(char[] matriz, int[] ordem){
        // recebe matriz e ordem de troca de colunas
        int coll = matriz.length;
        char[] newMatriz = new char[coll];

        //   AttributeHandler auxVetHandler;     // para ordenar vetor

        for(int b = 0; b < coll - 1; b++){
            if(ordem[b] != b)
                newMatriz[b] = matriz[ordem[b]];
        }

        newMatriz[coll-1] = matriz[coll-1];


        return newMatriz;
    }

    public int[] criaOrdemAleatoria(int numAttr){


        int aux = 0, cont = 0, aux1 = 0;
        double pChange = 0.9, prob = 0;
        Random randomNumbers = new Random();

        int[] ordem = new int[numAttr];

        for(int h = 0; h < numAttr; h++)
            ordem[h] = h;

        for(int i = 0; i < numAttr; i++){
            prob = Math.random();
            if(prob < pChange){
                aux = randomNumbers.nextInt(numAttr-1);
                aux1 = ordem[i];
                ordem[i] = ordem[aux];
                ordem[aux] = aux1;
            }
        }

        return ordem;
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

    public void crossValidation(){

        double[][] matriz = initialData;
        int line = matriz.length;
        int coll = matriz[0].length;
        double[][] matrizTreino;
        double[][] matrizTeste;
        int fold = 10;
        int contTreino = 0, contTeste = 0, it = 1;
        double[] mainClassification;
        int grMax = 15; // granularidade maxima de intervalo
        double[][] oneClassTrain;
        double acertos = 0, media = 0;



        DecimalFormat show = new DecimalFormat("0.00");

        it = 1;
        matriz = shuffle(matriz);
        int[] cv = indicesCV(line,fold);

        while(it <= fold){

            networks = new Networks[nroClasses];

            coll = matriz[0].length;
            contTreino = 0;
            contTeste = 0;
            for(int i = 0; i < line; i++)
                if(cv[i] == it)
                    contTeste++;

            matrizTreino = new double[line - contTeste][coll];
            matrizTeste = new double[contTeste][coll];

            contTeste = 0;
            contTreino = 0;
            for(int h = 0; h < line; h++)
                if(cv[h] != it){
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


            int[] ordem;
            ordem = criaOrdemAleatoria(coll-1);                // define ordem aleatoriamente
            matrizTreino = ordenaAtributosPorOrdem(matrizTreino,ordem);  // ordena atributos de acordo com a ordem estabelecida
            matrizTeste = ordenaAtributosPorOrdem(matrizTeste,ordem);
            attributeType = ordenaAtributosPorOrdem(attributeType,ordem);
            // inicia construção do grafo

            particionaAtributoSAbDG(matrizTreino);   // cria vetAtrHandler

            int granularidade;
            // if-else adicionado para diferenciar atributos numerico e categorico
            for(int a = 0; a < coll-1; a++){          //  #####  classe é atrHandles mas é particionada pelo numero de classe VERRR
                // atributos aleatórios e tamanho de partição aleatória
                granularidade = (int)(1 + (Math.random()*grMax));

                //  if(attributeType[a] == 'n')                                     // atributo real - numerico
                vetAtrHandler[a].histogramPartFlowGraph(granularidade);
                //  else
                //      vetAtrHandler[a].CategoricalFlowGraph();

                vetAtrHandler[a].SAbDGVertexWeight();   //  define pesos dos vértices


            }

            for(int i = 0; i < nroClasses; i++) {   // anda em classes

                oneClassTrain = criaTreinamento(matrizTreino,Classes[i]);                 // #####  matrizToInput ##############
                networks[i] = new Networks(oneClassTrain, Classes[i], matrizTreino.length);
                networks[i].learnSAbSDG(vetAtrHandler);   // estrutura do grafo sAbDG é semelhante a do flow graph
            }


            //    mainClassification = DnoClassifier(matrizTeste); // - varios classificadores para teste
            mainClassification = sAbDGClassifier(matrizTeste); // - varios classificadores para teste
            acertos = 0;
            for(int i = 0; i < mainClassification.length; i++){
                if(mainClassification[i] == matrizTeste[i][coll-1])
                    acertos++;

            }

            media += (acertos/mainClassification.length);
            System.out.println(acertos/mainClassification.length);
            networks = null;
            vetAtrHandler = null;


        } // fim do while it < 10        -- cross validation --

       System.out.println((media/fold)*100);


    }

    public double[][] sAbDGVertexClassifier(double[][] matriz){   // classificador que só considera vertices

        int line = matriz.length;
        int coll = matriz[0].length;
        double maior, acertos = 0;
        int indMaior;
        double[][] privateProbVector = new double[line][nroClasses+1];
        double[][] labels = new double[line][2];          // classe e prob de pert. a classe
        predLabels = new double[line];
        errorModel = new double[line];
        oracle = new int[line];
        numTe = line;
        classifierAcertos = 0;
        double soma, aux = 0;

        for(int z = 0; z < line; z++)
           for(int y = 0; y < nroClasses + 1; y++)
            privateProbVector[z][y] = 0;  // 1 para prod. de prob

                    // TENTAR SOMA DOS LOGS add their logs and then taking exp of the sum
        for(int i = 0; i < line; i++) {
            for (int classe = 1; classe < nroClasses + 1; classe++) {
                if(!isAttrRand)
                for (int j = 0; j < coll - 1; j++) {
                    // soma dos logs
                   // aux = vetAtrHandler[j].getWeightedInterval(matriz[i][j], classe);
                   // if(aux != 0)
                       privateProbVector[i][classe] += Math.log(vetAtrHandler[j].getWeightedInterval(matriz[i][j], classe));
                   //  privateProbVector[i][classe] += vetAtrHandler[j].getWeightedInterval(matriz[i][j], classe);
                    //privateProbVector[i][classe] *= vetAtrHandler[j].getWeight(matriz[i][j], classe); // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
                }
                else
                    for (int j = 0; j < coll - 1; j++) {
                       if(attrMask[j] == 1)
                           privateProbVector[i][classe] += Math.log(vetAtrHandler[j].getWeightedInterval(matriz[i][j], classe));
                        //  privateProbVector[i][classe] += vetAtrHandler[j].getWeightedInterval(matriz[i][j], classe);
                        //privateProbVector[i][classe] *= vetAtrHandler[j].getWeight(matriz[i][j], classe); // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
                    }

               // if(privateProbVector[i][classe] != 0)
                   privateProbVector[i][classe] = Math.exp(privateProbVector[i][classe]);

            }
        }


     //  Math.exp(-1*privateProbVector[i][b])

        for(int j = 0; j < line; j++) {
            soma = 0;
            for (int classe = 1; classe < nroClasses + 1; classe++)
                soma += privateProbVector[j][classe];

            for (int classe = 1; classe < nroClasses + 1; classe++)
                privateProbVector[j][classe] /= soma;
        }


        for(int i = 0; i < line; i++){
            indMaior = 0;
            maior = 0;

            for(int b = 1; b < nroClasses + 1; b++)
                if(maior < privateProbVector[i][b]) {
                    maior = privateProbVector[i][b];
                    indMaior = b;
                }

            labels[i][0] = indMaior;
            labels[i][1] = privateProbVector[i][indMaior];
            predLabels[i] = labels[i][0];

            // estatistica de acerto do classificador parte do comite
               if(labels[i][0] == matriz[i][coll-1]) {
                   classifierAcertos++;
                   errorModel[i] = 0; // erro armazenados para calculo do erro boosting - updateClfWeigh
                   oracle[i] = 1;
                }
                else {
                   errorModel[i] = 1;
                   oracle[i] = 0;
               }

        //    if(labels[i] == 0)
        //        System.out.println("Zero");

        } // for-line

        // classifierAccAcumulado = classifierAcertos/numTe;
        //  System.out.println(acertos/line);

        return labels; // privateProbVector;

    }

    public double[][] RulesAbDGVertexClassifier(double[][] matriz){   // classificador que só considera vertices

        int line = matriz.length;
        int coll = matriz[0].length;
        double maior, acertos = 0;
        int indMaior;
        double[][] privateProbVector = new double[line][nroClasses+1];
        double[][] ruleVector = new double[line][coll-1];  // line x nroAtr
        double[][] labels = new double[line][2];          // classe e prob de pert. a classe
        predLabels = new double[line];
        errorModel = new double[line];
        oracle = new int[line];
        numTe = line;
        classifierAcertos = 0;
        double soma, somaAnt = 0;
        int[] somaClassIL = new int[nroClasses+1];

        for(int z = 0; z < line; z++)
            for(int y = 0; y < nroClasses + 1; y++)
                privateProbVector[z][y] = 0;  // 1 para prod. de prob

        // para contar num. de el. em cada classe
        for(int j = 0; j < line; j++)             // P(Ci)      -   porcentagem de elementos da classe i no conjunto de treinamento
            somaClassIL[(int)matriz[j][coll-1]]++;


        // TENTAR SOMA DOS LOGS add their logs and then taking exp of the sum
        for(int i = 0; i < line; i++) {
            if(!isAttrRand)
                for (int j = 0; j < coll - 1; j++) {
                    if(vetAtrHandler[j].getCoverageInterval(matriz[i][j]) < 1)
                        ruleVector[i][j] = 1 - vetAtrHandler[j].getEntropyInterval(matriz[i][j]) +  vetAtrHandler[j].getCoverageInterval(matriz[i][j]) + vetAtrHandler[j].getAccInterval(matriz[i][j]);
                }
            else
                for (int j = 0; j < coll - 1; j++) {
                    if(attrMask[j] == 1)
                        if(vetAtrHandler[j].getCoverageInterval(matriz[i][j]) < 1)
                            ruleVector[i][j] = 1 - vetAtrHandler[j].getEntropyInterval(matriz[i][j]) + vetAtrHandler[j].getCoverageInterval(matriz[i][j]) + vetAtrHandler[j].getAccInterval(matriz[i][j]);
                }

        }


        // TENTAR SOMA DOS LOGS add their logs and then taking exp of the sum
        if (!isAttrRand) {
            for (int i = 0; i < line; i++) {
                soma = 0;
                somaAnt = 0;
                while (soma < 2.0) {
                    System.out.println(soma);
                    indMaior = 0;
                    maior = ruleVector[i][0];
                    for (int j = 1; j < coll - 1; j++) {
                        if (ruleVector[i][j] != -2 && ruleVector[i][j] > maior) {
                            indMaior = j;
                            maior = ruleVector[i][j];
                        }
                    }
                    somaAnt = soma;
                    soma += ruleVector[i][indMaior];

                    if (ruleVector[i][indMaior] > 0.2 /*soma > somaAnt*/)
                        for (int classe = 1; classe < nroClasses + 1; classe++)
                            privateProbVector[i][classe] += Math.log(vetAtrHandler[indMaior].getWeightedInterval(matriz[i][indMaior], classe));
                    else
                       soma = 2;

                    ruleVector[i][indMaior] = -2;

                }
            }
        }
        else //
        {
            for (int i = 0; i < line; i++) {
                soma = 0;
                somaAnt = 0;
                while (soma < 2.0) {    //  soma < 2.5 || ...
                    indMaior = -1;
                    maior = -10;
                    for (int j = 0; j < coll - 1; j++)
                        if(attrMask[j] == 1) {
                            if (ruleVector[i][j] != -2 && ruleVector[i][j] > maior) {
                                indMaior = j;
                                maior = ruleVector[i][j];
                            }
                        }

                    if(indMaior != -1) {
                        somaAnt = soma;
                        soma += ruleVector[i][indMaior];

                        if (ruleVector[i][indMaior] > 0.2 /*soma > somaAnt*/) {
                            for (int classe = 1; classe < nroClasses + 1; classe++)
                                privateProbVector[i][classe] += Math.log(vetAtrHandler[indMaior].getWeightedInterval(matriz[i][indMaior], classe));
                        } else {
                            //  j = coll; // para sair do for j
                            soma = 2.0;
                        }

                        ruleVector[i][indMaior] = -2;
                    }
                    else{
                       soma = 2.0;
                    }
                }

            }

        }


        for(int i = 0; i < line; i++){
            for (int classe = 1; classe < nroClasses + 1; classe++)
                privateProbVector[i][classe] = Math.exp(privateProbVector[i][classe]);
        }


        //  Math.exp(-1*privateProbVector[i][b])

        for(int j = 0; j < line; j++) {
            soma = 0;
            for (int classe = 1; classe < nroClasses + 1; classe++)
                soma += privateProbVector[j][classe];

            for (int classe = 1; classe < nroClasses + 1; classe++)
                privateProbVector[j][classe] /= soma;
        }


        for(int i = 0; i < line; i++){
            indMaior = 0;
            maior = 0;

            for(int b = 1; b < nroClasses + 1; b++)
                if(maior < privateProbVector[i][b]) {
                    maior = privateProbVector[i][b];
                    indMaior = b;
                }

            labels[i][0] = indMaior;
            labels[i][1] = privateProbVector[i][indMaior];
            predLabels[i] = labels[i][0];

            // estatistica de acerto do classificador parte do comite
            if(labels[i][0] == matriz[i][coll-1]) {
                classifierAcertos++;
                errorModel[i] = 0; // erro armazenados para calculo do erro boosting - updateClfWeigh
                oracle[i] = 1;
            }
            else {
                errorModel[i] = 1;
                oracle[i] = 0;
            }



            //    if(labels[i] == 0)
            //        System.out.println("Zero");

        } // for-line

        // classifierAccAcumulado = classifierAcertos/numTe;
        //  System.out.println(acertos/line);

        return labels; // privateProbVector;

    }



     public double[][] sinlgeRulesAbDGVertexClassifier(double[][] matriz){
        // retorna regra e prob para a regra de maior valor de ruleVector

        int line = matriz.length;
        int coll = matriz[0].length;
        double maior, acertos = 0;
        int indMaior;
        privateProbVector = new double[line][nroClasses+1];  // posição 0 usada para a força da regra
        double[][] ruleVector = new double[line][coll-1];  // line x nroAtr
        double[][] labels = new double[line][2];          // classe e prob de pert. a classe
        predLabels = new double[line];
        errorModel = new double[line];
        oracle = new int[line];
        numTe = line;
        classifierAcertos = 0;
        double soma, somaAnt = 0;
        int[] somaClassIL = new int[nroClasses+1];

        for(int z = 0; z < line; z++)
            for(int y = 0; y < nroClasses + 1; y++)
                privateProbVector[z][y] = 0;  // 1 para prod. de prob

        // para contar num. de el. em cada classe
        for(int j = 0; j < line; j++)             // P(Ci)      -   porcentagem de elementos da classe i no conjunto de treinamento
            somaClassIL[(int)matriz[j][coll-1]]++;


        // TENTAR SOMA DOS LOGS add their logs and then taking exp of the sum
        for(int i = 0; i < line; i++) {
            if(!isAttrRand)
                for (int j = 0; j < coll - 1; j++) {
                    if(vetAtrHandler[j].getCoverageInterval(matriz[i][j]) < 1)
                        ruleVector[i][j] = 1 - vetAtrHandler[j].getEntropyInterval(matriz[i][j]) +  vetAtrHandler[j].getCoverageInterval(matriz[i][j]) + vetAtrHandler[j].getAccInterval(matriz[i][j]);
                }
            else
                for (int j = 0; j < coll - 1; j++) {
                    if(attrMask[j] == 1)
                        if(vetAtrHandler[j].getCoverageInterval(matriz[i][j]) < 1)
                            ruleVector[i][j] = 1 - vetAtrHandler[j].getEntropyInterval(matriz[i][j]) + vetAtrHandler[j].getCoverageInterval(matriz[i][j]) + vetAtrHandler[j].getAccInterval(matriz[i][j]);
                }

        }


        // TENTAR SOMA DOS LOGS add their logs and then taking exp of the sum
        if (!isAttrRand) {
            for (int i = 0; i < line; i++) {
                soma = 0;
                somaAnt = 0;
                while (soma < 2.0) {
                  //  System.out.println(soma);
                    indMaior = 0;
                    maior = ruleVector[i][0];
                    for (int j = 1; j < coll - 1; j++) {
                        if (ruleVector[i][j] != -2 && ruleVector[i][j] > maior) {
                            indMaior = j;
                            maior = ruleVector[i][j];
                        }
                    }
                    somaAnt = soma;
                    soma += ruleVector[i][indMaior];

                    if (ruleVector[i][indMaior] > 0.2 /*soma > somaAnt*/) {
                        privateProbVector[i][0] += ruleVector[i][indMaior];    // força da regra
                        for (int classe = 1; classe < nroClasses + 1; classe++)
                            privateProbVector[i][classe] += Math.log(vetAtrHandler[indMaior].getWeightedInterval(matriz[i][indMaior], classe));
                    }
                    else
                        soma = 2;

                    ruleVector[i][indMaior] = -2;

                }
            }
        }
        else //
        {
            for (int i = 0; i < line; i++) {
                soma = 0;
                somaAnt = 0;
                while (soma < 2.0) {    //  soma < 2.5 || ...
                    indMaior = -1;
                    maior = -10;
                    for (int j = 0; j < coll - 1; j++)
                        if(attrMask[j] == 1) {
                            if (ruleVector[i][j] != -2 && ruleVector[i][j] > maior) {
                                indMaior = j;
                                maior = ruleVector[i][j];
                            }
                        }

                    if(indMaior != -1) {
                        somaAnt = soma;
                        soma += ruleVector[i][indMaior];

                        if (ruleVector[i][indMaior] > 0.2 /*soma > somaAnt*/) {
                            privateProbVector[i][0] += ruleVector[i][indMaior];    // força da regra
                            for (int classe = 1; classe < nroClasses + 1; classe++)
                                privateProbVector[i][classe] += Math.log(vetAtrHandler[indMaior].getWeightedInterval(matriz[i][indMaior], classe));
                        } else {
                            //  j = coll; // para sair do for j
                            soma = 2.0;
                        }

                        ruleVector[i][indMaior] = -2;
                    }
                    else{
                        soma = 2.0;
                    }
                }

            }

        }


        for(int i = 0; i < line; i++){
            for (int classe = 1; classe < nroClasses + 1; classe++)
                privateProbVector[i][classe] = Math.exp(privateProbVector[i][classe]);
        }


        //  Math.exp(-1*privateProbVector[i][b])
/*
        for(int j = 0; j < line; j++) {
            soma = 0;
            for (int classe = 1; classe < nroClasses + 1; classe++)
                soma += privateProbVector[j][classe];

            for (int classe = 1; classe < nroClasses + 1; classe++)
                privateProbVector[j][classe] /= soma;
        }
*/

        for(int i = 0; i < line; i++){
            indMaior = 0;
            maior = 0;

            for(int b = 1; b < nroClasses + 1; b++)
                if(maior < privateProbVector[i][b]) {
                    maior = privateProbVector[i][b];
                    indMaior = b;
                }

            labels[i][0] = indMaior;
            labels[i][1] = privateProbVector[i][indMaior];
            predLabels[i] = labels[i][0];

            // estatistica de acerto do classificador parte do comite
            if(labels[i][0] == matriz[i][coll-1]) {
                classifierAcertos++;
                errorModel[i] = 0; // erro armazenados para calculo do erro boosting - updateClfWeigh
                oracle[i] = 1;
            }
            else {
                errorModel[i] = 1;
                oracle[i] = 0;
            }



            //    if(labels[i] == 0)
            //        System.out.println("Zero");

        } // for-line

        // classifierAccAcumulado = classifierAcertos/numTe;
        //  System.out.println(acertos/line);


        return privateProbVector;

    }


    public double[][] RuleClassifierFull(double[][] matriz, double neta){   // classificadores para testes
        // classificador que recebe parametro ja escolhino na fase de sele��o de modelos

        int line = matriz.length;
        int coll = matriz[0].length;
        double somaCorretos = 0, maior, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0, somaCorretos3 = 0;
        double somaProb = 0, somaSum = 0;
        int indMaior;
        double classe1 = 0, classe2 = 0,  aux = 0;
        double classifications = 0;            // rotulos atribuidos     //   double[] classifications2 = new double[line];
        predLabels = new double[line];
        double soma = 0;
        double[][] labels = new double[line][2];

        if(isAttrRand)
            networksFull.criaProbClassWeighted(matriz,attrMask);       // Weighted(matriz,attrGain);                // ############### classificador 3
        else
            networksFull.criaProbClassWeightedSA(matriz);       // Weighted(matriz,attrGain);                // ############### classificador 3


        for(int a = 0; a < line; a++){


// codigo classificador


        } // for-line

        classifierAcertos = classifications/line;


        return labels;

    }


    public double[] sAbDGClassifier(double[][] matriz){   // classificador

        int line = matriz.length;
        int coll = matriz[0].length;
        double maior, acertos = 0;
        int indMaior;
        double[] classifications = new double[11];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
        double[] cerToLastAttr;
        double[] vetProbClass = new double[nroClasses];
        double[] labels = new double[line];          // para debug

   //    matriz = ordenaAtributosPorOrdem(matriz,ordem);  // cada SAbDG tem uma ordem específica de atributos

        for(int b = 0; b < nroClasses; b++){
            networks[b].criaProbClassSAbDGlog(matriz);       // Weighted(matriz,attrGain);                // ############### classificador 3
            //       vetProbClass[b] = cerToLastAttr[a] + networks.criaProbClassFGlog(matriz[a], Classes[b]);
        }

        for(int a = 0; a < line; a++){

            maior = networks[0].getProb(a);
            indMaior = 0;

            for(int b = 1; b < nroClasses; b++)
                if(maior < networks[b].getProb(a)) {
                    maior = networks[b].getProb(a);
                    indMaior = b;
                }

             labels[a] = Classes[indMaior];


            //   if(labelAndProb[a][0] == matriz[a][coll-1]) {
            //        acertos++;
            //        labelAndProb[a][2] = 1;
            //}
        } // for-line

        //  System.out.println(acertos/line);

        return labels;

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

    public int[] indicesCV(int line, int fold){

        int[] vetCV = new int[line];
        int cont = 1;
        for(int j = 0; j < line; j++) {
            vetCV[j] = cont;
            cont++;
            if(cont > fold)
                cont = 1;
        }

        return vetCV;
    }

    public void updateClfWeight(double[] weights){

       double erroMBoost = 0;

       for(int i = 0; i < weights.length; i++)
          erroMBoost += weights[i] * errorModel[i];

        Weight = erroMBoost/(1-erroMBoost); //Math.log((1-erroMBoost)/erroMBoost);

    }

    public void updateAccClassifier(){
        // atualiza peso de classificador. Não pode ser atualizado no método classificador pq teoricamente o label não é conhecido

        classifierAccAcumulado += classifierAcertos/numTe;
    }

    public double[][] DnoClassifierFull(double[][] matriz, double neta){   // classificadores para testes
        // classificador que recebe parametro ja escolhino na fase de sele��o de modelos

        int line = matriz.length;
        int coll = matriz[0].length;
        double somaCorretos = 0, maior, classe = 0, somaCorretos1 =  0, somaCorretos2 = 0, somaCorretos3 = 0;
        double somaProb = 0, somaSum = 0;
        int indMaior;
        double classe1 = 0, classe2 = 0,  aux = 0;
        double classifications = 0;            // rotulos atribuidos     //   double[] classifications2 = new double[line];
        predLabels = new double[line];
        double soma = 0;
        double[][] labels = new double[line][2];

        if(isAttrRand)
           //for(int b = 0; b < nroClasses; b++){                                  // vetAttrCorr
                networksFull.criaProbClassWeighted(matriz,attrMask);       // Weighted(matriz,attrGain);                // ############### classificador 3
            //    vetAux = networks[b].getVetCorrelation();
        //     }
         else
          //  for(int b = 0; b < nroClasses; b++){                                  // vetAttrCorr
                networksFull.criaProbClassWeightedSA(matriz);       // Weighted(matriz,attrGain);                // ############### classificador 3
                //    vetAux = networks[b].getVetCorrelation();
          //  }



        for(int a = 0; a < line; a++){


            somaProb = 0;
            somaSum = 0;
            for(int b = 0; b < nroClasses; b++){
                somaProb += networksFull.getProb(a,b);  // a - indice x, b - classe
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
            labels[a][0] = classe2;
            labels[a][1] = maior; // prob do maior
            predLabels[a] = classe2;

            if(classe2 == matriz[a][coll-1])
                classifications++;


        } // for-line

        classifierAcertos = classifications/line;


        return labels;

    }




    public void setWeight(double W){
        Weight = W;
    }

    public double getWeight(){
        return Weight;
    }


    public void calculateLastAcc(){
        lastAcc = classifierAcertos;
    }

    public double getClassAcc(){
       // double beta = 0.3;
       // Weight = (1-beta)* Weight + beta*classifierAccAcumulado;
        //return classifierAccAcumulado; // Weight;

        return lastAcc;
    }

    public double[] getPredLabels(){
        return predLabels;
    }

    public int[] getOracle(){
        return oracle;
    }

    public void resetAlpha(){
        alpha = 0.999;
    }

    double[] Classes;
    Networks[] networks;
    private AttributeHandler[] vetAtrHandler;
    NetworkFull networksFull;
    private int nroClasses;
    private char[] attributeType;
    private double[][] initialData;
    private int[] ordem; // para possibilitar atualização incremental
    private double Weight, alpha;

    // incremental learning
    private int classifierAgeUpdate;
    private double classifierAccAcumulado = 0;
    private double classifierAcertos = 0;
    private double numTe = 0, lastAcc = 1;
    private double[][] privateProbVector;
    private int[] oracle;
    private double[] predLabels, errorModel;
    boolean isAttrRand = false;
    private int[] attrMask;


}
