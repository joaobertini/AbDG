/**
 * Created by João on 12/03/2018.
 */
public class GAvertices {

    public GAvertices(int atr1Len) {


        candidatesIntervals = new int[atr1Len];
        for(int i = 0; i < atr1Len; i++)
            if(Math.random() > 0.4)
                candidatesIntervals[i] = 1; // inicia atributo aleatoriamente.

    }


    public void reproduction(int[] p1, int[] p2, int favP1){ // p1 e p2 são vetores de mesmo atributo de pais diferentes

        int len = candidatesIntervals.length;

        for(int i = 0; i < len; i++)
            if(p1[i] == p2[i])             // genes iguais mantem-se iguais
                candidatesIntervals[i] = p1[i];
            else{
                if(favP1 == 0) {
                    if (Math.random() > 0.3)               // diferença em gene favorece p1
                        candidatesIntervals[i] = p1[i];
                    else
                        candidatesIntervals[i] = p2[i];
                    }
                    else
                    if (Math.random() > 0.35)         // diferença em gene favoreve p2
                        candidatesIntervals[i] = p2[i];
                    else
                        candidatesIntervals[i] = p1[i];
                }


    }

 /*   public void cross(int[] p){

        int len = conexoes.length;

        for(int i = 0; i < len; i++)
            for(int j = 0; j < col; j++)
                conexoes[i][j] = p[i][j];
    }

*/
    public void mutation(){

        int len = candidatesIntervals.length;
        double mutationRate = 0.3;

        for(int i = 0; i < len; i++)
            if (Math.random() < mutationRate) {
               if (candidatesIntervals[i] == 0)
                   candidatesIntervals[i] = 1;
               else
                   candidatesIntervals[i] = 0;
            }

    }

  /*  public int getConexao(int i, int j){
        return conexoes[i][j];

    }

    public int[][] getMatConexao(){
        return conexoes;

    }
*/

    private int[] candidatesIntervals; // matriz de conexões entre atributos atr1 e atr2
    private int atr1, atr2;


}
