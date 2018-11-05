package Functions;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class estacionario {

    private int dimension;
    private int randomSeed;
    private int nPopulation;
    private ArrayList<String> logger;
    private ArrayList<ArrayList<Integer>> poblacion;
    private ArrayList<Pair<Integer, Integer>> costList;
    private ArrayList<Pair<Integer, Integer>> buffer;
    private String file;

    public estacionario(int nDimension, String fileName, int arandomSeed){

        dimension = nDimension;
        randomSeed = arandomSeed;
        nPopulation = 50;

        poblacion = new ArrayList<>();
        logger = new ArrayList<>();
        file = fileName;

        costList = new ArrayList<>();
        buffer = new ArrayList<>();

    }

    public int estacionarioSolucion(int[][] fluxMatrix, int[][] distMatrix){

        ArrayList<Integer> availableTerminals;
        Random rand = new Random(randomSeed);
        ArrayList<Integer> colisions;
        int cost;
        int minCostBuffer = 0;

        for(int i = 0; i < 5; i++){

            buffer.add(new Pair<Integer, Integer>(0,0));

        }

        for(int j = 0; j < nPopulation;j++){

            availableTerminals = new ArrayList<>();

            // Generación de la solución inicial
            for (int i = 0; i < dimension; i++) {

                // Añadimos a cada posicion su valor
                availableTerminals.add(i);

            }

            // Barajamos las posiciones (Mezcla)
            Collections.shuffle(availableTerminals, rand);

            colisions = new ArrayList<>();

            for (int i = 0; i < dimension; i++) {

                if (availableTerminals.get(i) == i) {
                    colisions.add(i);
                }

            }

            if (colisions.size() > 1) {

                for (int i = 0; i < colisions.size() - 1; i++) {

                    availableTerminals.set(colisions.get(i), colisions.get(i + 1));

                }

                availableTerminals.set(colisions.get(colisions.size() - 1), colisions.get(0));

            } else if (colisions.size() == 1) {

                int randPos;
                do {
                    randPos = rand.nextInt(dimension);
                } while (randPos == colisions.get(0));

                availableTerminals.set(colisions.get(0), availableTerminals.get(randPos));
                availableTerminals.set(randPos, colisions.get(0));

            }

            poblacion.add(availableTerminals);

            cost = 0;

            // Calculamos el coste
            for (int i = 0; i < dimension; i++) {

                for (int k = 0; k < dimension; k++) {

                    if (i != k) {
                        cost += fluxMatrix[i][k] * distMatrix[availableTerminals.get(i)][availableTerminals.get(k)];
                    }

                }

            }

            costList.add(new Pair<Integer, Integer>(j, cost));

            if(cost > minCostBuffer){

                for(int i = 4; i >= 0; i--){

                    if(cost > buffer.get(i).getValue()){
                        buffer.set(i, new Pair<Integer, Integer>(i, cost));
                        minCostBuffer = buffer.get(0).getValue();
                        break;
                    }

                }

            }

        }



        return 0;
    }


}


// Poblacion = 50 individuos
// Poblacion aleatoria completamente
// Soluciones factibles
// Parada 50000 iteraciones de la funcion objetivo
// Torneo binario aleatorio