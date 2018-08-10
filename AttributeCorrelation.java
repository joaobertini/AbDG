/**
 * Created by IntelliJ IDEA.
 * User: Jo�o
 * Date: 17/07/2009
 * Time: 09:59:26
 * To change this template use File | Settings | File Templates.
 */
public class AttributeCorrelation {

    public AttributeCorrelation(int atr1, int atr2){
        this.atr1 = atr1;
        this.atr2 = atr2;
    }

    public AttributeCorrelation(int atr1, int atr2, double[] vetAtr1, double[] vetAtr2){
        this.atr1 = atr1;
        this.atr2 = atr2;
        this.vetAtr1 = vetAtr1;        // vetor de intervalos do atributo 1
        this.vetAtr2 = vetAtr2;
        this.atr1Len = vetAtr1.length;
        this.atr2Len = vetAtr2.length;
        correlations = new double[atr1Len-1][atr2Len-1];

        // flow graph
        correlationsFlowGraph = new double[atr1Len-1][atr2Len-1];
   }


    public void buildCorrelation(double v1, double v2){

        int i1 = -1, i2 = -1;

        for(int i = 0; i < atr1Len-1; i++)
            if(v1 > vetAtr1[i] && v1 <= vetAtr1[i+1])
                i1 = i;

        if(i1 == -1)                            // condicoes de fronteira
            if(v1 <= vetAtr1[0])
                i1 = 0;
            else if (v1 > vetAtr1[atr1Len-1])
                i1 = atr1Len-2;

        for(int j = 0; j < atr2Len-1; j++)
            if(v2 > vetAtr2[j] && v2 <= vetAtr2[j+1])
                i2 = j;

        if(i2 == -1)                            // condicoes de fronteira
            if(v2 <= vetAtr2[0])
                i2 = 0;
            else if (v2 > vetAtr2[atr2Len-1])
                i2 = atr2Len-2;

        if(i1 != -1 && i2 != -1)
            correlations[i1][i2]++;             // num. de ex. que pertence a i1 e a i2

       //  System.out.println();
    }

    public void updateCorrelation(double v1, double v2){ // usado na versão incremental

                int i1 = -1, i2 = -1;

        for(int i = 0; i < atr1Len-1; i++)
            if(v1 > vetAtr1[i] && v1 <= vetAtr1[i+1])
                i1 = i;

        if(i1 == -1)                            // condicoes de fronteira
            if(v1 <= vetAtr1[0])
                i1 = 0;
            else if (v1 > vetAtr1[atr1Len-1])
                i1 = atr1Len-2;

        for(int j = 0; j < atr2Len-1; j++)
            if(v2 > vetAtr2[j] && v2 <= vetAtr2[j+1])
                i2 = j;

        if(i2 == -1)                            // condicoes de fronteira
            if(v2 <= vetAtr2[0])
                i2 = 0;
            else if (v2 > vetAtr2[atr2Len-1])
                i2 = atr2Len-2;

        if(i1 != -1 && i2 != -1)
            updateCorrelations[i1][i2]++;

        //  System.out.println();
    }
    public double findCorrelation(double v1, double v2){

        int i1 = -1, i2 = -1;

        for(int i = 0; i < atr1Len-1; i++)
            if(v1 > vetAtr1[i] && v1 <= vetAtr1[i+1])
                i1 = i;

        if(i1 == -1)                            // condicoes de fronteira
            if(v1 <= vetAtr1[0])
                i1 = 0;
            else if (v1 > vetAtr1[atr1Len-1])
                i1 = atr1Len-2;

        for(int j = 0; j < atr2Len-1; j++)                            // implementar busca binaria
            if(v2 > vetAtr2[j] && v2 <= vetAtr2[j+1])
                i2 = j;

        if(i2 == -1)                            // condicoes de fronteira
            if(v2 <= vetAtr2[0])
                i2 = 0;
            else if (v2 > vetAtr2[atr2Len-1])
                i2 = atr2Len-2;

        if(i1 == -1 || i2 == -1)
            return 0;
        else
            return correlations[i1][i2];


    }

    public double findCorrelationFlowGraph(double v1, double v2){

        int i1 = -1, i2 = -1;

        for(int i = 0; i < atr1Len-1; i++)
            if(v1 > vetAtr1[i] && v1 <= vetAtr1[i+1])
                i1 = i;

        if(i1 == -1)                            // condicoes de fronteira
            if(v1 <= vetAtr1[0])
                i1 = 0;
            else if (v1 > vetAtr1[atr1Len-1])
                i1 = atr1Len-2;

        for(int j = 0; j < atr2Len-1; j++)                            // implementar busca binaria
            if(v2 > vetAtr2[j] && v2 <= vetAtr2[j+1])
                i2 = j;

        if(i2 == -1)                            // condicoes de fronteira
            if(v2 <= vetAtr2[0])
                i2 = 0;
            else if (v2 > vetAtr2[atr2Len-1])
                i2 = atr2Len-2;

        if(i1 == -1 || i2 == -1)
            return 0;
        else
            return correlationsFlowGraph[i1][i2];


    }

    public void createWeightsProb(int line, int lineTr){ // line é tamanho da classe

        double Pc = line/(double)lineTr;

        for(int i = 0; i < correlations.length; i++)
            for(int j = 0; j < correlations[0].length; j++)
                correlations[i][j] = (correlations[i][j]/line)*Pc;    // joint P(I1,I2,c)


    }

    public void createWeights(int line){

        for(int i = 0; i < correlations.length; i++)
           for(int j = 0; j < correlations[0].length; j++)
               correlations[i][j] /= line;

   //     System.out.println();
    }

    public void updateWeights(int line, double alpha){
      //  double alpha = 0.99;

        for(int i = 0; i < correlations.length; i++)
            for(int j = 0; j < correlations[0].length; j++)
                updateCorrelations[i][j] /= line;

        for(int i = 0; i < correlations.length; i++)
            for(int j = 0; j < correlations[0].length; j++)
                correlations[i][j] = updateCorrelations[i][j] + alpha*correlations[i][j];

        //     System.out.println(); somaClassIL[s] = auxSomaClass[s] + alpha*somaClassIL[s];
        normalizaCorrelations();
    }


    public void normalizaCorrelations(){

        int line = correlations.length;
        int coll = correlations[0].length;
        double soma = 0;

        for(int i = 0; i < line; i++)
            for(int j = 0; j < coll; j++)
                soma += correlations[i][j];

        for(int i = 0; i < line; i++)
            for(int j = 0; j < coll; j++)
                if(correlations[i][j] != 0)
                    correlations[i][j] /= soma;

    }

    public void createWeightsFlowGraph(int line){

        total += line;
        for(int i = 0; i < correlations.length; i++)
            for(int j = 0; j < correlations[0].length; j++)
                correlationsFlowGraph[i][j] = correlations[i][j] / total;

        //     System.out.println();
    }


    public void createWeightsPAPER(int line, double pw){ // line - tamanho da classe, prob marginal

        double aux;
        for(int i = 0; i < correlations.length; i++)
           for(int j = 0; j < correlations[0].length; j++){
               aux = correlations[i][j];
               correlations[i][j] = (aux/(double)line)*pw;    // P(Iak,Ibq,Cj)    joint
           }

   //     System.out.println();
    }
    
     public void createWeightsPAPERimputation(int line, int observed, double pw){ // line - tamanho da classe, prob marginal

        double aux;
        for(int i = 0; i < correlations.length; i++)
           for(int j = 0; j < correlations[0].length; j++){
               aux = correlations[i][j];
               correlations[i][j] = (observed/(double)line)*(aux/(double)line)*pw;    // P(Iak,Ibq,Cj)    joint
           }

   //     System.out.println();
    }


    public void showCM(){

        System.out.println("atrs  1");
        for(int i = 0; i < vetAtr1.length; i++)
           System.out.print(vetAtr1[i] + " ");

        System.out.println("atrs  2");
        for(int i = 0; i < vetAtr2.length; i++)
           System.out.print(vetAtr2[i] + " ");

           System.out.println("Correlações");
        for(int i = 0; i < correlations.length; i++) {
           for(int j = 0; j < correlations[0].length; j++)
              System.out.print(correlations[i][j] + " ");
           System.out.println();
        }

    }

     public void showCMEx(int lin, int col){

         System.out.print(correlations[lin][col] + " ");


    }

    public void initUpdate(){
        updateCorrelations = new double[atr1Len-1][atr2Len-1];
    }

      public double getCMEx(int lin, int col){
          return correlations[lin][col];
      }

    public void setCMEx(int lin, int col, double soma){
        if(soma != 0)
           correlations[lin][col] /= soma;
      }


     public int getCMLen(){
       return correlations.length;

    }

     public int getCMCol(){
       return correlations[0].length;

    }

    public double[][] getFullCorrelation(){
        return correlations;
    }

    public double getCorrelation(int interval1, int interval2){
       return correlations[interval1][interval2];
    }

    public void showAtributes(){
        System.out.println(atr1 + " " + atr2);
    }


    // ######################  to imputation   ####################

    public double[] findPossibleCorrelations(int atrFalta, int atrTem, double valTem){     //  atr q falta, atr qeu tem e valor do atr qu tem

        int i1 = -1;
                                                     // objeto corresponde � correlacao entre atr1 e atr2.
        if(atr2 == atrFalta){                                    // falta atr2

            for(int i = 0; i < atr1Len-1; i++)
                if(valTem > vetAtr1[i] && valTem <= vetAtr1[i+1])
                    i1 = i;

            if(i1 == -1)                            // condicoes de fronteira
                if(valTem <= vetAtr1[0])
                    i1 = 0;
                else if (valTem > vetAtr1[atr1Len-1])
                    i1 = atr1Len-2;
        }

        else if(atr1 == atrFalta){                            // falta atr1

            for(int j = 0; j < atr2Len-1; j++)                            // implementar busca binaria
                if(valTem > vetAtr2[j] && valTem <= vetAtr2[j+1])
                    i1 = j;

            if(i1 == -1)                            // condicoes de fronteira
                if(valTem <= vetAtr2[0])
                    i1 = 0;
                else if (valTem > vetAtr2[atr2Len-1])
                    i1 = atr2Len-2;

        }

        if(i1 == -1)
            return null;
        else if(atr2 == atrFalta)
            return correlations[i1];
        else{
            int aux = correlations.length;
            double[] cor = new double[aux];
            for(int j = 0; j < aux; j++)
                cor[j] = correlations[j][i1];

            return cor;
        }
    }

    public double getGuessFromInterval(int intervalo, int side){

        double ini;
        double fim;


        if(side == 1){

         ini = vetAtr1[intervalo];
         fim = vetAtr1[intervalo + 1];

        }

        else{

         ini = vetAtr2[intervalo];
         fim = vetAtr2[intervalo + 1];
        }


        return (ini + fim)/2;   // chuta no meio

    }

     public double[] getInterval(int intervalo, int atr){

        double[] edges = new double[2];


        if(atr1 == atr){

         edges[0] = vetAtr1[intervalo];
         edges[1] = vetAtr1[intervalo + 1];

        }

        else{

         edges[0] = vetAtr2[intervalo];
         edges[1] = vetAtr2[intervalo + 1];
        }


        return edges;   

    }

    public int getIntervalNumATR1(double val){

        int i1 = -1;

        for(int i = 0; i < atr1Len-1; i++)
            if(val > vetAtr1[i] && val <= vetAtr1[i+1])
                i1 = i;

        if(i1 == -1)                            // condicoes de fronteira
            if(val <= vetAtr1[0])
                i1 = 0;
            else if (val > vetAtr1[atr1Len-1])
                i1 = atr1Len-2;


        return i1;

    }

    public int getIntervalNumATR2(double val){

        int i1 = -1;

        for(int i = 0; i < atr2Len-1; i++)
            if(val > vetAtr2[i] && val <= vetAtr2[i+1])
                i1 = i;

        if(i1 == -1)                            // condicoes de fronteira
            if(val <= vetAtr2[0])
                i1 = 0;
            else if (val > vetAtr2[atr2Len-1])
                i1 = atr2Len-2;


        return i1;

    }

    private int atr1, atr2;
    private int atr1Len, atr2Len;
    private double[] vetAtr1, vetAtr2;
    private double[][] correlations;         // criar duas matrizes - contagem e pesos
    private double[][] updateCorrelations;
                                            // Flow graphs
    private double[][] correlationsFlowGraph;
    private int total = 0;
}



