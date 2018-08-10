/**
 * Created by IntelliJ IDEA.
 * User: João
 * Date: 16/02/2012
 * Time: 11:42:11
 * To change this template use File | Settings | File Templates.
 */
public class NetworkFull {

    public NetworkFull(double[][] matriz){
       E = matriz;
       line = E.length;        // tamanho da classe
       coll = E[0].length;
      // vetCorrelations = new AttributeCorrelation[coll-2];

       // inseridos para imputa��o



    }

    public NetworkFull(double[][] matriz, double classe, int trainLen){
       E = matriz;
       line = E.length;                           // conjuto com classe 'classe'  usado no treinamento desta rede
       coll = E[0].length;
       // fuletCorrelations = new AttributeCorrelation[coll-2];
       this.classe = classe;
       this.trSize = trainLen;      // tamanho do conjunto de treinamento completo


       numAttr = coll-1;
       numEdgeFull = numAttr + (numAttr*(numAttr-3))/2;       // numero de 'matrizes' de conex�es entre atributos
       fullVetCorrelations = new AttributeCorrelation[numEdgeFull];  
     //  this.ordem = ordem;
    }



    public void learnFullConection(AttributeHandler[] atr){   // conecta cada atributo com todos os outros - resulta em
      vetAtr = atr;   // como estava em Networks

      int cont = 0;
      for(int i = 0; i < numAttr; i++){
        for(int j = i  + 1; j < numAttr; j++){
 //           System.out.println(i + " " + j );
            fullVetCorrelations[cont] = new AttributeCorrelation(i, j, atr[i].getVetAtr(), atr[j].getVetAtr());
            cont++;
         }

       }

      cont = 0;

      for(int i = 0; i < numAttr; i++){
         for(int j = i + 1; j < numAttr; j++){                        // separar por classe
            for(int k = 0; k < line; k++)
                fullVetCorrelations[cont].buildCorrelation(E[k][i],E[k][j]);
     //    vetCorrelations[j].showCM();
         fullVetCorrelations[cont].createWeights(line);
         cont++;
     //     vetCorrelations[j].showCM();
         }
      }

   //     System.out.println();
    }

    public void learnFullConection(AttributeHandler[] atr, int[] atrMask, int lineTr){   // conecta cada atributo com todos os outros - resulta em
        vetAtr = atr;   // como estava em Networks

        int cont = 0;
        for(int i = 0; i < numAttr; i++){
            for(int j = i  + 1; j < numAttr; j++){
                //           System.out.println(i + " " + j );
                if(atrMask[i] == 1 && atrMask[j] == 1)
                    fullVetCorrelations[cont] = new AttributeCorrelation(i, j, atr[i].getVetAtr(), atr[j].getVetAtr());
                cont++;
            }

        }

        cont = 0;

        for(int i = 0; i < numAttr; i++){
            for(int j = i + 1; j < numAttr; j++){
                if(atrMask[i] == 1 && atrMask[j] == 1) {// separar por classe
                    for (int k = 0; k < line; k++)
                        fullVetCorrelations[cont].buildCorrelation(E[k][i], E[k][j]);
                    //    vetCorrelations[j].showCM();
                    //fullVetCorrelations[cont].createWeights(line);
                    fullVetCorrelations[cont].createWeightsProb(line, lineTr);
                }
                cont++;
                //     vetCorrelations[j].showCM();
            }
        }

        //     System.out.println();
    }


    public void updateFullConection(double[][] E, double alpha){   // conecta cada atributo com todos os outros - resulta em
      int cont = 0;
      int line = E.length;


        for(int i = 0; i < numAttr; i++){
            for(int j = i + 1; j < numAttr; j++){                        // separar por classe
                fullVetCorrelations[cont].initUpdate(); // cria matriz de atualização em Attribute Correlation
                for(int k = 0; k < line; k++)
                    fullVetCorrelations[cont].updateCorrelation(E[k][i],E[k][j]);
                //    vetCorrelations[j].showCM();
                fullVetCorrelations[cont].updateWeights(line, alpha);
                cont++;
                //     vetCorrelations[j].showCM();
            }
        }

        //     System.out.println();
    }

    public void updateFullConection(double[][] E, int[] atrMask, double alpha){   // conecta cada atributo com todos os outros - resulta em
        int cont = 0;
        int line = E.length;

        for(int i = 0; i < numAttr; i++){
            for(int j = i + 1; j < numAttr; j++){                        // separar por classe
                if(atrMask[i] == 1 && atrMask[j] == 1) {
                    fullVetCorrelations[cont].initUpdate();
                    for(int k = 0; k < line; k++)
                        fullVetCorrelations[cont].updateCorrelation(E[k][i], E[k][j]);
                    //    vetCorrelations[j].showCM();
                    fullVetCorrelations[cont].updateWeights(line, alpha);
                }
                cont++;
                //     vetCorrelations[j].showCM();
            }
        }

        //     System.out.println();
    }

    public double[][] getCorrelation(int cont){

        return fullVetCorrelations[cont].getFullCorrelation();
    }


    public void learnFullConectionImputation(AttributeHandler[] atr, double[][] MTI){   // conecta cada atributo com todos os outros - resulta em
         vetAtr = atr;   // como estava em Networks

         int cont = 0;
         int pairCount = 0;  // conta quantas instancias possuem ambos atributos Ai e Aj
         for(int i = 0; i < numAttr; i++){
           for(int j = i  + 1; j < numAttr; j++){
    //           System.out.println(i + " " + j );
               fullVetCorrelations[cont] = new AttributeCorrelation(i, j, atr[i].getVetAtr(), atr[j].getVetAtr());
               cont++;
            }

          }

         cont = 0;

         for(int i = 0; i < numAttr; i++){
            for(int j = i + 1; j < numAttr; j++){                        // separar por classe
                pairCount = 0;
                for(int k = 0; k < line; k++)
                  if(MTI[k][i] != emptyValue && MTI[k][j] != emptyValue){
                      fullVetCorrelations[cont].buildCorrelation(MTI[k][i],MTI[k][j]);
                      pairCount++;
                  }

            fullVetCorrelations[cont].createWeights(pairCount);
            cont++;
        //     vetCorrelations[j].showCM();
            }
         }

      //    System.out.println();
       }


    public void learnFullConectionPAPER(AttributeHandler[] atr){   // conecta cada atributo com todos os outros - resulta em
      vetAtr = atr;   // como estava em Networks
      double pw = line/(double)trSize;

      int cont = 0;
      for(int i = 0; i < numAttr; i++){
        for(int j = i  + 1; j < numAttr; j++){
 //           System.out.println(i + " " + j );
            fullVetCorrelations[cont] = new AttributeCorrelation(i, j, atr[i].getVetAtr(), atr[j].getVetAtr());
            cont++;
         }

       }

      cont = 0;

      for(int i = 0; i < numAttr; i++){
         for(int j = i + 1; j < numAttr; j++){                        // separar por classe
            for(int k = 0; k < line; k++)
                fullVetCorrelations[cont].buildCorrelation(E[k][i],E[k][j]);
     //    vetCorrelations[j].showCM();
         fullVetCorrelations[cont].createWeightsPAPER(line,pw);
         cont++;
     //     vetCorrelations[j].showCM();
         }
      }

   //     System.out.println();
    }

    public void learnFullConectionImputationPAPER(AttributeHandler[] atr, double[][] MTI){   // conecta cada atributo com todos os outros - resulta em
         vetAtr = atr;   // como estava em Networks
         double pw = line/(double)trSize;       // probabilidade marginal da classe

         int cont = 0;
         int pairCount = 0;  // conta quantas instancias possuem ambos atributos Ai e Aj
         for(int i = 0; i < numAttr; i++){
           for(int j = i  + 1; j < numAttr; j++){
    //           System.out.println(i + " " + j );
               fullVetCorrelations[cont] = new AttributeCorrelation(i, j, atr[i].getVetAtr(), atr[j].getVetAtr());
               cont++;
            }

          }

         cont = 0;

         for(int i = 0; i < numAttr; i++){
            for(int j = i + 1; j < numAttr; j++){                        // separar por classe
                pairCount = 0;
                for(int k = 0; k < line; k++)
                  if(MTI[k][i] != emptyValue && MTI[k][j] != emptyValue){
                      fullVetCorrelations[cont].buildCorrelation(MTI[k][i],MTI[k][j]);
                      pairCount++;
                  }

            fullVetCorrelations[cont].createWeightsPAPERimputation(line,pairCount,pw);
            cont++;
        //     vetCorrelations[j].showCM();
            }
         }

       //   System.out.println();
       }

    public double[] probClass(double[][] A){           // retorna vetor de probabilidades da rede corrente

       int lineTe = A.length;
       int coll = A[0].length;
       double[] probVector = new double[lineTe];

       for(int z = 0; z < lineTe; z++)
          probVector[z] = 0;                // zera vetor

       for(int i = 0; i < lineTe; i++){
          for(int j = 0; j < coll - 2; j++)
             probVector[i] += fullVetCorrelations[j].findCorrelation(A[i][j], A[i][j+1]);
       }

       return probVector;
    }

     public void criaProbClass(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

       int lineTe = A.length;
       int coll = A[0].length;
       double aux = 1, soma = 0, maior = 0, somaGain = 0;
       privateProbVector = new double[lineTe];
       privateSumVector = new double[lineTe];

       for(int z = 0; z < lineTe; z++){
          privateProbVector[z] = 1;                // zera vetor
          privateSumVector[z] = 0;
       }

       for(int i = 0; i < coll-1; i++)
           somaGain += vetAtr[i].getIntervalGain();
         //      System.out.println(vetAtr[i].getIntervalGain());
         //

       for(int i = 0; i < lineTe; i++){
      //     System.out.print("instancia " + i + "rede classe " + classe);
           soma = 0;
           aux = 1;
         for(int j = 0; j < coll - 2; j++){
          //   System.out.print(" #### " +  vetAtr[j].getWeight(A[i][j],(int)classe) + " " + vetCorrelations[j].findCorrelation(A[i][j], A[i][j+1])  + " " +  vetAtr[j+1].getWeight(A[i][j+1],(int)classe));
           privateProbVector[i] *= vetAtr[j].getWeight(A[i][j],(int)classe); // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
         //  soma += aux;
           privateSumVector[i] += fullVetCorrelations[j].findCorrelation(A[i][j], A[i][j+1]);

        //     maior += vetAtr[j].getWeight(A[i][ordem[j]],(int)classe);
        //     soma += vetCorrelations[j].findCorrelation(A[i][ordem[j]], A[i][ordem[j+1]]);

        }
            privateProbVector[i] *= vetAtr[coll-2].getWeight(A[i][coll-2],(int)classe); // * (vetAtr[coll - 2].getIntervalGain()/somaGain);
         //   System.out.println( "result " + privateProbVector[i]);

       }

    }

    public void criaProbClassMaxMin(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

       int lineTe = A.length;
       int coll = A[0].length;
       double aux = 0, somaGain = 0, corr = 0;
       privateProbVector = new double[lineTe];

       for(int z = 0; z < lineTe; z++)
          privateProbVector[z] = 0;                // zera vetor

     //  for(int i = 0; i < coll-1; i++)
     //     System.out.println(vetAtr[i].getIntervalGain());
         // somaGain += vetAtr[i].getIntervalGain();

       for(int i = 0; i < lineTe; i++){
           somaGain = 0;
          for(int j = 0; j < coll - 2; j++) {
             aux = Math.min(vetAtr[j].getWeight(A[i][j],(int)classe) , vetAtr[j+1].getWeight(A[i][j+1],(int)classe));  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
             corr = fullVetCorrelations[j].findCorrelation(A[i][j], A[i][j+1]);
             if(corr != 0)
                somaGain += Math.max(aux, corr);
             else
                somaGain += aux;
           }
            privateProbVector[i] = somaGain;
       }

    }

 /*   public double calculateEmptyAtributeValue(double[] inst ,int atr){   //      imputa��o

        // verificar (atr-1) x atr e atr x (atr + 1)
        int coll = inst.length;
        int intervalo = 0, inta, intb;
        double[] atrAnt;
        double[] atrDep;
        int side = 1;

       if(atr > 0 && atr < coll - 2){
          atrAnt = vetCorrelations[atr-1].findPossibleCorrelations(inst[atr-1],1);  // retorna vetor com possiveis intervalos - pertencentes ao atributo faltante
          atrDep = vetCorrelations[atr].findPossibleCorrelations(inst[atr+1],2);
          inta = maiorInd(atrAnt);
          intb = maiorInd(atrDep);
          if(inta != intb){
            if(atrAnt[inta] > atrDep[intb]){
               intervalo = inta;
               atr--;
               side = 1;
            }
            else{
               intervalo = intb;
               side = 2;
            }
          }
          else{
             intervalo = intb;
             side = 2;
          }

           // encontrar maior entre atrAnt e atrDep
      }
       else if(atr == 0){
           atrDep = vetCorrelations[atr].findPossibleCorrelations(inst[atr+1],2);
           intervalo = maiorInd(atrDep);
           side = 1; // procura em vetAtr1
         }

      else if(atr == (coll-2)){
           atr--;
           atrAnt = vetCorrelations[atr].findPossibleCorrelations(inst[atr],1);
           intervalo = maiorInd(atrAnt);
           side = 2;  // procura em vetAtr2
         }

       return vetCorrelations[atr].getGuessFromInterval(intervalo,side);
        //   encontrar intervalo correspondente � correla��o em attributeCorrelation


    }  */



    public void criaProbClassWeighted(double[][] A, double[] atrW){           // armazena no objeto o vetor de probabilidades da rede corrente

         int lineTe = A.length;
         int coll = A[0].length;
         int cont = 0;
         double aux = 1, soma = 0, maior = 0, somaGain = 0;
         privateProbVector = new double[lineTe];
         privateSumVector = new double[lineTe];
         privateProdSumVector = new double[lineTe];

         for(int z = 0; z < lineTe; z++){
             privateProbVector[z] = 1;                // zera vetor
             privateSumVector[z] = 0;
             privateProdSumVector[z] = 1;
         }


         for(int i = 0; i < lineTe; i++){
             soma = 0;
             aux = 1;                                   //   peso de atributo - atrW[j]
             for(int j = 0; j < coll - 1; j++){
                 //   System.out.print(" #### " +  vetAtr[j].getWeight(A[i][j],(int)classe) + " " + vetCorrelations[j].findCorrelation(A[i][j], A[i][j+1])  + " " +  vetAtr[j+1].getWeight(A[i][j+1],(int)classe));
                 privateProbVector[i] *= vetAtr[j].getWeight(A[i][j],(int)classe); // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
             }
         }

         cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
         for(int a = 0; a < numAttr; a++){
             for(int b = a + 1; b < numAttr; b++){
                 for(int i = 0; i < lineTe; i++){
                     privateSumVector[i] += fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]);
                 }
                 cont++;
             }

         }


        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
         for(int a = 0; a < numAttr; a++){
             for(int b = a + 1; b < numAttr; b++){
                 for(int i = 0; i < lineTe; i++){
                     privateProdSumVector[i] += fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]) * vetAtr[a].getWeight(A[i][a],(int)classe)  * vetAtr[b].getWeight(A[i][b],(int)classe);
                 }
                 cont++;
             }

         }


     }

    public void criaProbClassWeighted(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe];
        privateSumVector = new double[lineTe];
        privateProdSumVector = new double[lineTe];

        for(int z = 0; z < lineTe; z++){
            privateProbVector[z] = 1;                // zera vetor
            privateSumVector[z] = 0;
            privateProdSumVector[z] = 1;
        }


        for(int i = 0; i < lineTe; i++){
            soma = 0;
            aux = 1;                                   //   peso de atributo - atrW[j]
            for(int j = 0; j < coll - 1; j++){
                //   System.out.print(" #### " +  vetAtr[j].getWeight(A[i][j],(int)classe) + " " + vetCorrelations[j].findCorrelation(A[i][j], A[i][j+1])  + " " +  vetAtr[j+1].getWeight(A[i][j+1],(int)classe));
                privateProbVector[i] *= vetAtr[j].getWeight(A[i][j],(int)classe); // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
        }

        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    privateSumVector[i] += fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]);
                }
                cont++;
            }

        }


        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    privateProdSumVector[i] += fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]) * vetAtr[a].getWeight(A[i][a],(int)classe)  * vetAtr[b].getWeight(A[i][b],(int)classe);
                }
                cont++;
            }

        }


    }

    public void criaProbClassWeighted(double[][] A, int[] atrMask){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe];
        privateSumVector = new double[lineTe];
        privateProdSumVector = new double[lineTe];

        for(int z = 0; z < lineTe; z++){
            privateProbVector[z] = 0;                // zera vetor
            privateSumVector[z] = 0;
            privateProdSumVector[z] = 1;
        }


        for(int i = 0; i < lineTe; i++){
            soma = 0;
            aux = 1;                                   //   peso de atributo - atrW[j]
            for(int j = 0; j < coll - 1; j++){
                if(atrMask[j] == 1)
                    privateProbVector[i] += Math.log(vetAtr[j].getWeightedInterval(A[i][j],(int)classe)); // faster // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
            privateProbVector[i] = Math.exp(privateProbVector[i]);
        }



        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                if(atrMask[a] == 1 && atrMask[b] == 1 )
                    for(int i = 0; i < lineTe; i++){
                        privateSumVector[i] += Math.log(fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]));
                    }
                cont++;
            }

        }

        for(int i = 0; i < lineTe; i++)
            privateSumVector[i] = Math.exp(privateSumVector[i]);


    /*
        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                if(atrMask[a] == 1 && atrMask[b] == 1 )
                for(int i = 0; i < lineTe; i++){
                    privateSumVector[i] += fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]);
                }
                cont++;
            }

        }
*/

  /*      cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    privateProdSumVector[i] += fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]) * vetAtr[a].getWeight(A[i][a],(int)classe)  * vetAtr[b].getWeight(A[i][b],(int)classe);
                }
                cont++;
            }

        }
*/

    }

    public void criaProbClassWeightedSA(double[][] A){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe];
        privateSumVector = new double[lineTe];
        privateProdSumVector = new double[lineTe];

        for(int z = 0; z < lineTe; z++){
            privateProbVector[z] = 0;                // zera vetor
            privateSumVector[z] = 0;
            privateProdSumVector[z] = 1;
        }


        for(int i = 0; i < lineTe; i++){
            soma = 0;
            aux = 1;                                   //   peso de atributo - atrW[j]
            for(int j = 0; j < coll - 1; j++){
                privateProbVector[i] += Math.log(vetAtr[j].getWeightedInterval(A[i][j],(int)classe)); // faster // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
            privateProbVector[i] = Math.exp(privateProbVector[i]);
        }



        // trata peso de correlacao entre atributos como a soma dos logs.
        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    if(fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]) > 0)
                        privateSumVector[i] += Math.log(fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]));
                }
                cont++;
            }

        }

        for(int i = 0; i < lineTe; i++)
            privateSumVector[i] = Math.exp(privateSumVector[i]);

        /*

        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                        privateSumVector[i] += fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]);
                    }
                cont++;
            }

        }
*/

  /*      cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    privateProdSumVector[i] += fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]) * vetAtr[a].getWeight(A[i][a],(int)classe)  * vetAtr[b].getWeight(A[i][b],(int)classe);
                }
                cont++;
            }

        }
*/

    }

    public void criaProbClassWeightedGeneticAlgorithm(double[][] A, GAedges[] vetEdge){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe];
        privateSumVector = new double[lineTe];
        privateProdSumVector = new double[lineTe];

        for(int z = 0; z < lineTe; z++){
            privateProbVector[z] = 0;                // zera vetor
            privateSumVector[z] = 0;
            privateProdSumVector[z] = 1;
        }


        for(int i = 0; i < lineTe; i++){
            soma = 0;
            aux = 1;                                   //   peso de atributo - atrW[j]
            for(int j = 0; j < coll - 1; j++){
               if(vetAtr[j].getWeight(A[i][j],(int)classe) != 0)
                   privateProbVector[i] += Math.log(vetAtr[j].getWeight(A[i][j],(int)classe)); // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
        }

        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    if(vetEdge[cont].getConexao(fullVetCorrelations[cont].getIntervalNumATR1(A[i][a]),fullVetCorrelations[cont].getIntervalNumATR2(A[i][b])) == 1)
                        if(fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]) != 0)
                            privateSumVector[i] += Math.log(fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]));
                }
                cont++;
            }

        }


    }


    public void criaProbClassWeightedGeneticAlgorithmF1(double[][] A, GAedges[] vetEdge){           // armazena no objeto o vetor de probabilidades da rede corrente

        int lineTe = A.length;
        int coll = A[0].length;
        int cont = 0;
        double aux = 1, soma = 0, maior = 0, somaGain = 0;
        privateProbVector = new double[lineTe];
        privateSumVector = new double[lineTe];
        privateProdSumVector = new double[lineTe];

        for(int z = 0; z < lineTe; z++){
            privateProbVector[z] = 0;                // zera vetor
            privateSumVector[z] = 0;
            privateProdSumVector[z] = 1;
        }


        for(int i = 0; i < lineTe; i++){
            soma = 0;
            aux = 1;                                   //   peso de atributo - atrW[j]
            for(int j = 0; j < coll - 1; j++){
                if(vetAtr[j].getWeight(A[i][j],(int)classe) != 0)
                    privateProbVector[i] *= vetAtr[j].getWeight(A[i][j],(int)classe); // * (vetAtr[j].getIntervalGain()/somaGain);//(vetAtr[j].getWeight(A[i][j],(int)classe) * vetAtr[j+1].getWeight(A[i][j+1],(int)classe)) ;  //     vetAtr[j].getIntervalGain()/somaGain + vetAtr[j+1].getIntervalGain()/somaGain;
            }
        }

        cont = 0;                                                   // fullVetCorrelations tem a dimens�o de numEdgeFull
        for(int a = 0; a < numAttr; a++){
            for(int b = a + 1; b < numAttr; b++){
                for(int i = 0; i < lineTe; i++){
                    if(vetEdge[cont].getConexao(fullVetCorrelations[cont].getIntervalNumATR1(A[i][a]),fullVetCorrelations[cont].getIntervalNumATR2(A[i][b])) == 1)
                        if(fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]) != 0)
                            privateSumVector[i] += fullVetCorrelations[cont].findCorrelation(A[i][a], A[i][b]);
                }
                cont++;
            }

        }


    }

    public double retrieveSum(double[] inst ,int atr, int numInter){   //      imputa��o teste

         // verificar (atr-1) x atr e atr x (atr + 1)
         int coll = inst.length;
         int intervalo = 0, inta, intb;
         int cont = 0;
         int len = numInter;            // numero de intervalos -1
         double[] somaAtr = new double[len];
         double[] atrCorr = new double[len];
         int auxInd = 0; // armazena algum indice de vetCorrelation onde um dos attributos seja o faltante
         indiceIntervalo = 0;   // imputa��o de testes
         indiceVetCorr = 0;

       for(int i = 0; i < numAttr; i++){
          for(int j = i + 1; j < numAttr; j++){
             if(i == atr){
                atrCorr = fullVetCorrelations[cont].findPossibleCorrelations(i, j, inst[j]); // falta atributo i
                auxInd = cont;

               for(int p = 0; p < len; p++)
                  somaAtr[p] += atrCorr[p];
             }
            else if (j == atr){
                atrCorr = fullVetCorrelations[cont].findPossibleCorrelations(j, i, inst[i]);
                auxInd = cont;

                for(int p = 0; p < len; p++)
                 somaAtr[p] += atrCorr[p];
             }

             cont++;
             // retorna vetor com possiveis intervalos - pertencentes ao atributo faltante

             // atrCorr armazena os possiveis intervalos do atributo atr faltante  -  todos t�m a mesma dimens�o

          }
        }


         indiceIntervalo = maiorInd(somaAtr);
         indiceVetCorr = auxInd;

         return somaAtr[indiceIntervalo];  // maior soma



     }

      public double retrieveSumExperimenting(double[] inst ,int atr, int numInter){   //      imputa��o teste

         // verificar (atr-1) x atr e atr x (atr + 1)
         int coll = inst.length;
         int intervalo = 0, inta, intb;
         int cont = 0;
         int len = numInter;            // numero de intervalos -1
         double[] somaAtr = new double[len];
         double[] atrCorr = new double[len];
         int auxInd = 0; // armazena algum indice de vetCorrelation onde um dos attributos seja o faltante
         double weightVert = 0;
         indiceIntervalo = 0;   // imputa��o de testes
         indiceVetCorr = 0;

       for(int i = 0; i < numAttr; i++){
          for(int j = i + 1; j < numAttr; j++){
             if(i == atr){
                atrCorr = fullVetCorrelations[cont].findPossibleCorrelations(i, j, inst[j]); // falta atributo i
                auxInd = cont;
                weightVert = vetAtr[j].getIntervalGain() * vetAtr[j].getWeight(inst[j],(int)classe);

               for(int p = 0; p < len; p++)
                  somaAtr[p] += weightVert * atrCorr[p];
             }
            else if (j == atr){
                atrCorr = fullVetCorrelations[cont].findPossibleCorrelations(j, i, inst[i]);
                auxInd = cont;
                weightVert = vetAtr[i].getIntervalGain() * vetAtr[i].getWeight(inst[j],(int)classe);

                for(int p = 0; p < len; p++)
                 somaAtr[p] += weightVert * atrCorr[p];
             }

             cont++;
             // retorna vetor com possiveis intervalos - pertencentes ao atributo faltante

             // atrCorr armazena os possiveis intervalos do atributo atr faltante  -  todos t�m a mesma dimens�o

          }
        }

          for(int p = 0; p < len; p++)
             somaAtr[p] *= vetAtr[atr].getIntClass(p,(int)classe);// * vetAtr[atr].getIntervalGain(); // peso dos vertices, no atributo desconhecido


         indiceIntervalo = maiorInd(somaAtr);
         indiceVetCorr = auxInd;

         return somaAtr[indiceIntervalo];  // maior soma



     }

    public double[] getIntervalTest(int atr){

        return fullVetCorrelations[indiceVetCorr].getInterval(indiceIntervalo,atr);

    }

      public double[] retrieveInterval(double[] inst ,int atr, int numInter){   //      imputa��o treino

         // verificar (atr-1) x atr e atr x (atr + 1)
         int coll = inst.length;
         int intervalo = 0, inta, intb;
         int cont = 0;
         int len = numInter;            // numero de intervalos -1
         double[] somaAtr = new double[len];
         double[] atrCorr = new double[len];
         int auxInd = 0; // armazena algum indice de vetCorrelation onde um dos attributos seja o faltante

       for(int i = 0; i < numAttr; i++){
          for(int j = i + 1; j < numAttr; j++){
             if(i == atr){
                atrCorr = fullVetCorrelations[cont].findPossibleCorrelations(i, j, inst[j]); // falta atributo i
                auxInd = cont;

               for(int p = 0; p < len; p++)
                  somaAtr[p] += atrCorr[p];
             }
            else if (j == atr){
                atrCorr = fullVetCorrelations[cont].findPossibleCorrelations(j, i, inst[i]);
                auxInd = cont;

                for(int p = 0; p < len; p++)
                 somaAtr[p] += atrCorr[p];
             }
                         
             cont++;
             // retorna vetor com possiveis intervalos - pertencentes ao atributo faltante

             // atrCorr armazena os possiveis intervalos do atributo atr faltante  -  todos t�m a mesma dimens�o

          }
        }


         intervalo = maiorInd(somaAtr,atr,inst[coll-1]);   // resolve impate


         return fullVetCorrelations[auxInd].getInterval(intervalo,atr);
         //   encontrar intervalo correspondente � correla��o em attributeCorrelation

     }

     public double[] retrieveIntervalExperimenting(double[] inst ,int atr, int numInter){   //      imputa��o treino
                           // atr � atributo que falta
         // verificar (atr-1) x atr e atr x (atr + 1)
         int coll = inst.length;
         int intervalo = 0, inta, intb;
         int cont = 0;
         int len = numInter;            // numero de intervalos -1
         double[] somaAtr = new double[len];
         double[] atrCorr = new double[len];
         int auxInd = 0; // armazena algum indice de vetCorrelation onde um dos attributos seja o faltante
         double weightVert = 0;

       for(int i = 0; i < numAttr; i++){
          for(int j = i + 1; j < numAttr; j++){
             if(i == atr){
                atrCorr = fullVetCorrelations[cont].findPossibleCorrelations(i, j, inst[j]); // falta atributo i
                auxInd = cont;
                weightVert = vetAtr[j].getWeight(inst[j],(int)classe) * vetAtr[j].getIntervalGain();

               for(int p = 0; p < len; p++)
                  somaAtr[p] += weightVert * atrCorr[p];   // peso do vertice em que existe valor de atributo
             }
            else if (j == atr){
                atrCorr = fullVetCorrelations[cont].findPossibleCorrelations(j, i, inst[i]);
                auxInd = cont;
                weightVert = vetAtr[i].getWeight(inst[j],(int)classe) * vetAtr[i].getIntervalGain();

                for(int p = 0; p < len; p++)
                 somaAtr[p] += weightVert * atrCorr[p];
             }

             cont++;
             // retorna vetor com possiveis intervalos - pertencentes ao atributo faltante

             // atrCorr armazena os possiveis intervalos do atributo atr faltante  -  todos t�m a mesma dimens�o

          }
        }


          for(int p = 0; p < len; p++)
             somaAtr[p] *= vetAtr[atr].getIntClass(p,(int)classe); // peso dos vertices, no atributo desconhecido

         intervalo = maiorInd(somaAtr,atr,inst[coll-1]);   // resolve impate

         return fullVetCorrelations[auxInd].getInterval(intervalo,atr);
         //   encontrar intervalo correspondente � correla��o em attributeCorrelation

     }

    

      public double getProdSum(int i){
         return privateProdSumVector[i];
     }



     public int maiorInd(double[] A){
        int ind = 0;
        double maior = A[0];
        int cont = 0;
        for(int i = 1; i < A.length; i++)
           if(A[i] > maior){
               maior = A[i];
               ind = i;
            }
        
       return ind;
    }

    public int maiorInd(double[] A, int atr, double classe){
        int ind = 0;
        double maior = A[0];
        int cont = 0;
        for(int i = 1; i < A.length; i++)
           if(A[i] > maior){
               maior = A[i];
               ind = i;
            }

       for(int i = 0; i < A.length; i++)
          if(A[i] == maior)
             cont++;
          if(cont > 1){
           ind = vetAtr[atr].getIntFromHighestWeightofClass((int)classe);

          }

        somaPesos = maior;  // para refinig data sets
           // System.out.println("Empate!");

       return ind;
    }

    public double getProb(int i){
        return privateProbVector[i];
    }

    public double getSum(int i){
        return privateSumVector[i];
    }


    public double[] calculaIntervalos(int atr, int nroInt){  // recebe atributo ao qual sera econtrado menor valor

        double menor = E[0][atr];
        double maior = menor;
        double range, intLen;
        double[] intervals = new double[nroInt];

        for(int i = 1; i < line; i++){
           if(E[i][atr] < menor)
              menor = E[i][atr];
           if(E[i][atr] > maior)
              maior = E[i][atr];
        }

        range = maior - menor;
        intLen = range/(nroInt-1);

        for(int j = 0; j < nroInt; j++)
           intervals[j] = menor + j * intLen;

        return intervals;

        }

    public double getClasse(){
        return classe;
    }

    public AttributeCorrelation[] getVetCorrelation(){
        return fullVetCorrelations;
    }

    public  AttributeHandler[] getVetAtr(){
        return vetAtr;
    }


    public double getSomaPesos(){
        return somaPesos;
    }


    private double[][] E;
    private double[] privateProbVector;
    private double[] privateProdSumVector;
    private double[] privateSumVector;
    private double classe;
    private double somaPesos;  // usada em refinig data sets
  //  private int[] ordem;
    private int line, coll, trSize;
    private AttributeCorrelation[] fullVetCorrelations;
    private AttributeHandler[] vetAtr;
    private double emptyValue = -10000;
    private int numAttr, numEdgeFull;

    //  usado na imputa��o de instancias de teste
    private int indiceIntervalo, indiceVetCorr;

}
