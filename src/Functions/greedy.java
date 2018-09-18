package Functions;
import javafx.util.Pair;

import java.util.Arrays;

public class greedy {

    private int dimension;
    private boolean[] availableTerminals;
    private int[] solutionRepresentation;

    public greedy(int nDimension){

        dimension = nDimension;
        solutionRepresentation = new int[nDimension];
        availableTerminals = new boolean[nDimension];
        Arrays.fill(availableTerminals, Boolean.TRUE);

    }

    public int greedySolution(int[][] fluxMatrix, int[][] distMatrix){

        //Pos, Value
        int currentBestPos;
        int currentBestValue;
        int solutionValue = 0;

        for(int i = 0; i < dimension; i++){

            currentBestPos = currentBestValue = -99;

            for(int j = 0; j < dimension; j++){

                if(availableTerminals[j] &&(fluxMatrix[i][j] - distMatrix[i][j]) > currentBestValue){

                    currentBestPos = j;
                    currentBestValue = fluxMatrix[i][j] - distMatrix[i][j];

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

        return solutionValue;
    }

}
