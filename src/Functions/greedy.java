package Functions;
import javafx.util.Pair;

import java.util.Collections;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.Arrays;


// Comparador para ordenar pares de enteros de mayor a menor
class SortGreaterToLesser implements Comparator<Pair<Integer, Integer>>
{

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

// Comparador para ordenar pares de enteros de menor a mayor
class SortLesserToGreater implements Comparator<Pair<Integer, Integer>>
{

    public int compare(Pair<Integer, Integer> a, Pair<Integer, Integer> b)
    {
        if (a.getValue() < b.getValue()) {
            return -1;
        } else if (a.getValue() > b.getValue()){
            return 1;
        } else {
            return 0;
        }
    }
}

public class greedy {

    // Dimension de las matrices cuadradas
    private int dimension;

    // Vectores de flujo y discancia acumulados ordenados
    private ArrayList<Pair<Integer, Integer>> orderedFluxValueVector;
    private ArrayList<Pair<Integer, Integer>> orderedDistValueVector;

    // Vector de representacion de la solucion
    private int[] solutionRepresentation;


    // Constructor
    public greedy(int nDimension){

        dimension = nDimension;
        orderedFluxValueVector = new ArrayList<Pair<Integer, Integer>>();
        orderedDistValueVector = new ArrayList<Pair<Integer, Integer>>();
        solutionRepresentation = new int[nDimension];

    }

    public int greedySolution(int[][] fluxMatrix, int[][] distMatrix){


        //Calculamos el flujo y la distancia acumulada para cada terminal

        int tempFlux, tempDist;

        for(int i = 0; i < dimension; i++){

            tempFlux = 0;
            tempDist = 0;

            // Calculamos el valor acumulado correspondiente
            for(int j = 0; j < dimension; j++){

                tempFlux += fluxMatrix[i][j];
                tempDist += distMatrix[i][j];

            }

            // Añadimos el valor acumulado correspondiente a cada terminar
            orderedFluxValueVector.add(new Pair<Integer, Integer>(i, tempFlux));
            orderedDistValueVector.add(new Pair<Integer, Integer>(i, tempDist));

        }

        // Una vez obtenemos todos los valores acumulados, estos se ordenan
        Collections.sort(orderedFluxValueVector, new SortGreaterToLesser());
        Collections.sort(orderedDistValueVector, new SortLesserToGreater());



        // Asignamos al vector solucion la primera solucion
        int currentTerminal;

        int solutionValue = 0;

        for(int i = 0; i < dimension; i++){

            currentTerminal = orderedFluxValueVector.get(i).getKey();

            solutionRepresentation[currentTerminal] = orderedDistValueVector.get(i).getKey();

        }

        // Comprobamos que ningun terminal sea emparejado con si mismo

        int tempSwap;

        for(int i = 0; i < dimension; i++){

            // En caso de que un terminal sea conectado a si mismo, lo intercambiamos con el anterior
            if(solutionRepresentation[i] == i){

                tempSwap = solutionRepresentation[i];

                // En caso de que ocurra en la posicion inicial, se intercambia con la posicion posterior
                if(i == 0){

                    solutionRepresentation[i] = solutionRepresentation[i+1];
                    solutionRepresentation[i+1] = tempSwap;

                }else{

                    solutionRepresentation[i] = solutionRepresentation[i-1];
                    solutionRepresentation[i-1] = tempSwap;
                    i--;

                }

            }

        }

        //Calculo de costes

        for(int i = 0; i < dimension; i++){

            for(int j = 0; j < dimension; j++){

                solutionValue += fluxMatrix[i][j] * distMatrix[solutionRepresentation[i]][solutionRepresentation[j]];

            }

        }

        // Comprobacion por consola de que ningun terminal ha sido emparejado con sigo mismo || Borrar posteriormente
        for(int i = 0; i < dimension; i++){
            System.out.printf("%d | %d  \n", i, solutionRepresentation[i]);
        }

        System.out.println();

        return solutionValue;
    }

}
