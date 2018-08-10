import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by João on 05/08/2016.
 */
public class FlowGraph {


    public FlowGraph(double[][] matriz, char[] attrType, double[] classes){
        this.initialData = shuffle(matriz);
        this.attributeType = attrType;
        this.Classes = classes;
        this.nroClasses = classes.length;


       // buildFlowGraph(matriz);

    }



    public FlowGraph(double[][] matriz, double[] classes){   // usado em aprendizado incremental
      //  this.initialData = shuffle(matriz);
        this.Classes = classes;
        this.nroClasses = classes.length;

        buildFlowGraph(matriz);
    }


    public void buildFlowGraph(double[][] matriz){  //  cria flw graph - considera atributos numéricos

        int coll = matriz[0].length;
        int grMax = 15;

        ordem = criaOrdemAleatoria(coll-1);                // define ordem aleatoriamente
        matriz = ordenaAtributosPorOrdem(matriz,ordem);  // ordena atributos de acordo com a ordem estabelecida
      //  matrizTeste = ordenaAtributosPorOrdem(matrizTeste,ordem);
       // attributeType = ordenaAtributosPorOrdem(attributeType,ordem);
        // inicia construção do grafo
        particionaAtributoFlowGraph(matriz);   // cria vetAtrHandler

        int granularidade;
        // if-else adicionado para diferenciar atributos numerico e categorico
        for(int a = 0; a < coll; a++){          //  #####  classe é atrHandles mas é particionada pelo numero de classe VERRR
            // atributos aleatórios e tamanho de partição aleatória
            granularidade = (int)(1 + (Math.random()*grMax));

           if(a < coll - 1 )// if(attributeType[a] == 'n')                                     // atributo real - numerico
                vetAtrHandler[a].histogramPartFlowGraph(granularidade);
           else
                vetAtrHandler[a].CategoricalFlowGraph();

            vetAtrHandler[a].flowGraphVertexWeight();   //  define pesos dos vértices


        }

        networks = new Networks(matriz,true);  // cria flow graph, representado como objeto de Networks
        networks.learnFlowGraph(vetAtrHandler);


    }


 /*   public void updateFlowGraph(double[][] matriz){  //  atualiza usando contador de valores reais para numero de individuos em vertices e arestas
                                                        // outra ideia - usar esquema do prequential para atualizar pesos

        int coll = matriz[0].length;
        matriz = ordenaAtributosPorOrdem(matriz,ordem);

        // if-else adicionado para diferenciar atributos numerico e categorico
        for(int a = 0; a < coll; a++){          //  #####  classe é atrHandles mas é particionada pelo numero de classe VERRR

            vetAtrHandler[a].flowGraphVertexWeight(selecionaAtributo(matriz,a));   //  define pesos dos vértices

        }

        //networks = new Networks(matriz,true);  // cria flow graph, representado como objeto de Networks
        networks.updateFlowGraph(matriz);


    }
*/

    public void particionaAtributoFlowGraph(double[][] matriz){

        int coll = matriz[0].length;
        double[][] MO;
        vetAtrHandler = new AttributeHandler[coll];  // classe tem o mesmo tratamento q atributo em Flow Graph

        for(int a = 0; a < coll-1; a++){

            vetAtrHandler[a] = new AttributeHandler(selecionaAtributo(matriz,a), false);

        }

        vetAtrHandler[coll-1] = new AttributeHandler(selecionaAtributo(matriz,coll - 1), true);
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
        double[][] mainClassification;
        int grMax = 15; // granularidade maxima de intervalo


        DecimalFormat show = new DecimalFormat("0.00");

            it = 1;
            matriz = shuffle(matriz);
            int[] cv = indicesCV(line,fold);

            while(it <= fold){


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
                 particionaAtributoFlowGraph(matrizTreino);   // cria vetAtrHandler

                int contAtrZero = 0;
                int granularidade;
                // if-else adicionado para diferenciar atributos numerico e categorico
                for(int a = 0; a < coll; a++){          //  #####  classe é atrHandles mas é particionada pelo numero de classe VERRR
                    // atributos aleatórios e tamanho de partição aleatória
                    granularidade = (int)(1 + (Math.random()*grMax));
                    System.out.print(granularidade + " ");

                    if(attributeType[a] == 'n')         // atributo real - numerico
                        vetAtrHandler[a].histogramPartFlowGraph(granularidade);
                    else
                        vetAtrHandler[a].CategoricalFlowGraph();

                    vetAtrHandler[a].flowGraphVertexWeight();   //  define pesos dos vértices


                }

                networks = new Networks(matrizTreino,true);  // cria flow graph, representado como objeto de Networks
                networks.learnFlowGraph(vetAtrHandler);


                   //    mainClassification = DnoClassifier(matrizTeste); // - varios classificadores para teste
               mainClassification =  flowGraphClassifierTest(matrizTeste); // - varios classificadores para teste

                for(int i = 0; i < mainClassification.length; i++){
                    System.out.println(mainClassification[i][0] + " " + mainClassification[i][1]  + " " + mainClassification[i][2]);

                }

                networks = null;
                vetAtrHandler = null;


            } // fim do while it < 10        -- cross validation --



 /*
        for(int i = 0; i < desvio1.length; i++){
            desvio1[i] = Math.sqrt((somaQuadClassification1[i] - (Math.pow(somaClassification1[i],2)/100))/99);
            mediaClass1[i] = somaClassification1[i]/100;
            System.out.println(show.format(i*0.1) + "  " + show.format(mediaClass1[i]) + "   " + show.format(desvio1[i]));
        }
*/
        // System.out.println(mediaClass + "  " + desvio);// + "  " + mediaCenter + "  " + desvioCenter + "  " +  newMediaClass + "  " + newDesvio);


    }

    public double[][] flowGraphClassifierTest(double[][] matriz){   // classificador flow graph

        int line = matriz.length;
        int coll = matriz[0].length;
        double maior, acertos = 0;
        int indMaior;
        double[] classifications = new double[11];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
        double[] cerToLastAttr;
        double[] vetProbClass = new double[nroClasses];
        double[][] labelAndProb = new double[line][3];          // para debug

        cerToLastAttr = networks.criaProbSemClasseFG(matriz);  // calcula produtorio até o ultimo atributo, para todos os elementos de matriz
      //  cerToLastAttr = networks.criaProbSemClasseFGlog(matriz);

        for(int a = 0; a < line; a++){

            for(int b = 0; b < nroClasses; b++)
                vetProbClass[b] = 0;

            for(int b = 0; b < nroClasses; b++){
                vetProbClass[b] = cerToLastAttr[a] * networks.criaProbClassFG(matriz[a], Classes[b]);       // Weighted(matriz,attrGain);                // ############### classificador 3
              //  vetProbClass[b] = cerToLastAttr[a] + networks.criaProbClassFGlog(matriz[a], Classes[b]);
            }

            // normalizar vetProbClass ?

           maior = vetProbClass[0];
           indMaior = 0;
           for(int c = 1; c < nroClasses; c++){
               if(vetProbClass[c] > maior){
                   maior = vetProbClass[c];
                   indMaior = c;
                    }
                }

            labelAndProb[a][0] = Classes[indMaior];
            labelAndProb[a][1] = maior;///((coll*2-1));//*Math.log(1000));


            if(labelAndProb[a][0] == matriz[a][coll-1]) {
                acertos++;
                labelAndProb[a][2] = 1;
            }
        } // for-line

      //  System.out.println(acertos/line);

        return labelAndProb;

    }

    public double[] flowGraphClassifier(double[][] matriz){   // classificador flow graph

        int line = matriz.length;
        int coll = matriz[0].length;
        double maior, acertos = 0;
        int indMaior;
        double[] classifications = new double[11];            // rotulos atribuidos     //   double[] classifications2 = new double[line];
        double[] cerToLastAttr;
        double[] vetProbClass = new double[nroClasses];
        double[] labels = new double[line];          // para debug

        cerToLastAttr = networks.criaProbSemClasseFG(matriz);  // calcula produtorio até o ultimo atributo, para todos os elementos de matriz
       //    cerToLastAttr = networks.criaProbSemClasseFGlog(matriz);

        for(int a = 0; a < line; a++){

            for(int b = 0; b < nroClasses; b++)
                vetProbClass[b] = 0;

            for(int b = 0; b < nroClasses; b++){
                vetProbClass[b] = cerToLastAttr[a] * networks.criaProbClassFG(matriz[a], Classes[b]);       // Weighted(matriz,attrGain);                // ############### classificador 3
           //       vetProbClass[b] = cerToLastAttr[a] + networks.criaProbClassFGlog(matriz[a], Classes[b]);
            }

            // normalizar vetProbClass ?

            maior = vetProbClass[0];
            indMaior = 0;
            for(int c = 1; c < nroClasses; c++){
                if(vetProbClass[c] > maior){
                    maior = vetProbClass[c];
                    indMaior = c;
                }
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

    double[] Classes;
    Networks networks;
    private AttributeHandler[] vetAtrHandler;
    private int nroClasses;
    private char[] attributeType;
    private double[][] initialData;
    private int[] ordem; // para possibilitar atualização incremental



}
