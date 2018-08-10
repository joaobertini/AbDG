/**
 * Created by João on 19/01/2016.
 */
public class GAedges {


    public GAedges(int atr1, int atr2, int atr1Len, int atr2Len) {


        conexoes = new int[atr1Len - 1][atr2Len - 1];
        for(int i = 0; i < atr1Len-1; i++)
            for(int j = 0; j < atr2Len-1; j++)
                if(Math.random() > 0.4)
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

    public int getConexao(int i, int j){
        return conexoes[i][j];

    }

    public int[][] getMatConexao(){
        return conexoes;

    }


    private int[][] conexoes; // matriz de conexões entre atributos atr1 e atr2
    private int atr1, atr2;


}
