
/**
 * Created by IntelliJ IDEA.
 * User: João
 * Date: 13/09/2007
 * Time: 21:20:19
 * To change this template use File | Settings | File Templates.
 */
public class Aresta {     

    public Aresta(int id){
        this.id = id;
    }

    public Aresta(int id, int atributo, double atr1, double atr2){
        this.intNumber = id;
        this.atributo = atributo;
        this.intInicio = atr1;
        this.intFinal = atr2;
    }

    public Aresta(int id, int classe){
        this.id = id;
        this.classe = classe;
    }

    public void addNeighbor(){
        neighbors++;
    }

    public void setIndice(double ia){     // indice Alneu
       indice = ia;
    }



    private int id;
    private int classe;
    private int neighbors;
    private int atributo;
    private double weight;
    private int intNumber;                // numero do intervalo
    private double intInicio, intFinal;     // intervalo
    private double indice;

}
