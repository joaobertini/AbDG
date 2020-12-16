/**
 * Created by João on 19/01/2016.
 */
public class GAedges {


    public GAedges(int atr1, int atr2, int atr1Len, int atr2Len) {
        // crição aleatória de arestas
        this.atr1 = atr1;
        this.atr2 = atr2;
        this.atr1Len = atr1Len;
        this.atr2Len = atr2Len;
        conexoes = new int[atr1Len - 1][atr2Len - 1];
        for(int i = 0; i < atr1Len-1; i++)
            for(int j = 0; j < atr2Len-1; j++)
                if(Math.random() > 0.4)
                   conexoes[i][j] = 1;
                else
                   conexoes[i][j] = 0;

    }

    public GAedges(int atr1, int atr2, int[] projAtr1, int[] projAtr2) {
        // crição de arestas usando produto externo - geração de filhos
        this.atr1 = atr1;
        this.atr2 = atr2;
        atr1Len = projAtr1.length + 1;   // VER  porque atrLen vem do tamanho do vetor que define o intervalo
        atr2Len = projAtr2.length + 1;
        conexoes = new int[atr1Len - 1][atr2Len - 1];

        // outer product
        for(int i = 0; i < atr1Len-1; i++)
            for(int j = 0; j < atr2Len-1; j++)
                conexoes[i][j] = projAtr1[i] * projAtr2[j];

        projToZeroOneProb();
    }

    public void projToZeroOneProb(){
        // transforma matriz de conexoes, que contem a multiplicação das projeções, em uma matriz de 0s e 1s.
        // usando probabilidade, dividindo pelo maior valor.

        double maior = 0;

        for(int i = 0; i < atr1Len-1; i++)
            for(int j = 0; j < atr2Len-1; j++)
                if(conexoes[i][j] > maior)
                    maior = conexoes[i][j];


        for(int i = 0; i < atr1Len-1; i++)
            for(int j = 0; j < atr2Len-1; j++)
                if(Math.random() < conexoes[i][j]/maior)
                    conexoes[i][j] = 1;
                else
                    conexoes[i][j] = 0;


    }

    public void reproduction(int[][] p1, int[][] p2, int favP1){

        int len = conexoes.length;
        int col = conexoes[0].length;

        for(int i = 0; i < len; i++)
            for(int j = 0; j < col; j++)
                if(p1[i][j] == p2[i][j])             // genes iguais mantem-se iguais
                     conexoes[i][j] = p1[i][j];
                else{
                    if(favP1 == 0) {
                        if (Math.random() > 0.3)               // diferença em gene favorece p1
                            conexoes[i][j] = p1[i][j];
                        else
                            conexoes[i][j] = p2[i][j];
                    }
                    else
                    if (Math.random() > 0.35)         // diferença em gene favoreve p2
                        conexoes[i][j] = p2[i][j];
                    else
                        conexoes[i][j] = p1[i][j];
                }


    }

    public void cross(int[][] p){

        int len = conexoes.length;
        int col = conexoes[0].length;

        for(int i = 0; i < len; i++)
            for(int j = 0; j < col; j++)
                conexoes[i][j] = p[i][j];
    }

    public void mutation(){

        int len = conexoes.length;
        int col = conexoes[0].length;
        double mutationRate = 0.3;

        for(int i = 0; i < len; i++)
            for (int j = 0; j < col; j++)
                if (Math.random() < mutationRate) {
                    if (conexoes[i][j] == 0)
                        conexoes[i][j] = 1;
                    else
                        conexoes[i][j] = 0;
                }

    }

    public int[] getProjConnection(int atrToProject){
        // faz a projeção da matriz conexoes em um vetor na direção de atrToProject, i.e. deve ficar com a dimensão de atrToProject

        int[] projection;
        if(atrToProject == atr1) {
            projection = new int[atr1Len-1];

            for (int i = 0; i < atr1Len - 1; i++)
                for (int j = 0; j < atr2Len - 1; j++)
                    projection[i] += conexoes[i][j];
        }
        else{
            projection = new int[atr2Len-1];

            for (int j = 0; j < atr2Len - 1; j++)
                for (int i = 0; i < atr1Len - i; i++)
                    projection[j] += conexoes[i][j];
        }

        return projection;
    }


    public int getConexao(int i, int j) {
        try {
            return conexoes[i][j];
        } catch (ArrayIndexOutOfBoundsException erro1) {
            System.err.println("Erro");
        }
        return conexoes[i][j];
      }

    public int[][] getMatConexao(){
        return conexoes;

    }


    private int[][] conexoes; // matriz de conexões entre atributos atr1 e atr2
    private int atr1, atr2;
    private int atr1Len, atr2Len;


}
