public class Rule {

    public Rule(int[] rawRule, double[] probs, double[] boundaries){
        intBounds = boundaries;
        atrSize = boundaries.length / 2;
        this.attrs = new int[atrSize];
        double maior;

        for(int i = 0; i < rawRule.length; i+=2){
            if(rawRule[i] == 0 && rawRule[i+1] == 0)
                break;
            else {
             attrs[rawRule[i]] = 1;
             if(rawRule[i+1] != -1)
                 attrs[rawRule[i+1]] = 1;
            }
        }

        for(int j = 0; j < atrSize; j++)
            if(attrs[j] != 0)
                ruleSize++;


         maior = 0;
         for(int p = 1; p < probs.length; p++)
             if(probs[p] > maior){
                 maior = probs[p];
                 classe = p;
             }


        strengh = probs[0] / ruleSize;


        // descobrir intervalos

    }

    public double avalia(double[] ex){  // avalia nova instância, retorna classe se regra é ativada ou -1 se regra não é ativada

        for(int i = 0; i < atrSize; i++)
            if(attrs[i] != 0){
                if(i == 0) {
                    if (!(ex[i] >= intBounds[2 * i] && ex[i] <= intBounds[2 * i + 1]))
                        return -1;
                }
                else {
                    if (!(ex[i] > intBounds[2 * i] && ex[i] <= intBounds[2 * i + 1]))
                        return -1;
                }
            }
                return classe;

    }

    public void printRule(){

        for(int i = 0; i < atrSize; i++)
            if(attrs[i] != 0){
                System.out.print( intBounds[2*i] + " <  " +  "x" + i + "   <= " + intBounds[2*i+1] + " ");
            }
        System.out.println("then " + classe + " S " + strengh);
    }

    public boolean isSameAs(Rule r2){

        double[] r2Bound = r2.getIntBounds();
        int[] r2Attrs = r2.getAttrs();

        for(int i = 0; i < atrSize; i++)
           if(attrs[i] == 1 && r2Attrs[i] ==1)
                if(!( r2Bound[2*i] == intBounds[2*i] && r2Bound[2*i+1] == intBounds[2*i+1]))
                    return false;

        return true;
    }


    public int getRuleSize(){
        return ruleSize;
    }

    public double getStrengh(){
        return strengh;
    }

    public double[] getIntBounds(){
        return intBounds;
    }

    public int[] getAttrs(){
        return attrs;
    }


    private int ruleSize, atrSize; // tamanho da regra, numero de atributos do conjunto
    private int[] attrs;  // atributos usados nas regras
    private double strengh, classe;
    private double[] intBounds;

}
