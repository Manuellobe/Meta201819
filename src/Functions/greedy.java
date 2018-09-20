package Functions;
import javafx.util.Pair;

import java.util.Collections;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.Arrays;

class Sortbyvalue implements Comparator<Pair<Integer, Integer>>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Pair<Integer, Integer> a, Pair<Integer, Integer> b)
    {
        if (a.getValue() < b.getValue()) {
            return 1;
        } else if (a.getValue() > b.getValue()){
            return -1;
        } else {
            return 0;
        }
    }
}

public class greedy {

    private int dimension;
    private ArrayList<Pair<Integer, Integer>> orderedFluxValueVector;
    private boolean[] availableTerminals;
    private int[] solutionRepresentation;

    public greedy(int nDimension){

        dimension = nDimension;
        orderedFluxValueVector = new ArrayList<Pair<Integer, Integer>>();
        solutionRepresentation = new int[nDimension];
        availableTerminals = new boolean[nDimension];
        Arrays.fill(availableTerminals, Boolean.TRUE);

    }

    public int greedySolution(int[][] fluxMatrix, int[][] distMatrix){


        //Calculamos el valor de flujo de cada terminal

        int tempValue;

        for(int i = 0; i < dimension; i++){

            tempValue = 0;

            for(int j = 0; j < dimension; j++){

                tempValue += fluxMatrix[i][j];

            }

            orderedFluxValueVector.add(new Pair<Integer, Integer>(i, tempValue));

        }

        Collections.sort(orderedFluxValueVector, new Sortbyvalue());

        //Pos, Value
        int currentBestPos;
        int currentBestValue;
        int solutionValue = 0;

        for(int i = 0; i < dimension; i++){

            currentBestPos = currentBestValue = -99;

            for(int j = 0; j < dimension; j++){

                if(availableTerminals[j] && i != j){

                    if(distMatrix[i][j] == 0){
                        tempValue = fluxMatrix[i][j];
                    }
                    else
                        tempValue = fluxMatrix[i][j] / distMatrix[i][j];

                    if(tempValue > currentBestValue){
                        currentBestPos = j;
                        currentBestValue = tempValue;
                    }

                }

            }

            solutionRepresentation[i] = currentBestPos;
            availableTerminals[currentBestPos] = false;

        }

        //Calculo de costes

        for(int i = 0; i < dimension; i++){

            for(int j = 0; j < dimension; j++){

                solutionValue += fluxMatrix[i][j] * distMatrix[solutionRepresentation[i]][solutionRepresentation[j]];

            }

        }

        for(int i = 0; i < dimension; i++){
            System.out.print(solutionRepresentation[i] + " ");
        }

        System.out.println();

        return solutionValue;
    }

}
