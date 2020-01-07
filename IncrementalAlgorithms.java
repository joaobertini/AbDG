import java.text.DecimalFormat;
import java.util.Random;
import java.io.*;

/**
 * Created by João on 04/08/2016.
 */
public class IncrementalAlgorithms {


    public IncrementalAlgorithms(char[] inputFile, String filename) {  // deals Heterogeneous Attributesr

        int tokenPos = 0;
        int tkPos = 0;
        int tkPos1 = 0;
        int lineNumber = 1;        // conta numero de linhas
        int collNumber = 0;
        char ch, ln, cl;

        // calcula o numero de linhas.
        while ((ln = inputFile[tkPos]) != '\0') {
            if (ln == '\n') {
                lineNumber++;
            }
            tkPos++;
        }
        // calcula o numero de colunas
        while ((cl = inputFile[tkPos1]) != '\n') {  // adicionado para comecar com -
            while (Character.isLetter(inputFile[tkPos1])) {
                collNumber++;
                tkPos1++;
            }
            tkPos1++;

        }
        //aloca matriz de dados
        double matriz[][];
        matriz = new double[lineNumber - 1][collNumber];


        int i = 0;
        int j = 0;
        int k = 0;
        StringBuffer number = new StringBuffer();
        attributeType = new char[collNumber];
        double[] runningAvg = new double[collNumber];      // para calcular media acumulativa - usada para atribuir valores a ? numericos
        tkPos1 = 0;

        while ((cl = inputFile[tkPos1]) != '\n') {
            while (Character.isLetter(inputFile[tkPos1])) {
                collNumber++;
                attributeType[k] = inputFile[tkPos1];
                tkPos1++;
                k++;
            }
            tkPos1++;
        }

        // constroi matriz de dados
        tokenPos = tkPos1 + 1;

        while ((ch = inputFile[tokenPos]) != '\0') {

            while ((ch = inputFile[tokenPos]) != '\n') {
                if (Character.isDigit(inputFile[tokenPos]) || (ch = inputFile[tokenPos]) == '-' || (ch = inputFile[tokenPos]) == '?') {  // primeira coluna
                    while (Character.isDigit(inputFile[tokenPos]) || (ch = inputFile[tokenPos]) == '.' ||
                            (ch = inputFile[tokenPos]) == '-') {
                        number.append(inputFile[tokenPos]);
                        tokenPos++;
                    }
                    if ((inputFile[tokenPos]) == '?') {
                        if (attributeType[j] == 'c') {
                            matriz[i][j] = 10000;  // valor diferente para atr categorico
                        } else {
                            if (i > 1)
                                matriz[i][j] = runningAvg[j] / (double) (i - 1);
                            else
                                matriz[i][j] = 0;
                        }
                        j++;
                        tokenPos++;
                    } else {
                        //  System.out.println(number.toString());
                        matriz[i][j] = Double.valueOf(number.toString()).doubleValue();
                        runningAvg[j] += matriz[i][j];
                        j++;
                        number.delete(0, number.length());
                    }
                } else
                    tokenPos++;

                if ((ch = inputFile[tokenPos]) == '\0') {
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


        // correção necessaria para execução de C45  -  trocar as classes -1 por 2
        for (int c = 0; c < line; c++)
            if (matriz[c][coll - 1] == 0)                                 // 10  POKER - LED
                matriz[c][coll - 1] = 2;


        //     for (int c = 0; c < line; c++)
        //         if (matriz[c][coll - 1] == 0) {
        ///           System.out.println(c);
        //           for(int p = 0; p < coll; p++)
        //               System.out.println(matriz[c][p] + " ");
        //         }

        //             matriz[c][coll - 1] = 10;                                  //       2 airlines


        double[] tempClass = new double[line];

        int cont = 0;
        boolean newClass = true;

        // descobre quantas classes possui E
        for (int p = 0; p < line; p++) {
            newClass = true;
            for (int q = 0; q < cont; q++)
                if (matriz[p][coll - 1] == tempClass[q])
                    newClass = false;

            if (newClass) {
                tempClass[cont] = matriz[p][coll - 1];
                cont++;
            }
        }

        Classes = new double[cont]; //declara vetor de classes
        // em Classes estao as classes originais
        for (int c = 0; c < cont; c++)
            Classes[c] = tempClass[c];


        nroClasses = cont;


        //   SimplifiedAbDG f = new SimplifiedAbDG(matriz, attributeType, Classes);
        //   f.crossValidation();

        //sAbDGEBIEnsembleReal(matriz,50,1000,"ELEC",5,1);

        // funcionando
   //     sAbDGRULEEnsembleRealNew(matriz,50,1000,"ASSET",5,1);

        //varia o tamanho da regra
        //sAbDGRULEEnsembleArtificialVarK(50,10000, 100,1,10);

        // sAbDGRULEEnsembleArtificial(50, 100000, 1000, 5, 1);
        // sAbDGRULEEnsembleArtificialRandomSizeInt(50, 100000, 100, 5, 1);
        // sAbDGRULEEnsembleReal(matriz,50,100,"ELEC",1,1);
        //sAbDGEBIEnsemble(matriz,100, 100, "ELEC", 1);



        // Print Rule  // ############################### PRINT #####################
      //  sAbDGRULEEnsembleRealNewPrintRule(matriz,50,100,"AGRA",5,1);


        // varia pool size x deciding commitee
   /*     double auxd;
        for(int p = 10; p < 220; p += 20){
            for(double d = 0; d <=1 ; d += 0.25) {
                if(d == 0)
                  auxd = 0.1;
                else
                  auxd = d;

                System.out.print(p + " " + (int)(auxd*p));
                System.out.println(sAbDGRULEEnsembleRealNew(matriz,p,100,"SEA",(int)(auxd*p),1));
            }
        }
     */

        //  varia lambda {0.1,0.2,..., 0.9}
        int[] chunckSize = {50,	100, 250, 500,750, 1000, 1500, 2000};
        //double[][] result = new double[50][8];
        //cont = 0;
        for (int is = 0; is < 10; is++) {
        for (double l = 0.1; l <= 1.05; l += 0.2) {
            System.out.print(l + " ");
            for (int cs = 0; cs < 8; cs++) {

                    System.out.print(varLambdasAbDGRULEEnsembleRealNew(matriz, 50, chunckSize[cs],"SEA", 5, 1, l) + " ");
                }
                System.out.println();
            }
        }



            // rule max {3, 4, ... 10}  x acc rule {0.5,0.6...,0.9}  // superficie
  /*      double[][] result = new double[75][7];
        cont = 0;
        for (int is = 0; is < 15; is++) {
           for (double ra = 0.5; ra < 0.95; ra += 0.1) {
               for (int rs = 2; rs <= 8; rs++) {
                    //System.out.print(rs + " " + ra + " ");
                    result[cont][rs-2] = varRuleSizeAccsAbDGRULEEnsembleRealNew(matriz, 50, 100, "SEA", 5, 1, rs, ra);

                }
                cont++;
            }
            //  hyperplaneRegressionAbp2(1500);
        }

       for(int s = 0; s < result.length; s++) {
            for (int r = 0; r < result[0].length; r++)
                System.out.print(result[s][r] + " ");

            System.out.println();
        }
*/


   /*     double[][] result = new double[7][20]; // grafico

        double ra = 0.6;
       // int rs = 5;
        cont = 0;
           // for (double ra = 0.5; ra < 0.95; ra += 0.1) {
                for (int rs = 2; rs <= 8; rs++) {
                    for (int is = 0; is < 20; is++)
                    //System.out.print(rs + " " + ra + " ");
                    result[rs-2][is] = varRuleSizeAccsAbDGRULEEnsembleRealNew(matriz, 50, 100, "SEA", 5, 1, rs, ra);
                        // rs-2
                    cont++;
                }

           // }
            //  hyperplaneRegressionAbp2(1500);


        for(int s = 0; s < result.length; s++) {
            for (int r = 0; r < result[0].length; r++)
                System.out.print(result[s][r] + " ");

            System.out.println();
        }

*/
    }





    public IncrementalAlgorithms() {

        Classes = new double[2];
        Classes[0] = 1;
        Classes[1] = 2;
        nroClasses = 2;

        sAbDGRULEEnsembleArtificial(50, 1000000, 1000, 5, 1);
        //  sAbDGStaticEnsemble(50, 100, "GAU", 100000, 1);
       // sAbDGEBIEnsemble(50, 100, "HYP", 1000000, 1);
        //  sAbDGEBIEnsembleAuto(50, 100, "HYP", 100000, 1);
        //sAbDGEnsemble(15, 100, "SEA", 5000, 10);
        //  sAbDGEnsembleChu(15, 100, "SEA", 5000, 10);
    }

    public double sAbDGEBIEnsemble(int commetteeSize, int NumPrev, String auxFileName, int len, int rep) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassificationT, testLabels;
        double[] weights;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;
        long t0 = 0, t1;


        double[][] m;

        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro, meanClfAcc = 0.5;
        double sampleSize = 1000;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(int) ((len - NumPrev) / sampleSize + 1)];
        long[] time = new long[(int) ((len - NumPrev) / sampleSize + 1)];
        double somaNom, somaDenom, alpha = 0.998;

        int[] mBestClf = new int[commetteeSize];

        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração

        for (int r = 0; r < rep; r++) {

            m = hyperplane(line); // gaussData(line); // // SEAconcept(line); // hyperplane(line);//SEAconcept(line);//  // // gaussData(line); // SEAconcept(line); // sineData(line);//   //circleData(line); // //  //  //  mixedData(line); //

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;

                    t0 = System.currentTimeMillis();
                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);

                    // ################# TESTE
                    int aleatorios = 45;
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }

                    t1 = System.currentTimeMillis();
                    time[0] = (t1 - t0);

                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[][] comiteOutput = new double[contTreino][nroClasses + 1];  // resultado do comite


                    if (i == 1)
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;

                    for (k = 0; k < commetteeSize; k++) {
                        testClassification = comite[k].DnoClassifierFull(matrizTreino, 0.5); //sAbDGVertexClassifier(matrizTreino);

                        // correctClassification[][k] = 0;
                        for (int c = 0; c < testClassification.length; c++) {
                            if (mBestClf[k] == 1) {  // comite[k].getClassAcc() >  meanClfAcc) {// mBestClf[k] == 1) //if(correctClassification[k][i-1] > 0.7)
                                comiteOutput[c][(int) testClassification[c][0]]++;// += Math.abs(Math.log(1 / comite[k].getWeight())); // comiteOutput[c][(int)testClassification[c][0]]++;//  testClassification[c][1]; // comite[k].getClassAcc(); //  //
                                correctClassification[k][i] = 1;
                                // mBestClf[k]++;
                            }
                        }

                        comite[k].calculateLastAcc();
                    }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        maior = 0;
                        indMaior = 0;
                        for (int b = 1; b < nroClasses + 1; b++)
                            if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                                maior = comiteOutput[a][b];// comiteOutput[a][b];
                                indMaior = b;
                            }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != indMaior)) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }

                        t1 = System.currentTimeMillis();
                        mcnemar[contPrequential] = erro;
                        if (contPrequential % sampleSize == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[(int) (contPrequential / sampleSize)] = (somaNom / somaDenom);

                            time[(int) (contPrequential / sampleSize)] = (t1 - t0);
                        }


                    }


                    for (int c = 0; c < commetteeSize; c++) {   // atializa peso de todos
                        if (mBestClf[c] != 1)  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)
                            comite[c].updateFadingFactor(matrizTreino);  //boosting(matrizTreino,weights)
                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        //     System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                    }
                    // System.out.println();
                    mBestClf = encontraMelhores(clfAcc, 5);


                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "DISCO.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/


        // rodar uma unica vez
        PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "TIME.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for (int t = 0; t < time.length; t++)
            out2.println(time[t] * 0.001);

        out2.close();




    /*    for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
       // System.out.println();
        }
*/

        return mediaErro;
    }

    public double sAbDGEBIEnsembleAuto(int commetteeSize, int NumPrev, String auxFileName, int len, int rep) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassificationT, testLabels;
        double[] weights;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        double[][] m;

        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro, meanClfAcc = 0.5;
        double sampleSize = 100;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(int) ((len - NumPrev) / sampleSize + 1)];
        double somaNom, somaDenom, alpha = 0.998;

        int[] mBestClf = new int[commetteeSize];

        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração

        for (int r = 0; r < rep; r++) {

            m = hyperplane(line); // gaussData(line); // // SEAconcept(line); // hyperplane(line);//SEAconcept(line);//  // // gaussData(line); // SEAconcept(line); // sineData(line);//   //circleData(line); // //  //  //  mixedData(line); //

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;
                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                 /*   for(int j = 0; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }
                */

                    // ################# TESTE
                    int aleatorios = 30;
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        // forest[j] = new Tree(C45(matrizTreino));
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[][] comiteOutput = new double[contTreino][nroClasses + 1];  // resultado do comite


                    if (i == 1)
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;

                    for (k = 0; k < commetteeSize; k++) {
                        testClassification = comite[k].sAbDGVertexClassifier(matrizTreino);

                        // correctClassification[][k] = 0;
                        for (int c = 0; c < testClassification.length; c++) {
                            // for(int j = 1; j < nroClasses + 1; j++)
                            // comiteOutput[c][j] += testClassification[c][j];
                            if (mBestClf[k] == 1) {  // comite[k].getClassAcc() >  meanClfAcc) {// mBestClf[k] == 1) //if(correctClassification[k][i-1] > 0.7)
                                comiteOutput[c][(int) testClassification[c][0]]++;// += Math.abs(Math.log(1 / comite[k].getWeight())); // comiteOutput[c][(int)testClassification[c][0]]++;//  testClassification[c][1]; // comite[k].getClassAcc(); //  //
                                correctClassification[k][i] = 1;
                                // mBestClf[k]++;
                            }
                            //   if(testClassification[c][0] == matrizTreino[c][coll-1])    // para encontrar pior arvore
                            //      correctClassification[k][i]++;
                        }

                        //       correctClassification[k][i] /= matrizTreino.length;
                        comite[k].calculateLastAcc();
                    }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        maior = 0;
                        indMaior = 0;
                        for (int b = 1; b < nroClasses + 1; b++)
                            if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                                maior = comiteOutput[a][b];// comiteOutput[a][b];
                                indMaior = b;
                            }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != indMaior)) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }


                        mcnemar[contPrequential] = erro;
                        if (contPrequential % sampleSize == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[(int) (contPrequential / sampleSize)] = (somaNom / somaDenom);
                        }


                    }


                    for (int c = 0; c < commetteeSize; c++) {   // atializa peso de todos
                        if (mBestClf[c] != 1)  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)
                            comite[c].updateFadingFactor(matrizTreino);  //boosting(matrizTreino,weights)
                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 1; h < commetteeSize; h++) {
                        //              System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                        // comite
                    }

                    // IMPLEMENTAR - TAMANHO DO COMITE ONDE AS LINHAS DE ENTROPIA E MEDIA DOS CLASSIFICADORES SE CRUZAM
                    int decidingComite = heuristicHowManyDecide(clfAcc, commetteeSize, matrizTreino.length);
                    // mBestClf = encontraMelhores(clfAcc,decidingComite);

                   /*
                   double aux;
                   for(int me = 2; me < commetteeSize; me++){
                        mBestClf = encontraMelhores(clfAcc,me);
                        aux = resolveComite(mBestClf,commetteeSize,matrizTreino);
                        //heuristicHowManyDecide(clfAcc, mBestClf,commetteeSize,matrizTreino.length);
             //         System.out.println(me +  " " +  aux);
                    //   System.out.println();
                        System.out.println(me + " " + aux + " " + clfAcc[me] + " " + entropyMeasrure(me,mBestClf,commetteeSize,matrizTreino.length) + " ");
                    }
                        System.out.println();
                    */

                    mBestClf = encontraMelhores(clfAcc, decidingComite);


                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "GRANU_EBI.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/



/*
        // rodar uma unica vez
        PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "SEA_PREQ.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < prequential.length; t++)
            out2.println(prequential[t]);

        out2.close();


        */

    /*    for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
       // System.out.println();
        }
*/

        return mediaErro;
    }


    public double resolveComite(int[] mBestClf, int commeetteSize, double[][] matrizTreino) {

        int contTreino = matrizTreino.length;
        int coll = matrizTreino[0].length;
        double acc = 0;


        double[][] comiteOutput = new double[contTreino][nroClasses + 1];

        for (int k = 0; k < commeetteSize; k++)
            for (int c = 0; c < contTreino; c++)
                if (mBestClf[k] == 1) {  // comite[k].getClassAcc() >  meanClfAcc) {// mBestClf[k] == 1) //if(correctClassification[k][i-1] > 0.7)
                    comiteOutput[c][(int) comite[k].getPredLabels()[c]]++;// += Math.abs(Math.log(1 / comite[k].getWeight())); // comiteOutput[c][(int)testClassification[c][0]]++;//  testClassification[c][1]; // comite[k].getClassAcc(); //  //
                }

        double maior = 0;
        int indMaior = -1;
        for (int a = 0; a < contTreino; a++) {
            maior = 0;
            indMaior = 0;
            for (int b = 1; b < nroClasses + 1; b++)
                if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                    maior = comiteOutput[a][b];// comiteOutput[a][b];
                    indMaior = b;
                }

            if ((matrizTreino[a][coll - 1] == indMaior)) {        // classes sao 1 e 2
                acc++;
            }

        }

        //  System.out.print(acc/contTreino + " ");
        return acc / contTreino;
    }

    public int heuristicHowManyDecide(double[] clfAcc, int commetteeSize, int len) {
        double somaAcc = 0;
        int numClfs = 0;
        int[] mBestClf = new int[commetteeSize];
        double[] sortedClfAcc = copiaVetor(clfAcc);
        quicksort(sortedClfAcc, 0, commetteeSize - 1);

        for (int me = 2; me < commetteeSize; me++) {  // ANDA NOS IMPARES
            somaAcc = 0;
            mBestClf = encontraMelhores(clfAcc, me);

            //     System.out.println(me + " " + sortedClfAcc[commetteeSize - me] + " " + entropyMeasrure(me, mBestClf, commetteeSize, len) + " ");

            somaAcc = sortedClfAcc[commetteeSize - me] - 2 * entropyMeasrure(me, mBestClf, commetteeSize, len);
            if (somaAcc < 0) {             //sortedClfAcc[commetteeSize - me] < entropyMeasrure(me, mBestClf, commetteeSize, len)
                numClfs = me;
                break;
            }
        }


        System.out.println(numClfs);
        return numClfs;
    }

    public double[] copiaVetor(double[] A) {

        double[] B = new double[A.length];

        for (int i = 0; i < A.length; i++)
            B[i] = A[i];

        return B;
    }

    public double entropyMeasrure(int L, int[] mBestClf, int enSize, int len) {  // medida de diversidade do ensemle

        double soma = 0, somaMenosL = 0, menor = 0, somaOut = 0;
        somaOut = 0;
        for (int j = 0; j < len; j++) {

            soma = 0;
            somaMenosL = 0;
            menor = 0;
            for (int i = 0; i < enSize; i++) {

                if (mBestClf[i] == 1) {
                    soma += (double) comite[i].getOracle()[j];
                }

            }
            somaMenosL = L - soma;

            menor = Math.min(soma, somaMenosL);

            somaOut += menor;
        }
        return (1 / (double) len) * (2 / (double) (L - 1)) * somaOut;

    }

    public double sAbDGEBIEnsemble(double[][] m, int commetteeSize, int NumPrev, String auxFileName, int rep) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassificationT, testLabels;
        double[] weights;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        int len = m.length;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro, meanClfAcc = 0.5;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev) / 100 + 1];
        double somaNom, somaDenom, alpha = 0.999;
        int[] mBestClf = new int[commetteeSize];

        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração

        for (int r = 0; r < rep; r++) {

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;
                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                 /*   for(int j = 0; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }
                */

                    // ################# TESTE
                    int aleatorios = 50;
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        // forest[j] = new Tree(C45(matrizTreino));
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[][] comiteOutput = new double[contTreino][nroClasses + 1];  // resultado do comite


                 /*   if(i > 1)   // EBI
                        mBestClf = encontraMelhores(correctClassification,i-1,10);
                    else
                        for(int t = 0; t <commetteeSize; t++)
                            mBestClf[t] = 1;
                  */

                    if (i == 1)
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;

                    for (k = 0; k < commetteeSize; k++) {
                        testClassification = comite[k].sAbDGVertexClassifier(matrizTreino);

                        // correctClassification[][k] = 0;
                        for (int c = 0; c < testClassification.length; c++) {
                            // for(int j = 1; j < nroClasses + 1; j++)
                            // comiteOutput[c][j] += testClassification[c][j];
                            if (mBestClf[k] == 1) {  // comite[k].getClassAcc() >  meanClfAcc) {// mBestClf[k] == 1) //if(correctClassification[k][i-1] > 0.7)
                                comiteOutput[c][(int) testClassification[c][0]]++;// += Math.abs(Math.log(1 / comite[k].getWeight())); // comiteOutput[c][(int)testClassification[c][0]]++;//  testClassification[c][1]; // comite[k].getClassAcc(); //  //
                                correctClassification[k][i] = 1;
                                // mBestClf[k]++;
                            }
                            //   if(testClassification[c][0] == matrizTreino[c][coll-1])    // para encontrar pior arvore
                            //      correctClassification[k][i]++;
                        }

                        //       correctClassification[k][i] /= matrizTreino.length;
                        comite[k].calculateLastAcc();
                    }


                 /*   testLabels = new double[matrizTreino.length];
                    for(k = 0; k < commetteeSize; k++){
                        testClassificationT = onlineClassification(matrizTreino,k);

                        // correctClassification[][k] = 0;
                        for(int c = 0; c < testClassificationT.length; c++){
                            // for(int j = 1; j < nroClasses + 1; j++)
                            // comiteOutput[c][j] += testClassification[c][j];
                            if(mBestClf[k] == 1) //if(correctClassification[k][i-1] > 0.7)
                                comiteOutput[c][(int)testClassificationT[c]] += forest[k].getWeight(); // comiteOutput[c][(int)testClassification[c][0]]++;//  testClassification[c][1]; // comite[k].getClassAcc(); //  //
                            if(testClassificationT[c] == matrizTreino[c][coll-1])    // para encontrar pior arvore
                                testLabels[c] = 0;
                            else
                                testLabels[c] = 1;
                        }

                        forest[k].setLabels(testLabels);
                       /// comite[k].updateAccClassifier();
                    }
                   */


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        maior = 0;
                        indMaior = 0;
                        for (int b = 1; b < nroClasses + 1; b++)
                            if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                                maior = comiteOutput[a][b];// comiteOutput[a][b];
                                indMaior = b;
                            }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != indMaior)) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }


                        mcnemar[contPrequential] = erro;
                        // PREQUENTIAL with fading factor

                        // prequential para todos os valores
                        //  prequential[contPrequential] = (erro + (contPrequential-1)*prequential[contPrequential-1])/(double)contPrequential;

                        // prequential de 100 em 100
                        if (contPrequential % 100 == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[contPrequential / 100] = (somaNom / somaDenom);
                        }


                        // prequential window
                        //      contPreqW = ((contPrequential - 1) % NumPrev);
                        //       SpreqW = SpreqW - preqWindow[contPreqW] + erro;
                        //       preqWindow[contPreqW] = erro;
                        //       preqWEst[contPrequential] = (SpreqW/(double)Math.min(NumPrev,contPrequential));

                    }

             /*       for(int e = 0; e < fold; e++)
                        if(weights[e] != 1)     // erro
                            weights[e] = (1 - erroComiteTreino)/erroComiteTreino;

                    weights = normalizaPesos(weights);    // normaliza peso de instancias
            */


                    for (int c = 0; c < commetteeSize; c++) {   // atializa peso de todos
                        //forest[c].updateClfWeight(weights);
                        //            comite[c].updateClfWeight(weights);
                        if (comite[c].getClassAcc() < meanClfAcc)
                            comite[c].batchUpdateIntConf(matrizTreino);  //boosting(matrizTreino,weights)
                        // comite[c].updateFadingFactor(boosting(matrizTreino,weights));
                        //comite[c].batchUpdateIntConf(boosting(matrizTreino,weights)); //comite[c].batchUpdateIntConf(boosting(matrizTreino,weights));
                        //  else
                        //      comite[c].updateFadingFactor(matrizTreino);
                        // comite[c].batchUpdateIntConf(matrizTreino);
                        //
                        // atualizar somente os que nao foram usados na ultima classificacao
                        //   comite[c].batchUpdateIntConf(matrizTreino);
                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        System.out.print(h + " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();
                        System.out.println();
                    }
                    mBestClf = encontraMelhores(clfAcc, 50);
                    // meanClfAcc = encontraTercQuartil(clfAcc); //somaAcc/commetteeSize;
/*
                    // ###################################  Testes ##################################################################
                    for(int j = 0; j < commetteeSize; j++) {
                       // comite[j].updateFadingFactor(boosting(matrizTreino,weights));
                        comite[j].updateClfWeight(weights);
                        if(comite[j].getWeight() > 1) {
                            //forest[j] = new Tree(C45(boosting(matrizTreino,weights)));
                            comite[j] = new SimplifiedAbDG(boosting(matrizTreino,weights), Classes, 0);// SimplifiedAbDG(boosting(matrizTreino,weights), Classes, 2 + (int)(Math.random()*6));
                            mBestClf[j] = -1; // para não ser incluido na proxima classificacao pois nao tem peso
                        }
                        else {
                            comite[j].updateFadingFactor(matrizTreino);
                            mBestClf[j]++;
                        }
                            //mBestClf[j] = 1;
                            //comite[j].batchUpdateIntConf(boosting(matrizTreino,weights));
                        //comite[j].updateFadingFactor(boosting(matrizTreino,weights));   // boosting(treino)
                  //      else
                  //          comite[j].batchUpdateIntConf(matrizTreino);
                      //  comite[j].batchUpdateIntConf(matrizTreino);  // short memory batchUpdate(matrizTreino);
                    }
*/

                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "GRANU_EBI.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/



/*
        // rodar uma unica vez
        PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "SEA_PREQ.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < prequential.length; t++)
            out2.println(prequential[t]);

        out2.close();


        */

        for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
            // System.out.println();
        }


        return mediaErro;
    }

    public double sAbDGEBIEnsembleRules(double[][] m, int commetteeSize, int NumPrev, String auxFileName, int deciding, int rep) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassificationT, testLabels;
        double[] weights;
        double sampleSize = 100;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        int len = m.length;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro, meanClfAcc = 0.5;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev) / 100 + 1];
        long[] time = new long[(len - NumPrev) / 100 + 1];
        double somaNom, somaDenom, alpha = 0.998;
        int[] mBestClf = new int[commetteeSize];

        long t0 = 0;
        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração

        for (int r = 0; r < rep; r++) {

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;

                    t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);

                    // ################# TESTE
                    int aleatorios = 43;
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                    long t1 = System.currentTimeMillis();
                    time[0] = (t1 - t0);
                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    //    long t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[][] comiteOutput = new double[contTreino][nroClasses + 1];  // resultado do comite


                    if (i == 1)
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;

                    for (k = 0; k < commetteeSize; k++) {
                        testClassification = comite[k].DnoClassifierFull(matrizTreino, 0.5);
                        // testClassification = comite[k].sAbDGVertexClassifier(matrizTreino);

                        // correctClassification[][k] = 0;
                        for (int c = 0; c < testClassification.length; c++) {
                            if (mBestClf[k] == 1) {  // comite[k].getClassAcc() >  meanClfAcc) {// mBestClf[k] == 1) //if(correctClassification[k][i-1] > 0.7)
                                comiteOutput[c][(int) testClassification[c][0]]++;// += Math.abs(Math.log(1 / comite[k].getWeight())); // comiteOutput[c][(int)testClassification[c][0]]++;//  testClassification[c][1]; // comite[k].getClassAcc(); //  //
                                correctClassification[k][i] = 1;
                                // mBestClf[k]++;
                            }
                        }

                        comite[k].calculateLastAcc();
                    }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        maior = 0;
                        indMaior = 0;
                        for (int b = 1; b < nroClasses + 1; b++)
                            if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                                maior = comiteOutput[a][b];// comiteOutput[a][b];
                                indMaior = b;
                            }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != indMaior)) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }

                        long t1 = System.currentTimeMillis();

                        mcnemar[contPrequential] = erro;
                        if (contPrequential % sampleSize == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[(int) (contPrequential / sampleSize)] = (somaNom / somaDenom);

                            time[(int) (contPrequential / sampleSize)] = (t1 - t0);
                        }

                    }


                    for (int c = 0; c < commetteeSize; c++) {   // atializa peso de todos
                        if (mBestClf[c] != 1)  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)
                            comite[c].updateFadingFactor(matrizTreino);  //boosting(matrizTreino,weights)
                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        //      System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                    }
                    // System.out.println();
                    mBestClf = encontraMelhores(clfAcc, deciding);

                    //  long t1 = System.currentTimeMillis();

                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + deciding + "DISCO.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/


        // rodar uma unica vez
        PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "TIME.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for (int t = 0; t < time.length; t++)
            out2.println(time[t] * 0.001);

        out2.close();




   /*     for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
            // System.out.println();
        }
*/

        return mediaErro;
    }


    public double sAbDGEBIEnsembleReal(double[][] m, int commetteeSize, int NumPrev, String auxFileName, int deciding, int rep) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassificationT, testLabels;
        double[] weights;
        double sampleSize = 1000;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        int len = m.length;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro, meanClfAcc = 0.5;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev) / 100 + 1];
        long[] time = new long[(len - NumPrev) / 100 + 1];
        double somaNom, somaDenom, alpha = 0.998;
        int[] mBestClf = new int[commetteeSize];

        long t0 = 0;
        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração

        for (int r = 0; r < rep; r++) {

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;

                    t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);

                    // ################# TESTE
                    int aleatorios = 43;
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                    long t1 = System.currentTimeMillis();
                    time[0] = (t1 - t0);
                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    //    long t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[][] comiteOutput = new double[contTreino][nroClasses + 1];  // resultado do comite


                    if (i == 1)
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;

                    for (k = 0; k < commetteeSize; k++) {
                        testClassification = comite[k].RulesAbDGVertexClassifier(matrizTreino); // DnoClassifierFull(matrizTreino, 0.5);
                        // testClassification = comite[k].sAbDGVertexClassifier(matrizTreino);

                        // correctClassification[][k] = 0;
                        for (int c = 0; c < testClassification.length; c++) {
                            if (mBestClf[k] == 1) {  // comite[k].getClassAcc() >  meanClfAcc) {// mBestClf[k] == 1) //if(correctClassification[k][i-1] > 0.7)
                                comiteOutput[c][(int) testClassification[c][0]]++;// += Math.abs(Math.log(1 / comite[k].getWeight())); // comiteOutput[c][(int)testClassification[c][0]]++;//  testClassification[c][1]; // comite[k].getClassAcc(); //  //
                                correctClassification[k][i] = 1;
                                // mBestClf[k]++;
                            }
                        }

                        comite[k].calculateLastAcc();
                    }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        maior = 0;
                        indMaior = 0;
                        for (int b = 1; b < nroClasses + 1; b++)
                            if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                                maior = comiteOutput[a][b];// comiteOutput[a][b];
                                indMaior = b;
                            }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != indMaior)) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }

                        long t1 = System.currentTimeMillis();

                        mcnemar[contPrequential] = erro;
                        if (contPrequential % sampleSize == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[(int) (contPrequential / sampleSize)] = (somaNom / somaDenom);

                            time[(int) (contPrequential / sampleSize)] = (t1 - t0);
                        }

                    }


                    for (int c = 0; c < commetteeSize; c++) {   // atializa peso de todos
                        if (mBestClf[c] != 1)  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)
                            comite[c].updateFadingFactor(matrizTreino);  //boosting(matrizTreino,weights)
                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        //      System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                    }
                    // System.out.println();
                    mBestClf = encontraMelhores(clfAcc, deciding);

                    //  long t1 = System.currentTimeMillis();

                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + deciding + "DISCO.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/


        // rodar uma unica vez
        PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "TIME.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for (int t = 0; t < time.length; t++)
            out2.println(time[t] * 0.001);

        out2.close();




   /*     for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
            // System.out.println();
        }
*/

        return mediaErro;
    }

    public double sAbDGRULEEnsembleArtificialVarK(int commetteeSize, int sizeStream, int NumPrev, int deciding, int rep) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassBaseModel, testLabels;
        double[] weights;
        double sampleSize = 100;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        double[] somaErroQuadVet = new double[commetteeSize];
        double[] somaErroExecs = new double[commetteeSize];
        double[] mediaErroVet = new double[commetteeSize];
        double[] desvioErroVet = new double[commetteeSize];
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        int len = sizeStream;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro, meanClfAcc = 0.5;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev) / 100 + 1];
        long[] time = new long[(len - NumPrev) / 100 + 1];
        double somaNom, somaDenom, alpha = 0.998;
        int[] mBestClf = new int[commetteeSize];

        long t0 = 0;
        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração

        String auxFileName = "HYP";   //#########################################
        deciding = 1001; //#####################################

        for (int r = 0; r < rep; r++) {

            double[] somaErroVet = new double[commetteeSize];


            double[][] m = hyperplane(line); // SEAconcept(line); //    //     // gaussData(line); // // SEAconcept(line); // hyperplane(line);//SEAconcept(line);//  // // gaussData(line); // SEAconcept(line); // sineData(line);//   //circleData(line); // //  //  //  mixedData(line); //

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;

                    t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);

                    // ################# TESTE
                    int aleatorios = 46;
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                    long t1 = System.currentTimeMillis();
                    time[0] = (t1 - t0);
                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    //    long t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[][] comiteOutput = new double[contTreino][commetteeSize];//[nroClasses+1];  // resultado do comite
                    // comiteOutput[][0] usado para armazenar força da regra

                    if (i == 1)
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;


                    double maiorR = 0;
                    int indMaiorR = 0;

                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        // ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                        comite[k].RuleClassifierFull(matrizTreino);  //sinlgeRulesAbDGVertexClassifier(matrizTreino); // DnoClassifierFull(matrizTreino, 0.5);

                        if (i > 1)
                            comite[k].updateAlpha();

                        comite[k].calculateLastAcc();
                    }
                    // ver se precisa


                    for (int e = 0; e < contTreino; e++) {
                        //  para uma unica instancia
                        //  constroi matriz de classificadores por força/probs de classe
                        testClassification = new double[commetteeSize][nroClasses + 1];
                        for (int c = 0; c < commetteeSize; c++) {
                            testClassBaseModel = comite[c].getStrenghtAndProbVector(e);
                            for (int d = 0; d < nroClasses + 1; d++)
                                testClassification[c][d] = testClassBaseModel[d];
                        }

                        for (int kr = 1; kr < commetteeSize; kr += 2) {

                            // avaliação do comite para o exemplo e
                            // avaliações que usam uma unica regra
                            // comiteOutput[e] = highestStrengh(testClassification,e);
                            //    comiteOutput[e] = highestProb(testClassification,e);
                            //     comiteOutput[e] = highestStrengTimesProb(testClassification,e);

                            // avaliaçãoes multi-regra
                            //    comiteOutput[e][kr] = votingMajority(testClassification,kr,e); // k ou thr 1000 no arq
                            comiteOutput[e][kr] = votingMajorityProb(testClassification, kr, e);    // 1001
                            //  comiteOutput[e] = votingStrengthWeigth(testClassification, kr, e);   1002
                            //  comiteOutput[e] = votingProbWeigth(testClassification, kr, e);       1003
                            //  comiteOutput[e][kr] = votingStrengthProbWeigth(testClassification, kr, e);    //1004
                        }
                    }


                    // }


                    for (int kr = 1; kr < commetteeSize; kr += 2) {
                        errorSum = 0;        // resolve comite
                        maior = 0;
                        indMaior = -1;
                        erroComiteTreino = 0;
                        for (int a = 0; a < contTreino; a++) {
                            //   maior = 0;
                            //    indMaior = 0;
                            //      for (int b = 1; b < nroClasses + 1; b++)
                            //         if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                            //          maior = comiteOutput[a][b];// comiteOutput[a][b];
                            //            indMaior = b;
                            //      }

                            contPrequential++;     // i em gama13
                            if ((matrizTreino[a][coll - 1] != comiteOutput[a][kr])) {        // classes sao 1 e 2
                                erroComiteTreino += weights[a];
                                errorSum++;
                                erro = 1;

                            } else {
                                weights[a] = 1;
                                erro = 0;

                            }


                        }
                        somaErroVet[kr] += errorSum;
                        //  somaErroQuadVet[kr] += Math.pow(errorSum,2);
                    }


                    for (int c = 0; c < commetteeSize; c++) {   // atializa peso de todos
                        //   if(mBestClf[c] != 1)  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)
                        comite[c].updateFadingFactor(matrizTreino);  //boosting(matrizTreino,weights)
                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        //      System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                    }
                    // System.out.println();
                    //   mBestClf = encontraMelhores(clfAcc,deciding);

                    //  long t1 = System.currentTimeMillis();


                }


            }  // fim it
            // soma erro para cada iteração
            for (int kr = 1; kr < commetteeSize; kr += 2) {
                somaErroExecs[kr] += (somaErroVet[kr] / sizeStream) * 100;
                somaErroQuadVet[kr] += Math.pow((somaErroVet[kr] / sizeStream) * 100, 2);

            }

        }  // fim rep

        for (int kr = 1; kr < commetteeSize; kr += 2) {
            mediaErroVet[kr] = (somaErroExecs[kr] / rep);
            desvioErroVet[kr] = Math.sqrt(((rep) * somaErroQuadVet[kr] - (Math.pow(somaErroExecs[kr], 2))) / (double) ((rep) * (rep - 1)));

        }

        //  mediaErro = (somaErro/(double)((it-1)*rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + deciding + "RULEAbDG_VAR_K.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        // out.println("################################################################################");

        for (int s = 1; s < mediaErroVet.length; s += 2)
            out.println(s + " " + mediaErroVet[s] + " " + desvioErroVet[s]); //(somaErroVet[s]/sizeStream)*100);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/


        // rodar uma unica vez
   /*     PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "TIME.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < time.length; t++)
            out2.println(time[t]*0.001);

        out2.close();

*/


   /*     for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
            // System.out.println();
        }
*/

        return mediaErro;
    }

    public double sAbDGRULEEnsembleArtificial(int commetteeSize, int sizeStream, int NumPrev, int deciding, int rep) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassBaseModel = new double[10];  // para não dar erro de inicializaçao
        double[] weights;
        double sampleSize = 1000;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0, sizeDeciding;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        int len = sizeStream;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev) / 100 + 1];
        long[] time = new long[(len - NumPrev) / 100 + 1];
        double somaNom, somaDenom, alpha = 0.998;
        int[] mBestClf = new int[commetteeSize];

        long t0 = 0;
        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração

        String auxFileName = "SEA";   //#########################################


        for (int r = 0; r < rep; r++) {

            double[][] m = SEAconcept(line); // hyperplane(line);// // gaussData(line); //   gaussData(line); // SEAconcept(line); // sineData(line);//   //circleData(line); // //  //  //  mixedData(line); //

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;

                    t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);

                    // ################# TESTE
                    int aleatorios = commetteeSize - 4; // 4 são nao aleatorios
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                    long t1 = System.currentTimeMillis();
                    time[0] = (t1 - t0);
                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    //    long t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[] comiteOutput = new double[contTreino];//[nroClasses+1];  // resultado do comite
                    // comiteOutput[][0] usado para armazenar força da regra

                    if (i == 1) {
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;
                        sizeDeciding = commetteeSize;   // primeiro batch todos decidem
                    } else
                        sizeDeciding = deciding;


                    double maiorR = 0;
                    int indMaiorR = 0;

                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        // ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                        comite[k].RuleClassifierFullProbOnly(matrizTreino);
                        //comite[k].RuleClassifierFull(matrizTreino);  //sinlgeRulesAbDGVertexClassifier(matrizTreino); // DnoClassifierFull(matrizTreino, 0.5);

                        //    if(i > 1)
                        //        comite[k].updateAlpha();

                        // comite[k].calculateLastAcc();
                    }
                    // ver se precisa


                    for (int e = 0; e < contTreino; e++) {
                        //  para uma unica instancia
                        //  constroi matriz de classificadores por força/probs de classe
                        //testClassification = new double[commetteeSize][nroClasses + 1];
                        testClassification = new double[sizeDeciding][nroClasses + 1];

                        int contBest = 0;
                        for (int c = 0; c < commetteeSize; c++) {
                            if (mBestClf[c] == 1) {    // so considera os melhores
                                testClassBaseModel = comite[c].getStrenghtAndProbVector(e);
                                for (int d = 0; d < nroClasses + 1; d++)                        // multiplica por acc
                                    testClassification[contBest][d] = testClassBaseModel[d];// * comite[c].getClassAcc();
                                contBest++;                                   //   ####################################################
                            }

                        }

                        // print RULE
                        //    for(int j = 0; j < coll-1; j++)
                        //        System.out.print(" Atr " + j + " " + matrizTreino[e][j]);
                        //    System.out.println();


                        int kr = 25;
                        int aux;
                        // avaliação do comite para o exemplo e
                        // avaliações que usam uma unica regra
                     ///   comiteOutput[e] = highestStrengh(testClassification);  // precisa do classificador RuleClassifier


                     //    comiteOutput[e] = (double)highestProb(testClassification);  // abdg-hp

                           //  aux = highestStrengTimesProb(testClassification,e);

                     //   comiteOutput[e] = simpleVoting(testClassification);
                       // comiteOutput[e] = sumOfProbs(testClassification);

                        comiteOutput[e] = weightedVoting(testClassification);  // abdg-we


                        // avaliaçãoes multi-regra
                        //comiteOutput[e] = votingMajority(testClassification,kr,e); // k ou thr
                        //  comiteOutput[e] = votingMajorityProb(testClassification,kr,e);
                        //  comiteOutput[e] = votingStrengthWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingStrengthProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = comite[aux].getPredLabels()[e]; // retorna a classe

                        // print RULE
                        //  comite[aux].printRule(e,matrizTreino[e]);
                        //  System.out.println(comiteOutput[e] +  " Correta " + matrizTreino[e][coll-1] );
                    }


                    // }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        //   maior = 0;
                        //    indMaior = 0;
                        //      for (int b = 1; b < nroClasses + 1; b++)
                        //         if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                        //          maior = comiteOutput[a][b];// comiteOutput[a][b];
                        //            indMaior = b;
                        //      }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != comiteOutput[a])) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }

                        long t1 = System.currentTimeMillis();

                        mcnemar[contPrequential] = erro;
                        if (contPrequential % sampleSize == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;  // de 100 %
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[(int) (contPrequential / sampleSize)] = (somaNom / somaDenom);

                            time[(int) (contPrequential / sampleSize)] = (t1 - t0);
                        }

                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        //      System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                    }
                    // System.out.println();

                    mBestClf = encontraMelhores(clfAcc, deciding);

                    if (i > 1) {
                        // Update
                        for (int c = 0; c < commetteeSize; c++) {   // atualiza peso de todos
                            comite[c].updateAlpha();
                            if (mBestClf[c] != 1) {  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)

                                comite[c].updateFadingFactor(matrizTreino);  //boosting(matrizTreino,weights)
                            }

                            //   comite[c].updateKL(matrizTreino);  //boosting(matrizTreino,weights)
                        }
                    }

                    for (k = 0; k < commetteeSize; k++)
                        comite[k].calculateLastAcc();  // passa acc atual para last

                    // #####################################ajusta rule size
                    double maiorAcc, aux;
                    int rSize = 0, newRSize = 0;
                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        if(comite[k].getClassAcc() < 0.6  /*mBestClf[k] != 1*/) {// ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                            maiorAcc = comite[k].getClassAcc();
                            rSize = comite[k].getRuleSize();
                            newRSize = rSize;
                            for(r = 1; r < 7; r++) {
                                if (r != rSize) {
                                    comite[k].setRuleSize(r);
                                    comite[k].RuleClassifierFullProbOnly(matrizTreino);
                                    aux = comite[k].getClassAcc();
                                    if (aux > maiorAcc) {
                                        maiorAcc = aux;
                                        newRSize = r;
                                    }
                                }
                            }
                          //  System.out.println(rSize + " " + newRSize);
                            comite[k].setRuleSize(newRSize);
                        }
                    }

                    //  long t1 = System.currentTimeMillis();

                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + deciding + "RULEAbDG.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/


        // rodar uma unica vez
   /*     PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "TIME.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < time.length; t++)
            out2.println(time[t]*0.001);

        out2.close();

*/


   /*     for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
            // System.out.println();
        }
*/

        return mediaErro;
    }

    public double sAbDGRULEEnsembleArtificialRandomSizeInt(int commetteeSize, int sizeStream, int NumPrev, int deciding, int rep) {
        // considera intervalos com tamanhos aleatorios
        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassBaseModel = new double[10];  // para não dar erro de inicializaçao
        double[] weights;
        double sampleSize = 1000;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0, sizeDeciding;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        int len = sizeStream;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev) / 100 + 1];
        long[] time = new long[(len - NumPrev) / 100 + 1];
        double somaNom, somaDenom, alpha = 0.998;
        int[] mBestClf = new int[commetteeSize];

        long t0 = 0;
        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração

        String auxFileName = "HYP";   //#########################################


        for (int r = 0; r < rep; r++) {

            double[][] m = hyperplane(line);  // SEAconcept(line); // // // gaussData(line); //   gaussData(line); // SEAconcept(line); // sineData(line);//   //circleData(line); // //  //  //  mixedData(line); //

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;

                    t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);

                    // ################# Todos aleatórios
                    for (int j = 0; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes );    // cria comitê
                        contaRedesCriadas++;
                    }


                    long t1 = System.currentTimeMillis();
                    time[0] = (t1 - t0);
                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    //    long t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[] comiteOutput = new double[contTreino];//[nroClasses+1];  // resultado do comite
                    // comiteOutput[][0] usado para armazenar força da regra

                    if (i == 1) {
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;
                        sizeDeciding = commetteeSize;   // primeiro batch todos decidem
                    } else
                        sizeDeciding = deciding;


                    double maiorR = 0;
                    int indMaiorR = 0;

                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        // ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                        comite[k].RuleClassifierFullProbOnlyRandSize(matrizTreino);
                        //comite[k].RuleClassifierFull(matrizTreino);  //sinlgeRulesAbDGVertexClassifier(matrizTreino); // DnoClassifierFull(matrizTreino, 0.5);

                        //    if(i > 1)
                        //        comite[k].updateAlpha();

                        // comite[k].calculateLastAcc();
                    }
                    // ver se precisa


                    for (int e = 0; e < contTreino; e++) {
                        //  para uma unica instancia
                        //  constroi matriz de classificadores por força/probs de classe
                        //testClassification = new double[commetteeSize][nroClasses + 1];
                        testClassification = new double[sizeDeciding][nroClasses + 1];

                        int contBest = 0;
                        for (int c = 0; c < commetteeSize; c++) {
                            if (mBestClf[c] == 1) {    // so considera os melhores
                                testClassBaseModel = comite[c].getStrenghtAndProbVector(e);
                                for (int d = 0; d < nroClasses + 1; d++)                        // multiplica por acc
                                    testClassification[contBest][d] = testClassBaseModel[d];// * comite[c].getClassAcc();
                                contBest++;                                   //   ####################################################
                            }

                        }

                        // print RULE
                        //    for(int j = 0; j < coll-1; j++)
                        //        System.out.print(" Atr " + j + " " + matrizTreino[e][j]);
                        //    System.out.println();


                        int kr = 25;
                        int aux;
                        // avaliação do comite para o exemplo e
                        // avaliações que usam uma unica regra
                        ///   comiteOutput[e] = highestStrengh(testClassification);  // precisa do classificador RuleClassifier


                        //  comiteOutput[e] = (double)highestProb(testClassification);

                        //  aux = highestStrengTimesProb(testClassification,e);

                        comiteOutput[e] = simpleVoting(testClassification);


                        // comiteOutput[e] = sumOfProbs(testClassification);

                        // avaliaçãoes multi-regra
                        //comiteOutput[e] = votingMajority(testClassification,kr,e); // k ou thr
                        //  comiteOutput[e] = votingMajorityProb(testClassification,kr,e);
                        //  comiteOutput[e] = votingStrengthWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingStrengthProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = comite[aux].getPredLabels()[e]; // retorna a classe

                        // print RULE
                        //  comite[aux].printRule(e,matrizTreino[e]);
                        //  System.out.println(comiteOutput[e] +  " Correta " + matrizTreino[e][coll-1] );
                    }


                    // }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        //   maior = 0;
                        //    indMaior = 0;
                        //      for (int b = 1; b < nroClasses + 1; b++)
                        //         if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                        //          maior = comiteOutput[a][b];// comiteOutput[a][b];
                        //            indMaior = b;
                        //      }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != comiteOutput[a])) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }

                        long t1 = System.currentTimeMillis();

                        mcnemar[contPrequential] = erro;
                        if (contPrequential % sampleSize == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;  // de 100 %
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[(int) (contPrequential / sampleSize)] = (somaNom / somaDenom);

                            time[(int) (contPrequential / sampleSize)] = (t1 - t0);
                        }

                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                    }
                     System.out.println();

                    mBestClf = encontraMelhores(clfAcc, deciding);

                    if (i > 1) {
                        // Update
                        for (int c = 0; c < commetteeSize; c++) {   // atualiza peso de todos
                            comite[c].updateAlpha();
                            if (mBestClf[c] != 1) {  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)

                                comite[c].updateFadingFactorRandSize(matrizTreino);  //boosting(matrizTreino,weights)
                            }

                            //   comite[c].updateKL(matrizTreino);  //boosting(matrizTreino,weights)
                        }
                    }

                    for (k = 0; k < commetteeSize; k++)
                        comite[k].calculateLastAcc();  // passa acc atual para last


                    //  long t1 = System.currentTimeMillis();

                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + deciding + "RULEAbDG.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/


        // rodar uma unica vez
   /*     PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "TIME.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < time.length; t++)
            out2.println(time[t]*0.001);

        out2.close();

*/


   /*     for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
            // System.out.println();
        }
*/

        return mediaErro;
    }

    public double sAbDGRULEEnsembleReal(double[][] m, int commetteeSize, int NumPrev, String auxFileName, int deciding, int rep) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassBaseModel, testLabels;
        double[] weights;
        double sampleSize = 1000;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        int len = m.length;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro, meanClfAcc = 0.5;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev) / 100 + 1];
        long[] time = new long[(len - NumPrev) / 100 + 1];
        double somaNom, somaDenom, alpha = 0.998;
        int[] mBestClf = new int[commetteeSize];

        long t0 = 0;
        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração

        for (int r = 0; r < rep; r++) {

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;

                    t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);

                    // ################# TESTE
                    int aleatorios = 46;
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                    long t1 = System.currentTimeMillis();
                    time[0] = (t1 - t0);
                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    //    long t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[] comiteOutput = new double[contTreino];//[nroClasses+1];  // resultado do comite
                    // comiteOutput[][0] usado para armazenar força da regra

                    if (i == 1)
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;


                    double maiorR = 0;
                    int indMaiorR = 0;

                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        // ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                        comite[k].RuleClassifierFull(matrizTreino);  //sinlgeRulesAbDGVertexClassifier(matrizTreino); // DnoClassifierFull(matrizTreino, 0.5);

                        comite[k].calculateLastAcc();
                    }
                    // ver se precisa


                    for (int e = 0; e < contTreino; e++) {
                        //  para uma unica instancia
                        //  constroi matriz de classificadores por força/probs de classe
                        testClassification = new double[commetteeSize][nroClasses + 1];
                        for (int c = 0; c < commetteeSize; c++) {
                            testClassBaseModel = comite[c].getStrenghtAndProbVector(e);
                            for (int d = 0; d < nroClasses + 1; d++)
                                testClassification[c][d] = testClassBaseModel[d];
                        }

                        int kr = 5;
                        // avaliação do comite para o exemplo e
                        // avaliações que usam uma unica regra
                        //       comiteOutput[e] = highestStrengh(testClassification,e);
                        //        comiteOutput[e] = highestProb(testClassification,e);
                        //        comiteOutput[e] = highestStrengTimesProb(testClassification,e);
                        // avaliaçãoes multi-regra
                        comiteOutput[e] = votingMajority(testClassification, kr, e); // k ou thr
                        //         comiteOutput[e] = votingMajorityProb(testClassification,kr,e);
                        //        comiteOutput[e] = votingStrengthWeigth(testClassification, kr, e);
                        //        comiteOutput[e] = votingProbWeigth(testClassification, kr, e);
                        //        comiteOutput[e] = votingStrengthProbWeigth(testClassification, kr, e);
                    }


                    // }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        //   maior = 0;
                        //    indMaior = 0;
                        //      for (int b = 1; b < nroClasses + 1; b++)
                        //         if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                        //          maior = comiteOutput[a][b];// comiteOutput[a][b];
                        //            indMaior = b;
                        //      }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != comiteOutput[a])) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }

                        long t1 = System.currentTimeMillis();

                        mcnemar[contPrequential] = erro;
                        if (contPrequential % sampleSize == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[(int) (contPrequential / sampleSize)] = (somaNom / somaDenom);

                            time[(int) (contPrequential / sampleSize)] = (t1 - t0);
                        }

                    }


                    for (int c = 0; c < commetteeSize; c++) {   // atializa peso de todos
                        //   if(mBestClf[c] != 1)  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)
                        comite[c].updateFadingFactor(matrizTreino);  //boosting(matrizTreino,weights)
                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        //      System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                    }
                    // System.out.println();
                    //  mBestClf = encontraMelhores(clfAcc,deciding);

                    //  long t1 = System.currentTimeMillis();

                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + deciding + "RULEAbDG.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/


        // rodar uma unica vez
   /*     PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "TIME.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < time.length; t++)
            out2.println(time[t]*0.001);

        out2.close();

*/


   /*     for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
            // System.out.println();
        }
*/

        return mediaErro;
    }


    public double sAbDGRULEEnsembleRealNew(double[][] m, int commetteeSize, int NumPrev, String auxFileName, int deciding, int rep) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassBaseModel = new double[10];  // para não dar erro de inicializaçao
        double[] weights;
        double sampleSize = 1000;     //###############################################
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0, sizeDeciding;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        int len = m.length;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev) / 100 + 1];
        long[] time = new long[(len - NumPrev) / 100 + 1];
        double somaNom, somaDenom, alpha = 0.998;
        int[] mBestClf = new int[commetteeSize];

        long t0 = 0;
        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração


        for (int r = 0; r < rep; r++) {

            // double[][] m = hyperplane(line);  // SEAconcept(line); // // // gaussData(line); //   gaussData(line); // SEAconcept(line); // sineData(line);//   //circleData(line); // //  //  //  mixedData(line); //

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;

                    t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);

                    // ################# TESTE
                    int aleatorios = commetteeSize - 4; // 4 são nao aleatorios
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                    long t1 = System.currentTimeMillis();
                    time[0] = (t1 - t0);
                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    //    long t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[] comiteOutput = new double[contTreino];//[nroClasses+1];  // resultado do comite
                    // comiteOutput[][0] usado para armazenar força da regra

                    if (i == 1) {
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;
                        sizeDeciding = commetteeSize;   // primeiro batch todos decidem
                    } else
                        sizeDeciding = deciding;


                    double maiorR = 0;
                    int indMaiorR = 0;

                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        // ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                        comite[k].RuleClassifierFullProbOnly(matrizTreino);
                        //comite[k].RuleClassifierFull(matrizTreino);  //sinlgeRulesAbDGVertexClassifier(matrizTreino); // DnoClassifierFull(matrizTreino, 0.5);

                        //    if(i > 1)
                        //        comite[k].updateAlpha();

                        // comite[k].calculateLastAcc();
                    }
                    // ver se precisa


                    for (int e = 0; e < contTreino; e++) {
                        //  para uma unica instancia
                        //  constroi matriz de classificadores por força/probs de classe
                        //testClassification = new double[commetteeSize][nroClasses + 1];
                        testClassification = new double[sizeDeciding][nroClasses + 1];

                        int contBest = 0;
                        for (int c = 0; c < commetteeSize; c++) {
                            if (mBestClf[c] == 1) {    // so considera os melhores
                                testClassBaseModel = comite[c].getStrenghtAndProbVector(e);
                                for (int d = 0; d < nroClasses + 1; d++)                        // multiplica por acc
                                    testClassification[contBest][d] = testClassBaseModel[d];// * comite[c].getClassAcc();
                                contBest++;                                   //   ####################################################
                            }

                        }

                        // print RULE
                        //    for(int j = 0; j < coll-1; j++)
                        //        System.out.print(" Atr " + j + " " + matrizTreino[e][j]);
                        //    System.out.println();


                        int kr = 25;
                        int aux;
                        // avaliação do comite para o exemplo e
                        // avaliações que usam uma unica regra
                        ///   comiteOutput[e] = highestStrengh(testClassification);  // precisa do classificador RuleClassifier


                        //   comiteOutput[e] = (double)highestProb(testClassification);



                        // multi-regra rules
                        //  comiteOutput[e] = simpleVoting(testClassification);
                        //comiteOutput[e] = sumOfProbs(testClassification);
                        comiteOutput[e] = weightedVoting(testClassification);


                        // avaliaçãoes multi-regra
                        //comiteOutput[e] = votingMajority(testClassification,kr,e); // k ou thr
                        //  comiteOutput[e] = votingMajorityProb(testClassification,kr,e);
                        //  comiteOutput[e] = votingStrengthWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingStrengthProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = comite[aux].getPredLabels()[e]; // retorna a classe

                        // print RULE
                        //  comite[aux].printRule(e,matrizTreino[e]);
                        //  System.out.println(comiteOutput[e] +  " Correta " + matrizTreino[e][coll-1] );
                    }


                    // }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        //   maior = 0;
                        //    indMaior = 0;
                        //      for (int b = 1; b < nroClasses + 1; b++)
                        //         if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                        //          maior = comiteOutput[a][b];// comiteOutput[a][b];
                        //            indMaior = b;
                        //      }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != comiteOutput[a])) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }

                        long t1 = System.currentTimeMillis();

                        mcnemar[contPrequential] = erro;
                        if (contPrequential % sampleSize == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;  // de 100 %
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[(int) (contPrequential / sampleSize)] = (somaNom / somaDenom);

                            time[(int) (contPrequential / sampleSize)] = (t1 - t0);
                        }

                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        //      System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                    }
                    // System.out.println();

                    mBestClf = encontraMelhores(clfAcc, deciding);

                    if (i > 1) {
                        // Update
                        for (int c = 0; c < commetteeSize; c++) {   // atualiza peso de todos
                            comite[c].updateAlpha();
                            if (mBestClf[c] != 1) {  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)

                                comite[c].updateFadingFactor(matrizTreino);  //boosting(matrizTreino,weights)
                            }

                            //   comite[c].updateKL(matrizTreino);  //boosting(matrizTreino,weights)
                        }
                    }

                    for (k = 0; k < commetteeSize; k++)
                        comite[k].calculateLastAcc();  // passa acc atual para last

                    // #####################################ajusta rule size
                    double maiorAcc, aux;
                    int rSize = 0, newRSize = 0;
                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        if(comite[k].getClassAcc() < 0.6  /*mBestClf[k] != 1*/) {// ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                            maiorAcc = comite[k].getClassAcc();
                            rSize = comite[k].getRuleSize();
                            newRSize = rSize;
                            for(r = 1; r < 7; r++) {
                                if (r != rSize) {
                                    comite[k].setRuleSize(r);
                                    comite[k].RuleClassifierFullProbOnly(matrizTreino);
                                    aux = comite[k].getClassAcc();
                                    if (aux > maiorAcc) {
                                        maiorAcc = aux;
                                        newRSize = r;
                                    }
                                }
                            }
                            //  System.out.println(rSize + " " + newRSize);
                            comite[k].setRuleSize(newRSize);
                        }
                    }

                    //  long t1 = System.currentTimeMillis();

                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + deciding + "RULEAbDG.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/


        // rodar uma unica vez
   /*     PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "TIME.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < time.length; t++)
            out2.println(time[t]*0.001);

        out2.close();

*/


   /*     for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
            // System.out.println();
        }
*/

        return mediaErro;
    }

    public double sAbDGRULEEnsembleRealNewPrintRule(double[][] m, int commetteeSize, int NumPrev, String auxFileName, int deciding, int rep) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassBaseModel = new double[10];  // para não dar erro de inicializaçao
        double[] weights;
        double sampleSize = 100;     //###############################################
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0, sizeDeciding;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        int len = m.length;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev) / 100 + 1];
        long[] time = new long[(len - NumPrev) / 100 + 1];
        double somaNom, somaDenom, alpha = 0.998;
        int[] mBestClf = new int[commetteeSize];

        DecimalFormat dec = new DecimalFormat("0.00");

        long t0 = 0;
        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração


        for (int r = 0; r < rep; r++) {

            // double[][] m = hyperplane(line);  // SEAconcept(line); // // // gaussData(line); //   gaussData(line); // SEAconcept(line); // sineData(line);//   //circleData(line); // //  //  //  mixedData(line); //

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;

                    t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);

                    // ################# TESTE
                    int aleatorios = commetteeSize - 4; // 4 são nao aleatorios
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                    long t1 = System.currentTimeMillis();
                    time[0] = (t1 - t0);
                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    //    long t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[] comiteOutput = new double[contTreino];//[nroClasses+1];  // resultado do comite
                    // comiteOutput[][0] usado para armazenar força da regra

                    if (i == 1) {
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;
                        sizeDeciding = commetteeSize;   // primeiro batch todos decidem
                    } else
                        sizeDeciding = deciding;


                    double maiorR = 0;
                    int indMaiorR = 0;

                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        // ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                        comite[k].RuleClassifierFullProbOnly(matrizTreino);
                        //comite[k].RuleClassifierFull(matrizTreino);  //sinlgeRulesAbDGVertexClassifier(matrizTreino); // DnoClassifierFull(matrizTreino, 0.5);

                        //    if(i > 1)
                        //        comite[k].updateAlpha();

                        // comite[k].calculateLastAcc();
                    }
                    // ver se precisa

                  //  int conta = 0;
                    for (int e = 0; e < contTreino; e++) {
                        //  para uma unica instancia
                        //  constroi matriz de classificadores por força/probs de classe
                        //testClassification = new double[commetteeSize][nroClasses + 1];
                        testClassification = new double[sizeDeciding][nroClasses + 1];

                        int contBest = 0, conta = 0;
                        for (int c = 0; c < commetteeSize; c++) {
                            if (mBestClf[c] == 1) {    // so considera os melhores
                                testClassBaseModel = comite[c].getStrenghtAndProbVector(e);
                                for (int d = 0; d < nroClasses + 1; d++)                        // multiplica por acc
                                    testClassification[contBest][d] = testClassBaseModel[d];// * comite[c].getClassAcc();
                                contBest++;                                   //   ####################################################
                            }




                        // print RULE,

                            if(i > 10) {
                                if (mBestClf[c] == 1) {
                                    for (int j = 0; j < coll - 1; j++)
                                        System.out.print(" Atr " + j + " " + dec.format(matrizTreino[e][j]));
                                    System.out.println();

                                    System.out.println("Regra ");
                                    comite[c].printRule(e, matrizTreino[e]);
                                    for(int t = 1; t < nroClasses + 1; t++)
                                        System.out.print(testClassification[conta][t] + " ");
                                    System.out.println(" Correta " + dec.format(matrizTreino[e][coll - 1]));
                                    System.out.println();
                                    conta++;

                                }
                               // System.out.println();
                            }


                    }

                          System.out.println();
                   //       comiteOutput[e] = (double)highestProb(testClassification);



                        // multi-regra rules
                        //  comiteOutput[e] = simpleVoting(testClassification);
                        //comiteOutput[e] = sumOfProbs(testClassification);
                        comiteOutput[e] = weightedVoting(testClassification);


                        // avaliaçãoes multi-regra
                        //comiteOutput[e] = votingMajority(testClassification,kr,e); // k ou thr
                        //  comiteOutput[e] = votingMajorityProb(testClassification,kr,e);
                        //  comiteOutput[e] = votingStrengthWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingStrengthProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = comite[aux].getPredLabels()[e]; // retorna a classe

                        // print RULE
                       //   comite[aux].printRule(e,matrizTreino[e]);
                      //    System.out.println(comiteOutput[e] +  " Correta " + matrizTreino[e][coll-1] );
                    }


                    // }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        //   maior = 0;
                        //    indMaior = 0;
                        //      for (int b = 1; b < nroClasses + 1; b++)
                        //         if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                        //          maior = comiteOutput[a][b];// comiteOutput[a][b];
                        //            indMaior = b;
                        //      }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != comiteOutput[a])) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }

                        long t1 = System.currentTimeMillis();

                        mcnemar[contPrequential] = erro;
                        if (contPrequential % sampleSize == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;  // de 100 %
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[(int) (contPrequential / sampleSize)] = (somaNom / somaDenom);

                            time[(int) (contPrequential / sampleSize)] = (t1 - t0);
                        }

                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        //      System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                    }
                    // System.out.println();

                    mBestClf = encontraMelhores(clfAcc, deciding);

                    if (i > 1) {
                        // Update
                        for (int c = 0; c < commetteeSize; c++) {   // atualiza peso de todos
                            comite[c].updateAlpha();
                            if (mBestClf[c] != 1) {  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)

                                comite[c].updateFadingFactor(matrizTreino);  //boosting(matrizTreino,weights)
                            }

                            //   comite[c].updateKL(matrizTreino);  //boosting(matrizTreino,weights)
                        }
                    }

                    for (k = 0; k < commetteeSize; k++)
                        comite[k].calculateLastAcc();  // passa acc atual para last

                    // #####################################ajusta rule size
                    double maiorAcc, aux;
                    int rSize = 0, newRSize = 0;
                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        if(comite[k].getClassAcc() < 0.6  /*mBestClf[k] != 1*/) {// ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                            maiorAcc = comite[k].getClassAcc();
                            rSize = comite[k].getRuleSize();
                            newRSize = rSize;
                            for(r = 1; r < 7; r++) {
                                if (r != rSize) {
                                    comite[k].setRuleSize(r);
                                    comite[k].RuleClassifierFullProbOnly(matrizTreino);
                                    aux = comite[k].getClassAcc();
                                    if (aux > maiorAcc) {
                                        maiorAcc = aux;
                                        newRSize = r;
                                    }
                                }
                            }
                            //  System.out.println(rSize + " " + newRSize);
                            comite[k].setRuleSize(newRSize);
                        }
                    }

                    //  long t1 = System.currentTimeMillis();

                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);

        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + deciding + "RULEAbDG.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/


        // rodar uma unica vez
   /*     PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "TIME.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < time.length; t++)
            out2.println(time[t]*0.001);

        out2.close();

*/


   /*     for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
            // System.out.println();
        }
*/

        return mediaErro;
    }

    public double varLambdasAbDGRULEEnsembleRealNew(double[][] m, int commetteeSize, int NumPrev, String auxFileName, int deciding, int rep, double lambda) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassBaseModel = new double[10];  // para não dar erro de inicializaçao
        double[] weights;
        double sampleSize = 100;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0, sizeDeciding;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        int len = m.length;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev) / 100 + 1];
        long[] time = new long[(len - NumPrev) / 100 + 1];
        double somaNom, somaDenom, alpha = 0.998;
        int[] mBestClf = new int[commetteeSize];

        long t0 = 0;
        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração


        for (int r = 0; r < rep; r++) {

         //    double[][] m =  SEAconcept(50000); // hyperplane(line);// // gaussData(line); //   gaussData(line); // SEAconcept(line); // sineData(line);//   //circleData(line); // //  //  //  mixedData(line); //

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;

                    t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);

                    // ################# TESTE
                    int aleatorios = commetteeSize - 4; // 4 são nao aleatorios
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                    long t1 = System.currentTimeMillis();
                    time[0] = (t1 - t0);
                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    //    long t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[] comiteOutput = new double[contTreino];//[nroClasses+1];  // resultado do comite
                    // comiteOutput[][0] usado para armazenar força da regra

                    if (i == 1) {
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;
                        sizeDeciding = commetteeSize;   // primeiro batch todos decidem
                    } else
                        sizeDeciding = deciding;


                    double maiorR = 0;
                    int indMaiorR = 0;

                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        // ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                        comite[k].RuleClassifierFullProbOnly(matrizTreino);
                        //comite[k].RuleClassifierFull(matrizTreino);  //sinlgeRulesAbDGVertexClassifier(matrizTreino); // DnoClassifierFull(matrizTreino, 0.5);

                        //    if(i > 1)
                        //        comite[k].updateAlpha();

                        // comite[k].calculateLastAcc();
                    }
                    // ver se precisa


                    for (int e = 0; e < contTreino; e++) {
                        //  para uma unica instancia
                        //  constroi matriz de classificadores por força/probs de classe
                        //testClassification = new double[commetteeSize][nroClasses + 1];
                        testClassification = new double[sizeDeciding][nroClasses + 1];

                        int contBest = 0;
                        for (int c = 0; c < commetteeSize; c++) {
                            if (mBestClf[c] == 1) {    // so considera os melhores
                                testClassBaseModel = comite[c].getStrenghtAndProbVector(e);
                                for (int d = 0; d < nroClasses + 1; d++)                        // multiplica por acc
                                    testClassification[contBest][d] = testClassBaseModel[d];// * comite[c].getClassAcc();
                                contBest++;                                   //   ####################################################
                            }

                        }

                        // print RULE
                        //    for(int j = 0; j < coll-1; j++)
                        //        System.out.print(" Atr " + j + " " + matrizTreino[e][j]);
                        //    System.out.println();


                       // int kr = 25;
                      //  int aux;
                        // avaliação do comite para o exemplo e
                        // avaliações que usam uma unica regra
                        ///   comiteOutput[e] = highestStrengh(testClassification);  // precisa do classificador RuleClassifier


                       comiteOutput[e] = (double)highestProb(testClassification);



                        // multi-regra rules
                        //  comiteOutput[e] = simpleVoting(testClassification);
                        //comiteOutput[e] = sumOfProbs(testClassification);

                   //    comiteOutput[e] = (double)weightedVoting(testClassification);


                        // avaliaçãoes multi-regra
                        //comiteOutput[e] = votingMajority(testClassification,kr,e); // k ou thr
                        //  comiteOutput[e] = votingMajorityProb(testClassification,kr,e);
                        //  comiteOutput[e] = votingStrengthWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingStrengthProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = comite[aux].getPredLabels()[e]; // retorna a classe

                        // print RULE
                        //  comite[aux].printRule(e,matrizTreino[e]);
                        //  System.out.println(comiteOutput[e] +  " Correta " + matrizTreino[e][coll-1] );
                    }


                    // }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        //   maior = 0;
                        //    indMaior = 0;
                        //      for (int b = 1; b < nroClasses + 1; b++)
                        //         if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                        //          maior = comiteOutput[a][b];// comiteOutput[a][b];
                        //            indMaior = b;
                        //      }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != comiteOutput[a])) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }

                        long t1 = System.currentTimeMillis();

                        mcnemar[contPrequential] = erro;
                        if (contPrequential % sampleSize == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;  // de 100 %
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[(int) (contPrequential / sampleSize)] = (somaNom / somaDenom);

                            time[(int) (contPrequential / sampleSize)] = (t1 - t0);
                        }

                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        //      System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                    }
                    // System.out.println();

                    mBestClf = encontraMelhores(clfAcc, deciding);

                    if (i > 1) {
                        // Update
                        for (int c = 0; c < commetteeSize; c++) {   // atualiza peso de todos
                            comite[c].updateAlphaVARLamda(lambda);
                            if (mBestClf[c] != 1) {  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)

                                comite[c].updateFadingFactor(matrizTreino);  //boosting(matrizTreino,weights)
                            }

                            //   comite[c].updateKL(matrizTreino);  //boosting(matrizTreino,weights)
                        }
                    }

                    for (k = 0; k < commetteeSize; k++)
                        comite[k].calculateLastAcc();  // passa acc atual para last

                    // #####################################ajusta rule size
                    double maiorAcc, aux;
                    int rSize = 0, newRSize = 0;
                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        if(comite[k].getClassAcc() < 0.6  /*mBestClf[k] != 1*/) {// ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                            maiorAcc = comite[k].getClassAcc();
                            rSize = comite[k].getRuleSize();
                            newRSize = rSize;
                            for(r = 1; r < 7; r++) {
                                if (r != rSize) {
                                    comite[k].setRuleSize(r);
                                    comite[k].RuleClassifierFullProbOnly(matrizTreino);
                                    aux = comite[k].getClassAcc();
                                    if (aux > maiorAcc) {
                                        maiorAcc = aux;
                                        newRSize = r;
                                    }
                                }
                            }
                            //  System.out.println(rSize + " " + newRSize);
                            comite[k].setRuleSize(newRSize);
                        }
                    }

                    //  long t1 = System.currentTimeMillis();

                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + deciding + "RULEAbDG.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/


        // rodar uma unica vez
   /*     PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "TIME.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < time.length; t++)
            out2.println(time[t]*0.001);

        out2.close();

*/


   /*     for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
            // System.out.println();
        }
*/

        return mediaErro;
    }

    public double varRuleSizeAccsAbDGRULEEnsembleRealNew(double[][] m, int commetteeSize, int NumPrev, String auxFileName, int deciding, int rep, int ruleMax, double ruleAcc) {

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] testClassBaseModel = new double[10];  // para não dar erro de inicializaçao
        double[] weights;
        double sampleSize = 100;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0, sizeDeciding;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i = 0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        int len = m.length;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev) / 100 + 1];
        long[] time = new long[(len - NumPrev) / 100 + 1];
        double somaNom, somaDenom, alpha = 0.998;
        int[] mBestClf = new int[commetteeSize];

        long t0 = 0;
        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if (line % NumPrev == 0)
            it = (int) Math.ceil(line / NumPrev);
        else
            it = (int) Math.ceil(line / NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        double[][] correctClassification = new double[commetteeSize][it];
        for (int c = 0; c < commetteeSize; c++)
            correctClassification[c][0] = 1;  // primeira iteração


        for (int r = 0; r < rep; r++) {

          //  double[][] m = SEAconcept(50000); // hyperplane(line);  //  // // // gaussData(line); //   gaussData(line); // SEAconcept(line); // sineData(line);//   //circleData(line); // //  //  //  mixedData(line); //

            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            // forest = new Tree[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if (i == 0) {   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for (int h = 0; h < trainFold; h++) {
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for (int y = 0; y < coll; y++) {
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;

                    t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);

                    // ################# TESTE
                    int aleatorios = commetteeSize - 4; // 4 são nao aleatorios
                    for (int j = 0; j < commetteeSize - aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j, ruleMax);    // cria comitê
                        contaRedesCriadas++;
                    }

                    for (int j = commetteeSize - aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0, ruleMax);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                    long t1 = System.currentTimeMillis();
                    time[0] = (t1 - t0);
                } else {
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i * fold;
                    i2 = (i + 1) * fold;
                    if (i == (it - 1)) {        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it - 1) * NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for (int h = i1; h < i2; h++) {
                        if ((h >= (i * fold) && (h < i2))) {              //if(h < i*fold + testFold){
                            for (int y = 0; y < coll; y++)
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1 / (double) fold;
                            contTreino++;
                        }
                    }

                    //    long t0 = System.currentTimeMillis();

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[] comiteOutput = new double[contTreino];//[nroClasses+1];  // resultado do comite
                    // comiteOutput[][0] usado para armazenar força da regra

                    if (i == 1) {
                        for (int t = 0; t < commetteeSize; t++)
                            mBestClf[t] = 1;
                        sizeDeciding = commetteeSize;   // primeiro batch todos decidem
                    } else
                        sizeDeciding = deciding;


                    double maiorR = 0;
                    int indMaiorR = 0;

                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        // ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                        comite[k].RuleClassifierFullProbOnly(matrizTreino);
                        //comite[k].RuleClassifierFull(matrizTreino);  //sinlgeRulesAbDGVertexClassifier(matrizTreino); // DnoClassifierFull(matrizTreino, 0.5);

                        //    if(i > 1)
                        //        comite[k].updateAlpha();

                        // comite[k].calculateLastAcc();
                    }
                    // ver se precisa


                    for (int e = 0; e < contTreino; e++) {
                        //  para uma unica instancia
                        //  constroi matriz de classificadores por força/probs de classe
                        //testClassification = new double[commetteeSize][nroClasses + 1];
                        testClassification = new double[sizeDeciding][nroClasses + 1];

                        int contBest = 0;
                        for (int c = 0; c < commetteeSize; c++) {
                            if (mBestClf[c] == 1) {    // so considera os melhores
                                testClassBaseModel = comite[c].getStrenghtAndProbVector(e);
                                for (int d = 0; d < nroClasses + 1; d++)                        // multiplica por acc
                                    testClassification[contBest][d] = testClassBaseModel[d];// * comite[c].getClassAcc();
                                contBest++;                                   //   ####################################################
                            }

                        }

                        // print RULE
                        //    for(int j = 0; j < coll-1; j++)
                        //        System.out.print(" Atr " + j + " " + matrizTreino[e][j]);
                        //    System.out.println();


                        int kr = 25;
                        int aux;
                        // avaliação do comite para o exemplo e
                        // avaliações que usam uma unica regra
                        ///   comiteOutput[e] = highestStrengh(testClassification);  // precisa do classificador RuleClassifier


                    //   comiteOutput[e] = (double)highestProb(testClassification);



                        // multi-regra rules
                        //  comiteOutput[e] = simpleVoting(testClassification);
                        //comiteOutput[e] = sumOfProbs(testClassification);

                      comiteOutput[e] = weightedVoting(testClassification);


                        // avaliaçãoes multi-regra
                        //comiteOutput[e] = votingMajority(testClassification,kr,e); // k ou thr
                        //  comiteOutput[e] = votingMajorityProb(testClassification,kr,e);
                        //  comiteOutput[e] = votingStrengthWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = votingStrengthProbWeigth(testClassification, kr, e);
                        //  comiteOutput[e] = comite[aux].getPredLabels()[e]; // retorna a classe

                        // print RULE
                        //  comite[aux].printRule(e,matrizTreino[e]);
                        //  System.out.println(comiteOutput[e] +  " Correta " + matrizTreino[e][coll-1] );
                    }


                    // }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    erroComiteTreino = 0;
                    for (int a = 0; a < contTreino; a++) {
                        //   maior = 0;
                        //    indMaior = 0;
                        //      for (int b = 1; b < nroClasses + 1; b++)
                        //         if (comiteOutput[a][b] > maior) { // comiteOutput[a][b]
                        //          maior = comiteOutput[a][b];// comiteOutput[a][b];
                        //            indMaior = b;
                        //      }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != comiteOutput[a])) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }

                        long t1 = System.currentTimeMillis();

                        mcnemar[contPrequential] = erro;
                        if (contPrequential % sampleSize == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;  // de 100 %
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[(int) (contPrequential / sampleSize)] = (somaNom / somaDenom);

                            time[(int) (contPrequential / sampleSize)] = (t1 - t0);
                        }

                    }


                    double[] clfAcc = new double[commetteeSize];
                    for (int h = 0; h < commetteeSize; h++) {
                        //      System.out.print( " " + comite[h].getClassAcc()); //.getWeight());
                        clfAcc[h] = comite[h].getClassAcc();

                    }
                    // System.out.println();

                    mBestClf = encontraMelhores(clfAcc, deciding);

                    if (i > 1) {
                        // Update
                        for (int c = 0; c < commetteeSize; c++) {   // atualiza peso de todos
                            comite[c].updateAlpha();
                            if (mBestClf[c] != 1) {  //(comite[c].getClassAcc() < meanClfAcc)  //(mBestClf[c] != 1)

                                comite[c].updateFadingFactor(matrizTreino);  //boosting(matrizTreino,weights)
                            }

                            //   comite[c].updateKL(matrizTreino);  //boosting(matrizTreino,weights)
                        }
                    }

                    for (k = 0; k < commetteeSize; k++)
                        comite[k].calculateLastAcc();  // passa acc atual para last

                    // #####################################ajusta rule size
                    double maiorAcc, aux;
                    int rSize = 0, newRSize = 0;
                    for (k = 0; k < commetteeSize; k++) { // RuleClassifierFull
                        if(comite[k].getClassAcc() < ruleAcc ) {// ruleClassifierFull calcula força da regra e prob para as classes para cada exemplo
                            maiorAcc = comite[k].getClassAcc();
                            rSize = comite[k].getRuleSize();
                            newRSize = rSize;
                            for(r = 1; r < ruleMax; r++) {
                                if (r != rSize) {
                                    comite[k].setRuleSize(r);
                                    comite[k].RuleClassifierFullProbOnly(matrizTreino);
                                    aux = comite[k].getClassAcc();
                                    if (aux > maiorAcc) {
                                        maiorAcc = aux;
                                        newRSize = r;
                                    }
                                }
                            }
                            //  System.out.println(rSize + " " + newRSize);
                            comite[k].setRuleSize(newRSize);
                        }
                    }

                    //  long t1 = System.currentTimeMillis();

                    saidaTotal[i][0] = i + 1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum / (double) contTreino) * 100;
                    somaErro += errorSum / (double) contTreino * 100;
                    somaErroQuad += Math.pow(errorSum / (double) contTreino, 2);

                }


            }  // fim it

        }  // fim rep


        mediaErro = (somaErro / (double) ((it - 1) * rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + deciding + "RULEAbDG.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch (java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);


        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro, 2) / (double) it * rep)) / (double) (it * rep - 1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        // for(int s = 0; s < it; s++)
        //    out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for (int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/


        // rodar uma unica vez
   /*     PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "TIME.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < time.length; t++)
            out2.println(time[t]*0.001);

        out2.close();

*/


   /*     for (int c = 0; c < commetteeSize; c++) {
            for (int d = 0; d < it; d++)
                System.out.println(c + " " + d + " " + comite[c].getClassAcc()); //  correctClassification[c][d]
            // System.out.println();
        }
*/

        return mediaErro;
    }


    // métodos para avaliação do comite baseado em regras

    public int highestStrengh(double[][] classifications) {
        // retorna a classificação correspondente à regra de maior força para a instancia example
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior, classe;
        int indMaior;

        maior = classifications[0][0];
        indMaior = 0;
        for (int i = 1; i < line; i++)
            if (classifications[i][0] > maior) {  // compara força das regras
                maior = classifications[i][0];
                indMaior = i;
            }

        // classe = comite[indMaior].getPredLabels()[example];

        return indMaior;

    }


    public int highestProb(double[][] classifications) {
        // retorna a classificação correspondente à regra de maior prob. para a instancia example
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior = -10, prob;
        int cls = 0;  //[indMaior, classe]
        int indMaior;


        indMaior = -1;
        for (int i = 0; i < line; i++) {
            for (int j = 1; j < nroClasses + 1; j++) {
                if (classifications[i][j] > maior) {
                    maior = classifications[i][j];
                    indMaior = i;
                    cls = j;
                }


            }
        }
        //classe = comite[indMaior].getPredLabels()[example];

        return cls;

    }

    public int highestProbTieBreak(double[][] classifications) {
        // retorna a classificação correspondente à regra de maior prob. para a instancia example
        // considera a possibilidade de empate e resolve usando cov. ou cov e ent.
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior = -10, prob;
        int cls = 0;  //[indMaior, classe]
        int indMaior;
        int[] indIguais = new int[line]; // usado para tratar empate, preenchido com classe
        boolean empate = false;
        // classification[][0] = força.. [][1] prob cl 1.. [][2] prob cl 2.

        indMaior = -1;
        for (int i = 0; i < line; i++) {
            for (int j = 1; j < nroClasses + 1; j++) {
                if (classifications[i][j] > maior) {
                    maior = classifications[i][j];
                    indMaior = i;
                    cls = j;
                }
            }
        }

        // verifica se ocorre empate
        indIguais[indMaior] = cls;
        for(int i = 0; i < line; i++){
            for(int j = 1; j < nroClasses + 1; j++)
                if(i != indMaior && maior == classifications[i][j]) {
                    empate = true;
                    indIguais[i] = j;
                }
         }

         if(empate){ // pega a classe do el que tem maior força
             maior = classifications[indMaior][0]; // força
             for (int i = 0; i < line; i++) {
                 if(indIguais[i] != 0){
                    if(maior < classifications[i][0]){
                        maior = classifications[i][0];
                        indMaior = i;
                    }
                 }
             }

             cls = indIguais[indMaior];

         }

        //classe = comite[indMaior].getPredLabels()[example];

        return cls;

    }

    public int highestProb(double[][] classifications, int example, int[] bClf) {
        // retorna a classificação correspondente à regra de maior prob. para a instancia example
        // considerando os melhores classificadores
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior, prob;
        int bestClf = 0;  //[indMaior, classe]
        int indMaior;

        maior = -10;
        indMaior = -1;
        for (int i = 0; i < line; i++) {
            if (bClf[i] == 1) {   // considera somente os melhores clfs.
                prob = comite[i].getProbPredLabel(example);
                if (prob > maior) {  // compara probs das regras
                    maior = comite[i].getProbPredLabel(example);
                    indMaior = i;
                }
            }
        }

        //classe = comite[indMaior].getPredLabels()[example];

        return indMaior; // bestClf; // retorna o melhor classificador

    }

    public int simpleVoting(double[][] classifications) {
        // retorna a classificação resultante da maioria dos votos, cada um dado pela maior prob.
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior = 0, prob;
        int indMaior = 0, cls = 0;
        int[] voting = new int[nroClasses + 1];  //[indMaior, classe]


        for (int i = 0; i < line; i++) {
            maior = classifications[i][1];
            cls = 1;
            indMaior = 1;
            for (int j = 2; j < nroClasses + 1; j++) {
                if (classifications[i][j] > maior) {
                    maior = classifications[i][j];
                    indMaior = i;
                    cls = j;
                }

            }
            voting[cls]++;
        }


        maior = voting[1];
        indMaior = 1;
        for (int j = 2; j < nroClasses + 1; j++)
            if (voting[j] > maior) {  // compara probs das regras
                maior = voting[j];
                indMaior = j;
            }

        //classe = comite[indMaior].getPredLabels()[example];

        return indMaior; // bestClf; // retorna o melhor classificador

    }

    public int weightedVoting(double[][] classifications) {
        // retorna a classificação resultante da maioria dos votos, cada um dado pela maior prob.
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior = 0, prob;
        int indMaior = 0, cls = 0;
        double[] voting = new double[nroClasses + 1];  //[indMaior, classe]


        for (int i = 0; i < line; i++) {
            maior = classifications[i][1];
            cls = 1;
            indMaior = 1;
            for (int j = 2; j < nroClasses + 1; j++) {
                if (classifications[i][j] > maior) {
                    maior = classifications[i][j];
                  //  indMaior = i;
                    cls = j;
                }

            }
            voting[cls] += classifications[i][cls];
        }


        maior = voting[1];
        indMaior = 1;
        for (int j = 2; j < nroClasses + 1; j++)
            if (voting[j] > maior) {
                maior = voting[j];
                indMaior = j;
            }

        //classe = comite[indMaior].getPredLabels()[example];

        return indMaior; // bestClf; // retorna o melhor classificador

    }

    public int sumOfProbs(double[][] classifications) {
        // retorna a classificação resultante maior soma das probs
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior = 0, prob;
        int indMaior = 0, cls = 0;
        double[] voting = new double[nroClasses + 1];  //[indMaior, classe]


        for (int i = 0; i < line; i++) {
           for (int j = 1; j < nroClasses + 1; j++) {
                 voting[j] = classifications[i][j];
            }
        }


        maior = voting[1];
        indMaior = 1;
        for (int j = 2; j < nroClasses + 1; j++)
            if (voting[j] > maior) {  // compara probs das regras
                maior = voting[j];
                indMaior = j;
            }

        //classe = comite[indMaior].getPredLabels()[example];

        return indMaior; // bestClf; // retorna o melhor classificador

    }

    public int highestStrengTimesProb(double[][] classifications, int example) {
        // retorna a classificação correspondente à regra de maior forçaxprob para a instancia example
        int line = classifications.length;
        //int coll = matriz[0].length;
        double maior, prob;
        double classe;// = new double[2];  // [indMaior][classe]
        int indMaior;

        maior = comite[0].getProbPredLabel(example) * classifications[0][0];  // prob x força
        indMaior = 0;
        for (int i = 1; i < line; i++) {
            prob = comite[i].getProbPredLabel(example) * classifications[i][0];
            if (prob > maior) {  // compara probs das regras
                maior = prob; //comite[i].getProbPredLabel(example)*classifications[i][0];
                indMaior = i;
            }
        }
        //classe = comite[indMaior].getPredLabels()[example];
        //comite[indMaior].printRule(example,matriz);
        //System.out.println(classe +  " Correta " + matriz[example][coll-1] );

        return indMaior;
    }




// multi-rules

    public double votingMajority(double[][] classifications, int k, int example){
        // retorna a classificação correspondente à marioria das k regra de maior força para a instancia example
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior, classe;
        int[] votes = new int[nroClasses+1];
        int indMaior = -1;
        double[] auxClass = new double[line];  // mascara para classifications


        for(int a = 0; a < k; a++) {
            maior = -1;
            for (int i = 1; i < line; i++)
                if (auxClass[i] != -2 && classifications[i][0] > maior) {  // compara força das regras
                    maior = classifications[i][0];
                    indMaior = i;
                }

                votes[(int) comite[indMaior].getPredLabels()[example]]++;
                auxClass[indMaior] = -2;
                //classifications[indMaior][0] = -2; // para que não seja considerado novamente

        }

        int indMajority = 0;
        int maiorClasse = 0;
        for(int j = 1; j < votes.length; j++)
            if(votes[j] > maiorClasse){
                maiorClasse = votes[j];
                indMajority = j;
            }

        classe = indMajority;

        return classe;

    }

    public double votingMajorityProb(double[][] classifications, int k, int example){
        // retorna a classificação correspondente à marioria das k regra de maior prob para a instancia example
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior, classe;
        int[] votes = new int[nroClasses+1];
        int indMaior = -1;
        double[] auxClass = new double[line];  // mascara para classifications

        for(int a = 0; a < k; a++) {
            maior = -1;
            for (int i = 1; i < line; i++)
                if (auxClass[i] != -2 && comite[i].getProbPredLabel(example) > maior) {  // compara prob das regras
                    maior = comite[i].getProbPredLabel(example);
                    indMaior = i;
                }

            votes[(int) comite[indMaior].getPredLabels()[example]]++;
            auxClass[indMaior] = -2;
            //classifications[indMaior][0] = -2; // para que não seja considerado novamente

        }

        int indMajority = 0;
        int maiorClasse = 0;
        for(int j = 1; j < votes.length; j++)
            if(votes[j] > maiorClasse){
                maiorClasse = votes[j];
                indMajority = j;
            }

        classe = indMajority;

        return classe;

    }

    public double votingStrengthWeigth(double[][] classifications, int k, int example){
        // retorna a classificação correspondente à marioria das k regras ponderada pela soma das forças para a instancia example
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior, classe;
        int[] votes = new int[nroClasses+1];
        int indMaior = -1;
        double[] auxClass = new double[line];  // mascara para classifications

        for(int a = 0; a < k; a++) {
            maior = -1;
            for (int i = 1; i < line; i++)
                if (auxClass[i] != -2 && classifications[i][0] > maior) {  // compara força das regras
                    maior = classifications[i][0];
                    indMaior = i;
                }

            votes[(int) comite[indMaior].getPredLabels()[example]] += classifications[indMaior][0];
            auxClass[indMaior] = -2;
                //classifications[indMaior][0] = -2; // para que não seja considerado novamente

        }

        int indMajority = 0;
        int maiorClasse = 0;
        for(int j = 1; j < votes.length; j++)
            if(votes[j] > maiorClasse){
                maiorClasse = votes[j];
                indMajority = j;
            }

        classe = indMajority;

        return classe;

    }

    public double votingProbWeigth(double[][] classifications, int k, int example){
        // retorna a classificação correspondente à marioria das k regra ponderada pela prob. para a instancia example
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior, classe, prob;
        int[] votes = new int[nroClasses+1];
        int indMaior = -1;
        int[] vetUsed = new int[line];

        for(int a = 0; a < k; a++) {
            maior = -1;
            for (int i = 0; i < line; i++)
                if (vetUsed[i] != -1 && comite[i].getProbPredLabel(example) > maior) {  // compara força das regras
                    maior = comite[i].getProbPredLabel(example);
                    indMaior = i;
                }

            votes[(int) comite[indMaior].getPredLabels()[example]] += comite[indMaior].getProbPredLabel(example);
            vetUsed[indMaior] = -1; // para que não seja considerado novamente

        }

        int indMajority = 0;
        int maiorClasse = 0;
        for(int j = 1; j < votes.length; j++)
            if(votes[j] > maiorClasse){
                maiorClasse = votes[j];
                indMajority = j;
            }

        classe = indMajority;

        return classe;

    }

    public double votingStrengthProbWeigth(double[][] classifications, int k, int example){
        // retorna a classificação correspondente à marioria das k regra ponderada pela prob. para a instancia example
        int line = classifications.length;
        int coll = classifications[0].length;
        double maior, classe, prob;
        int[] votes = new int[nroClasses+1];
        int indMaior = -1;
        int[] vetUsed = new int[line];

        for(int a = 0; a < k; a++) {
            maior = -1;
            for (int i = 0; i < line; i++)
                if (vetUsed[i] != -1 && comite[i].getProbPredLabel(example)*classifications[i][0] > maior) {  // compara força das regras
                    maior = comite[i].getProbPredLabel(example)*classifications[i][0];
                    indMaior = i;
                }

            votes[(int) comite[indMaior].getPredLabels()[example]]++;// += maior; //comite[indMaior].getProbPredLabel(example);
            vetUsed[indMaior] = -1; // para que não seja considerado novamente

        }

        int indMajority = 0;
        int maiorClasse = 0;
        for(int j = 1; j < votes.length; j++)
            if(votes[j] > maiorClasse){
                maiorClasse = votes[j];
                indMajority = j;
            }

        classe = indMajority;

        return classe;

    }


    public int[] encontraMelhores(double[] clfAcc, int m){  // encontra os m melhores classificadores

        int cs = clfAcc.length;
        int[] mBest = new int[cs];
        double maior = 0;
        int indMaior = 0;

        for(int j = 0; j < m; j++) {
            maior = 0;
            for (int i = 0; i < cs; i++)
                if (mBest[i] != 1)
                    if (clfAcc[i] > maior) {
                        maior = clfAcc[i];
                        indMaior = i;
                    }

            mBest[indMaior] = 1;
            //comite[indMaior].resetAlpha();
        }
       return mBest;
    }


    public double sAbDGStaticEnsemble(int commetteeSize, int NumPrev, String auxFileName, int len, int rep){

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[][] testClassification;
        double[] weights;
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i=0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;
        double erroComiteTreino = 0;

        double[][] m;

        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        // Prequential para todos
        //  double[] prequential = new double[len - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(len - NumPrev)/100 + 1];
        double somaNom, somaDenom, alpha = 0.998;

        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if( line % NumPrev == 0)
            it = (int)Math.ceil(line/NumPrev);
        else
            it = (int)Math.ceil(line/NumPrev) + 1;                  // define numero de iteraçoes

        double[][] saidaTotal = new double[it][2];

        int[][] correctClassification = new int[commetteeSize][it];

        for(int r = 0; r < rep; r++){

            m = SEAconcept(line);// sineData(line);// gaussData(line); // circleData(line);//  //  mixedData(line); //  //    hyperplane(line); //  //////
            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {

                if(i == 0){   // constroi comite
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for(int h = 0; h < trainFold; h++){
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for(int y = 0; y < coll; y++ ){
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    int initGranu = 3;
                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                 /*   for(int j = 0; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }
                */

                    // ################# TESTE
                    int aleatorios = 10;
                    for(int j = 0; j < commetteeSize-aleatorios; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, initGranu + j);    // cria comitê
                        contaRedesCriadas++;
                    }

                    for(int j = commetteeSize-aleatorios; j < commetteeSize; j++) {
                        comite[j] = new SimplifiedAbDG(matrizTreino, Classes, 0);    // cria comitê
                        contaRedesCriadas++;
                    }
                    //###########################

                }
                else{
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i*fold;
                    i2 = (i+1)*fold;
                    if(i == (it-1)){        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it-1)*NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for(int h = i1; h < i2; h++){
                        if((h >= (i*fold) && (h < i2))){              //if(h < i*fold + testFold){
                            for(int y = 0; y < coll; y++ )
                                matrizTreino[contTreino][y] = m[h][y];
                            weights[contTreino] = 1/(double)fold;
                            contTreino++;
                        }
                    }

                    matrizTreino = normalizacaoMaiorMenor(matrizTreino);
                    double[][] comiteOutput = new double[contTreino][nroClasses+1];  // resultado do comite

                    for(k = 0; k < commetteeSize; k++){
                        testClassification = comite[k].sAbDGVertexClassifier(matrizTreino);

                        // correctClassification[][k] = 0;
                        for(int c = 0; c < testClassification.length; c++){
                            for(int j = 1; j < nroClasses + 1; j++)
                                // comiteOutput[c][j] += testClassification[c][j];
                                comiteOutput[c][(int)testClassification[c][0]]++;// comiteOutput[c][(int)testClassification[c][0]] += testClassification[c][1]; // comite[k].getClassAcc(); //  //
                            if(testClassification[c][0] == matrizTreino[c][coll-1])    // para encontrar pior arvore
                                correctClassification[k][i]++;
                        }

                        //   System.out.println("Acc " + comite[k].getClassAcc());
                        comite[k].updateAccClassifier();
                    }

                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    for(int a = 0; a < contTreino; a++) {
                        maior = comiteOutput[a][0];
                        indMaior = 0;
                        for (int b = 1; b < nroClasses + 1; b++)
                            if (comiteOutput[a][b] > maior) {
                                maior = comiteOutput[a][b];
                                indMaior = b;
                            }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != indMaior)) {        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        } else {
                            weights[a] = 1;
                            erro = 0;

                        }

                        mcnemar[contPrequential] = erro;
                        // PREQUENTIAL with fading factor

                        // prequential para todos os valores
                        //  prequential[contPrequential] = (erro + (contPrequential-1)*prequential[contPrequential-1])/(double)contPrequential;

                        // prequential de 100 em 100
                        if(contPrequential % 100 == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[contPrequential / 100] = (somaNom / somaDenom);
                        }


                        // prequential window
                        //      contPreqW = ((contPrequential - 1) % NumPrev);
                        //       SpreqW = SpreqW - preqWindow[contPreqW] + erro;
                        //       preqWindow[contPreqW] = erro;
                        //       preqWEst[contPrequential] = (SpreqW/(double)Math.min(NumPrev,contPrequential));

                    }

                    for(int e = 0; e < fold; e++)
                        if(weights[e] != 1)     // erro
                            weights[e] = (1 - erroComiteTreino)/erroComiteTreino;

                    weights = normalizaPesos(weights);    // normaliza peso de instancias


                    // #####################################################################################################
                    for(int j = 0; j < commetteeSize; j++) {
                        // comite[j].updateFadingFactor(boosting(matrizTreino,weights));
                        comite[j].updateFadingFactor(matrizTreino);   // boosting(treino)
                        //  comite[j].batchUpdateIntConf(boosting(matrizTreino,weights));  // short memory batchUpdate(matrizTreino);
                    }

                    saidaTotal[i][0] = i+1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum/(double)contTreino) * 100;
                    somaErro += errorSum/(double)contTreino * 100;
                    somaErroQuad += Math.pow(errorSum/(double)contTreino,2);

                }



            }  // fim it

        }  // fim rep


        mediaErro = (somaErro/(double)((it-1)*rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "SEA_GRANU.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);



        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro,2)/(double)it*rep))/(double)(it*rep-1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        //  for(int s = 0; s < it; s++)
        //     out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        // out.println("################################################################################");

        for(int s = 0; s < prequential.length; s++)
            out.println( prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/



/*
        // rodar uma unica vez
        PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "SEA_PREQ.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < prequential.length; t++)
            out2.println(prequential[t]);

        out2.close();


        */

        for (int c = 0; c < commetteeSize; c++)
            for(int d = 0; d < it; d++)
                System.out.println((c+3) + " " +  (d+1) + " " + correctClassification[c][d]); //comite[c].getClassAcc());


        return mediaErro;
    }

    public double[][] boosting(double[][] A, double[] w){    // retorna um conjunto boosting, ponderado por w
        // Bertini
        // shuffle?
        int line = w.length, meio = 0, meioAnt = -1;
        int coll = A[0].length;
        int i1 = 0, i2 = line-1;
        double[] cumW = new double[line]; // cumulative W - w_i � a soma de W_0 a W_i
        double prob = 0;
        double[][] baggA = new double[line][coll];

        cumW[0] = w[0];
        for(int i = 1; i < line; i++)         // soma cumulativa, valor sorteado deve estar em Wi < s =< Wi+1
            cumW[i] = cumW[i-1] + w[i];

        for(int i = 0; i < line; i++){
            prob = Math.random();
            i1 = 0; i2 = line-1;
            meioAnt = -1;
            while(meio != meioAnt){      // encontra indice correspondente ao valor sorteado
                meioAnt = meio;
                meio = (i2+i1)/2;
                if(cumW[meio] < prob)
                    i1 = meio;
                if(cumW[meio] >= prob)
                    i2 = meio;
            }

            for(int j = 0; j < coll; j++)
                baggA[i][j] = A[i2][j];

        }


        return baggA;
    }




    public double sAbDGEnsemble(int commetteeSize, int NumPrev, String auxFileName, int len, int rep){

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[] testClassification;
        int[] correctClassification = new int[commetteeSize];
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i=0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;


        double[][] m;

        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        double[] prequential = new double[(len - NumPrev)/100 + 1];
        double somaNom, somaDenom, alpha = 0.998;

        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if( line % NumPrev == 0)
            it = (int)Math.ceil(line/NumPrev);
        else
            it = (int)Math.ceil(line/NumPrev) + 1;                  // define numero de itera��es

        double[][] saidaTotal = new double[it][2];


        for(int r = 0; r < rep; r++){

            m = SEAconcept(line); //hyperplane(line);
            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            int contPrequential = 0;

            for (i = 0; i < it; i++) {
                if(i==0){
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for(int h = 0; h < trainFold; h++){
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for(int y = 0; y < coll; y++ ){
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    //    matrizTreino = normalizaPeloMaior(matrizTreino);
                    comite[i] = new SimplifiedAbDG(matrizTreino,Classes);    // nova arvore
                    contaRedesCriadas++;
                }
                else{

                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    contTreino = 0;

                    // train folds
                    i1 = i*fold;
                    i2 = (i+1)*fold;
                    if(i == (it-1)){        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it-1)*NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for(int h = i1; h < i2; h++){
                        if((h >= (i*fold) && (h < i2))){              //if(h < i*fold + testFold){
                            for(int y = 0; y < coll; y++ )
                                matrizTreino[contTreino][y] = m[h][y];
                            contTreino++;
                        }
                    }

                    double[][] comiteOutput = new double[contTreino][nroClasses+1];  // resultado do comite

                    if(i < commetteeSize){             // vetor de arvores ainda n�o atingiu limite maximo
                        redesNoComite++;

                        for(k = 0; k < i; k++){
                            testClassification = comite[k].sAbDGClassifier(matrizTreino);
                            teTimeD = System.currentTimeMillis();

                            for(int c = 0; c < testClassification.length; c++)
                                comiteOutput[c][(int)testClassification[c]]++;

                        }
                        comite[i] = new SimplifiedAbDG(matrizTreino, Classes);

                        contaRedesCriadas++;
                    }
                    else{                               // vetor de redes esta completo - substitui pior rede
                        redesNoComite = commetteeSize;

                        pior = 0;

                        for(k = 0; k < commetteeSize; k++){
                            testClassification = comite[k].sAbDGClassifier(matrizTreino);

                            correctClassification[k] = 0;
                            for(int c = 0; c < testClassification.length; c++){
                                comiteOutput[c][(int)testClassification[c]]++;
                                if(testClassification[c] == matrizTreino[c][coll-1])    // para encontrar pior arvore
                                    correctClassification[k]++;
                            }

                        }
                        //  System.out.println(pior);
                        // encontra pior
                        double menor = 100;
                        for(int p = 0; p < commetteeSize; p++)
                            if(correctClassification[p] < menor){
                                menor = correctClassification[p];
                                pior = p;
                            }

                        if(pior != -1){            // troca pior rede
                            comite[pior] = null;
                            comite[pior] = new SimplifiedAbDG(matrizTreino,Classes);    // nova arvore

                            contaRedesCriadas++;
                        }
                    }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    for(int a = 0; a < contTreino; a++) {
                        maior = comiteOutput[a][0];
                        indMaior = 0;
                        for (int b = 1; b < nroClasses + 1; b++)
                            if (comiteOutput[a][b] > maior) {
                                maior = comiteOutput[a][b];
                                indMaior = b;
                            }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != indMaior)) {        // classes sao 1 e 2
                            errorSum++;
                            erro = 1;

                        } else {
                            erro = 0;

                        }


                        mcnemar[contPrequential] = erro;
                        // PREQUENTIAL with fading factor


                        if(contPrequential % 100 == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[contPrequential / 100] = (somaNom / somaDenom);
                        }


                        // prequential window
                        //      contPreqW = ((contPrequential - 1) % NumPrev);
                        //       SpreqW = SpreqW - preqWindow[contPreqW] + erro;
                        //       preqWindow[contPreqW] = erro;
                        //       preqWEst[contPrequential] = (SpreqW/(double)Math.min(NumPrev,contPrequential));

                    }

                    saidaTotal[i][0] = i+1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum/(double)contTreino) * 100;
                    somaErro += errorSum/(double)contTreino * 100;
                    somaErroQuad += Math.pow(errorSum/(double)contTreino,2);

                }



            }  // fim it

        }  // fim rep


        mediaErro = (somaErro/(double)((it-1)*rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREE.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }

        out = new PrintWriter(outputStream);



        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro,2)/(double)it*rep))/(double)(it*rep-1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        for(int s = 0; s < it; s++)
            out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        out.println("################################################################################");

        //    for(int s = 0; s < prequential.length; s++)
        //       out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

/*        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
*/




        // rodar uma unica vez
        PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "SEA_PREQ.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < prequential.length; t++)
            out2.println(prequential[t]);

        out2.close();




        return mediaErro;
    }



    public double sAbDGEnsemble(double[][] m, int commetteeSize, int NumPrev, String auxFileName, int rep){

        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[] testClassification;
        int[] correctClassification = new int[commetteeSize];
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i=0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaTempoTreino = 0, somaTempoTeste = 0, somaQuadTempoTreino = 0, somaQuadTempoTeste = 0;
        double desvioTempoTreino, desvioTempoTeste;


        int len = m.length;
        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro;
        double[] preqWEst = new double[len - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        double[] prequential = new double[(len - NumPrev)/100 + 1];
        double somaNom, somaDenom, alpha = 0.998;

        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if( line % NumPrev == 0)
            it = (int)Math.ceil(line/NumPrev);
        else
            it = (int)Math.ceil(line/NumPrev) + 1;                  // define numero de itera��es

        double[][] saidaTotal = new double[it][2];


        for(int r = 0; r < rep; r++){

            //  m = hyperplane(line);
            int coll = m[0].length;

            comite = new SimplifiedAbDG[commetteeSize];
            double SpreqW = 0;
            int contPreqW = 0;
            int contPrequential = 0;

            for (i = 0; i < it; i++) {
                if(i==0){
                    redesNoComite = 0;
                    trainFold = NumPrev; //  numero de instancias no primeiro conjunto, no caso deve ser igual a N
                    matrizTreino = new double[trainFold][coll];
                    contTreino = 0;
                    for(int h = 0; h < trainFold; h++){
                        //   if((h >= 0) && (h < trainFold)){            //if(h < i*fold + testFold){
                        for(int y = 0; y < coll; y++ ){
                            matrizTreino[contTreino][y] = m[h][y];
                        }
                        contTreino++;
                        //     }
                    }

                    //    matrizTreino = normalizaPeloMaior(matrizTreino);
                    comite[i] = new SimplifiedAbDG(matrizTreino,Classes);    // nova arvore
                    contaRedesCriadas++;
                }
                else{
                    fold = NumPrev;  // tamanho dos demais conjuntos

                    matrizTreino = new double[fold][coll];
                    contTreino = 0;

                    // train folds
                    i1 = i*fold;
                    i2 = (i+1)*fold;
                    if(i == (it-1)){        // ultimo conjunto pode ser menor que demais
                        i2 = line;
                        NumPrev = line - (it-1)*NumPrev;
                        matrizTreino = new double[NumPrev][coll];
                    }

                    for(int h = i1; h < i2; h++){
                        if((h >= (i*fold) && (h < i2))){              //if(h < i*fold + testFold){
                            for(int y = 0; y < coll; y++ )
                                matrizTreino[contTreino][y] = m[h][y];
                            contTreino++;
                        }
                    }


                    double[][] comiteOutput = new double[contTreino][nroClasses+1];  // resultado do comite

                    if(i < commetteeSize){             // vetor de arvores ainda n�o atingiu limite maximo
                        redesNoComite++;

                        for(k = 0; k < i; k++){
                            testClassification = comite[k].sAbDGClassifier(matrizTreino);
                            teTimeD = System.currentTimeMillis();

                            for(int c = 0; c < testClassification.length; c++)
                                comiteOutput[c][(int)testClassification[c]]++;

                        }
                        comite[i] = new SimplifiedAbDG(matrizTreino, Classes);

                        contaRedesCriadas++;
                    }
                    else{                               // vetor de redes esta completo - substitui pior rede
                        redesNoComite = commetteeSize;

                        pior = 0;

                        for(k = 0; k < commetteeSize; k++){
                            testClassification = comite[k].sAbDGClassifier(matrizTreino);

                            correctClassification[k] = 0;
                            for(int c = 0; c < testClassification.length; c++){
                                comiteOutput[c][(int)testClassification[c]]++;
                                if(testClassification[c] == matrizTreino[c][coll-1])    // para encontrar pior arvore
                                    correctClassification[k]++;
                            }

                        }
                        //  System.out.println(pior);
                        // encontra pior
                        double menor = 100;
                        for(int p = 0; p < commetteeSize; p++)
                            if(correctClassification[p] < menor){
                                menor = correctClassification[p];
                                pior = p;
                            }

                        if(pior != -1){            // troca pior rede
                            comite[pior] = null;
                            comite[pior] = new SimplifiedAbDG(matrizTreino,Classes);    // nova arvore

                                contaRedesCriadas++;
                        }
                    }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    for(int a = 0; a < contTreino; a++) {
                        maior = comiteOutput[a][0];
                        indMaior = 0;
                        for (int b = 1; b < nroClasses + 1; b++)
                            if (comiteOutput[a][b] > maior) {
                                maior = comiteOutput[a][b];
                                indMaior = b;
                            }

                        contPrequential++;     // i em gama13
                        if ((matrizTreino[a][coll - 1] != indMaior)) {        // classes sao 1 e 2
                            errorSum++;
                            erro = 1;

                        } else {
                            erro = 0;

                        }


                        mcnemar[contPrequential] = erro;
                        // PREQUENTIAL with fading factor


                        if(contPrequential % 100 == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[contPrequential / 100] = (somaNom / somaDenom);
                        }


                        // prequential window
                        //      contPreqW = ((contPrequential - 1) % NumPrev);
                        //       SpreqW = SpreqW - preqWindow[contPreqW] + erro;
                        //       preqWindow[contPreqW] = erro;
                        //       preqWEst[contPrequential] = (SpreqW/(double)Math.min(NumPrev,contPrequential));

                    }

                    saidaTotal[i][0] = i+1;//*NumPrev;
                    saidaTotal[i][1] += (errorSum/(double)contTreino) * 100;
                    somaErro += errorSum/(double)contTreino * 100;
                    somaErroQuad += Math.pow(errorSum/(double)contTreino,2);

                }



            }  // fim it

        }  // fim rep


        mediaErro = (somaErro/(double)((it-1)*rep));


        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREE.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out = new PrintWriter(outputStream);



        desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro,2)/(double)it*rep))/(double)(it*rep-1));
        out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);

        for(int s = 0; s < it; s++)
           out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

        out.println("################################################################################");

        //    for(int s = 0; s < prequential.length; s++)
        //       out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez

        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();





        // rodar uma unica vez
        PrintWriter out2;
        FileOutputStream outputStream2 = null;
        try {
            outputStream2 = new FileOutputStream ("C:\\Java\\EnsembleStream\\Results\\" + auxFileName + commetteeSize + NumPrev + "SEA_PREQ.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out2 = new PrintWriter(outputStream2);


        for(int t = 0; t < prequential.length; t++)
            out2.println(prequential[t]);

        out2.close();




        return mediaErro;
    }


    public double sAbDGEnsembleChu(int commetteeSize, int NumPrev,String auxFileName, int len, int rep){
        // comite baseado em boosting proposto em 'Fast and Light Boosting for Adaptive Mining of Data Streams'
        int trainFold;
        int fold;
        double[][] matrizTreino = null;
        double[][] matrizTeste = null;
        double[] testClassification;
        int[] correctClassification = new int[commetteeSize];
        int pior = -1, cont = 0;
        double redesNoComite = 0;
        int contaRedesCriadas = 0;
        double somaNeuronios = 0, somaQuadNeuronios = 0;
        double desvioNeuronios = 0;
        int contTreino = 0, indMaior;
        int k, i=0, i1 = 0, i2 = 0;
        double errorSum = 0, mediaErro = 0;
        double maior = 0;
        // double[] saida = new double[NumPrev];
        //  double[] testErStats = new double[NumPrev];
        double somaErro = 0, somaErroQuad = 0, desvio = 0;
        long trTimeA = 0, trTimeB = 0, teTimeC = 0, teTimeD = 0;
        double somaErroAtual = 0;

        double[] mcnemar = new double[len - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro;
        double[] prequential = new double[(len - NumPrev)/100 + 1];
        double somaNom, somaDenom, alpha = 0.998;

        int olderBase = 0, bases = 0;

        double[] weights;
        double acc = 0, menor, erroComiteTreino;
        double[] classes;

        double[][] m;

        //  m = incluiBias(m);
        int line = len;

        int it = 0;
        if( line % NumPrev == 0)
            it = (int)Math.ceil(line/NumPrev);
        else
            it = (int)Math.ceil(line/NumPrev) + 1;                  // define numero de itera��es

        double[][] saidaTotal = new double[it][2];



        for(int r = 0; r < rep; r++){

            m = SEAconcept(line);

            int coll = m[0].length;


            comite = new SimplifiedAbDG[commetteeSize];
            double[] commeteeAccuracies = new double[it];
            bases = 0;
            double SpreqW = 0;
            int contPreqW = 0;
            int contPrequential = 0, classifs = 0;
            olderBase = 0;


            for(i = 0; i < it ; i++){         // numero de folds         colocar 50

                //   System.out.println(bases);

                if(bases == 0){

                    fold = NumPrev;
                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    classes = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i*fold;
                    i2 = (i+1)*fold;

                    for(int h = i1; h < i2; h++){
                        if((h >= (i*fold) && (h < i2))){              //if(h < i*fold + testFold){
                            for(int y = 0; y < coll; y++ )
                                matrizTreino[contTreino][y] = m[h][y];
                            classes[contTreino] = m[h][coll-1];
                            weights[contTreino] = 1/(double)fold;
                            contTreino++;
                        }
                    }


                    comite[i] = new SimplifiedAbDG(matrizTreino, Classes);
                    testClassification = comite[i].sAbDGClassifier(matrizTreino);
                    comite[bases].setWeight(calculateBaseWeight(testClassification,classes,weights));   // define peso do classificador atual

                    //     somaNeuronios += this.commetteClassificacao[bases].getNroNeuronio();
                    //     somaQuadNeuronios += Math.pow(this.commetteClassificacao[bases].getNroNeuronio(),2);
                    contaRedesCriadas++;
                    bases++;

                    if(i > 0){
                        saidaTotal[i][0] = i+1;
                        saidaTotal[i][1] += 100;
                        somaErro += 100;
                    }


                }
                else{                                       // i != 0
                    fold = NumPrev;
                    matrizTreino = new double[fold][coll];
                    weights = new double[fold];
                    classes = new double[fold];
                    contTreino = 0;

                    // train folds
                    i1 = i*fold;
                    i2 = (i+1)*fold;

                    for(int h = i1; h < i2; h++){
                        if((h >= (i*fold) && (h < i2))){              //if(h < i*fold + testFold){
                            for(int y = 0; y < coll; y++ )
                                matrizTreino[contTreino][y] = m[h][y];
                            classes[contTreino] = m[h][coll-1];
                            weights[contTreino] = 1/(double)fold;
                            contTreino++;
                        }
                    }


                    double[][] comiteOutput = new double[contTreino][nroClasses];  // p/ duas classes
                    double aux;
                    // RESOLVE COMITE

                    for(k = 0; k < Math.min(bases,commetteeSize); k++){
                        testClassification = comite[k].sAbDGClassifier(matrizTreino);


                        for(int c = 0; c < testClassification.length; c++)
                            if(comite[k].getWeight() > 0)
                                comiteOutput[c][((int)testClassification[c] == 1) ? 1 : 0] += Math.abs(Math.log10(1/comite[k].getWeight()));
                            else
                                comiteOutput[c][((int)testClassification[c] == 1) ? 1 : 0] += (Math.log10(10000));
                    }                                                                 //  ABS - - --


                    errorSum = 0;                                   // resolve comite
                    erroComiteTreino = 0;
                    maior = 0;
                    indMaior = 0;
                    for(int a = 0; a < contTreino; a++){
                        maior = comiteOutput[a][0];
                        indMaior = 0;
                        for(int b = 1; b < nroClasses; b++)
                            if(comiteOutput[a][b] > maior){
                                maior = comiteOutput[a][b];
                                indMaior = b;
                            }

                        contPrequential++;     // i em gama13
                        if((matrizTreino[a][coll-1] == 2 && indMaior == 1) || (matrizTreino[a][coll-1] == 1 && indMaior == 0)){        // classes sao 1 e 2
                            erroComiteTreino += weights[a];
                            errorSum++;
                            erro = 1;

                        }
                        else{
                            weights[a] = 1;
                            erro = 0;

                        }

                        mcnemar[contPrequential] = erro;
                        // PREQUENTIALS
                        // prequential all
                        if(contPrequential % 100 == 0) {
                            somaNom = 0;
                            somaDenom = 0;

                            for (int p = 1; p <= contPrequential; p++) {
                                somaNom += Math.pow(alpha, ((double) contPrequential - p)) * mcnemar[p] * 100;
                                somaDenom += Math.pow(alpha, ((double) contPrequential - p));
                            }

                            prequential[contPrequential / 100] = (somaNom / somaDenom);
                        }
                    }

                    saidaTotal[i][0] = i+1;//*NumPrev;
                    somaErroAtual = errorSum/(double)contTreino *100;
                    saidaTotal[i][1] += somaErroAtual;
                    somaErro += somaErroAtual;

                    commeteeAccuracies[i] = (100 - somaErroAtual)/100;     // acuracia do comite para o conjunto i
                    classifs++;
                    // if(bases > 3)
                    if(changeDetection(commeteeAccuracies,i,bases)){            // detec��o de mudan�a
                        //  System.out.println("Mudan�a Abrupta " + i);

                        for(int j = 0; j < bases; j++)
                            comite[j] = null;       // remove todas as redes

                        for(int a = 0; a < it; a++)
                            commeteeAccuracies[a] = 0;

                        bases = 0;
                        olderBase = 0;
                        classifs = 0;


                    }
                  /*  else if(changeDetection2(commeteeAccuracies,i,classifs)){
                        for(int j = 0; j < bases; j++)
                            comite[j] = null;       // remove todas as redes

                        for(int a = 0; a < it; a++)
                            commeteeAccuracies[a] = 0;

                        bases = 0;
                        olderBase = 0;
                        classifs = 0;

                    }
                  */  else{

                        // considerando que n�o ocorreu mudan�a
                        for(int e = 0; e < fold; e++)
                            if(weights[e] != 1)     // erro
                                weights[e] = (1 - erroComiteTreino)/erroComiteTreino;

                        weights = normalizaPesos(weights);    // normaliza peso

                        // treinar novo classificador base usando weights

                        if(bases < commetteeSize){             // vetor de arvores ainda n�o atingiu limite maximo

                            comite[i] = new SimplifiedAbDG(matrizTreino, Classes);
                            testClassification = comite[i].sAbDGClassifier(matrizTreino);
                            comite[bases].setWeight(calculateBaseWeight(testClassification,classes,weights));

                                                  // somaNeuronios += this.commetteClassificacao[bases].getNroNeuronio();
                            // somaQuadNeuronios += Math.pow(this.commetteClassificacao[bases].getNroNeuronio(),2);
                            contaRedesCriadas++;
                            bases++;

                        }
                        else {
                            // retira o classificador mais antigo
                            comite[olderBase] = null;
                            comite[olderBase] = new SimplifiedAbDG(matrizTreino, Classes);
                            testClassification = comite[olderBase].sAbDGClassifier(matrizTreino);
                            comite[olderBase].setWeight(calculateBaseWeight(testClassification,classes,weights));


                            //  somaNeuronios += this.commetteClassificacao[olderBase].getNroNeuronio();
                            //  somaQuadNeuronios += Math.pow(this.commetteClassificacao[olderBase].getNroNeuronio(),2);
                            contaRedesCriadas++;

                            if(olderBase < commetteeSize-1)      // atualiza membro mais velho do comite
                                olderBase++;
                            else
                                olderBase = 0;
                        }

                    }
                    // ESTATISTICA DE TEMPO
                }
            }

        }


        mediaErro = (somaErro/(double)((it-1)*rep));

        //  System.out.println(mediaErro);
        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "CHU_PREQ_TREE.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out = new PrintWriter(outputStream);



        // desvio = Math.sqrt((somaErroQuad - (Math.pow(somaErro,2)/(double)line))/(double)(line-1));
        //out.println("Erro medio " + mediaErro + " Desvio " + desvio);

        //     desvioNeuronios = Math.sqrt((somaQuadNeuronios - (Math.pow(somaNeuronios,2)/(double)contaRedesCriadas))/(double)(contaRedesCriadas-1));
        //     out.println("Numero medio neuronios " + somaNeuronios/(double)contaRedesCriadas + " Desvio " + desvioNeuronios);



        for(int s = 0; s < prequential.length; s++)
            out.println(prequential[s]);

        //    for(int s = 0; s < prequential.length; s++)
        //       out.println(prequential[s]);

        //     out.println("################################################################################");
        //     for(int s = 0; s < prequential.length; s++)
        //         out.println(preqWEst[s]);


        out.close();


        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "CHU_TREE_McNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();
        ///////

        return mediaErro;
    }





    public double calculateBaseWeight(double[] pred, double[] A, double[] w){    // Chu

        int line = A.length;
        double erro = 0;


        for(int i = 0; i < line; i++)
            if(pred[i] != A[i])
                erro += w[i];

        return erro/(1-erro);

    }

    public double[] normalizaPesos(double[] pesos){      // usado em comit� do Chu

        int line = pesos.length;
        double soma = 0;

        for(int i = 0; i < line; i++)
            soma += pesos[i];

        for(int i = 0; i < line; i++)
            pesos[i] /= soma;



        return pesos;

    }

    public boolean changeDetection(double[] accs, int actual, int numBases){   // passa o vetor como acuracias e a acuracia no conjunto atual
        // Chu
        if(numBases < 4)
            return false;
        // teste verifica se acuracia atual difere das outras
        int line = accs.length;
        double soma = 0, media = 0, var = 0, somaQuad = 0, cont = 0;
        double thrs = 0.25;
        int i1 = 1, i2 = actual;                                                 // definir threshold  #################################
        int winSize = 20;
        if(actual > winSize && actual < line){
            i1 = actual - winSize;
            i2 = actual;
        }

        for(int i = i1; i < i2; i++)
            if(i != actual && accs[i] != 0){
                soma += accs[i];
                cont++;
            }

        media = soma/cont;

        for(int i = i1; i < i2; i++)
            if(accs[i] != 0)
                somaQuad += Math.pow(accs[i] - media,2);

        var = 1/(cont - 1)*somaQuad;

        // test

        double aux = 0.5 * Math.sqrt(2*Math.PI*var);
        double auxE = Math.exp(-1*((accs[actual] - media)/2*var));
        double pdf = aux*auxE;

        //   System.out.println(actual + " " + pdf);

        if(pdf >= thrs)
            return true;           // teste para mudan�a abrupta
        else{
            return false;
        }

    }


    public double calculateWeight(double acc, int[] nclass){    // WCEA

        double msei = 1 - acc, mser = 0, w = 0;
        double total = 0;

        for(int i = 0; i < nclass.length; i++)
            total += nclass[i];

        for(int i = 0; i < nclass.length; i++)
            mser += (nclass[i]/total)*Math.pow((1 - nclass[i]/total),2);

        w = mser - msei;

        //   System.out.println(w);
        return w;
    }



    public double[][] sineData(int line){

        Random random = new Random();
        int coll = 3;
        int part = line/4;
        double[][] matriz = new double[line][coll];
        boolean key = false;
        double seno = 0;

        for(int i = 0; i < line; i++){


            if(i % part == 0 && i != 0 && i != 1){         // troca conceito
                key = !key;
            }


            for(int j = 0; j < coll-1; j++)
                matriz[i][j] = random.nextDouble();

            seno = 0.5 + 0.3*Math.sin(3*Math.PI*matriz[i][0]);

            if(key){
                if(matriz[i][1] < seno)
                    matriz[i][coll-1] = 1;
                else
                    matriz[i][coll-1] = 2;
            }

            else{
                if(matriz[i][1] < seno)
                    matriz[i][coll-1] = 2;
                else
                    matriz[i][coll-1] = 1;
            }

        }

        return matriz;
    }

    public double[][] circleData(int line){

        Random random = new Random();
        int coll = 3;
        int part = line/4;
        double[][] matriz = new double[line][coll];
        double a = 0.2, b = 0.5, r = 0.15, soma = 0;


        attributeType = new char[3];  //########################################################

        attributeType[0] = 'n';
        attributeType[1] = 'n';
        attributeType[2] = 'y';

        for(int i = 0; i < line; i++){
            if(i % part == 0 && i != 0 && i != 1){         // troca conceito
                a += 0.2;
                r += 0.05;
            }


            for(int j = 0; j < coll-1; j++)
                matriz[i][j] = random.nextDouble();

            soma = Math.pow((matriz[i][0] - a),2) + Math.pow((matriz[i][1] - b),2);


            if(soma > Math.pow(r,2))
                matriz[i][coll-1] = 1;
            else
                matriz[i][coll-1] = 2;
        }

        return matriz;

    }

    public double[][] gaussData(int line){

        int coll = 3;
        int part = line/4;
        double[][] matriz = new double[line][coll];
        Random random = new Random();
        boolean key = true;
        double prob = 0;

        attributeType = new char[3];  //########################################################

        attributeType[0] = 'n';
        attributeType[1] = 'n';
        attributeType[2] = 'y';

        for(int i = 0; i < line; i++){

            if(i % part == 0 && i != 0 && i != 1){         // troca conceito
                key = !key;
            }

            prob = Math.random();

            if(prob > 0.5){

                matriz[i][0] = random.nextGaussian();
                matriz[i][1] = random.nextGaussian();
                if(key)
                    matriz[i][2] = 1;
                else
                    matriz[i][2] = 2;
            }
            else{
                matriz[i][0] = 2 + random.nextGaussian()*4;
                matriz[i][1] = random.nextGaussian()*4;
                if(key)
                    matriz[i][2] = 2;
                else
                    matriz[i][2] = 1;
            }
        }

        return matriz;

    }

    public double[][] mixedData(int line){

        Random random = new Random();
        int coll = 5;
        int part = line/4;
        double[][] matriz = new double[line][coll];
        boolean key = false;
        double seno = 0;

        for(int i = 0; i < line; i++){


            if(i % part == 0 && i != 0 && i != 1){         // troca conceito
                key = !key;
            }

            for(int k = 0; k < 2; k++)
                if(Math.random() > 0.5)
                    matriz[i][k] = 1;
                else
                    matriz[i][k] = -1;

            for(int j = 2; j < coll-1; j++)
                matriz[i][j] = random.nextDouble();

            seno = 0.5 + 0.3*Math.sin(3*Math.PI*matriz[i][3]);

            if(key){
                if(matriz[i][0] < seno || matriz[i][1] < seno || matriz[i][2] < seno)
                    matriz[i][coll-1] = 1;
                else
                    matriz[i][coll-1] = 2;
            }

            else{
                if(matriz[i][0] < seno || matriz[i][1] < seno || matriz[i][2] < seno)
                    matriz[i][coll-1] = 2;
                else
                    matriz[i][coll-1] = 1;
            }

        }

        return matriz;

    }

    public double[][] hyperplane(int line){


        int coll = 11;        // + 1 for onlineTRee2
        int part = line/8;

        double[][] matriz = new double[line][coll];
        double[] weights = new double[10];
        Random random = new Random();
        double signal, soma = 0, thr;
        double[] vetSignals = new double[10];
        double prodInt = 0;
        int k = 5; // numero de dimensoes que sofrem altera��o


        attributeType = new char[11];  //########################################################

        for(int a = 0; a < coll-1; a++)
            attributeType[a] = 'n';
        attributeType[coll-1] = 'y';

        for(int w = 0; w < 10; w++){
            weights[w] = Math.random();          // pesos aleatorios
            soma += weights[w];
            if(Math.random() > 0.5)
                vetSignals[w] = 1;
            else
                vetSignals[w] = -1;
        }


        for(int i = 0; i < line; i++){

            prodInt = 0;
            for(int j = 0; j < coll-1; j++){
                matriz[i][j] = Math.random();        // dist gaussiana
                prodInt += matriz[i][j] * weights[j];
            }

            soma = 0;
            for(int h = 0; h < weights.length; h++)
                soma += weights[h];

            thr = (soma/2);

            if(prodInt >= thr)
                matriz[i][coll-1] = 1;                          // onlineTREE
            else
                matriz[i][coll-1] = 2;


            for(int d = 0; d < k; d++)
                weights[d] += 0.0001*vetSignals[d];           //  gradual drift


            if(i % part == 0 && i != 0 && i != 1){

                for(int w = 0; w < k; w++)                // change hiperplane moving direction
                    if(Math.random() <= 0.1)
                        vetSignals[w] *= -1;

            }

            //         for(int r = 0; r < weights.length; r++)
            //           System.out.println(i + "  " + weights[r]);

        }

        return matriz;
    }

    public void hyperplaneRegression(int line){  // gradual proposto por Kolter
        // para o paper do ASOC - só cria o data set

        int coll = 11;        // + 1 for onlineTRee2
        int part = line/4;

        double[][] matriz = new double[line][coll];
        double[] weights = new double[10];
        Random random = new Random();
        double signal, soma = 0, thr;
        double[] vetSignals = new double[10];
        double prodInt = 0;
        int k = 5; // numero de dimensoes que sofrem altera��o


        for(int i = 0; i < line; i++) {

            for (int j = 0; j < coll - 1; j++) {
                matriz[i][j] = Math.random();
            }

            if (i <= line / 4)
                matriz[i][coll - 1] = (matriz[i][0] + matriz[i][1] + matriz[i][2]) / 3;
            else if (i > line / 4 && i <= line / 2)
                matriz[i][coll - 1] = (matriz[i][1] + matriz[i][2] + matriz[i][3]) / 3;
            else if (i > line / 2 && i <= 3 * (line / 4))
                matriz[i][coll - 1] = (matriz[i][3] + matriz[i][4] + matriz[i][5]) / 3;
            else
                matriz[i][coll - 1] = (matriz[i][6] + matriz[i][7] + matriz[i][8]) / 3;


            if(Math.random() <= 0.1){  // insere ruido  +- .1
                if(matriz[i][coll - 1] < 0.9 && matriz[i][coll - 1] > 0.1){
                    if(Math.random() > 0.5)
                        matriz[i][coll-1]+=0.1;
                    else
                        matriz[i][coll-1]-=0.1;
                }

            }

        }


        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\HyperplaneRegression.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out = new PrintWriter(outputStream);


        for(int s = 0; s < line; s++) {
            for (int r = 0; r < coll; r++)
                out.print(matriz[s][r] + " ");

            out.println();
        }

        out.close();


    }

    public void hyperplaneRegressionAbp1(int line){  // abrupto
        // para o paper do ASOC - só cria o data set

        int coll = 11;        // + 1 for onlineTRee2
        int part = line/4;

        double[][] matriz = new double[line][coll];
        double[] weights = new double[10];
        Random random = new Random();
        double signal, soma = 0, thr;
        double[] vetSignals = new double[10];
        double prodInt = 0;
        int k = 5; // numero de dimensoes que sofrem altera��o


        for(int i = 0; i < line; i++) {

            for (int j = 0; j < coll - 1; j++) {
                matriz[i][j] = Math.random();
            }

            if (i <= line / 4)
                matriz[i][coll - 1] = (matriz[i][0] + matriz[i][1] + matriz[i][2]) / 3;
            else if (i > line / 4 && i <= line / 2)
                matriz[i][coll - 1] = (matriz[i][3] + matriz[i][4] + matriz[i][5]) / 3;
            else if (i > line / 2 && i <= 3 * (line / 4))
                matriz[i][coll - 1] = (matriz[i][0] + matriz[i][1] + matriz[i][2]) / 3;
            else
                matriz[i][coll - 1] = (matriz[i][6] + matriz[i][7] + matriz[i][8]) / 3;


            if(Math.random() <= 0.1){  // insere ruido  +- .1
                if(matriz[i][coll - 1] < 0.9 && matriz[i][coll - 1] > 0.1){
                    if(Math.random() > 0.5)
                        matriz[i][coll-1]+=0.1;
                    else
                        matriz[i][coll-1]-=0.1;
                }

            }

        }


        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\HyperplaneRegression.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out = new PrintWriter(outputStream);


        for(int s = 0; s < line; s++) {
            for (int r = 0; r < coll; r++)
                out.print(matriz[s][r] + " ");

            out.println();
        }

        out.close();


    }

    public void hyperplaneRegressionAbp2(int line){  // abrupto
        // para o paper do ASOC - só cria o data set

        int coll = 11;        // + 1 for onlineTRee2
        int part = line/4;

        double[][] matriz = new double[line][coll];
        double[] weights = new double[10];
        Random random = new Random();
        double signal, soma = 0, thr;
        double[] vetSignals = new double[10];
        double prodInt = 0;
        int k = 5; // numero de dimensoes que sofrem altera��o


        for(int i = 0; i < line; i++) {

            for (int j = 0; j < coll - 1; j++) {
                if(j < 3)               // 0 1 2
                    matriz[i][j] = Math.random()*0.5;
                else if(j > 2 && j < 6)  //  3 4 5
                    matriz[i][j] = 0.5 + Math.random()*0.5;
                else
                    matriz[i][j] = Math.random();
            }

            if (i <= line / 4)
                matriz[i][coll - 1] = (matriz[i][0] + matriz[i][1] + matriz[i][2]) / 3;
            else if (i > line / 4 && i <= line / 2)
                matriz[i][coll - 1] = (matriz[i][3] + matriz[i][4] + matriz[i][5]) / 3;
            else if (i > line / 2 && i <= 3 * (line / 4))
                matriz[i][coll - 1] = (matriz[i][0] + matriz[i][1] + matriz[i][2]) / 3;
            else
                matriz[i][coll - 1] = (matriz[i][6] + matriz[i][7] + matriz[i][8]) / 3;


            if(Math.random() <= 0.1){  // insere ruido  +- .1
                if(matriz[i][coll - 1] < 0.9 && matriz[i][coll - 1] > 0.1){
                    if(Math.random() > 0.5)
                        matriz[i][coll-1]+=0.1;
                    else
                        matriz[i][coll-1]-=0.1;
                }

            }

        }


        DecimalFormat dec = new DecimalFormat("0.00");
        // salva resultado em arquivo
        PrintWriter out;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\HyperplaneRegression.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out = new PrintWriter(outputStream);


        for(int s = 0; s < line; s++) {
            for (int r = 0; r < coll; r++)
                out.print(matriz[s][r] + " ");

            out.println();
        }

        out.close();


    }
    public double[][] SEAconcept(int line){

        int cont = 0;
        double prob = 0;
        double[][] sea = new double[line][4];
        int conceptPatterns = line/4;
        double[] conceitos = new double[4];
        conceitos[0] = 8; //9.5;//6;//7;   //8               // artigo ; decrescente ;conceito1
        conceitos[1] = 9; //8;//9;//9;
        conceitos[2] = 7; //6;//4;//6;
        conceitos[3] = 9.5; //4;//8;//9.5;

        attributeType = new char[4];  //########################################################

        for(int a = 0; a < 3; a++)
            attributeType[a] = 'n';
        attributeType[3] = 'y';

        for(int i = 0; i < 4; i++) {
            cont = i*conceptPatterns;
            for(int j = cont; j < (i+1)*conceptPatterns; j++){
                sea[j][0] = Math.random()*10;
                sea[j][1] = Math.random()*10;
                sea[j][2] = Math.random()*10;
                /*
                     if(sea[j][0] < 0.009)                   // para eveita gera��o de E
                        sea[j][0] = Math.random()*10;
                     if(sea[j][1] < 0.009)
                        sea[j][1] = Math.random()*10;
                     if(sea[j][2] < 0.009)
                        sea[j][2] = Math.random()*10;
                */
                prob = Math.random();           // *

                if(prob < 0.9){

                    if(sea[j][0] + sea[j][1] <= conceitos[i]){     // pertence � classe 1
                        sea[j][3] = 1;
                        //  statConcept[i][0]++;
                    }
                    else{
                        sea[j][3] = 2;            // pertence � classe 2
                        //   statConcept[i][1]++;
                    }
                }

                else{    // insere ruido
                    if(sea[j][0] + sea[j][1] <= conceitos[i]){     // ruido
                        sea[j][3] = 2;
                        //   statConcept[i][1]++;
                    }
                    else{
                        sea[j][3] = 1;            // ruido
                        //   statConcept[i][0]++;
                    }
                    //  contRuido++;
                }
            }

            //     System.out.println("porcentagem de ruido " + contRuido/(double)conceptPatterns * 100);
            // contRuido = 0;
            //  System.out.print("Concept " + (i+1) +  "Class 1 " + statConcept[i][0] + " Classe 2 "+ statConcept[i][1]);
            //   System.out.println("Percentages " + statConcept[i][0]/15000 + "  " + statConcept[i][1]/15000);
        }

        return sea;

    }

    public double[][] normalizacaoMaiorMenor(double[][] A){
        double normMatriz[][];
        double maior, menor;
        int line = A.length;
        int coll = A[0].length;
        normMatriz = new double[line][coll];

        for(int q = 0; q < coll - 1; q++) {
            maior = A[0][q];
            menor = A[0][q];
            for(int p = 0; p < line; p++){
                if(A[p][q] > maior)
                    maior = A[p][q];
                if(A[p][q] < menor)
                    menor = A[p][q];
            }

            if(maior == menor) {
                for(int r = 0; r < line; r++)
                    normMatriz[r][q] = 0;
            }
            else{
                for(int r = 0; r < line; r++)
                    normMatriz[r][q] = (A[r][q] - menor) / (maior - menor);

            }
        }
        for(int c = 0; c < line; c++)
            normMatriz[c][coll - 1] = A[c][coll -1];   // coloca a classe

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


        // mostra newE

/*        for(int j = 0; j < line; j++){
       //     System.out.print(mask[j] + "   ");
           for(int k = 0; k < coll; k++)
               System.out.print(newE[j][k] + " ");
            System.out.println();
         }
 */

        return newE;
    }






    public double[] onlineClassification(double[][] testSet, int tree){

        int a = 0, line = testSet.length, coll = testSet[0].length;
        double[] classification = new double[line];
        TreeNode treeNode;
        double acertos = 0;

        for(int i = 0; i < line; i++) {
            treeNode = forest[tree].getRoot();
            while(treeNode.getClasse() == -1) {
                a = treeNode.getAttribute();
                if (attributeType[a] == 'n'){
                    if (testSet[i][a] <= treeNode.getThreshold())
                        treeNode = treeNode.leftNode;
                    else  // if(testSet[i][a] <= treeNode.getThreshold())
                        treeNode = treeNode.rightNode;
                }
                else{
                    if (testSet[i][a] == treeNode.getThreshold())
                        treeNode = treeNode.leftNode;
                    else  // if(testSet[i][a] <= treeNode.getThreshold())
                        treeNode = treeNode.rightNode;
                }
            }
            classification[i] = treeNode.getClasse();
            if(testSet[i][coll-1] == classification[i])
                acertos++;
        }

        forest[tree].setAcc((acertos/line)*100);

        return classification;
    }


    public TreeNode C45(double[][] A){

        double[][] newA, newB;
        TreeNode treeNode;
        double classe = hasOneClass(A);     // retorma -1 se tem mais que uma classe; senão retorna a unica classe

        if(classe == -1){
            treeNode = criaNo(A);
            // false <= thr

            newA = criaTreinamento(A, treeNode.getAttribute(), treeNode.getThreshold(),false);
            newB = criaTreinamento(A, treeNode.getAttribute(), treeNode.getThreshold(),true);

            if(newA.length != 0 && newB.length != 0){

                treeNode.insertAtLeft(C45(newA));
                // true > que thr e != quando categorico
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
        for(int a = 0; a < coll-1; a++) {
            atributoOrdenado = quicksort(selecionaAtributo(A, a), 0, line - 1);
            if (attributeType[a] == 'n'){
                if (atributoOrdenado[0] != atributoOrdenado[line - 1])
                    for (int i = 1; i < line - 1; i++) {
                        particao = (atributoOrdenado[i - 1] + atributoOrdenado[i]) / 2;    // caso atributo seja numerico - continuo
                        T = i;
                        T1 = line - i;
                        gain = info(A, 0, 0, 0) - ((T / line) * info(A, a, particao, 1) + (T1 / line) * info(A, a, particao, 2));

                        if (gain >= mainGain) {
                            mainGain = gain;
                            feature = a;
                            findThr = 0;
                            while (particao > atributoOrdenado[findThr])
                                findThr++;
                            if (findThr == 0)
                                threshold = atributoOrdenado[0];
                            else
                                threshold = atributoOrdenado[findThr - 1];
                        }
                    }

            }
            else {  // atributo categorico
                double[][] atrVal = retiraRepeticao(atributoOrdenado);   // retorna matriz com valor de atributo e quantidade de cada atributo
                if (atributoOrdenado[0] != atributoOrdenado[line - 1])
                    for (int i = 0; i < atrVal.length; i++) {
                        T = atrVal[i][1];
                        T1 = line - atrVal[i][1];
                        gain = infoCategorical(A, 0, 0, 0) - ((T / line) * infoCategorical(A, a, atrVal[i][0], 1) + (T1 / line) * infoCategorical(A, a, atrVal[i][0], 2));

                        if (gain >= mainGain) {
                            mainGain = gain;
                            feature = a;
                            threshold = atrVal[i][0];
                        }
                    }


            }

        }


        //  System.out.println("gain " + mainGain + " part " + threshold + " particao " + particao + " atributo " + feature);

        return new TreeNode(feature, threshold);
    }


    public double[][] retiraRepeticao(double[] Atr){    // retorna vetor de atributos sem repeti��es

        int len = Atr.length;
        int cont = 0, contValid = 0;
        double menor = Atr[0];                      // menor elemento     - recebe vetor ordenado
        double maior = Atr[Atr.length - 1];      // maior elemento

        for(int i = 0; i < len; i++)
            if(i == 0)
                contValid++;
            else if(Atr[i] != Atr[i-1])
                contValid++;

        double[][] intervals = new double[contValid][2];   // valor de atributo e numero de repetição

        contValid = 0;
        for(int i = 1; i < len; i++){

            if(Atr[i] != Atr[i-1]) {
                intervals[contValid][0] = Atr[i-1];
                intervals[contValid][1] = ++cont;
                contValid++;
                cont = 0;
            }
            else
                cont++;
        }
        intervals[contValid][0] = Atr[len-1];
        intervals[contValid][1] = ++cont;



        return intervals;
    }

    public double infoCategorical(double[][] A, int atributo, double attrVal, int particionSide){

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
                    if(A[c][atributo] == attrVal){
                        S++;
                        classes[(int)A[c][coll-1]]++;
                    }

                for(int k = 1; k < nroClasses+1; k++)
                    if(classes[k] > 0)
                        info += -1 * (classes[k]/S) * (Math.log10(classes[k]/S)/Math.log10(2));

                break;

            case 2:

                for(int c = 0; c < line; c++)
                    if(A[c][atributo] != attrVal){
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

        if(attributeType[atributo] == 'n'){
            if(!isGreaterThanThr){                  // atributo numerico
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
        }

        else                                    // atributo categorico
            if(!isGreaterThanThr){              // equivale à diferente de
                for(int i = 0; i < line; i++)
                    if(A[i][atributo] == threshold)
                        cont++;

                newA = new double[cont][coll];
                cont = 0;
                for(int i = 0; i < line; i++)
                    if(A[i][atributo] == threshold){
                        for(int j = 0; j < coll; j++)
                            newA[cont][j] = A[i][j];
                        cont++;
                    }
            }
            else{
                for(int i = 0; i < line; i++)
                    if(A[i][atributo] != threshold)
                        cont++;

                newA = new double[cont][coll];
                cont = 0;
                for(int i = 0; i < line; i++)
                    if(A[i][atributo] != threshold){
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





    public double[] selecionaAtributo(double[][] A, int a){

        int line = A.length;

        double[] atributo = new double[line];

        for(int i = 0; i < line; i++)
            atributo[i] = A[i][a];

        return atributo;
    }




    double[] Classes;
    Networks[] networks;
    private AttributeHandler[] vetAtrHandler;
    private int nroClasses;
    private char[] attributeType;
    private SimplifiedAbDG[] comite;
    private Tree[] forest;


}

