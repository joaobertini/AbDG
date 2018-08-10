import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by João on 26/01/2018.
 */
public class SEA {


    public SEA(){

        Classes = new double[2];
        Classes[0] = 1;
        Classes[1] = 2;
        nroClasses = 2;
        commetteeStreetClassTree(15, 100, "CIR", 100000, 1);
    }

    public double commetteeStreetClassTree(int commetteeSize, int NumPrev, String auxFileName, int line, int rep){

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
        double somaNom, somaDenom, alpha = 0.998;


        double[] mcnemar = new double[line - NumPrev + 1];   // vetor para teste McNemar - 0/ acerto , 1/erro
        // Prequential
        double erro;
       // double[] prequential = new double[line - NumPrev + 1];
        // prequantial em intervalos
        double[] prequential = new double[(line - NumPrev)/100 + 1];
        double[] preqWEst = new double[line - NumPrev + 1];
        double[] preqWindow = new double[NumPrev];
        double[][] m;



        int it = 0;
        if( line % NumPrev == 0)
            it = (int)Math.ceil(line/NumPrev);
        else
            it = (int)Math.ceil(line/NumPrev) + 1;                  // define numero de itera��es

        double[][] saidaTotal = new double[it][2];



        // fazer isso aqui � n�o � certo, pois como � uma simula��o voce n�o sabe todos os valores.
        //     CascadeCorrelationRegressao rede = new CascadeCorrelationRegressao(m);   //############# esta treinando uma rede com todos os exemplos?
        ///
        //#
      //  it = 269;   // elec
        //#
        ///


        for(int r = 0; r < rep; r++){

             m = hyperplane(line);// gaussData(line);// SEAconcept(line);// sineData(line); //  // // circleData(line); //               // WWWWWWWWWWWWWWWWWWWWWWWWWWWW
            int coll = m[0].length;

            forest = new Tree[commetteeSize];
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
                    forest[i] = new Tree(C45(matrizTreino));    // nova arvore

                    //   somaNeuronios += this.commetteClassificacao[i].getNroNeuronio();
                    //   somaQuadNeuronios += Math.pow(this.commetteClassificacao[i].getNroNeuronio(),2);
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


                    double[][] comite = new double[contTreino][nroClasses+1];  // resultado do comite

                    if(i < commetteeSize){             // vetor de arvores ainda n�o atingiu limite maximo
                        redesNoComite++;

                        for(k = 0; k < i; k++){
                            testClassification = onlineClassification(matrizTreino,k);
                            teTimeD = System.currentTimeMillis();

                            for(int c = 0; c < testClassification.length; c++)
                                comite[c][(int)testClassification[c]]++;

                        }
                        forest[i] = new Tree(C45(matrizTreino));    // nova arvore

                        //   somaNeuronios += this.commetteClassificacao[i].getNroNeuronio();
                        //   somaQuadNeuronios += Math.pow(this.commetteClassificacao[i].getNroNeuronio(),2);
                        contaRedesCriadas++;
                    }
                    else{                               // vetor de redes esta completo - substitui pior rede
                        redesNoComite = commetteeSize;

                        pior = 0;

                        for(k = 0; k < commetteeSize; k++){
                            testClassification = onlineClassification(matrizTreino,k);

                            correctClassification[k] = 0;
                            for(int c = 0; c < testClassification.length; c++){
                                comite[c][(int)testClassification[c]]++;
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
                            forest[pior] = null;
                            forest[pior] = new Tree(C45(matrizTreino));    // nova arvore

                            //       somaNeuronios += this.commetteClassificacao[pior].getNroNeuronio();
                            //       somaQuadNeuronios += Math.pow(this.commetteClassificacao[pior].getNroNeuronio(),2);
                            contaRedesCriadas++;
                        }
                    }


                    errorSum = 0;        // resolve comite
                    maior = 0;
                    indMaior = -1;
                    for(int a = 0; a < contTreino; a++){
                        maior = comite[a][0];
                        indMaior = 0;
                        for(int b = 1; b < nroClasses+1; b++)
                            if(comite[a][b] > maior){
                                maior = comite[a][b];
                                indMaior = b;
                            }

                        contPrequential++;     // i em gama13
                        if((matrizTreino[a][coll-1] != indMaior)){        // classes sao 1 e 2
                            errorSum++;
                            erro = 1;

                        }
                        else{
                            erro = 0;

                        }

                        mcnemar[contPrequential] = erro;
                        // PREQUENTIALS
                        // prequential all
                       // prequential[contPrequential] = (erro + (contPrequential-1)*prequential[contPrequential-1])/(double)contPrequential;

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

        //for(int s = 0; s < it; s++)
        //   out.println(saidaTotal[s][0] + "  " + saidaTotal[s][1]/rep);

      //  out.println("################################################################################");

            for(int s = 0; s < prequential.length; s++)
               out.println(prequential[s]);

//       out.println("################################################################################");
        //        for(int s = 0; s < prequential.length; s++)
        //           out.println(preqWEst[s]);


        out.close();

        // rodar uma unica vez
        PrintWriter out1;
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream ("C:\\Java\\AbDG\\SAbDGEnsemble\\" + auxFileName + commetteeSize + NumPrev + "SEA_TREEMcNemar.txt");    // ############# tr - testfold; test trainFold    ###############
        } catch ( java.io.IOException e) {
            System.out.println("Could not create result.txt");

        }
        out1 = new PrintWriter(outputStream1);


        for(int t = 0; t < mcnemar.length; t++)
            out1.println(mcnemar[t]);

        out1.close();



        return mediaErro;
    }


    // metodos relacionados ao C4.5
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

    public double[] selecionaAtributo(double[][] A, int a){

        int line = A.length;

        double[] atributo = new double[line];

        for(int i = 0; i < line; i++)
            atributo[i] = A[i][a];

        return atributo;
    }

    public double[][] sineData(int line){

        Random random = new Random();
        int coll = 3;
        int part = line/4;
        double[][] matriz = new double[line][coll];
        boolean key = false;
        double seno = 0;

        attributeType = new char[3];  //########################################################

        attributeType[0] = 'n';
        attributeType[1] = 'n';
        attributeType[2] = 'y';

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

        attributeType = new char[3];
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


    char[] attributeType;
    Tree tree;
    Tree[] forest;
    private int nroClasses;
    private double[] Classes;
}
